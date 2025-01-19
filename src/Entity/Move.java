package Entity;

import Main.Game;
import Entity.Enemy;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Move extends Entity {
    Game game;
    Enemy owner;
    Entity target;
    String name;
    BufferedImage intent;

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

        switch(name) {
            default: case "Attack":
                int total_hp = target.health + target.block;
                total_hp -= owner.damage;
                if(total_hp > target.max_health) {
                    target.block = total_hp - target.max_health;
                } else {
                    target.block = 0;
                    target.health = total_hp;
                }

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
                target.xscale = 0.5;
                target.yscale = 1.5;
                break;
        }
    }

    public void draw(Graphics2D g2d) {
        g2d.drawImage(intent, owner.x * scale, owner.y * scale - 48 * scale, intent.getWidth() * scale, intent.getHeight() * scale, null);
    }
}
