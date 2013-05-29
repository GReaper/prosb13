package AstonBirrasFc;

import EDU.gatech.cc.is.util.Vec2;
import teams.ucmTeam.Behaviour;
import teams.ucmTeam.RobotAPI;

public class Portero extends Behaviour
{
	private static int SIDE;
	private Ayudas ayuda;
	public void configure() 
	{ 
		ayuda=new Ayudas();
	}

	public int takeStep() 
	{				
		//Cojo posicion de la bola
		Vec2 ball=myRobotAPI.getBall();
		//Cojo donde esta mi porteria
		Vec2 ourgoal=myRobotAPI.getOurGoal();
		Vec2[] teammates=myRobotAPI.getTeammates();
		Vec2[] opponents=myRobotAPI.getOpponents();
		SIDE=myRobotAPI.getFieldSide();
		if(!ayuda.cercano(myRobotAPI.getPosition(), myRobotAPI.toFieldCoordinates(ourgoal), myRobotAPI, 0.15))
		{ //Si no esta cerca de la porteria(factor del 15%) evita a contrarios y a compañeros
			
			//Evita colision compañeros
			for(int i =0;i<teammates.length;i++)
			{
				if(ayuda.cercanoRadio(myRobotAPI.getPosition(), myRobotAPI.toFieldCoordinates(teammates[i]),0.2))
				{
					ayuda.evitaColision(myRobotAPI.toFieldCoordinates(teammates[i]), myRobotAPI);
					myRobotAPI.setDisplayString("Portero (ECC)");
				}
			}
			
			for(int i =0;i<opponents.length;i++)
			{
				if(ayuda.cercanoRadio(myRobotAPI.getPosition(), myRobotAPI.toFieldCoordinates(opponents[i]),0.2))
				{
					ayuda.evitaColision(myRobotAPI.toFieldCoordinates(opponents[i]), myRobotAPI);
					myRobotAPI.setDisplayString("Portero (ECO)");
				}
			}
			return myRobotAPI.ROBOT_OK;
		}
		else
		//Si la pelota esta detras del portero intenta ir a por ella y golpearla hacia fuera
		if( ball.x * SIDE > 0)
		{
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
		}
		else
		//Si esta fuera del area de gol vuelve
		if( (Math.abs(ourgoal.x) > myRobotAPI.getPlayerRadius() * 1.5) ||
				 (Math.abs(ourgoal.y) > myRobotAPI.getPlayerRadius() * 2.9) )
		{
			myRobotAPI.setSteerHeading(ayuda.irAPosicionParando(myRobotAPI.toFieldCoordinates(ourgoal), myRobotAPI,0.0001));
			myRobotAPI.setSpeed(1.0);
			myRobotAPI.setDisplayString("Portero (area)");
		}
		else		
		{
			//Para que este entre el balon y la porteria
			if( ball.y > 0)
			{
				Vec2 move=new Vec2((double)SIDE, 8);
				myRobotAPI.setSteerHeading(ayuda.irAPosicionParando(myRobotAPI.toFieldCoordinates(move), myRobotAPI,0.0001));
			}
			else
			{
				Vec2 move=new Vec2((double)SIDE, -8);
				myRobotAPI.setSteerHeading(ayuda.irAPosicionParando(myRobotAPI.toFieldCoordinates(move), myRobotAPI,0.0001));
			}

			if( Math.abs( ball.y) < myRobotAPI.getPlayerRadius() * 0.15)
				myRobotAPI.setSpeed(0);
			else
				myRobotAPI.setSpeed(1.0);
			myRobotAPI.setDisplayString("Portero (wait)");
				
		}
		return myRobotAPI.ROBOT_OK;
	}

	public void onInit(RobotAPI r) 
	{
	}

	public void onRelease(RobotAPI r) { }

	public void end() { }
}
