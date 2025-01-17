package Game;

import Engine.AbsctractGame;
import Engine.GameContainer;
import Engine.Renderer;

import java.awt.event.KeyEvent;

public class GameManager extends AbsctractGame {

    public GameManager() {

    }

    @Override
    public void update(GameContainer gc, float dt) {
        if(gc.getInput().isKeyDown(KeyEvent.VK_A)) {
            System.out.println("A");
        }
    }

    @Override
    public void render(GameContainer gc, Renderer r) {

    }

    public static void main(String[] args) {
        GameContainer gc = new GameContainer(new GameManager());
        gc.start();
    }
}
