package AstonBirrasFc;

import teams.ucmTeam.Behaviour;
import teams.ucmTeam.RobotAPI;

public class QuietoParao extends Behaviour
{

	@Override
	public void configure() {}

	@Override
	public void end() {}

	@Override
	public void onInit(RobotAPI r) 
	{
		r.setDisplayString("quieto Parao"); 
		
	}

	@Override
	public void onRelease(RobotAPI r) {}

	@Override
	public int takeStep()
	{
		
		myRobotAPI.setSpeed(0);
		return 0;
	}
	
	
}
