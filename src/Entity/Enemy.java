package Entity;

import Main.Game;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.util.Random;

import static java.lang.Math.sin;

public class Enemy extends Entity{
    Game game;
    boolean dead;

    public Enemy(Game game) {
        this.game = game;
    }

    public void setup(int x, int y, String name) {
        this.x = x;
        this.y = y;
        xscale = 1.5;
        yscale = 0.5;
        scale = game.scale;
        this.name = name;

        switch(name) {
            default: case "Mouse":
                setSprite(getImg("/Resources/Enemy/mouse.png"));
                shadow = getImg("/Resources/Other/shadow.png");
                max_health = 10;
                break;
            case "Tish":
                //
                break;
        }

        health = max_health;
    }

    public void update() {
        sin_timer += 1;
        lil_sin = sin(sin_timer / 10) / 20;
        xscale = lerp(xscale, 1.0 - lil_sin, 0.1);
        yscale = lerp(yscale, 1.0 + lil_sin, 0.1);
        if(shake > 0) shake -= 0.5;
        else shake = 0;

        if(health <= 0) {
            dead = true;
        }

        if(isHovered(game.input) && game.input.isButtonDown(MouseEvent.BUTTON1)) {
            xscale = 1.25;
            yscale = 0.75;
        }

        if(isHovered(game.input)) {
            game.setSelectedTarget(this, false);
        }
    }

    public void draw(Graphics2D g2d) {
        g2d.setColor(Color.WHITE);

        g2d.drawImage(shadow, x * scale - 48, y * scale, 32 * scale, 16 * scale, null);

        Random rand = new Random();
        int shake_x = rand.nextInt((int)shake + 1 - (- (int)shake) ) - (int)shake;
        int shake_y = rand.nextInt((int)shake + 1 - (- (int)shake) ) - (int)shake;
        shake_x *= scale;
        shake_y *= scale;

        width = (int)(xscale * sprite_width * scale);
        height = (int)(yscale * sprite_height * scale);
        g2d.drawImage(sprite, x * scale - width / 2 + shake_x, y * scale - height / 2 + shake_y, width, height, null);
    }
}
