package AstonBirrasFc;

import teams.ucmTeam.Behaviour;
import teams.ucmTeam.RobotAPI;
import EDU.gatech.cc.is.util.Vec2;

public class Delantero extends Behaviour
{
	private Ayudas ayuda;
	
	public void configure() 
	{ 
		ayuda=new Ayudas();
	}

	public int takeStep() 
	{				
		//Cojo posicion de la bola
		Vec2 ball=myRobotAPI.getBall();
		Vec2 ballCoordinates=myRobotAPI.toFieldCoordinates(ball);
		int defenseside=myRobotAPI.getFieldSide();
		int ataqueside=(-1)*defenseside;
		ayuda.evitaColision(myRobotAPI.getClosestMate(), myRobotAPI);

		//Si la pelota esta en el lado donde se encuentra el delantero va a por la pelota
		if( (ballCoordinates.x >= 0 && ataqueside==myRobotAPI.EAST_FIELD) || (ballCoordinates.x <= 0 && ataqueside==myRobotAPI.WEST_FIELD))
		{
			
			myRobotAPI.setDisplayString("PK");
			//Si puede golpearla, la golpea
			//Sino prueba a pasarla
			if(ayuda.cercano(myRobotAPI.getPosition(), myRobotAPI.toFieldCoordinates(myRobotAPI.getOpponentsGoal()), myRobotAPI, 0.15))
			{
				myRobotAPI.setBehindBall(ball);
				
				Vec2 goal = myRobotAPI.toFieldCoordinates(myRobotAPI.getOpponentsGoal());
				double angleAux = ayuda.anguloNecesario(goal, myRobotAPI, 0.01);
				double angle = ayuda.degToRad(ayuda.radToDeg(angleAux) + 180);
				myRobotAPI.setSteerHeading(angle);
				if(myRobotAPI.alignedToBallandGoal())
				{
					myRobotAPI.setDisplayString("Tiro");
					myRobotAPI.kick();					
				}	
				
				
			}
			else
			{
				//Recorre donde estan todos sus compaņeros y se la pasa al mas cercano
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
				return myRobotAPI.ROBOT_OK;
			}
		}
		else
		{
			//Se queda en el medio esperando a que pase la bola
			//Me quedo en el cuarto de campo esperando a algun contrario
			//Evita bloquearse con compaņeros
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
				
				// Return
				return myRobotAPI.ROBOT_OK;
			}
			
		}
		return myRobotAPI.ROBOT_OK;
	}

	public void onInit(RobotAPI r) 
	{
	}

	public void onRelease(RobotAPI r) { }

	public void end() { }
}
