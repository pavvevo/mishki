package Entity.UI.Nodes;

import Main.Game;

import javax.imageio.ImageIO;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.awt.*;
import java.util.Random;

import static java.lang.Math.clamp;

public class Map {
    List<Node> connectionlist;
    Node[][] m = new Node[10][5];
    Game game;
    int nodeX, nodeY;
    int playerclickbr = 0;

    public int scroll_y = 0;
    double scroll_y_double = 0;
    int scroll_lerp_y = 0;
    int scroll_anchor = 0;
    boolean what_fix = false;
    BufferedImage map_image;

    public Map(Game game) {
        this.game = game;
    }
    public void setup() {
        connectionlist = new ArrayList<Node>();
        nodeX = 220/m[9].length/3 * game.scale;
        nodeY = 180/m.length/6 * game.scale;
        m[9][2] = new Boss(game, "Boss");
        createNodes(m);
        for(int i = m.length-1; i >= 0; i--) {
            for(int j = 0; j < m[i].length; j++) {
                if(m[i][j] != null) {
                    m[i][j].x += (j*nodeX + 50 * game.scale) - 80;
                    m[i][j].y += (i*nodeY * game.scale) * -1 + 300;
                }

            }
        }
        pathsUp(m);
        pathsDown(m);

        try {
            map_image = ImageIO.read(getClass().getResourceAsStream("/Resources/Other/Backgrounds/map.png"));
        } catch (IOException e) {
            System.out.println("CANT LOAD MAP IMAGE");
        }
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
        Battle k1 = new Battle(game, "Battle");
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
    public Node randomNode() {
        Random random = new Random();
        int randomNumber = random.nextInt(100 + 1 - 2) + 2;
        if(randomNumber < 75) {
            Battle k1 = new Battle(game, "Battle");
            return k1;
        }
        else if(randomNumber <= 90 && randomNumber >= 75) {
            //tuk beshe shop
            Battle k2 = new Battle(game, "Battle");
            return k2;
        }
        else if(randomNumber > 90) {
            Chest k3 = new Chest(game, "Chest");
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
                            m[i][j].addConnection(m[checkDistX(m[i][j], m, i)][checkDistY(m[i][j], m, i)]);

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
                            m[i][j].addConnection(m[checkDistX(m[i][j], m, i)][checkDistY(m[i][j], m, i)]);
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
                            m[i][j].addConnection(m[checkDistX(m[i][j], m, i)][checkDistY(m[i][j], m, i)]);
                        }

                    }
                }
            }
        }
    }
    public void pathsDown(Node[][] m) {
        for(int i = 1; i < m.length-1; i++) {
            for (int j = 0; j < m[i].length; j++) {
                if(m[i][j] != null) {
                    if(m[i][j].recievepath == 0) {
                        m[checkDistX(m[i][j], m, -i)]     [checkDistY(m[i][j], m, -i)].addConnection(m[i][j]);
                    }
                }
            }
        }
    }
    public int checkDistX(Node node, Node[][] m, int currI) {
        double dist = 1000000000;
        double tempdist = 0;
        int x = 0;
        if(currI >= 0) {
            for(int i = currI+1; i < m.length; i++) {
                for (int j = 0; j < m[i].length; j++) {
                    if(m[i][j] != null) {
                        tempdist = Math.sqrt(Math.pow(m[i][j].x - node.x, 2) + Math.pow(m[i][j].y - node.y, 2));
                        if(tempdist < dist) {
                            dist = tempdist;
                            x = i;
                        }
                    }
                }
            }
            return x;
        }
        else {
            currI *= -1;
            for(int i = m.length-(m.length - currI)-1; i >= 0; i--) {
                for (int j = 0; j < m[i].length; j++) {
                    if(m[i][j] != null) {
                        tempdist = Math.sqrt(Math.pow(m[i][j].x - node.x, 2) + Math.pow(m[i][j].y - node.y, 2));
                        if(tempdist < dist) {
                            dist = tempdist;
                            x = i;
                        }
                    }
                }
            }
            return x;
        }
    }
    public int checkDistY(Node node, Node[][] m, int currI) {
        double dist = 1000000000;
        double tempdist = 0;
        int y = 0;
        if(currI >= 0) {
            for(int i = currI+1; i < m.length; i++) {
                for (int j = 0; j < m[i].length; j++) {
                    if(m[i][j] != null) {
                        tempdist = Math.sqrt(Math.pow(m[i][j].x - node.x, 2) + Math.pow(m[i][j].y - node.y, 2));
                        if(tempdist < dist) {
                            dist = tempdist;
                            y = j;

                        }
                    }
                }
            }
            return y;
        }
        else {
            currI *= -1;
            for(int i = m.length-(m.length - currI)-1; i >= 0; i--) {
                for (int j = 0; j < m[i].length; j++) {
                    if(m[i][j] != null) {
                        tempdist = Math.sqrt(Math.pow(m[i][j].x - node.x, 2) + Math.pow(m[i][j].y - node.y, 2));
                        if(tempdist < dist) {
                            dist = tempdist;
                            y = j;

                        }
                    }
                }
            }
            return y;
        }

    }

    public void draw(Graphics2D g2d) {

        g2d.drawImage(map_image, 0, scroll_y * game.scale, map_image.getWidth() * game.scale, map_image.getHeight() * game.scale, null);

        for(int i = 0; i < m.length; i++) {
            for(int j = 0; j < m[i].length; j++) {
                if(m[i][j] != null) {
                    m[i][j].draw(g2d, scroll_y);
                }

            }
        }
        for(int i = 0; i < m.length; i++) {
            for (int j = 0; j < m[i].length; j++) {
                if(m[i][j] != null) {
                    m[i][j].draw2(g2d, scroll_y);
                }
            }
        }

    }

    public double lerp(double a, double b, double f) {
        return (a * (1.0 - f)) + (b * f);
    }

    public void update() {

        if(game.input.isButtonDown(MouseEvent.BUTTON1)) {
            scroll_anchor = game.input.mouse_y - scroll_y;
        }
        if(game.input.isButton(MouseEvent.BUTTON1)) {
            scroll_lerp_y = game.input.mouse_y - scroll_anchor;
        }

        scroll_lerp_y = clamp(scroll_lerp_y ,-map_image.getHeight() / 2 - 20, 20);

        scroll_y_double = lerp(scroll_y_double, scroll_lerp_y, 0.1);
        scroll_y = (int)scroll_y_double;

//        if(playerclickbr == 0) {
//            for (int j = 0; j < m[9].length; j++) {
//                if (m[0][j] != null) {
//                    m[0][j].update();
//                    m[0][j].isAvailable = true;
//                    if(m[0][j].isClicked()) {
//                        playerClick(m[0][j]);
//                    }
//                }
//            }
//        }
//        else {
//            for(int i = 0; i < connectionlist.size(); i++) {
//                connectionlist.get(i).update();
//                connectionlist.get(i).isAvailable = true;
//                if(connectionlist.get(i).isClicked()) {
//
//                    playerClick(connectionlist.get(i));
//                }
//            }
//        }
    }
    public void playerClick(Node node) {
        playerclickbr++;
        node.isAvailable = false;
        node.xscale = 1;
        node.yscale = 1;
        connectionlist = node.getConnections();
        switch(node.name) {
            default:

                break;
            case "Battle":
                game.changeState(Game.STATE.GAME);
                //game.deck.at_shop = false;
                break;
            case "Boss":
                break;
            case "Shop":
                game.changeState(Game.STATE.SHOP);
                //game.deck.at_shop = true;
                break;
            case "Chest":
                game.changeState(Game.STATE.CHEST);
                break;

        }

    }
}
