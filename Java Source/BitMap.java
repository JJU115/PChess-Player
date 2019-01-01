/*
	BitMap.java
	
	Date of Creation: Dec 18, 2018
	Date of last modification: Dec 24, 2018
	
	Author: Justin Underhay
*/

/*
	BitMap.java - A compressed representation of a Chessboard object with simpler functionality for use in
	constructing GameState objects that form the GameTree object.

	Data members:
		map - byte[] 	- Array where each index represents a piece ID holding the ID number of the Tile the piece resides on. A -1
							means the piece is captured.
							
		specials - int	- 32 bit number where the ith digit counting right to left is 1 if the piece with ID i has moved, 0 otherwise.

*/

import java.lang.Math;


public class BitMap {

	private byte[] map;
	private int specials;


	//Construct a BitMap representation of the given Chessboard.
	public BitMap(Chessboard C, int specs) {

		map = new byte[32];
		int[] CP = C.getList();

		for (int i = 0; i<32; i++)
			map[i] = (byte) CP[i];

		if (specs == -1)
			specials = 0x00000000;	//32 bit hexadecimal
		else
			specials = specs;		
	}


	//Construct a BitMap identical to the given one but with Piece of ID P moved to space of ID T.
	public BitMap(BitMap B, int P, int T) {

		map = new byte[32];
		byte[] old = B.getMap();

		for (int i = 0; i<32; i++)
			map[i] = old[i];

		specials = B.getSpecs();	
			
		//See if capture made
		if (getPosition(T) != -1)
			map[getPosition(T)] = -1;	

		map[P] = (byte) T;

		//Update specs
		specials |= (int) Math.pow(2, P);
	}


	/*
		getMap() - Returns the map byte array.
	*/
	public byte[] getMap() {
		return map;
	}


	/*
		getSpecs() - Returns the specials int.
	*/
	public int getSpecs() {
		return specials;
	}


	/*
		getPosition(int T) - Given a Tile ID T returns the contents of that space on the represented board.
		If a piece occupies the space its ID is returned otherwise a -1 signifying an empty space is returned.
	*/
	public int getPosition(int T) {

		for (int i = 0; i < 32; i++)
			if (map[i] == T)
				return i;

		return -1;		
	}


	/*
		getPiecePos(int P) - Given a Piece ID P returns the Tile ID of the space that piece occupies.
		If the Piece has been captured a -1 is returned.
	*/
	public int getPiecePos(int P) {
		return map[P];
	}


	/*
		getPStatus(int P) - Given a Piece ID P returns whether or not the Piece has moved at all.
		This method's primary use is for checking King/Rook Castling requirements and Pawn double jump requirements.
	*/
	public boolean getPStatus(int P) {
		if ((specials & (int) Math.pow(2, P)) != 0)
			return true;
		else
			return false; 
	}


	/*
		updateBoardScan() - Looks over the board for changes in white pieces that warrant change to specials int.
		In particular the white Pawns, King, and Rooks. 
	*/
    public void updateBoardScan() {
        //White pawns
        for (int i=0; i<8; i++)
            if (getPiecePos(i) != 48 + i)
                specials |= (int) Math.pow(2, i);
        

        //White rooks
        if (getPiecePos(8) != 56)
            specials |= (int) Math.pow(2, 8);

        if (getPiecePos(9) != 63)
            specials |= (int) Math.pow(2, 9);   

        //White king
        if (getPiecePos(15) != 60)
            specials |= (int) Math.pow(2, 15);
	}
	

	/*
		examine(int king, Piece[] P) - Examines this BitMap to see if the King with ID king is in Check.
		A Piece array P is provided to generate moves of opposing pieces.
	*/
	public boolean examine(int king, Piece[] P) {
		
        int k = (king == 31) ? 0 : 16;
         for (int i = k; i<k+16; i++) {
            if (this.getPiecePos(P[i].getID()) != -1) {
				for (int M : P[i].move(this))
					if (M != -1 && this.getPosition(M) == king)
                        return true;
            }    
        }
        return false;
	}

}