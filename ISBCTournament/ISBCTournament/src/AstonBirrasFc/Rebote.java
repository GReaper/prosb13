package AstonBirrasFc;

import EDU.gatech.cc.is.util.Vec2;
import teams.ucmTeam.Behaviour;
import teams.ucmTeam.RobotAPI;

public class Rebote extends Behaviour
{

	private Ayudas ayuda;
	/*
	private enum Estados{SIN_BOLA, IR_POR_BOLA,REBOTARPOSITIVO,AVANCEDESTINOPOSITIVO,
		REBOTARNEGATIVO,AVANCEDESTINONEGATIVO,LLEGADA};
	*/
	
	private enum Estados{SIN_BOLA,CON_BOLA,REBOTEPOSITIVO,REBOTENEGATIVO}
	
	private float anguloGolpeo;
	
	private Estados estado;
	@Override
	public void configure() 
	{
		// TODO Auto-generated method stub
		ayuda= new Ayudas();
		estado=Estados.SIN_BOLA;
		anguloGolpeo=45;
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
/*
	@Override
	public int takeStep() 
	{
		double angulo;
		double b;
		Vec2 destino;
		myRobotAPI.setDisplayString(estado.toString());
		switch(estado)
		{
			case SIN_BOLA:
				myRobotAPI.setSpeed(1000);
				angulo=ayuda.irAPosicionParando(myRobotAPI.toFieldCoordinates(myRobotAPI.getBall()), myRobotAPI,0.05);					
				myRobotAPI.setSteerHeading(angulo);	
		
				estado=Estados.IR_POR_BOLA;
				break;
				
			case IR_POR_BOLA:
				if(ayuda.cercano(myRobotAPI.getPosition(), myRobotAPI.toFieldCoordinates(myRobotAPI.getBall()), myRobotAPI, 0.1)
						||myRobotAPI.closestToBall() )
				{
					myRobotAPI.setBehindBall(myRobotAPI.getOpponentsGoal());
					if(myRobotAPI.getPosition().x>=0)
						estado=Estados.REBOTARPOSITIVO;
					else
						estado=Estados.REBOTARNEGATIVO;
				}
				else
				{
					if(ayuda.cercano(myRobotAPI.getPosition(), myRobotAPI.toFieldCoordinates(myRobotAPI.getBall()), myRobotAPI, 0.1)
							|| myRobotAPI.closestToBall())
					{
						myRobotAPI.setBehindBall(myRobotAPI.getOpponentsGoal());
					}
					else
					{
						myRobotAPI.setSpeed(1000);
						angulo=ayuda.irAPosicionParando(myRobotAPI.toFieldCoordinates(myRobotAPI.getBall()), myRobotAPI,0.05);					
						myRobotAPI.setSteerHeading(angulo);	
					}
					
				}
				break;
			case REBOTARPOSITIVO:
					if(myRobotAPI.canKick())
					{
						myRobotAPI.setSteerHeading(ayuda.degToRad(anguloGolpeo));	
						myRobotAPI.kick();
						estado=Estados.AVANCEDESTINOPOSITIVO;						
					}
					else
					{
						myRobotAPI.setBehindBall(myRobotAPI.getOpponentsGoal());
					}
				break;
				
			case REBOTARNEGATIVO:
				if(myRobotAPI.canKick())
				{
					myRobotAPI.setSteerHeading(ayuda.degToRad(-anguloGolpeo));	
					myRobotAPI.kick();
					estado=Estados.AVANCEDESTINONEGATIVO;						
				}
				else
				{
					myRobotAPI.setBehindBall(myRobotAPI.getOpponentsGoal());
				}
			break;
				
			case AVANCEDESTINOPOSITIVO:
				
				b= myRobotAPI.getPosition().y*Math.cos(ayuda.degToRad(anguloGolpeo))/Math.sin(ayuda.degToRad(anguloGolpeo));
				
				destino=new Vec2(myRobotAPI.getPosition().x,2*b);
				if(ayuda.cercano(myRobotAPI.getPosition(), destino, myRobotAPI, 0.10))
				{
					myRobotAPI.setBehindBall(myRobotAPI.getOpponentsGoal());
					estado=Estados.LLEGADA;
				}
				else
				{
					angulo=ayuda.irAPosicionParando(myRobotAPI.toFieldCoordinates(myRobotAPI.getBall()), myRobotAPI,0.05);
					myRobotAPI.setSteerHeading(angulo);	
				}
				
				break;
				
				case AVANCEDESTINONEGATIVO:
				
				b= myRobotAPI.getPosition().y*Math.cos(ayuda.degToRad(anguloGolpeo))/Math.sin(ayuda.degToRad(anguloGolpeo));
				
				destino=new Vec2(myRobotAPI.getPosition().x,2*b);
				if(ayuda.cercano(myRobotAPI.getPosition(), destino, myRobotAPI, 0.10))
				{
					myRobotAPI.setBehindBall(myRobotAPI.getOpponentsGoal());
					estado=Estados.LLEGADA;
				}
				else
				{
					angulo=ayuda.irAPosicionParando(myRobotAPI.toFieldCoordinates(myRobotAPI.getBall()), myRobotAPI,0.05);
					myRobotAPI.setSteerHeading(angulo);	
				}
				
				break;
			case LLEGADA:
				if(myRobotAPI.canKick())
				{
					//myRobotAPI.setSteerHeading(ayuda.degToRad(anguloGolpeo));	
					myRobotAPI.kick();
					estado=Estados.SIN_BOLA;						
				}
				else
				{
					myRobotAPI.setBehindBall(myRobotAPI.getOpponentsGoal());
				}
				break;
		}
		
		
		if(ayuda.alguienConBola(myRobotAPI))
		{
			estado=Estados.SIN_BOLA;
		}
		
		return myRobotAPI.ROBOT_OK;
	}

	*/
	
	public int takeStep() 
	{
		double angulo;
		Vec2 myPos;
		myRobotAPI.setDisplayString(estado.toString());
		switch(estado)
		{
		case SIN_BOLA:	
						myRobotAPI.setSpeed(1000);
						angulo=ayuda.irAPosicionParando(myRobotAPI.toFieldCoordinates(myRobotAPI.getBall()), myRobotAPI,0.05);					
						myRobotAPI.setSteerHeading(angulo);	
						if(ayuda.cercano(myRobotAPI.getPosition(), myRobotAPI.toFieldCoordinates(myRobotAPI.getBall()), myRobotAPI, 0.1))
							estado=Estados.CON_BOLA;
						break;
		case CON_BOLA:
					myRobotAPI.setBehindBall(myRobotAPI.getOpponentsGoal());
					if (myRobotAPI.canKick())
					{
						myPos=myRobotAPI.getPosition();
						if(myPos.y>0)
						
							estado=Estados.REBOTEPOSITIVO;
						else
							estado=Estados.REBOTENEGATIVO;
						
					}
					
					if(!ayuda.cercano(myRobotAPI.getPosition(), myRobotAPI.toFieldCoordinates(myRobotAPI.getBall()), myRobotAPI, 0.1))
						estado=Estados.SIN_BOLA;
					
			
			
		case REBOTEPOSITIVO:
						if(myRobotAPI.canKick())
						{
							myRobotAPI.setSteerHeading(ayuda.degToRad(anguloGolpeo));	
							myRobotAPI.kick();
							estado=Estados.SIN_BOLA;						
						}
						else
						{
							myRobotAPI.setBehindBall(myRobotAPI.getOpponentsGoal());
						}
			
		case REBOTENEGATIVO:
						if(myRobotAPI.canKick())
						{
							myRobotAPI.setSteerHeading(ayuda.degToRad(-anguloGolpeo));	
							myRobotAPI.kick();
							estado=Estados.SIN_BOLA;						
						}
						else
						{
							myRobotAPI.setBehindBall(myRobotAPI.getOpponentsGoal());
						}
		
			
						
			
		}
		
		return 0;
		
	}
	

}
