package Entity.UI.Nodes;

import Main.Game;

import java.awt.image.BufferedImage;

public class Chest extends Node{
    BufferedImage image;
    public Chest(Game game) {
        super(game);
        setSprite(getImg("/Resources/UI/Map/map_icon_chest.png"));
    }

}
