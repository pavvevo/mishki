package Main;

import Entity.Entity;
import Entity.UI.Battle.Coin;
import Entity.UI.Battle.Enemy;
import Entity.UI.Battle.Player;
import Entity.UI.*;
import Entity.General.*;
import Entity.UI.Deck;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import Entity.UI.Button;
import Entity.UI.Cursor;
import Entity.UI.Nodes.Map;
import Entity.UI.Other.Chest_State;
import Entity.UI.Other.ShopCards;

import static java.lang.Math.clamp;

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
    public STATE to_State = State;

    public boolean in_transition = false;
    public int transition_y = -screen_height * 4;

    final int FPS = 60;

    Thread gameThread;
    boolean has_started = false;

    Random rand;

    //neshta
    Button buttonMenu;
    Button buttonMap;

    BufferedImage bg;
    BufferedImage logo;
    BufferedImage chest_bg;
    BufferedImage shop_bg;
    BufferedImage fade;
    BufferedImage map_bg;
    BufferedImage map_bg_back;
    BufferedImage map_bg_middle;
    BufferedImage map_bg_front;

    Cursor cursor;
    public Input input;

    public Deck deck;
    public Entity hovered_entity;
    public Card hovered_card;
    public Card other_card;
    public Card dragged_card;
    public CardHolder hovered_holder;

    public List<Particle> particles;

    Coin coin;
    public Player player;
    public List<Enemy> enemies;
    Map map;
    Chest_State chest;
    public ShopCards shop;

    public int tails_mana = 0;
    public int heads_mana = 0;
    public boolean turn = true;

    public void changeState(STATE to) {
        if(in_transition) return;
        to_State = to;
        transition_y = -fade.getHeight();
        in_transition = true;
    }

    public void startState(STATE state) {
        switch(state) {
            case GAME:
                enemies = new ArrayList<Enemy>();
                Enemy new_enemy = new Enemy(this, this.player);
                new_enemy.setup("Mouse");
                new_enemy.x = 230;
                new_enemy.y = 70;
                this.enemies.add(new_enemy);
            break;
        }
    }

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
            bg = ImageIO.read(getClass().getResourceAsStream("/Resources/Other/Backgrounds/bg.png"));
            logo = ImageIO.read(getClass().getResourceAsStream("/Resources/Other/logo.png"));
            chest_bg = ImageIO.read(getClass().getResourceAsStream("/Resources/Other/Backgrounds/chest_bg.png"));
            shop_bg = ImageIO.read(getClass().getResourceAsStream("/Resources/Other/Backgrounds/shop_bg.png"));
            fade = ImageIO.read(getClass().getResourceAsStream("/Resources/Other/fade.png"));
            map_bg = ImageIO.read(getClass().getResourceAsStream("/Resources/Other/Backgrounds/map_bg.png"));
            map_bg_back = ImageIO.read(getClass().getResourceAsStream("/Resources/Other/Backgrounds/map_bg_back.png"));
            map_bg_middle = ImageIO.read(getClass().getResourceAsStream("/Resources/Other/Backgrounds/map_bg_middle.png"));
            map_bg_front = ImageIO.read(getClass().getResourceAsStream("/Resources/Other/Backgrounds/map_bg_front.png"));
        } catch (IOException e) {
            System.out.println("CANT LOAD CERTAIN IMAGE");
        }

        rand = new Random();
        particles = new ArrayList<Particle>();

        buttonMenu = new Button(this, "Main Menu", 160, 140, 5, 2);
        input = new Input(this);
        map = new Map(this);
        chest = new Chest_State(this);
        cursor = new Cursor(this);
        player = new Player(this);
        enemies = new ArrayList<Enemy>();
        coin = new Coin(this);
        deck = new Deck(this);


        cursor.setup(100, 100);
        player.setup(40, 100);
        coin.setup(160, 75);
        map.setup();
        chest.setup();
        shop = new ShopCards(this);
        buttonMap = new Button(this, "Map", 40, 140, 5, 2);

        has_started = true;

        gameThread = new Thread(this);
        gameThread.start();
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
                    i -= 1;
                }
            }
        }
    }

    public void changeTurn(boolean to) {
        turn = to;
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
            hovered_card = null;
            other_card = null;
            hovered_holder = null;
            hovered_entity = null;

            cursor.update();

            switch(State) {
                case GAME:
                    //player.update();
                    //coin.update();

                    for(Enemy enemy : enemies) {
                        enemy.update();
                    }

                    deck.early_update();
                    deck.update();
                break;

                case MENU:
                    buttonMenu.update();
                break;

                case MAP:
                    map.update();
                    buttonMap.update();
                    break;
                case CHEST:
                    chest.update();
                    break;
                case SHOP:
                    deck.update();
                    shop.update();
                    buttonMap.update();
                    break;
            }

            for(int i = particles.size() - 1; i >= 0; i--) {
                particles.get(i).update();
                if(particles.get(i).finished) {
                    particles.remove(i);
                }
            }

            input.update();

            if(in_transition) {
                transition_y += 10;
                if(transition_y >= -fade.getHeight() / 2 && State != to_State) {
                    startState(to_State);
                    State = to_State;
                }
                if(transition_y >= screen_height) {
                    in_transition = false;
                }
            }

        }
    }

    public void drawBackground(Graphics2D g2d) {
        int bg_width = (int)(map_bg.getWidth() * scale * 1.1);
        int bg_height = (int)(map_bg.getHeight() * scale * 1.1);
        int bg_move = 20;
        g2d.drawImage(map_bg, input.mouse_x / bg_move - screen_width / bg_move / 2, input.mouse_y / bg_move - screen_height / bg_move / 2, bg_width, bg_height, null);
        g2d.drawImage(map_bg_back, input.mouse_x / (bg_move - 5) - screen_width / (bg_move - 5) / 2, input.mouse_y / (bg_move - 5) - screen_height / (bg_move - 5) / 2, bg_width, bg_height, null);
        g2d.drawImage(map_bg_middle, input.mouse_x / (bg_move - 10) - screen_width / (bg_move - 10) / 2, input.mouse_y / (bg_move - 10) - screen_height / (bg_move - 10) / 2, bg_width, bg_height, null);
        g2d.drawImage(map_bg_front, input.mouse_x / (bg_move - 15) - screen_width / (bg_move - 15) / 2, input.mouse_y / (bg_move - 15) - screen_height / (bg_move - 15) / 2, bg_width, bg_height, null);
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
                    //player.draw(g2d);
                    //coin.draw(g2d);

                    for(Enemy enemy : enemies) {
                        enemy.draw(g2d);
                    }

                    deck.draw(g2d, this);
                    break;

                case MENU:
                    drawBackground(g2d);
                    buttonMenu.draw(g2d);
                    break;
                case MAP:

                    map.draw(g2d);
                    break;
                case SHOP:
                    g2d.drawImage(shop_bg, 0, 0, shop_bg.getWidth() * scale, shop_bg.getHeight() * scale, null);
                    shop.draw(g2d);
                    deck.draw(g2d, this);
                    buttonMap.draw(g2d);
                    break;
                case CHEST:
                    g2d.drawImage(chest_bg, 0, 0, chest_bg.getWidth() * scale, chest_bg.getHeight() * scale, null);
                    chest.draw(g2d);
            }

            for(Particle p : particles) {
                p.draw(g2d);
            }

            g2d.drawImage(fade, 0, transition_y * scale, fade.getWidth() * scale, fade.getHeight() * scale, null);
            cursor.draw(g2d);

        }
        g2d.dispose();

    }
}
