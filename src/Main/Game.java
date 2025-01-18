package Main;

import Entity.Entity;
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
import Entity.UI.Nodes.Map;

public class Game extends JPanel implements Runnable {
    public final int scale = 3;
    public final int screen_width = 320 * scale;
    public final int screen_height = 180 * scale;

    public enum STATE {
        GAME,
        MENU,
        MAP,
        SHOP
    }
    public STATE State = STATE.MENU;

    final int FPS = 60;

    Thread gameThread;
    boolean has_started = false;

    //neshta
    Button buttonMenu;

    BufferedImage bg;

    Cursor cursor;
    public Input input;
    public Deck deck;
    Coin coin;
    Player player;
    public Enemy enemy;
    Map map;

    public Entity selected_target;
    public boolean is_target_player = false;

    public int tails_mana = 0;
    public int heads_mana = 0;
    public boolean turn = true;
    public int max_tosses = 3;
    public int tosses = max_tosses;

    public Game() {
        this.setPreferredSize(new Dimension(screen_width, screen_height));
        this.setBackground(Color.GRAY);
        this.setDoubleBuffered(true);

        Toolkit toolkit = Toolkit.getDefaultToolkit();
        Image cursorImage = toolkit.createImage(""); // Invisible cursor image
        java.awt.Cursor invisibleCursor = toolkit.createCustomCursor(cursorImage, new Point(0, 0), "InvisibleCursor");
        this.setCursor(invisibleCursor);
    }

    public void startGame() {
        try {
            bg = ImageIO.read(getClass().getResourceAsStream("/Resources/Other/bg.png"));
        } catch (IOException e) {
            System.out.println("CANT LOAD BACKGROUND");
        }
        buttonMenu = new Button(this, "Main Menu", 100, 50, 5, 2);
        input = new Input(this);
        map = new Map(this);
        cursor = new Cursor(this);
        enemy = new Enemy(this);
        player = new Player(this);
        coin = new Coin(this);
        deck = new Deck(this,5);
        cursor.setup(100, 100);
        enemy.setup(240, 50, "Mouse");
        player.setup(40, 100);
        coin.setup(160, 75);
        map.setup();

        has_started = true;

        gameThread = new Thread(this);
        gameThread.start();
    }

    public void setSelectedTarget(Entity target, boolean is_player) {
        this.selected_target = target;
        this.is_target_player = is_player;
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

            selected_target = null;
            cursor.update();

            switch(State) {
                case GAME:
                    player.update();
                    enemy.update();
                    coin.update();
                    deck.update();
                break;

                case MENU:
                    buttonMenu.update();
                break;
                case MAP:
                    map.update();

            }

            input.update();
        }
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        if(has_started) {
            switch(State) {
                case GAME:
                    g2d.drawImage(bg, 0, 0, bg.getWidth() * scale, bg.getHeight() * scale, null);
                    player.draw(g2d);
                    enemy.draw(g2d);
                    coin.draw(g2d);
                    deck.draw(g2d);
                    break;

                case MENU:
                    buttonMenu.draw(g2d);
                    break;
                case MAP:
                    map.draw(g2d);
            }
            cursor.draw(g2d);
        }
        g2d.dispose();

    }
}
