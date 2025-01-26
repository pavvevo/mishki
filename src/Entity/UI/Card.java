package Entity.UI;

import Entity.Entity;
import Main.Game;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

import static java.lang.Math.sin;

public class Card extends Entity {

    Game game;
    Deck my_deck;
    CardHolder my_holder;

    BufferedImage card_background;
    BufferedImage card_front;
    BufferedImage heads_cost_icon;
    BufferedImage tails_cost_icon;

    boolean is_dragged = false;

    public int lerp_x;
    public int lerp_y;
    public int slot_x;
    public int slot_y;

    int move_delta = 0;
    public double rotation_delta;
    public double offset_y;

    //battle
    String name;
    public int cost_heads = 0;
    public int cost_tails = 0;
    public boolean cast_on_self = false;

    public Card(Game game) {
        this.game = game;
        card_background = getImg("/Resources/UI/Cards/Other/card.png");
        card_front = getImg("/Resources/UI/Cards/card_rock_throw.png");
        heads_cost_icon = getImg("/Resources/UI/Cards/Other/cost_heads.png");
        tails_cost_icon = getImg("/Resources/UI/Cards/Other/cost_tails.png");

        setSprite(card_background);
    }

    public void getCard(String name) {
        this.name = name;
        cost_tails = 0;
        cost_heads = 0;
        cast_on_self = false;

        switch(name) {
            default: case "Rock Throw":
                card_front = getImg("/Resources/UI/Cards/card_rock_throw.png");
                cost_heads = 1;
                break;
            case "Tail Defence":
                card_front = getImg("/Resources/UI/Cards/card_tail_defence.png");
                cost_tails = 1;
                cast_on_self = true;
                break;
        }
    }

    public void update() {
        xscale = lerp(xscale, target_xscale, 0.1);
        yscale = lerp(yscale, target_yscale, 0.1);

        move_delta = x;
        double_x = lerp(double_x, lerp_x, 0.1);
        double_y = lerp(double_y, lerp_y + offset_y, 0.1);
        x = (int)double_x;
        y = (int)double_y;
        move_delta = x - move_delta;

        lerp_x = slot_x;
        lerp_y = slot_y;

        int sin_offset = x;
        lil_sin = sin((sin_timer + sin_offset) / 20);
        offset_y = 3 * lil_sin;
        sin_timer += 1;

        if(isHovered(game.input) && !is_dragged) {
            game.other_card = this;
        }

        if(isHovered(game.input) && game.hovered_card == null && game.dragged_card == null) {
            game.hovered_card = this;

            target_xscale = 1.1;
            target_yscale = 1.1;

            offset_y = -15;

            if(game.input.isButtonDown(MouseEvent.BUTTON1) && game.dragged_card == null) {
                game.dragged_card = this;
                is_dragged = true;
            }
        } else {
            target_xscale = 1.0;
            target_yscale = 1.0;
        }

        if(is_dragged) {
            lerp_x = game.input.mouse_x;
            lerp_y = game.input.mouse_y;
            if(my_holder == null) {
                slot_x = lerp_x;
                slot_y = lerp_y;
            }
            offset_y = -20;

            target_xscale = 1.25;
            target_yscale = 1.25;

            if(game.hovered_entity != null) {
                target_xscale = 0.75;
                target_yscale = 0.75;
            }
        }

    }

    public boolean late_update() {

        if(game.input.isButtonDown(MouseEvent.BUTTON3)) {
            if(is_dragged) {
                game.dragged_card = null;
                is_dragged = false;

                yscale = 1.5;
                xscale = 1.0;
            }
        }

        if(game.input.isButtonUp(MouseEvent.BUTTON1)) {
            if(is_dragged) {
                if (game.hovered_holder != my_holder && game.hovered_holder != null) {
                    game.hovered_holder.swapCard(this, my_holder);
                    game.dragged_card = null;
                    is_dragged = false;
                    return true;
                }
            }

            game.dragged_card = null;
            is_dragged = false;
        }

        return false;
    }

    public void draw(Graphics2D g2d) {
        int card_width = (int)(card_background.getWidth() * scale * xscale);
        int card_height = (int)(card_background.getHeight() * scale * yscale);
        int card_x = x * scale - card_width / 2;
        int card_y = y * scale - card_height / 2;

        rotation_delta = lerp(rotation_delta, move_delta * 5, 0.1);

        g2d.rotate(Math.toRadians(rotation_delta), card_x + (double)card_width / 2, card_y + (double)card_height / 2);
        g2d.drawImage(card_background, card_x, card_y, card_width, card_height, null);
        g2d.drawImage(card_front, card_x, card_y, card_width, card_height, null);
        g2d.rotate(Math.toRadians(-rotation_delta), card_x + (double)card_width / 2, card_y + (double)card_height / 2);
    }
}
