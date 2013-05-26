package AstonBirrasFc;

import java.util.ArrayList;

import teams.ucmTeam.Behaviour;
import teams.ucmTeam.RobotAPI;
import EDU.gatech.cc.is.util.Vec2;

public class Pepe extends Behaviour
{
	private Ayudas ayudas;
	
	public void configure() 
	{ 
		ayudas = new Ayudas();
	}

	public int takeStep() 
	{	
		//Cojo donde estan los jugadores del equipo contrario
		int side=myRobotAPI.getFieldSide();
		Vec2[] opponents=myRobotAPI.getOpponents();
		ArrayList<Integer> cerca=new ArrayList<Integer>();
		for(int i=0; i< opponents.length;i++)
		{
			Vec2 opponent=opponents[i];
			Vec2 opponentGlobal=myRobotAPI.toFieldCoordinates(opponent);
			//Miro cuales cumplen que esten en el area de mi equipo
			//Los que esten en el area los guardo en un Vec2
			if(side== myRobotAPI.EAST_FIELD)
			{//Juego en el equipo del este
				
				if(opponentGlobal.x >=0)
				{
					//El oponente esta en mi lado del campo
					cerca.add(i);
				}
				
			}
			else
			{//Juego en el equipo del oeste
				
				if(opponentGlobal.x <=0)
				{
					cerca.add(i);
				}
			}			
		}
		
		//En este punto ya se cuales son los que estan en mi campo
		//Si el arraylist no es vacio, es que hay algun jugador dentro de mi campo
		//Sino me quedo en el medio esperando y evitando contrarios
		if(!cerca.isEmpty())
		{
			double distanciaComparar=Double.MAX_VALUE;
			int robotCerca=0;
			for(int i=0;i< cerca.size();i++)
			{
				Vec2 posicionContrario=opponents[cerca.get(i)];
				double distancia=ayudas.distanciaEntre(myRobotAPI.getPosition(), posicionContrario);
				if(distancia < distanciaComparar)
				{
					distanciaComparar=distancia;
					robotCerca=cerca.get(i);
				}
			}
			//En este punto tengo el robot que esta a menos distancia y en mi area
			//Giro el centro hacia el he intento ir a bloquearlo
			double angulo=ayudas.anguloDestino(opponents[robotCerca], myRobotAPI);
			myRobotAPI.setSteerHeading(angulo);
			myRobotAPI.setSpeed(1.0);
			myRobotAPI.setDisplayString("Pepe(Bloq)");
			return myRobotAPI.ROBOT_OK;
		}
		else
		{
			//Me quedo en el cuarto de campo esperando a algun contrario
			//Evita bloquearse con compañeros
			ayudas.evitaColision(myRobotAPI.getClosestMate(), myRobotAPI);
			
			//Coje los tres cuartos de campo y se queda cerca de ese punto
			Double thirdSize = (myRobotAPI.getRightFieldBound() - myRobotAPI.getLeftFieldBound()) / 3;
			// Check if the player is in the first third of the field
			if (!(myRobotAPI.getPosition().x * myRobotAPI.getFieldSide() >= thirdSize))
			{
				// 1.- Set steering
				Vec2 dest = myRobotAPI.getOurGoal();
				double angle = ayudas.anguloDestino(dest, myRobotAPI);			
				myRobotAPI.setSteerHeading(angle);	
				
				// 2.- Increase speed
				myRobotAPI.setSpeed(1000);

				// Set displayed text
				myRobotAPI.setDisplayString("Pepe (Wait)");
				
				// Return
				return myRobotAPI.ROBOT_OK;
			}
		}
		 
		return myRobotAPI.ROBOT_OK;
	}

	public void onInit(RobotAPI r) 
	{
	}

	public void onRelease(RobotAPI r) { }

	public void end() { }
}
