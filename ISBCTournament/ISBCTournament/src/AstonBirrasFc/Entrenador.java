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
		/* Vamos ganando o empatados */
		/*if (RobotAPI.getMyScore() >= RobotAPI.getOpponentsScore())*/
		
		/* Vamos perdiendo */
		_players[0].setBehaviour(_behaviours[7]);
		_players[1].setBehaviour(_behaviours[5]);
		_players[2].setBehaviour(_behaviours[8]);
		_players[3].setBehaviour(_behaviours[4]);
		_players[4].setBehaviour(_behaviours[6]);
		
	}
	
	public Behaviour getDefaultBehaviour(int id) 
	{
		return _behaviours[1];
	}
	
	public Behaviour[] createBehaviours() 
	{
		return new Behaviour[] {new GoToBall(), 
								new QuietoParao(),
								new MedioCentroAtacante(),
								new Rebote(),
								new Desmarcador(),
								new MedioCentroDefensivo(),
								new Regateador(),
								new Portero(),
								new BloqueaPortero()};
		
	}
}
