/*
*	Pawn.java
*	Date of creation: April 25, 2018
*	Date of last modification: Nov 19, 2018
*	
*	Author: Justin Underhay
*	
*/

/*
	Pawn.java represents the pawn piece.
	This class contains exclusive data members and instance methods to handle the pawn's special moves.
	
	Inherits from Piece.java.
	
	Data members:
		currentTurn - int		- Stores the current turn number of the game when setCurrentTurn invoked
		doubleJump - boolean	- True if the pawn has made a double jump, false otherwise
*/


import javax.swing.ImageIcon;


public class Pawn extends Piece {
	
	private int currentTurn; 
	private boolean doubleJump;

	
	public Pawn(char pieceColor, char pieceType, int ID) {

		if (osName == "windows")
			path = "Supplemental\\";
		else
			path = "./Supplemental/";

		
		if (pieceColor == 'W')
			setImage(new ImageIcon(path + "W-Pawn.png"));
		else
			setImage(new ImageIcon(path + "B-Pawn.png"));
		
		setColor(pieceColor);
		setType(pieceType);
		setID(ID);
		
		currentTurn = 0;
		doubleJump = false;
		setCapture(false);	
	}
	
	
	/*
		setCurrentTurn(int turnNum) - Sets the currentTurn data member to turnNum. 
	*/
	public void setCurrentTurn(int turnNum) {
		currentTurn = turnNum;
	}	
	
	
	/*
		getCurrentTurn() - Returns the currentTurn data member.
	*/
	public int getCurrentTurn() {
		return currentTurn;
	}	
	
	
	/*
		setCurrentTurn(int turnNum) - Sets the currentTurn data member to turnNum. 
	*/
	public void setDoubleJump(boolean value) {
		doubleJump = value;
	}	
	
	
	/*
		getDoubleJump() - Returns the doubleJump data member.
	*/
	public boolean getDoubleJump() {
		return doubleJump;
	}	
	
	
	/*
		move(Chessboard cBoard) - Generates an integer array containing the Tile ID numbers of Tiles the Pawn may legally move to.
		Pawns may move one square towards the other player's side and one square diagonally to capture enemy pieces. 
		On each pawn's first move it may double jump, moving two squares forward rather than one. If the pawn has already moved at all it may not double jump.
		Pawns have the special move en-passant which allows movement one square forward diagonally behind an enemy pawn that is directly adjacent on the left or right
		if that pawn has on the directly preceding turn double jumped.
		If a pawn reaches the end of the opposite side of the board from where it started it may be promoted into a Rook, Bishop, Queen, or Knight.
	*/
	public int[] move(Chessboard cBoard) {
		//Need only check 1 square forward but have 3 special cases: double jump, en-passant, and diagonal capture
		
		int[] moves = new int[7];
		int mod = (getColor() == 'B') ? 1 : -1;
		int C = (cBoard.fetchTileOfPiece(getID())).getID();
		
		moves[0] = cBoard.fetchTileOfPiece(getID()).getID();
		
		// This section determines whether the pawn is elligible to double jump
		if (!doubleJump && cBoard.fetchTile(C+mod*8).getPiece() == null && cBoard.fetchTile(C+mod*16).getPiece() == null) {
			if (getColor() == 'B') 
				moves[1] = (C >= 8 && C <= 15) ? C+16 : -1;				
			else 	
				moves[1] = (C >= 48 && C <= 55) ? C-16 : -1;
		} else 
			moves[1] = -1;	
		
		
		//The pawn can always move one square forward unless there is a piece blocking the path		
		moves[2] = (cBoard.fetchTile(C+mod*8).getPiece() == null) ? C+mod*8 : -1;
			
			
		// Following section determine whether the pawn can move diagonally to capture an enemy piece	
		Tile T;
		for (int q=3; q<5; q++) {
			if ((C+q-3)%8 != 0) {
				T = (getColor() == 'B') ? cBoard.fetchTile(C+7+(q-3)*2) : cBoard.fetchTile(C-9+(q-3)*2);
				if (T.getPiece() != null && T.getPiece().getColor() != getColor())	
					moves[q] = (getColor() == 'B') ? C+7+(q-3)*2 : C-9+(q-3)*2;
				else
					moves[q] = -1;
			} else
				moves[q] = -1;	
		}	
		
		
		// Section for checking En-Passant eligibility
		Piece target;
		if (getColor() == 'B') {
			for (int p=5; p<7; p++) {
				if (C > 31+(6-p) && C < 39+(6-p)) {
					target = cBoard.fetchTile(C-1+(p-5)*2).getPiece();
					if (target != null && target.getType() == 'P' && target.getColor() != getColor())
						if (target.getDoubleJump() && currentTurn == target.getCurrentTurn()+1)
							moves[p] = C+7+(p-5)*2;
				}		
			}
			
		} else {
			for (int p=5; p<7; p++) {
				if (C > 23+(6-p) && C < 31+(6-p)) {
					target = cBoard.fetchTile(C-1+(p-5)*2).getPiece();
					if (target != null && target.getType() == 'P' && target.getColor() != getColor())
						if (target.getDoubleJump() && currentTurn == target.getCurrentTurn()+1)
							moves[p] = C-9+(p-5)*2;
				}		
			}
		}	
		
		//Any spots in the array equal to zero here have been deemed illegal moves and are set to -1
		moves[5] = (moves[5] != 0) ? moves[5] : -1;
		moves[6] = (moves[6] != 0) ? moves[6] : -1;
			
		return moves;	
	}	



	public int[] move(BitMap B) {

		int[] moves = new int[4];
		int I = B.getPiecePos(getID());
		int mod = (getID() < 16) ? -1 : 1;
		
		if (getID() < 16)
			moves[0] = (I > 7 && B.getPosition(I-8) == -1) ? I-8 : -1;
		else
			moves[0] = (I < 56 && B.getPosition(I+8) == -1) ? I+8 : -1;


		if (moves[0] != -1 && !B.getPStatus(getID()) && B.getPosition(I+mod*16) == -1)
			moves[1] = I+mod*16;
		else
			moves[1] = -1;
	
		//Unless computer play for white implemented, this first part is unnecessary	
		if (getID() < 16) {
			if (I%8 != 0 && I > 8)
				moves[2] = (B.getPosition(I-9) > 15) ? I-9 : -1;
			else
				moves[2] = -1;	

			if ((I+1)%8 != 0 && I > 7)
				moves[3] = (B.getPosition(I-7) > 15) ? I-7 : -1;
			else
				moves[3] = -1;

		} else {
			if (I%8 != 0 && I < 57)
				moves[2] = (B.getPosition(I+7) < 16 && B.getPosition(I+7) >= 0) ? I+7 : -1;
			else
				moves[2] = -1;

			if ((I+1)%8 != 0 && I < 55)
				moves[3] = (B.getPosition(I+9) < 16 && B.getPosition(I+9) >= 0) ? I+9 : -1;
			else
				moves[3] = -1;	
		}	
		

		return moves;
	}
}	
