package Entity.UI;

import Entity.Entity;
import Main.Game;
import Entity.Player;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Deck {
    List<Card> cards;
    public int size;

    public boolean has_selected;
    public boolean has_hovered;
    Card selectedCard;
    BufferedImage indicator;
    public double indicator_x;
    public double lerp_x;

    Random rand;

    public boolean inspect_enabled = false;

    Game game;
    Player player;

    public Deck(Game game, int size) {
        this.size = size;
        this.game = game;
        player = game.player;

        cards = new ArrayList<Card>();
        for(int i = 0; i < size; i++) {
            String name = "";
            if(i == 0) name = "Rock Throw";
            if(i == 1) name = "Tail Defence";
            if(i == 2) name = "Heads Down";
            if(i == 3) name = "Heads Up";
            Card new_card = new Card(game, name);
            new_card.x = 116 + 42 * i;
            new_card.y = 300 + 100 * i;
            new_card.lerp_y = 142;
            new_card.sin_timer = i * 20;
            new_card.my_deck = this;
            cards.add(new_card);
        }

        try {
            indicator = ImageIO.read(getClass().getResourceAsStream("/Resources/Other/indicator.png"));
        } catch (IOException e) {
            System.out.println("CANT LOAD");
        }
    }

    public void update() {
        has_hovered = false;
        for(int i = 0; i < size; i++) {
            cards.get(i).update();
        }

        indicator_x = lerp(indicator_x, lerp_x, 0.1);
    }


    public double lerp(double a, double b, double f) {
        return (a * (1.0 - f)) + (b * f);
    }

    public void draw(Graphics2D g2d) {
        for(int i = size - 1; i >= 0; i--) {
            cards.get(i).draw(g2d);
        }

        if(has_selected) {
            selectedCard.draw(g2d);
        }

        if(inspect_enabled) {
            if (has_hovered && !has_selected) {
                g2d.setColor(Color.WHITE);
                //trqnva da se polzva game.scale ama mi dava 093283409128039 errora tuiche 3
                g2d.drawImage(indicator, (int) indicator_x * 3 - 24, 80 * 3, 24 * 3, 32 * 3, null);
            }
        }
    }
}
