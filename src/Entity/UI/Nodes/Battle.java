package Entity.UI.Nodes;

import Main.Game;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Battle extends Node{
    String name = "Battle";
    public Battle(Game game) {
        super(game);
        setSprite(getImg("/Resources/UI/Map/map_icon_battle.png"));
    }
    public String toString() {
        return " Battle ";
    }
}
