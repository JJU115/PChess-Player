/*
*	Chessboard.java
*	Date of creation: April 14, 2018
*	Date of last modification: Nov 2, 2018
*	
*	Author: Justin Underhay
*	
*/

/*
	Chessboard.java is the main class for the PlayChess program. 	
	Generates the chessboard for play by initializing an 8x8 grid of squares, each one being a Tile object, in the likeness of a proper chess board.
	Sets pieces on the board, manages tiles, and keeps track of pieces.
	
	Data members:
		board - JPanel 		- Main container for graphical components
		tiles - Tile[][] 	- 2D Array holding the Tile objects 	
		pieceList - int[] 	- Array where each index represents a piece ID and whose value is the Tile that piece currently occupies
		
*/


import javax.swing.JPanel;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.util.Arrays;


public class Chessboard {
	
	private JPanel board;
	private Tile[][] tiles;
	private int[] pieceList;
	
	 
	public Chessboard() {
		
		//Initialize graphical portion of the board 
		board = new JPanel();
		board.setPreferredSize(new Dimension(1000,1000));
		board.setLayout(new GridLayout(8,8));
		
		//Initialize data memebers
		tiles = new Tile[8][8];
		pieceList = new int[32];
		
		//Colors of the board tiles
		Color LIGHT = new Color(255,222,173);	
		Color DARK = new Color(160,82,45);
		
		//Set Tiles on the board and fill tiles array
		for (int i=0; i<8; i++) {
			for (int j=0; j<8; j++) {
				Tile next = new Tile(i*8+j);
				if ((i % 2 == 0 && j % 2 == 0) || (i % 2 != 0 && j % 2 != 0)) {
					next.setColor(DARK);	
					board.add(next.getPanel());
				} else {
					next.setColor(LIGHT);
					board.add(next.getPanel());
				}
				tiles[i][j] = next;	
			}
		}	
	}


	/*
		Additional constructor taking a Chessboard object and 2 integers as arguments.
		The 2 integers are: the ID of a Piece and a Tile ID.
		Copies the given Chessboard and changes it to reflect a move from the Piece to the Tile.
		This constructor's main purpose is for use in the GameTree class.
	*/
	public Chessboard(Chessboard C, int P, int M) {
		
		tiles = new Tile[8][8];
		
		for (int i=0; i<8; i++) 
			for (int j=0; j<8; j++)	
				tiles[i][j] = new Tile(C.fetchTile(i*8+j));
			
		pieceList = Arrays.copyOf(C.getList(), 32);	
		
		fetchTile(M).setPiece(fetchPiece(P));
		fetchTileOfPiece(P).setPiece(null);
		
		for (int k=0; k<32; k++) {
			if (pieceList[k] == M) {
				pieceList[k] = -1;
				break;
			}	
		}
		
		pieceList[P] = M;						
	}
	
	
	/*
		getList() - Returns the pieceList array.
	*/
	public int[] getList() {
		return pieceList;
	}	

	
	/*
		getBoard() - Returns the JPanel representing the board.
	*/
	public JPanel getBoard() {
		return board;
	}	
	
	
	/*
		setPieces() - Creates and sets the Piece objects on the board at the start of the program.
		Also fills the pieceList array with piece positions.
		Each piece has a color, type, and ID number
	*/
	public void setPieces() {
	
		//White pawns: 0-7
		//Black pawns: 16-23
		for (int i=0; i<8; i++) {
			tiles[1][i].setPiece(new Pawn('B','P',i+16));
			tiles[6][i].setPiece(new Pawn('W','P',i));
			
			pieceList[i] = 48 + i;
			pieceList[i+16] = 8 + i;
		}	
		
		/*
			White Rooks: 8-9
			Black Rooks: 24-25
			White Knights: 10-11
			Black Knights: 26-27
			White Bishops: 12-13
			Black Bishops: 28-29
		*/
		for (int j=0; j<2; j++) {
			tiles[0][j*7].setPiece(new Rook('B','R',j+24));
			tiles[7][j*7].setPiece(new Rook('W','R',j+8));
			
			pieceList[j+8] = 56+j*7;
			pieceList[j+24] = j*7;
		
			tiles[0][j*5+1].setPiece(new Knight('B','N',j+26));
			tiles[7][j*5+1].setPiece(new Knight('W','N',j+10));
			
			pieceList[j+10] = j*5+57;
			pieceList[j+26] = j*5+1;
	
			tiles[0][j*3+2].setPiece(new Bishop('B','B',28+j));
			tiles[7][j*3+2].setPiece(new Bishop('W','B',12+j));
			
			pieceList[12+j] = j*3+58;
			pieceList[28+j] = j*3+2;
		}
		
		
		//White Queen: 14
		//Black Queen: 30
		tiles[0][3].setPiece(new Queen('B','Q',30));
		tiles[7][3].setPiece(new Queen('W','Q',14));
		
		pieceList[14] = 59;
		pieceList[30] = 3;
		
		//White King: 15
		//Black King: 31
		tiles[0][4].setPiece(new King('B','K',31));
		tiles[7][4].setPiece(new King('W','K',15));
		
		pieceList[15] = 60;
		pieceList[31] = 4;
	}
	
	
	/*
		updatePieceList(int pID, int tID) - Given a piece ID and a Tile ID updates the pieceList array to reflect the current piece positions. 
	*/
	public void updatePieceList(int pID, int tID) {
		pieceList[pID] = tID;
	}	
	
	
	/*
		resetBoard() - Sets the activated property of every Tile object to false to clear player selections. 
	*/
	public void resetBoard() {
		for (int i=0; i<8; i++)
			for (int j=0; j<8; j++)
				tiles[i][j].setAct(false);
	}	
	
	
	/*
		fetchTile(int ID) - Given a Tile ID returns the Tile object associated with that ID.
	*/
	public Tile fetchTile(int ID) { 
		return tiles[(ID-(ID%8))/8][ID%8]; 
	}
	
	
	/*
		fetchTileOfPiece(int pieceID) - Given a Piece ID returns the Tile object that piece occupies. If the piece is no longer on the board
		returns null.
	*/
	public Tile fetchTileOfPiece(int pieceID) {
		int P = pieceList[pieceID];
		if (P == -1)
			return null;
		return tiles[(P-(P%8))/8][P%8];
	}	
	
	
	/*
		fetchPiece(int pieceID) - Given a Piece ID returns the piece object associated with it. Returns null if piece is no longer on the board.
	*/
	public Piece fetchPiece(int pieceID) {
		Tile T = fetchTileOfPiece(pieceID);	
		if (T != null)
			return T.getPiece();
		
		return null;
	}

}	