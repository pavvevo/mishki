package Entity;

import java.awt.*;

import static java.lang.Math.sin;

public class Coin extends Entity {
    public int spin = 0;
    public int side = 0;

    public Coin() {

    }

    public void spin_coin(int spin) {
        this.spin = spin;
    }

    public void update() {
        if(spin > 0) {
            spin -= 1;

            xscale = sin(spin);
        }
    }

    public void draw(Graphics2D g2d) {
        g2d.setColor(Color.WHITE);
        int width = (int)(xscale * sprite_width * scale);
        int height = (int)(yscale * sprite_height * scale);
        g2d.drawImage(sprite, x * scale - width / 2, y * scale - height / 2, width, height, null);
    }
}
