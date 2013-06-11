package AstonBirrasFc;

import teams.ucmTeam.Behaviour;
import teams.ucmTeam.RobotAPI;
import EDU.gatech.cc.is.util.Vec2;

public class Delantero extends Behaviour
{
	private Ayudas ayuda;
	private enum Estados {Golpear,DetrasPelota,PasaBola,EsperarBola};
	private Estados estado;
	
	public void configure() 
	{ 
		ayuda=new Ayudas();
	}

	public int takeStep() 
	{		
		estado=calculaEstado();
		
		switch (estado) {
			case Golpear:
				myRobotAPI.setDisplayString("Del. (Tiro)");
				myRobotAPI.kick();		
				break;
				
			case DetrasPelota:
				myRobotAPI.setBehindBall(myRobotAPI.getOpponentsGoal());
				break;
				
			case PasaBola:
				//Recorre donde estan todos sus compañeros y se la pasa al mas cercano
				//que este en la zona de ataque
				Vec2[] teammates=myRobotAPI.getTeammates();
				int cercano=0;
				boolean cercaJugador=false;
				boolean cercaPorteria=false;
				for(int i=0;i < teammates.length;i++)
				{
					Vec2 friend=teammates[i];
					cercaJugador=ayuda.cercano(myRobotAPI.getPosition(), myRobotAPI.toFieldCoordinates(friend), myRobotAPI, 0.1);
					cercaPorteria=ayuda.cercano(myRobotAPI.toFieldCoordinates(friend), myRobotAPI.toFieldCoordinates(myRobotAPI.getOpponentsGoal()), myRobotAPI, 0.3);
					if(cercaJugador && cercaPorteria)
					{
						cercano=i;
					}
				}
				
				myRobotAPI.passBall(myRobotAPI.toFieldCoordinates(teammates[cercano]));
				myRobotAPI.setDisplayString("Pasa balon");
				break;
				
			case EsperarBola:
				//Devuelve EsperarBola
				//Se queda en el medio esperando a que pase la bola
				//Me quedo en el cuarto de campo esperando a algun contrario
				//Evita bloquearse con compañeros
				ayuda.evitaColision(myRobotAPI.getClosestMate(), myRobotAPI);
				ayuda.evitaColision(myRobotAPI.getClosestOpponent(), myRobotAPI);
				
				//Debe esperar en el medio del campo
				Double thirdSize = (myRobotAPI.getRightFieldBound() - myRobotAPI.getLeftFieldBound()) / 3;
				// Check if the player is in the first third of the field
				if (!(myRobotAPI.getPosition().x * myRobotAPI.getFieldSide() >= thirdSize))
				{
					// 1.- Set steering
					Vec2 dest = myRobotAPI.getOpponentsGoal();
					double angle = ayuda.anguloDestino(dest, myRobotAPI);			
					myRobotAPI.setSteerHeading(angle);	
					
					// 2.- Increase speed
					myRobotAPI.setSpeed(1000);

					// Set displayed text
					myRobotAPI.setDisplayString("Del. (Wait)");
				}
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
		Vec2 ballCoordinates=myRobotAPI.toFieldCoordinates(ball);
		int defenseside=myRobotAPI.getFieldSide();
		int ataqueside=(-1)*defenseside;

		//Si la pelota esta en el lado donde se encuentra el delantero va a por la pelota
		if( (ballCoordinates.x >= 0 && ataqueside==myRobotAPI.EAST_FIELD) || (ballCoordinates.x <= 0 && ataqueside==myRobotAPI.WEST_FIELD))
		{
			myRobotAPI.setDisplayString("Del. (PK)");
			//Si puede golpearla, la golpea
			//Sino prueba a pasarla
			if(ayuda.cercano(myRobotAPI.getPosition(), myRobotAPI.toFieldCoordinates(myRobotAPI.getOpponentsGoal()), myRobotAPI, 0.25)
					&& myRobotAPI.canKick() /*&& myRobotAPI.alignedToBallandGoal()*/
					&& !ayuda.cercanoRadio(myRobotAPI.getPosition(),  myRobotAPI.toFieldCoordinates(myRobotAPI.getClosestOpponent()), myRobotAPI.getPlayerRadius()*2))
			{
				//Devuelve Golpear
				return Estados.Golpear;			
			}
			
			if (!ayuda.cercano(myRobotAPI.getPosition(), myRobotAPI.toFieldCoordinates(myRobotAPI.getOpponentsGoal()), myRobotAPI, 0.25))
			{
				//Devuelve detrasPelota
				return Estados.DetrasPelota;
			}
			
			if (ayuda.cercano(myRobotAPI.getPosition(), myRobotAPI.toFieldCoordinates(myRobotAPI.getOpponentsGoal()), myRobotAPI, 0.25)
					&& ayuda.cercanoRadio(myRobotAPI.getPosition(),  myRobotAPI.toFieldCoordinates(myRobotAPI.getClosestOpponent()), myRobotAPI.getPlayerRadius()*2))
			{
				//Devuelve pasaBola
				return Estados.PasaBola;
			}
			else
			{
				return Estados.DetrasPelota;
			}
		}
		//Por defecto espera la bola
		return Estados.EsperarBola;
	}
	public void onInit(RobotAPI r) 
	{
	}

	public void onRelease(RobotAPI r) { }

	public void end() { }
}
