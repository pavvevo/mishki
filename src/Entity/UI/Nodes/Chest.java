package Entity.UI.Nodes;

import Main.Game;

import java.awt.image.BufferedImage;

public class Chest extends Node{
    public Chest(Game game, String name) {
        super(game, name);
        setSprite(getImg("/Resources/UI/Map/map_icon_chest.png"));
    }
    public String toString() {
        return " Chest ";
    }
}
