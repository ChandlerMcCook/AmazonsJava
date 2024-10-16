 package Amazons;

//TrialGame.java
import java.util.Scanner;
import java.util.regex.Pattern;
//import java.util.ArrayList;


enum pieceColor {
	white,
	black;
	
	// used to flip the value. If white then switch to black, if black switch to white.
	public pieceColor flip() {
		return this == white ? black : white;
	}
}

public class TrialGame {

// the piece objects will be the actual game pieces themselves.
 public static class Piece {
     // Piece logic here
	 
	 private pieceColor color;
	 
	 public Piece(pieceColor setColor) {
		 this.color = setColor;
	 }
	 
	//getter     
     public pieceColor getPiece() {
    	 return this.color;
     }
 }

 //
 public static class Square {
	 // tells if there is a barrier on this square
     private boolean isBlocked;
     // null if there is no piece on the square
     private Piece piece;
     // temporary flag to mark valid squares to move to
     private boolean validMoveFlag;

     // constructor
     public Square() {
         // starts the square as empty
         this.isBlocked = false;
         this.piece = null;
     }

     // prints out the square for
     public void printSquare() {
         if (piece != null) {
             // Show 'W' for white piece, 'B' for black piece
             if (piece.color == pieceColor.white) {
                 System.out.print(" W ");
             } else {
                 System.out.print(" B ");
             }
         } else if (isBlocked) {
             System.out.print(" X ");
         } else if (validMoveFlag) {
        	System.out.print(" . ");
         } else {
             System.out.print("   ");
         }
     }

     // checks if the square is empty
     public boolean checkIfEmpty() {
         // if there is no piece on the square, 
         // and the square is not blocked,
         // it is empty and return true
         if (piece == null && isBlocked == false) {
             return true;
         } else {
             return false;
         }
     }
     
     // getters and setters

     public boolean getIsBlocked() {
         return isBlocked;
     }
     
     public void makeBlocked() {
         this.isBlocked = true;
     }
     
     public pieceColor getPiece() {
         return (piece != null) ? piece.getPiece() : null;
     }
     
     public void setPiece(pieceColor color) {
    	 this.piece = new Piece(color);
     }
     
     public void deletePiece() {
    	 this.piece = null;
     }
     
     public boolean getValid() {
    	 return validMoveFlag;
     }
     
     public void markValid() {
    	 this.validMoveFlag = true;
     }
     
     public void markInvalid() {
    	 this.validMoveFlag = false;
     }   
 }

 //board class, it is a 2d array of Square objects.
 public static class Board {
	  // size of board, chosen by user when created
     private final int boardSize = 10;
     // creates the array of square objects
     private Square[][] board;
     
     private pieceColor turn;
     
     private Scanner input = new Scanner(System.in);
     
     private int [][] whitePieces = new int[4][2];
     private int [][] blackPieces = new int[4][2];

     // constructor
     public Board() {
    	 turn = pieceColor.white;
    	 
         board = new Square[boardSize][boardSize];
         for (int i = 0; i < boardSize; i++) {
             for (int j = 0; j < boardSize; j++) {
                 // initializes every square
                 board[i][j] = new Square();
             }
         }
         
         board[0][3].setPiece(pieceColor.white);
         whitePieces[0][0] = 0;
         whitePieces[0][1] = 3;
         board[0][6].setPiece(pieceColor.white);
         whitePieces[1][0] = 0;
         whitePieces[1][1] = 6;
         board[3][0].setPiece(pieceColor.white);
         whitePieces[2][0] = 3;
         whitePieces[2][1] = 0;
         board[3][9].setPiece(pieceColor.white);
         whitePieces[3][0] = 3;
         whitePieces[3][1] = 9;
         
         board[9][3].setPiece(pieceColor.black);
         blackPieces[0][0] = 9;
         blackPieces[0][1] = 3;
         board[9][6].setPiece(pieceColor.black);
         blackPieces[0][0] = 9;
         blackPieces[0][1] = 6;
         board[6][0].setPiece(pieceColor.black);
         blackPieces[0][0] = 6;
         blackPieces[0][1] = 0;
         board[6][9].setPiece(pieceColor.black);
         blackPieces[0][0] = 6;
         blackPieces[0][1] = 9;
     }

     // prints out the current state of the board to console
     public void display() {
         System.out.println("-----------------------------------------");
         for (int i = boardSize-1; i >= 0; i--) {
             for (int j = 0; j < boardSize; j++) {
                 // prints each square
            	 System.out.print("|");
                 board[i][j].printSquare();
             }
             System.out.println("|\n-----------------------------------------");
         }
     }
     
     // takes in the piece color, and where you want to place the piece
     // checks if the space is empty and in bounds, then creates the piece
     // and places piece on board in that spot.
     // if not empty, returns error message and does not create piece.
     public void addPiece(pieceColor color, int x, int y) {
         // checks if you can place it there.
         if (x >= 0 && x < boardSize && y >= 0 && y < boardSize && board[y][x].checkIfEmpty() == true) {
             board[y][x].setPiece(color); // Place the piece on the square
         } else {
             System.out.println("Coordinates are out of bounds or space is occupied.");
         }
     }
     
     public boolean checkValidMoves(int x, int y) {
    	 // something to note is that it looks backwards writing board[y][x] but that is because
    	 // chess notation is column then row, but arrays are indexed row then column
    	 
    	 boolean flag = false;
    	 
    	 // mark left
    	 for (int i=x-1; i>=0 && board[y][i].checkIfEmpty(); i--) {
    		 flag = true;
    	 }
    	 
    	 // mark right
    	 for (int i=x+1; i<boardSize && board[y][i].checkIfEmpty(); i++) {
			 flag = true;
    	 }
    	 
    	 // mark down
    	 for (int i=y-1; i>=0 && board[i][x].checkIfEmpty(); i--) {
			 flag = true;
    	 }
    	 
    	 // mark up
    	 for (int i=y+1; i<boardSize && board[i][x].checkIfEmpty(); i++) {
			 flag = true;
    	 }
    	 
    	 // mark down-left
    	 int i=y-1, j=x-1;
    	 while (i>=0 && j>=0 && board[i][j].checkIfEmpty()) {
    		 i--;
    		 j--;
    		 flag = true;
    	 }
    	 
    	 // mark down-right
    	 i=y-1;
    	 j=x+1;
    	 while (i>=0 && j<boardSize && board[i][j].checkIfEmpty()) {
    		 i--;
    		 j++;
			 flag = true;
    	 }
    	 
    	 // mark up-left
    	 i=y+1;
    	 j=x-1;
    	 while (i<boardSize && j>=0 && board[i][j].checkIfEmpty()) {
    		 i++;
    		 j--;
			 flag = true;
    	 }
    	 
    	 // mark up-right
    	 i=y+1;
    	 j=x+1;
    	 while (i<boardSize && j<boardSize && board[i][j].checkIfEmpty()) {
    		 i++;
    		 j++;
			 flag = true;
    	 }
    	     	 
    	 return flag;
     }
     
     private void markValidMoves(int x, int y) {
    	 
    	 // mark left
    	 for (int i=x-1; i>=0 && board[y][i].checkIfEmpty(); i--) {
    		 board[y][i].markValid();
    	 }
    	 
    	 // mark right
    	 for (int i=x+1; i<boardSize && board[y][i].checkIfEmpty(); i++) {
    		 board[y][i].markValid();
    	 }
    	 
    	 // mark down
    	 for (int i=y-1; i>=0 && board[i][x].checkIfEmpty(); i--) {
    		 board[i][x].markValid();
    	 }
    	 
    	 // mark up
    	 for (int i=y+1; i<boardSize && board[i][x].checkIfEmpty(); i++) {
    		 board[i][x].markValid();
    	 }
    	 
    	 // mark down-left
    	 int i=y-1, j=x-1;
    	 while (i>=0 && j>=0 && board[i][j].checkIfEmpty()) {
    		 board[i][j].markValid();
    		 i--;
    		 j--;
    	 }
    	 
    	 // mark down-right
    	 i=y-1;
    	 j=x+1;
    	 while (i>=0 && j<boardSize && board[i][j].checkIfEmpty()) {
    		 board[i][j].markValid();
    		 i--;
    		 j++;
    	 }
    	 
    	 // mark up-left
    	 i=y+1;
    	 j=x-1;
    	 while (i<boardSize && j>=0 && board[i][j].checkIfEmpty()) {
    		 board[i][j].markValid();
    		 i++;
    		 j--;
    	 }
    	 
    	 // mark up-right
    	 i=y+1;
    	 j=x+1;
    	 while (i<boardSize && j<boardSize && board[i][j].checkIfEmpty()) {
    		 board[i][j].markValid();
    		 i++;
    		 j++;
    	 }
     }
     
     private int[] decodeSquare(String coord) {
    	 int [] location = new int[2];
    	 
    	 location[0] = coord.charAt(0) - 'a';
    	 if (coord.length() == 3) {
    		 location[1] = 9;
    	 } else {
    		 location[1] = coord.charAt(1) - '1'; 
    	 }
    	 
    	 
    	 return location;
     }
     
     public boolean takeTurn() {
//    	 Scanner input = new Scanner(System.in);
    	 
    	 boolean pieceCanMove = false;
    	 if (turn == pieceColor.white) {
    		 for (int[] pieceArray : whitePieces) {
    			 if (checkValidMoves(pieceArray[1], pieceArray[0])) {
    				 pieceCanMove = true;
    			 }
    		 }
    		 
    		 if (!pieceCanMove) {
    			 winner(turn.flip());
        		 input.close();
        		 return true;
    		 }
    		 
    		 System.out.println("White's Turn:");
    	 } else {
    		 for (int[] pieceArray : blackPieces) {
    			 if (checkValidMoves(pieceArray[1], pieceArray[0])) {
    				 pieceCanMove = true;
    			 }
    		 }
    		 
    		 if (!pieceCanMove) {
    			 winner(turn.flip());
        		 input.close();
        		 return true;
    		 }
    		 
    		 System.out.println("Black's Turn:");
    	 }
    	 
    	 display();
    	 
    	 Pattern pattern = Pattern.compile("[a-j]([1-9]|10)");
    	 
    	 // get a move from the user that is one of their pieces
    	 int x, y; // increase scope of x and y
    	 do {
	    	 System.out.print("Enter the coordinate of a piece: ");
	    	 
	    	 // keep asking for coordinate until user inputs a-j followed by 1-10
	    	 while (!input.hasNext(pattern)) {
	    		 System.out.println("Please enter a valid coordinate");
	    		 input.next();
	    	 }
	    	 
	    	 String stringCoord = input.next();
	    	 
	    	 int arrCoord[] = decodeSquare(stringCoord);
	    	 
	    	 x = arrCoord[0];
	    	 y = arrCoord[1];
	    	 
	    	 if (board[y][x].getPiece() == null) {
	    		 System.out.println("Select a square with a piece");
	    	 } else if (board[y][x].getPiece() != turn) {
	    		 System.out.println("Select a " + turn + " piece");
	    	 }
    	 } while(board[y][x].getPiece() == null || board[y][x].getPiece() != turn);
    	 
    	 int originalX = x;
    	 int originalY = y;
    	 
    	 markValidMoves(x, y);
    	 
    	 display();
    	 
    	 do {
    		 System.out.print("Enter a move: ");
    		 
    		// keep asking for coordinate until user inputs a-j followed by 1-10
	    	 while (!input.hasNext(pattern)) {
	    		 System.out.println("Please enter a valid coordinate");
	    		 input.next();
	    	 }
	    	 
	    	 String stringCoord = input.next();
	    	 
	    	 int arrCoord[] = decodeSquare(stringCoord);
	    	 
	    	 x = arrCoord[0];
	    	 y = arrCoord[1];
	    	 
	    	 if (!board[y][x].getValid()) {
	    		 System.out.println("Enter a valid move");
	    	 }
    	 } while (!board[y][x].getValid());
    	 
    	 // move piece to different square
    	 board[y][x].setPiece(board[originalY][originalX].getPiece());
    	 board[originalY][originalX].deletePiece();
    	 
    	 // move piece in piece array
    	 if (turn == pieceColor.white) {
    		 for (int i=0; i<whitePieces.length; i++) {
    			 if (originalY == whitePieces[i][0] && originalX == whitePieces[i][1]) {
    				 whitePieces[i][0] = y;
    				 whitePieces[i][1] = x;
    			 }
    		 }
    	 } else {
    		 for (int i=0; i<blackPieces.length; i++) {
    			 if (originalY == blackPieces[i][0] && originalX == blackPieces[i][1]) {
    				 blackPieces[i][0] = y;
    				 blackPieces[i][1] = x;
    			 }
    		 }
    	 }
    	 
    	 resetFlags();
    	 
    	 markValidMoves(x, y);
    	 display();
    	 
    	 do {
    		 System.out.print("Enter a coordinate to place wall: ");
    		 
    		// keep asking for coordinate until user inputs a-j followed by 1-10
	    	 while (!input.hasNext(pattern)) {
	    		 System.out.println("Please enter a valid coordinate");
	    		 input.next();
	    	 }
	    	 
	    	 String stringCoord = input.next();
	    	 
	    	 int arrCoord[] = decodeSquare(stringCoord);
	    	 
	    	 x = arrCoord[0];
	    	 y = arrCoord[1];
	    	 
	    	 if (!board[y][x].getValid()) {
	    		 System.out.println("Enter a valid move");
	    	 }
    	 } while(!board[y][x].getValid() || !placeBlock(x, y));
    	 
    	 resetFlags();
    	 
    	 turn = turn.flip();
    	 
    	 return false;
     }
     
     // reset valid flags
     private void resetFlags() {
    	 for (Square [] row : board) {
    		 for (Square s : row) {
    			 s.markInvalid();
    		 }
    	 }
     }
     
     
     // takes in the location where you want to place a block
     // checks if the space is empty and in bounds, then sets square to blocked
     // if not empty, returns error message and does not block space.
     public boolean placeBlock(int x, int y) {
     	if (x >= 0 && x < boardSize && y >= 0 && y < boardSize && board[y][x].checkIfEmpty() == true) {
     		// place a block at this location
     		board[y][x].makeBlocked();
     		return true;
     	} else {
            System.out.println("Coordinates are out of bounds or space is occupied.");
            return false;
         }
     }
     
     public void winner(pieceColor color) {
    	 System.out.println("Congradulations " + color + ", you won!\n");
    	 input.close();
     }
 }

 public static void main(String[] args) {
     // Start your game
     Scanner input = new Scanner(System.in);

     // Setup the board
     Board board = new Board();
     
     boolean doneFlag = false;
     while(!doneFlag) {
    	 doneFlag = board.takeTurn();
     }
     
     input.close();
 }
}