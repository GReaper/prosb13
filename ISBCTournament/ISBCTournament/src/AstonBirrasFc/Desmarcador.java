package AstonBirrasFc;

import java.util.Random;

import EDU.gatech.cc.is.util.Vec2;
import teams.ucmTeam.Behaviour;
import teams.ucmTeam.RobotAPI;

public class Desmarcador extends Behaviour
{
	private Ayudas helper;
	
	public void configure() 
	{ 
		helper = new Ayudas();
	}

	public int takeStep() 
	{		
		// Take closes opponent
		Vec2 closestOp = myRobotAPI.getClosestOpponent();
		
		// If the player isn't in the opponents field, move him
		Double quarterSize = (myRobotAPI.getRightFieldBound() - myRobotAPI.getLeftFieldBound()) / 4;
		if (myRobotAPI.getPosition().x * myRobotAPI.getFieldSide() >= -quarterSize)
		{
			// Check whether that opponent is too close
			if (helper.cercano(myRobotAPI.getPosition(),  myRobotAPI.toFieldCoordinates(closestOp), myRobotAPI, myRobotAPI.getPlayerRadius()*2))
			{
				// Try to avoid collision
				myRobotAPI.avoidCollisions();
				// Set displayed text
				myRobotAPI.setDisplayString("Desm. (A)");
				// Return
				return myRobotAPI.ROBOT_OK;
			}
			
			// 1.- Set steering. We make a random decision to go to the left
			// or right corner
			Vec2 dest = myRobotAPI.getOpponentsGoal();
			// 2.- Decide the corner to go to. Take a random int that can be 0 or 1.
			// With 1 we will go to the left corner (and viceversa)
			Random r = new Random();
			if (r.nextInt(2) == 1)
			{
				// Go to left corner
				dest = new Vec2(myRobotAPI.getOpponentsGoal().x, myRobotAPI.getLowerFieldBound());
			}
			else
			{
				// Go to right corner
				dest = new Vec2(myRobotAPI.getOpponentsGoal().x, myRobotAPI.getUpperFieldBound());				
			}
			double angle = helper.irAPosicion(dest, myRobotAPI, 0.01);
			
			myRobotAPI.setSteerHeading(angle);	
			// 2.- Increase speed
			myRobotAPI.setSpeed(1000);

			// Set displayed text
			myRobotAPI.setDisplayString("Desm. (M)");
			
			// Return
			return myRobotAPI.ROBOT_OK;
		}
		
		// Check whether that opponent is too close
		if (helper.cercano(myRobotAPI.getPosition(), myRobotAPI.toFieldCoordinates(closestOp), myRobotAPI, myRobotAPI.getPlayerRadius()*3))
		{
			// Nearly opponent, move to another position
			
			// 1.- Set steering
			/*Vec2 dest = myRobotAPI.getOpponentsGoal();
			if (Math.abs(myRobotAPI.getPosition().y - myRobotAPI.getUpperFieldBound()) >
				Math.abs(myRobotAPI.getPosition().y - myRobotAPI.getLowerFieldBound()))
			{
				// Go to upper side
				Double yVal = myRobotAPI.getUpperFieldBound() - ((myRobotAPI.getUpperFieldBound()-myRobotAPI.getLowerFieldBound())/4);
				dest = new Vec2(myRobotAPI.getPosition().x, yVal);
			}
			else
			{
				// Go to lower side
				Double yVal = myRobotAPI.getLowerFieldBound() + ((myRobotAPI.getUpperFieldBound()-myRobotAPI.getLowerFieldBound())/4);
				dest = new Vec2(myRobotAPI.getPosition().x, yVal);				
			}
			double angle = helper.irAPosicion(dest, myRobotAPI, 0.01);
			myRobotAPI.setSteerHeading(angle);	*/

			myRobotAPI.avoidCollisions();
			// 2.- Increase speed
			myRobotAPI.setSpeed(1000);

			// Set displayed text
			myRobotAPI.setDisplayString("Desm. (D)");
		}
		else
		{
			// No nearly opponents, don't move
			
			// 1.- Reduce speed
			myRobotAPI.setSpeed(0);
			
			// Set displayed text
			myRobotAPI.setDisplayString("Desm. (S)");
		}
		 
		return myRobotAPI.ROBOT_OK;
	}

	public void onInit(RobotAPI r) 
	{
	}

	public void onRelease(RobotAPI r) { }

	public void end() { }
}
