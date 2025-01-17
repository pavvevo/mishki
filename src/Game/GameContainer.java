package Game;

public class GameContainer implements Runnable {

    Thread thread;
    Window window;
    Renderer renderer;
    boolean running = true;
    final double UPDATE_CAP = 1.0/ 60.0;
    int width = 320,height = 180;
    float scale = 4f;
    String title = "Flipping fox";
    public GameContainer() {

    }
    public void start() {
        window = new Window(this);
        renderer = new Renderer(this);
        thread = new Thread(this);
        thread.run();
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

            render = false;

            firstTime = System.nanoTime() / 1000000000.0;
            passedTime = firstTime - lastTime;
            lastTime = firstTime;

            unprocessedTime += passedTime;
            frameTime += passedTime;


            while (unprocessedTime >= UPDATE_CAP) {
                unprocessedTime -= UPDATE_CAP;
                render = true;
                if(frameTime >= 1.0) {
                    frameTime = 0;
                    fps = frames;
                    frames = 0;
                    System.out.println("FPS: " + fps);
                }
            }
            if(render) {
                renderer.clear();
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
    public void dispose() {

    }

    public Window getWindow() {
        return window;
    }

    public static void main(String[] args) {
        GameContainer gc = new GameContainer();
        gc.start();
    }
}
