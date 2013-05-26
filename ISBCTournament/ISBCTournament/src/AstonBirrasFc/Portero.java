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
		SIDE=myRobotAPI.getFieldSide();
		//Si la pelota esta detras del portero intenta ir a por ella y golpearla hacia fuera
		if( ball.x * SIDE > 0)
		{
			myRobotAPI.setBehindBall(ball);
			myRobotAPI.kick();
			myRobotAPI.setDisplayString("Portero (kick)");
		}
		else
		//Si esta fuera del area de gol vuelve
		if( (Math.abs(ourgoal.x) > myRobotAPI.getPlayerRadius() * 1.4) ||
				 (Math.abs(ourgoal.y) > myRobotAPI.getPlayerRadius() * 4.25) )
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
				Vec2 move=new Vec2((double)SIDE, 7);
				myRobotAPI.setSteerHeading(ayuda.irAPosicionParando(myRobotAPI.toFieldCoordinates(move), myRobotAPI,0.0001));
			}
			else
			{
				Vec2 move=new Vec2((double)SIDE, -7);
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
