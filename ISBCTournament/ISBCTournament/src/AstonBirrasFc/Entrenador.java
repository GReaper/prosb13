package AstonBirrasFc;

import teams.ucmTeam.Behaviour;
import teams.ucmTeam.RobotAPI;
import teams.ucmTeam.TeamManager;

public class Entrenador extends TeamManager 
{
	
	
	
	public int onConfigure() 
	{
		
		
		return RobotAPI.ROBOT_OK;
	}
	
	public void onTakeStep() 
	{
		_players[0].setBehaviour(_behaviours[2]);
		//_players[1].setBehaviour(_behaviours[0]);
		//_players[2].setBehaviour(_behaviours[2]);
		_players[3].setBehaviour(_behaviours[4]);
		_players[4].setBehaviour(_behaviours[3]);
		
	}
	
	public Behaviour getDefaultBehaviour(int id) 
	{
		return _behaviours[0];
	}
	
	public Behaviour[] createBehaviours() 
	{
		return new Behaviour[] {new GoToBall(), 
								new QuietoParao(),
								new LocoBola(),
								new Rebote(),
								new Desmarcador()};
	}
}
