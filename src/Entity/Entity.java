package Entity;

import Main.Game;
import Main.Input;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class Entity {
    public int x, y;
    public int max_health;
    public int health;
    public String name;

    public int scale;
    public BufferedImage sprite;
    public int sprite_width;
    public int sprite_height;
    public double xscale;
    public double yscale;
    public int width;
    public int height;
    Game game;

    public BufferedImage getImg(String path ) {
        try {
            BufferedImage img = ImageIO.read(getClass().getResourceAsStream(path));
            return img;
        } catch (IOException e) {
            System.out.println("CANT LOAD");
            return null;
        }
    }

    public void setSprite(BufferedImage img) {
        this.sprite = img;
        sprite_width = sprite.getWidth();
        sprite_height = sprite.getHeight();
    }

    public double lerp(double a, double b, double f) {
        return (a * (1.0 - f)) + (b * f);
    }

    public boolean isHovered(Input input) {
        if(input == null) return false;

        return input.mouse_x > x - width / 2 && input.mouse_x < x + width / 2
                && input.mouse_y > y - height / 2 && input.mouse_y < y + height / 2;
    }
}