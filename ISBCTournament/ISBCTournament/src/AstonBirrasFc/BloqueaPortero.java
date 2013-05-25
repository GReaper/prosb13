package AstonBirrasFc;

import EDU.gatech.cc.is.util.Vec2;
import teams.ucmTeam.Behaviour;
import teams.ucmTeam.RobotAPI;

public class BloqueaPortero extends Behaviour
{
	private Ayudas helper;
	
	public void configure() 
	{ 
		helper = new Ayudas();
	}

	public int takeStep() 
	{	
		// Avoid collisions only if the opponents goal is far enough
		if (!helper.cercanoRadio(myRobotAPI.getPosition(), helper.porteroEnemigo(myRobotAPI), myRobotAPI.getPlayerRadius()*3))
		{
			// Take closest opponent
			Vec2 closestOp = myRobotAPI.getClosestOpponent();
			
			// Check whether the player is blocked by the closest teammate
			if (myRobotAPI.isBlocking(myRobotAPI.getClosestMate()))
			{
				helper.evitarBloqueo(myRobotAPI.getClosestMate(), myRobotAPI);
	
				// Set displayed text
				myRobotAPI.setDisplayString("BloqPort. (AF)");

				// Return
				return myRobotAPI.ROBOT_OK;
			}

			// Check for opponent blocking
			if (myRobotAPI.opponentBlocking())
			{
				helper.evitarBloqueo(closestOp, myRobotAPI);
	
				// Set displayed text
				myRobotAPI.setDisplayString("BloqPort. (AO2)");
				
				// Return
				return myRobotAPI.ROBOT_OK;				
			}
			
			// Check whether that opponent is too close
			if (helper.cercanoRadio(myRobotAPI.getPosition(),  myRobotAPI.toFieldCoordinates(closestOp), myRobotAPI.getPlayerRadius()*2))
			{
				helper.evitaColision(closestOp, myRobotAPI);

				// Set displayed text
				myRobotAPI.setDisplayString("BloqPort. (AO1)");
				
				// Return
				return myRobotAPI.ROBOT_OK;
			}
			
			// 1.- Set steering
			Vec2 dest = myRobotAPI.toFieldCoordinates(myRobotAPI.getOpponentsGoal());//helper.porteroEnemigo(myRobotAPI);
			double angle = helper.anguloDestino(dest, myRobotAPI);
			myRobotAPI.setSteerHeading(angle);	

			// 2.- Increase speed
			myRobotAPI.setSpeed(1000);
			
			// Set displayed text
			myRobotAPI.setDisplayString("BloqPort. (GTG)");
			 
			return myRobotAPI.ROBOT_OK;
		}
		
		// Move towards opponents goalie
		myRobotAPI.blockGoalKeeper();
		
		// Set displayed text
		myRobotAPI.setDisplayString("BloqPort. (B)");
		 
		return myRobotAPI.ROBOT_OK;
	}

	public void onInit(RobotAPI r) 
	{
	}

	public void onRelease(RobotAPI r) { }

	public void end() { }
}
