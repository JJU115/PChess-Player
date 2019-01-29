/*
	Evaluate.java

	Date of Creation: Dec 3, 2018
	Date of last modification: Dec 19, 2018
	
	Author: Justin Underhay
*/

/*
	Evaluate.java is the class responsible for calculating static evaluation values for board positions.
	The single instance method, evaluateState, calculates the value for a given BitMap based on a variety
	of factors such as captured pieces, piece positions, and potential to make captures.

	A number of changes are to be made, including an opening move database and improvements to the
	evaluation function to create a stronger playing program.

	Data members:
		P - Piece[] 	- An array of Piece objects to generate moves for given BitMaps.
*/


public class Evaluate {

	//The following static values are the Piece values and positioning tables used to calculate board values 

	private static final double[] P_VALS = {2.5, 2.5, 2.5, 2.5, 2.5, 2.5, 2.5, 2.5, 12.7, 12.7, 8.1, 8.1, 8.3, 8.3, 19.4, 500};

	private static final double[][] PAWN_VALS =  {	{0, 0, 0, 0, 0, 0, 0, 0},
							{5, 10, 10, -20, -20, 10, 10, 5},
							{5, -5, -10, 0, 0, -10, -5, 5},
							{0, 0, 0, 20, 20, 0, 0, 0},
							{5, 5, 10, 25, 25, 10, 5, 5},
							{10, 10, 20, 30, 30, 20, 10, 10},
							{50, 50, 50, 50, 50, 50, 50, 50},
							{0, 0, 0, 0, 0, 0, 0, 0}};
	
	private static final double[][] KNIGHT_VALS = { {-50,-40,-30,-30,-30,-30,-40,-50},
							{-40,-20,  0,  5,  5,  0,-20,-40},
							{-30,  5, 10, 15, 15, 10,  5,-30},
							{-30,  0, 15, 20, 20, 15,  0,-30},
							{-30,  5, 15, 20, 20, 15,  5,-30},
							{-30,  0, 10, 15, 15, 10,  0,-30},
							{-40,-20,  0,  0,  0,  0,-20,-40},
							{-50,-40,-30,-30,-30,-30,-40,-50}};

	private static final double[][] BISHOP_VALS = { {-20,-10,-10,-10,-10,-10,-10,-20},
							{-10,  5,  0,  0,  0,  0,  5,-10},
							{-10,  10,  10, 10, 10,  10,  10,-10},
							{-10,  0,  10, 10, 10,  10,  0,-10},
							{-10,  5, 5, 10, 10, 5,  5,-10},
							{-10, 0, 5, 10, 10, 5, 0,-10},
							{-10,  0,  0,  0,  0,  0,  0,-10},
							{-20,-10,-10,-10,-10,-10,-10,-20}};

	private static final double[][] ROOK_VALS = { {0,  0,  0,  5,  5,  0,  0,  0},
						      {-5, 0,  0,  0,  0,  0,  0, - 5},
						      {-5,  0,  0,  0,  0,  0,  0, -5},
						      {-5,  0,  0,  0,  0,  0,  0, -5},
						      {-5,  0,  0,  0,  0,  0,  0, -5},
						      {-5,  0,  0,  0,  0,  0,  0, -5},
						      {5,  10,  10,  10,  10,  10,  10, 5},
						      {0,  0,  0,  0,  0,  0,  0,  0}};

	private static final double[][] QUEEN_VALS = {  {-20,-10,-10, -5, -5,-10,-10,-20},
							{-10,  0,  5,  0,  0,  0,  0,-10},
							{-10,  5,  5,  5,  5,  5,  0,-10},
							{0,  0,  5,  5,  5,  5,  0, -5},
							{-5,  0,  5,  5,  5,  5,  0, -5},
							{-10,  0,  5,  5,  5,  5,  0,-10},
							{-10,  0,  0,  0,  0,  0,  0,-10},
							{-20,-10,-10, -5, -5,-10,-10,-20}};

	private static final double[][] KING_VALS = {   {20, 50, 10,  0,  0, 10, 50, 20},
							{20, 20,  0,  0,  0,  0, 20, 20},
							{-10,-20,-20,-20,-20,-20,-20,-10},
							{-20,-30,-30,-40,-40,-30,-30,-20},
							{-30,-40,-40,-50,-50,-40,-40,-30},
							{-30,-40,-40,-50,-50,-40,-40,-30},
							{-30,-40,-40,-50,-50,-40,-40,-30},
							{-30,-40,-40,-50,-50,-40,-40,-30}};



    private Piece[] P;

    public Evaluate(Piece[] P) {
        this.P = new Piece[32]; 
        System.arraycopy(P, 0, this.P, 0, 32);
    }


	/*
		evaluateState(BitMap layout) - With the given BitMap layout calculate a static evaluation value
		based on a variety of factors. The returned value is a decimal, positive or negative, assigned to
		a GameState and placed within the GameTree. Higher values represent more advantageous layouts for
		the computer, lower values represent a better layout for the human player. 
	*/
    public double evaluateState(BitMap layout) {

        double eval = 0.0;
		int[] BpiecePositions = new int[16];
		int[] WpiecePositions = new int[16];
		
		//Count up all pieces still in play
		for (int B=0; B<16; B++) {
			BpiecePositions[B] = layout.getPiecePos(B+16);
			WpiecePositions[B] = layout.getPiecePos(B);

			if (BpiecePositions[B] != -1)
				eval += P_VALS[B];

			if (WpiecePositions[B] != -1)
				eval -= P_VALS[B];	
		}


		int[] BMoves;
		int[] WMoves;

		//Look for potential captures
		for (int B=0; B<16; B++) {

			if (layout.getPiecePos(B+16) != -1) {
				BMoves = P[B+16].move(layout);

				for (int BM : BMoves) {
					if (BM != -1 && layout.getPosition(BM) > 15)
						System.out.println(B);

					if (BM != -1 && layout.getPosition(BM) != -1)
						eval += P_VALS[layout.getPosition(BM)];
				}	
			}		
			
			if (layout.getPiecePos(B) != -1) {
				WMoves = P[B].move(layout);
				for (int WM : WMoves)
					if (WM != -1 && layout.getPosition(WM) != -1)
						eval -= P_VALS[layout.getPosition(WM)%16];	
			}					
		}


		//Consult the positioning tables

		//Pawns
		for (int B=0; B<8; B++)
			if (BpiecePositions[B] != -1)
				eval += PAWN_VALS[BpiecePositions[B]/8][BpiecePositions[B]%8]/10;
			
		//Rooks/Knights/Bishops
		for (int B=8; B<10; B++) {
			if (BpiecePositions[B] != -1)
				eval += ROOK_VALS[BpiecePositions[B]/8][BpiecePositions[B]%8]/10;
				
			if (BpiecePositions[B+2] != -1)	
				eval +=	KNIGHT_VALS[BpiecePositions[B+2]/8][BpiecePositions[B+2]%8]/10;

			if (BpiecePositions[B+4] != -1)	
				eval +=	BISHOP_VALS[BpiecePositions[B+4]/8][BpiecePositions[B+4]%8]/10;
		}					

		//Queen and King
		if (BpiecePositions[14] != -1)
			eval += QUEEN_VALS[BpiecePositions[14]/8][BpiecePositions[14]%8]/10;

		eval +=	KING_VALS[BpiecePositions[15]/8][BpiecePositions[15]%8]/10;	
		

		//Piece mobility

		//Pawn structure

		//King safety

		//Bonuses

		//Penalties

        return eval;
    }  
}
