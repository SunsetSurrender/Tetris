package main.Tile;

import java.awt.*;

public class Block_L1 extends Tile {

    public Block_L1(){
        create(Color.blue);
    }

    public void setXY(int x, int y){ //default x y
        // o -- b1
        // o -- b0 because it rotates
        // o o -- b2 and b3

        b[0].x = x;
        b[0].y = y;
        b[1].x = b[0].x;
        b[1].y = b[0].y - Block.size;
        b[2].x = b[0].x;
        b[2].y = b[0].y + Block.size;
        b[3].x = b[0].x + Block.size;
        b[3].y = b[0].y + Block.size;

    }

    public void getDirection1(){
        // o
        // o
        // o o

        tempB[0].x = b[0].x;
        tempB[0].y = b[0].y;
        tempB[1].x = b[0].x;
        tempB[1].y = b[0].y - Block.size;
        tempB[2].x = b[0].x;
        tempB[2].y = b[0].y + Block.size;
        tempB[3].x = b[0].x + Block.size;
        tempB[3].y = b[0].y + Block.size;

        updateXY(1);
    }

    public void getDirection2(){
        //
        // o(b2) o (b0) o(b1)
        // o (b3)

        tempB[0].x = b[0].x;
        tempB[0].y = b[0].y;
        tempB[1].x = b[0].x + Block.size;
        tempB[1].y = b[0].y;
        tempB[2].x = b[0].x - Block.size;
        tempB[2].y = b[0].y;
        tempB[3].x = b[0].x - Block.size;
        tempB[3].y = b[0].y + Block.size;

        updateXY(2);

    }

    public void getDirection3(){
        //o o
        //  o
        //  0

        tempB[0].x = b[0].x;
        tempB[0].y = b[0].y;
        tempB[1].x = b[0].x;
        tempB[1].y = b[0].y + Block.size;
        tempB[2].x = b[0].x;
        tempB[2].y = b[0].y - Block.size;
        tempB[3].x = b[0].x - Block.size;
        tempB[3].y = b[0].y - Block.size;

        updateXY(3);


    }
    public void getDirection4(){
        //       o
        //   o o o
        //
        tempB[0].x = b[0].x;
        tempB[0].y = b[0].y;
        tempB[1].x = b[0].x - Block.size;
        tempB[1].y = b[0].y;
        tempB[2].x = b[0].x + Block.size;
        tempB[2].y = b[0].y;
        tempB[3].x = b[0].x + Block.size;
        tempB[3].y = b[0].y - Block.size;

        updateXY(4);
    }
}
