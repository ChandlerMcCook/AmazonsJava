package Amazons;

//EDIT EDIT EDIT

//TrialGame.java

// Game Structure Explanation:
// The game is made up of several parts:
// Board, Square, Piece.

// Board
// a size x size array of Square objects, where size is chosen by the user.

// Square
// each square is instantiated when the board is created (all empty)
// each square has information on:
// is it occupied? What occupies it? is it blocked by a barrier?
// What specific piece object is on it, if any?

// Pieces
// Objects that represent the player pieces

import java.util.Scanner;

enum pieceColor {
    white, black
}

public class TrialGame {

    // the piece objects will be the actual game pieces themselves.
    public static class Piece {

        private pieceColor color;
        private int x;
        private int y;

        // Constructor
        public Piece(pieceColor color, int x, int y) {
            this.color = color;
            this.x = x;
            this.y = y;
        }
        
        // Getters and setters
        public int getX() {
            return x;
        }

        public void setX(int x) {
            this.x = x;
        }

        public int getY() {
            return y;
        }

        public void setY(int y) {
            this.y = y;
        }

        public pieceColor getColor() {
            return color;
        }
    }

    //
    public static class Square {
        // tells if there is a barrier on this square
        private boolean isBlocked;
        // null if there is no piece on the square
        private Piece piece;

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
                    System.out.print("W ");
                } else {
                    System.out.print("B ");
                }
            } else if (isBlocked) {
                System.out.print("X ");
            } else {
                System.out.print(". ");
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
        
        public void setIsBlocked(boolean isBlocked) {
            this.isBlocked = isBlocked;
        }
        
        public Piece getPiece() {
            return piece;
        }
        
        public void setPiece(Piece piece) {
            this.piece = piece;
        }
    }

    // board class, it is a 2d array of Square objects.
    public static class Board {

        // size of board, chosen by user when created
        private int boardSize;
        // creates the array of square objects
        private Square[][] board;

        // constructor with dynamic size
        public Board(int size) {
            // use default size of 10 if input is 0
            boardSize = (size > 0) ? size : 10; 
            board = new Square[boardSize][boardSize];
            for (int i = 0; i < boardSize; i++) {
                for (int j = 0; j < boardSize; j++) {
                    // initializes every square
                    board[i][j] = new Square();
                }
            }
        }

        // prints out the current state of the board to console
        public void display() {
            for (int i = 0; i < boardSize; i++) {
                System.out.println("");
                for (int j = 0; j < boardSize; j++) {
                    // prints each square
                    board[i][j].printSquare();
                }
            }
            System.out.println();
        }
        
        // takes in the piece color, and where you want to place the piece
        // checks if the space is empty and in bounds, then creates the piece
        // and places piece on board in that spot.
        // if not empty, returns error message and does not create piece.
        public void addPiece(pieceColor color, int x, int y) {
            // checks if you can place it there.
            if (x >= 0 && x < boardSize && y >= 0 && y < boardSize && board[x][y].checkIfEmpty() == true) {
                Piece piece = new Piece(color, x, y);
                board[x][y].setPiece(piece); // Place the piece on the square
            } else {
                System.out.println("Coordinates are out of bounds or space is occupied.");
            }
        }
        
        // takes in the location where you want to place a block
        // checks if the space is empty and in bounds, then sets square to blocked
        // if not empty, returns error message and does not block space.
        public void placeBlock(int x, int y) {
        	if (x >= 0 && x < boardSize && y >= 0 && y < boardSize && board[x][y].checkIfEmpty() == true) {
        		// place a block at this location
        		board[x][y].setIsBlocked(true);
        	} else {
                System.out.println("Coordinates are out of bounds or space is occupied.");
            }
        }
    }

    public static void main(String[] args) {
        // Start your game
        Scanner input = new Scanner(System.in);

        // Setup the board
        System.out.print("How big would you like your board to be? \n(put 0 for default size of 10): ");
        int size = input.nextInt();
        Board board = new Board(size);

        // Display the empty board
        System.out.println("Initial board:");
        board.display();
        
        // Add a white piece at (6, 0)
        board.addPiece(pieceColor.white, 6, 0);
        // Display the board again to see the piece placement
        System.out.println("\nAfter placing a piece at (6, 0):");
        board.display();
        
        board.addPiece(pieceColor.white, 6, 9);
        board.addPiece(pieceColor.white, 9, 3);
        board.addPiece(pieceColor.white, 9, 6);
        
        board.addPiece(pieceColor.black, 0, 3);
        board.addPiece(pieceColor.black, 0, 6);
        board.addPiece(pieceColor.black, 3, 0);
        board.addPiece(pieceColor.black, 3, 9);
        System.out.println("\nAfter filling the rest of the board:");
        board.display();
        
        board.placeBlock(1,3);
        System.out.println("\nAfter placing a test barrier:");
        board.display();
        
        input.close();
    }
}
