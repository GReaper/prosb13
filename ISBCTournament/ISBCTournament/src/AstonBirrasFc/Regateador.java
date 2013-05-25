package AstonBirrasFc;

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
		if (!helper.cercanoRadio(myRobotAPI.getPosition(), myRobotAPI.toFieldCoordinates(myRobotAPI.getBall()), myRobotAPI.getPlayerRadius()*3))
		{
			// Check whether the player is blocked by the closest teammate and avoid him
			if (myRobotAPI.isBlocking(myRobotAPI.getClosestMate()))
			{
				helper.evitarBloqueo(myRobotAPI.getClosestMate(), myRobotAPI);
				
				// Set displayed text
				myRobotAPI.setDisplayString("Reg. (AF)");
				
				// Return
				return myRobotAPI.ROBOT_OK;
			}
			
			// Check whether that opponent is too close or if the player is blocked
			if (helper.cercanoRadio(myRobotAPI.getPosition(),  myRobotAPI.toFieldCoordinates(closestOp), myRobotAPI.getPlayerRadius()*2)
					|| myRobotAPI.isBlocking(closestOp))
			{
				helper.evitaColision(closestOp, myRobotAPI);
				
				// Set displayed text
				myRobotAPI.setDisplayString("Reg. (AO)");
				
				// Return
				return myRobotAPI.ROBOT_OK;
			}
			
			// Set max speed
			myRobotAPI.setSpeed(0.8);
			
			// Go to ball
			myRobotAPI.setBehindBall(myRobotAPI.getOpponentsGoal());
			
			// Set displayed text
			myRobotAPI.setDisplayString("Reg. (GTB)");
			
			// Return
			return myRobotAPI.ROBOT_OK;
		}
		
		// Check whether that opponent is too close or if the player is blocked
		if (helper.cercanoRadio(myRobotAPI.getPosition(),  myRobotAPI.toFieldCoordinates(closestOp), myRobotAPI.getPlayerRadius()*2)
				|| myRobotAPI.isBlocking(closestOp))
		{
			Vec2 heading = myRobotAPI.getOpponentsGoal();
			// 1.- Set heading	
			if (myRobotAPI.getPosition().y >= 0)
			{
				// Go to the lower side
				if (closestOp.y >= myRobotAPI.getPosition().y)
				{
					heading = new Vec2(heading.x, myRobotAPI.getLowerFieldBound()/*myRobotAPI.getPosition().y-(hg*0.1)*/);
				}
				else
				{
					heading = new Vec2(heading.x, myRobotAPI.getUpperFieldBound()/*myRobotAPI.getPosition().y+(hg*0.1)*/);
				}
			}
			else
			{
				// Go to the upper side
				if (closestOp.y <= myRobotAPI.getPosition().y)
				{
					heading = new Vec2(heading.x, myRobotAPI.getUpperFieldBound()/*myRobotAPI.getPosition().y+(hg*0.1)*/);
				}
				else
				{
					heading = new Vec2(heading.x, myRobotAPI.getLowerFieldBound()/*myRobotAPI.getPosition().y-(hg*0.1)*/);
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
		
		// Try to kick
		if (myRobotAPI.alignedToBallandGoal() && myRobotAPI.canKick())
		{
			myRobotAPI.kick();
		}
		
		// Push the ball towards the opponent's goal
		myRobotAPI.setSpeed(0.4);
		myRobotAPI.setBehindBall(myRobotAPI.getOpponentsGoal());
		myRobotAPI.setDisplayString("Reg. (ToGoal)");		
		 
		return myRobotAPI.ROBOT_OK;
	}

	public void onInit(RobotAPI r) 
	{
	}

	public void onRelease(RobotAPI r) { }

	public void end() { }
}
