package AstonBirrasFc;


import EDU.gatech.cc.is.util.Vec2;
import teams.ucmTeam.Behaviour;
import teams.ucmTeam.RobotAPI;

public class Portero extends Behaviour
{
	private static int SIDE;
	private Ayudas ayuda;
	private enum Estados {DesbloqCOM, DesbloqCON,MoverPortero,PelotaAtrasGolpear,FueraAreaVuelve,Porteria};
	private Estados estado;
	
	public void configure() 
	{ 
		ayuda=new Ayudas();
	}

	public int takeStep() 
	{				
		estado=calculaEstado();
		
		Vec2 ball=myRobotAPI.getBall();
		Vec2[] teammates=myRobotAPI.getTeammates();
		Vec2[] opponents=myRobotAPI.getOpponents();
		Vec2 ourgoal=myRobotAPI.getOurGoal();
		SIDE=myRobotAPI.getFieldSide();
		
		switch (estado) {
			case DesbloqCOM:
				ayuda.evitarBloqueo(myRobotAPI.getClosestMate(), myRobotAPI);
				myRobotAPI.setDisplayString("Por. (EBC)");
				
				//Evita colision compañeros
				for(int i =0;i<teammates.length;i++) 
				{
					if(ayuda.cercanoRadio(myRobotAPI.getPosition(), myRobotAPI.toFieldCoordinates(teammates[i]),0.2))
					{
						ayuda.evitaColision(myRobotAPI.toFieldCoordinates(teammates[i]), myRobotAPI);
						myRobotAPI.setDisplayString("Portero (ECC)");
					}
				}
				//Evita colision oponentes
				for(int i =0;i<opponents.length;i++)
				{
					if(ayuda.cercanoRadio(myRobotAPI.getPosition(), myRobotAPI.toFieldCoordinates(opponents[i]),0.2))
					{
						ayuda.evitaColision(myRobotAPI.toFieldCoordinates(opponents[i]), myRobotAPI);
						myRobotAPI.setDisplayString("Portero (ECO)");
					}
				}
				
				break;
				
			case DesbloqCON:
				ayuda.evitarBloqueo(myRobotAPI.getClosestOpponent(), myRobotAPI);
				myRobotAPI.setDisplayString("Por. (EBO)");
				
				//Evita colision compañeros
				for(int i =0;i<teammates.length;i++) 
				{
					if(ayuda.cercanoRadio(myRobotAPI.getPosition(), myRobotAPI.toFieldCoordinates(teammates[i]),0.2))
					{
						ayuda.evitaColision(myRobotAPI.toFieldCoordinates(teammates[i]), myRobotAPI);
						myRobotAPI.setDisplayString("Portero (ECC)");
					}
				}
				//Evita colision Oponentes
				for(int i =0;i<opponents.length;i++)
				{
					if(ayuda.cercanoRadio(myRobotAPI.getPosition(), myRobotAPI.toFieldCoordinates(opponents[i]),0.2))
					{
						ayuda.evitaColision(myRobotAPI.toFieldCoordinates(opponents[i]), myRobotAPI);
						myRobotAPI.setDisplayString("Portero (ECO)");
					}
				}
				break;
				
			case MoverPortero:
				myRobotAPI.setSteerHeading(ayuda.irAPosicionParando(myRobotAPI.toFieldCoordinates(ourgoal), myRobotAPI,0.01));
				myRobotAPI.setSpeed(1.0);
				myRobotAPI.setDisplayString("Portero (area)");
				break;
				
			case PelotaAtrasGolpear:
				// Decide hacia que lado golpear el balon
				Vec2 dest = myRobotAPI.getOpponentsGoal(); 
				// decide donde golpea la bola
				if (myRobotAPI.getPosition().y > 0)
				{
					// Golpea a la parte superior
					dest = new Vec2(0, myRobotAPI.getUpperFieldBound());
				}
				else
				{
					// Golpea a la parte inferior
					dest = new Vec2(0, myRobotAPI.getLowerFieldBound());				
				}
				
				// Comprueba si puede golpear la bola
				if (myRobotAPI.canKick())
				{
					myRobotAPI.setBehindBall(myRobotAPI.toEgocentricalCoordinates(dest));
					myRobotAPI.kick();
					myRobotAPI.setDisplayString("Portero (kick)");
				}
				break;
				
			case FueraAreaVuelve:
				Vec2 move;
				if (ourgoal.y < 0)
				{
					move=new Vec2((double)SIDE, -8);				
				}
				else
				{
					move=new Vec2((double)SIDE, 8);				
				}
				myRobotAPI.setSteerHeading(ayuda.irAPosicionParando(myRobotAPI.toFieldCoordinates(move), myRobotAPI,0.01));
				//myRobotAPI.setSpeed(1.0);
				myRobotAPI.setDisplayString("Portero (area)");
				break;
				
			case Porteria:
				Vec2 movePor;
				//Para que este entre el balon y la porteria
				if( ball.y > 0)
				{
					movePor=new Vec2((double)SIDE, 8);
					myRobotAPI.setSteerHeading(ayuda.irAPosicionParando(myRobotAPI.toFieldCoordinates(movePor), myRobotAPI,0.0001));
				}
				else
				{
					movePor=new Vec2((double)SIDE, -8);
					myRobotAPI.setSteerHeading(ayuda.irAPosicionParando(myRobotAPI.toFieldCoordinates(movePor), myRobotAPI,0.0001));
				}

				if( Math.abs( ball.y) < myRobotAPI.getPlayerRadius() * 0.15)
				{
					myRobotAPI.setSpeed(0);
				}				
				else
				{
					myRobotAPI.setSpeed(1.0);
				}			
				myRobotAPI.setDisplayString("Portero (wait)");
				
				break;
				
			default:
				break;
		}
		return myRobotAPI.ROBOT_OK;
	}

	
	private Estados calculaEstado()
	{
		//Cojo posicion de la bola
		Vec2 ball=myRobotAPI.getBall();
		SIDE=myRobotAPI.getFieldSide();
		//if(distancia > 0.18)
		// Tomar seccion % del campo total
		double section = Math.abs(myRobotAPI.getLeftFieldBound()-myRobotAPI.getRightFieldBound())*0.05;
		if (Math.abs(myRobotAPI.getPosition().x) < Math.abs(myRobotAPI.getLeftFieldBound())-section)
		{ //Si no esta cerca de la porteria(factor del 15%) evita a contrarios y a compañeros
			
			
			//Si esta bloqueado se zafa de el(amigo)
			if (myRobotAPI.isBlocking(myRobotAPI.getClosestMate()))
			{
				return Estados.DesbloqCOM;
			}
			
			//Si esta bloqueado se zafa de el(Oponente)
			if (myRobotAPI.isBlocking(myRobotAPI.getClosestOpponent()))
			{
				return Estados.DesbloqCON;
			}				
				
			if( (myRobotAPI.getPosition().x * SIDE < 1.37 || Math.abs(myRobotAPI.getPosition().y) > 0.25))
			{
				return Estados.MoverPortero;
			}
			myRobotAPI.setDisplayString("Portero (Out)");
		}
		
		//Si la pelota esta detras del portero intenta ir a por ella y golpearla hacia fuera
		if( ball.x * SIDE > 0)
		{
			return Estados.PelotaAtrasGolpear;
		}
		
		//Si esta fuera del area de gol vuelve
		if( (myRobotAPI.getPosition().x * SIDE < 1.07 || Math.abs(myRobotAPI.getPosition().y) > 0.2222))
		{
			return Estados.FueraAreaVuelve;
		}
		return Estados.Porteria;
	}
	
	public void onInit(RobotAPI r) 
	{
	}

	public void onRelease(RobotAPI r) { }

	public void end() { }
}
