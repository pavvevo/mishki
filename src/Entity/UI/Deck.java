package Entity.UI;

import Main.Game;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class Deck {
    List<Card> cards;
    public int size;

    public boolean has_selected;
    Card selectedCard;

    Game game;

    public Deck(Game game, int size) {
        this.size = size;

        cards = new ArrayList<Card>();
        for(int i = 0; i < size; i++) {
            Card new_card = new Card(game, "Attack");
            new_card.x = 116 + 42 * i;
            new_card.y = 142;
            new_card.sin_timer = i * 20;
            new_card.my_deck = this;
            cards.add(new_card);
        }
    }

    public void update() {
        for(int i = 0; i < size; i++) {
            cards.get(i).update();
        }
    }

    public void draw(Graphics2D g2d) {
        for(int i = 0; i < size; i++) {
            cards.get(i).draw(g2d);
        }

        if(has_selected) {
            selectedCard.draw(g2d);
        }
    }
}
