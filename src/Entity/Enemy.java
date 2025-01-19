package Entity;

import Entity.UI.Buff;
import Main.Game;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static java.lang.Math.max;
import static java.lang.Math.sin;

public class Enemy extends Entity{
    Game game;
    Player player;
    boolean dead;

    public int damage = 0;
    public int block_power = 0;

    BufferedImage block_icon;

    List<Move> moves;
    public int cur_move = 0;
    public int move_count = 0;

    public int turn_wait = -100;

    public Enemy(Game game, Player player) {
        this.game = game;
        this.player = player;
        buffs = new ArrayList<Buff>();
    }

    public void setup(int x, int y, String name) {
        this.x = x;
        this.y = y;
        lerp_x = x;
        lerp_y = y;
        xscale = 1.5;
        yscale = 0.5;
        scale = game.scale;
        this.name = name;

        cur_move = 0;
        shake = 0;

        moves = new ArrayList<Move>();

        block_icon = getImg("/Resources/UI/Battle/block_icon.png");

        shadow = getImg("/Resources/Other/shadow.png");

        switch(name) {
            default: case "Mouse":
                setSprite(getImg("/Resources/Enemy/mouse.png"));
                shadow = getImg("/Resources/Other/shadow.png");
                max_health = 20;
                block_power = 10;
                damage = 6;

                Move move_1 = new Move(game, this, player,"Attack");
                moves.add(move_1);
                Move move_2 = new Move(game, this, this,"Block");
                moves.add(move_2);

                move_count = moves.size();

                break;
            case "Fly":
                setSprite(getImg("/Resources/Enemy/fly.png"));
                max_health = 10;
                block_power = 5;
                block = 25;
                damage = 10;

                game.addBuff(this, "Guard", 5);

                Move move_fly = new Move(game, this, player,"Attack");
                moves.add(move_fly);
                Move move_block = new Move(game, this, this,"Block");
                moves.add(move_block);
                moves.add(move_block);
                moves.add(move_block);

                move_count = moves.size();
                break;
            case "Fly":
                setSprite(getImg("/Resources/Enemy/fly.png"));
                max_health = 10;
                block_power = 5;
                block = 25;
                damage = 10;

                game.addBuff(this, "Guard", 5);

                Move move_fly = new Move(game, this, player,"Attack");
                moves.add(move_fly);
                Move move_block = new Move(game, this, this,"Block");
                moves.add(move_block);
                moves.add(move_block);
                moves.add(move_block);

                move_count = moves.size();
            case "Tish":
                //
                break;
        }

        health = max_health;
    }

    public void update() {
        sin_timer += 1;
        lil_sin = sin(sin_timer / 10) / 20;
        xscale = lerp(xscale, 1.0 - lil_sin, 0.1);
        yscale = lerp(yscale, 1.0 + lil_sin, 0.1);

        x = (int)lerp(x, lerp_x, 0.1);
        y = (int)lerp(y, lerp_y, 0.1);

        if(shake > 0) shake -= 0.5;
        else shake = 0;

        if(health <= 0) {
            dead = true;
            game.shop.newCards();
            game.State = game.State.SHOP;
        }

        if(isHovered(game.input) && game.input.isButtonDown(MouseEvent.BUTTON1)) {
            xscale = 1.25;
            yscale = 0.75;
        }

        if(isHovered(game.input)) {
            game.setSelectedTarget(this, false);
        }

        if(!game.turn) {
            if(turn_wait == -100) {
                turn_wait = 90;
            }
            turn_wait--;
        }

        if(turn_wait == 0) {
            moves.get(cur_move).trigger();

            cur_move++;
            if(cur_move >= move_count) cur_move = 0;
            turn_wait = -1;
        }

        if(turn_wait == -90) {
            game.changeTurn(true);
            turn_wait = -100;
        }
    }

    public void draw(Graphics2D g2d) {
        g2d.setColor(Color.WHITE);

        g2d.drawImage(shadow, x * scale - 48, y * scale, 32 * scale, 16 * scale, null);

        Random rand = new Random();
        int shake_x = rand.nextInt((int)shake + 1 - (- (int)shake) ) - (int)shake;
        int shake_y = rand.nextInt((int)shake + 1 - (- (int)shake) ) - (int)shake;
        shake_x *= scale;
        shake_y *= scale;

        width = (int)(xscale * sprite_width * scale);
        height = (int)(yscale * sprite_height * scale);
        g2d.drawImage(sprite, x * scale - width / 2 + shake_x, y * scale - height / 2 + shake_y, width, height, null);

        //move indicator
        moves.get(cur_move).draw(g2d);

        //HEALTHBAR
        g2d.setColor(Color.BLACK);
        int hb_width = 64 * scale;
        int hb_height = 8 * scale;
        int start_x = x * scale - hb_width + 32 * scale;
        int start_y = y * scale - hb_height + 32 * scale;
        g2d.fillRoundRect(start_x, start_y, hb_width, hb_height, 10, 10);
        g2d.setColor(Color.GREEN);
        if(block > 0) {
            Color blue = new Color(49, 162, 242);
            g2d.setColor(blue);
        }
        int border = 2 * scale;
        double health_slot = (double)hb_width / max_health;
        int final_width = (int)(health_slot * health);
        if(block > 0) final_width = (int)(health_slot * max_health);
        g2d.fillRoundRect(start_x + border, start_y + border - 1, final_width - 2 * border, hb_height - 2 *border + 3, 10, 10);

        g2d.setColor(Color.BLACK);
        Font currentFont = g2d.getFont();
        Font newFont = currentFont.deriveFont(currentFont.getSize() * 0.15f * scale);
        g2d.setFont(newFont);
        g2d.drawString(String.valueOf(health) + "/" + String.valueOf(max_health), x * scale + 4 * scale - hb_width / 2, y * scale + 30 * scale + 1);

        if(block > 0) {
            int icon_x = x * scale - 8 * scale + hb_width / 2 - block_icon.getWidth() / 2;
            int icon_y = y * scale + 24 * scale - block_icon.getHeight();
            g2d.drawImage(block_icon, icon_x, icon_y, block_icon.getWidth() * scale, block_icon.getHeight() * scale, null);

            g2d.setColor(Color.WHITE);
            g2d.drawString(String.valueOf(block), icon_x + block_icon.getWidth() / 2 * scale - 2 * scale, icon_y + block_icon.getHeight() / 2 * scale + 3 * scale);
        }

        //buffs
        for(int i = 0; i < buffs.size(); i++) {
            buffs.get(i).x = start_x + 20 * i * scale;
            buffs.get(i).y = start_y + 10 * scale;
            buffs.get(i).draw(g2d);
        }
    }
}
