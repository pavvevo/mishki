package Entity.Battle;

import Entity.Entity;
import Entity.General.Particle;
import Main.Game;

import java.awt.*;
import java.awt.image.BufferedImage;

import static java.lang.Math.max;

public class Move extends Entity {
    Game game;
    Entity owner;
    Entity target;
    String name;
    BufferedImage intent;
    int move_length = 1;
    int frame = 0;
    public boolean finished = false;

    public Move(Game game, Entity owner, Entity target, String name, int move_length) {
        this.game = game;
        this.owner = owner;
        this.target = target;
        this.name = name;
        this.move_length = move_length;
        scale = game.scale;
    }

    public void update() {

        if(finished) return;

        Enemy enemy_target = null;
        if(target instanceof Enemy) { enemy_target = (Enemy) target; }
        Player player_target = null;
        if(target instanceof Player) { player_target = (Player) target; }

        if (owner instanceof Card) {
            Card card_owner = (Card) owner;
            if(frame == 0) cardEffect(card_owner);

            switch (name) {
                case "Rock Throw":
                    if (frame == 10) {
                        enemy_target.health -= 5;
                        enemy_target.gotAttacked();
                    }
                    break;
                case "Tail Defence":
                    if (frame == 10) {
                        player_target.block += 5;
                        player_target.gotBlock();
                    }
                    break;
                case "Dull Claw":
                    if (frame == 10) {
                        enemy_target.gotAttacked();
                    }
                    break;
            }
        }

        //enemy
        switch(name) {
            case "Attack":
                move_length = 60;
                break;
        }

        frame += 1;
        if(frame > move_length) {
            finished = true;
        }
    }

    void cardEffect(Card card_owner) {
        Particle back_particle = new Particle(game);
        back_particle.double_x = card_owner.x;
        back_particle.double_y = card_owner.y;
        back_particle.life = 15;
        back_particle.direction = -90;
        back_particle.speed = 5;
        back_particle.friction = 0.9;
        back_particle.sprite = card_owner.card_background;
        game.particles.add(back_particle);

        Particle front_particle = new Particle(game);
        front_particle.double_x = card_owner.x;
        front_particle.double_y = card_owner.y;
        front_particle.life = 15;
        front_particle.direction = -90;
        front_particle.speed = 5;
        front_particle.friction = 0.9;
        front_particle.sprite = card_owner.card_front;
        game.particles.add(front_particle);
        card_owner.my_holder.removeCard(card_owner);
    }

    public void draw(Graphics2D g2d) {

    }
}
