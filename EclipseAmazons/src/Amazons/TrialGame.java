package Amazons;

//TrialGame.java
import java.util.Scanner;

public class TrialGame {

// the piece objects will be the actual game pieces themselves.
 public static class Piece {
     // Piece logic here
 }

 //
 public static class Square {
     // this tells us if the space has a piece in it
	 private boolean isOccupied;
     // tells if there is a barrier on this square
     private boolean isBlocked;
     // null if there is no piece on the square
     private Piece piece;
     
     //constructor
     public Square() {
         //starts the square as empty
    	 this.isOccupied = false;
         this.isBlocked = false;
         this.piece = null;
     }
     
     //prints out the square for
     public void printSquare() {
         if (isOccupied) {
             System.out.print("X");
         }	else if (isBlocked) {
        	 System.out.print("0");
         } else {
        	 System.out.print(".");
         }
     }

     //getters and setters
     public void setPiece () {
    	 
     }
     
     public Piece getPiece() {
    	 return piece;
     }
 }

 //board class, it is a 2d array of Square objects.
 public static class Board {

     //creates the array of square objects
     Square[][] board = new Square[10][10];

     //constructor
     public Board() {
         for(int i = 0; i < 10; i++) {
             for(int j = 0; j < 10; j++) {
                  //initializes every square
                 board[i][j] = new Square();
             }
         }
         
         board[0][3]
     }

     public void display() {
         for(int i = 0; i < 10; i++) {
             for(int j = 0; j < 10; j++) {
                 //prints each square
                 //board[i][j].printSquare();
             }
         }
     }

     

     //board logic
 }

 public static void main(String[] args) {
     // Start your game
     Scanner input = new Scanner(System.in);

     Board board = new Board();

     board.display();
     // domryhri
 }
}
