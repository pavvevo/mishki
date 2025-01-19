package Entity.UI.Other;

import Entity.UI.Nodes.Node;
import Main.Game;
import Entity.Entity;
import java.awt.*;
import java.awt.event.MouseEvent;

public class Chest_State extends Entity {
    Game game;
    int scale;
    public Chest_State(Game game) {
        this.game = game;
    }
    public void setup() {
        scale = game.scale;
        x = game.screen_width;
        y = game.screen_height;
    }
    public void draw(Graphics g2d) {
        Font currentFont = g2d.getFont();
        Font newFont = currentFont.deriveFont(currentFont.getSize() * 1f * scale);
        g2d.setFont(newFont);
        g2d.setColor(Color.white);
        g2d.drawString("+1 Roll!", 120 * scale, 75 * scale);
    }
    public void update() {
        //alo
        if(game.input.isButtonDown(MouseEvent.BUTTON1)) {
            game.max_tosses++;
            game.tosses = game.max_tosses;
            game.State = game.State.MAP;
        }
    }
}
