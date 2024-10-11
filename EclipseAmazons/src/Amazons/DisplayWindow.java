package Amazons;


import javax.swing.JFrame;

//CONTAINS THE MAIN WINDOW STUFF

public class DisplayWindow {
    public static void main(String[] args) {
        // Create a new JFrame object
        JFrame window = new JFrame("Game of Amazons");

        // make program exit when closed
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // make it is you can't resize it
        window.setResizable(false);
        
        //Takes information from GamePanel.java
        // for window size and color
        GamePanel gp = new GamePanel();
        window.add(gp);
        window.pack();
        
        //make it appear in center of screen
        window.setLocationRelativeTo(null);
        
        // Make the window visible
        window.setVisible(true);
        
        gp.launchGame();
        
    }
}

