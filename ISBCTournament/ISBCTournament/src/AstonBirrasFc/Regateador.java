package AstonBirrasFc;

import java.util.Random;

import EDU.gatech.cc.is.util.Vec2;
import teams.ucmTeam.Behaviour;
import teams.ucmTeam.RobotAPI;

public class Regateador extends Behaviour
{
	private Ayudas helper;
	
	public void configure() 
	{ 
		helper = new Ayudas();
	}

	public int takeStep() 
	{	
		// Get height size
		double hg = myRobotAPI.getUpperFieldBound() - myRobotAPI.getLowerFieldBound();
		
		// Take closest opponent
		Vec2 closestOp = myRobotAPI.getClosestOpponent();
		
		// Check if the ball is not close enough
		if (!helper.cercano(myRobotAPI.getPosition(), myRobotAPI.toFieldCoordinates(myRobotAPI.getBall()), myRobotAPI, myRobotAPI.getPlayerRadius()*1.5))
		{
			myRobotAPI.setSpeed(1000);
			myRobotAPI.avoidCollisions();
			myRobotAPI.
		}
		
		// Check whether that opponent is too close or if the player is blocked
		if (helper.cercano(myRobotAPI.getPosition(),  myRobotAPI.toFieldCoordinates(closestOp), myRobotAPI, myRobotAPI.getPlayerRadius()*2)
				|| myRobotAPI.isBlocking(closestOp))
		{
			Vec2 heading = myRobotAPI.getOpponentsGoal();
			// 1.- Set heading	
			if (myRobotAPI.getPosition().y >= 0)
			{
				// Go to the lower side
				if (closestOp.y >= myRobotAPI.getPosition().y)
				{
					heading = new Vec2(heading.x, myRobotAPI.getPosition().y-(hg*0.1));
				}
				else
				{
					heading = new Vec2(heading.x, myRobotAPI.getPosition().y+(hg*0.1));
				}
			}
			else
			{
				// Go to the upper side
				if (closestOp.y <= myRobotAPI.getPosition().y)
				{
					heading = new Vec2(heading.x, myRobotAPI.getPosition().y+(hg*0.1));
				}
				else
				{
					heading = new Vec2(heading.x, myRobotAPI.getPosition().y-(hg*0.1));
				}
			}
			myRobotAPI.setBehindBall(myRobotAPI.toEgocentricalCoordinates(heading));
			
			// 2.- Increase speed
			myRobotAPI.setSpeed(0.3);
			
			// Set displayed text
			myRobotAPI.setDisplayString("Reg. (AO)");
			
			// Return
			return myRobotAPI.ROBOT_OK;
		}
		
		// Push the ball towards the opponent's goal
		myRobotAPI.setSpeed(0.3);
		myRobotAPI.setBehindBall(myRobotAPI.getOpponentsGoal());
		myRobotAPI.setDisplayString("Reg. (ToGoal)");
		
		// Check whether the player is blocked by the closest teammate
		/*if (myRobotAPI.isBlocking(myRobotAPI.getClosestMate()))
		{
			// 1.- Set steering
			Vec2 dest = myRobotAPI.toFieldCoordinates(myRobotAPI.getClosestMate());
			double angleAux = helper.anguloNecesario(dest, myRobotAPI, 0.01);
			double angle = helper.degToRad(helper.radToDeg(angleAux) + 180);
			myRobotAPI.setSteerHeading(angle);	

			// 2.- Increase speed
			myRobotAPI.setSpeed(1000);
			
			// Set displayed text
			myRobotAPI.setDisplayString("Desm. (AF)");
			
			// Return
			return myRobotAPI.ROBOT_OK;
		}*/
		
		 
		return myRobotAPI.ROBOT_OK;
	}

	public void onInit(RobotAPI r) 
	{
	}

	public void onRelease(RobotAPI r) { }

	public void end() { }
}
