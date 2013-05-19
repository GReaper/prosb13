package AstonBirrasFc;


import teams.ucmTeam.RobotAPI;
import EDU.gatech.cc.is.util.Vec2;

public class Ayudas 
{

	/**
	 * Devuelve el angulo que hay que poner para llegar a un sitio
	 * 
	 * @param destino en coordenadas globales
	 * @param myRobotAPI
	 * @return el angulo a poner.Si estamos en el sitio pone la vel a 0
	 */
public double irAPosicionParando(Vec2 destino,RobotAPI myRobotAPI,double parada)
	{
		
		Vec2 myPos=myRobotAPI.getPosition();
		Vec2 resta=(Vec2) destino.clone();
		resta.sub(myPos);	
		
		if(cercano(myPos,destino,myRobotAPI,parada))
		{
			myRobotAPI.setSpeed(0);
			if(myRobotAPI.getFieldSide()==RobotAPI.WEST_FIELD)
			{
				return 0;
			}
			else
			{
				return degToRad(180);
			}
		}
		
		
		return resta.t;
		
	}
	
	public double irAPosicionNoParando(Vec2 destino,RobotAPI myRobotAPI)
	{
		
		Vec2 myPos=myRobotAPI.getPosition();
		Vec2 resta=(Vec2) destino.clone();
		resta.sub(myPos);	

		
		return resta.t;
		
	}
	
	/**
	 * Determina si dos puntos estan muy cerca(factor de cerca)
	 * @param yo(coordenadas globales)
	 * @param otraPosicion(coordenadas globales)
	 * @param myRobotAPI
	 * @param factor
	 * @return
	 */
	public boolean cercano(Vec2 yo, Vec2 otraPosicion,RobotAPI myRobotAPI, double factor)
	{
		double arriba=RobotAPI.getUpperFieldBound();
		double x= arriba*factor;
		double derecha=RobotAPI.getRightFieldBound();
		double y= derecha*factor;
		
		
		return yo.x>otraPosicion.x-x && yo.x<otraPosicion.x+x
				&&
				yo.y>otraPosicion.y-y && yo.y<otraPosicion.y+y;
	}
	public double radToDeg(double rad)
	{
		return (rad*360)/(Vec2.PI2);
	}
	public double degToRad(double deg)
	{
		return (Vec2.PI2*deg)/360;
	}
	/**
	 * Dice si la pelota esta en mi campo
	 * @param myRobotAPI
	 * @return
	 */
	public boolean pelotaEnMiCampo(RobotAPI myRobotAPI)
	{
		Vec2 bola= myRobotAPI.getBall();
		bola=myRobotAPI.toFieldCoordinates(bola);
		int equipo=myRobotAPI.getFieldSide();
		
		if((equipo==RobotAPI.WEST_FIELD && bola.x<=0)  || (equipo==RobotAPI.EAST_FIELD && bola.x>=0))
		{
			return true;
		}
		return false;
		
	}
	/**
	 * Calcula cual es la mitad del campo en la X
	 * @param myRobotAPI
	 * @return
	 */
	public double miMitadX(RobotAPI myRobotAPI)
	{
		double derecha=RobotAPI.getRightFieldBound();
		derecha*=0.5;
		int equipo=myRobotAPI.getFieldSide();
		
			
		if(equipo==RobotAPI.WEST_FIELD)
		{
			return -derecha;
			
		}
		else
		{
			return derecha;			
		}
	}
	
	/**
	 * Calcula cual es el cuarto del campo en la X
	 * @param myRobotAPI
	 * @return
	 */
	public double miCuartoX(RobotAPI myRobotAPI)
	{
		double derecha=RobotAPI.getRightFieldBound();
		derecha*=0.25;
		int equipo=myRobotAPI.getFieldSide();
		
			
		if(equipo==RobotAPI.WEST_FIELD)
		{
			return -derecha;
			
		}
		else
		{
			return derecha;			
		}
	}
	
	
	public Vec2 getMiCentro(RobotAPI myRobotAPI)
	{
		double derecha=RobotAPI.getRightFieldBound()*0.5;
		int equipo=myRobotAPI.getFieldSide();
		//double arriba=RobotAPI.getUpperFieldBound();
		if(equipo==RobotAPI.WEST_FIELD)
		{
			derecha*=-1;
		}
		return new Vec2(derecha, 0);
	}

	public boolean alguienConBola(RobotAPI myRobotAPI) 
	{
		Vec2[] enemigos= myRobotAPI.getOpponents();
		Vec2[] amigos= myRobotAPI.getTeammates();
		Vec2 bola= myRobotAPI.getBall();
		
		for(int i=0;i<enemigos.length;i++)
		{
			if(cercano(enemigos[i], bola, myRobotAPI, 0.05))
			{
				return true;
			}
		}
		for(int i=0;i<amigos.length;i++)
		{
			if(cercano(amigos[i], bola, myRobotAPI, 0.05))
			{
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Devuelve el angulo que hay que poner para llegar a un sitio
	 * 
	 * @param destino en coordenadas globales
	 * @param myRobotAPI
	 * @return el �ngulo necesario
	 */
	public double anguloNecesario(Vec2 destino,RobotAPI myRobotAPI,double parada)
	{		
		Vec2 myPos=myRobotAPI.getPosition();
		Vec2 resta=(Vec2) destino.clone();
		resta.sub(myPos);	
		if(cercano(myPos,destino,myRobotAPI,parada))
		{
			myRobotAPI.setSpeed(0);
			if(myRobotAPI.getFieldSide()==RobotAPI.WEST_FIELD)
			{
				return 0;
			}
			else
			{
				return degToRad(180);
			}
		}	
		
		return resta.t;		
	}
	
	/**
	 * Determina si dos puntos están muy cerca. El factor de cercanía dependera, para mayor
	 * precisión, del radio de jugador.
	 * @param origen (coordenadas globales)
	 * @param destino (coordenadas globales)
	 * @param factor. Medida de margen para cercanía
	 * @return booleano indicando si están cerca o no
	 */
	public boolean cercanoRadio(Vec2 origen, Vec2 destino, double factor)
	{
		// Buscar en circunferencia
		return origen.x > destino.x-factor && 
				origen.x < destino.x+factor &&
				origen.y > destino.y-factor && 
				origen.y < destino.y+factor;
	}
	
	/**
	 * Devuelve el angulo necesario para llegar a un punto
	 * 
	 * @param destino en coordenadas globales
	 * @param myRobotAPI
	 * @return el angulo necesario en radianes
	 */
	public double anguloDestino(Vec2 destino,RobotAPI myRobotAPI)
	{		
		Vec2 myPos = myRobotAPI.getPosition();
		Vec2 resta=(Vec2) destino.clone();
		resta.sub(myPos);			
		return resta.t;		
	}
}
