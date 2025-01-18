package Main;

import Entity.Coin;
import Entity.Enemy;
import Entity.Player;
import Entity.UI.Cursor;

import javax.swing.*;
import java.awt.*;

public class Game extends JPanel implements Runnable {
    public final int scale = 3;
    public final int screen_width = 320 * scale;
    public final int screen_height = 180 * scale;

    final int FPS = 60;

    Thread gameThread;
    boolean has_started = false;

    //neshta
    Cursor cursor;
    public Input input;

    Coin coin;

    Player player;
    Enemy enemy;

    public Game() {
        this.setPreferredSize(new Dimension(screen_width, screen_height));
        this.setBackground(Color.GRAY);
        this.setDoubleBuffered(true);
    }

    public void startGame() {

        input = new Input(this);

        cursor = new Cursor(this);
        cursor.setup(100,100);

        coin = new Coin(this);

        player = new Player(this);
        player.setup(100, 100);

        enemy = new Enemy(this);
        enemy.setup(200, 50, "Mouse");

        has_started = true;

        gameThread = new Thread(this);
        gameThread.start();
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
        if(has_started) {
            player.update();
            enemy.update();
            coin.update();
            cursor.update();
        }
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        if(has_started) {
            player.draw(g2d);
            enemy.draw(g2d);
            coin.draw(g2d);
            cursor.draw(g2d);
        }
        g2d.dispose();
    }
}
