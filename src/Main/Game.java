package Main;

import Entity.Coin;
import Entity.Enemy;
import Entity.Player;
import Entity.UI.Card;
import Entity.UI.Cursor;
import Entity.UI.Button;
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

    public enum STATE {
        GAME,
        MENU
    }
    public STATE State = STATE.MENU;

    final int FPS = 60;

    Thread gameThread;
    boolean has_started = false;

    //neshta
    BufferedImage bg;

    Cursor cursor;
    public Input input;
    Deck deck;
    Coin coin;
    Button buttonMenu;
    Player player;
    Enemy enemy;

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
        buttonMenu = new Button(this, "Main Menu", 100, 50, 5, 2);


        cursor = new Cursor(this);
        enemy = new Enemy(this);
        player = new Player(this);
        coin = new Coin(this);
        deck = new Deck(this,5);
        cursor.setup(100, 100);
        enemy.setup(200, 50, "Mouse");
        player.setup(100, 100);
        coin.setup(180, 90);









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
            cursor.update();
            if(State == STATE.GAME) {
                player.update();
                enemy.update();
                coin.update();


            }
            else if(State == STATE.MENU) {
                buttonMenu.update();
                if(buttonMenu.isClicked() && buttonMenu.isHovered(input)) {
                    State = STATE.GAME;
                }
            }
            input.update();
        }
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        if(has_started) {


            if(State == STATE.GAME) {
                g2d.drawImage(bg, 0, 0, bg.getWidth() * scale, bg.getHeight() * scale, null);
                player.draw(g2d);
                enemy.draw(g2d);
                coin.draw(g2d);
                deck.draw(g2d);
            }
            else if(State == STATE.MENU) {
                buttonMenu.draw(g2d);
            }
            cursor.draw(g2d);
        }
        g2d.dispose();
    }
}
