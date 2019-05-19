/*
*	Knight.java
*	Date of creation: May 17, 2018
*	Date of last modification: Nov 4, 2018
*	
*	Author: Justin Underhay
*	
*/

/*
	Knight.java represents the knight piece.
	The Knight possesses no special moves.
*/


import javax.swing.ImageIcon;


public class Knight extends Piece {
	
	public Knight(char pieceColor, char pieceType, int ID) {

		if (osName == "windows")
			path = "Supplemental\\";
		else
			path = "./Supplemental/";

		
		if (pieceColor == 'W')
			setImage(new ImageIcon(path + "W-Knight.png"));
		else
			setImage(new ImageIcon(path + "B-Knight.png"));
		
		setColor(pieceColor);
		setType(pieceType);
		setID(ID);
		setCapture(false);	
	}	
	
	
	/*
		move(Chessboard cBoard) - Generates an integer array containing the Tile ID numbers of Tiles the Knight may legally move to.
		The Knight moves in an 'L' shape: 2 squares horizontally and 1 square vertically, or 2 squares vertically and 1 square horizontally.
		The Knight is the only piece able to jump over other pieces.
	*/
	public int[] move(Chessboard cBoard) {
		//Sequentially check all possible squares the Knight can move to, up to a maximum of 8 different spots.
		
		int[] moves = new int[9];
		int I = cBoard.fetchTileOfPiece(getID()).getID();
		
		moves[0] = I;
		
		if (I >= 16) {
			moves[1] = (I%8 != 0) ? I-17 : -1;		//Up-left
			moves[2] = ((I+1)%8 != 0) ? I-15 : -1;	//Up-right
		} else
			moves[1] = moves[2] = -1;	
		
		if (I <= 47) {
			moves[3] = (I%8 != 0) ? I+15 : -1;		//Down-left
			moves[4] = ((I+1)%8 != 0) ? I+17 : -1;	//Down-right
		} else
			moves[3] = moves[4] = -1;	
		
		if ((I+1)%8 != 0 && (I+2)%8 != 0) {
			moves[5] = (I > 7) ? I-6 : -1;		//Right-up
			moves[6] = (I < 56) ? I+10 : -1;	//Right-down
		} else
			moves[5] = moves[6] = -1;

		if ((I%8) != 0 && (I-1)%8 != 0) {
			moves[7] = (I > 7) ? I-10 : -1;		//Left-up	
			moves[8] = (I < 56) ? I+6 : -1;		//Left-down
		} else
			moves[7] = moves[8] = -1;

		for (int i=1; i<9; i++) {
			if (moves[i] == -1)
				continue;
			Piece P = cBoard.fetchTile(moves[i]).getPiece();
			if (P != null && P.getColor() == getColor())
				moves[i] = -1;
		}	
					
		return moves;
	}	



	public int[] move(BitMap B) {

		int[] moves = new int[9];
		int I = B.getPiecePos(getID());

		moves[0] = -1;

		if (I >= 16) {
			moves[1] = (I%8 != 0) ? I-17 : -1;		//Up-left
			moves[2] = ((I+1)%8 != 0) ? I-15 : -1;	//Up-right
		} else
			moves[1] = moves[2] = -1;	
		
		if (I <= 47) {
			moves[3] = (I%8 != 0) ? I+15 : -1;		//Down-left
			moves[4] = ((I+1)%8 != 0) ? I+17 : -1;	//Down-right
		} else
			moves[3] = moves[4] = -1;	
		
		if ((I+1)%8 != 0 && (I+2)%8 != 0) {
			moves[5] = (I > 7) ? I-6 : -1;		//Right-up
			moves[6] = (I < 56) ? I+10 : -1;	//Right-down
		} else
			moves[5] = moves[6] = -1;

		if ((I%8) != 0 && (I-1)%8 != 0) {
			moves[7] = (I > 7) ? I-10 : -1;		//Left-up	
			moves[8] = (I < 56) ? I+6 : -1;		//Left-down
		} else
			moves[7] = moves[8] = -1;


		for (int i=1; i<9; i++) {
			if (moves[i] == -1)
				continue;
			int P = B.getPosition(moves[i]);
			if (P != -1 && (P < 16 && getID() < 16) || (P > 15 && getID() > 15))
				moves[i] = -1;
		}	
						
		return moves;	
	}
}	
