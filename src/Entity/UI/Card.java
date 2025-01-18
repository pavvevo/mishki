package Entity.UI;

import Entity.Entity;
import Main.Game;

import java.awt.*;
import java.awt.event.MouseEvent;

import static java.lang.Math.sin;

public class Card extends Entity {

    Game game;
    Deck my_deck;

    boolean selected = false;

    public double y_offset = 0.0;
    public double lerp_offset = 0.0;

    public Card(Game game, String name) {
        this.name = name;
        this.game = game;
        setSprite(getImg("/Resources/UI/Cards/card.png"));

        switch(name) {

            default: case "Attack":
                break;
        }
    }

    public void update() {
        xscale = lerp(xscale, target_xscale, 0.1);
        yscale = lerp(yscale, target_yscale, 0.1);

        target_xscale = 1.0;
        target_yscale = 1.0;

        if(isHovered(game.input)) {

            if(!my_deck.has_selected) {
                target_xscale = 1.25;
                target_yscale = 1.25;
            }

            if(game.input.isButton(MouseEvent.BUTTON1)) {
                if(!my_deck.has_selected) {
                    my_deck.selectedCard = this;
                    my_deck.has_selected = true;
                    selected = true;
                }
            }
        }

        if(selected) {
            target_xscale = 1.5;
            target_yscale = 1.5;

            if(game.input.isButtonUp(MouseEvent.BUTTON1)) {
                selected = false;
                my_deck.has_selected = false;
                my_deck.selectedCard = null;
            }

            lerp_offset = -20;
        } else {
            lil_sin = sin(sin_timer / 20);
            lerp_offset = (int)(lil_sin * 5);
        }
        y_offset = lerp(y_offset, lerp_offset, 0.1);
        sin_timer += 1;
    }

    public void draw(Graphics2D g2d) {

        g2d.setColor(Color.WHITE);
        width = (int)(xscale * sprite_width * scale);
        height = (int)(yscale * sprite_height * scale);
        g2d.drawImage(sprite, x * scale - width / 2, (y + (int)y_offset) * scale - height / 2, width, height, null);
    }
}