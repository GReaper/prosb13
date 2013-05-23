package AstonBirrasFc;

import java.util.SortedMap;

import EDU.gatech.cc.is.util.Vec2;
import teams.ucmTeam.Behaviour;
import teams.ucmTeam.RobotAPI;

public class LocoBola extends Behaviour
{

	private Ayudas ayuda;
	
	private enum Estado{ZONA_A,ZONA_B,ZONA_C}
	
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
		
		Estado zona= zonaBola();
		myRobotAPI.setDisplayString(zona.name()); 
		switch(zona)
		{
			case ZONA_A: bloquearRivalCercano(); break;
			case ZONA_B: irAPorBola(); break;
			case ZONA_C: recolocar();break;
		
		
		}
				
		
		return 0;
	}

	public Estado zonaBola()
	{
		if(ayuda.pelotaEnMiCampo(myRobotAPI))
		{
			Vec2 pelota= myRobotAPI.toFieldCoordinates(myRobotAPI.getBall());
			if(Math.abs(pelota.x)>Math.abs(ayuda.miMitadX(myRobotAPI)))
			{
				return Estado.ZONA_A;
			}
			return Estado.ZONA_B;
			
		}
		else
		{
			return Estado.ZONA_C;
		}
		
		
	}
	
	private void irAPorBola()
	{
		myRobotAPI.setBehindBall(myRobotAPI.getOpponentsGoal());
	}
	
	private void recolocar()
	{
		double x=ayuda.miCuartoX(myRobotAPI);
		double y= myRobotAPI.getBall().y;
		Vec2 destino=new Vec2(x, y);
		double angle=ayuda.irAPosicionParando(destino, myRobotAPI,0.005);
		myRobotAPI.setSteerHeading(angle);
		
	}
	
	private void bloquearRivalCercano()
	{
		SortedMap<Double,Vec2> rivales=myRobotAPI.getSortedOpponents();
		double val1=rivales.firstKey();
		Vec2 rivalCercano=rivales.get(val1);
		double angle=ayuda.irAPosicionNoParando(rivalCercano, myRobotAPI);
		myRobotAPI.setSteerHeading(angle);		
		
	}

}
