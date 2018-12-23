/*
	GameTree.java
	
	Date of Creation: Oct 20, 2018
	Date of last modification: Dec 7, 2018
	
	Author: Justin Underhay
*/

/*
	GameTree.java represents the game tree generated and searched by the computer when the PlayChess file is executed.
	When PlayChess is executing and the user decides to play against the computer, the computer's moves are decided by
	examining the current state of the board and generating all possible moves for an arbitrary number of turns with each
	turn being one level in the tree. Each board state at the bottom of the tree is evaluated using Evaluate.java based
	on its favourability and the scores are propogated up the tree with the computer choosing the highest scoring one. 

*/


import java.lang.Double;
import java.lang.Math;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveAction;


public class GameTree {
	
	private static final ForkJoinPool mainPool = new ForkJoinPool();

	class TreeGenerator extends RecursiveAction {
		private boolean init;
		private Piece[] targets;
		private int limit;

		TreeGenerator(boolean i, Piece[] P, int l) {
			this.init = i;
			this.targets = P;
			this.limit = l;
		}


		protected void compute() {
			if (init) {
				invokeAll(new TreeGenerator(false, Arrays.copyOfRange(targets, 0, 4), limit),
						  new TreeGenerator(false, Arrays.copyOfRange(targets, 4, 8), limit),	
						  new TreeGenerator(false, Arrays.copyOfRange(targets, 8, 12), limit),
						  new TreeGenerator(false, Arrays.copyOfRange(targets, 12, 16), limit));
			} else {
				BitBoard start = head.getBoard();
				for (Piece P1 : targets) {
					if (start.getPiecePos(P1.getID()) != -1) {
						for (int M : P1.move(start)) {
							if (M != -1) {
								BitBoard temp = new BitBoard(start, P1.getID(), M);
								if (!temp.examine(31, pieces)) { 	
									GameState child = new GameState(temp, 1, P1.getID(), M); 
									head.setChild(child);
									treeDepth = child.getDepth();
									if (child.getDepth() < limit) 
										generateGameTree(child, 0, limit);		
								}
							}
						}
					}
				}
			}
		}

	}



	class TreeSearcher extends RecursiveAction {
		private GameState top;
		private double alpha, beta;
		private boolean search;
		

		public TreeSearcher(GameState G, double A, double B, boolean S) {
			this.top = G;
			this.alpha = A;
			this.beta = B;
			this.search = S;
		}

		protected void compute() {
			
			if (top.getDepth() == treeDepth) {
				top.setEval(evaluator.evaluateState(top.getBoard()));
				return;
			
			} else if (search) {

				if (top.getDepth()%2 == 0)
					alphaBetaSearch(top, true, alpha, beta);
				else
					alphaBetaSearch(top, false, alpha, beta);

			} else {

				mainPool.invoke(new TreeSearcher(top.getChild(0), alpha, beta, false));

				if (top.getDepth()%2 == 0)
					alpha = top.getChild(0).getEval();
				else
					beta = 	top.getChild(0).getEval();

				ArrayList<TreeSearcher> TS = new ArrayList<TreeSearcher>(top.getNumChildren()-1);
				for (int i=1; i<top.getNumChildren(); i++) 
					TS.add(new TreeSearcher(top.getChild(i), alpha, beta, true));
				
				invokeAll(TS);
				
				if (top.getDepth()%2 == 0) {
					for (GameState G : top.getChildren())
						if (top.getEval() < G.getEval())
							top.setEval(G.getEval());
				} else {
					for (GameState G : top.getChildren())
						if (top.getEval() > G.getEval())
							top.setEval(G.getEval());
				}	
			}
		}

	}

	

	private GameState head;
	private int treeDepth;
	private Piece[] pieces;
	private boolean[] last;
	private Evaluate evaluator;
	
	
	public GameTree() {
		treeDepth = 0;

		pieces = new Piece[32];

		for (int i=0; i<8; i++) {
			pieces[i] = new Pawn('W','P',i);
			pieces[i+16] = new Pawn('B','P',i+16);
		}

		for (int j=8; j<10; j++) {
			pieces[j] = new Rook('W','R',j);
			pieces[j+16] = new Rook('B','R',j+16);

			pieces[j+2] = new Knight('W','N',j+2);
			pieces[j+18] = new Knight('B','N',j+18);

			pieces[j+4] = new Bishop('W','B',j+4);
			pieces[j+20] = new Bishop('B','B',j+20);
		}

		pieces[14] = new Queen('W','Q',14);
		pieces[30] = new Queen('B','Q',30);

		pieces[15] = new King('W','K',15);
		pieces[31] = new King('B','K',31);

		evaluator = new Evaluate(pieces);
	}	



	public GameState getHead() {
		return head;
	}
	
	
	//Non-parallel generation
	public void generateGameTree(GameState start, int turn, int limit) {
		int[] M;
		BitBoard curr = start.getBoard();
	//	System.out.println(Arrays.toString(curr.getSpecs()));
		
		for (int i=turn; i<turn+16; i++) {
			if (curr.getPiecePos(i) != -1) {
				M = pieces[i].move(curr);	
				for (int j=0; j<M.length; j++) {
					if (M[j] != -1) {
						BitBoard temp = new BitBoard(curr, i, M[j]);
						if (turn != 16 || !temp.examine(31, pieces)) { 	//CPU does not consider moves which put it into check
							GameState child = new GameState(temp, start.getDepth()+1, i, M[j]); 
							start.setChild(child);
							treeDepth = child.getDepth();
							if (child.getDepth() < limit) 
								generateGameTree(child, (turn+16)%32, limit);		
						}
					}	
				}
			}
		}

		curr = null;
	}



	//Non-parallel search
	public GameState alphaBetaSearch(GameState node, boolean maxPlayer, double alpha, double beta) {
		if (node.getDepth() == treeDepth) {
			node.setEval(evaluator.evaluateState(node.getBoard()));
			return node;
		}	

		if (maxPlayer) {
			node.setEval(Double.NEGATIVE_INFINITY);

			for (GameState S : node.getChildren()) {
				node.setEval(Math.max(node.getEval(), (alphaBetaSearch(S, false, alpha, beta)).getEval()));
				alpha = Math.max(alpha, node.getEval());
				if (alpha >= beta)
					break;
			}

		} else {
			node.setEval(Double.POSITIVE_INFINITY);

			for (GameState S : node.getChildren()) {
				node.setEval(Math.min(node.getEval(),(alphaBetaSearch(S, true, alpha, beta)).getEval()));
				beta = Math.min(beta, node.getEval());
				if (alpha >= beta)
					break;
			}
		}	

		return node;
	}



	public int[] getNextMove(Chessboard C) {

		//Need to put in update to Bitboard special array here for white pieces
		if (last == null) {
			BitBoard B = new BitBoard(C, null);
			B.updateBoardScan();
			head = new GameState(new BitBoard(C,null), 0, -1, -1);	
		} else {
			BitBoard B = new BitBoard(C, last);
			B.updateBoardScan();
			head = new GameState(new BitBoard(C, last), 0, -1, -1);	
		}	

		System.gc();


		generateGameTree(head, 16, 1);
	//	mainPool.invoke(new TreeGenerator(true, Arrays.copyOfRange(pieces, 16, 32), 3));

		alphaBetaSearch(head, true, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY);
	//	mainPool.invoke(new TreeSearcher(head, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY, false));
		
		GameState max = head.getChild(0);

		for (GameState G : head.getChildren()) 
			if (G.getEval() > max.getEval()) 
				max = G;

		last = max.getBoard().getSpecs();
		return max.getMove();				
	}


	public static void main(String[] args) {
		Chessboard C = new Chessboard();
		C.setPieces();
		BitBoard B = new BitBoard(C, null);
		GameTree tree = new GameTree();
		tree.getNextMove(C);
				
	}

}
