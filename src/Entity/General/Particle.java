package Entity.General;

import Main.Game;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

import static java.lang.Math.*;

public class Particle {

    Game game;
    int scale;

    public double sprite_scale = 1.0;
    public double xscale = 1.0;
    public double yscale = 1.0;

    public BufferedImage sprite;
    public int x, y;
    public double double_x, double_y;
    public double direction;
    public double angle;
    public double speed;
    public double friction;
    public int life;

    public boolean finished = false;

    public Particle(Game game) {
        this.game = game;
        this.scale = game.scale;
    }

    public double lerp(double a, double b, double f) {
        return (a * (1.0 - f)) + (b * f);
    }

    public void update() {
        if(finished) return;

        double_x += cos(toRadians(direction)) * speed;
        double_y += sin(toRadians(direction)) * speed;
        speed *= friction;

        x = (int)(double_x * scale);
        y = (int)(double_y * scale);
        life -= 1;
        if(life <= 0) {
            sprite_scale = lerp(sprite_scale, 0.0, 0.1);
            if(sprite_scale < 0.1) {
                finished = true;
            }
        }
    }

    public void draw(Graphics2D g2d) {
        if(finished) return;

        int width = (int)(sprite.getWidth() * scale * sprite_scale * xscale);
        int height = (int)(sprite.getHeight() * scale * sprite_scale * yscale);

        g2d.rotate(toRadians(angle), x, y);
        g2d.drawImage(sprite, x - width / 2, y - height / 2, width, height, null);
        g2d.rotate(-toRadians(angle), x, y);
    }

    public BufferedImage getImg(String path ) {
        try {
            return ImageIO.read(getClass().getResourceAsStream(path));
        } catch (IOException e) {
            System.out.println("CANT LOAD:" + path);
            return null;
        }
    }
}
