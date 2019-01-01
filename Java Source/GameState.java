/*
	GameState.java

	
	Date of Creation: Oct 20, 2018
	Date of last modification: Dec 19, 2018
	
	Author: Justin Underhay
*/

/*
	GameState.java is the class from which GameState objects are instantiated. GameState objects form the "nodes"
	of the non-binary game/decision tree used by the playing program to formulate its movement sequence. Each GameState
	holds a BitMap representation of a potential board position, children of GameStates are GameStates with BitMaps
	that can be reached from the parent BitMap in a single move.

	Data members:
		evalValue - double		- The static evaluation value representing this GameState's BitMap favorability to the computer.
		layout - BitMap			- The compressed representation of a board held by this GameState.
		depth - int				- This GameStates depth in the GameTree. The head is depth 0, any other GameState has depth equal to
									its parent plus one.
		move - int[]			- Two element int array holding the move made from the parent GameState's BitMap to this GameState's BitMap.

		children - ArrayList<GameState>	- An ArrayList of all GameStates that are children of this one.
		numChildren - int		- The number of children this GameState has.
*/


import java.util.ArrayList;


public class GameState {
	
	
	private double evalValue;
	private BitMap layout;
	private int depth;
	private int[] move;
	private ArrayList<GameState> children;
	private int numChildren;
	
	
	
	public GameState(BitMap B, int D, int P, int M) {
		
		layout = B;
		depth = D;

		if (P != -1 && M != -1) {
			move = new int[2];
			move[0] = P;
			move[1] = M;
		}	
		
		children = new ArrayList<GameState>(25);
		numChildren = 0;
		
	}
	
	
	/*
		setEval(double E) - Sets this GameStates evalValue data member to the given one E.
	*/
	public void setEval(double E) {
		evalValue = E;
	}


	/*
		getEval() - Returns the evalValue data member.
	*/
	public double getEval() {
		return evalValue;
	}


	/*
		getBoard() - Returns the layout data member.
	*/
	public BitMap getBoard() {
		return layout;
	}


	/*
		getDepth() - Returns the depth data member.
	*/
	public int getDepth() {
		return depth;
	}


	/*
		getMove() - Returns the move data member.
	*/
	public int[] getMove() {
		return move;
	}	
	
	
	/*
		getChildren() - Returns the children data member.
	*/
	public ArrayList<GameState> getChildren() {
		return children;
	}	


	/*
		getChild(index) - Returns the GameState in position index of this GameState's children ArrayList.
	*/
	public GameState getChild(int index) {
		return children.get(index);
	}


	/*
		getNumChildren() - Returns the numChildren data member.
	*/
	public int getNumChildren() {
		return numChildren;	
	}

	
	/*
		setChild(GameState child) - Adds the given GameState child to this GameState's children ArrayList.
	*/
	public void setChild(GameState child) {
		children.add(child);
		numChildren++;
	}

}