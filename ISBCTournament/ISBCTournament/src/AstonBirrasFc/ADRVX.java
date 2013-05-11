package AstonBirrasFc;

import teams.ucmTeam.TeamManager;
import teams.ucmTeam.UCMPlayer;

public class ADRVX extends UCMPlayer 
{

	@Override
	protected TeamManager getTeamManager() 
	{
		return new Entrenador();
	}
}
