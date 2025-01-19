package Entity.UI;

import Entity.Entity;
import Main.Game;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Random;

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

    String description;

    public  boolean selectable = true;
    public int cost_heads = 0;
    public int cost_tails = 0;
    public boolean cast_on_self = false;
    public boolean cast_on_enemy = false;

    public int damage = 0;
    public int block = 0;

    public double timer;

    Random rand;

    public Card(Game game, String name) {
        this.name = name;
        this.game = game;
        setSprite(getImg("/Resources/UI/Cards/card.png"));
        rand = new Random();
        setCardType(name);

        heads_icon = getImg("/Resources/UI/Cards/notches_heads.png");
        tails_icon = getImg("/Resources/UI/Cards/notches_tails.png");
        target_enemy_icon = getImg("/Resources/UI/Battle/enemy_target.png");
        target_player_icon = getImg("/Resources/UI/Battle/player_target.png");
        enemy_arrow = getImg("/Resources/UI/Battle/enemy_arrow.png");
        player_arrow = getImg("/Resources/UI/Battle/player_arrow.png");
    }

    public void setCardType(String card_type) {

        name = card_type;
        cost_heads = 0;
        cost_tails = 0;
        cast_on_enemy = false;
        cast_on_self = false;
        damage = 0;

        switch(card_type) {
            default: case "":
                selectable = false;
                card_icon = getImg("/Resources/UI/Cards/card_empty.png");
                break;
            case "Rock Throw":
                cost_heads = 1;
                card_icon = getImg("/Resources/UI/Cards/card_rock_throw.png");
                damage = 4;
                cast_on_enemy = true;
                description = "Deals 4 Damage. Costs 1 Head.";
                break;
            case "Tail Defence":
                cost_tails = 1;
                card_icon = getImg("/Resources/UI/Cards/card_tail_defence.png");
                cast_on_self = true;
                description = "Gives 4 Block. Block disappears at the start of your next turn. Costs 1 Tails.";
                break;
            case "Heads Down":
                cost_tails = 1;
                card_icon = getImg("/Resources/UI/Cards/card_heads_down.png");
                cast_on_self = true;
                description = "Converts all of your Heads into Tails + 1. Costs 1 Tails.";
                break;
            case "Heads Up":
                cost_tails = 2;
                card_icon = getImg("/Resources/UI/Cards/card_heads_up.png");
                cast_on_self = true;
                description = "Grants x2 Buff. The next Heads you roll gives 2 Heads. Costs 2 Tails.";
                break;
            case "Intimidate":
                cost_heads = 1;
                card_icon = getImg("/Resources/UI/Cards/card_Intimidate.png");
                cast_on_enemy = true;
                description = "Applies Intimidate debuff. Enemy deals 1 less damage per Intimidate. \n Remove 1 per turn. \n Costs 1 Heads";
                break;
            case "New Stick":
                cost_heads = 1;
                card_icon = getImg("/Resources/UI/Cards/card_new_stick.png");
                cast_on_self = true;
                break;
            case "Sneak Strike":
                cost_heads = 1;
                card_icon = getImg("/Resources/UI/Cards/card_sneak_strike.png");
                cast_on_enemy = true;
                damage = 3;
                break;
            case "Dull Claw":
                cost_heads = 0;
                card_icon = getImg("/Resources/UI/Cards/card_dull_claw.png");
                cast_on_enemy = true;
                damage = 2;
                break;
            case "Magic Mush":
                cost_tails = 1;
                card_icon = getImg("/Resources/UI/Cards/card_magic_mush.png");
                cast_on_self = true;
                break;
        }
    }

    public void update() {
        casting();
        visual();
    }

    public void casting() {
        if(selected && game.input.isButtonUp(MouseEvent.BUTTON1)) {
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

            int final_damage = damage;
            if(game.getBuffAmmount(game.player, "New Stick") > 0) {
                final_damage += 2;
            }

            int total_hp;

            switch(name) {
                case "Rock Throw":
                    total_hp = target.health + target.block;
                    total_hp -= final_damage;
                    if(total_hp > target.max_health) {
                        target.block = total_hp - target.max_health;
                    } else {
                        target.block = 0;
                        target.health = total_hp;
                    }

                    target.xscale = 1.5;
                    target.yscale = 0.5;
                    target.shake = 10;
                    break;
                case "Intimidate":
                    game.addBuff(target, "Intimidate", 2);
                    target.shake = 7;

                    break;
                case "Sneak Strike":
                    total_hp = target.health + target.block;
                    if(target.block > 0) total_hp -= final_damage;
                    total_hp -= final_damage;
                    if(total_hp > target.max_health) {
                        target.block = total_hp - target.max_health;
                    } else {
                        target.block = 0;
                        target.health = total_hp;
                    }

                    target.xscale = 1.5;
                    target.yscale = 0.5;
                    target.shake = 10;
                    break;
                case "Dull Claw":
                    total_hp = target.health + target.block;
                    total_hp -= final_damage;
                    damage += 2;
                    cost_heads += 1;
                    if(total_hp > target.max_health) {
                        target.block = total_hp - target.max_health;
                    } else {
                        target.block = 0;
                        target.health = total_hp;
                    }

                    target.xscale = 1.5;
                    target.yscale = 0.5;
                    target.shake = 10;
                    break;
            }
    }

    public void castOnSelf(Entity target) {
        switch(name) {
            case"Tail Defence":
                target.block += 4;
                target.yscale = 1.25;
                target.xscale = 0.75;
                break;
            case "Heads Down":
                game.tails_mana += game.heads_mana + 1;
                game.heads_mana = 0;
            break;
            case "Heads Up":
                game.addBuff(game.player, "Heads Up", 1);
                break;
            case "New Stick":
                game.addBuff(game.player, "New Stick", 2);
                break;
            case "Magic Mush":
                String[] buff_names = {
                        "Intimidate",
                        "Guard",
                        "Anger",
                        "Poison",
                };

                String buff_name = buff_names[rand.nextInt(buff_names.length)];
                game.addBuff(game.player, buff_name, rand.nextInt(3) + 1);
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
        if(game.input.isButton(MouseEvent.BUTTON3)) {

            my_deck.menu_open = true;

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

                g2d.setColor(Color.BLACK);
                g2d.drawString(name, 20 * scale, 20 * scale);
                g2d.drawString(description, 20 * scale, 40 * scale);
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