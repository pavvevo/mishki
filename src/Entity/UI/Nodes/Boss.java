package Entity.UI.Nodes;

import Main.Game;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Boss extends Node{
    String name = "Boss";
    public Boss(Game game) {
        super(game);
        setSprite(getImg("/Resources/UI/Map/map_icon_boss.png"));
    }

    public String toString() {
        return "Boss";
    }
}