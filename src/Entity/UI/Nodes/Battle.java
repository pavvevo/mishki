package Entity.UI.Nodes;

import Main.Game;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Battle extends Node{
    public Battle(Game game, String name) {
        super(game, name);
        setSprite(getImg("/Resources/UI/Map/map_icon_battle.png"));
    }
    public String toString() {
        return " Battle ";
    }

    @Override
    public void update() {
        super.update();
//        if(!isAvailable) {
//            System.out.println("aaaa");
//            setSprite(getImg("/Resources/UI/Map/map_icon_battle_gray.png"));
//
//        }
//        else {
//            setSprite(getImg("/Resources/UI/Map/map_icon_battle.png"));
//        }


    }
}
