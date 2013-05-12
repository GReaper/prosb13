package AstonBirrasFc;

import EDU.gatech.cc.is.util.Vec2;
import teams.ucmTeam.Behaviour;
import teams.ucmTeam.RobotAPI;

public class LocoBola extends Behaviour
{

	private Ayudas ayuda;
	@Override
	public void configure() 
	{
		// TODO Auto-generated method stub
		ayuda= new Ayudas();
	}

	@Override
	public void end() 
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onInit(RobotAPI arg0) 
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onRelease(RobotAPI arg0)
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public int takeStep() 
	{
		
		if(ayuda.pelotaEnMiCampo(myRobotAPI))
		{	
			
			if(ayuda.cercano(myRobotAPI.getPosition(), myRobotAPI.toFieldCoordinates(myRobotAPI.getBall()), myRobotAPI, 0.15) || myRobotAPI.closestToBall())
			{
				myRobotAPI.setSpeed(0.4);
				myRobotAPI.setBehindBall(myRobotAPI.getOpponentsGoal());
				if (myRobotAPI.canKick())
					 myRobotAPI.kick();
				
				myRobotAPI.setDisplayString("Detras" );
			}
			else
			{
			
				myRobotAPI.setSpeed(1000);
				double angulo=ayuda.irAPosicionParando(myRobotAPI.toFieldCoordinates(myRobotAPI.getBall()), myRobotAPI,0.05);
				
					
				myRobotAPI.setSteerHeading(angulo);	
				if (myRobotAPI.canKick() && myRobotAPI.alignedToBallandGoal())
					 myRobotAPI.kick();
				
				
				myRobotAPI.setDisplayString("Loco" );
				
			}
			 
		}
		else
		{
			double micuarto=ayuda.miCuartoX(myRobotAPI);
			Vec2 destino=ayuda.getMiCentro(myRobotAPI);// new Vec2(micuarto, myRobotAPI.toFieldCoordinates(myRobotAPI.getBall()).y);
			//destino=myRobotAPI.toEgocentricalCoordinates(destino);
			myRobotAPI.setSpeed(1000);
			double angulo=ayuda.irAPosicionParando(destino, myRobotAPI,0.01);
			
			myRobotAPI.setSteerHeading(angulo);	
			
			
			myRobotAPI.setDisplayString("A centro");
			
		}
		
		/*
		myRobotAPI.setSpeed(1000);
		double angulo=ayuda.irAPosicion(new Vec2(0, 0.3), myRobotAPI);
		if(angulo==Double.MAX_VALUE)
		{
			myRobotAPI.setSpeed(0);
		}
		else
		{
			myRobotAPI.setSteerHeading(angulo);	
		}
		*/
		return myRobotAPI.ROBOT_OK;
	}



}
