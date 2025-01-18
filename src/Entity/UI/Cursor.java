package Entity.UI;

import Main.Game;
import java.awt.*;
import Entity.Entity;

public class Cursor extends Entity {
    Game game;

    public Cursor(Game game) {
        this.game = game;
    }

    public void setup(int x, int y) {
        this.x = x;
        this.y = y;
        xscale = 1;
        yscale = 1;
        scale = 3;
        getSprite("/Resources/cursor_up.png");
    }

    public  void update() {
        x = game.input.mouse_x + 16;
        y = game.input.mouse_y + 9;
    }

    public void draw(Graphics2D g2d) {
        g2d.setColor(Color.WHITE);
        int width = (int)(xscale * sprite_width * scale);
        int height = (int)(yscale * sprite_height * scale);
        g2d.drawImage(getSprite(), x * scale - width / 2, y * scale - height / 2, width, height, null);
    }
}