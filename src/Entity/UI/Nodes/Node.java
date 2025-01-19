package Entity.UI.Nodes;

import Entity.Entity;
import Main.Game;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

import static java.lang.Math.sin;

public class Node extends Entity {
    Game game;
    boolean isAvailable = true;
    int recievepath;
    private List<Node> connections;
    public Node(Game game, String name) {
        recievepath = 0;
        this.game = game;
        this.name = name;
        this.connections = new ArrayList<Node>();
    }


    public List<Node> getConnections() {
        return connections;
    }
    public void addConnection(Node node) {
        this.connections.add(node);
        node.recievepath++;
    }


    public boolean isClicked() {
        if(game.input.isButton(MouseEvent.BUTTON1) && isAvailable == true && isHovered(game.input)) {
            return true;
        }
        return false;
    }




    public void draw(Graphics2D g2d) {
        g2d.setColor(Color.ORANGE);
        g2d.setStroke(new BasicStroke(20));
        for(int i = 0; i < connections.size(); i++) {
            g2d.drawLine(this.x*scale, this.y*scale, connections.get(i).x*scale, connections.get(i).y*scale);
        }
        g2d.setColor(Color.WHITE);
        width = (int)(xscale * sprite_width * scale);
        height = (int)(yscale * sprite_height * scale);

    }
    public void draw2(Graphics g2d) {
        g2d.drawImage(sprite, x * scale - width / 2, y * scale - height / 2, width, height, null);
    }
    public void update() {
        sin_timer += 1;
        lil_sin = sin(sin_timer / 10) / 20;
        xscale = lerp(xscale, target_xscale+ lil_sin, 0.1);
        yscale = lerp(yscale, target_yscale - lil_sin, 0.1);
        if(isHovered(game.input)) {
            target_xscale = 1.75;
            target_yscale = 1.75;
        }
        else {
            target_xscale = 1;
            target_yscale = 1;
        }
    }
}
