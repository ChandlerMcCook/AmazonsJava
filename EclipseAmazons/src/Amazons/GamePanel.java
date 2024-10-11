package Amazons;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import javax.swing.JPanel;


//Contains information on the size and color of the game panel


public class GamePanel extends JPanel implements Runnable{

	//SIZE OF GAME PANEL / WINDOW
	public static final int WIDTH = 1100;
	public static final int HEIGHT = 800;
	//FPS for the game
	final int FPS = 60;
	//Thread to run the fps of game
	Thread gameThread;
	Board2 board = new Board2();
	
	public GamePanel() {
		// SETS THE AUTO SIZE AND COLOR
		setPreferredSize(new Dimension (WIDTH, HEIGHT));
		setBackground(Color.white);
	}
	
	public void launchGame() {
		gameThread = new Thread(this);
		gameThread.start();
	}
	
	// contains game loop (not quite sure how this works)
	public void run() {
		
		//GAME LOOP
		double drawInterval = 1;
		double delta = 0;
		long lastTime = System.nanoTime();
		long currentTime;
		
		while(gameThread != null) {
			
			currentTime = System.nanoTime();
			
			delta += (currentTime - lastTime)/drawInterval;
			lastTime = currentTime;
			
			if(delta >= 1) {
				update();
				repaint();
				delta--;
			}
		}
	}
	
	private void update() {
		
	}
	
	//draws objects on the panel
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		Graphics2D g2 = (Graphics2D)g;
		
		board.draw(g2);
	}
	

}
