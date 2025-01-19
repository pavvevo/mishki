package Entity.UI.Nodes;

import Main.Game;

import java.awt.image.BufferedImage;

public class Shop extends Node {
    public Shop(Game game, String name) {
        super(game, name);
        setSprite(getImg("/Resources/UI/Map/map_icon_shop.png"));
    }
    public String toString() {
        return " Shop ";
    }

}
