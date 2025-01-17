package Main;

import Entity.Player;

import javax.swing.*;
import java.awt.*;

public class Game extends JPanel implements Runnable {
    final int scale = 3;
    final int screen_width = 320 * scale;
    final int screen_height = 180 * scale;

    final int FPS = 60;

    Thread gameThread;
    //Player player = new Player(this);

    Canvas canvas;

    public Game() {
        this.setPreferredSize(new Dimension(screen_width, screen_height));
        this.setBackground(Color.GRAY);
        this.setDoubleBuffered(true);
    }

    public void startGame() {
        gameThread = new Thread(this);
        gameThread.start();

      //  player.setup(100, 100);
    }

    @Override
    public void run() {

        double draw_interval = 1000000000/FPS;
        double delta = 0;
        long last_time = System.nanoTime();
        long current_time;
        long timer = 0;
        int draw_count = 0;

        while(gameThread != null) {

            current_time = System.nanoTime();
            delta += (current_time - last_time) / draw_interval;
            timer += current_time - last_time;
            last_time = current_time;

            if(delta >= 1) {
                update();
                repaint();
                delta -= 1;
                draw_count += 1;
            }

            if(timer >= 1000000000) {
                System.out.println("FPS: " + draw_count);
                draw_count = 0;
                timer = 0;
            }
        }
    }

    public void update() {
      //   player.update();
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        //player.draw(g2d);
        g2d.dispose();
    }
}
