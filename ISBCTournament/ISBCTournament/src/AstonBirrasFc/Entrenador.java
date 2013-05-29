package AstonBirrasFc;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.SortedMap;

import EDU.gatech.cc.is.util.Vec2;
import teams.ucmTeam.Behaviour;
import teams.ucmTeam.RobotAPI;
import teams.ucmTeam.TeamManager;

public class Entrenador extends TeamManager 
{
	private RobotAPI myRobotAPI;
	private Ayudas helper;
	int quienPortero=0;
	int quienDefensa=1;
	int jugador2=2;
	int jugador3=3;
	int jugador4=4;
	
	int porteroPrevio=-1;
	int defensaPrevio=-1;
	
	boolean jugadaAtaque=false;
	boolean jugadaDesesperada=false;
	boolean cambioJugadaDesesperada=false;
	
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
		 * 2 - MCO <-> Pepe <-> Reboteador<-> portero
		 * 3 - Delantero <-> Desmarcador <-> Regateador <-> BloqueoPortero
		 * 4 - Delantero <-> Desmarcador <-> Regateador <-> BloqueoPortero
		 */
		myRobotAPI = _players[quienPortero].getRobotAPI();
		if(myRobotAPI.getJustScored()==1)
		{//acabamos de marcar
			quienPortero=0;
			quienDefensa=1;
			jugador2=2;
			jugador3=3;
			jugador4=4;
			_players[quienPortero].setBehaviour(_behaviours[7]);
			_players[quienDefensa].setBehaviour(_behaviours[5]);//MCD
			_players[jugador2].setBehaviour(_behaviours[5]);//MCD
			_players[jugador3].setBehaviour(_behaviours[11]);//delantero
			_players[jugador4].setBehaviour(_behaviours[4]);//rebote
			
		}
		if(myRobotAPI.getJustScored()==-1)
		{//nos acaban de marcar
			quienPortero=0;
			quienDefensa=1;
			jugador2=2;
			jugador3=3;
			jugador4=4;
			_players[quienPortero].setBehaviour(_behaviours[7]);
			_players[quienDefensa].setBehaviour(_behaviours[5]);
			_players[jugador2].setBehaviour(_behaviours[0]);//go to ball
			_players[jugador3].setBehaviour(_behaviours[11]);//delantero
			_players[jugador4].setBehaviour(_behaviours[4]);//rebote
		}
		
		/*
		// Vamos empatados
		//  
		//  Comportamiento normal
		//
		if (myRobotAPI.getMyScore() == myRobotAPI.getOpponentScore())
		{
			// Cambiar portero por MC si es necesario
			if (_players[0].getRobotAPI().opponentBlocking() || _players[0].getRobotAPI().teammateBlocking())
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
		
		// Vamos ganando 
		//  
		//  Agregamos un pepe o bloqueador atrás para defender más
		//
		else if (myRobotAPI.getMyScore() > myRobotAPI.getOpponentScore())
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
			// Por ir ganando, agregamos otro Pepe
			if (helper.cercanoRadio(_players[4].getRobotAPI().getPosition(), 
									_players[4].getRobotAPI().toFieldCoordinates(_players[4].getRobotAPI().getBall()), 
									_players[4].getRobotAPI().getPlayerRadius()*3))
			{
				_players[2].setBehaviour(_behaviours[11]);
				_players[3].setBehaviour(_behaviours[8]);
				_players[4].setBehaviour(_behaviours[6]);
				
			}
			// Si es al revés, que el delantero "inicial" es quien tiene cerca la bola,
			// éste pasa a regateador y el desmarcador a bloqueador
			// Por ir ganando, agregamos otro MCD (si esto no funciona, cambiar por Pepe)
			else if (helper.cercanoRadio(_players[3].getRobotAPI().getPosition(), 
						_players[3].getRobotAPI().toFieldCoordinates(_players[4].getRobotAPI().getBall()), 
						_players[3].getRobotAPI().getPlayerRadius()*3))
			{
				_players[2].setBehaviour(_behaviours[11]);
				_players[3].setBehaviour(_behaviours[6]);
				_players[4].setBehaviour(_behaviours[8]);
				
			}
			// Si no se da ninguno de los casos, el MCO "inicial" pasa a Pepe
			// o reboteador
			else
			{
				// TODO: falta el jugador PEPE!
				// Cambio a reboteador.
				_players[2].setBehaviour(_behaviours[11]);
				_players[3].setBehaviour(_behaviours[9]);
				_players[4].setBehaviour(_behaviours[4]);
			}
		}
		
		// Vamos perdiendo 
		//  
		//  
		//
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
		*/
		
		//siempre debera haber un portero
		//si el portero esta bloqueado, miramos quien sera el portero
		boolean cambioPortero=false;
		boolean cambioDefensa=true;
		int maximoPortero=3;
		int maximoDefensa=4;
		
		//creamos la lista ordenada
		//List<Vec2Ordered> listaOrdenada= new ArrayList<Vec2Ordered>();
		
		
		/*
		Vec2 yo=myRobotAPI.getPosition(); listaOrdenada.add(new Vec2Ordered(yo, myRobotAPI.getFieldSide(),quienPortero));
		
		Vec2 p2= _players[quienDefensa].getRobotAPI().getPosition();listaOrdenada.add(new Vec2Ordered(p2, myRobotAPI.getFieldSide(),quienDefensa));
		
		Vec2 p3= _players[jugador2-1].getRobotAPI().getPosition();listaOrdenada.add(new Vec2Ordered(p3, myRobotAPI.getFieldSide(),jugador2-1));
		
		Vec2 p4= _players[jugador3-1].getRobotAPI().getPosition();listaOrdenada.add(new Vec2Ordered(p4, myRobotAPI.getFieldSide(),jugador3-1));
		
		Vec2 p5= _players[jugador4-1].getRobotAPI().getPosition();listaOrdenada.add(new Vec2Ordered(p5, myRobotAPI.getFieldSide(),jugador4-1));
		
		
		
		Collections.sort(listaOrdenada);
		*/
		
		
		if (estaBloqueado(quienPortero))
		{
			int aux=0;
			boolean parar=false;
			while(aux<maximoPortero && !parar)
			{
				if(estaBloqueado(aux))
					aux++;
				else
					parar=true;
			}
			if(!parar)
			{
				quienPortero=0;
			}
			porteroPrevio=quienPortero;
			
			quienPortero=aux;
			_players[quienPortero].setBehaviour(_behaviours[7]);
			cambioPortero=true;
			
			
		}
		else
		{
			porteroPrevio=-1;
		}
		
		//siempre debera haber un MCD
		if (estaBloqueado(quienDefensa))
		{
			int aux=0;
			boolean parar=false;
			while(aux<maximoDefensa && !parar)
			{
				if(estaBloqueado(aux) || aux==quienPortero)
					aux++;
				else
					parar=true;
			}
			if(!parar)
			{
				quienDefensa=1;
			}
			defensaPrevio=quienDefensa;
			
			quienDefensa=aux;
			_players[quienDefensa].setBehaviour(_behaviours[5]);
			cambioDefensa=true;
			
		}
		else
		{
			defensaPrevio=-1;
		}
		
		//si hay un cambio de defensa o portero, resasignamos los jugadores
		//con su valor correspondiente para cada estado
		
		
		if(!jugadaDesesperada)
		{
			//detectar jugada desesperada
			//es jugada desesperada si:
			//poco tiempo y estamos empatados
			//perdemos de mas de 2
			//ganamos de mas de 2
		
			//con la defensa siempre ajustada, deberemos mirar el resto de los jugadores para que 
			//ataquen o defiendan en funcion de como esta el resultado
			//la pelota y si estan bloqueados
			if (myRobotAPI.getMyScore() == myRobotAPI.getOpponentScore())
			{
				if(myRobotAPI.getTimeStamp()>60*1000)//media parte
				{
					jugadaDesesperada=true;
				}
				
				
				//3->MCO
				//4->Delantero
				//5->Desmarque
				if((cambioPortero||cambioDefensa) && !(defensaPrevio==quienPortero && porteroPrevio==quienDefensa))
				{
					int nAsignados=0;
					
					for(int i=0;i<5;i++)
					{
						if(i!= quienPortero && i!= quienDefensa)
						{
							switch(nAsignados)
							{
							case 0:jugador2=i;break;
							case 1:jugador3=i;break;
							case 2:jugador4=i;break;
							}
							
							nAsignados++;
						}
					}
					_players[jugador2].setBehaviour(_behaviours[2]);//MCO
					_players[jugador3].setBehaviour(_behaviours[9]);//delantero
					_players[jugador4].setBehaviour(_behaviours[4]);//dsesmarque
					jugadaAtaque=false;
					
					
				}
				
				//no estamos en jugada de ataque y el jugador 5 esta muy cerca de la bola
				//jugador4-> bloqueo
				//jugador5->regate
				if(!jugadaAtaque && helper.cercanoRadio(_players[jugador4].getRobotAPI().getPosition(), 
						_players[jugador4].getRobotAPI().toFieldCoordinates(_players[jugador4].getRobotAPI().getBall()), 
						_players[jugador4].getRobotAPI().getPlayerRadius()*3))
				{
					jugadaAtaque=true;
					_players[jugador4].setBehaviour(_behaviours[6]);//regate
					_players[jugador3].setBehaviour(_behaviours[8]);//bloqueo
				}
				else
				{	//miramos la jugada contraria
					//jugador5-> bloqueo
					//jugador4->regate	
					if(!jugadaAtaque && helper.cercanoRadio(_players[jugador3].getRobotAPI().getPosition(), 
							_players[jugador3].getRobotAPI().toFieldCoordinates(_players[jugador3].getRobotAPI().getBall()), 
							_players[jugador3].getRobotAPI().getPlayerRadius()*3))
					{
						jugadaAtaque=true;
						_players[jugador3].setBehaviour(_behaviours[6]);//regate
						_players[jugador4].setBehaviour(_behaviours[8]);//bloqueo
					}
					else
					{	//miramos si es el jugador 3 el que esta cerca 
						if(!jugadaAtaque && helper.cercanoRadio(_players[jugador2].getRobotAPI().getPosition(), 
								_players[jugador2].getRobotAPI().toFieldCoordinates(_players[jugador2].getRobotAPI().getBall()), 
								_players[jugador2].getRobotAPI().getPlayerRadius()*3))
						{
							jugadaAtaque=true;
							_players[jugador2].setBehaviour(_behaviours[6]);//regate
							_players[jugador3].setBehaviour(_behaviours[2]);//regate
							_players[jugador4].setBehaviour(_behaviours[8]);//bloqueo
						}
						else
						{
							if(jugadaAtaque)
							{
								//ninguno esta cercano reiniciamos
								_players[jugador2].setBehaviour(_behaviours[2]);//MCO
								_players[jugador3].setBehaviour(_behaviours[9]);//delantero
								_players[jugador4].setBehaviour(_behaviours[4]);//dsesmarque
							}
							jugadaAtaque=false;
						}
						
					}
					
				}
				
			}
			else
			{
				if (myRobotAPI.getMyScore() < myRobotAPI.getOpponentScore())
				{//perdemos
					
					if(myRobotAPI.getMyScore()-myRobotAPI.getOpponentScore()<=2
							|| myRobotAPI.getTimeStamp()>80*1000)
					{
						jugadaDesesperada=true;
					}
					
					if(cambioPortero||cambioDefensa && !(defensaPrevio==quienPortero && porteroPrevio==quienDefensa))
					{
						int nAsignados=0;
						
						for(int i=0;i<5;i++)
						{
							if(i!= quienPortero && i!= quienDefensa)
							{
								switch(nAsignados)
								{
								case 0:jugador2=i;break;
								case 1:jugador3=i;break;
								case 2:jugador4=i;break;
								}
								
								nAsignados++;
							}
						}
						_players[jugador2].setBehaviour(_behaviours[10]);//locobola
						_players[jugador3].setBehaviour(_behaviours[9]);//delantero
						_players[jugador4].setBehaviour(_behaviours[4]);//desmarque					
						
					}
					//no estamos en jugada de ataque y el jugador 5 esta muy cerca de la bola
					//jugador4-> bloqueo
					//jugador5->regate
					if(!jugadaAtaque && helper.cercanoRadio(_players[jugador4].getRobotAPI().getPosition(), 
							_players[jugador4].getRobotAPI().toFieldCoordinates(_players[jugador4].getRobotAPI().getBall()), 
							_players[jugador4].getRobotAPI().getPlayerRadius()*3))
					{
						jugadaAtaque=true;
						_players[jugador4].setBehaviour(_behaviours[6]);//regate
						_players[jugador3].setBehaviour(_behaviours[8]);//bloqueo
					}
					else
					{	//miramos la jugada contraria
						//jugador5-> bloqueo
						//jugador4->regate	
						if(!jugadaAtaque && helper.cercanoRadio(_players[jugador3].getRobotAPI().getPosition(), 
								_players[jugador3].getRobotAPI().toFieldCoordinates(_players[jugador3].getRobotAPI().getBall()), 
								_players[jugador3].getRobotAPI().getPlayerRadius()*3))
						{
							jugadaAtaque=true;
							_players[jugador3].setBehaviour(_behaviours[6]);//regate
							_players[jugador4].setBehaviour(_behaviours[8]);//bloqueo
						}
						else
						{
							if(jugadaAtaque)
							{
								//ninguno esta cercano reiniciamos
								_players[jugador2].setBehaviour(_behaviours[10]);//MCO
								_players[jugador3].setBehaviour(_behaviours[9]);//delantero
								_players[jugador4].setBehaviour(_behaviours[4]);//dsesmarque
							}
							jugadaAtaque=false;
							
						}
						
					}
				}
				else
				{//ganamos
					if(myRobotAPI.getMyScore()-myRobotAPI.getOpponentScore()>=2
							|| myRobotAPI.getTimeStamp()>80*1000)
					{
						jugadaDesesperada=true;
					}
					
					if(cambioPortero||cambioDefensa && !(defensaPrevio==quienPortero && porteroPrevio==quienDefensa))
					{
						int nAsignados=0;
						
						for(int i=0;i<5;i++)
						{
							if(i!= quienPortero && i!= quienDefensa)
							{
								switch(nAsignados)
								{
								case 0:jugador2=i;break;
								case 1:jugador3=i;break;
								case 2:jugador4=i;break;
								}
								
								nAsignados++;
							}
						}
						_players[jugador2].setBehaviour(_behaviours[11]);//pepe
						_players[jugador3].setBehaviour(_behaviours[2]);//MCO
						_players[jugador4].setBehaviour(_behaviours[4]);//desmarque
						
						
					}
					//no estamos en jugada de ataque y el jugador 5 esta muy cerca de la bola
					//jugador4-> bloqueo
					//jugador5->regate
					if(!jugadaAtaque && helper.cercanoRadio(_players[jugador4].getRobotAPI().getPosition(), 
							_players[jugador4].getRobotAPI().toFieldCoordinates(_players[jugador4].getRobotAPI().getBall()), 
							_players[jugador4].getRobotAPI().getPlayerRadius()*3))
					{
						jugadaAtaque=true;
						_players[jugador4].setBehaviour(_behaviours[6]);//regate
						_players[jugador3].setBehaviour(_behaviours[8]);//bloqueo
					}
					else
					{	//miramos la jugada contraria
						//jugador5-> bloqueo
						//jugador4->regate	
						if(!jugadaAtaque && helper.cercanoRadio(_players[jugador3].getRobotAPI().getPosition(), 
								_players[jugador3].getRobotAPI().toFieldCoordinates(_players[jugador3].getRobotAPI().getBall()), 
								_players[jugador3].getRobotAPI().getPlayerRadius()*3))
						{
							jugadaAtaque=true;
							_players[jugador3].setBehaviour(_behaviours[6]);//regate
							_players[jugador4].setBehaviour(_behaviours[8]);//bloqueo
						}
						else
						{
							if(jugadaAtaque)
							{
								//ninguno esta cercano reiniciamos
								_players[jugador2].setBehaviour(_behaviours[11]);//MCO
								_players[jugador3].setBehaviour(_behaviours[2]);//delantero
								_players[jugador4].setBehaviour(_behaviours[4]);//dsesmarque
							}
							jugadaAtaque=false;
							
						}
						
					}
				}
			}
		}
		else
		{
			//jugada desesperada
			
			if(cambioPortero||cambioDefensa && !(defensaPrevio==quienPortero && porteroPrevio==quienDefensa))
			{
				int nAsignados=0;
				
				for(int i=0;i<5;i++)
				{
					if(i!= quienPortero && i!= quienDefensa)
					{
						switch(nAsignados)
						{
						case 0:jugador2=i;break;
						case 1:jugador3=i;break;
						case 2:jugador4=i;break;
						}
						
						nAsignados++;
					}
				}
				
				cambioJugadaDesesperada=false;
			}
			
			
			if (myRobotAPI.getMyScore() == myRobotAPI.getOpponentScore() && !cambioJugadaDesesperada)
			{//empate
				_players[jugador2].setBehaviour(_behaviours[3]);//desmarque
				_players[jugador4].setBehaviour(_behaviours[8]);//bloqueador
				_players[jugador3].setBehaviour(_behaviours[9]);//delantero
			
				
			}
			else
			{
				if (myRobotAPI.getMyScore() > myRobotAPI.getOpponentScore()&& !cambioJugadaDesesperada)
				{//ganamos
					_players[jugador4].setBehaviour(_behaviours[7]);//otro portero
					_players[jugador2].setBehaviour(_behaviours[2]);//MCO
					_players[jugador3].setBehaviour(_behaviours[9]);//delantero
					
				}
				else
				{//perdemos
					if( !cambioJugadaDesesperada)
					{
						_players[jugador2].setBehaviour(_behaviours[0]);//go to ball
						_players[jugador4].setBehaviour(_behaviours[8]);//bloqueador						
						_players[jugador3].setBehaviour(_behaviours[9]);//delantero
						
					}
				}
			}
			cambioJugadaDesesperada=true;
			
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
								new Desmarcador(),			//3
								new Rebote(),				//4
								new MedioCentroDefensivo(),	//5
								new Regateador(),			//6
								new Portero(),				//7
								new BloqueaPortero(),		//8
								new Delantero(),			//9
								new LocoBola(),				//10
								new Pepe()					//11
							
							};
		
	}
	
	/*public boolean estaBloqueado2(int numero,List<Vec2Ordered>lista)
	{
		
		int id= lista.get(numero).getId()%5;
		
		//probar con
		//return _players[id].getRobotAPI().blocked();
		
		return _players[id].getRobotAPI().opponentBlocking()
				||	_players[id].getRobotAPI().teammateBlocking();
		
		//return _players[numero].getRobotAPI().opponentBlocking();
		//||	_players[numero].getRobotAPI().teammateBlocking();
	}*/
	
	public boolean estaBloqueado(int numero)
	{
		
		return _players[numero].getRobotAPI().opponentBlocking()
		||	_players[numero].getRobotAPI().teammateBlocking();
	}
}
