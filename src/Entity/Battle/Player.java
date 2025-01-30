package Entity.Battle;

import Entity.Entity;
import Entity.General.Particle;
import Main.Game;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Random;

import static java.lang.Math.max;
import static java.lang.Math.sin;

public class Player extends Entity {
    Game game;
    Random rand;
    BufferedImage sprite_front;
    BufferedImage sprite_back;
    BufferedImage shadow;
    int shadow_offset_x;
    int shadow_offset_y;

    BufferedImage healtbar;
    int health;
    int max_health;

    public Player(Game game) {
        this.game = game;
        scale = game.scale;
        rand = new Random();

        tweens = new ArrayList<>();
        healtbar = getImg("/Resources/UI/Battle/healthbar.png");
    }

    public void setup() {
        shake = 0;
        xscale = 1;
        yscale = 1;

        sprite_front = getImg("/Resources/Player/player_front.png");
        sprite_back = getImg("/Resources/Player/player_back.png");
        setSprite(sprite_back);

        shadow = getImg("/Resources/Other/shadow_small.png");
        shadow_offset_x = -shadow.getWidth() / 2 * scale;
        //shadow_offset_y = -6 * scale;

        max_health = 100;
        health = max_health;
    }

    public void update() {
        sin_timer += 1;
        lil_sin = sin(sin_timer / 20) / 40;
        xscale = lerp(xscale, 1.0 - lil_sin, 0.1);
        yscale = lerp(yscale, 1.0 + lil_sin, 0.1);

        if(shake > 0) shake -= 0.25;
        else shake = 0;

        updateTweens();

        if(isHovered(game.input)) {
            game.hovered_entity = this;
            if(game.input.isButtonDown(MouseEvent.BUTTON1)) {
                xscale = 1.2;
                yscale = 0.8;

                if(sprite == sprite_front) {
                    setSprite(sprite_back);
                } else {
                    setSprite(sprite_front);
                }
            }
        }
    }

    public void gotBlock() {
        yscale = 1.5;
        xscale = 0.8;

        Particle block_particle = new Particle(game);
        block_particle.double_x = x;
        block_particle.double_y = y + 2 * scale;
        block_particle.life = 5 + rand.nextInt(25);
        block_particle.direction = -90;
        block_particle.speed = 5;
        block_particle.friction = 0.9;
        block_particle.sprite = block_particle.getImg("/Resources/Other/Particles/particle_block_spark.png");
        game.particles.add(block_particle);
    }

    public void draw(Graphics2D g2d) {
        int shake_x = rand.nextInt((int)shake + 1 - (- (int)shake) ) - (int)shake;
        int shake_y = rand.nextInt((int)shake + 1 - (- (int)shake) ) - (int)shake;
        shake_x *= scale;
        shake_y *= scale;

        int width = (int)(sprite_width * scale * xscale);
        int height = (int)(sprite_height * scale * yscale);
        g2d.drawImage(shadow, x * scale + shadow_offset_x, y * scale + shadow_offset_y, shadow.getWidth() * scale, shadow.getHeight() * scale, null);
        g2d.drawImage(sprite, x * scale - width / 2 + shake_x, y * scale - height / 2 + shake_y, width, height, null);

        //healthbar
        int hb_width = healtbar.getWidth() * scale;
        int hb_height = healtbar.getHeight() * scale;
        int hb_x = x * scale - hb_width / 2;
        int hb_y = y * scale - hb_height / 2 + 32 * scale;
        g2d.setColor(Color.black);
        g2d.fillRect(hb_x + 4 * scale, hb_y + 2 * scale, hb_width - 7 * scale, hb_height - 5 * scale);
        Color red = new Color(190, 38, 51);
        g2d.setColor(red);
        int health_percent = (int)((hb_width - 7 * scale) * (health / max_health));
        g2d.fillRect(hb_x + 4 * scale, hb_y + 2 * scale, health_percent, hb_height - 5 * scale);
        g2d.drawImage(healtbar, hb_x, hb_y, hb_width, hb_height, null);
    }
}