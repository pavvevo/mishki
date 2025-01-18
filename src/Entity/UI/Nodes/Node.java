package Entity.UI.Nodes;

import Entity.Entity;
import Main.Game;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Node extends Entity {
    Game game;

    public Node(Game game) {
        this.game = game;
    }
    public void draw(Graphics2D g2d) {

        g2d.setColor(Color.WHITE);
        width = (int)(xscale * sprite_width * scale);
        height = (int)(yscale * sprite_height * scale);
        g2d.drawImage(sprite, x * scale - width / 2, y * scale - height / 2, width, height, null);

    }

    @Override
    public String toString() {
        return " alo ";
    }
}
