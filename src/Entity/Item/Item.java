package Entity.Item;

import java.awt.image.BufferedImage;

public abstract class Item {

    int id;
    String name;
    BufferedImage sprite;

    public Item(String name, int id) {
        this.id = id;
        this.name = name;
    }

    public abstract void onBattleStart();
    public abstract void onTurnStart();
    public abstract void onTurnEnd();
}