package Entity.UI;

import Entity.Entity;
import Main.Game;

import java.awt.*;
import java.awt.event.MouseEvent;

public class Card extends Entity {

    Game game;
    Deck my_deck;

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
        xscale = lerp(xscale, 1.0, 0.1);
        yscale = lerp(yscale, 1.0, 0.1);

        if(isHovered(game.input)) {
            xscale = 1.25;
            yscale = 1.25;

            if(game.input.isButton(MouseEvent.BUTTON1)) {
                if(my_deck.selectedCard == null) {

                }
            }
        }
    }

    public void draw(Graphics2D g2d) {
        g2d.setColor(Color.WHITE);
        width = (int)(xscale * sprite_width * scale);
        height = (int)(yscale * sprite_height * scale);
        g2d.drawImage(sprite, x * scale - width / 2, y * scale - height / 2, width, height, null);
    }
}
