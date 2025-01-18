package Entity.UI;

import Main.Game;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;


import Entity.Entity;

import javax.imageio.ImageIO;

public class Cursor extends Entity {
    Game game;
    BufferedImage up;
    BufferedImage down;
    public Cursor(Game game) {
        this.game = game;
    }

    public void setup(int x, int y) {
        this.x = x;
        this.y = y;
        xscale = 1;
        yscale = 1;
        scale = 3;
        try {
             up = ImageIO.read(getClass().getResourceAsStream("/Resources/UI/cursor_up.png"));
             down = ImageIO.read(getClass().getResourceAsStream("/Resources/UI/cursor_up.png"));

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public  void update() {
        x = game.input.mouse_x + 15;
        y = game.input.mouse_y + 7;
        if(!game.input.isButton(MouseEvent.BUTTON1)) {
            setSprite(getImg("/Resources/UI/cursor_up.png"));
        } else {
            setSprite(getImg("/Resources/UI/cursor_down.png"));
        }
    }

    public void draw(Graphics2D g2d) {
        g2d.setColor(Color.WHITE);
        int width = (int)(xscale * sprite_width * scale);
        int height = (int)(yscale * sprite_height * scale);
        g2d.drawImage(sprite, x * scale - width / 2, y * scale - height / 2, width, height, null);
    }
}