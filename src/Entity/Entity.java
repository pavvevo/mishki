package Entity;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Entity {
    public int x, y;
    public int max_health;
    public int health;

    public int scale;
    BufferedImage sprite;
    public int sprite_width;
    public int sprite_height;
    public double xscale;
    public double yscale;


    public void getSprite(String path ) {
        try {
            BufferedImage img = ImageIO.read(getClass().getResourceAsStream(path));
            setSprite(img);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setSprite(BufferedImage sprite) {
        this.sprite = sprite;
        sprite_width = sprite.getWidth();
        sprite_height = sprite.getHeight();
    }

    public double lerp(double a, double b, double f)
    {
        return (a * (1.0 - f)) + (b * f);
    }
}