package Entity.UI.Nodes;

import Main.Game;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Boss extends Node{
    public Boss(Game game, String name) {
        super(game, name);
        setSprite(getImg("/Resources/UI/Map/map_icon_boss.png"));
    }

    public String toString() {
        return "Boss";
    }
}