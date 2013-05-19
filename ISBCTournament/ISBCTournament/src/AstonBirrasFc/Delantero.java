package AstonBirrasFc;

import teams.ucmTeam.Behaviour;
import teams.ucmTeam.RobotAPI;
import EDU.gatech.cc.is.util.Vec2;

public class Delantero extends Behaviour
{
	private static int ATTACKSIDE;
	private Ayudas ayuda;
	
	public void configure() 
	{ 
		ayuda=new Ayudas();
	}

	public int takeStep() 
	{				
		//Cojo posicion de la bola
		Vec2 ball=myRobotAPI.getBall();
		int defenseside=myRobotAPI.getFieldSide();
		ATTACKSIDE=(-1)*defenseside;
		//Si esta bloqueado busca desbloquearse
		if (myRobotAPI.isBlocking(myRobotAPI.getClosestMate()))
		{
			Vec2 dest = myRobotAPI.toFieldCoordinates(myRobotAPI.getClosestMate());
			double angleAux = ayuda.anguloNecesario(dest, myRobotAPI, 0.01);
			double angle = ayuda.degToRad(ayuda.radToDeg(angleAux) + 180);
			myRobotAPI.setSteerHeading(angle);	

			myRobotAPI.setSpeed(1);
			
			myRobotAPI.setDisplayString("Evita bloqueo COM");
		}
		else
		if(myRobotAPI.isBlocking(myRobotAPI.getClosestOpponent()))
		{
			Vec2 dest = myRobotAPI.toFieldCoordinates(myRobotAPI.getClosestOpponent());
			double angleAux = ayuda.anguloNecesario(dest, myRobotAPI, 0.01);
			double angle = ayuda.degToRad(ayuda.radToDeg(angleAux) + 180);
			myRobotAPI.setSteerHeading(angle);	

			myRobotAPI.setSpeed(1);
			
			myRobotAPI.setDisplayString("Evita bloqueo OPP");
		}
		else
		//Si la pelota esta en el lado donde se encuentra el delantero va a por la pelota
		if( ball.x * ATTACKSIDE > 0)
		{
			myRobotAPI.setBehindBall(ball);
			myRobotAPI.setDisplayString("Busca tiro o pase");
			//Si puede golpearla, la golpea
			//Sino prueba a pasarla
			if(myRobotAPI.canKick())
			{
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
			}
		}
		else
		{
			
		}
		return myRobotAPI.ROBOT_OK;
	}

	public void onInit(RobotAPI r) 
	{
	}

	public void onRelease(RobotAPI r) { }

	public void end() { }
}
