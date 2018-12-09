/*
*	Bishop.java
*	Date of creation: May 15, 2018
*	Date of last modification: Nov 6, 2018
*	
*	Author: Justin Underhay
*	
*/

/*
	Bishop.java represents the Bishop piece.
	The Bishop possesses no special moves.
*/

import javax.swing.ImageIcon;


public class Bishop extends Piece {
	
	public Bishop(char pieceColor, char pieceType, int ID) {
		
		if (pieceColor == 'W')
			setImage(new ImageIcon("Supplemental\\W-Bishop.png"));
		else
			setImage(new ImageIcon("Supplemental\\B-Bishop.png"));
		
		setColor(pieceColor);
		setType(pieceType);
		setID(ID);
		
		setCapture(false);	
	}	
	

	/*
		move(Chessboard cBoard) - Generates an integer array containing the Tile ID numbers of Tiles the Bishop may legally move to.
		The bishop can move any number of squares diagonally but cannot jump over other pieces. 
	*/
	public int[] move(Chessboard cBoard) {
		//Determine the max number of squares movable in each direction then check for another piece in the same path

		int[] directions = new int[4];
		int I = cBoard.fetchTileOfPiece(getID()).getID();
		int P = I;
		int q, mLen = 0, amt = 0, mod = 0, i = 1;
	
		for (int p=0; p<2; p++) {
			while (I > 7 && (I+p)%8 != 0) {
				directions[p]++;
				I -= (9 - p*2);
			}
			I = P;
			
			while (I < 56 && (I+p)%8 != 0) {
				directions[p+2]++;
				I += (7 + p*2);
			}	
			I = P;
			mLen += (directions[p]+directions[p+2]);
		}	
	
	
		int[] moves = new int[mLen+1];
		int[] mods = {-9, -7, 7, 9};
		
		moves[0] = I;
		
		for (int j=0; j<4; j++) {
			I = P;
			mod = mods[j];
			amt = directions[j];
		
			I += mod;
			for (q=0; q<amt; q++) {
				Tile T = cBoard.fetchTile(I);
				if (T.getPiece() != null) {
					if (T.getPiece().getColor() != getColor())
						moves[i++] = T.getID();
					else
						moves[i++] = -1;
					I += mod;
					break;
				}
				moves[i++] = T.getID();
				I += mod;		
			}

			for (int l=0; l<amt-q-1; l++) 
				moves[i++] = -1;
		}
		return moves;
	}	



	public int[] move(BitBoard B) {

		int[] preCheck = new int[14];
		int I = B.getPiecePos(getID());
		int mod, mag;
		int pos = 0;

		for (int i=0; i<4; i++) {
			mod = (i%2 == 0) ? 1 : -1;
			mag = (i < 2) ? 7 : 9;

			for (int j=1; j<8; j++) {
				if (i == 0 || i == 3) {
					if ((I + mod*mag*(j-1)) % 8 == 0)
						break;
				} else { 
					if ((I + mod*mag*(j-1) + 1) % 8 == 0)	
						break;
				}
				
				if (I + mod*mag*j < 0 || I + mod*mag*j > 63)
					break;

				if (getID() < 16 && B.getPosition(I+mod*mag*j) > 15) {
					preCheck[pos++] = I + mod*mag*j;
					break;	
				} else if (getID() > 15 && B.getPosition(I+mod*mag*j) < 16) {
					preCheck[pos++] = I + mod*mag*j;
					break;
				}	

				if (B.getPosition(I + mod*mag*j) == -1)
					preCheck[pos++] = I + mod*mag*j;
				else	 
					break;
			}		
		}			

		int[] moves = new int[pos];

		for (int i=0; i<pos; i++)
			moves[i] = preCheck[i];

		return moves;	
	}
}	