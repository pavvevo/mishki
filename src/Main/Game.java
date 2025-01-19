package Main;

import Entity.Entity;
import Entity.Coin;
import Entity.Enemy;
import Entity.Player;
import Entity.UI.*;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

import Entity.UI.Button;
import Entity.UI.Cursor;
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
    public STATE State = STATE.GAME;

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
    public Player player;
    public Enemy enemy;
    Map map;

    public Entity selected_target;
    public boolean is_target_player = false;

    public int tails_mana = 0;
    public int heads_mana = 0;
    public boolean turn = true;
    public int max_tosses = 3;
    public int tosses = max_tosses;

    //card vars
    public int heads_up_buff = 0;

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
        player = new Player(this);
        enemy = new Enemy(this, player);
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

    public void resetGame() {
        State = STATE.MENU;
        cursor.setup(100, 100);
        enemy.setup(240, 50, "Mouse");
        player.setup(40, 100);
        coin.setup(160, 75);
        map.setup();
    }

    public void setSelectedTarget(Entity target, boolean is_player) {
        this.selected_target = target;
        this.is_target_player = is_player;
    }

    public void addBuff(Entity target, String name, int ammount) {
        boolean updated = false;
        for(int i = 0; i < target.buffs.size(); i++) {
            if(target.buffs.get(i).name.equals(name)) {
                target.buffs.get(i).remaining += ammount;
                updated = true;
            }
        }
        if(!updated) {
            Buff new_buff = new Buff(this, name);
            target.buffs.add(new_buff);
        }
    }

    public int getBuffAmmount(Entity target, String name) {
        for(int i = 0; i < target.buffs.size(); i++) {
            if(target.buffs.get(i).name.equals(name)) {
                return target.buffs.get(i).remaining;
            }
        }

        return 0;
    }

    public void removeBuff(Entity target, String name, int ammount) {
        for(int i = 0; i < target.buffs.size(); i++) {
            if(target.buffs.get(i).name.equals(name)) {
                target.buffs.get(i).remaining -= ammount;
                if(target.buffs.get(i).remaining <= 0) {
                    target.buffs.remove(i);
                }
            }
        }
    }

    public void startBattle() {
        enemy.setup(240, 50, "Mouse");

        for(int i = 0; i < player.buffs.size(); i++) {
            player.buffs.remove(i);
        }
        heads_mana = 0;
        tails_mana = 0;
    }

    public void changeTurn(boolean to) {
        turn = to;

        //to player
        if(turn) {
            coin.gravity = 0.2;
            coin.yscale = 5;
            coin.xscale = 0.2;
            coin.vsp = 10;
            player.block = 0;
            tosses = max_tosses;

            removeBuff(enemy, "Intimidate", 1);

            if(getBuffAmmount(player, "Poison") > 0) {
                player.health -= 2;
                removeBuff(player, "Poison", 1);
            }

            if(getBuffAmmount(enemy, "Poison") > 0) {
                enemy.health -= 2;
                removeBuff(enemy, "Poison", 1);
            }
        }

        //to enemy
        if(!turn) {
            heads_mana = 0;
            tails_mana = 0;
            enemy.block = 0;
        }
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

            Font currentFont = g2d.getFont();
            Font newFont = currentFont.deriveFont(currentFont.getSize() * 0.5f * scale);
            g2d.setFont(newFont);

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
