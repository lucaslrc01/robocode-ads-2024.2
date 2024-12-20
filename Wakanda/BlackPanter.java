package Wakanda;

import robocode.HitRobotEvent;
import robocode.Robot;
import robocode.ScannedRobotEvent;

import java.awt.*;


public class BlackPanter extends Robot {

    boolean peek; // Não virar se houver um robô ali
    double moveAmount; // Quantidade de movimento

    /**
     * run: Mover-se pelas paredes
     */
    public void run() {
        // Definir as cores
        setBodyColor(Color.black);     // Corpo preto
        setGunColor(Color.black);      // Canhão preto
        setRadarColor(Color.magenta);  // Radar roxo
        setBulletColor(Color.cyan);    // Cor das balas
        setScanColor(Color.cyan);      // Cor da varredura

        // Inicializa moveAmount com o valor máximo possível para este campo de batalha.
        moveAmount = Math.max(getBattleFieldWidth(), getBattleFieldHeight());
        // Inicializa peek como falso
        peek = false;

        // turnLeft para enfrentar uma parede.
        // getHeading() % 90 significa o resto da
        // divisão de getHeading() por 90.
        turnLeft(getHeading() % 90);
        ahead(moveAmount);
        // Gira o canhão 90 graus para a direita.
        peek = true;
        turnGunRight(90);
        turnRight(90);

        while (true) {
            // Olha antes de virar quando o ahead() completar.
            peek = true;
            // Move ao longo da parede
            ahead(moveAmount);
            // Não olhar agora
            peek = false;
            // Gira para a próxima parede
            turnRight(90);
        }
    }

    /**
     * onHitRobot:  Afasta-se um pouco.
     */
    public void onHitRobot(HitRobotEvent e) {
        // Se ele estiver à nossa frente, volta um pouco.
        if (e.getBearing() > -90 && e.getBearing() < 90) {
            back(100);
        } // Caso contrário, ele está atrás de nós, então anda um pouco.
        else {
            ahead(100);
        }
    }

    /**
     * onScannedRobot:  Atirar!
     */
    public void onScannedRobot(ScannedRobotEvent e) {
        // Calcular a distância do robô inimigo
        double distance = e.getDistance();  

        // A potência do tiro aumenta com a diminuição da distância
        double firePower = 3.0;  // Potência máxima de 3 para tiros distantes
        if (distance < 100) {  // Se o inimigo estiver muito perto
            firePower = 3.0;  // Potência máxima para inimigos muito próximos
        } else if (distance < 200) {  // Se o inimigo estiver relativamente perto
            firePower = 2.0;  // Potência média para inimigos a uma distância média
        } else {
            firePower = 1.0;  // Potência mínima para inimigos distantes
        }

        // Dispara com a potência determinada pela distância
        if (getGunHeat() == 0 && getEnergy() > firePower) {
            fire(firePower);  // Dispara se a arma não estiver sobreaquecida e se tiver energia
        }

        // Nota: scan() é chamado automaticamente enquanto o robô se move. 
        // Chamamos manualmente aqui para garantir que o radar continue verificando enquanto o robô se move.
        if (peek) {
            scan();
        }
    }

    /**
     * onWin: Comportamento após a vitória
     */
    public void onWin(robocode.WinEvent e) {
        // Exibe uma mensagem ao vencer
        System.out.println("Wakanda venceu a batalha!");
        
        // A "dancinha da vitória" pode ser um giro repetido com avanço
        for (int i = 0; i < 5; i++) {
            turnRight(45);  // Gira 45 graus
            ahead(100);     // Avança 100 unidades
            turnLeft(45);   // Gira 45 graus para o outro lado
            ahead(100);     // Avança novamente
        }
    }
}

