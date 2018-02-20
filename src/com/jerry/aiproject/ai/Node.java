package com.jerry.aiproject.ai;

/**
 * This class is to keep track of each tile's
 * information, it is also only to make the 
 * algorithm easier to implement with pseudocode.
 * Each node represents a tile. Implements 
 * Comparable to sort the list for the algorithms
 * that need a sorted open list. 
 * @author Jerry
 */
public class Node implements Comparable<Object> {

	protected int row, col; //Location of the node. 
	protected float cost; //The path cost for this node.
	protected Node parent; //The parent of the current node.
	protected float heuristic; //The heuristic cost for this node.
	protected int depth; //How far the node is in the tree. 

	public Node(int r, int c) {
		row = r;
		col = c;
	}

	/**
	 * This method sets the parent of the
	 * current node and returns the current 
	 * depth of the tree. 
	 * @param parentNode the parent node. 
	 * @return the depth of the tree. 
	 */
	public int setParent(Node parentNode) {
		depth = parentNode.depth + 1;
		parent = parentNode;

		return depth;
	}

	/**
	 * Mostly for the A* Search algorithm, to 
	 * sort the nodes in the open list based 
	 * on the heuristic cost of the node. 
	 */
	@Override
	public int compareTo(Object n) {
		Node passedNode = (Node)n;

		float thisCost = heuristic + cost;
		float passedCost = passedNode.heuristic + passedNode.cost;

		if(thisCost > passedCost)
			return 1;
		else if(thisCost < passedCost)
			return -1;
		else
			return 0;
	}
	
	public int getRow() {
		return row;
	}
	public int getCol() {
		return col;
	}
}