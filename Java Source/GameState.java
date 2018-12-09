/*
	GameState.java

	
	Date of Creation: Oct 20, 2018
	Date of last modification: Dec 4, 2018
	
	Author: Justin Underhay
*/


import java.util.ArrayList;
import java.lang.Math;


public class GameState {
	
	
	private double evalValue;
	private BitBoard layout;
	private int depth;
	private int[] move;
	private ArrayList<GameState> children;
	private int numChildren;
	
	
	
	public GameState(BitBoard B, int D, int P, int M) {
		
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
	
	
	public void setEval(double E) {
		evalValue = E;
	}


	public double getEval() {
		return evalValue;
	}


	public BitBoard getBoard() {
		return layout;
	}


	public void discard() {
		layout = null;
	}	


	public int getDepth() {
		return depth;
	}


	public int[] getMove() {
		return move;
	}	
	
	
	public ArrayList<GameState> getChildren() {
		return children;
	}	


	public GameState getChild(int index) {
		return children.get(index);
	}


	public int getNumChildren() {
		return numChildren;
	}

	
	public void setChild(GameState child) {
		children.add(child);
		numChildren++;
	}

}