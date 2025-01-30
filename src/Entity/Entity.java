package Entity;

import Entity.General.Tween;
import Entity.UI.Battle.Buff;
import Main.Game;
import Main.Input;

import javax.imageio.ImageIO;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.List;

public class Entity {
    public int x, y;
    public double double_x, double_y;
    public int max_health;
    public int health;
    public int block = 0;
    public String name;
    public double shake;

    public int scale = 3;
    public BufferedImage shadow;
    public BufferedImage sprite;
    public int sprite_width;
    public int sprite_height;
    public double xscale = 1.0;
    public double yscale = 1.0;
    public double target_xscale = 1.0;
    public double target_yscale = 1.0;
    public int width;
    public int height;
    Game game;

    public List<Tween> tweens;

    public int lerp_x;
    public int lerp_y;
    public double lil_sin = 0;
    public double sin_timer = 0;

    public BufferedImage getImg(String path ) {
        try {
            return ImageIO.read(getClass().getResourceAsStream(path));
        } catch (IOException e) {
            System.out.println("CANT LOAD: " + path);
            return null;
        }
    }

    public void setSprite(BufferedImage img) {
        this.sprite = img;
        sprite_width = sprite.getWidth();
        sprite_height = sprite.getHeight();
    }

    public boolean isTweenActive(String name) {
        for(Tween t : tweens) {
            if(t.name.equals(name)) {
                return true;
            }
        }
        return false;
    }

    public void updateTweens() {
        for (int i = tweens.size() - 1; i >= 0; i--) {
            Tween tween = tweens.get(i);
            if (tween.is_finished) {
                tweens.remove(i);
            } else {
                tween.update();
            }
        }
    }

    public double lerp(double a, double b, double f) {
        return (a * (1.0 - f)) + (b * f);
    }

    public boolean isHovered(Input input) {
        if(input == null) return false;

        boolean hovered = input.mouse_x > x - sprite_width / 2 && input.mouse_x < x + sprite_width / 2 &&
                input.mouse_y > y - sprite_height / 2 && input.mouse_y < y + sprite_height / 2;

        return hovered;
    }
    public boolean isClicked() {
        if(game.input.isButton(MouseEvent.BUTTON1)) {
            return true;
        }
        return false;
    }
}