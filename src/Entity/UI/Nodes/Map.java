package Entity.UI.Nodes;

import Entity.UI.Card;
import Main.Game;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
public class Map {
    Node[][] m = new Node[10][5];
    Game game;
    int nodeX, nodeY;
    public Map(Game game) {
        this.game = game;
    }
    public void setup() {

        nodeX = 220/m[9].length/7 * game.scale;
        nodeY = 180/m.length/9 * game.scale;
        m[9][2] = new Boss(game);
        createNodes(m);
        for(int i = m.length-1; i >= 0; i--) {
            for(int j = 0; j < m[i].length; j++) {
                System.out.print(m[i][j]);
                if(m[i][j] != null) {
                    m[i][j].x += (j*nodeX + 50 * game.scale) - 25;
                    m[i][j].y += (i*nodeY * game.scale) * -1 + 170;
                }

            }
            System.out.println("///");
        }
        pathsUp(m);
    }

    public void createNodes(Node[][] m) {
        for(int i = 0; i < m.length; i++) {
            for(int j = 0; j < m[i].length; j++) {
                //create a node object
                if(i == 0) {
                    m[i][j] = ifDoNode1stLevel();
                }
                else if(i < m.length - 1) {
                    m[i][j] = ifDoNode();

                }


                int br = 0;
                if(i > 0) {
                    for(int k = 0; k < m[i-1].length; k++) {
                        if(m[i-1][k] != null) {
                            br++;
                        }
                    }
                    if(br == 0) {
                        i -= 1;

                    }
                    br = 0;
                }



            }

        }
    }
    public Node battleNode() {
        Battle k1 = new Battle(game);
        return k1;
    }
    public Node ifDoNode1stLevel() {
            Random random = new Random();
            int randomNumber = random.nextInt(100 + 2 - 1) + 2;
            if(randomNumber < 20) {
                return battleNode();
            }
            return null;
    }


public Node ifDoNode() {
        Random random = new Random();
         int randomNumber = random.nextInt(100 + 2 - 1) + 2;
         if(randomNumber < 20) {
             return randomNode();
         }
        return null;
}
//runva dali ima <= 2 puteki ako nqma slaga puteka nazad runva otnovo ako pak ima s <=2 puteki slaga i nagore
    //proverqva vsichki sledvashti redove za da nameri nai-blizkiq node, no samo ako trite pred nego sa prazni
    //   /\ moje da se polzva i nazad za da nameri zadniq node
    public Node randomNode() {
        Random random = new Random();
        int randomNumber = random.nextInt(100 + 1 - 2) + 2;
        if(randomNumber < 75) {
            Battle k1 = new Battle(game);
            return k1;
        }
        else if(randomNumber <= 90 && randomNumber >= 75) {
            Shop k2 = new Shop(game);
            return k2;
        }
        else if(randomNumber > 90) {
            Chest k3 = new Chest(game);
            return k3;
        }
        return null;
    }

    public void pathsUp(Node[][] m) {
        for(int i = 0; i < m.length-1; i++) {
            for (int j = 0; j < m[i].length; j++) {
                if(m[i][j] != null) {
                    if(m[i][j] == m[i][0]) {
                        //check if adjacent spaces have nodes
                        if(m[i+1][j] != null) {
                            //connect
                            m[i][j].addConnection(m[i+1][j]);

                        }
                        else if(m[i+1][j+1] != null) {
                            //connect
                            m[i][j].addConnection(m[i+1][j+1]);
                        }
                        else {
                            //method check distance
                        }

                    }
                    else if(m[i][j] == m[i][4]) {
                        //check if adjacent spaces have nodes
                        if(m[i+1][j] != null) {
                            m[i][j].addConnection(m[i+1][j]);
                        }
                        else if(m[i+1][j-1] != null) {
                            m[i][j].addConnection(m[i+1][j-1]);
                        }
                        else {
                            //method check distance
                        }

                    }
                    else {
                        //check if adjacent spaces have nodes
                        if(m[i+1][j] != null) {
                            m[i][j].addConnection(m[i+1][j]);
                        }
                        else if(m[i+1][j-1] != null) {
                            m[i][j].addConnection(m[i+1][j-1]);
                        }
                        else if(m[i+1][j+1] != null) {
                            m[i][j].addConnection(m[i+1][j+1]);
                        }
                        else {
                            //method check distance
                        }

                    }
                }
            }
        }
    }









    public void draw(Graphics2D g2d) {

        g2d.setColor(Color.WHITE);
        for(int i = 0; i < m.length; i++) {
            for(int j = 0; j < m[i].length; j++) {
                if(m[i][j] != null) {
                    m[i][j].draw(g2d);
                }

            }
        }
    }
    public void update() {

    }
}
