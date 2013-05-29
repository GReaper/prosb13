package AstonBirrasFc;

import EDU.gatech.cc.is.util.Vec2;
import teams.ucmTeam.Behaviour;
import teams.ucmTeam.RobotAPI;

public class MedioCentroAtacante extends Behaviour
{
	
	private enum Estados{SINPELOTA,IRACENTRO,ZONAC,IRPELOTA,ESQUIVO,DISPARO};
	private Estados estado;
	private Ayudas ayuda;

	@Override
	public void configure() 
	{
		this.ayuda=new Ayudas();
		estado=Estados.SINPELOTA;
		
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
		myRobotAPI.setDisplayString("MCO_"+estado.toString());
		double nuevaX;
		double angulo;
		int miZona;
		int zonaPelota;
		Vec2 destino;
		Vec2[] oponentes;
		Vec2 porteria;
		
		
		if(myRobotAPI.opponentBlocking())
		{
			ayuda.evitarBloqueo(myRobotAPI.getClosestOpponent(), myRobotAPI);
		}
		else
		switch(estado)
		{
		default:
			case SINPELOTA: 
							zonaPelota= zonaPelota();
							switch (zonaPelota)
							{
								case -1: this.estado=Estados.ZONAC; break;
								case 0: this.estado=Estados.IRPELOTA; break;
								case 1: this.estado=Estados.IRACENTRO; break;
							}
					break;
		
			case ZONAC: 
				
				nuevaX=ayuda.miMitadX(myRobotAPI);
				
				destino= new Vec2(nuevaX, 0);
				angulo=ayuda.irAPosicionNoParando(destino, myRobotAPI);
				myRobotAPI.setSteerHeading(angulo);
								
				zonaPelota= zonaPelota();
				if(zonaPelota==0)
				{
					myRobotAPI.setBehindBall(myRobotAPI.getOpponentsGoal());
					this.estado=Estados.IRPELOTA;
				}
				else
				{
					if(zonaPelota==1)
					{
						estado=Estados.IRACENTRO;
					}
				}
				
				break;
				
			case IRACENTRO:
				
				//vamos a la posicion central en y y 1/4 en x
				nuevaX=-ayuda.miCuartoX(myRobotAPI);
				
				destino= new Vec2(nuevaX, 0);
				angulo=ayuda.irAPosicionNoParando(destino, myRobotAPI);
				myRobotAPI.setSteerHeading(angulo);
				
				zonaPelota= zonaPelota();
				if(zonaPelota==0)
				{
					myRobotAPI.setBehindBall(myRobotAPI.getOpponentsGoal());
					this.estado=Estados.IRPELOTA;
				}
				else
				{
					if(zonaPelota==-11)
					{
						estado=Estados.ZONAC;
					}
				}
				
				
				break;
			case IRPELOTA:

				myRobotAPI.setBehindBall(myRobotAPI.getOpponentsGoal());
				if(myRobotAPI.closestToBall())
				{
					this.estado=Estados.ESQUIVO;
				}
				miZona=miZona();
				if(miZona==-1)
				{
					estado=Estados.ZONAC;
				}
				else
				{
					if(miZona==1)
					{
						estado=Estados.IRACENTRO;
					}
				}
				
				break;
				
			
				
				
			case ESQUIVO://si la bola esta muy lejos, habra que hacer como con irPelota
				
					if(!myRobotAPI.closestToBall())
					{
						estado=Estados.IRPELOTA;
					}
					else
					{
						estado=Estados.DISPARO;
					}
				break;
				
			case DISPARO:
					oponentes=myRobotAPI.getOpponents();
					porteria= myRobotAPI.getOpponentsGoal();
					Vec2 noDisparo=myRobotAPI.closestTo(oponentes, porteria);
					double anguloDisparo=calcularAnguloDisparo(noDisparo);
					myRobotAPI.setSteerHeading(anguloDisparo);
					if(myRobotAPI.canKick())
					{
						myRobotAPI.kick();
					}
					else
					{
						myRobotAPI.setBehindBall(myRobotAPI.getOpponentsGoal());
					}
					
				break;
				
				
				
				
					
		}
		return myRobotAPI.ROBOT_OK;
	}

	private double calcularAnguloDisparo(Vec2 noDisparo) 
	{
		//TODO calcular a donde debera apuntar el user
		
		
		//se debera apuntar al palo mas lejano al portero
		Vec2 porteriaRival=myRobotAPI.getOpponentsGoal();
		
		porteriaRival=myRobotAPI.toFieldCoordinates(porteriaRival);
		
		Vec2 paloArriba= new Vec2(porteriaRival.x, 0.5);
		Vec2 paloAbajo= new Vec2(porteriaRival.x, -0.5);
		
		Vec2[] palos={paloArriba,paloAbajo};
		
		Vec2 paloCercano=myRobotAPI.closestTo(palos, noDisparo);
		
		if(paloArriba.equals(paloCercano))
		{
			return ayuda.irAPosicionNoParando(paloAbajo, myRobotAPI);//asi se dispara al portero
		}
		else
		{
			return ayuda.irAPosicionNoParando(paloArriba, myRobotAPI);//asi se dispara al portero
		}
		
	}

	private int zonaPelota()
	{

		double derecha=RobotAPI.getRightFieldBound();
		double zonaA=derecha*0.5;
		
		Vec2 pelota= myRobotAPI.toFieldCoordinates(myRobotAPI.getBall());
		
		if(pelota.x<=-zonaA) return -1;
		if(pelota.x>= zonaA) return 1;
		return 0;
		
		
	}

	private int miZona()
	{
	
		double derecha=RobotAPI.getRightFieldBound();
		double zonaA=derecha*0.5;
		
		Vec2 yo= myRobotAPI.getPosition();
		
		if(yo.x<=-zonaA) return -1;
		if(yo.x>= zonaA) return 1;
		return 0;
	}
	
	
}
