package main;
import javax.swing.*;

public class Main {

    public static void main(String[] args) {

        JFrame window = new JFrame("Tetris");
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setResizable(false);

        //Instanciating the GamePanel to the window

        GamePanel gp = new GamePanel();
        window.add(gp);
        window.pack(); //so the window adapts to the panel size

        window.setLocationRelativeTo(null);
        window.setVisible(true);

        gp.startGame();



    }
}
