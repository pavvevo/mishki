package Entity.UI.Nodes;

import Main.Game;

import java.awt.*;
import java.util.ArrayList;
import java.util.Random;

public class Map {
    ArrayList<Node> nodes = new ArrayList<Node>();
    Node[][] m = new Node[10][5];

    Game game;
    int node_level;
    public Map(Game game) {
    }
    public void setup() {
        m[9][2] = new Boss(game);
        createNodes(m);
        for(int i = 0; i < m.length; i++) {
            for(int j = 0; j < m[i].length; j++) {
                System.out.print(m[i][j]);
            }
            System.out.println("///");
        }
    }

    public void createNodes(Node[][] m) {
        for(int i = 0; i < m.length-1; i++) {
            for(int j = 0; j < m[i].length; j++) {
                    m[i][j] = ifDoNode();
            }
            //check if last level is null and return if so
            int br = 0;
            if(i > 0) {
                for(int k = 0; k < m[i-1].length; k++) {
                    if(m[i][k] != null) {
                        br++;
                    }
                }
                if(br == 0) {
                    i -= 1;
                    br = 0;
                }
            }
        }
    }


public Node ifDoNode() {
        Random random = new Random();
         int randomNumber = random.nextInt(100 + 1 - 1) + 1;
         if(randomNumber < 30) {
             return randomNode();
         }
        return null;
}

    public Node randomNode() {
        Random random = new Random();
        int randomNumber = random.nextInt(100 + 1 - 1) + 1;
        if(randomNumber < 65) {
            Battle k1 = new Battle(game);
            return k1;
        }
        else if(randomNumber < 90 && randomNumber > 65) {
            Shop k2 = new Shop(game);
            return k2;
        }
        else if(randomNumber > 90) {
            Chest k3 = new Chest(game);
            return k3;
        }
        return null;
    }

    public void draw(Graphics2D g2d) {
        g2d.setColor(Color.WHITE);
        for(int i = 0; i < m.length; i++) {
            for(int j = 0; j < m[i].length; j++) {
                if(m[i][j] != null) {
                    g2d.drawRect(100,200,200,200);
                    m[i][j].draw(g2d);
                }

            }
        }
    }
    public void update() {

    }
}
