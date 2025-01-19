package Entity.UI;

import Entity.Entity;
import Entity.Enemy;
import Main.Game;

import java.awt.*;
import java.awt.event.MouseEvent;

import static java.lang.Math.sin;

public class Button extends Entity {

    Game game;

    public double target_xscale = 1;
    public double target_yscale = 1;

    public int lerp_x;
    public int lerp_y;

    public Button(Game game, String name, int x, int y, int w, int h) {
        this.name = name;
        this.game = game;
        this.x = x;
        this.y = y;
        lerp_x = x;
        lerp_y = y;
        switch(name) {
            default: case "Menu Button":
                setSprite(getImg("/Resources/UI/button_start.png"));
                break;
        }
    }

    public void update() {
        sin_timer += 1;
        lil_sin = sin(sin_timer / 10) / 20;
        xscale = lerp(xscale, target_xscale + lil_sin, 0.1);
        yscale = lerp(yscale, target_yscale - lil_sin, 0.1);

        x = (int)lerp(x, lerp_x, 0.1);
        y = (int)lerp(y, lerp_y, 0.1);

        if(isHovered(game.input)) {
            target_xscale = 1.25;
            target_yscale = 1.25;

            if(game.input.isButtonDown(MouseEvent.BUTTON1)) {
                switch(name) {
                    default: case "Menu Button":
                        game.State = game.State.MAP;
                        break;
                }
            }
        } else {
            target_xscale = 1.0;
            target_yscale = 1.0;
        }
    }

    public void draw(Graphics2D g2d) {
        g2d.setColor(Color.WHITE);
        width = (int)(xscale * sprite_width * scale);
        height = (int)(yscale * sprite_height * scale);
        g2d.drawImage(sprite, x * scale - width / 2, y * scale - height / 2, width, height, null);
    }
}
