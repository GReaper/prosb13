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
		// Take closest opponent
		Vec2 closestOp = myRobotAPI.getClosestOpponent();
		
		// Check whether the player is blocked by the closest teammate
		if (myRobotAPI.isBlocking(myRobotAPI.getClosestMate()))
		{
			// 1.- Set steering
			/*Vec2 dest = myRobotAPI.toFieldCoordinates(myRobotAPI.getClosestMate());
			double angleAux = helper.anguloDestino(dest, myRobotAPI);
			double angle = helper.degToRad(helper.radToDeg(angleAux) + 160);
			myRobotAPI.setSteerHeading(angle);	*/
			helper.evitarBloqueo(myRobotAPI.getClosestMate(), myRobotAPI);

			// 2.- Increase speed
			//myRobotAPI.setSpeed(1000);
			
			// Set displayed text
			myRobotAPI.setDisplayString("Desm. (AF)");
			
			// Return
			return myRobotAPI.ROBOT_OK;
		}
		
		// If the player isn't in the opponents field, move him
		Double quarterSize = (myRobotAPI.getRightFieldBound() - myRobotAPI.getLeftFieldBound()) / 4;
		if (myRobotAPI.getPosition().x * myRobotAPI.getFieldSide() >= -quarterSize)
		{			
			// Check whether that opponent is blocking
			if (myRobotAPI.isBlocking(closestOp))
			{
				// 1.- Set steering
				/*Vec2 dest = myRobotAPI.toFieldCoordinates(closestOp);
				double angleAux = helper.anguloDestino(dest, myRobotAPI);
				double angle = helper.degToRad(helper.radToDeg(angleAux) + 160);
				myRobotAPI.setSteerHeading(angle);	*/
				helper.evitarBloqueo(closestOp, myRobotAPI);

				// 2.- Increase speed
				//myRobotAPI.setSpeed(1000);
				
				// Set displayed text
				myRobotAPI.setDisplayString("Desm. (AO)");
				
				// Return
				return myRobotAPI.ROBOT_OK;
			}
			
			// Check whether that opponent is too close
			if (helper.cercanoRadio(myRobotAPI.getPosition(),  myRobotAPI.toFieldCoordinates(closestOp), myRobotAPI.getPlayerRadius()*2))
			{
				// 1.- Set steering
				/*Vec2 dest = myRobotAPI.toFieldCoordinates(closestOp);
				double angleAux = helper.anguloDestino(dest, myRobotAPI);
				double angle = helper.degToRad(helper.radToDeg(angleAux) + 160);
				myRobotAPI.setSteerHeading(angle);	*/
				helper.evitaColision(closestOp, myRobotAPI);

				// 2.- Increase speed
				//myRobotAPI.setSpeed(1000);
				
				// Set displayed text
				myRobotAPI.setDisplayString("Desm. (AO)");
				
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
			double angle = helper.anguloDestino(dest, myRobotAPI);
			
			myRobotAPI.setSteerHeading(angle);	
			
			// 2.- Increase speed
			myRobotAPI.setSpeed(1000);

			// Set displayed text
			myRobotAPI.setDisplayString("Desm. (M)");
			
			// Return
			return myRobotAPI.ROBOT_OK;
		}
		
		// Check whether that opponent is too close
		if (helper.cercanoRadio(myRobotAPI.getPosition(), myRobotAPI.toFieldCoordinates(closestOp), myRobotAPI.getPlayerRadius()*2))
		{
			// Nearly opponent, move to another position
			
			// 1.- Set steering
			/*Vec2 dest = myRobotAPI.toFieldCoordinates(closestOp);
			double angleAux = helper.anguloDestino(dest, myRobotAPI);
			double angle = helper.degToRad(helper.radToDeg(angleAux) + 180);
			myRobotAPI.setSteerHeading(angle);	*/
			helper.evitaColision(closestOp, myRobotAPI);

			// 2.- Increase speed
			//myRobotAPI.setSpeed(1000);

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
		
		// Check if the player is the closest to ball and there aren't mates closer
		if (myRobotAPI.closestToBall() && !helper.cercanoRadio(myRobotAPI.toFieldCoordinates(myRobotAPI.getClosestMate()), myRobotAPI.toFieldCoordinates(myRobotAPI.getBall()), myRobotAPI.getPlayerRadius()*2.5))
		{
			// Head to opponent's goal
			myRobotAPI.setBehindBall(myRobotAPI.getOpponentsGoal());
			// Increase speed
			myRobotAPI.setSpeed(0.7);
			// Set displayed text
			myRobotAPI.setDisplayString("Desm. (GTB)");
		}
		
		// Try to kick
		if (myRobotAPI.alignedToBallandGoal() && myRobotAPI.canKick())
		{
			myRobotAPI.kick();
		}
		
		return myRobotAPI.ROBOT_OK;
	}

	public void onInit(RobotAPI r) 
	{
	}

	public void onRelease(RobotAPI r) { }

	public void end() { }
}
