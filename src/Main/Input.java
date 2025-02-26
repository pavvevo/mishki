package Main;

import java.awt.*;
import java.awt.event.*;

public class Input implements MouseListener, MouseMotionListener {

    Game game;

    final int NUM_BUTTONS = 5;
    public boolean[] buttons = new boolean[NUM_BUTTONS];
    public boolean[] buttonsLast = new boolean[NUM_BUTTONS];
    public int mouse_x, mouse_y;








    public Input(Game game) {
        this.game = game;
        mouse_x = 0;
        mouse_y = 0;

        game.addMouseMotionListener(this);
        game.addMouseListener(this);
    }
    public void update() {
        for (int i = 0; i < NUM_BUTTONS; i++) {
            buttonsLast[i] = buttons[i];
        }
    }
    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {
        buttons[e.getButton()] = true;
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        buttons[e.getButton()] = false;
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
        mouse_x = (int)(e.getX() / game.scale);
        mouse_y = (int)(e.getY() / game.scale);
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        mouse_x = (int)(e.getX() / game.scale);
        mouse_y = (int)(e.getY() / game.scale);
    }

    public boolean isButton(int key) {
        return buttons[key];
    }

    public boolean isButtonUp(int key) {
        return !buttons[key] && buttonsLast[key];
    }

    public boolean isButtonDown(int key) {
        return buttons[key] && !buttonsLast[key];
    }

}