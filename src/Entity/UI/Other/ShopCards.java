package Entity.UI.Other;

import Entity.Entity;
import Entity.Battle.Deck;
import Main.Game;

import java.awt.*;
import java.awt.image.BufferedImage;

import static java.lang.Math.sin;

public class ShopCards extends Entity {
    Deck deck;
    Game game;

    BufferedImage twinkle;

    public ShopCards(Game game) {
        this.game = game;
        deck = game.deck;

        twinkle = getImg("/Resources/Other/twinkle.png");

        int offset_x = 25 * scale;
    }

    public void newCards() {

    }

    public void update() {

        sin_timer += 1;
        lil_sin = sin(sin_timer / 10) / 20;
        xscale = lerp(xscale, 1.0 - lil_sin, 0.1);
        yscale = lerp(yscale, 1.0 + lil_sin, 0.1);
    }

    public void draw(Graphics2D g2d) {
        width = (int)(xscale * twinkle.getWidth() * scale);
        height = (int)(yscale * twinkle.getHeight() * scale);
        g2d.drawImage(twinkle, 160 * scale -width / 2, 60 * scale-height / 2, width, height, null);

        g2d.setColor(Color.WHITE);
        g2d.drawString("Choose Cards", 150 * scale, 100 * scale);
    }
}
