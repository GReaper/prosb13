package AstonBirrasFc;

import EDU.gatech.cc.is.util.Vec2;
import teams.ucmTeam.Behaviour;
import teams.ucmTeam.RobotAPI;

public class MedioCentroDefensivo extends Behaviour
{
	private Ayudas helper;
	private enum States {AvoidFrBlock, MoveMiddle, MoveThird, FollowBall, Kicking, GoToBall};
	private States state;
	
	public void configure() 
	{ 
		helper = new Ayudas();
		state = States.MoveThird;
	}

	public int takeStep() 
	{		
		// Decide the side to kick the ball in case it's needed
		Vec2 dest = myRobotAPI.getOpponentsGoal();
		// Decide where to kick the ball
		if (myRobotAPI.getPosition().y > 0)
		{
			// Kick to the upper middle side
			dest = new Vec2(0, myRobotAPI.getUpperFieldBound());
		}
		else
		{
			// Kick to the lower middle side
			dest = new Vec2(0, myRobotAPI.getLowerFieldBound());				
		}
		
		// Evaluate environment
		state = evaluateEnv();
		
		// Make movements
		switch (state) 
		{
			case AvoidFrBlock:
				// Avoid block
				helper.evitarBloqueo(myRobotAPI.getClosestMate(), myRobotAPI);				
				// Set displayed text
				myRobotAPI.setDisplayString("MCD (AF)");								
				break;

			case MoveMiddle:
				// 1.- Set steering
				dest = myRobotAPI.getOurGoal();
				double angle = helper.anguloDestino(dest, myRobotAPI);			
				myRobotAPI.setSteerHeading(angle);					
				// 2.- Increase speed
				myRobotAPI.setSpeed(1000);
				// Set displayed text
				myRobotAPI.setDisplayString("MCD (RM)");								
				break;

			case MoveThird:	
				// 1.- Set steering
				dest = myRobotAPI.getOurGoal();
				double angle2 = helper.anguloDestino(dest, myRobotAPI);			
				myRobotAPI.setSteerHeading(angle2);					
				// 2.- Increase speed
				myRobotAPI.setSpeed(1000);
				// Set displayed text
				myRobotAPI.setDisplayString("MCD (RT)");							
				break;

			case FollowBall:	
				// 1.- Set steering
				dest = myRobotAPI.toFieldCoordinates(myRobotAPI.getBall());
				double angle3 = helper.anguloDestino(dest, myRobotAPI);			
				myRobotAPI.setSteerHeading(angle3);					
				// 2.- Increase speed
				myRobotAPI.setSpeed(0.2);
				// Set displayed text
				myRobotAPI.setDisplayString("MCD (FB2)");							
				break;

			case Kicking:					
				myRobotAPI.setBehindBall(myRobotAPI.toEgocentricalCoordinates(dest));
				myRobotAPI.kick();					
				// Set displayed text
				myRobotAPI.setDisplayString("MCD (K)");		
				break;

			case GoToBall:		
				// Go to ball to follow it
				myRobotAPI.setSpeed(1000);				
				myRobotAPI.setBehindBall(myRobotAPI.toEgocentricalCoordinates(dest));
				// Set displayed text
				myRobotAPI.setDisplayString("MCD (FB)");				
				break;
	
			default:	
				// 1.- Set steering
				dest = myRobotAPI.getOurGoal();
				double angle4 = helper.anguloDestino(dest, myRobotAPI);			
				myRobotAPI.setSteerHeading(angle4);					
				// 2.- Increase speed
				myRobotAPI.setSpeed(1000);
				// Set displayed text
				myRobotAPI.setDisplayString("MCD (RT)");
				break;
		}
		
		return myRobotAPI.ROBOT_OK;
	}

	public void onInit(RobotAPI r) 
	{
	}

	public void onRelease(RobotAPI r) { }

	public void end() { }
	
	private States evaluateEnv()
	{
		// Check whether the player is blocked by the closest teammate
		if (myRobotAPI.isBlocking(myRobotAPI.getClosestMate()))
		{
			// Return
			return States.AvoidFrBlock;
		}
		
		// If the ball is in our field the MCD can move until the middle section,
		// else he may only remain in the first third section
		if (myRobotAPI.toFieldCoordinates(myRobotAPI.getBall()).x * myRobotAPI.getFieldSide() >= 0)
		{
			// Check if the player is in the first middle of the field
			if (!(myRobotAPI.getPosition().x * myRobotAPI.getFieldSide() >= 0))
			{
				// Return
				return States.MoveMiddle;
			}
		}
		else
		{
			Double thirdSize = (myRobotAPI.getRightFieldBound() - myRobotAPI.getLeftFieldBound()) / 3;
			// Check if the player is in the first third of the field
			if (!(myRobotAPI.getPosition().x * myRobotAPI.getFieldSide() >= thirdSize))
			{
				// Return
				return States.MoveThird;
			}
			else
			{
				// Move following the ball
				// Return
				return States.FollowBall;
			}
		}
		
		// Check if the player can kick the ball
		if (myRobotAPI.canKick())
		{
			return States.Kicking;
		}
		
		// Go to ball to follow it
		return States.GoToBall;		
	}
}
