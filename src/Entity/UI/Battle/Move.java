package Entity.UI.Battle;

import Entity.Entity;
import Main.Game;

import java.awt.*;
import java.awt.image.BufferedImage;

import static java.lang.Math.max;

public class Move extends Entity {
    Game game;
    Enemy owner;
    Entity target;
    String name;
    BufferedImage intent;

    int display_number;

    public Move(Game game, Enemy owner, Entity target, String name) {
        this.game = game;
        this.owner = owner;
        this.target = target;
        this.name = name;
        scale = game.scale;
        getMove(name);
    }

    public void getMove(String name) {

    }

    public void trigger() {

    }

    public void draw(Graphics2D g2d) {

    }
}
