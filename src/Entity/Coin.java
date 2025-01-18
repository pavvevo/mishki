package Entity;

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
    private int wait = 0;

    private double gravity = 0.2;
    private double vsp = 0;
    private double offset_y = 0;

    public BufferedImage heads;
    public BufferedImage tails;

    BufferedImage heads_icon;
    BufferedImage tails_icon;

    Random rand;
    Game game;

    public Coin(Game game) {
        this.game = game;
    }

    public void setup(int x, int y) {
        this.x = x;
        this.y = y;
        offset_y = 0;
        heads = getImg("/Resources/UI/coin_heads.png");
        tails = getImg("/Resources/UI/coin_tails.png");
        setSprite(heads);
        shadow = getImg("/Resources/Other/shadow.png");
        rand = new Random();

        heads_icon = getImg("/Resources/UI/Cards/notches_heads.png");
        tails_icon = getImg("/Resources/UI/Cards/notches_tails.png");
    }

    public void spin_coin(int spin) {
        if(game.tosses > 0) {
            game.tosses -= 1;
            this.spin = spin;
            spin_speed = 0.25;
            xscale = 1.5;
            vsp = -5.0;
        }
    }

    public void update() {

        if(spin > 0) {
            vsp += gravity;
            offset_y += vsp;
            if (offset_y > 0) {
                side = rand.nextInt(2);

                if(side == 0) {
                    game.tails_mana += 1;
                } else {
                    game.heads_mana += 1;
                }

                offset_y = 0;
                xscale = 0.75;
                yscale = 0.5;
                spin = 0;
            }
        }

        if(isHovered(game.input)) {

            if(spin <= 0) {
                if(game.input.isButtonDown(MouseEvent.BUTTON1)) {
                    spin_coin(60);
                }
            }
        }
        if(spin > 0) {
            yscale = abs(sin(spin * spin_speed));
            xscale = 1.25;
            if(yscale < 0.25) {
                if(wait <= 0) {
                    if(side == 0) {
                        side = 1;
                        wait = 5;
                    } else {
                        side = 0;
                        wait = 5;
                    }
                }
            }

            wait -= 1;
            spin_speed -= 0.001;
            spin -= 1;
        } else {
            yscale = lerp(yscale, 1.0, 0.2);
            xscale = lerp(xscale, 1.0, 0.2);
        }
    }

    public void draw(Graphics2D g2d) {
        if(side == 0) {
            setSprite(tails);
        } else {
            setSprite(heads);
        }

        g2d.setColor(Color.WHITE);

        int shadow_width  = 48 * scale;
        int shadow_height = 32 * scale;
        if(offset_y < 0) {
            shadow_width += offset_y * scale / 3;
            shadow_height += offset_y * scale / 3;
        }
        g2d.drawImage(shadow, x * scale - shadow_width / 2, y * scale - (shadow_height / 2 - 32), shadow_width, shadow_height, null);
        width = (int)(xscale * sprite_width * scale);
        height = (int)(yscale * sprite_height * scale);
        g2d.drawImage(sprite, x * scale - width / 2, (y + (int)offset_y) * scale - height / 2, width, height, null);

        //UI
        for(int i = 0; i < game.heads_mana; i++) {
            g2d.drawImage(heads_icon, (int)(10 * scale + scale * i * (heads_icon.getWidth() + 2)), 10 * scale, heads_icon.getWidth() * scale, heads_icon.getHeight() * scale, null);
        }

        for(int i = 0; i < game.tails_mana; i++) {
            g2d.drawImage(tails_icon, (int)(10 * scale + scale * i * (tails_icon.getWidth() + 2)), 12 * scale + tails_icon.getHeight() * scale, tails_icon.getWidth() * scale, tails_icon.getHeight() * scale, null);
        }
    }
}
