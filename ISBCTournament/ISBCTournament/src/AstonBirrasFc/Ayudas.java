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
		//Vec2 myPos = myRobotAPI.getPosition();
		/*Vec2 resta=(Vec2) destino.clone();
		resta.sub(myPos);			
		return resta.t;	*/
		//double angle = /*(Math.toRadians(360) - myPos.t) +*/ Math.atan2(destino.y - myPos.y, destino.x - myPos.x);
		//angle += Math.PI/2.0;
		/*if(angle < 0)
	    {
	        angle += 360;
	    }*/

	    return myRobotAPI.normalizeZero(myRobotAPI.toEgocentricalCoordinates(destino).t);//angle;
		//return myRobotAPI.toEgocentricalCoordinates(destino).t;//angle;
	}

	/**
	 * Coloca el ángulo del jugador para evitar colisión con el jugador dado
	 * 
	 * @param jugador a evitar (coordenadas egocentricas)
	 * @return 
	 */
	public void evitaColision(Vec2 jugador, RobotAPI myRobotAPI)
	{
		double angle = 0;
		if (jugador.x <= 0)
		{
			angle = myRobotAPI.normalizeZero(myRobotAPI.normalizeZero(jugador.t) + 2*Math.PI);
		}
		else if (jugador.y <= 0)
		{
			angle = myRobotAPI.normalizeZero(myRobotAPI.normalizeZero(jugador.t) + Math.PI);
		}
		else
		{
			angle = myRobotAPI.normalizeZero(myRobotAPI.normalizeZero(jugador.t) - Math.PI);
		}		
		//double angle = jugador.t + Math.PI;
		myRobotAPI.setSteerHeading(angle);
		myRobotAPI.setSpeed(1000);
	}

	/**
	 * Coloca el ángulo del jugador para salir del bloqueo con el jugador dado
	 * 
	 * @param jugador a evitar (coordenadas egocentricas)
	 * @return 
	 */
	public void evitarBloqueo(Vec2 jugador, RobotAPI myRobotAPI)
	{
		double angle = 0;
		if (jugador.y <= 0)
		{
			angle = myRobotAPI.normalizeZero(myRobotAPI.normalizeZero(jugador.t) + Math.PI);
		}
		else
		{
			angle = myRobotAPI.normalizeZero(myRobotAPI.normalizeZero(jugador.t) - Math.PI);
		}	
		myRobotAPI.setSteerHeading(angle);
	}
	
	/**
	 * Devuelve la posición del oponente más cercano a la portería
	 * 
	 * @param myRobotAPI
	 * @return Vec2 con las coordenadas globales
	 */
	public Vec2 porteroEnemigo(RobotAPI myRobotAPI)
	{		
		// Tomar todos los enemigos (ego)
		Vec2[] enms = myRobotAPI.getOpponents();
		// Tomar portería enemiga (ego)
		Vec2 port = myRobotAPI.getOpponentsGoal();

		// Vec2 con candidato inicial
		Vec2 cand = myRobotAPI.toFieldCoordinates(enms[0]);
		double dist = distanciaEntre(cand, myRobotAPI.toFieldCoordinates(port));
		for (int i=1;i < enms.length; i++)
		{
			if (distanciaEntre(myRobotAPI.toFieldCoordinates(enms[i]), myRobotAPI.toFieldCoordinates(port)) < dist)
			{
				cand = myRobotAPI.toFieldCoordinates(enms[i]);
			}			
		}
		
		return cand;		
	}
	
	/**
	 *  Calcula la distancia entre dos puntos
	 *  
	 *  @param origen
	 *  @param destino
	 *  @return distancia entre ambos
	 * 
	 */
	public double distanciaEntre(Vec2 o, Vec2 d)
	{
		return Math.sqrt(Math.pow((d.x - o.x), 2) + Math.pow((d.y - o.y), 2));
	}
}
