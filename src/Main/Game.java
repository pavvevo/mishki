package Main;

import Entity.Entity;
import Entity.Coin;
import Entity.Enemy;
import Entity.Player;
import Entity.UI.*;
import Entity.UI.Deck;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
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

    public OldCard selected_card;

    Cursor cursor;
    public Input input;

    public Deck deck;
    public Card hovered_card;
    public Card other_card;
    public Card dragged_card;
    public CardHolder hovered_holder;

    Coin coin;
    public Player player;
    public Enemy enemy;
    Map map;
    Chest_State chest;
    public ShopCards shop;

    public Entity selected_target;
    public boolean is_target_player = false;

    public int tails_mana = 0;
    public int heads_mana = 0;
    public boolean turn = true;
    public int max_tosses = 3;
    public int tosses = max_tosses;

    String[] enemy_names = {
            "Mouse",
            "Fly"
    };

    //card vars
    public int heads_up_buff = 0;

    public void changeState(STATE to) {

        if(in_transition) return;
        to_State = to;
        transition_y = -fade.getHeight();
        in_transition = true;
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
            bg = ImageIO.read(getClass().getResourceAsStream("/Resources/Other/bg.png"));
            logo = ImageIO.read(getClass().getResourceAsStream("/Resources/Other/logo.png"));
            chest_bg = ImageIO.read(getClass().getResourceAsStream("/Resources/Other/chest_bg.png"));
            shop_bg = ImageIO.read(getClass().getResourceAsStream("/Resources/Other/shop_bg.png"));
            fade = ImageIO.read(getClass().getResourceAsStream("/Resources/Other/fade.png"));

            map_bg = ImageIO.read(getClass().getResourceAsStream("/Resources/Other/map_bg.png"));
            map_bg_back = ImageIO.read(getClass().getResourceAsStream("/Resources/Other/map_bg_back.png"));
            map_bg_middle = ImageIO.read(getClass().getResourceAsStream("/Resources/Other/map_bg_middle.png"));
            map_bg_front = ImageIO.read(getClass().getResourceAsStream("/Resources/Other/map_bg_front.png"));
        } catch (IOException e) {
            System.out.println("CANT LOAD CERTAIN IMAGE");
        }

        rand = new Random();

        buttonMenu = new Button(this, "Main Menu", 160, 140, 5, 2);
        input = new Input(this);
        map = new Map(this);
        chest = new Chest_State(this);
        cursor = new Cursor(this);
        player = new Player(this);
        enemy = new Enemy(this, player);
        coin = new Coin(this);
        deck = new Deck(this);
        cursor.setup(100, 100);
        enemy.setup(240, 50, enemy_names[rand.nextInt(enemy_names.length)]);
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

    public void resetGame() {
        State = STATE.MENU;
        cursor.setup(100, 100);
        enemy.setup(240, 50, enemy_names[rand.nextInt(enemy_names.length)]);
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
                    i -= 1;
                }
            }
        }
    }

    public void startBattle() {
        enemy.setup(240, 50, enemy_names[rand.nextInt(enemy_names.length)]);

        for(int i = 0; i < player.buffs.size(); i++) {
            player.buffs.remove(i);
            i -= 1;
        }

        tosses = max_tosses;

        heads_mana = 0;
        tails_mana = 0;

        State = STATE.GAME;
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
            removeBuff(player, "Anger", 1);

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

            removeBuff(enemy, "Anger", 1);
            removeBuff(player, "Guard", 1);

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
            hovered_card = null;
            other_card = null;
            hovered_holder = null;

            cursor.update();
            switch(State) {
                case GAME:
                    //player.update();
                    //enemy.update();
                    //coin.update();
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

            }

            input.update();

            if(in_transition) {
                transition_y += 10;
                if(transition_y >= -fade.getHeight() / 2 && State != to_State) {
                    State = to_State;
                    if(State == STATE.GAME) {
                        startBattle();
                    }
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
                    //enemy.draw(g2d);
                    //coin.draw(g2d);
                    deck.draw(g2d, this);
                    break;

                case MENU:
                    drawBackground(g2d);
                    buttonMenu.draw(g2d);
                    break;
                case MAP:
                    drawBackground(g2d);
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
            g2d.drawImage(fade, 0, transition_y * scale, fade.getWidth() * scale, fade.getHeight() * scale, null);
            cursor.draw(g2d);

        }
        g2d.dispose();

    }
}
