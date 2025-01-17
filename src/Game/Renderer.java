package Game;

import java.awt.image.DataBuffer;
import java.awt.image.DataBufferInt;

public class Renderer {

    int pW, pH;
    int[] p;

    public Renderer(GameContainer gc) {
        pW = gc.width;
        pH = gc.height;
        p = ((DataBufferInt)gc.getWindow().getImage().getRaster().getDataBuffer()).getData();
    }
    public void clear() {
        for (int i = 0; i < p.length; i++) {
            p[i] += 0;
        }
    }
}
