package Main;

import Entity.Coin;
import Entity.Enemy;
import Entity.Player;
import Entity.UI.Cursor;
import Entity.UI.Deck;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class Game extends JPanel implements Runnable {
    public final int scale = 3;
    public final int screen_width = 320 * scale;
    public final int screen_height = 180 * scale;

    final int FPS = 60;

    Thread gameThread;
    boolean has_started = false;

    //neshta
    BufferedImage bg;

    Cursor cursor;
    public Input input;

    Coin coin;

    Player player;
    Enemy enemy;

    Deck deck;

    public Game() {
        this.setPreferredSize(new Dimension(screen_width, screen_height));
        this.setBackground(Color.GRAY);
        this.setDoubleBuffered(true);
    }

    public void startGame() {

        try {
            bg = ImageIO.read(getClass().getResourceAsStream("/Resources/Other/bg.png"));
        } catch (IOException e) {
            System.out.println("CANT LOAD BACKGROUND");
        }

        input = new Input(this);

        cursor = new Cursor(this);
        cursor.setup(100,100);

        coin = new Coin(this);
        coin.setup(160,70);

        deck = new Deck(this,5);

        player = new Player(this);
        player.setup(40, 120);

        enemy = new Enemy(this);
        enemy.setup(225, 75, "Mouse");

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
            deck.update();

            input.update();
        }
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        if(has_started) {
            g2d.drawImage(bg, 0, 0, bg.getWidth() * scale, bg.getHeight() * scale, null);
            player.draw(g2d);
            enemy.draw(g2d);
            coin.draw(g2d);
            deck.draw(g2d);
            cursor.draw(g2d);
        }
        g2d.dispose();
    }
}
