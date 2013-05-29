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
		//if(distancia > 0.18)
		// Tomar seccion % del campo total
		double section = Math.abs(myRobotAPI.getLeftFieldBound()-myRobotAPI.getRightFieldBound())*0.05;
		if (Math.abs(myRobotAPI.getPosition().x) < Math.abs(myRobotAPI.getLeftFieldBound())-section)
		{ //Si no esta cerca de la porteria(factor del 15%) evita a contrarios y a compaņeros
			
			
			//Si esta bloqueado se zafa de el(amigo)
			if (myRobotAPI.isBlocking(myRobotAPI.getClosestMate()))
			{
				ayuda.evitarBloqueo(myRobotAPI.getClosestMate(), myRobotAPI);
				myRobotAPI.setDisplayString("Por. (EBC)");
			}
			
			//Si esta bloqueado se zafa de el(Oponente)
			if (myRobotAPI.isBlocking(myRobotAPI.getClosestOpponent()))
			{
				ayuda.evitarBloqueo(myRobotAPI.getClosestOpponent(), myRobotAPI);
				myRobotAPI.setDisplayString("Por. (EBO)");
			}
			
			//Evita colision compaņeros
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
			if( (myRobotAPI.getPosition().x * SIDE < 1.37 || Math.abs(myRobotAPI.getPosition().y) > 0.25))
			{
				myRobotAPI.setSteerHeading(ayuda.irAPosicionParando(myRobotAPI.toFieldCoordinates(ourgoal), myRobotAPI,0.01));
				myRobotAPI.setSpeed(1.0);
				myRobotAPI.setDisplayString("Portero (areaaaaaa)");
			}
			myRobotAPI.setDisplayString("Portero (Out)");
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
			myRobotAPI.setDisplayString("Portero (back)");
		}
		else
		//Si esta fuera del area de gol vuelve
		if( (myRobotAPI.getPosition().x * SIDE < 1.07 || Math.abs(myRobotAPI.getPosition().y) > 0.2222))
		{
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
		}
		else		
		{
			//Para que este entre el balon y la porteria
			if( ball.y > 0)
			{
				Vec2 move=new Vec2((double)SIDE, 8);
				myRobotAPI.setSteerHeading(ayuda.irAPosicionParando(myRobotAPI.toFieldCoordinates(move), myRobotAPI,0.0001));
				myRobotAPI.setDisplayString("y>0");
			}
			else
			{
				Vec2 move=new Vec2((double)SIDE, -8);
				myRobotAPI.setSteerHeading(ayuda.irAPosicionParando(myRobotAPI.toFieldCoordinates(move), myRobotAPI,0.0001));
				myRobotAPI.setDisplayString("y<0");
			}

			if( Math.abs( ball.y) < myRobotAPI.getPlayerRadius() * 0.15)
			{
				myRobotAPI.setSpeed(0);
				myRobotAPI.setDisplayString("speed0");
			}				
			else
			{
				myRobotAPI.setSpeed(1.0);
				myRobotAPI.setDisplayString("speed1");
			}			
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
