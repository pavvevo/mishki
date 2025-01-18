package Entity.UI;

import Entity.Entity;
import Main.Game;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;

import static java.lang.Math.atan2;
import static java.lang.Math.sin;

public class Card extends Entity {
    Game game;
    Deck my_deck;

    boolean selected = false;

    public double y_offset = 0.0;
    public double lerp_offset = 0.0;
    public int lerp_y = 0;

    BufferedImage heads_icon;
    BufferedImage tails_icon;

    BufferedImage target_enemy_icon;
    BufferedImage target_player_icon;
    BufferedImage enemy_arrow;
    BufferedImage player_arrow;

    BufferedImage card_icon;

    public  boolean selectable = true;
    public int cost_heads = 0;
    public int cost_tails = 0;
    public boolean cast_on_self = false;
    public boolean cast_on_enemy = false;

    public int damage = 0;
    public int block = 0;

    public double timer;

    public Card(Game game, String name) {
        this.name = name;
        this.game = game;
        setSprite(getImg("/Resources/UI/Cards/card.png"));

        setCardType(name);

        heads_icon = getImg("/Resources/UI/Cards/notches_heads.png");
        tails_icon = getImg("/Resources/UI/Cards/notches_tails.png");
        target_enemy_icon = getImg("/Resources/UI/enemy_target.png");
        target_player_icon = getImg("/Resources/UI/player_target.png");
        enemy_arrow = getImg("/Resources/UI/enemy_arrow.png");
        player_arrow = getImg("/Resources/UI/player_arrow.png");
    }

    public void setCardType(String cardType) {
        switch(cardType) {
            default: case "":
                selectable = false;
                card_icon = getImg("/Resources/UI/Cards/card_empty.png");
                break;
            case "Rock Throw":
                cost_heads = 1;
                card_icon = getImg("/Resources/UI/Cards/card_rock_throw.png");
                damage = 2;
                cast_on_enemy = true;
                break;
            case "Tail Defence":
                cost_tails = 1;
                card_icon = getImg("/Resources/UI/Cards/card_tail_defence.png");
                cast_on_self = true;
                break;
        }
    }

    public void update() {
        casting();
        visual();
    }

    public void casting() {
        if(selectable && game.input.isButtonUp(MouseEvent.BUTTON1)) {
            if(game.tails_mana >= cost_tails && game.heads_mana >= cost_heads) {
                boolean can_target = game.selected_target != null;

                if (!game.is_target_player && !cast_on_enemy) {
                    can_target = false;
                }
                if (game.is_target_player && !cast_on_self) {
                    can_target = false;
                }

                if (can_target) {

                    if (!game.is_target_player) {
                        castOnEnemy(game.selected_target);
                    } else {
                        castOnSelf(game.selected_target);
                    }

                    game.tails_mana -= cost_tails;
                    game.heads_mana -= cost_heads;
                }
            }
        }
    }

    public void visual() {

        y = (int)lerp(y, lerp_y, 0.2);

        xscale = lerp(xscale, target_xscale, 0.1);
        yscale = lerp(yscale, target_yscale, 0.1);

        target_xscale = 1.0;
        target_yscale = 1.0;

        if(selectable) {
            if (isHovered(game.input)) {
                if (!my_deck.has_selected) {
                    target_xscale = 1.25;
                    target_yscale = 1.25;
                    my_deck.has_hovered = true;
                }

                if (game.input.isButton(MouseEvent.BUTTON1)) {
                    if (!my_deck.has_selected) {
                        my_deck.selectedCard = this;
                        my_deck.has_selected = true;
                        selected = true;
                    }
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

    public boolean isClicked() {
        if(game.input.isButton(MouseEvent.BUTTON1)) {
            return true;
        }
        return false;
    }

    public void castOnEnemy(Entity target) {
            switch(name) {
                case "Rock Throw":
                    target.health -= damage;
                    target.xscale = 1.5;
                    target.yscale = 0.5;
                    target.shake = 10;
                    break;
            }
    }

    public void castOnSelf(Entity target) {
        switch(name) {
            case"Tail Defence":
                target.block += 5;
                target.yscale = 1.25;
                target.xscale = 0.75;
                break;
        }
    }

    public void draw(Graphics2D g2d) {

        //samata karta
        width = (int)(xscale * sprite_width * scale);
        height = (int)(yscale * sprite_height * scale);
        g2d.setColor(Color.WHITE);
        g2d.drawImage(sprite, x * scale - width / 2, (y + (int)y_offset) * scale - height / 2, width, height, null);

        width = (int)(xscale * card_icon.getWidth() * scale);
        height = (int)(yscale * card_icon.getHeight() * scale);

        g2d.drawImage(card_icon, x * scale - width / 2, (y + (int)y_offset) * scale - height / 2, width, height, null);

        if(selected) {
            boolean show_target = game.selected_target != null;

            BufferedImage target_icon = target_enemy_icon;
            if(game.is_target_player) target_icon = target_player_icon;

            if(!game.is_target_player && !cast_on_enemy) { show_target = false; }
            if(game.is_target_player && !cast_on_self) { show_target = false; }

            BufferedImage target_arrow = enemy_arrow;
            if(cast_on_self) target_arrow = player_arrow;

            int arrow_x = x * scale - target_arrow.getWidth();
            int arrow_y = y * scale - height;
            int target_x = game.input.mouse_x * scale;
            int target_y = game.input.mouse_y * scale;

            g2d.rotate(atan2(arrow_y - target_y, arrow_x - target_x) - 1.5708, x * scale + target_arrow.getWidth() / 2, arrow_y + target_arrow.getHeight());
            g2d.drawImage(target_arrow, arrow_x, arrow_y - target_arrow.getHeight(), enemy_arrow.getWidth() * scale, target_arrow.getHeight() * scale, null);
            g2d.rotate(-atan2(arrow_y - target_y, arrow_x - target_x) + 1.5708, x * scale + target_arrow.getWidth() / 2, arrow_y + target_arrow.getHeight());

            if(show_target) {
                timer += 0.01;

                int target_width = target_icon.getWidth() * scale;
                int target_height = target_icon.getHeight() * scale;
                int mouse_x = game.selected_target.x * scale;
                int mouse_y = game.selected_target.y * scale;
                g2d.rotate(timer, target_x, target_y);
                g2d.drawImage(target_icon, target_x - target_width / 2, target_y - target_height / 2, target_width, target_height, null);
                g2d.rotate(-timer, target_x, target_y);
            }
        }

        //notchove
        int offset_x = -(int)((tails_icon.getWidth() * scale + scale) * (cost_tails + cost_heads) / 2);
        int notch_y = (y + (int)y_offset) * scale - height / 2 + 4 * scale;
        for(int i = 0; i < cost_heads; i++) {
            g2d.drawImage(heads_icon, x * scale + offset_x, notch_y, heads_icon.getWidth() * scale, heads_icon.getHeight() * scale, null);
            offset_x += heads_icon.getWidth() * scale + scale;
        }

        for(int i = 0; i < cost_tails; i++) {
            g2d.drawImage(tails_icon, x * scale + offset_x, notch_y, tails_icon.getWidth() * scale, tails_icon.getHeight() * scale, null);
            offset_x += tails_icon.getWidth() * scale + scale;
        }

        //info //NE E GOTOVO DOBAVI TEKST I DRUGI NESHTA
        if(my_deck.inspect_enabled) {

            if (isHovered(game.input) && !selected && !my_deck.has_selected) {
                g2d.setColor(Color.BLACK);
                int rect_width = (320 - 20) * scale;
                int rect_height = 90 * scale;

                my_deck.lerp_x = x;

                Color brown = new Color(164, 100, 34);
                int offset = 18;

                g2d.fillRect(10 * scale, 10 * scale, rect_width, rect_height);
                g2d.setColor(brown);
                g2d.fillRect(10 * scale + offset / 2, 10 * scale + offset / 2, rect_width - offset, rect_height - offset);
            }
        }

    }

    public BufferedImage getImg(String path ) {
        try {
            return ImageIO.read(getClass().getResourceAsStream(path));
        } catch (IOException e) {
            System.out.println("CANT LOAD");
            return null;
        }
    }
}