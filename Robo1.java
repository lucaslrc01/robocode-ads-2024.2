package DeLeon;
import robocode.*;

public class Robo1 extends Robot
{
	public void run() {
		while (true) {
			turnRadarLeft(360);
		} 
	}
	
	public void onScannedRobot (ScannedRobotEvent robo) {
		double angulo = robo.getBearing();
		turnRight(angulo);
		
		if (getEnergy() >= 80) {
			fire(3);
		} else if (getEnergy() >= 40) {
			fire(2);
		} else if (getEnergy() >= 10) {
			fire (1);
		} else {
			doNothing();
		}
	}
	
	public void onHitByBullet (HitByBulletEvent i) {
		double larguraArena = getBattleFieldWidth();
		back(larguraArena / 5);
		turnRight(30);
		ahead(larguraArena / 6);
		
	}
	
	public void onHitWall (HitWallEvent i) {
		double larguraArena = getBattleFieldWidth();
		ahead(larguraArena / 3);
	}
}
	