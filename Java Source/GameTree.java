/*
	GameTree.java
	
	Date of Creation: Oct 20, 2018
	Date of last modification: Dec 24, 2018
	
	Author: Justin Underhay
*/

/*
	GameTree.java represents the game tree generated and searched by the computer when the PlayChess file is executed.
	When PlayChess is executing and the user decides to play against the computer, the computer's moves are decided by
	examining the current state of the board and generating all possible moves for an arbitrary number of turns with each
	turn being one level in the tree. Each board state at the bottom of the tree is evaluated using Evaluate.java based
	on its favorability and the scores are propogated up the tree with the computer choosing its highest scoring child.
	
	The game tree can be generated and searched in two different ways, in a serial iterative manner or in a parallel manner. The parallel
	method attempts to improve over the serial one by splitting work among ForkJoin tasks and invoking them via a ForkJoinPool.
	The search algorithm is the alpha-beta pruning method. 

	Data members:
		head - GameState	 - The head of this GameTree.
		treeDepth - int		 - The depth of this GameTree.
		pieces - Piece[]	 - A Piece array used to generate moves and create new GameStates.
		last - int			 - Integer representing special conditions of pieces in the head's BitMap.
		evaluator - Evaluate - Class used to calculate static evaluation values of BitMaps.

*/


import java.lang.Double;
import java.lang.Math;
import java.util.ArrayList;
import java.util.List;
import java.util.Arrays;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveAction;


public class GameTree {
	
	//The ForkJoin thread pool used for parallel execution of game tree generation and searching.
	private static final ForkJoinPool mainPool = new ForkJoinPool();

	/*
		TreeGenerator is the ForkJoinTask subclass created and invoked to generate the game tree in parallel.
		A TreeGenerator task invoked with the init member set to true will generate the first level of the 
		tree serially, then split those nodes as equally as possible among n groups where n is the number
		of processors available to the JVM. n new tasks are then invoked with each one responsible for generating
		portions of the tree that extend the nodes given to it by using the generateGameTree method. 
	*/
	class TreeGenerator extends RecursiveAction {
		private boolean init;
		private int limit;
		private List<GameState> nodes;

		TreeGenerator(boolean i, List<GameState> N, int l) {
			this.init = i;
			this.nodes =  N;
			this.limit = l;
		}


		protected void compute() {
			if (init) {

				//Generate the first level of the tree
				BitMap start = head.getBoard();
				for (int i=16; i<32; i++) {
					if (start.getPiecePos(i) != -1) {
						for (int M : pieces[i].move(start)) {
							if (M != -1) {
								BitMap temp = new BitMap(start, i, M);
								if (!temp.examine(31, pieces)) { 	
									head.setChild(new GameState(temp, 1, i, M));
									treeDepth = 1;	
								}
							}
						}
					}
				}

				if (treeDepth < limit) {

					//Split head children into groups then invoke tasks
					int numProcessors = Runtime.getRuntime().availableProcessors();
					ArrayList<GameState> HC = head.getChildren();
					List<TreeGenerator> tasks = new ArrayList<TreeGenerator>();

					//Not evenly divisible, split as equally as possible
					if (HC.size() % numProcessors != 0) {

						if (HC.size() < numProcessors) {
							for (int i=0; i<HC.size(); i++)
								tasks.add(new TreeGenerator(false, HC.subList(i, i+1), limit));

						} else {

							int amt = (HC.size()-HC.size()%numProcessors)/numProcessors;

							for (int i=0; i<numProcessors; i++) 
								tasks.add(new TreeGenerator(false, HC.subList(i*amt, amt*(i+1)), limit));

							tasks.add(new TreeGenerator(false, HC.subList(amt*numProcessors, HC.size()), limit));	
						}

					//Evenly divisible	
					} else {
						
						int amt = HC.size()/numProcessors;

						for (int i=0; i<numProcessors; i++) 
							tasks.add(new TreeGenerator(false, HC.subList(i*amt, amt*(i+1)), limit));
					}	

					//Invoke the tasks
					invokeAll(tasks);
				}

			} else {

				for (GameState G : nodes)
					generateGameTree(G, 0, limit);		
			}
		}
	}


	/*
		TreeSearcher is the ForkJoinTask subclass created and invoked to search the game tree in parallel.
		A parallel alpha-beta pruning search is used, specifically the Young Brothers Wait Concept where at
		any unsearched node the leftmost subtree is searched completely first to establish a bound used
		in a parallel search of all remaining subtrees using one worker thread per subtree. 
	*/
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
			
			//Terminal node
			if (top.getNumChildren() == 0) {
				top.setEval(evaluator.evaluateState(top.getBoard()));
				return;
			
			//Search subtree rooted at top
			} else if (search) {

				if (top.getDepth()%2 == 0)
					alphaBetaSearch(top, true, alpha, beta);
				else
					alphaBetaSearch(top, false, alpha, beta);

			} else {

				//Search leftmost subtree
				mainPool.invoke(new TreeSearcher(top.getChild(0), alpha, beta, false));

				//Assign value bound
				if (top.getDepth()%2 == 0) {
					alpha = top.getChild(0).getEval();
					top.setEval(alpha);
				} else {
					beta = 	top.getChild(0).getEval();
					top.setEval(beta);
				}	


				//Instantiate tasks to search remaining subtrees
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
	private int last;
	private Evaluate evaluator;
	
	
	public GameTree() {

		treeDepth = 0;
		last = -1;
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


	/*
		getHead() - Returns the head member of this GameTree.
	*/
	public GameState getHead() {
		return head;
	}
	
	
	/*
		generateGameTree(GameState start, int turn, int limit) - Builds the game tree rooted at start
		where turn is the first color to move. The limit integer specifies the max depth to build the tree
		to. 
	*/
	public void generateGameTree(GameState start, int turn, int limit) {
		int[] M;
		BitMap curr = start.getBoard();
		
		for (int i=turn; i<turn+16; i++) {
			if (curr.getPiecePos(i) != -1) {
				M = pieces[i].move(curr);	

				for (int j=0; j<M.length; j++) {
					if (M[j] != -1) {

						BitMap temp = new BitMap(curr, i, M[j]);
						//When generating its own moves the CPU does not inlcude those that put itself into check
						if (turn != 16 || !temp.examine(31, pieces)) { 

							GameState child = new GameState(temp, start.getDepth()+1, i, M[j]); 
							start.setChild(child);
							treeDepth = Math.max(child.getDepth(), treeDepth);
							if (child.getDepth() < limit) 
								generateGameTree(child, (turn+16)%32, limit);		
						}
					}	
				}
			}
		}

		curr = null;
	}



	/*
		alphaBetaSearch(GameState node, boolean maxPlayer, double alpha, double beta) - Starting at a tree
		rooted at node conducts an alpha-beta search where at each level, maxPlayer determines whether or
		not the CPU's moves are being searched and the alpha/beta values represent bounds on currently 
		acquired values to avoid searching every node of the tree.
	*/
	public GameState alphaBetaSearch(GameState node, boolean maxPlayer, double alpha, double beta) {
		//Terminal node, get static evaluation
		if (node.getNumChildren() == 0) {
			node.setEval(evaluator.evaluateState(node.getBoard()));
			return node;
		}	

		//node must acquire the highest valued child
		if (maxPlayer) {
			node.setEval(Double.NEGATIVE_INFINITY);

			for (GameState S : node.getChildren()) {
				node.setEval(Math.max(node.getEval(), (alphaBetaSearch(S, false, alpha, beta)).getEval()));
				alpha = Math.max(alpha, node.getEval());
				if (alpha >= beta)
					break;
			}

		//node must acquire lowest valued child
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


	/*
		getNextMove(Chessboard C) - Initiates procedures to generate a tree rooted at a GameState with
		the given Chessboard C as its BitMap then searches the tree to find the best next move. The int
		array returned has the ID of the Piece to move in index 0 and the ID of the Tile to move it to
		in index 1. 
	*/
	public int[] getNextMove(Chessboard C) {

		//Look for any promoted Pawns
		for (int i=0; i<8; i++) 
			if (C.fetchPiece(i) != null && C.fetchTileOfPiece(i).getID() < 8)
				pieces[i] = C.fetchPiece(i);
				
		//Update special piece conditions and assign head of tree
		BitMap B = new BitMap(C, last);
		B.updateBoardScan();
		head = new GameState(new BitMap(C, last), 0, -1, -1);	
		
		//Clean up any leftover nodes from prior generations
		System.gc();
		
		/*
		Generate the tree. Can be done serially with the generateGameTree method or in parallel by
		invoking a TreeGenerator task. Only one should be executed.
		*/
		//generateGameTree(head, 16, 4);
		mainPool.invoke(new TreeGenerator(true, null, 4));
		
		/*
		Search the tree. Can be done serially with the alphaBetaSearch method or in parallel by
		invoking a TreeSearcher task. Only one should be executed.
		*/
		//alphaBetaSearch(head, true, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY);
		mainPool.invoke(new TreeSearcher(head, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY, false));
		
		//Acquire the highest valued child of the root
		GameState max = head.getChild(0);

		for (GameState G : head.getChildren()) 
			if (G.getEval() > max.getEval()) 
				max = G;
		
		last = max.getBoard().getSpecs();
		return max.getMove();				
	}
	
}