package Amazons;

//TrialGame.java
import java.util.Scanner;

public class TrialGame {

 public static class Piece {
     // Piece logic here
 }

 public static class Square {
     private boolean isOccupied;
     //private boolean isBlocked;
     private Piece piece; //null if there is no piece on the square

     //constructor
     public Square() {
         this.isOccupied = false;
         //this.isBlocked = false;
         this.piece = null;
     }
     
     public void printSquare() {
         if (isOccupied) {
             System.out.print("This is here: " + piece + " ");
         }
     }

     //getters and setters
     public void setPiece () {
    	 
     }
     
     public void getPiece() {
    	 return piece;
     }
 }

 //board class
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
