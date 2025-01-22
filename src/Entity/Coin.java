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

    public void spin_coin(int spin) {
        game.tosses -= 1;
        this.spin = spin;
        spin_speed = 0.25;
        xscale = 1.5;
        vsp = -5.0;
    }

    public void update() {

        //coin spining
        if(spin > 0) {
            if (offset_y > 0) {
                side = rand.nextInt(2);

                if(side == 0) {
                    game.tails_mana += 1;
                    if(game.getBuffAmmount(game.player, "Hidden Penny") > 0) { game.heads_mana += 1; game.removeBuff(game.player, "Hidden Penny", 1);}
                    if(game.getBuffAmmount(game.player, "Coin Trick") > 0) { game.tosses += 1; game.removeBuff(game.player, "Coin Trick", 1);}

                } else {
                    game.heads_mana += 1;

                    if(game.getBuffAmmount(game.player, "Hidden Penny") > 0) game.tails_mana += 1;

                    if(game.getBuffAmmount(game.player, "Heads Up") > 0) {
                        game.heads_mana += 1;
                        game.removeBuff(game.player, "Heads Up", 1);
                    }
                }

                offset_y = 0;
                xscale = 1.5;
                yscale = 0.5;
                spin = 0;
            }
        }

        vsp += gravity;
        offset_y += vsp;
        if(spin <= 0) {
            if(offset_y >= 0) {
                offset_y = 0;
                vsp = 0;
            }
        }

        if (offset_y < -999) {
            offset_y = -200;
        }

        //click
        if(isHovered(game.input)) {
            target_xscale = 1.25;
            target_yscale = 1.25;
            if(spin <= 0) {
                if(game.input.isButtonDown(MouseEvent.BUTTON1)) {
                    if(game.tosses > 0 && game.turn && offset_y >= 0) {
                        spin_coin(60);
                    } else if(game.tosses == 0){
                        xscale = 1.75;
                        yscale = 0.25;
                        game.tosses = -1;
                    } else if(game.tosses == -1) {
                        game.changeTurn(false);
                        vsp = -20;
                        gravity = -0.5;
                        yscale = 5;
                        xscale = 0.2;
                    }
                }
            }
        } else {
            target_xscale = 1;
            target_yscale = 1;
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
            yscale = lerp(yscale, target_yscale, 0.2);
            xscale = lerp(xscale, target_yscale, 0.2);
        }
    }

    public void draw(Graphics2D g2d) {

        if(game.tosses >= 0) {
            if(side == 0) {
                setSprite(tails);
            } else {
                setSprite(heads);
            }
        } else {
            setSprite(end);
        }

        g2d.setColor(Color.WHITE);

        int shadow_width  = 48 * scale;
        int shadow_height = 32 * scale;
        if(offset_y < 0) {
            shadow_width += offset_y * scale / 3;
            shadow_height += offset_y * scale / 3;

            shadow_width = min(shadow_width, 48 * scale);
            shadow_height = min(shadow_height, 32 * scale);
        }

        if(offset_y < -100) {
            shadow_width = 0;
            shadow_height = 0;
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

        for(int i = 0; i < game.tosses; i++) {
            int icon_width = 8 * scale;
            g2d.drawImage(toss_icon, 160 * scale + icon_width * i - game.tosses * icon_width / 2, 8 * scale, icon_width, icon_width, null);
        }
    }
}
