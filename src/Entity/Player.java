package Entity;

import Main.Game;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

import static java.lang.Math.sin;

public class Player extends Entity {
    Game game;
    BufferedImage front_fox;
    BufferedImage back_fox;

    public Player(Game game) {
        this.game = game;
        System.out.println(this.game);
    }

    public void setup(int x, int y) {
        this.x = x;
        this.y = y;
        max_health = 20;
        health = max_health;
        name = "The Fox";
        scale = game.scale;
        back_fox = getImg("/Resources/Player/player_back.png");
        front_fox = getImg("/Resources/Player/player_front.png");
        shadow = getImg("/Resources/Other/shadow.png");

        setSprite(back_fox);
    }

    public void update() {
        sin_timer += 1;
        lil_sin = sin(sin_timer / 10) / 20;
        xscale = lerp(xscale, 1.0 + lil_sin, 0.1);
        yscale = lerp(yscale, 1.0 - lil_sin, 0.1);

        if(isHovered(game.input)) {
            game.setSelectedTarget(this, true);
        }

        if(isHovered(game.input) && game.input.isButtonDown(MouseEvent.BUTTON1)) {
            xscale = 1.25;
            yscale = 0.75;

            if(sprite == front_fox) {
                setSprite(back_fox);
            } else if(sprite == back_fox) {
                setSprite(front_fox);
            }
        }
    }

    public void draw(Graphics2D g2d) {
        g2d.setColor(Color.WHITE);

        g2d.drawImage(shadow, x * scale - 64, y * scale + 48, 48 * scale, 24 * scale, null);

        width = (int)(xscale * sprite_width * scale);
        height = (int)(yscale * sprite_height * scale);
        g2d.drawImage(sprite, x * scale - width / 2, y * scale - height / 2, width, height, null);
    }
}