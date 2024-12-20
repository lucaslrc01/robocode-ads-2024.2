package Blackpanter;
import robocode.*;
import java.awt.Color;
import java.util.Random;

public class Wakanda extends AdvancedRobot {

    // Distâncias e variáveis de controle
    private static final double MOVE_DISTANCE = 200;  // Distância maior para avançar
    private static final double BACK_DISTANCE = 100;   // Distância maior para retroceder
    private static final double WALL_MARGIN = 50;     // Margem para evitar as paredes

    // Variáveis para controle de estado
    private boolean isEvading = false;  // Flag para verificar se está em evasão
    private double lastBulletBearing = 0;  // Direção do último tiro que atingiu o robô

    /**
     * run: Comportamento principal do robô
     */
    public void run() {
        setColors(Color.black, Color.green, Color.green); // Corpo, canhão, radar

        while (true) {
            // Se for atingido, entra em evasão
            if (isEvading) {
                evade();  // Evita o inimigo
            } else {
                moveAndScan();  // Move e escaneia os inimigos
            }

            // O radar está sempre girando 360 graus para procurar inimigos
            setTurnRadarRight(360);  
            execute();  // Executa as ações
        }
    }

    /**
     * onScannedRobot: O que fazer quando detectar outro robô
     */
    public void onScannedRobot(ScannedRobotEvent e) {
        double distance = e.getDistance();  // Distância do inimigo
        double angle = e.getBearing();     // Ângulo em relação ao robô

        // Ajusta a direção do canhão
        double gunTurnAngle = normalizeBearing(getHeading() + angle - getGunHeading());
        setTurnGunRight(gunTurnAngle);

        // Define a força de disparo com base na distância
        double firePower = Math.min(400 / distance, 3);
        if (getGunHeat() == 0 && getEnergy() > firePower) {
            setFire(firePower);  // Dispara se possível
        }

        // Não se aproxima muito do inimigo, apenas escaneia e atira
        setTurnRight(angle + 90);  // Gira em direção ao inimigo e se move para a direita
        setAhead(MOVE_DISTANCE);  // Move-se para frente
    }

    /**
     * onHitByBullet: O que fazer quando o robô é atingido por um tiro
     */
    public void onHitByBullet(HitByBulletEvent e) {
        lastBulletBearing = e.getBearing();  // Armazena a direção do último disparo
        isEvading = true;  // Ativa o modo de evasão
    }

    /**
     * onHitWall: O que fazer quando o robô bate em uma parede
     */
    public void onHitWall(HitWallEvent e) {
        // Ao bater na parede, o robô se vira para desviar e se afasta
        double bearing = e.getBearing();
        turnRight(-bearing);  // Gira para desviar
        ahead(MOVE_DISTANCE);  // Move para longe da parede
    }

    /**
     * evade: Método para fugir do inimigo quando atingido
     */
    private void evade() {
        // Gira 180 graus em relação à direção do último disparo
        turnRight(lastBulletBearing + 180);  // Gira para longe da direção do disparo

        // Aumenta a velocidade para escapar mais rápido
        setMaxVelocity(8);  // Define a velocidade máxima para uma fuga rápida

        // Move-se para longe
        ahead(MOVE_DISTANCE * 2);  // Aumenta a distância para escapar

        // Realiza uma rotação aleatória para dificultar a mira do inimigo
        Random rand = new Random();
        turnRight(rand.nextInt(90) - 45);  // Gira aleatoriamente entre -45 e 45 graus

        // Após a evasão, volta ao estado normal
        isEvading = false;

        // Restaura a velocidade para movimento normal
        setMaxVelocity(4);  // Restaura a velocidade padrão
    }

    /**
     * moveAndScan: Método para movimentação e escaneamento constantes
     */
    private void moveAndScan() {
        // Se o robô estiver perto das paredes, evita-as
        double x = getX();
        double y = getY();
        double battlefieldWidth = getBattleFieldWidth();
        double battlefieldHeight = getBattleFieldHeight();

        // Verifica se está perto das paredes
        if (x < WALL_MARGIN || x > battlefieldWidth - WALL_MARGIN || y < WALL_MARGIN || y > battlefieldHeight - WALL_MARGIN) {
            // Gira para se afastar da parede
            setTurnRight(90);
            setAhead(MOVE_DISTANCE);  // Move-se mais rápido para longe da parede
        } else {
            // Movimento aleatório se estiver longe das paredes
            Random rand = new Random();
            if (rand.nextBoolean()) {
                setAhead(MOVE_DISTANCE * 2);  // Move-se mais rápido para frente
            } else {
                setBack(BACK_DISTANCE);  // Move-se mais rápido para trás
            }
        }
    }

    /**
     * Normaliza o ângulo para o intervalo [-180, 180]
     */
    public double normalizeBearing(double angle) {
        while (angle > 180) {
            angle -= 360;
        }
        while (angle <= -180) {
            angle += 360;
        }
        return angle;
    }

    /**
     * onWin: O que fazer quando o robô ganha a batalha
     */
    public void onWin(WinEvent e) {
        // Exibe uma mensagem quando o robô ganha
        System.out.println("Wakanda venceu a batalha!");
    }
}
