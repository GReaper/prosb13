package AstonBirrasFc;

import teams.ucmTeam.Behaviour;
import teams.ucmTeam.RobotAPI;
import teams.ucmTeam.TeamManager;

public class Entrenador extends TeamManager 
{
	private RobotAPI myRobotAPI;
	private Ayudas helper;
	
	public int onConfigure() 
	{
		helper = new Ayudas();
		return RobotAPI.ROBOT_OK;
	}
	
	public void onTakeStep() 
	{
		/*
		 * 0 - Portero <-> MC
		 * 1 - MCD <-> MCO <-> Portero
		 * 2 - MCO <-> Pepe <-> Reboteador
		 * 3 - Delantero <-> Desmarcador <-> Regateador <-> BloqueoPortero
		 * 4 - Delantero <-> Desmarcador <-> Regateador <-> BloqueoPortero
		 */
		myRobotAPI = _players[0].getRobotAPI();
		
		/* Vamos ganando o empatados */
		if (myRobotAPI.getMyScore() >= myRobotAPI.getOpponentScore())
		{
			// Cambiar portero por MC si es necesario
			if (_players[0].getRobotAPI().opponentBlocking())
			{
				_players[0].setBehaviour(_behaviours[5]);
				_players[1].setBehaviour(_behaviours[7]);				
			}
			// Comprobar cambios en el MC. El portero no está bloqueado y no es necesario
			// cambiarlo.
			else
			{
				_players[0].setBehaviour(_behaviours[7]);
				_players[1].setBehaviour(_behaviours[5]);				
			}
			
			// Si el desmarcador "inicial" tiene cerca la bola, el delantero pasa a 
			// bloquear al portero y el desm. a regateador
			if (helper.cercanoRadio(_players[4].getRobotAPI().getPosition(), 
									_players[4].getRobotAPI().toFieldCoordinates(_players[4].getRobotAPI().getBall()), 
									_players[4].getRobotAPI().getPlayerRadius()*3))
			{
				_players[2].setBehaviour(_behaviours[2]);
				_players[3].setBehaviour(_behaviours[8]);
				_players[4].setBehaviour(_behaviours[6]);
				
			}
			// Si es al revés, que el delantero "inicial" es quien tiene cerca la bola,
			// éste pasa a regateador y el desmarcador a bloqueador
			else if (helper.cercanoRadio(_players[3].getRobotAPI().getPosition(), 
						_players[3].getRobotAPI().toFieldCoordinates(_players[4].getRobotAPI().getBall()), 
						_players[3].getRobotAPI().getPlayerRadius()*3))
			{
				_players[2].setBehaviour(_behaviours[2]);
				_players[3].setBehaviour(_behaviours[6]);
				_players[4].setBehaviour(_behaviours[8]);
				
			}
			// Si no se da ninguno de los casos, el MCO "inicial" pasa a Pepe
			// o reboteador
			else
			{
				// TODO: falta el jugador PEPE!
				// Cambio a reboteador.
				_players[2].setBehaviour(_behaviours[3]);
				_players[3].setBehaviour(_behaviours[9]);
				_players[4].setBehaviour(_behaviours[4]);
			}
		}
		
		/* Vamos perdiendo */
		else
		{
			// Cambiar portero por MC si es necesario
			if (_players[0].getRobotAPI().opponentBlocking())
			{
				_players[0].setBehaviour(_behaviours[5]);
				_players[1].setBehaviour(_behaviours[7]);				
			}
			// Comprobar cambios en el MC. El portero no está bloqueado y no es necesario
			// cambiarlo.
			else
			{
				_players[0].setBehaviour(_behaviours[7]);
				_players[1].setBehaviour(_behaviours[5]);				
			}		
		}		
	}
	
	public Behaviour getDefaultBehaviour(int id) 
	{
		// Comportamientos por defecto
		switch (id)
		{
			// Jugador 0, portero inicial
			case 0: return  _behaviours[7];
			// Jugador 1, MCD inicial
			case 1: return  _behaviours[5];
			// Jugador 2, MCO inicial
			case 2: return  _behaviours[2];
			// Jugador 3, Delantero inicial
			case 3: return  _behaviours[9];
			// Jugador 4, Desmarcador inicial
			case 4: return  _behaviours[4];
			// En caso de llegar aquí, Go to ball inicial
			default: return  _behaviours[0];
		}
	}
	
	public Behaviour[] createBehaviours() 
	{
		return new Behaviour[] {
								new GoToBall(),			 	//0
								new QuietoParao(),			//1
								new MedioCentroAtacante(),	//2
								new Rebote(),				//3
								new Desmarcador(),			//4
								new MedioCentroDefensivo(),	//5
								new Regateador(),			//6
								new Portero(),				//7
								new BloqueaPortero(),		//8
								new Delantero(),			//9
								new LocoBola()				//10
							};
		
	}
}
