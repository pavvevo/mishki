package Entity;

import Entity.UI.Buff;
import Main.Game;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Random;

import static java.lang.Math.max;
import static java.lang.Math.sin;

public class Player extends Entity {
    Game game;
    BufferedImage front_fox;
    BufferedImage back_fox;

    BufferedImage block_icon;

    public Player(Game game) {
        this.game = game;
        buffs = new ArrayList<Buff>();
    }

    public void setup(int x, int y) {
        this.x = x;
        this.y = y;
        max_health = 30;
        health = max_health;
        name = "The Fox";
        scale = game.scale;
        back_fox = getImg("/Resources/Player/player_back.png");
        front_fox = getImg("/Resources/Player/player_front.png");
        shadow = getImg("/Resources/Other/shadow.png");

        block_icon = getImg("/Resources/UI/Battle/block_icon.png");

        setSprite(back_fox);
    }

    public void update() {
        sin_timer += 1;
        lil_sin = sin(sin_timer / 10) / 20;
        xscale = lerp(xscale, 1.0 + lil_sin, 0.1);
        yscale = lerp(yscale, 1.0 - lil_sin, 0.1);

        if(shake > 0) shake -= 0.5;
        else shake = 0;

        if(health <= 0) {
            game.resetGame();
        }

        if(isHovered(game.input)) {
            game.setSelectedTarget(this, true);
        }

        if(game.turn) {
            if(sprite == back_fox) {
                xscale = 1.25;
                yscale = 0.75;
            }
            setSprite(front_fox);
        } else {
            if(sprite == front_fox) {
                xscale = 1.25;
                yscale = 0.75;
            }
            setSprite(back_fox);
        }

        if(isHovered(game.input) && game.input.isButtonDown(MouseEvent.BUTTON1)) {
            xscale = 1.25;
            yscale = 0.75;
        }
    }

    public void draw(Graphics2D g2d) {
        g2d.setColor(Color.WHITE);

        g2d.drawImage(shadow, x * scale - 64, y * scale + 48, 48 * scale, 24 * scale, null);

        width = (int)(xscale * sprite_width * scale);
        height = (int)(yscale * sprite_height * scale);

        Random rand = new Random();
        int shake_x = rand.nextInt((int)shake + 1 - (- (int)shake) ) - (int)shake;
        int shake_y = rand.nextInt((int)shake + 1 - (- (int)shake) ) - (int)shake;
        shake_x *= scale;
        shake_y *= scale;

        g2d.drawImage(sprite, x * scale - width / 2 + shake_x, y * scale - height / 2 + shake_y, width, height, null);

        g2d.setColor(Color.BLACK);
        int hb_width = 64 * scale;
        int hb_height = 8 * scale;
        int start_x = x * scale - hb_width + 32 * scale;
        int start_y = y * scale - hb_height + 48 * scale;
        g2d.fillRoundRect(start_x, start_y - 1, hb_width, hb_height + 3, 10, 10);
        g2d.setColor(Color.GREEN);
        if(block > 0) {
            Color blue = new Color(49, 162, 242);
            g2d.setColor(blue);
        }
        int border = 2 * scale;
        double health_slot = (double)hb_width / max_health;
        int final_width = (int)(health_slot * health);
        if(block > 0) final_width = (int)(health_slot * max_health);
        g2d.fillRoundRect(start_x + border, start_y + border - 1, final_width - 2 * border, hb_height - 2 *border + 3, 10, 10);

        g2d.setColor(Color.BLACK);
        g2d.drawString(String.valueOf(health) + "/" + String.valueOf(max_health), x * scale + 4 * scale - hb_width / 2, y * scale + 46 * scale + 1);

        if(block > 0) {
            int icon_x = x * scale - 8 * scale + hb_width / 2 - block_icon.getWidth() / 2;
            int icon_y = y * scale + 40 * scale - block_icon.getHeight();
            g2d.drawImage(block_icon, icon_x, icon_y, block_icon.getWidth() * scale, block_icon.getHeight() * scale, null);

            g2d.setColor(Color.WHITE);
            g2d.drawString(String.valueOf(block), icon_x + block_icon.getWidth() / 2 * scale - 2 * scale, icon_y + block_icon.getHeight() / 2 * scale + 3 * scale);
        }

        //buffs
        for(int i = 0; i < buffs.size(); i++) {
            buffs.get(i).x = start_x + 20 * i * scale;
            buffs.get(i).y = start_y + 10 * scale;
            buffs.get(i).draw(g2d);
        }
    }
}