package main;

import javax.swing.*;
import java.awt.*;
import java.security.Key;

public class GamePanel extends JPanel implements Runnable{

    //Window size

    public static final int width= 1280;
    public static final int height= 720;
    final int FPS= 60; // 60 refreshes per second.

    //Thread will run the game loop
    Thread gameThread;
    GameplayManagement gp;

    // instantiating Sound objects (static)
    public static Sound music = new Sound();
    public static Sound se = new Sound();

    public GamePanel(){

        // Settings
        this.setPreferredSize(new Dimension(width,height));
        this.setBackground(Color.black); // setting background color
        this.setLayout(null); // to make the layout as customizable as possible.

        //implementing key listener
        this.addKeyListener(new KeyHandler());
        this.setFocusable(true);

        gp = new GameplayManagement();

    }

    public void startGame(){
        gameThread = new Thread(this);
        gameThread.start();//when thread starts the run method is automatically called

        // starting music

        music.play(0, true);
        music.loop();
    }

    @Override
    public void run(){
        // game Loop - draw and update (objects or text)

        double drawInterval = 1000000000 /FPS;
        double delta = 0;
        long lastTime = System.nanoTime();
        long currentTime;

        while(gameThread!= null){
            currentTime = System.nanoTime();
            delta += (currentTime - lastTime) / drawInterval;
            lastTime = currentTime;

            if(delta >1){
                //update and repaint will be called 60 times 60 times per second
                update();
                repaint();
                delta--;
            }
        }
    }

    private void update(){
        //gp.update();

        if(KeyHandler.pausePressed == false && gp.gameOver == false) {
            gp.update();
        }
    }

    public void paintComponent(Graphics g){
        super.paintComponent(g);

        Graphics2D g2 = (Graphics2D) g;
        try {
            gp.draw(g2);
        }
        catch (FontFormatException e) {
            throw new RuntimeException(e);
        }
    }
}
