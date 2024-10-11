package Amazons;

import java.awt.Color;
import java.awt.Graphics2D;

public class Board2 {
	
	final int MAX_COL = 10;
	final int MAX_ROW = 10;
	public static final int SQUARE_SIZE = 80;
	public static final int HALF_SQUARE_SIZE = SQUARE_SIZE/2;
	
	
	public void draw(Graphics2D g2) {
		int color = 0;
		
		for(int row = 0; row < MAX_ROW; row++) {
			for(int col = 0; col < MAX_COL; col++) {
				if(color == 0) {
					g2.setColor(new Color(210,165,125));
					color = 1;
				}
				else {
					g2.setColor(new Color(175,115,70));
					color = 0;
				}
				//               x                  y              width     height
				g2.fillRect(col*SQUARE_SIZE, row*SQUARE_SIZE, SQUARE_SIZE, SQUARE_SIZE);
				
			}
			
			if(color == 0) {
				color = 1;
			} else {
				color = 0;
			}
		}
	}
	
	
	
}
