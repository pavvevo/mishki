package Entity.UI.Battle;

import Entity.Entity;
import Main.Game;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Buff extends Entity {

    BufferedImage icon;
    Game game;
    public int remaining;
    boolean has_icon = false;

    public Buff(Game game, String name) {
        this.game = game;
        this.name = name;
    }

    public void setBuff() {
        switch(name) {
            case "Heads Up":
                icon = getImg("/Resources/UI/Battle/Buffs/buff_heads_up.png");
                has_icon = true;
                break;
            case "Intimidate":
                icon = getImg("/Resources/UI/Battle/Buffs/buff_intimidate.png");
                has_icon = true;
                break;
            case "New Stick":
                icon = getImg("/Resources/UI/Battle/Buffs/buff_new_stick.png");
                has_icon = true;
                break;
            case "Guard":
                icon = getImg("/Resources/UI/Battle/Buffs/buff_guard.png");
                has_icon = true;
                break;
            case "Anger":
                icon = getImg("/Resources/UI/Battle/Buffs/buff_anger.png");
                has_icon = true;
                break;
            case "Poison":
                icon = getImg("/Resources/UI/Battle/Buffs/buff_poison.png");
                has_icon = true;
                break;
        }
    }

    public void draw(Graphics2D g2d) {
        setBuff();
        if(!has_icon) return;

        g2d.drawImage(icon, x, y, icon.getWidth() * scale, icon.getHeight() * scale, null);

        g2d.setColor(Color.WHITE);
        g2d.drawString(String.valueOf(remaining), x, y + 20 * scale);
    }
}
