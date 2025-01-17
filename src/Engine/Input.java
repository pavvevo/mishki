package Engine;


import java.awt.event.*;

public class Input implements MouseListener, MouseMotionListener, KeyListener, MouseWheelListener {

    GameContainer gc;

    final int NUM_KEYS = 256;
    boolean[] keys = new boolean[NUM_KEYS];
    boolean[] keysLast = new boolean[NUM_KEYS];
    final int NUM_BUTTONS = 5;
    boolean[] buttons = new boolean[NUM_BUTTONS];
    boolean[] buttonsLast = new boolean[NUM_BUTTONS];

    int mouseX, mouseY;
    int scroll;

    public Input(GameContainer gc) {
        this.gc = gc;
        mouseX = 0;
        mouseY = 0;
        scroll = 0;

        gc.window.getCanvas().addKeyListener(this);
        gc.window.getCanvas().addMouseMotionListener(this);
        gc.window.getCanvas().addMouseListener(this);
        gc.window.getCanvas().addMouseWheelListener(this);
    }
    public void update() {
        scroll = 0;
        for (int i = 0; i < NUM_KEYS; i++) {
            keysLast[i] = keys[i];
        }
        for (int i = 0; i < NUM_BUTTONS; i++) {
            buttonsLast[i] = buttons[i];
        }
    }

    public boolean isKey(int keyCode) {
        return keys[keyCode];
    }
    public boolean isKeyUp(int keyCode) {
        return !keys[keyCode] && keysLast[keyCode];
    }
    public boolean isKeyDown(int keyCode) {
        return keys[keyCode] && !keysLast[keyCode];
    }
    public boolean isButton(int button) {
        return buttons[button];
    }
    public boolean isButonUp(int button) {
        return !buttons[button] && buttonsLast[button];
    }
    public boolean isButtonDown(int button) {
        return buttons[button] && !buttonsLast[button];
    }

    // \/ mouse listener
    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {
        buttonsLast[e.getButton()] = true;
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        buttonsLast[e.getButton()] = false;
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
        mouseX = (int)(e.getX() / gc.scale);
        mouseY = (int)(e.getY() / gc.scale);
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        mouseX = (int)(e.getX() / gc.scale);
        mouseY = (int)(e.getY() / gc.scale);
    }
    //  \/ key listener

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        keys[e.getKeyCode()] = true;
    }

    @Override
    public void keyReleased(KeyEvent e) {
        keys[e.getKeyCode()] = false;
    }
    // \/ mouse wheel listener
    @Override
    public void mouseWheelMoved(MouseWheelEvent e) {
        scroll = e.getWheelRotation();
    }
}
