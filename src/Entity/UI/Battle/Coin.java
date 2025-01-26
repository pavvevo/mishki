package Entity.UI.Battle;

import Entity.Entity;
import Main.Game;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.util.Random;

import static java.lang.Math.*;

public class Coin extends Entity {
    public int spin = 0;
    public double spin_speed = 0.25;
    public int side = 0;
    public int wait = 0;

    public double gravity = 0.2;
    public double vsp = 0;
    public double offset_y = 0;

    BufferedImage heads;
    BufferedImage tails;
    BufferedImage end;

    BufferedImage heads_icon;
    BufferedImage tails_icon;

    BufferedImage toss_icon;

    Random rand;
    Game game;

    public Coin(Game game) {
        this.game = game;
    }

    public void setup(int x, int y) {
        this.x = x;
        this.y = y;
        offset_y = 0;
        heads = getImg("/Resources/UI/Battle/coin_heads.png");
        tails = getImg("/Resources/UI/Battle/coin_tails.png");
        end = getImg("/Resources/UI/Battle/coin_end.png");
        setSprite(heads);
        shadow = getImg("/Resources/Other/shadow.png");
        rand = new Random();

        heads_icon = getImg("/Resources/UI/Cards/Other/notches_heads.png");
        tails_icon = getImg("/Resources/UI/Cards/Other/notches_tails.png");
        toss_icon = getImg("/Resources/UI/Cards/Other/toss_icon.png");
    }

    public void update() {

    }

    public void draw(Graphics2D g2d) {

    }
}
