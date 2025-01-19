package Entity.UI;

import Entity.Entity;
import Main.Game;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Buff extends Entity {

    BufferedImage icon;
    Game game;
    public int remaining = 1;

    public Buff(Game game, String name) {
        this.game = game;
        this.name = name;
    }

    public void setBuff() {
        switch(name) {
            case "Heads Up":
                icon = getImg("/Resources/UI/Battle/buff_heads_up.png");
                break;
        }
    }

    public void draw(Graphics2D g2d) {
        setBuff();
        g2d.drawImage(icon, x, y, icon.getWidth() * scale, icon.getHeight() * scale, null);

        g2d.setColor(Color.WHITE);
        g2d.drawString(String.valueOf(remaining), x, y + 20 * scale);
    }
}
