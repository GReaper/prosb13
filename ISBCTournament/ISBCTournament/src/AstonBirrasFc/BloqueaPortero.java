package AstonBirrasFc;

import EDU.gatech.cc.is.util.Vec2;
import teams.ucmTeam.Behaviour;
import teams.ucmTeam.RobotAPI;

public class BloqueaPortero extends Behaviour
{
	private Ayudas helper;
	private enum States {AvoidFrBlock, AvoidOppBlock, AvoidOpp, ToGoal, Blocking};
	private States state;
	
	public void configure() 
	{ 
		helper = new Ayudas();
		state = States.ToGoal;
	}

	public int takeStep() 
	{	
		// Evaluate environment
		state = evaluateEnv();
		
		// Take closest opponent
		Vec2 closestOp = myRobotAPI.getClosestOpponent();
		
		// Make movement based on state
		switch (state) 
		{
			case AvoidFrBlock:	
				// Avoid block
				helper.evitarBloqueo(myRobotAPI.getClosestMate(), myRobotAPI);	
				// Set displayed text
				myRobotAPI.setDisplayString("BloqPort. (AF)");		
				break;
				
			case AvoidOppBlock:	
				// Avoid block
				helper.evitarBloqueo(closestOp, myRobotAPI);	
				// Set displayed text
				myRobotAPI.setDisplayString("BloqPort. (AO2)");							
				break;
				
			case AvoidOpp:	
				// Avoid collision
				helper.evitaColision(closestOp, myRobotAPI);
				// Set displayed text
				myRobotAPI.setDisplayString("BloqPort. (AO1)");							
				break;
				
			case ToGoal:	
				// 1.- Set steering
				Vec2 dest = myRobotAPI.toFieldCoordinates(myRobotAPI.getOpponentsGoal());
				double angle = helper.anguloDestino(dest, myRobotAPI);
				myRobotAPI.setSteerHeading(angle);
				// 2.- Increase speed
				myRobotAPI.setSpeed(1000);				
				// Set displayed text
				myRobotAPI.setDisplayString("BloqPort. (GTG)");			
				break;
				
			case Blocking:	
				// Move towards opponents goalie
				myRobotAPI.blockGoalKeeper();				
				// Set displayed text
				myRobotAPI.setDisplayString("BloqPort. (B)");			
				break;
				
			default:
				// Move towards opponents goalie
				myRobotAPI.blockGoalKeeper();				
				// Set displayed text
				myRobotAPI.setDisplayString("BloqPort. (B)");
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
		// Avoid collisions only if the opponents goal is far enough
		if (!helper.cercanoRadio(myRobotAPI.getPosition(), helper.porteroEnemigo(myRobotAPI), myRobotAPI.getPlayerRadius()*3))
		{
			// Take closest opponent
			Vec2 closestOp = myRobotAPI.getClosestOpponent();
			
			// Check whether the player is blocked by the closest teammate
			if (myRobotAPI.isBlocking(myRobotAPI.getClosestMate()))
			{
				// Return
				return States.AvoidFrBlock;
			}

			// Check for opponent blocking
			if (myRobotAPI.opponentBlocking())
			{
				// Return
				return States.AvoidOppBlock;				
			}
			
			// Check whether that opponent is too close
			if (helper.cercanoRadio(myRobotAPI.getPosition(),  myRobotAPI.toFieldCoordinates(closestOp), myRobotAPI.getPlayerRadius()*2))
			{
				// Return
				return States.AvoidOpp;
			}
			
			// Move towards enemy goal
			return States.ToGoal;
		}
		
		// Move towards opponents goalie
		return States.Blocking;
	}
}
