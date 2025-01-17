package Input;

import Main.Game;


import java.awt.event.*;

public class Input implements MouseListener, MouseMotionListener, KeyListener, MouseWheelListener {

    Game gc;

    final int NUM_KEYS = 256;
    boolean[] keys = new boolean[NUM_KEYS];
    boolean[] keysLast = new boolean[NUM_KEYS];
    final int NUM_BUTTONS = 5;
    boolean[] buttons = new boolean[NUM_BUTTONS];
    boolean[] buttonsLast = new boolean[NUM_BUTTONS];

    int mouseX, mouseY;
    int scroll;

    public Input(Game gc) {
        this.gc = gc;
        mouseX = 0;
        mouseY = 0;
        scroll = 0;

      //  gc.getWindow().getCanvas().addKeyListener(this);
    }
    // \/ mouse listener
    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }


    //    \/ mouse motion listener
    @Override
    public void mouseDragged(MouseEvent e) {

    }

    @Override
    public void mouseMoved(MouseEvent e) {

    }
    //  \/ key listener

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {

    }

    @Override
    public void keyReleased(KeyEvent e) {

    }
    // \/ mouse wheel listener
    @Override
    public void mouseWheelMoved(MouseWheelEvent e) {

    }
}
