/*
*	Queen.java
*	Date of creation: May 18, 2018
*	Date of last modification: Nov 4, 2018
*	
*	Author: Justin Underhay
*	
*/

/*
	Queen.java represents the Queen piece.
	The queen possesses no special moves.
	
	Inherits from Piece.java

*/


import javax.swing.ImageIcon;


public class Queen extends Piece {
	
	
	public Queen(char pieceColor, char pieceType, int ID) {
		
		if (pieceColor == 'W')
			setImage(new ImageIcon("Supplemental\\W-Queen.png"));
		else
			setImage(new ImageIcon("Supplemental\\B-Queen.png"));
		
		setColor(pieceColor);
		setType(pieceType);
		setID(ID);
		setCapture(false);	
	}	
	
	
	/*
		move(Chessboard cBoard) - Generates an integer array containing the Tile ID numbers of Tiles the Queen may legally move to.
		The Queen combines the movement power of the Rook and Bishop: horizontally, vertically, and diagonally in any direction.
		The Queen cannot jump over other pieces.
	*/
	public int[] move(Chessboard cBoard) {
		//The Queen's valid moves can be obtained by creating a Rook and Bishop and calling their respective move methods. 
		
		int[] M1 = (new Rook(getColor(),'R',getID())).move(cBoard);
		int[] M2 = (new Bishop(getColor(),'B',getID())).move(cBoard);
		
		int[] moves = new int[M1.length+M2.length];
		
		System.arraycopy(M1,0,moves,0,M1.length);
		System.arraycopy(M2,1,moves,M1.length,M2.length-1);
		
		return moves;
	}	



	public int[] move(BitMap B) {

		int[] M1 = (new Rook(getColor(),'R',getID())).move(B);
		int[] M2 = (new Bishop(getColor(),'B',getID())).move(B);
		
		int[] moves = new int[M1.length+M2.length];

		System.arraycopy(M1,0,moves,0,M1.length);
		System.arraycopy(M2,0,moves,M1.length,M2.length);

		return moves;
	}
}	