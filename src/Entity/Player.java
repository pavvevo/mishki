package Entity;

import Main.Game;

import java.awt.*;

public class Player extends Entity {
    Game game;

    public Player(Game game) {
        this.game = game;
    }

    public void setup(int x, int y) {
        this.x = x;
        this.y = y;
        max_health = 20;
        health = max_health;
        xscale = 1.5;
        yscale = 0.5;
        scale = 3;
        getSprite("/Resources/player.png");
    }

    public void update() {
        xscale = lerp(xscale, 1.0, 0.1);
        yscale = lerp(yscale, 1.0, 0.1);
    }

    public void draw(Graphics2D g2d) {
        g2d.setColor(Color.WHITE);
        int width = (int)(xscale * sprite_width * scale);
        int height = (int)(yscale * sprite_height * scale);
        g2d.drawImage(sprite, x * scale - width / 2, y * scale - height / 2, width, height, null);
    }
}
