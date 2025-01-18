package Entity.UI.Nodes;

import Main.Game;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Battle extends Node{
    BufferedImage image;
    public Battle(Game game) {
        super(game);
        setSprite(getImg("/Resources/UI/Map/map_icon_battle.png"));
    }
}
