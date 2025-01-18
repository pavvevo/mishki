package Entity;

import Main.Game;
import java.awt.*;
import java.awt.image.BufferedImage;

import static java.lang.Math.abs;
import static java.lang.Math.sin;

public class Coin extends Entity {
    public int spin = 0;
    public int side = 0;

    public BufferedImage heads;
    public BufferedImage tails;

    Game game;

    public Coin(Game game) {
        this.game = game;
    }

    public void setup(int x, int y) {
        this.x = x;
        this.y = y;
        xscale = 1;
        yscale = 1;
        name = "Coin";
        scale = game.scale;
        heads = getImg("/Resources/UI/coin_heads.png");
        tails = getImg("/Resources/UI/coin_tails.png");
        setSprite(heads);
    }

    public void spin_coin(int spin) {
        this.spin = spin;
    }

    public void update() {

        if(isHovered(game.input) && game.input.buttons[0]) {
            spin_coin(180);
        }

        if(spin > 0) {
            spin -= 1;
        }
    }

    public void draw(Graphics2D g2d) {
        g2d.setColor(Color.WHITE);
        width = (int)(xscale * sprite_width * scale);
        height = (int)(yscale * sprite_height * scale);
        g2d.drawImage(sprite, x * scale - width / 2, y * scale - height / 2, width, height, null);
    }
}
