package Entity.UI.Nodes;

import Entity.Entity;
import Main.Game;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class Node extends Entity {
    Game game;
    int recievepath = 0;
    private List<Node> connections;
    public Node(Game game) {

        this.game = game;
        this.connections = new ArrayList<Node>();
    }


    @Override
    public String toString() {
        return " Node ";
    }

    public List<Node> getConnections() {
        return connections;
    }
    public void addConnection(Node node) {
        this.connections.add(node);
        node.recievepath++;
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
    public void update(Graphics g2d) {
        g2d.drawImage(sprite, x * scale - width / 2, y * scale - height / 2, width, height, null);
    }
}
