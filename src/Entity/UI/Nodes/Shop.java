package Entity.UI.Nodes;

import Main.Game;

import java.awt.image.BufferedImage;

public class Shop extends Node {
    BufferedImage image;
    public Shop(Game game) {
        super(game);
        setSprite(getImg("/Resources/UI/Map/map_icon_shop.png"));
    }

}
