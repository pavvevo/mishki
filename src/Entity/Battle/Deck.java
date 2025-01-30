package Entity.Battle;

import Entity.Entity;
import Entity.UI.Battle.CardHolder;
import Main.Game;

import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class Deck extends Entity {

    Game game;
    Random rand;

    ArrayList<Card> cards_draw;
    ArrayList<Card> cards_discard;
    CardHolder card_holder;

    String[] card_names = {
            "Rock Throw",
            "Tail Defence",
            "Dull Claw",
    };

    public int deck_size = 5;

    public Deck(Game game) {
        rand = new Random();
        card_holder = new CardHolder(game);
        card_holder.max_slots = deck_size;
        card_holder.sprite_width = 200;
        card_holder.x = 225;
        card_holder.y = 145;
        card_holder.name = "Deck";

        for(int i = 0; i < deck_size; i++) {
            Card new_card = new Card(game);
            new_card.my_deck = this;
            new_card.double_x = 200 * i;
            new_card.double_y = 200 + 200 * i;

            new_card.getCard(getRandomCardName());
            card_holder.addCard(new_card);
        }
    }

    public void drawCard() {
        card_holder.addCard(cards_draw.getFirst());
        cards_draw.removeFirst();
    }

    public void discardCard(Card card) {
        cards_discard.add(card);
        card_holder.removeCard(card);
    }

    public void drawHand() {
        cards_discard.removeAll(cards_draw);
        cards_draw = cards_discard;
        Collections.shuffle(cards_draw);
    }

    public String getRandomCardName() {
        return card_names[rand.nextInt(card_names.length)];
    }

    public void early_update() {
        card_holder.getHovered();
    }

    public void update() {
        card_holder.update();
    }

    public void draw(Graphics2D g2d, Game game) {
        card_holder.draw(g2d);

        if(game.hovered_card != null) {
            game.hovered_card.draw(g2d);
        }

        if(game.dragged_card != null) {
            game.dragged_card.draw(g2d);
        }
    }
}
