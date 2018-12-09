/*
	BitBoard.java

	
	Date of Creation: Nov 3, 2018
	Date of last modification: Nov 19, 2018
	
	Author: Justin Underhay
*/

import java.util.Arrays;

public class BitBoard {

    private int[][] board;  //2D array rep of Chessboard   
    private boolean[] special;  //Tracks Pawns double jumps, Rooks and Kings firstMoves


    public BitBoard(Chessboard C, boolean[] specs) {

        board = new int[8][8];
        int[] list = C.getList();

        for (int j=0; j<64; j++)
            board[j/8][j%8] = -1;

        for (int i=0; i<32; i++) 
            if (list[i] != -1)
                board[list[i]/8][list[i]%8] = i;  

        if (specs == null)        
            special = new boolean[22];   
        else
            special = specs;         
                 
    }


    //Copy constructor - Doesn't handle En-Passant yet!
    public BitBoard(BitBoard B2, int P, int T) {
        special = Arrays.copyOf(B2.getSpecs(), 22);
        board = new int[8][8];

        for (int i=0; i<64; i++)
            board[i/8][i%8] = B2.getPosition(i);

        int pS = B2.getPiecePos(P);
        board[pS/8][pS%8] = -1;
        board[T/8][T%8] = P; 
        
        //Check if Pawn special status changed - double jump
        if (P < 8)
            special[P] = true;
        if (P > 15 && P < 24)
             special[P-5] = true;    
        

        switch (P) {
            case 8: case 9:
                special[P] = true;
                break;
            case 15: case 24: case 25:
                special[P-5] = true;
                break;
            case 31:
                special[21] = true;
                break;        
        }

        //Castling
        if (P == 15 && !special[10]) {
            if (T == 62) {
                board[7][5] = 9;
                special[9] = true;
            } else if (T == 57) {
                board[7][2] = 8;
                special[8] = true;
            }    

        } else if (P == 31 && !special[21]) {
            if (T == 6) {
                board[0][5] = 25;
                special[20] = true;
            } else if (T == 57) {
                board[0][2] = 24;
                special[19] = true;
            }    
        }          
    }


    public boolean[] getSpecs() {
        return special;
    }


    public int getPosition(int ID) {
        return board[ID/8][ID%8];
    }


    public int getPiecePos(int pID) {
        for (int i=0; i<64; i++)
            if (board[i/8][i%8] == pID)
                return i;

        return -1;        
    }


    public boolean getPStatus(int pID) {
        if (pID < 10)
            return special[pID];
        else if (pID < 26)
            return special[pID-5];
        else 
            return special[21];       

    }

    //Examine this BitBoard to see if king with given param ID in check
    public boolean examine(int king, Piece[] P) {
        int[] M;
        int k = (king == 31) ? 0 : 16;
         for (int i = k; i<k+16; i++) {
            if (this.getPiecePos(P[i].getID()) != -1) {
                M = P[i].move(this);
                for (int j=0; j<M.length; j++)
                    if (M[j] != -1 && this.getPosition(M[j]) == king)
                        return true;
            }    
        }
        return false;
    }


    public void printout() {
        for (int i=0; i<8; i++) { 
            for (int j=0; j<8; j++)
                System.out.print(board[i][j] + " ");
            System.out.println();
        }    
    }


    //Main method for testing only
    public static void main(String[] args) {
        Chessboard C = new Chessboard();
        C.setPieces();
        BitBoard B = new BitBoard(C,null);
        BitBoard B2 = new BitBoard(B,26,28);
        BitBoard B3 = new BitBoard(B2,13,36);

        

        System.out.println(Arrays.toString(C.fetchPiece(26).move(B2)));
        for (int i=0; i<8; i++) {
            for (int j=0; j<8; j++)
                System.out.print(B2.getPosition(i*8+j) + " ");
            System.out.println();
        }    
        System.out.println("\n\n");    
        for (int i=0; i<8; i++) {
            for (int j=0; j<8; j++)
                System.out.print(B3.getPosition(i*8+j) + " ");
            System.out.println();
        }
    }
    
}