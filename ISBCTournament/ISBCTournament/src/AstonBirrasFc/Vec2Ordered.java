package AstonBirrasFc;

import EDU.gatech.cc.is.util.Vec2;


public class Vec2Ordered implements Comparable<Vec2Ordered> 
{
	private Vec2 posicion;
	private int field;
	private int numeroJugador;
	
	public Vec2Ordered(Vec2 pos,int field,int numJugador)
	{
		this.posicion=pos;
		this.field=field;
		this.numeroJugador=numJugador;
	}
	
	public Vec2 getPosicion()
	{
		return posicion;
	}
	
	public int getId()
	{
		return numeroJugador;
	}
	
	
	
	@Override
	public int compareTo(Vec2Ordered o) 
	{
		
		Vec2 otro=o.getPosicion();
		
		if(field==1)
		{//derecha
			if(otro.x> posicion.x)
			{
				return -1;
			}
			if(otro.x< posicion.x)
			{
				return 1;
			}
			
		}
		else
		{//izquierda
			
			if(otro.x< posicion.x)
			{
				return -1;
			}
			if(otro.x> posicion.x)
			{
				return 1;
			}
		}
		
		
		return 0;
	}

	
	
}
