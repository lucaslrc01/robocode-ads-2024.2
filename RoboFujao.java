package Corredores;
import robocode.*;

public class RoboFujao extends Robot
{
public void run() {
        
        setAdjustRadarForGunTurn(true);
        setAdjustGunForRobotTurn(true);

        while (true) {
            turnRadarRight(Double.POSITIVE_INFINITY);
           
        }
    }

    @Override
    public void onScannedRobot(ScannedRobotEvent evento) {
        double distancia = evento.getDistance();
        double anguloParaInimigo = evento.getBearing();
        double velocidadeInimigo = evento.getVelocity();

        if (distancia < 150 && velocidadeInimigo < 3) {
            double anguloMira = getHeading() + anguloParaInimigo - getGunHeading();
            anguloMira = normalizarAngulo(anguloMira);
            turnGunRight(anguloMira);

            double forcaTiro = Math.min(3, 400 / distancia);
            if (getGunHeat() == 0 && getEnergy() > forcaTiro) {
                fire(forcaTiro);
            }
        }

        if (distancia < 300) {
            double anguloFuga = anguloParaInimigo + 180;
            turnRight(normalizarAngulo(anguloFuga));
            ahead(150);
        }

        evitarParedes();
    }

    @Override
    public void onHitByBullet(HitByBulletEvent evento) {
        turnRight(90 - evento.getBearing());
        ahead(100);
    }

    @Override
    public void onHitWall(HitWallEvent evento) {
        back(50);
        turnRight(90);
        ahead(100);
    }

    private void evitarParedes() {
        double margemParede = 50;
        double larguraCampo = getBattleFieldWidth();
        double alturaCampo = getBattleFieldHeight();
        double x = getX();
        double y = getY();

        if (x < margemParede || x > larguraCampo - margemParede || y < margemParede || y > alturaCampo - margemParede) {
            turnRight(90);
            ahead(100);
        }
    }

    private double normalizarAngulo(double angulo) {
        while (angulo > 180) angulo -= 360;
        while (angulo < -180) angulo += 360;
        return angulo;
    }
}
