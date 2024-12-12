package Avancado;
import robocode.*;

public class RoboAvancado1 extends AdvancedRobot {

    public void run() {
       while (true) {
            turnRadarRight(360);

            if (proxParede()) {
                turnRight(90);  
                ahead(100);    
            } else {
                ahead(50);    
            }
        }
    }

     public void onScannedRobot(ScannedRobotEvent inimigo) {
        double distancia = inimigo.getDistance();
        double anguloParaOrbitar = inimigo.getBearing() + 90;
        
	setTurnRight(anguloParaOrbitar);
        setAhead(50);
        
	double anguloParaInimigo = getHeading() + inimigo.getBearing();
        double anguloArma = robocode.util.Utils.normalRelativeAngleDegrees(anguloParaInimigo - getGunHeading());
        
	setTurnGunRight(anguloArma);

        if (distancia > 100) {
            setAhead(50);
        } else {
            setBack(50);
        }

        if (getGunHeat() == 0) {
            fire(2);
        }

        execute();
    }
	
	private boolean proxParede() {
       
        double x = getX();
        double y = getY();


        double DistSegura = 60;
        return (x < DistSegura || x > getBattleFieldWidth() - DistSegura || y < DistSegura || y > getBattleFieldHeight() - DistSegura);
    }

}
