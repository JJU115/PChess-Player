/*
*	Piece.java
*	Date of creation: April 25, 2018
*	Date of last modification: Nov 6, 2018
*	
*	Author: Justin Underhay
*	
*/

/*
	Piece.java serves as the parent class for all other piece classes (Pawn, Rook, Bishop...) which inherit data members and methods.
	Other programs work primarily with Piece objects, so all procedures work with a Piece object or any of the child classes.
	
	Data members:
		color - char			- The color of the Piece, either black 'B' or white 'W' 
		type - char				- The specific subclass, Pawn 'P', Rook 'R', Queen 'Q' ect...
		pieceID - int			- The ID integer of this Piece ranging from 0-15 for White and 16-31 for Black
		captured - boolean		- True if the Piece has been captured, false otherwise
		image - ImageIcon		- A picture of the Piece as a .png file in the Supplemental directory 
*/


import javax.swing.ImageIcon;


public class Piece {
	
	private char color;
	private char type;
	private int pieceID;
	private boolean captured;
	private ImageIcon image;	
	
	
	/*
		setColor(char C) - Sets the color member to C.
	*/
	public void setColor(char C) {
		color = C;
	}
	

	/*
		getColor() - Returns the color data member.
	*/
	public char getColor() {
		return color;
	}
	

	/*
		getType() - Returns the type data member.
	*/
	public char getType() {
		return type;
	}	
	
	
	/*
		setType(char T) - Sets the type member to T.
	*/
	public void setType(char T) {
		type = T;
	}
	

	/*
		getID() - Returns the pieceID data member.
	*/
	public int getID() {
		return pieceID;
	}	
	

	/*
		setID(int I) - Sets the pieceID member to I.
	*/
	public void setID(int I) {
		pieceID = I;
	}
	
	
	/*
		setCapture(boolean B) - Sets the captured member to B.
	*/
	public void setCapture(boolean B) {
		captured = B;
	}

	
	/*
		Returns the captured data member.
	*/
	public boolean getCapture() {
		return captured;
	}
	
	
	/*
		setImage(ImageIcon I) - Sets the image member to I.
	*/
	public void setImage(ImageIcon I) {
		image = I;
	}	

	
	/*
		getImage() - Returns the image data member.
	*/
	public ImageIcon getImage() {
		return image;	
	}


	/*
		setCurrentTurn(int turnNum) - For use and overridden in Pawn.java.
	*/
	public void setCurrentTurn(int turnNum) { }
	
	
	/*
		getCurrentTurn() - For use and overridden in Pawn.java.
	*/
	public int getCurrentTurn() { 
		return -1;
	}
	
	
	/*
		notMoved() - For use and overridden in King.java.
	*/
	public boolean notMoved() { 
		return true;
	}
	
	
	/*
		moved() - For use and overridden in King.java.
	*/
	public void moved() { }


	public void setMoved(boolean B) { }

	
	/*
		setDoubleJump(boolean value) - For use and overridden in Pawn.java.
	*/
	public void setDoubleJump(boolean value) { }	
	
	
	/*
		getDoubleJump() - For use and overridden in Pawn.java.
	*/
	public boolean getDoubleJump() { 
		return false;
	}
	
	
	/*
		move(Chessboard cBoard) - Overridden in every child class. Fills and returns an int array containing ID numbers of Tiles
		the calling piece may legally move to.
	*/
	public int[] move(Chessboard cBoard) { 
		return null;
	}		


	public int[] move(BitBoard B) {
		return null;
	}
}	