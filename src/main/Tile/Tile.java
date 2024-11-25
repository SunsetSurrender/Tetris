package main.Tile;


//super class for all the different tiles or tetrominos
import main.GamePanel;
import main.GameplayManagement;
import main.KeyHandler;

import javax.xml.stream.events.StartDocument;
import java.awt.*;
import java.security.Key;

public class Tile {

    public Block b[] = new Block[4];
    public Block tempB[] = new Block[4];
    int autoDropCounter = 0;
    public int direction = 1; //there will be 4 directions (1/2/3/4) - used for rotating the mino
    //variables for creating collisions
    boolean leftCollision, rightCollision, bottomCollision;
    // new variable that will make the mino stop at the bottom.
    public boolean active = true;
    //variables to add some time margin to slide the various minos in place
    public boolean deactivating;
    int deactivateCounter = 0;

    //method to instantiate the arrays
    public void create(Color c){
        b[0] = new Block(c);
        b[1] = new Block(c);
        b[2] = new Block(c);
        b[3] = new Block(c);

        tempB[0] = new Block(c);
        tempB[1] = new Block(c);
        tempB[2] = new Block(c);
        tempB[3] = new Block(c);
    }

    public void setXY(int x, int y) {}

    public void updateXY(int direction) {
        checkRotationCollision();
        // if no collision is happening, we can rotate the mino
        if(leftCollision == false && rightCollision == false && bottomCollision == false){
            this.direction = direction;

            //insertion of tempB values into the b array
            //using the tempB will help when dealing the collision, so origianl positions can be restored

            b[0].x = tempB[0].x;
            b[0].y = tempB[0].y;
            b[1].x = tempB[1].x;
            b[1].y = tempB[1].y;
            b[2].x = tempB[2].x;
            b[2].y = tempB[2].y;
            b[3].x = tempB[3].x;
            b[3].y = tempB[3].y;
        }
    }

    //METHODS FOR ROTATION

    public void getDirection1() {}
    public void getDirection2() {}
    public void getDirection3() {}
    public void getDirection4() {}

    //methods for checking the possible types of collision, both in movement and rotation
    public void checkMovementCollision(){
        // first step is to reset the boolean variables for collision
        leftCollision = false;
        rightCollision = false;
        bottomCollision = false;

        //check static block collision
        checkStaticBlockCollision();

        //left wall
        //Checking for frame collision (scanning block array.)
        for(int i = 0; i < b.length ; i++){
            if(b[i].x == GameplayManagement.left_x){
                leftCollision = true;
            }
        }

        //right wall
        for(int i = 0; i < b.length; i++){
            if(b[i].x + Block.size == GameplayManagement.right_x){
                rightCollision = true;
            }
        }

        //bottom wall
        for(int i = 0; i < b.length; i++){
            if(b[i].y + Block.size == GameplayManagement.bottom_y){
                bottomCollision = true;
            }
        }
    }
    public void checkRotationCollision(){

        // first step is to reset the boolean variables for collision
        leftCollision = false;
        rightCollision = false;
        bottomCollision = false;

        //check static block collision
        checkStaticBlockCollision();

        // we will be using tempB instead of b[], because we check condition using temporary values

        //left wall
        //Checking for frame collision (scanning block array.)
        for(int i = 0; i < b.length ; i++){
            if(tempB[i].x < GameplayManagement.left_x){
                leftCollision = true;
            }
        }

        //right wall
        for(int i = 0; i < b.length; i++){
            if(tempB[i].x + Block.size > GameplayManagement.right_x){
                rightCollision = true;
            }
        }

        //bottom wall
        for(int i = 0; i < b.length; i++){
            if(tempB[i].y + Block.size > GameplayManagement.bottom_y){
                bottomCollision = true;
            }
        }

    }

    //new method for checking collisions between blocks.
    private void checkStaticBlockCollision() {

        // first step is to iterate/scan over the arraylist
        for(int i = 0; i < GameplayManagement.staticBlocks.size(); i++){
            //to get each blocks x and y - new variables will be used
            int targetX = GameplayManagement.staticBlocks.get(i).x;
            int targetY = GameplayManagement.staticBlocks.get(i).y;

            // using these targets x and y to check down, left and right collision.
            // check down
            for(int ii = 0; ii < b.length; ii++){
                if(b[ii].y + Block.size == targetY && b[ii].x == targetX){ // this means that there is a static block beneath your mino
                    bottomCollision = true;
                }
            }
            // check left
            for(int ii = 0; ii < b.length; ii++) {
                if(b[ii].x - Block.size == targetX && b[ii].y == targetY){
                    leftCollision = true;
                }
            }
            //check right
            for(int ii = 0; ii < b.length; ii++){
                if(b[ii].x + Block.size == targetX && b[ii].y == targetY){
                    rightCollision = true;
                }
            }
        }
    }

    //minos drop at a certain interval, so we will now adjust this for the current mino to reflect (Gameplay management with variable drop interval)
    public void update(){

        //we check if deactivating is on
        if(deactivating){
            deactivating();
        }

       //moving the tile
        //up will handle rotation
        if(KeyHandler.upPressed){
            switch(direction) {
                case 1:
                    getDirection2();
                    break;
                case 2:
                    getDirection3();
                    break;
                case 3:
                    getDirection4();
                    break;
                case 4:
                    getDirection1();
                    break;
            }

        KeyHandler.upPressed = false;
            GamePanel.se.play(3, false);
        }
        //before checking if a key is pressed, we check if the mino is touching a wall
        checkMovementCollision();

        if(KeyHandler.downPressed){
            // if the mino's bottom is not hitting, it can go down
            if(bottomCollision == false) {
                b[0].y += Block.size;
                b[1].y += Block.size;
                b[2].y += Block.size;
                b[3].y += Block.size;

                //when oved down, reset the autoDropCounter
                autoDropCounter = 0;
            }
            KeyHandler.downPressed = false;
        }
        if(KeyHandler.leftPressed){
            if(leftCollision == false){
                b[0].x -= Block.size;
                b[1].x -= Block.size;
                b[2].x -= Block.size;
                b[3].x -= Block.size;
            }

            KeyHandler.leftPressed = false;
        }

        if(KeyHandler.rightPressed){

            if(rightCollision == false) {

                b[0].x += Block.size;
                b[1].x += Block.size;
                b[2].x += Block.size;
                b[3].x += Block.size;
            }
            KeyHandler.rightPressed = false;
        }
        if(bottomCollision){
            if(deactivating == false){
                GamePanel.se.play(1,false); //we need the if statement with the deactivating on false, because it has to only sound once.
            }
            //active = false; //if bottom collision happens, the mino will be deactivated, staying in place bypassing the autodrop, as per the else.
            deactivating = true;

        }else{
            autoDropCounter++;// the counter will increase every frame
            if(autoDropCounter == GameplayManagement.dropInterval) {
                //the tile goes down
                b[0].y += Block.size;
                b[1].y += Block.size;
                b[2].y += Block.size;
                b[3].y += Block.size;
                autoDropCounter = 0; // we reset the counter at the end
            }
        }
    }
    private void deactivating(){
        deactivateCounter++;

        //wait 45 frames until deactivate
        if(deactivateCounter == 45){

            deactivateCounter = 0;
            checkMovementCollision(); // to check if the bottom is still hitting
            // if the bottom is still hitting after 45 frames, deactivate the mino
            if(bottomCollision) {
                active = false;
            }
        }
    }

    public void draw(Graphics2D g2){
        g2.setColor(b[0].c);
        //color filling of the mino
        int margin = 2; //adding margins between mino block so it looks nicer
        g2.fillRect(b[0].x + margin, b[0].y + margin, Block.size - (margin*2), Block.size - (margin*2));
        g2.fillRect(b[1].x + margin, b[1].y + margin, Block.size - (margin*2), Block.size - (margin*2));
        g2.fillRect(b[2].x + margin, b[2].y + margin, Block.size - (margin*2), Block.size - (margin*2));
        g2.fillRect(b[3].x + margin, b[3].y + margin, Block.size - (margin*2), Block.size - (margin*2));

    }
}
