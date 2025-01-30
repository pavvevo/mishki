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
    public double shrink_xscale = 0.0;
    public double shrink_yscale = 0.0;
    public boolean end_shrink = false;
    public boolean end_fade = true;

    public BufferedImage sprite;
    public double alpha = 1.0;
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

        xscale -= shrink_xscale;
        yscale -= shrink_yscale;
        if(xscale < 0.0) xscale = 0.0;
        if(yscale < 0.0) yscale = 0.0;

        x = (int)(double_x * scale);
        y = (int)(double_y * scale);
        life -= 1;
        if(life <= 0) {
            if(end_shrink) {
                sprite_scale = lerp(sprite_scale, 0.0, 0.2);
                if (sprite_scale < 0.1) {
                    finished = true;
                }
            } else if(end_fade) {
                alpha -= 0.05;
                if(alpha <= 0.0) finished = true;
            } else {
                finished = true;
            }
        }
    }

    public void draw(Graphics2D g2d) {
        if(finished) return;

        int width = (int)(sprite.getWidth() * scale * sprite_scale * xscale);
        int height = (int)(sprite.getHeight() * scale * sprite_scale * yscale);

        Composite originalComposite = g2d.getComposite();
        float final_alpha = (float)clamp(alpha, 0.0, 1.0);
        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, final_alpha));

        g2d.rotate(toRadians(angle), x, y);
        g2d.drawImage(sprite, x - width / 2, y - height / 2, width, height, null);
        g2d.rotate(-toRadians(angle), x, y);

        g2d.setComposite(originalComposite);
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
