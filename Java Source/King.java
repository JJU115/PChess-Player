/*
*	King.java
*	Date of creation: May 20, 2018
*	Date of last modification: Dec 19, 2018
*	
*	Author: Justin Underhay
*	
*/

/*
	King.java represents the king piece.
	This class contains exclusive data members and methods to handle the king's castling move.

	Inherits from Piece.java.	
	
	Data members:
		firstMove - boolean 	- True if the king has moved at all during the game, false otherwise.
*/


import javax.swing.ImageIcon;


public class King extends Piece {
	
	private boolean firstMove;
	
	public King(char pieceColor, char pieceType, int ID) {

		if (osName == "windows")
			path = "Supplemental\\";
		else
			path = "./Supplemental/";
			
		
		if (pieceColor == 'W')
			setImage(new ImageIcon(path + "W-King.png"));
		else
			setImage(new ImageIcon(path + "B-King.png"));
		
		setColor(pieceColor);
		setType(pieceType);
		setID(ID);
		setCapture(false);	
		firstMove = true;
	}
	
	/*
		notMoved() - Returns the firstMove boolean member.
	*/
	public boolean notMoved() {
		return firstMove;
	}


	/*
		moved() - Sets the firstMove boolean member to false, signifying that the King has made its first move.
	*/
	public void moved() {
		firstMove = false;
	}	


	public void setMoved(boolean B) {
		firstMove = B;
	}


	/*
		move(Chessboard cBoard) - Generates an integer array containing the Tile ID numbers of Tiles the King may legally move to.
		Kings may move one square in any direction including diagonally.
		Once per game if the King has not moved it may castle, moving two squares horizontally towards a Rook of the same color and placing
		that Rook on the last square the King moved over provided the Rook has not moved yet either.	
	*/
	public int[] move(Chessboard cBoard) {
		//First check if the King is on the fringe of the board to rule out potential moves then examine all 8 adjacent squares
		//Also assert the validity of castling conditions	
		
		int I = cBoard.fetchTileOfPiece(getID()).getID();
		int[] moves = new int[11];
		
		moves[0] = I;
		
		if (I <= 7)			//King is on the top edge of the board
			moves[1] = moves[2] = moves[3] = -1;
		if (I%8 == 0)		//King is on left edge of the board
			moves[1] = moves[4] = moves[6] = -1;
		if (I >= 56)		//King is on bottom edge of the board
			moves[6] = moves[7] = moves[8] = -1;
		if ((I+1)%8 == 0)	//King is on right edge of board
			moves[3] = moves[5] = moves[8] = -1;
		
		
		//Check all 8 squares around the King
		Piece P;
		int[] mods = {-9,-8,-7,-1,1,7,8,9};
		
		for (int i=0; i<8; i++) {
			if (moves[i+1] == -1)
				continue;

			P = cBoard.fetchTile(I+mods[i]).getPiece();
			if (P == null || P.getColor() != getColor())
				moves[i+1] = I+mods[i];
			else
				moves[i+1] = -1;	
		}	

		//Check elligibility for castling
		if (firstMove) {
			Piece R = cBoard.fetchTile(I+3).getPiece();
		
			if (cBoard.fetchTile(I+1).getPiece() == null && cBoard.fetchTile(I+2).getPiece() == null && R != null && R.getType() == 'R' && R.notMoved())	
				moves[9] = I+2;
			else
				moves[9] = -1;
				
			R = cBoard.fetchTile(I-4).getPiece();
			if (cBoard.fetchTile(I-1).getPiece() == null && cBoard.fetchTile(I-2).getPiece() == null && cBoard.fetchTile(I-3).getPiece() == null && R != null && R.getType() == 'R' && R.notMoved())	
				moves[10] = I-2;
			else
				moves[10] = -1;	
			
		} else 
			moves[9] = moves[10] = -1;
		
		
		return moves;
	}




	public int[] move(BitMap B) {

		int[] moves = new int[11];
		int I = B.getPiecePos(getID());

		moves[0] = -1;
		
		if (I <= 7)			//King is on the top edge of the board
			moves[1] = moves[2] = moves[3] = -1;
		if (I%8 == 0)		//King is on left edge of the board
			moves[1] = moves[4] = moves[6] = -1;
		if (I >= 56)		//King is on bottom edge of the board
			moves[6] = moves[7] = moves[8] = -1;
		if ((I+1)%8 == 0)	//King is on right edge of board
			moves[3] = moves[5] = moves[8] = -1;

		//Check all 8 squares around the King
		int P;
		int[] mods = {-9,-8,-7,-1,1,7,8,9};
		
		for (int i=0; i<8; i++) {
			if (moves[i+1] == -1)
				continue;
					
			P = B.getPosition(I+mods[i]);
			if (P == -1 || (getID() == 15 && P > 15) || (getID() == 31 && P < 16))
				moves[i+1] = I+mods[i];
			else
				moves[i+1] = -1;	
		}	
		

		//Check Castling requirments
		if (!B.getPStatus(getID())) {
			//Kingside
			int rook1 = B.getPosition(I+3);
			int rook2 = B.getPosition(I-4);

			if (B.getPosition(I+1) == -1 && B.getPosition(I+2) == -1 && rook1 == getID() - 6 && !B.getPStatus(rook1))
				moves[9] = I+2;
			else
				moves[9] = -1;

			if (B.getPosition(I-1) == -1 && B.getPosition(I-2) == -1 && B.getPosition(I-3) == -1 && rook2 == getID() - 7 && !B.getPStatus(rook2))	
				moves[10] = I-2;
			else
				moves[10] = -1;	
		} else 
			moves[9] = moves[10] = -1;
			
			
		return moves;

	}



}	
