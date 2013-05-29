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
			_players[jugador2].setBehaviour(_behaviours[11]);//pepe
			_players[jugador3].setBehaviour(_behaviours[2]);//mco
			_players[jugador4].setBehaviour(_behaviours[9]);//delantero
			
			return;
			
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
			_players[jugador2].setBehaviour(_behaviours[4]);//go to ball
			_players[jugador3].setBehaviour(_behaviours[9]);//delantero
			_players[jugador4].setBehaviour(_behaviours[3]);//rebote
			return;
			
		}
		
		//siempre debera haber un portero
		//si el portero esta bloqueado, miramos quien sera el portero
		boolean cambioPortero=false;
		boolean cambioDefensa=true;
		int maximoPortero=3;
		int maximoDefensa=4;
		
		//creamos la lista ordenada
		List<Vec2Ordered> listaOrdenada= new ArrayList<Vec2Ordered>();
		
		
		/**/
		Vec2 yo= _players[quienPortero].getRobotAPI().getPosition(); listaOrdenada.add(new Vec2Ordered(yo, myRobotAPI.getFieldSide(),quienPortero));
		
		Vec2 p2= _players[quienDefensa].getRobotAPI().getPosition();listaOrdenada.add(new Vec2Ordered(p2, myRobotAPI.getFieldSide(),quienDefensa));
		
		Vec2 p3= _players[jugador2].getRobotAPI().getPosition();listaOrdenada.add(new Vec2Ordered(p3, myRobotAPI.getFieldSide(),jugador2));
		
		Vec2 p4= _players[jugador3].getRobotAPI().getPosition();listaOrdenada.add(new Vec2Ordered(p4, myRobotAPI.getFieldSide(),jugador3));
		
		Vec2 p5= _players[jugador4].getRobotAPI().getPosition();listaOrdenada.add(new Vec2Ordered(p5, myRobotAPI.getFieldSide(),jugador4));
		
		
		
		Collections.sort(listaOrdenada);
		/**/
		
		
		if (estaBloqueadoPortero(quienPortero))
		{
			int aux=0;
			boolean parar=false;
			while(aux<maximoPortero && !parar)
			{
				if(estaBloqueado2(aux,listaOrdenada))
					aux++;
				else
					parar=true;
			}
			if(!parar)
			{
				aux=0;
			}
			porteroPrevio=quienPortero;
			
			quienPortero=listaOrdenada.get(aux).getId();
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
				if(estaBloqueado2(aux,listaOrdenada) || aux==quienPortero)
					aux++;
				else
					parar=true;
			}
			if(!parar)
			{
				aux=1;
			}
			defensaPrevio=quienDefensa;
			
			quienDefensa=listaOrdenada.get(aux).getId();;
			_players[quienDefensa].setBehaviour(_behaviours[5]);
			cambioDefensa=true;
			
		}
		else
		{
			defensaPrevio=-1;
		}
		
		
		//cambio a jugada desesperada
		if(_players[quienPortero].getRobotAPI().getTimeStamp()>=60*1000
				&& 
			myRobotAPI.getMyScore()- myRobotAPI.getOpponentScore()<=-2	
				)
		{
			jugadaDesesperada=true;
		}
		
		//TODO quedan poner bien los comentarios
		
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
					_players[jugador4].setBehaviour(_behaviours[3]);//dsesmarque
					jugadaAtaque=false;
					return;
					
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
								_players[jugador4].setBehaviour(_behaviours[3]);//dsesmarque
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
						_players[jugador2].setBehaviour(_behaviours[4]);//pepe
						_players[jugador3].setBehaviour(_behaviours[9]);//rebote
						_players[jugador4].setBehaviour(_behaviours[3]);//delantero					
						return;
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
								_players[jugador2].setBehaviour(_behaviours[4]);//MCO
								_players[jugador3].setBehaviour(_behaviours[9]);//delantero
								_players[jugador4].setBehaviour(_behaviours[3]);//dsesmarque
							}
							jugadaAtaque=false;
							
						}
						
					}
				}
				else
				{//ganamos
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
						_players[jugador4].setBehaviour(_behaviours[9]);//desmarque
						return;
						
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
								_players[jugador4].setBehaviour(_behaviours[9]);//dsesmarque
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
				
				cambioJugadaDesesperada=true;
			}
			else
			{
				cambioJugadaDesesperada=false;
			}
			
			
			if (myRobotAPI.getMyScore() < myRobotAPI.getOpponentScore()&& cambioJugadaDesesperada)
			{//perdiendo
				_players[jugador4].setBehaviour(_behaviours[6]);//otro portero
				_players[jugador2].setBehaviour(_behaviours[9]);//MCO
				_players[jugador3].setBehaviour(_behaviours[8]);//delantero				
				return;
			}
			else
			{
				jugadaDesesperada=false;
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
	
	public boolean estaBloqueado2(int numero,List<Vec2Ordered>lista)
	{
		
		int id= lista.get(numero).getId();
		
		//probar con
		//return _players[id].getRobotAPI().blocked();
		
		return _players[id].getRobotAPI().opponentBlocking()
				||	_players[id].getRobotAPI().teammateBlocking();
		
		//return _players[numero].getRobotAPI().opponentBlocking();
		//||	_players[numero].getRobotAPI().teammateBlocking();
	}
	
	
	
	public boolean estaBloqueado(int numero)
	{
		
		return _players[numero].getRobotAPI().opponentBlocking()
		||	_players[numero].getRobotAPI().teammateBlocking();
	}
	
	public boolean estaBloqueadoPortero(int numero)
	{
		Ayudas a= new Ayudas();
		return _players[numero].getRobotAPI().opponentBlocking() || a.pelotaEnMiCampo(_players[numero].getRobotAPI());
				
	}
}
