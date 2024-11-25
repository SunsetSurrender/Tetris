package main.Tile;

import java.awt.*;

public class Block extends Rectangle {

    //single block of each tile (composed of more bricks - tetrominos)
    public int x, y;
    public static final int size = 30; // size of 30x30
    public Color c;

    public Block(Color c){
        this.c = c;
    }

    public void draw(Graphics2D g2){
        //draws the block
        int margin = 2;
        g2.setColor(c);
        g2.fillRect(x + margin, y + margin, size - (margin*2), size - (margin*2));
    }

}
