import robocode.*;
import java.awt.*;

public class RoboAvancado1 extends AdvancedRobot {
    
    public void run() {
    
        setAdjustRadarForGunTurn(true);
        setAdjustGunForRobotTurn(true);

        while (true) {        
            setTurnRadarRight(360);
            execute();
        }
    }

    
    public void onScannedRobot(ScannedRobotEvent inimigo) {
        double distance = inimigo.getDistance(); 
        double angleToEnemy = inimigo.getBearing(); 

 
        double armaAngulo = getHeading() + angleToEnemy - getGunHeading();
        while (armaAngulo > 180) {
			armaAngulo -= 360;
		}
  		while (armaAngulo < -180) {
			armaAngulo += 360;
		}
		
		setTurnGunRight(armaAngulo);

        double firePower = Math.min(400 / distance, 3);
        if (getGunHeat() == 0 && getEnergy() > firePower) {
            setFire(firePower);
        }

        setTurnRight(angleToEnemy + 90); 
        setAhead(150); 
    }
}
