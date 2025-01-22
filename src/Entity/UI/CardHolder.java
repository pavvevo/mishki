package Entity.UI;

import Entity.Entity;
import Main.Game;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Random;
import java.util.Collections;

import static java.lang.Math.sin;

public class CardHolder extends Entity {

    Game game;
    ArrayList<Card> cards;
    public int card_count = 0;
    public int max_slots = 5;
    public String name = "";

    public CardHolder(Game game) {
        this.game = game;
        cards = new ArrayList<Card>();
        sprite_width = 200;
        sprite_height = 75;
    }

    public void addCard(Card card) {
        card.my_holder = this;
        card_count += 1;
        cards.add(card);
    }

    public void removeCard(Card card) {
        cards.remove(card);
        card_count -= 1;
    }

    public void swapCard(Card card, CardHolder holder) {
        if(card_count < max_slots) {
            card.my_holder = this;
            holder.cards.remove(card);
            cards.add(card);
        } else {
            if(game.other_card == null) return;
            card.my_holder = this;
            holder.cards.remove(card);
            cards.add(card);
            game.other_card.my_holder = holder;
            cards.remove(game.other_card);
            holder.cards.add(game.other_card);
        }
    }

    public void getHovered() {
        if(isHovered(game.input)) {
            game.hovered_holder = this;
        }
    }

    public void update() {
        card_count = cards.size();

        if(game.dragged_card != null) {
            if(cards.contains(game.dragged_card)) {
                for(int i = 0; i < cards.size(); i++) {
                    if(game.dragged_card.x > cards.get(i).x) {
                        if(cards.indexOf(game.dragged_card) < i) {
                            Collections.swap(cards, cards.indexOf(game.dragged_card), i);
                            break;
                        }
                    }

                    if(game.dragged_card.x < cards.get(i).x) {
                        if(cards.indexOf(game.dragged_card) > i) {
                            Collections.swap(cards, cards.indexOf(game.dragged_card), i);
                            break;
                        }
                    }
                }
            }
        }

        for(int i = 0; i < card_count; i++) {
            cards.get(i).slot_x = x - sprite_width / 2 + (i + 1) * sprite_width / (card_count + 1);
            cards.get(i).slot_y = y;
            cards.get(i).update();
        }

        for(int i = 0; i < card_count; i++) {
            boolean swapped = cards.get(i).late_update();
            if(swapped) break;
        }

        card_count = cards.size();
    }

    public void draw(Graphics2D g2d) {
        for(int i = cards.size() - 1; i >= 0; i--) {
            cards.get(i).draw(g2d);
        }

        //debug

//        g2d.setStroke(new BasicStroke(10));
//        g2d.setColor(Color.BLUE);
//        if(isHovered(game.input)) g2d.setColor(Color.RED);
//        g2d.drawRect(x * scale - sprite_width / 2 * scale, y * scale - sprite_height / 2 * scale, sprite_width * scale, sprite_height * scale);
    }
}
