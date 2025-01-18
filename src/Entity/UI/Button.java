package Entity.UI;

import Entity.Entity;
import Main.Game;

import java.awt.*;
import java.awt.event.MouseEvent;

public class Button extends Entity {

    Game game;
    int x, y;
    int w,h;

    public Button(Game game, String name, int x, int y, int w, int h) {
        this.name = name;
        this.game = game;
        this.x = x;
        this.y = y;
        setSprite(getImg("/Resources/UI/Cards/card.png"));

        switch(name) {

            default: case "Attack":
                break;
        }
    }

    public void update() {
        xscale = lerp(xscale, 1.0, 0.1);
        yscale = lerp(yscale, 1.0, 0.1);

        if(isHovered(game.input)) {
            xscale = 1.25;
            yscale = 1.25;
            System.out.println("nigger");
            if(game.input.isButton(MouseEvent.BUTTON1)) {

            }
        }

    }
    public boolean isClicked() {
        if(game.input.isButton(MouseEvent.BUTTON1)) {
            return true;
        }
        return false;
    }


    public void draw(Graphics2D g2d) {
        g2d.setColor(Color.WHITE);
        width = (int)(xscale * sprite_width * scale);
        height = (int)(yscale * sprite_height * scale);
        g2d.drawImage(sprite, x * scale - width / 2, y * scale - height / 2, width, height, null);
    }
}
