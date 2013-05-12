package AstonBirrasFc;

import EDU.gatech.cc.is.util.Vec2;
import teams.ucmTeam.Behaviour;
import teams.ucmTeam.RobotAPI;

public class MedioCentroDefensivo extends Behaviour
{
	private Ayudas helper;
	
	public void configure() 
	{ 
		helper = new Ayudas();
	}

	public int takeStep() 
	{		
		// Check whether the player is blocked by the closest teammate
		if (myRobotAPI.isBlocking(myRobotAPI.getClosestMate()))
		{
			// 1.- Set steering
			Vec2 dest = myRobotAPI.toFieldCoordinates(myRobotAPI.getClosestMate());
			double angleAux = helper.anguloNecesario(dest, myRobotAPI, 0.01);
			double angle = helper.degToRad(helper.radToDeg(angleAux) + 180);
			myRobotAPI.setSteerHeading(angle);	

			// 2.- Increase speed
			myRobotAPI.setSpeed(1000);
			
			// Set displayed text
			myRobotAPI.setDisplayString("MCD (AF)");
			
			// Return
			return myRobotAPI.ROBOT_OK;
		}
		
		// If the ball is in our field the MCD can move until the midle section,
		// else he may only remain in the first third section
		if (myRobotAPI.toFieldCoordinates(myRobotAPI.getBall()).x * myRobotAPI.getFieldSide() >= 0)
		{
			// Check if the player is in the first middle of the field
			if (!(myRobotAPI.getPosition().x * myRobotAPI.getFieldSide() >= 0))
			{
				// 1.- Set steering
				Vec2 dest = myRobotAPI.getOurGoal();
				double angle = helper.anguloNecesario(dest, myRobotAPI, 0.01);			
				myRobotAPI.setSteerHeading(angle);	
				
				// 2.- Increase speed
				myRobotAPI.setSpeed(1000);

				// Set displayed text
				myRobotAPI.setDisplayString("MCD (RM)");
				
				// Return
				return myRobotAPI.ROBOT_OK;
			}
		}
		else
		{
			Double thirdSize = (myRobotAPI.getRightFieldBound() - myRobotAPI.getLeftFieldBound()) / 3;
			// Check if the player is in the first third of the field
			if (!(myRobotAPI.getPosition().x * myRobotAPI.getFieldSide() >= thirdSize))
			{
				// 1.- Set steering
				Vec2 dest = myRobotAPI.getOurGoal();
				double angle = helper.anguloNecesario(dest, myRobotAPI, 0.01);			
				myRobotAPI.setSteerHeading(angle);	
				
				// 2.- Increase speed
				myRobotAPI.setSpeed(1000);

				// Set displayed text
				myRobotAPI.setDisplayString("MCD (RT)");
				
				// Return
				return myRobotAPI.ROBOT_OK;
			}
		}
		
		// Check if the player can kick the ball
		if (myRobotAPI.canKick())
		{
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
			double angle = helper.anguloNecesario(dest, myRobotAPI, 0.01);			
			myRobotAPI.setSteerHeading(angle);
			myRobotAPI.kick();
			
			// Set displayed text
			myRobotAPI.setDisplayString("MCD (K)");
		}
		
		// Nothing to do. Set speed to 0
		//myRobotAPI.setSpeed(0);
		//myRobotAPI.setDisplayString("MCD (W)");
		
		// Go to ball to follow it
		myRobotAPI.setSpeed(1000);
		myRobotAPI.setBehindBall(myRobotAPI.getOpponentsGoal());
		// Set displayed text
		myRobotAPI.setDisplayString("MCD (FB)");
		
		return myRobotAPI.ROBOT_OK;
	}

	public void onInit(RobotAPI r) 
	{
	}

	public void onRelease(RobotAPI r) { }

	public void end() { }
}
