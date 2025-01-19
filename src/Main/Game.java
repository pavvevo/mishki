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
import Entity.UI.Other.Chest_State;

public class Game extends JPanel implements Runnable {
    public final int scale = 3;
    public final int screen_width = 320 * scale;
    public final int screen_height = 180 * scale;

    public enum STATE {
        GAME,
        MENU,
        MAP,
        SHOP,
        CHEST
    }
    public STATE State = STATE.MENU;

    final int FPS = 60;

    Thread gameThread;
    boolean has_started = false;

    //neshta
    Button buttonMenu;

    BufferedImage bg;
    BufferedImage logo;
    BufferedImage map_bg;
    BufferedImage chest_bg;

    Cursor cursor;
    public Input input;
    public Deck deck;
    Coin coin;
    public Player player;
    public Enemy enemy;
    Map map;
    Chest_State chest;

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
            map_bg = ImageIO.read(getClass().getResourceAsStream("/Resources/Other/map_bg.png"));
            logo = ImageIO.read(getClass().getResourceAsStream("/Resources/Other/logo.png"));
            chest_bg = ImageIO.read(getClass().getResourceAsStream("/Resources/Other/chest_bg.png"));
        } catch (IOException e) {
            System.out.println("CANT LOAD CERTAIN IMAGE");
        }

        buttonMenu = new Button(this, "Main Menu", 160, 140, 5, 2);
        input = new Input(this);
        map = new Map(this);
        chest = new Chest_State(this);
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
        chest.setup();
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
            new_buff.remaining = ammount;
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
            removeBuff(player, "New Stick", 1);

            if(getBuffAmmount(player, "Poison") > 0) {
                player.health -= 2;
                player.shake = 5;
                removeBuff(player, "Poison", 1);
            }

            if(getBuffAmmount(enemy, "Poison") > 0) {
                enemy.health -= 2;
                player.shake = 5;
                removeBuff(enemy, "Poison", 1);
            }

            if(getBuffAmmount(enemy, "Guard") > 0) {
                enemy.xscale = 0.75;
                enemy.yscale = 1.25;
                enemy.block += 5;
            }
        }

        //to enemy
        if(!turn) {
            heads_mana = 0;
            tails_mana = 0;
            enemy.block = 0;

            if(getBuffAmmount(player, "Guard") > 0) {
                player.xscale = 0.75;
                player.yscale = 1.25;
                player.block += 5;
            }
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
                    break;
                case CHEST:
                    chest.update();
                    break;
                case SHOP:
                    deck.update();

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
                    g2d.drawImage(map_bg, 0, 0, map_bg.getWidth() * scale, map_bg.getHeight() * scale, null);
                    g2d.drawImage(logo, 160 * scale - logo.getWidth() * scale / 2, 10 * scale, logo.getWidth() * scale, logo.getHeight() * scale, null);
                    buttonMenu.draw(g2d);
                    break;
                case MAP:
                    g2d.drawImage(map_bg, 0, 0, map_bg.getWidth() * scale, map_bg.getHeight() * scale, null);
                    map.draw(g2d);
                    break;
                case SHOP:
                    g2d.drawImage(map_bg, 0, 0, map_bg.getWidth() * scale, map_bg.getHeight() * scale, null);
                    deck.draw(g2d);
                    break;
                case CHEST:
                    g2d.drawImage(chest_bg, 0, 0, chest_bg.getWidth() * scale, chest_bg.getHeight() * scale, null);
                    chest.draw(g2d);
            }
            cursor.draw(g2d);
        }
        g2d.dispose();

    }
}
