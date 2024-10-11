//package Piece;
//
//import java.awt.image.BufferedImage;
//
//import javax.imageio.ImageIO;
//
//import Amazons.Board2;
//
//public class GamePiece {
//	
//	public BufferedImage image;
//	public int x, y;
//	public int col, row, preCol, preRow;
//	public int color;
//	
//	public GamePiece(int color, int col, int row) {
//		
//		this.color = color;
//		this.col = col;
//		this.row = row;
//		x = getX(col);
//		y = getY(row);
//		preCol = col;
//		preRow = row;
//	}
////	public BufferedImage getImage(String imagePath) {
////		BufferedImage image = null;
////		
//////		try {
//////			image = ImageIO.read(getClass().getResourceAsStream(imagePath + ".png"));
//////		} catch (IOException e) {
//////			e.printStackTrace();
//////		}
////	}
//	public int getX(int col) {
//		return col * Board2.SQUARE_SIZE;
//	}
//	public int getY(int row) {
//		return row * Board2.SQUARE_SIZE;
//	}
//}
