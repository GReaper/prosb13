package AstonBirrasFc;

import java.util.Random;

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
			double angleAux = helper.irAPosicion(dest, myRobotAPI, 0.01);
			double angle = helper.degToRad(helper.radToDeg(angleAux) + 180);
			myRobotAPI.setSteerHeading(angle);	

			// 2.- Increase speed
			myRobotAPI.setSpeed(1000);
			
			// Set displayed text
			myRobotAPI.setDisplayString("Desm. (AF)");
			
			// Return
			return myRobotAPI.ROBOT_OK;
		}
		
		 
		return myRobotAPI.ROBOT_OK;
	}

	public void onInit(RobotAPI r) 
	{
	}

	public void onRelease(RobotAPI r) { }

	public void end() { }
}
