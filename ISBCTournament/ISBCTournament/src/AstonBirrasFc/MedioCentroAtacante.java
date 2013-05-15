package AstonBirrasFc;

import java.util.Iterator;
import java.util.Set;
import java.util.SortedMap;

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
		myRobotAPI.setDisplayString(estado.toString());
		double nuevaX;
		double angulo;
		int miZona;
		int zonaPelota;
		Vec2 destino;
		Vec2[] oponentes;
		Vec2 porteria;
		

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
				
				nuevaX=ayuda.miCuartoX(myRobotAPI);
				
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
					if(zonaPelota==-1)
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
						//myRobotAPI.setBehindBall(myRobotAPI.getOpponentsGoal());
						Vec2 amigoSolo=amigoSolo();
						if(amigoSolo!=null)
						{
							myRobotAPI.passBall(amigoSolo);
							estado=Estados.SINPELOTA;
						}
						else
						{
							/*
							if(tengoDisparo())
							{
								estado=Estados.DISPARO;
							}
							else
							{
								//avanzar a la zona mas lejana de los rivales mas cercanos
								Vec2 puntoRegate=puntoRegate();
								double anguloDisparo=calcularAnguloDisparo(puntoRegate);
								myRobotAPI.setSteerHeading(anguloDisparo);
								
							}
							*/
							estado=Estados.DISPARO;
							
							
						}
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
	
	private Vec2 amigoSolo()
	{
		Vec2[] amigos=myRobotAPI.getTeammates();
		Vec2[] rivales=myRobotAPI.getOpponents();
		Vec2 yo=myRobotAPI.getPosition();
		
		Vec2 solo=new Vec2(Double.MIN_VALUE, Double.MIN_VALUE);
		boolean cambio=false;
		for(int i=0;i<amigos.length;i++)
		{
			for(int j=0;j<rivales.length;j++)
			{
				if(ayuda.cercano(amigos[i], rivales[j], myRobotAPI, 0.1))
				{
					if(amigos[i].x>yo.x)
					{
						if(amigos[i].x>solo.x)
						{
							solo=amigos[i];
							cambio=true;
						}
					}
				}
			}
		}
		if(cambio)		
			return solo;
		else
			return null;
	}
	
	private boolean tengoDisparo() 
	{

		Vec2 porteria=myRobotAPI.getOpponentsGoal();
		Vec2 bola=myRobotAPI.getBall();
		Vec2 yo=myRobotAPI.getPosition();
		
		if(myRobotAPI.inVisualField(porteria, bola, yo))
		{
			return true;
		}
		return false;
	}
	
	private Vec2 puntoRegate()
	{
		/*
		SortedMap<Double, Vec2> rivales=myRobotAPI.getSortedOpponents();
	
		Iterator<Double> distancias=rivales.keySet().iterator();
		
		Vec2[] delante= new Vec2[5];
		int metidos=0;
		
		while(distancias.hasNext())
		{
			Double distancia=distancias.next();
			
			Vec2 rival= rivales.get(distancia);
			
			
			
		}
		*/
		
		Vec2[] rivales=myRobotAPI.getOpponents();
		Vec2 yo= myRobotAPI.getPosition();
		
		Vec2[] rivalAConsiderar= new Vec2[3];
		int metidos=0;
		
		for(int i=0;i<rivales.length;i++)
		{
			Vec2 rival= myRobotAPI.toFieldCoordinates(rivales[i]);
			
			if(ayuda.estaDelante(myRobotAPI,rival))
			{
				rivalAConsiderar[metidos]= rival;
				metidos++;
				if(metidos==3)
				{
					metidos--;
				}
			}
			
			
			
			
		}
		
		if(metidos==0 || metidos==1 || metidos==2)
		{
			return myRobotAPI.toFieldCoordinates(myRobotAPI.getOpponentsGoal());
		}
		
		//cuentas
		double A=((rivalAConsiderar[0].y-rivalAConsiderar[2].y)*(rivalAConsiderar[0].x-Math.pow(rivalAConsiderar[1].x, 2)+Math.pow(rivalAConsiderar[0].y, 2))-rivalAConsiderar[1].y)/(rivalAConsiderar[0].y-rivalAConsiderar[1].y);
		double B=Math.pow(rivalAConsiderar[0].x, 2)-Math.pow(rivalAConsiderar[2].x, 2)+Math.pow(rivalAConsiderar[0].y, 2)+Math.pow(rivalAConsiderar[2].y, 2);
		
		double xdestino=((B+A)*(rivalAConsiderar[0].y-rivalAConsiderar[1].y))/
				((2*(rivalAConsiderar[0].x-rivalAConsiderar[2].x)*(rivalAConsiderar[0].y-rivalAConsiderar[1].y))-(rivalAConsiderar[0].x-rivalAConsiderar[1].x));
		
		double ydestino=((Math.pow(rivalAConsiderar[0].x, 2))-(Math.pow(rivalAConsiderar[1].x, 2))-(2*xdestino*(rivalAConsiderar[0].x-rivalAConsiderar[1].x))+(Math.pow(rivalAConsiderar[0].y, 2))-(Math.pow(rivalAConsiderar[1].y, 2)))/(2*(rivalAConsiderar[0].y-rivalAConsiderar[1].y));
		
		return new Vec2(xdestino, ydestino);
	}

	
	
}
