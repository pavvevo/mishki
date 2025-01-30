package Entity.Battle;

import Entity.Entity;
import Entity.General.Particle;
import Main.Game;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Random;

import static java.lang.Math.*;

public class Enemy extends Entity {
    Game game;
    Player player;
    Random rand;
    BufferedImage sprite;
    BufferedImage shadow;
    int shadow_offset_x;
    int shadow_offset_y;

    BufferedImage healtbar;

    int health;
    int max_health;

    public Enemy(Game game, Player player) {
        this.game = game;
        this.player = player;
        scale = game.scale;
        rand = new Random();

        tweens = new ArrayList<>();
        healtbar = getImg("/Resources/UI/Battle/healthbar.png");
    }

    public void setup(String name) {
        shake = 0;
        xscale = 1;
        yscale = 1;
        this.name = name;

        switch(name) {
            case "Mouse":
                sprite = getImg("/Resources/Enemy/mouse.png");
                setSprite(sprite);

                shadow = getImg("/Resources/Other/shadow_small.png");
                shadow_offset_x = -shadow.getWidth() / 2 * scale;
                shadow_offset_y = -6 * scale;

                max_health = 100;
                health = max_health;
            break;
        }
    }

    public void update() {
        sin_timer += 1;
        lil_sin = sin(sin_timer / 10) / 20;
        xscale = lerp(xscale, 1.0 - lil_sin, 0.1);
        yscale = lerp(yscale, 1.0 + lil_sin, 0.1);

        if(shake > 0) shake -= 0.25;
        else shake = 0;

        updateTweens();

        sprite_height /= 2;
        if(isHovered(game.input)) {
            game.hovered_entity = this;
            if(game.input.isButtonDown(MouseEvent.BUTTON1)) {
                xscale = 1.2;
                yscale = 0.8;

                gotAttacked();
            }
        }

        sprite_height *= 2;
    }

    public void gotAttacked() {
        xscale = 0.5;
        yscale = 1.5;
        shake = 7;

        int rnd_angle = rand.nextInt(360);
        int rnd_x = rand.nextInt(4) * scale - 2 * scale;
        int rnd_y = rand.nextInt(4) * scale - 2 * scale;
        for(int i = 0; i < 8; i++) {
            Particle test_particle = new Particle(game);
            test_particle.double_x = x + rnd_x;
            test_particle.double_y = y - 4 * scale + rnd_y;
            test_particle.life = 10;
            test_particle.direction = (360 / 8) * i + rnd_angle;
            test_particle.angle = test_particle.direction;
            test_particle.speed = 7;
            test_particle.friction = 0.9;
            test_particle.sprite = test_particle.getImg("/Resources/Other/Particles/particle_spark.png");
            test_particle.end_fade = false;
            test_particle.end_shrink = true;
            game.particles.add(test_particle);
        }
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
        int hb_y = y * scale - hb_height / 2  - 56 * scale;
        g2d.setColor(Color.black);
            g2d.fillRect(hb_x + 4 * scale, hb_y + 2 * scale, hb_width - 7 * scale, hb_height - 5 * scale);
        Color red = new Color(190, 38, 51);
        g2d.setColor(red);
        int health_percent = (int)((hb_width - 7 * scale) * ((double)health / (double)max_health));
            g2d.fillRect(hb_x + 4 * scale, hb_y + 2 * scale, health_percent, hb_height - 5 * scale);
            g2d.drawImage(healtbar, hb_x, hb_y, hb_width, hb_height, null);
    }
}
