package AstonBirrasFc;

import EDU.gatech.cc.is.util.Vec2;
import teams.ucmTeam.Behaviour;
import teams.ucmTeam.RobotAPI;

public class GoToBall extends Behaviour
{

public void configure() { }

public int takeStep() 
{
 myRobotAPI.setBehindBall(myRobotAPI.getOpponentsGoal());
 if (myRobotAPI.canKick())
	 myRobotAPI.kick();
 
 
 return myRobotAPI.ROBOT_OK;
}

public void onInit(RobotAPI r) 
{
	r.setDisplayString("goToBallBehaviour"+ r.getID()); 
}

public void onRelease(RobotAPI r) { }

public void end() { }

}