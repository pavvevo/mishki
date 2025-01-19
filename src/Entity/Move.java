package Entity;

import Main.Game;
import Entity.Enemy;

import java.awt.*;
import java.awt.image.BufferedImage;

import static java.lang.Math.max;

public class Move extends Entity {
    Game game;
    Enemy owner;
    Entity target;
    String name;
    BufferedImage intent;

    int display_number;

    public Move(Game game, Enemy owner, Entity target, String name) {
        this.game = game;
        this.owner = owner;
        this.target = target;
        this.name = name;
        scale = game.scale;
        getMove(name);
    }

    public void getMove(String name) {
        switch(name) {
            default: case "Attack":
                intent = getImg("/Resources/UI/Battle/intent_attack.png");
                break;
            case "Block":
                intent = getImg("/Resources/UI/Battle/intent_block.png");
                break;
        }
    }

    public void trigger() {

        int final_damage = owner.damage;
        final_damage -= game.getBuffAmmount(owner, "Intimidate");
        final_damage += game.getBuffAmmount(owner, "Anger");

        final_damage = max(0, final_damage);

        switch(name) {
            default: case "Attack":
                target.block -= final_damage;
                if(target.block <= 0) {
                    target.health += target.block;
                }

                display_number = final_damage;

                owner.x -= 7 * scale;
                owner.y += 2 * scale;
                owner.xscale = 1.5;
                owner.yscale = 0.5;
                target.xscale = 1.5;
                target.yscale = 0.5;
                target.shake = 10;

                break;
            case "Block":
                target.block += owner.block_power;

                display_number = owner.block_power;

                target.xscale = 0.5;
                target.yscale = 1.5;
                break;
            case "Anger":
                game.addBuff(target, "Anger", 2);
                target.xscale = 0.5;
                target.yscale = 1.5;
                break;
        }
    }

    public void draw(Graphics2D g2d) {
        Font currentFont = g2d.getFont();
        Font newFont = currentFont.deriveFont(currentFont.getSize() * 0.75f * scale);
        g2d.setFont(newFont);
        if(name == "Attack") {
            g2d.setColor(Color.RED);

            g2d.drawString(Integer.toString(owner.damage), 260*scale, 15*scale);
        }
        else if(name == "Block") {
            g2d.setColor(Color.BLUE);
            g2d.drawString(Integer.toString(owner.block_power), 260*scale, 15*scale);
        }

        g2d.drawImage(intent, owner.x * scale, owner.y * scale - 48 * scale, intent.getWidth() * scale, intent.getHeight() * scale, null);
        g2d.setColor(Color.BLACK);
    }
}
