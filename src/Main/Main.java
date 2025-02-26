package Main;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;

public class Main {
    public static void main(String[] args) {
        JFrame window = new JFrame();
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);


        window.setResizable(false);
        window.setTitle("Foxing Flips 3D");
        Game game = new Game();
        window.add(game);

        window.pack();

        window.setLocationRelativeTo(null);
        window.setVisible(true);

        game.startGame();
    }
}
