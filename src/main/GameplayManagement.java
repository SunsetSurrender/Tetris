package main;
//class for drawing UI and other mechanics and main play area
//block size will be 30 px; (fitting 12 wide and 20 high)
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

import main.Tile.*;

import java.awt.*;
import java.util.ArrayList;
import java.util.Random;

public class GameplayManagement {
    //drawing play area

    final int width = 360;
    final int height = 600;

    //variables for indicating left bottom right and top parts of the area

    public static int left_x;
    public static int right_x;
    public static int top_y;
    public static int bottom_y;

    //Tile/mino
    Tile currentTile;
    final int block_start_X;
    final int block_start_y;

    // variables used to display the next mino in line.
    Tile nextMino;
    final int nextMino_x;
    final int nextMino_y;
    public static ArrayList<Block> staticBlocks = new ArrayList<>(); //inactive minos will go in here


    //Other tiles
    public static int dropInterval = 60; // a tile or mino will drop every 60 frames

    //Game Over variables
    boolean gameOver;


    //Effect variables
    boolean effectCounterOn;
    int effectCounter;
    ArrayList<Integer> effectY = new ArrayList<>();

    // Score -- variables to create score frame
    int level = 1;
    int lines;
    int score;

    //creation of constructor for setting the variables

    public GameplayManagement(){
        left_x = (GamePanel.width/2)-(width/2); // 1280/2 - 360/2 = 460
        right_x = left_x + width;
        top_y = 50;
        bottom_y = top_y + height;//play area height

        //starting position for minos in the play area so this is why width/2 - block size
        block_start_X = left_x + (width/2) - Block.size;
        block_start_y = top_y + Block.size;

        nextMino_x = right_x + 175;
        nextMino_y = top_y + 500;

        // set starting mino (currentTile)
        currentTile = pickMino();
        currentTile.setXY(block_start_X, block_start_y);
        //we create the next mino
        nextMino = pickMino();
        nextMino.setXY(nextMino_x,nextMino_y);
    }

    //method for randomizing the incoming tetraminos // calling constructors so they build at random

    private Tile pickMino(){
        Tile mino = null;
        int i = new Random().nextInt(7);

        switch(i) {
            case 0: mino = new Block_L1();
            break;
            case 1: mino = new Block_L2();
            break;
            case 2: mino = new Block_Square();
            break;
            case 3: mino = new Block_Bar();
            break;
            case 4: mino = new Block_T();
            break;
            case 5: mino = new Block_Z1();
            break;
            case 6: mino = new Block_Z2();
            break;
        }
        return mino;
    }

    public void update(){
        //check if the currentMino is active

        if(currentTile.active == false){
        // if the mino is not active, the mino will go into staticBlocks array list.
            staticBlocks.add(currentTile.b[0]);
            staticBlocks.add(currentTile.b[1]);
            staticBlocks.add(currentTile.b[2]);
            staticBlocks.add(currentTile.b[3]);

            //check if the game is over
            if(currentTile.b[0].x == block_start_X && currentTile.b[0].y == block_start_y){
                //this means that the current block immediately collided with a block and couldn't move at all, so it's xy are the same as the nextMino's
                gameOver = true;
                GamePanel.music.stop();
                GamePanel.se.play(2, false);
            }

            currentTile.deactivating = false;

            // now that it is added, the new mino will replace the currentTile (or current mino).
            currentTile = nextMino; // this is the replacement
            currentTile.setXY(block_start_X, block_start_y);
            nextMino = pickMino();
            nextMino.setXY(nextMino_x,nextMino_y);

            //when a mino becomes inactive check if lines can be deleted
            checkDelete();

            } else {
                currentTile.update();
            }
    }

    //method for deleting the lines!
    private void checkDelete() {
        int x = left_x;
        int y = top_y;
        int blockCount = 0;
        int lineCount = 0;


        while(x < right_x && y < bottom_y){

            //we scan the play area by block size, and if there is a static block at the coordinate, and we increase this block count.
            for(int i = 0; i < staticBlocks.size(); i++){
                if(staticBlocks.get(i).x == x && staticBlocks.get(i).y == y) {
                    //increase the count if there is a static block
                    blockCount++;
                }
            }

            x += Block.size;

            if(x == right_x) {
            // if the blockCount hits 12, that means that current y line is all filled with static blocks, that can be deleted

                if(blockCount == 12){

                    effectCounterOn = true;
                    effectY.add(y); // arraylist used as multiple lines can be deleted.

                    for(int i = staticBlocks.size()-1; i > - 1; i--) { //we check from largest number
                        //remove all the blocks in the current y line
                        if(staticBlocks.get(i).y == y) {
                            staticBlocks.remove(i);
                        }
                    }

                    lineCount++;
                    lines++;
                    //Drop Speed
                    // if the line score hits a certain number increase the drop speed
                    // 1 is the fastest

                    if(lines % 5 == 0 && dropInterval >1){ //for every ten lines deleted the level increase. For every level, the drop interval increases to 10. Starting at 60;

                        level++;

                        if(dropInterval > 15) {
                            dropInterval -= 15;
                        } else {
                            dropInterval -= 1; // when it gets to 10 we decrease it by one instead of 10
                        }
                    }


                    //after we remove we shift down the line
                    //a line has been deleted - we need to slide down all the blocks that are above it
                    for(int i = 0; i < staticBlocks.size(); i++){
                        //if a block is above the current Y, move it down by block size
                        if(staticBlocks.get(i).y < y) {
                            staticBlocks.get(i).y += Block.size;
                        }
                    }

                }
                blockCount = 0;
                x = left_x;
                y += Block.size;
            }
        }

        //Add Score
        if(lineCount > 0 ){
            GamePanel.se.play(4, false);
            int singleLineScore = 10 * level; // assigning how much the score count will be
            score += singleLineScore * lineCount;
        }

    }

    public void draw(Graphics2D g2) throws FontFormatException {

        //drawing the play area frame leaving space for the collision to occur

        g2.setColor(Color.white);//stroke color
        g2.setStroke(new BasicStroke(4f));//stroke width of 4 pixels for boundaries where collision will happen, hence the variables minus 4 (so it falls inside the white line, not outside)
        g2.drawRect(left_x -4, top_y - 4, width + 8, height + 8);//draw rectangle

        //next frame (square next showing what tile will come next

        int x = right_x + 100;
        int y = bottom_y - 200;
        g2.drawRect(x, y, 200, 200);
        g2.setFont(new Font("Arial", Font.PLAIN, 30)); //FONT Selection
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);//Anti-Aliasing
        g2.drawString("NEXT", x + 60, y + 60);// TEXT ADDITION

        //Draw the score frame

        g2.drawRect(x, top_y, 250, 300);

        x += 40;
        y = top_y + 90;
        g2.drawString("LEVEL: " + level, x, y);
        y += 70;
        g2.drawString("LINES: " + lines, x, y);
        y += 70;
        g2.drawString("SCORE: " + score, x, y);



        //Draw the current mino/tile
        if(currentTile != null){
            currentTile.draw(g2);
        }

        // Draw next mino
        nextMino.draw(g2);

        // draw static blocks
        for(int i = 0; i < staticBlocks.size(); i++){
            staticBlocks.get(i).draw(g2);// so it draws them one by one
        }

        //Draw Effect
        if(effectCounterOn){
            effectCounter++;

            g2.setColor(Color.YELLOW);
            for(int i = 0; i < effectY.size(); i++){
                g2.fillRect(left_x, effectY.get(i), width, Block.size); //width is the play area width
            }

            if(effectCounter == 15){ // when timer hits a certain number we reset everything and stop drawing the effect rectangle
                effectCounterOn =false;
                effectCounter = 0;
                effectY.clear();
            }
        }

        //Draw Pause && Game Over
        g2.setColor(Color.CYAN);
        g2.setFont(g2.getFont().deriveFont(50f));

        if(gameOver) {
            g2.setColor(Color.RED);
            x = left_x + 25;
            y = top_y + 320;
            g2.setFont(new Font("Arial", Font.BOLD, 50));
            g2.drawString("GAME OVER", x, y);
        }
        else if(KeyHandler.pausePressed) {
            x = left_x + 80;
            y = top_y + 320;
            g2.drawString("PAUSED", x, y);
        }

        //draw the game title
        x = 60;
        y = top_y + 320;
        g2.setColor(Color.white);
        g2.setFont(new Font("Helvetica", Font.ITALIC, 60));
        g2.drawString("Retro Tetris", x, y);

    }
}
