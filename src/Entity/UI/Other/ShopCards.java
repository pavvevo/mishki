package Entity.UI.Other;

import Entity.Entity;
import Entity.UI.Buff;
import Entity.UI.Card;
import Entity.UI.Deck;
import Main.Game;

import java.awt.*;
import java.awt.image.BufferedImage;

import static java.lang.Math.sin;

public class ShopCards extends Entity {
    Deck deck;
    Game game;

    Card card_1;
    Card card_2;
    Card card_3;

    BufferedImage twinkle;

    public ShopCards(Game game) {
        this.game = game;
        deck = game.deck;

        twinkle = getImg("/Resources/Other/twinkle.png");

        int offset_x = 25 * scale;

        card_1 = new Card(game, deck.getRandomCardName());
        card_1.x = offset_x + 10 * game.scale;
        card_1.lerp_y = 20 * game.scale;
        card_1.my_deck = deck;
        card_1.in_shop = true;

        card_2 = new Card(game, deck.getRandomCardName());
        card_2.x = offset_x +30 * game.scale;
        card_2.lerp_y = 20 * game.scale;
        card_2.my_deck = deck;
        card_2.in_shop = true;

        card_3 = new Card(game, deck.getRandomCardName());
        card_3.x = offset_x + 50 * game.scale;
        card_3.lerp_y = 20 * game.scale;
        card_3.my_deck = deck;
        card_3.in_shop = true;
    }

    public void update() {

        sin_timer += 1;
        lil_sin = sin(sin_timer / 10) / 20;
        xscale = lerp(xscale, 1.0 - lil_sin, 0.1);
        yscale = lerp(yscale, 1.0 + lil_sin, 0.1);

        card_1.update();
        card_2.update();
        card_3.update();
    }

    public void draw(Graphics2D g2d) {


        width = (int)(xscale * twinkle.getWidth() * scale);
        height = (int)(yscale * twinkle.getHeight() * scale);
        g2d.drawImage(twinkle, 160 * scale -width / 2, 60 * scale-height / 2, width, height, null);
        card_1.draw(g2d);
        card_2.draw(g2d);
        card_3.draw(g2d);

        g2d.setColor(Color.WHITE);
        g2d.drawString("Choose Cards", 150 * scale, 100 * scale);
    }
}
