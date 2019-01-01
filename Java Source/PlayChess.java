/*
*	PlayChess.java
*	Date of creation: June 10, 2018
*	Date of last modification: Dec 22, 2018
*	
*	Author: Justin Underhay
*	
*/

/*
	PlayChess.java is the driver of the chess program where the main method of the actual program is executed. 
	This class utilizes all other files as instantiated objects in order to play the game.
	The main method consists of one big loop which alternates between players enforcing valid moves and the rules.
	Check, Checkmate, Stalemate, and promotion are handled in outside static methods.
	The end of execution of the main method signifies the game has ended for one of several reasons whereby the program exits.
*/


import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.SpringLayout;
import javax.swing.JOptionPane;
import javax.swing.BorderFactory;
import javax.swing.border.EmptyBorder;
import javax.swing.border.CompoundBorder;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Font;
import java.util.Arrays;
import java.lang.Math;


public class PlayChess {
	
	public static void main(String[] args) {

		//Welcome, Help, DisplayScreen object instantiation
		Welcome page1 = new Welcome();
		Help page2 = new Help();
		DisplayScreen currentScreen = page1;
		
		
		//Initialize Welcome page JFrame and set its properties
		JFrame wPage = new JFrame("Welcome");
		wPage.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		wPage.setSize(new Dimension(700, 500));
		wPage.setLocation(610,290);
		wPage.setResizable(false);
		wPage.add(page1.getPanel());
		wPage.setVisible(true);
		
		
		//Initialize Help page JFrame and set its properties
		JFrame hPage = new JFrame("Help");
		hPage.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		hPage.setSize(new Dimension(920, 700));
		hPage.setResizable(false);
		hPage.add(page2.getScrollPane());
		
		
		//To keep track of currentScreen's current position on screen
		Point location;
		
		
		//Wait for user interaction and set currentScreen appropriately, with sleep() method to save CPU clock cycles while idling
		try {
			while (currentScreen.getChoice() != "Play!") {
				Thread.sleep(10);
				switch (currentScreen.getChoice()) {
					case "Quit":
						wPage.dispose();
						hPage.dispose();
						System.exit(3);
						break;
					case "Help":
						currentScreen = page2;
						page1.reset();
						wPage.setVisible(false);
						location = wPage.getLocation();
						location.translate(-100,0);
						hPage.setLocation(location);
						hPage.setVisible(true);
						break;
					case "Back":
						currentScreen = page1;
						page2.reset();
						hPage.setVisible(false);
						location = hPage.getLocation();
						location.translate(100,0);
						wPage.setLocation(location);
						wPage.setVisible(true);
						break;	
				}	
			}	
		} catch (InterruptedException e) {
			System.out.println("Unexpected error, quitting application...");
			System.exit(-1);
		}	
		
		
		//Initialize JComponents
		JFrame GUI = new JFrame("Java Chess");
		JLabel pTurn = new JLabel("White's Turn");
		JLabel pSelect = new JLabel();
		JLabel timer = new JLabel("0:00:00");
		JButton quitButton = new JButton("Quit");
		
		
		//Quit button
		quitButton.setActionCommand("Quit");
		quitButton.addActionListener(new AListener());
		
		
		//The layout for the GUI JFrame
		SpringLayout Slayout = new SpringLayout();
		GUI.setLayout(Slayout);
		
		
		//Set the decorative borders
		pTurn.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		pTurn.setBorder(new CompoundBorder(pTurn.getBorder(), new EmptyBorder(10,10,10,10)));
		pSelect.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		pSelect.setBorder(new CompoundBorder(pSelect.getBorder(), new EmptyBorder(10,10,10,10)));
		timer.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		timer.setBorder(new CompoundBorder(timer.getBorder(), new EmptyBorder(10,10,10,10)));
		
		
		//Size and font
		pSelect.setPreferredSize(new Dimension(135,135));
		quitButton.setFont(new Font("Times New Roman", Font.BOLD, 20));
		pTurn.setFont(new Font("Times New Roman", Font.PLAIN, 22));
		timer.setFont(new Font("Times New Roman", Font.BOLD, 22));
		
		
		//Set GUI properties
		GUI.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		GUI.setSize(new Dimension(1200,1050));
		GUI.setLocation(360,10);
		GUI.setResizable(false);
		
		
		//Create the chessboard and set its pieces
		Chessboard board = new Chessboard();
		board.setPieces();
		
		
		//Add all the components	
		GUI.add(board.getBoard());
		GUI.add(pTurn);
		GUI.add(pSelect);
		GUI.add(timer);
		GUI.add(quitButton);
		
		
		//Alter layout of components
		Slayout.putConstraint("West", pTurn, 20, "East", board.getBoard());
		Slayout.putConstraint("North", pTurn, 5, "North", GUI);
		Slayout.putConstraint("West", pSelect, 20, "East", board.getBoard());
		Slayout.putConstraint("North", pSelect, 10, "South", pTurn);
		Slayout.putConstraint("West", quitButton, 40, "East", board.getBoard());
		Slayout.putConstraint("North", quitButton, 950, "North", GUI);
		Slayout.putConstraint("West", timer, 45, "East", board.getBoard());
		Slayout.putConstraint("North", timer, 10, "South", pSelect);


		//Give option to play against CPU
		boolean playCPU = (JOptionPane.showConfirmDialog(null, "Would you like to play against the computer? (Computer always plays Black)") == JOptionPane.YES_OPTION) ? true : false;

		hPage.dispose();
		wPage.dispose();

		//Initialize the timer
		TimerThread clock = new TimerThread(timer);
		clock.start();

		GUI.setVisible(true);
		
		
		//Variables for use in main play loop
		boolean act = false, act2 = false, checkMate = false, staleMate = false;
		char turnColor = 'W';
		int T, mod, turnInt = 0, turnNum = 0;
		Piece placeHolder, checker = null;
		Tile Q = null, P = null;
		int[] validMoves;

		GameTree CPU = new GameTree();
		int[] CPUMove = new int[2];
		
		
		
		/*
			Main play loop. As long as neither player lands in checkmate or a stalemate has occurred play will continue 
			within this loop. First, all tiles containing pieces of the current player's turn are scanned to see if any
			have been clicked. After a tile is clicked the valid moveset of the piece on that tile is acquired and all
			tiles listed there will be scanned. If the player clicks the same tile the move is cancelled and the first step
			is repeated otherwise the selected piece will move to the second clicked tile and the turn ends.

			At the end of each turn the board is examined for check/checkmate/stalemate.
		*/
		
		
		while (!checkMate && !staleMate) {
			turnNum++;

			if (turnNum % 2 != 0 || !playCPU) {
			
				//1st loop: wait for player to click tile with piece on it
				while (!act2) {
					T = turnInt;
					Q = board.fetchTileOfPiece(T);
					board.resetBoard();
					
					
					while (!act) {
						T = (T == turnInt+15) ? turnInt : T+1;
						Q = board.fetchTileOfPiece(T);
						if (Q == null)
							continue;
						act = Q.getAct();
					}
					
					
					//At this point, Q is the player selected tile
					if (Q.getPiece().getType() == 'P')
						Q.getPiece().setCurrentTurn(turnNum);
					
					
					pSelect.setIcon(Q.getPiece().getImage());
					board.resetBoard();
					T = 0;
					validMoves = Q.getPiece().move(board);
					
					
					//P will hold the 2nd player selected tile
					P = board.fetchTile(validMoves[T]);
					
					
					//2nd loop: wait for player to select tile to move piece to
					while (!act2) {
						T = (T == validMoves.length-1) ? 0 : T+1;
						if (validMoves[T] != -1)
							P = board.fetchTile(validMoves[T]);
						act2 = P.getAct();
					}	
					
					
					//If the player has decided to castle this section ensures all conditions are fulfilled
					if (Q.getPiece().getType() == 'K' && Math.abs(P.getID()-Q.getID()) == 2) {
						if (validateCheck(board,Q.getID()+(P.getID()-Q.getID())/2,Q.getPiece().getColor()) != null) {
							act = act2 = false;
							JOptionPane.showMessageDialog(null,"Cannot castle, passing through check");
						}
						if (validateCheck(board,Q.getID(),Q.getPiece().getColor()) != null) {
							act = act2 = false;
							JOptionPane.showMessageDialog(null,"Cannot castle out of check");
						}	
					}	
				
				
					//If the ID of P and Q are equal, the player has clicked the same tile twice meaning they wish to select a different piece
					if (P.getID() == Q.getID()) {
						act = act2 = false;
						pSelect.setIcon(null);	
					
					
					//If P and Q have unequal IDs a move has been made. This section simulates that move and determines whether a player has
					//put themselves in check or has not escaped from it	
					} else if (act || act2) {	
						board.updatePieceList(Q.getPiece().getID(),P.getID());
						placeHolder = P.getPiece();
						
						if (placeHolder != null) 
							board.updatePieceList(placeHolder.getID(),-1);
					
						P.setPiece(Q.getPiece());
						Q.setPiece(null);
						checker = validateCheck(board,board.fetchTileOfPiece(turnInt+15).getID(),turnColor);
						
						if (checker != null) {
							JOptionPane.showMessageDialog(null,"Still in check or moving into check!");
							pSelect.setIcon(null);
							act = false;
							act2 = false;
						}	
					
						board.updatePieceList(P.getPiece().getID(),Q.getID());
						if (placeHolder != null)
							board.updatePieceList(placeHolder.getID(),P.getID());
						Q.setPiece(P.getPiece());
						P.setPiece(placeHolder);
					}	
				}
			} else {
				CPUMove = CPU.getNextMove(board);
				Q = board.fetchTileOfPiece(CPUMove[0]);
				P = board.fetchTile(CPUMove[1]);			
			}	
			
			
			//At this point it has been determined that the player's move is valid
			if (Q.getPiece().getType() == 'P') 
				Q.getPiece().setDoubleJump(true);
					
					
			board.updatePieceList(Q.getPiece().getID(),P.getID());
			
			
			if (P.getPiece() != null) {
				P.getPiece().setCapture(true);
				board.updatePieceList(P.getPiece().getID(), -1);
			}	
			
			
			mod = (turnColor == 'B') ? 1 : -1;
			
			
			//This section handles an en-passant capture
			if (Q.getPiece().getType() == 'P' && (P.getID()-Q.getID() == mod*7 || P.getID()-Q.getID() == mod*9)) {
				if (P.getPiece() == null) {
					Tile E = board.fetchTile(P.getID()-mod*8);
					E.getPiece().setCapture(true);
					board.updatePieceList(E.getPiece().getID(),-1);
					E.setPiece(null);
				}
			}
			
			
			//Set the piece in its new position
			P.setPiece(Q.getPiece());
			Q.setPiece(null);
			
			
			//Section for handling promotion
			if (P.getPiece().getType() == 'P') {
				Piece Pi = P.getPiece();
				if ((Pi.getColor() == 'B' && P.getID() >= 56) || (Pi.getColor() == 'W' && P.getID() <= 7))
					processPromotion(board, Pi);
			}	

			
			//Section for handling castling
			if (P.getPiece().getType() == 'R')
				P.getPiece().moved();
			
			if (P.getPiece().getType() == 'K') {
				P.getPiece().moved();
				if (P.getID()-Q.getID() == 2) {
					board.updatePieceList(board.fetchTile(P.getID()+1).getPiece().getID(),Q.getID()+1);	
					board.fetchTile(Q.getID()+1).setPiece(board.fetchTile(P.getID()+1).getPiece());
					board.fetchTile(Q.getID()+1).getPiece().moved();
					board.fetchTile(P.getID()+1).setPiece(null);
				} else if (P.getID()-Q.getID() == -2) {
					board.updatePieceList(board.fetchTile(P.getID()-2).getPiece().getID(),Q.getID()-1);
					board.fetchTile(Q.getID()-1).setPiece(board.fetchTile(P.getID()-2).getPiece());
					board.fetchTile(Q.getID()-1).getPiece().moved();
					board.fetchTile(P.getID()-2).setPiece(null);
				}	
			}
		
			
			//This section examines if the moving player has put the other in check
			checker = (turnColor == 'W') ? validateCheck(board,board.fetchTileOfPiece(31).getID(),'B') : validateCheck(board,board.fetchTileOfPiece(15).getID(),'W');
				
				
			//If the opposing player has been put in check, must examine if they have also been put in checkmate	
			if (checker != null && turnColor == 'W') 	{
				checkMate = examinePlayerStatus(16, board);
				JOptionPane.showMessageDialog(null,"Check by White");
			} else if (checker != null) { 
				checkMate = examinePlayerStatus(0, board);
				JOptionPane.showMessageDialog(null,"Check by Black");
			}	
				
				
			//All conditions checked at this point, now reset loop conditions, alter variables and change the turn	
			act = false;
			act2 = false;
			turnColor = (turnColor == 'W') ? 'B' : 'W';
			turnInt = (turnColor == 'W') ? 0 : 16;
			
			if (turnColor == 'B')
				pTurn.setText("Black's Turn");
			else
				pTurn.setText("White's Turn");
			
			pSelect.setIcon(null);
			
			//Examine for potential stalemate - King not in check but cannot move
			if (checker == null) 
				staleMate = examinePlayerStatus(turnInt, board);
				
		}
		//End of main play loop. This point is reached if checkmate or stale mate has occurred
		
		
		
		if (checkMate) {
			if (turnColor == 'W')
				JOptionPane.showMessageDialog(null,"Black wins by Checkmate!");
			else
				JOptionPane.showMessageDialog(null,"White wins by Checkmate!");	
		}

		if (staleMate)
			JOptionPane.showMessageDialog(null,"Stalemate!");	
		
		JOptionPane.showMessageDialog(null, "The program will now exit.");
		System.exit(0);
	}
	
	
	
	/*
		validateCheck(Chessboard C, int kingTile, char color) - Given kingTile, the ID of the Tile a player's King occupies, 
		checks if the King is currently under direct attack by any enemy piece(s) and returns that piece with the lowest ID. 
		Null otherwise.	
	*/
	
	public static Piece validateCheck(Chessboard C, int kingTile, char color) {
		
		//Initialize
		Piece attacker = null;
		int startIndex = (color == 'W') ? 16 : 0;
		int[] moves;
		
		
		//Get potential moves of every enemy piece
		for (int i=startIndex; i<startIndex+16; i++) {
			if (C.fetchPiece(i) == null)
				continue;
			attacker = C.fetchPiece(i);
			moves = attacker.move(C);
			
			
			//For every potential move check if kingTile is one of them
			for (int j=0; j<moves.length; j++) {
				if (moves[j] == -1)
					continue;
				if (moves[j] == kingTile) 
					return attacker;			
			}	
		}	
			
		return null;	
	}	
	
	
	
	/*
		examinePlayerStatus(int turn, Chessboard board) - For a given player, simulates every possible move of every piece they still possess
		and determines if at least one of those possible moves does not place their King in check, returning a boolean as a result.
		Useful for determining stalemate and checkmate.	
	*/
	
	public static boolean examinePlayerStatus(int turn, Chessboard board) {
	
		//Initialize		
		int[] pieceMoves;
		Piece P1, P2;
		boolean inCheck = true;
		char color = (turn == 16) ? 'B' : 'W';
		
		
		//For every piece that player to move has, simulate every move and see if the player is put in check
		for (int i=turn; i<turn+16; i++) {
			
			
			//Get piece and acquire its moves
			P1 = board.fetchPiece(i);
			if (P1 == null)
				continue;
			pieceMoves = P1.move(board);
			
			for (int j=1; j<pieceMoves.length; j++) {
				if (pieceMoves[j] == -1)
					continue;
				
				
				//Make necessary changes to board and pieceList to simulate move
				P2 = board.fetchTile(pieceMoves[j]).getPiece();
				board.fetchTile(pieceMoves[j]).setPiece(P1);
				board.fetchTile(pieceMoves[0]).setPiece(null);
				board.updatePieceList(P1.getID(), pieceMoves[j]);
				if (P2 != null)
					board.updatePieceList(P2.getID(), -1);
				
				
				//Examine for check
				if (validateCheck(board, board.fetchTileOfPiece(turn+15).getID(), color) == null) 
					inCheck = false;	
			
			
				//Reverse any simulation changes to board and pieceList
				board.fetchTile(pieceMoves[0]).setPiece(P1);
				board.updatePieceList(P1.getID(), pieceMoves[0]);
				board.fetchTile(pieceMoves[j]).setPiece(P2);
				if (P2 != null) 
					board.updatePieceList(P2.getID(), pieceMoves[j]);	
				
				
				//Only if the loop fully completes without jumping here has a stalemate occurred
				if (!inCheck)
					return false;
			}	
		}		

		return true;	
	}	
	
	
	
	/*
		processPromotion(Chessboard C, Piece P) - If a player's Pawn has reached the opposite end of the board from their starting position
		this static method is invoked to construct and display the promotion selection screen, a PromotionMenu object, and handle the player's
		interaction. The Pawn is replaced with the player's selected piece adopting the previous Pawn's ID.
	*/
	
	public static void processPromotion(Chessboard C, Piece P) {
		
		//Instantiation of base objects
		PromotionMenu selectScreen = new PromotionMenu(P.getColor());
		JFrame frame = new JFrame("Promotion");
		
		
		//Set main JFrame properties
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.setSize(new Dimension(500,350));	
		frame.add(selectScreen.getPanel());
		frame.setTitle("Pawn Promotion");
		frame.setVisible(true);
		
		
		//Wait for the player to make their piece selection, make use of sleep() method to save CPU clock cycles
		try {
			while (!selectScreen.selectionMade())
				Thread.sleep(10);
		} catch (InterruptedException e) {
			System.out.println("Unexpected error, quitting application...");
			System.exit(-1);
		}
		
		
		//Identify player choice and make piece change
		switch (selectScreen.getChoice()) {
			case "Knight":
				C.fetchTileOfPiece(P.getID()).setPiece(new Knight(P.getColor(), 'N', P.getID()));
				break;
			case "Rook":
				C.fetchTileOfPiece(P.getID()).setPiece(new Rook(P.getColor(), 'R', P.getID()));
				break;
			case "Bishop":
				C.fetchTileOfPiece(P.getID()).setPiece(new Bishop(P.getColor(), 'B', P.getID()));
				break;
			case "Queen":
				C.fetchTileOfPiece(P.getID()).setPiece(new Queen(P.getColor(), 'Q', P.getID()));	
				break;		
		}	
		
		
		//Promotion unlikely to occur many times per game hence the main JFrame can be discarded and recreated as necessary
		frame.dispose();
	}	
}	