package Amazons;

import java.util.Scanner;
import java.util.regex.Pattern;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.image.*;
import javafx.scene.input.MouseEvent;
import javafx.event.*;

import javafx.scene.control.*;
import javafx.scene.paint.Color;
import javafx.geometry.Pos;
import javafx.scene.text.*;
import javafx.scene.shape.*;

import java.io.*;
import java.net.*;

//enum pieceColor {
//	white,
//	black;
//	
//	// used to flip the value. If white then switch to black, if black switch to white.
//	public pieceColor flip() {
//		return this == white ? black : white;
//	}
//}

public class OnlineVersion extends Application {
	int masterBoardSize = 10;
	GridPane initialPane = new GridPane();
	Scene boardScene = new Scene(initialPane, 400, 400);
	Image whiteSquare = new Image("file:C:src/images/white.png");
	Image blackSquare = new Image("file:C:src/images/black.png");
	static Label endText = new Label();
	static Button winButton = new Button();
	
	
	// stuff to talk to the server
	private static Socket socket = null;
	private static DataInputStream in = null;
	private static DataOutputStream out = null;
	static private pieceColor playerColor;
	
	
	public class guiBoard {
		public guiBoard() {
			g.setHgap(0);
			g.setVgap(0);
		}
		public Board masterBoard = new Board(masterBoardSize);
		public GridPane g = new GridPane();	
		
		// Clear gridpane g of guiBoard, then recreate entire board from masterBoard
		public void refreshBoard() {
			
			g.getChildren().clear();
			
			for (int i=0; i<10; i++) {
				for (int j=0; j<10; j++) {
					
					ImageView square;
					StackPane squarePane = new StackPane();
					
					if (((j+i) % 2) == 0) {					
						square = new ImageView(whiteSquare);
					} else {
						square = new ImageView(blackSquare);
					}
					
					// add coordinates to user data of square
					square.setUserData(new int[] {i, j});
					
					//stackpane cannot be resolved to imageView?
					//squarePane.setOnMouseClicked(new squareClickHandler());
					square.setOnMouseClicked(new squareClickHandler());
					square.setFitHeight(60);
					square.setFitWidth(60);
					squarePane.getChildren().add(square);
					
					// If board square is not empty, fill with piece or wall
					if (!guiBoardObject.masterBoard.board[i][j].checkIfEmpty()) {
						// If piece, place piece
						if (guiBoardObject.masterBoard.board[i][j].getPiece() != null) {
							// Board now has a getter for the new pieceCircle field, this is used
							// to get a black or white circle
							Circle circle = guiBoardObject.masterBoard.board[i][j].getCircle();
							circle.setOnMouseClicked(new circleClickHandler());
							circle.setUserData(new int[] {i, j});
							squarePane.getChildren().add(circle);
						}
						// If blocked, place fire
						else if (guiBoardObject.masterBoard.board[i][j].getIsBlocked()) {
							//Circle redCircle = new Circle(0, 0, 15);
							//redCircle.setFill(javafx.scene.paint.Color.RED);
							//squarePane.getChildren().add(redCircle);
							Rectangle redSquare = new Rectangle();
							redSquare.setWidth(40); redSquare.setHeight(40);
							redSquare.setArcWidth(20);
							redSquare.setArcHeight(20);
							redSquare.setFill(Color.rgb(245,76,41));
							squarePane.getChildren().add(redSquare);
						}
					}
					// If empty but a move, add a blue circle
					else if (guiBoardObject.masterBoard.board[i][j].getValid()) {
						Circle blueCircle = new Circle(0, 0, 10);
						blueCircle.setFill(Color.rgb(91,143,185));
						blueCircle.setOnMouseClicked(new circleClickHandler());
						blueCircle.setUserData(new int[] {i, j});
						squarePane.getChildren().add(blueCircle);
					}
					
					// Needed to flip the board to get it to display correctly with respect to the console
					guiBoardObject.g.add(squarePane, j, (10 - i));
				}
			}
			return;
		}
	}
	
	//Board masterBoard = new Board(10);
	
	guiBoard guiBoardObject = new guiBoard();
	
	public void start(Stage primaryStage) {
		guiBoardObject.refreshBoard();
		guiBoardObject.g.setHgap(0);
		guiBoardObject.g.setVgap(0);
		//guiBoardObject.masterBoard.takeTurn();
		
		// Setting up scene for start screen
		Label startTitle = new Label("AMAZONS");
		startTitle.setFont(Font.font("Comic Sans MS", FontWeight.BOLD, 40));
		startTitle.setTextFill(Color.GAINSBORO);
		Rectangle titleBackground = new Rectangle();
		titleBackground.setFill(Color.SANDYBROWN);
		titleBackground.setArcWidth(20);
		titleBackground.setArcHeight(20);
		titleBackground.setWidth(380);
		titleBackground.setHeight(75);
		StackPane titlePane = new StackPane(titleBackground, startTitle);
		Label startText = new Label("Free shipping included.");
		startText.setFont(Font.font("Comic Sans MS", FontPosture.ITALIC, 20));
		Button startButton = new Button("Start");
		startButton.setStyle("-fx-background-color: crimson; -fx-border-radius: 20; -fx-background-radius: 20; -fx-padding: 5");
		startButton.setFont(Font.font("Comic Sans MS", FontWeight.BOLD, 20));
		startButton.setTextFill(Color.GOLD);
		startButton.setPrefHeight(50);
		startButton.setPrefWidth(140);
		Button instructionsButton = new Button("Instructions");
		instructionsButton.setStyle("-fx-background-color: crimson; -fx-border-radius: 20; -fx-background-radius: 20; -fx-padding: 5");
		instructionsButton.setFont(Font.font("Comic Sans MS", FontWeight.BOLD, 20));
		instructionsButton.setTextFill(Color.GOLD);
		instructionsButton.setPrefHeight(50);
		instructionsButton.setPrefWidth(140);
		HBox buttonsBox = new HBox(30, startButton, instructionsButton);
		buttonsBox.setAlignment(Pos.CENTER);
		VBox topBox = new VBox(10, titlePane, startText);
		topBox.setAlignment(Pos.CENTER);
		topBox.setTranslateY(-30);
		buttonsBox.setTranslateY(-30);
		VBox startBox = new VBox(40, topBox, buttonsBox);
		startBox.setAlignment(Pos.CENTER);
		startBox.setStyle("-fx-background-color: cadetblue");
		Scene startScene = new Scene(startBox, 400, 400);
		
		// Setting up scene for instructions screen
		Label instrTitle = new Label("How To Play");
		instrTitle.setFont(Font.font("Comic Sans MS", FontWeight.BOLD, 30));
		instrTitle.setUnderline(true);
		HBox instrTitleBox = new HBox(instrTitle);
		instrTitleBox.setAlignment(Pos.CENTER);
		Label instrHeader = new Label("Game Start:");
		instrHeader.setFont(Font.font("Comic Sans MS", FontWeight.BOLD, 20));
		Label instrBody = new Label("\tAt the start of the game there are 4 white and 4 black queen chess pieces on a chessboard. These can "
				+ "move and attack the same as normal queen pieces in chess: Anywhere up, down, left, right, or diagonal unless blocked by a wall.");
		instrBody.setFont(Font.font("Comic Sans MS", 15));
		instrBody.setWrapText(true);
		Label instrHeader2 = new Label("Gameplay Loop:");
		instrHeader2.setFont(Font.font("Comic Sans MS", FontWeight.BOLD, 20));
		Label instrBody2 = new Label("\tTo start your turn, you will first choose a piece. You can then move that piece anywhere not blocked by "
				+ "a wall. Once your piece has been moved you can shoot out a wall anywhere you are able to move to which will block all "
				+ "pieces (including your own!).");
		instrBody2.setFont(Font.font("Comic Sans MS", 15));
		instrBody2.setWrapText(true);
		Label instrHeader3 = new Label("Win Condition:");
		instrHeader3.setFont(Font.font("Comic Sans MS", FontWeight.BOLD, 20));
		Label instrBody3 = new Label("\tTo win the game you must be able to move at least one piece while your opponent cannot move any.");
		instrBody3.setFont(Font.font("Comic Sans MS", 15));
		instrBody3.setWrapText(true);
		Button backButton = new Button("Back");
		backButton.setFont(Font.font("Comic Sans MS", 15));
		backButton.setStyle("-fx-background-color: bisque; -fx-border-radius: 20; -fx-background-radius: 20; -fx-padding: 5");
		HBox backButtonBox = new HBox(backButton);
		backButtonBox.setAlignment(Pos.CENTER);
		backButtonBox.setPrefWidth(50);
		backButtonBox.setPrefHeight(50);
		backButtonBox.setTranslateY(3);
		VBox instrBox = new VBox(5, instrTitleBox, instrHeader, instrBody, instrHeader2, instrBody2, instrHeader3, instrBody3, backButtonBox);
		Scene instructionsScene = new Scene(instrBox, 400, 500);
		
		// Setting up scene for end screen
		endText.setFont(Font.font("Comic Sans MS", FontWeight.BOLD, 30));
		Button restartButton = new Button("Restart");
		restartButton.setStyle("-fx-background-color: bisque; -fx-border-radius: 20; -fx-background-radius: 20; -fx-padding: 5");
		VBox endBox = new VBox(20, endText, restartButton);
		endBox.setAlignment(Pos.CENTER);
		Scene endScene = new Scene(endBox, 400, 400);
		
		boardScene =  new Scene(guiBoardObject.g);
		
		backButton.setOnAction(e -> primaryStage.setScene(startScene));
		instructionsButton.setOnAction(e -> primaryStage.setScene(instructionsScene));
		startButton.setOnAction(e -> {
			primaryStage.setScene(boardScene);
			// connect to server, starts sending to server in "feedCoord" function
			try {
				System.out.println("Trying to connect");
	            socket = new Socket("10.80.10.191", 5555);
	            System.out.println("Connected");

	            // read from socket
	            in = new DataInputStream(socket.getInputStream());
	            
	            // sends output to the socket
	            out = new DataOutputStream(socket.getOutputStream());
	            
	            int playerNum = Integer.parseInt(in.readUTF());
	            
	            System.out.println("message received: "+playerNum);
	            if (playerNum == 0) {
	            	playerColor = pieceColor.white;
	            } else {
	            	playerColor = pieceColor.black;
	            	
	            	// get first move from white
	            	new Thread(() -> {
						try {						
							for (int i=0; i<3; i++) {	
								String inCoord = in.readUTF();
								System.out.println(inCoord);
								int coord1 = inCoord.charAt(0) - '0';
								int coord2 = inCoord.charAt(1) - '0';
								
								Platform.runLater(() -> {
									guiBoardObject.masterBoard.feedCoord(coord2, coord1, true);
									guiBoardObject.refreshBoard();
								});
							}
						} catch (IOException io) {
							System.out.println(io);
						}
					}).start();
	            }
	            
	            System.out.println("PLAYER: "+playerColor);
	        }
	        catch (UnknownHostException u) {
	            System.out.println(u);
	            return;
	        }
	        catch (IOException i) {
	            System.out.println(i);
	            return;
	        }
		});
		restartButton.setOnAction(e -> endText.setText("Sorry this feature is not working"));
		
		winButton.setOnAction(e -> {
			primaryStage.setScene(endScene);
		});
		
		primaryStage.setScene(startScene);
		primaryStage.setTitle("Amazons");
		primaryStage.show();
		
		
	}
	
	class squareClickHandler implements EventHandler<MouseEvent> {
		@Override
		public void handle(MouseEvent event) {
			ImageView spotClicked = (ImageView) event.getSource();
			int[] coord = (int[]) spotClicked.getUserData();
			
			System.out.println(("Square clicked at "+coord[0])+coord[1]);
			
			// if it's the players turn then do it locally else get coord from server
			if (playerColor == guiBoardObject.masterBoard.turn) {
				guiBoardObject.masterBoard.checkIfWinner();
				
				guiBoardObject.masterBoard.feedCoord(coord[1], coord[0], false);
				guiBoardObject.refreshBoard();
				
				guiBoardObject.masterBoard.checkIfWinner();

				if (playerColor != guiBoardObject.masterBoard.turn) {
					new Thread(() -> {
						try {						
							for (int i=0; i<3; i++) {	
								String inCoord = in.readUTF();
								System.out.println(inCoord);
								int coord1 = inCoord.charAt(0) - '0';
								int coord2 = inCoord.charAt(1) - '0';
								
								Platform.runLater(() -> {
									guiBoardObject.masterBoard.feedCoord(coord2, coord1, true);
									guiBoardObject.refreshBoard();
									guiBoardObject.masterBoard.checkIfWinner();
								});
							}
						} catch (IOException e) {
							System.out.println(e);
						}
					}).start();
				}
			}
		}
	}
	
	class circleClickHandler implements EventHandler<MouseEvent> {
		@Override
		public void handle(MouseEvent event) {
			Circle spotClicked = (Circle) event.getSource();
			int[] coord = (int[]) spotClicked.getUserData();
			
			System.out.println(("Circle clicked at "+coord[0])+coord[1]);
			
			// if it's the players turn then do it locally else get coord from server
			if (playerColor == guiBoardObject.masterBoard.turn) {
				guiBoardObject.masterBoard.checkIfWinner();
				
				guiBoardObject.masterBoard.feedCoord(coord[1], coord[0], false);
				guiBoardObject.refreshBoard();
				
				guiBoardObject.masterBoard.checkIfWinner();

				if (playerColor != guiBoardObject.masterBoard.turn) {
					new Thread(() -> {
						try {						
							for (int i=0; i<3; i++) {	
								String inCoord = in.readUTF();
								System.out.println(inCoord);
								int coord1 = inCoord.charAt(0) - '0';
								int coord2 = inCoord.charAt(1) - '0';
								
								Platform.runLater(() -> {
									guiBoardObject.masterBoard.feedCoord(coord2, coord1, true);
									guiBoardObject.refreshBoard();
									guiBoardObject.masterBoard.checkIfWinner();
								});
							}
						} catch (IOException e) {
							System.out.println(e);
						}
					}).start();
				}
			}
		}
	}

	 // the piece objects will be the actual game pieces themselves.
	 public static class Piece {
	     // Piece logic here
		 
		 private pieceColor color;
		 private Circle pieceCircle;
		 
		 public Piece(pieceColor setColor) {
			 this.color = setColor;
			 this.pieceCircle = new Circle(0, 0, 20);
			 if (setColor == pieceColor.black) {
				 this.pieceCircle.setFill(Color.rgb(117,14,33));
			 }
			 if (setColor == pieceColor.white) {
				 this.pieceCircle.setFill(Color.rgb(226,226,190));
			 }
		 }
		 
		//getter     
	     public pieceColor getPiece() {
	    	 return this.color;
	     }
	     
	     public Circle getCircle() {
	    	 return this.pieceCircle;
	     }
	 }

	 // holds the information of a given square on the board
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
	     
	     public Circle getCircle() {
	    	 return piece.getCircle();
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
		 private final int boardSize;
		 // creates the array of square objects
		 public Square[][] board;
		 // stores whose turn it is
		 private pieceColor turn;
		 
		 private Scanner input = new Scanner(System.in);
		 
		 private int [][] whitePieces;
		 private int [][] blackPieces;
		
		 private int turnPhase; // 0 - piece selection
		 						// 1 - piece movement
		 						// 2 - shooting
		 private int oldX;
		 private int oldY;
		 
		 
		 // constructor
		 public Board(int size) {
			 boardSize = size;
			 
			 turn = pieceColor.white;
			 turnPhase = 0;
		     board = new Square[boardSize][boardSize];
		     for (int i = 0; i < boardSize; i++) {
		         for (int j = 0; j < boardSize; j++) {
		             // initializes every square
		             board[i][j] = new Square();
		         }
		     }
		     if (size == 10) {
		    	 whitePieces = new int[4][2];
		    	 blackPieces = new int[4][2];
		    	 
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
		    	 blackPieces[1][0] = 9;
		    	 blackPieces[1][1] = 6;
		    	 board[6][0].setPiece(pieceColor.black);
		    	 blackPieces[2][0] = 6;
		    	 blackPieces[2][1] = 0;
		    	 board[6][9].setPiece(pieceColor.black);
		    	 blackPieces[3][0] = 6;
		    	 blackPieces[3][1] = 9;
		     } else {
		    	 whitePieces = new int[2][2];
		    	 blackPieces = new int[2][2];
		    	 
		    	 board[0][1].setPiece(pieceColor.white);
		    	 whitePieces[0][0] = 0;
		    	 whitePieces[0][1] = 1;
		    	 
		    	 board[0][4].setPiece(pieceColor.white);
		    	 whitePieces[1][0] = 0;
		    	 whitePieces[1][1] = 4;
		    	 
		    	 board[5][1].setPiece(pieceColor.black);
		    	 blackPieces[0][0] = 5;
		    	 blackPieces[0][1] = 1;
		    	 
		    	 board[5][4].setPiece(pieceColor.black);
		    	 blackPieces[1][0] = 5;
		    	 blackPieces[1][1] = 4;
		     }
		 }
		
		 // prints out the current state of the board to console
		 public void display() {
			 System.out.print("   ");
		     for (int i=0; i<boardSize; i++) {
		    	 char spot = (char) ('a' + i);
		    	 System.out.print("  " + spot + " ");
		     }
			 if (boardSize == 10) {    		 
				 System.out.println("\n    ---------------------------------------");
			 } else {
				 System.out.println("\n    -----------------------");
			 }
		     for (int i = boardSize-1; i >= 0; i--) {
		    	 System.out.printf("%-3d", i+1);
		         for (int j = 0; j < boardSize; j++) {
		             // prints each square
		        	 System.out.print("|");
		             board[i][j].printSquare();
		         }
		         System.out.printf("|  %-3d", i+1);
		         if (boardSize == 10) {    		 
		    		 System.out.println("\n    ---------------------------------------");
		    	 } else {
		    		 System.out.println("\n    -----------------------");
		    	 }
		     }
		     
		     System.out.print("   ");
		     for (int i=0; i<boardSize; i++) {
		    	 char spot = (char) ('a' + i);
		    	 System.out.print("  " + spot + " ");
		     }
		     System.out.println("\n");
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

		 
		 public boolean feedCoord(int guiX, int guiY, boolean fromNetwork) {
			 switch (turnPhase) {
			 case 0:
				 if (!fromNetwork) {					 
					 if (board[guiY][guiX].getPiece() == null) {
						 System.out.println("Select a square with a piece");
						 return true;
					 } else if (board[guiY][guiX].getPiece() != turn) {
						 System.out.println("Select a " + turn + " piece");
						 return true;
					 } else if (!checkValidMoves(guiX, guiY)) {
						 System.out.println("Select a piece that can move");
						 return true;
					 }
					 
					 // send to server
					 try {
						 out.writeUTF((""+guiY)+guiX);
					 } catch (IOException i) {					 
						 System.out.println(i);
					 }
				 }
				 
				 markValidMoves(guiX, guiY);
				 oldX = guiX;
				 oldY = guiY;
				 turnPhase++;
				 display();
				 break;
			 case 1:
				 if (!fromNetwork) {					 
					 if (!board[guiY][guiX].getValid()) {
						 System.out.println("Enter a valid move");
						 return true;
					 }
					 resetFlags();
					 if (turn == pieceColor.white) {
						 for (int i=0; i<whitePieces.length; i++) {
							 if (oldY == whitePieces[i][0] && oldX == whitePieces[i][1]) {
								 whitePieces[i][0] = guiY;
								 whitePieces[i][1] = guiX;
							 }
						 }
					 } else {
						 for (int i=0; i<blackPieces.length; i++) {
							 if (oldY == blackPieces[i][0] && oldX == blackPieces[i][1]) {
								 blackPieces[i][0] = guiY;
								 blackPieces[i][1] = guiX;
							 }
						 }
					 }
					 
					 // send to server
					 try {
						 out.writeUTF((""+guiY)+guiX);
					 } catch (IOException i) {					 
						 System.out.println(i);
					 }
				 } else {
					 resetFlags();
				 }
				 
				 board[guiY][guiX].setPiece(board[oldY][oldX].getPiece());
				 board[oldY][oldX].deletePiece();
				 markValidMoves(guiX, guiY);
				 turnPhase++;
				 display();
				 break;
			 case 2:
				 if (!fromNetwork) {					 
					 if (!board[guiY][guiX].getValid()) {
						 System.out.println("Enter a valid move");
						 return true;
					 }

					 // send to server
					 try {
						 out.writeUTF((""+guiY)+guiX);
					 } catch (IOException i) {					 
						 System.out.println(i);
					 }
				 }
				 
				 resetFlags();
				 board[guiY][guiX].makeBlocked();
				 
				 boolean pieceCanMove = false;
				 for (int[] pieceArray : whitePieces) {
				 	if (checkValidMoves(pieceArray[1], pieceArray[0])) {
				 		pieceCanMove = true;
				 	}
				 }
					 
				 // if no pieces can move call a loss
				 if (!pieceCanMove) {
					 resetFlags();
					 display();
					 winner(pieceColor.black);
					 input.close();
			         return true;
				 }
					 
				 pieceCanMove = false;
				 // check if any black pieces can move
				 for (int[] pieceArray : blackPieces) {
					 if (checkValidMoves(pieceArray[1], pieceArray[0])) {
						 pieceCanMove = true;
					 }
				 }
					 
				 // if no pieces can move call a loss
				 if (!pieceCanMove) {
					 resetFlags();
					 display();
					 winner(pieceColor.white);
			    	 input.close();
			    	 return true;
				 }
				 
				 
				 turnPhase = 0;
				 display();
				 turn = turn.flip();
				 break;
			 }
			 return true;
		 }
		 
		 public void checkIfWinner() {
			 
			 boolean pieceCanMove = false;
			 for (int[] pieceArray : whitePieces) {
			 	if (checkValidMoves(pieceArray[1], pieceArray[0])) {
			 		pieceCanMove = true;
			 	}
			 }
				 
			 // if no pieces can move call a loss
			 if (!pieceCanMove) {
				 resetFlags();
				 display();
				 winner(pieceColor.black);
				 input.close();
				 return;
			 }
				 
			 pieceCanMove = false;
			 // check if any black pieces can move
			 for (int[] pieceArray : blackPieces) {
				 if (checkValidMoves(pieceArray[1], pieceArray[0])) {
					 pieceCanMove = true;
				 }
			 }
				 
			 // if no pieces can move call a loss
			 if (!pieceCanMove) {
				 resetFlags();
				 display();
				 winner(pieceColor.white);
		    	 input.close();
		    	 return;
			 }
		 }
		 
		 public boolean takeTurn() { 
			 display();
			 Pattern pattern = Pattern.compile("[a-j]([1-9]|10)");
			 
			 // get a move from the user that is one of their pieces
			 int x, y; // increase scope of x and y
			 do {
		    	 System.out.print("Enter the coordinate of a piece or 'resign': ");
		    	 
		    	 // keep asking for coordinate until user is a-j followed by 1-10
		    	 while (!input.hasNext(pattern) && !input.hasNext("resign")) {
		    		 System.out.println("Please enter a valid coordinate");
		    		 input.next();
		    	 }
		    	 
		    	 if (input.hasNext("resign")) {
		    		 winner(turn.flip());
		    		 input.close();
		    		 return true;
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
			 
			 // after wall is placed, check if lost
			 boolean pieceCanMove = false;
			 if (turn == pieceColor.white) {
				 // check if any white piece can move
				 for (int[] pieceArray : whitePieces) {
					 if (checkValidMoves(pieceArray[1], pieceArray[0])) {
						 pieceCanMove = true;
					 }
				 }
				 
				 // if no pieces can move call a loss
				 if (!pieceCanMove) {
					 resetFlags();
					 display();
					 winner(turn.flip());
		    		 input.close();
		    		 return true;
				 }
				 
				 System.out.println("White's Turn:");
			 } else {
				 // check if any black pieces can move
				 for (int[] pieceArray : blackPieces) {
					 if (checkValidMoves(pieceArray[1], pieceArray[0])) {
						 pieceCanMove = true;
					 }
				 }
				 
				 // if no pieces can move call a loss
				 if (!pieceCanMove) {
					 resetFlags();
					 display();
					 winner(turn.flip());
		    		 input.close();
		    		 return true;
				 }
				 
				 System.out.println("Black's Turn:");
			 }
			 
			 resetFlags();
			 
			 turn = turn.flip();
			 
			 return false;
		 }
	
		 // reset valid flags for all squares
		 private void resetFlags() {
			 for (Square [] row : board) {
				 for (Square s : row) {
					 s.markInvalid();
				 }
			 }
			 return;
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
		 
		 
		 // call if someone wins 
		 public void winner(pieceColor color) {
			 if (color == playerColor) {
				 endText.setText("Congratulations, you won");
			 } else {
				 endText.setText("Sorry, you lost");
			 }
			 winButton.fire();
			 
			 System.out.println("Congratulations " + color + ", you won!\n");
			 input.close();
                
			 // stop connection with server
//			 try {
//				 out.writeUTF("WINNER");
//				 in.close();
//				 out.close();
//				 socket.close();
//			 } catch (IOException i) {
//				 System.out.println(i);
//			 }
				 
		 }
	 }

	 public static void main(String[] args) {
		 launch(args);
		 /*
	     // Start your game
	     Scanner input = new Scanner(System.in);
	
	     // Setup the board
	     Board board = new Board(10);
	     
	     boolean doneFlag = false;
	     while(!doneFlag) {
	    	 doneFlag = board.takeTurn();
	     }
	     
	     input.close();
	     */
	 }
}