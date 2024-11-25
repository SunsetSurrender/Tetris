package main;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.security.Key;

public class KeyHandler implements KeyListener {

    // we're also adding a pause feature
    public static boolean upPressed, downPressed, leftPressed, rightPressed, pausePressed;

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        int code = e.getKeyCode();

// this way we can link keys to tile movement

        if(code == KeyEvent.VK_W) {
            upPressed = true;
        }
        if(code == KeyEvent.VK_A) {
            leftPressed= true;
        }
        if(code == KeyEvent.VK_S) {
            downPressed = true;
        }
        if(code == KeyEvent.VK_D) {
            rightPressed = true;
        }
        if(code == KeyEvent.VK_SPACE) {
            if(pausePressed){
                pausePressed = false;
                GamePanel.music.play(0, true);
                GamePanel.music.loop();
            } else {
                pausePressed = true;
                GamePanel.music.stop(); // so the music stops when we press pause.
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }
}
