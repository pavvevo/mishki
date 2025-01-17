package Engine;

import Entity.Player;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;

public class GameContainer extends JPanel implements Runnable {
    //
    Player player = new Player(this);
    //

    Thread thread;
    Window window;
    Renderer renderer;
    Input input;
    AbsctractGame game;

    boolean running = true;
    final double UPDATE_CAP = 1.0/ 60.0;
    int width = 320,height = 180;
    float scale = 4f;
    String title = "Foxing Flips 3D";
    public GameContainer(AbsctractGame game) {
        this.setBackground(Color.GRAY);
        this.game = game;
    }
    public void start() {
        window = new Window(this);
        this.setBackground(Color.GRAY);
        renderer = new Renderer(this);
        input = new Input(this);
        thread = new Thread(this);
        thread.run();


        player.setup(100, 100);
    }
    public void stop() {

    }
    public void run() {
        running = true;

        boolean render = false;
        double firstTime = 0;
        double lastTime = System.nanoTime() / 1000000000.0;
        double passedTime = 0;
        double unprocessedTime = 0;

        double frameTime = 0;
        int frames = 0;
        int fps = 0;
        while (running) {
            //basic neshta za while loop-a
            render = false;

            firstTime = System.nanoTime() / 1000000000.0;
            passedTime = firstTime - lastTime;
            lastTime = firstTime;

            unprocessedTime += passedTime;
            frameTime += passedTime;

            //oshte edin while
            while (unprocessedTime >= UPDATE_CAP) {
                unprocessedTime -= UPDATE_CAP;
                render = true;
                //pishi ot tuk natam

                game.update(this, (float)UPDATE_CAP);

                input.update();
                if(frameTime >= 1.0) {
                    //
                    update();
                    repaint();
                    //
                    frameTime = 0;
                    fps = frames;
                    frames = 0;
                    System.out.println("FPS: " + fps);
                }
            }
            if(render) {

                renderer.clear();
                game.render(this,renderer);
                window.update();

                frames++;

            }
            else {
                try {
                    Thread.sleep(1);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }

            }
        }
        dispose();
    }
    //
    public void update() {
        player.update();
    }
    //
     //
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        player.draw(g2d);
        g2d.dispose();
    }
    //
    public void dispose() {

    }

    public Window getWindow() {
        return window;
    }

    public Input getInput() {
        return input;
    }
}
