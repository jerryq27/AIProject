package com.jerry.aiproject.utils;

import java.util.ArrayList;

import com.jerry.aiproject.aialgorithms.Node;

/**
 * This class is going to contain
 * the list of steps found by the 
 * AI algorithms. Implementation
 * broken down by Kevin Glass owner
 * of Cokandcode.com.  
 * @author Jerry
 */
public class Path {

	private ArrayList<Node> steps = new ArrayList<Node>();
	
	/**
	 * This method gets the length
	 * of the path in tiles. 
	 * @return the path length.
	 */
	public int getLength() {
		return steps.size();
	}
	
	/**
	 * This method returns the row
	 * of a specified tile. 
	 * @return the tile's row. 
	 */
	public int getRow(int index) {
		return steps.get(index).getRow();
	}
	
	/**
	 * This method returns the column
	 * of a specified tile. 
	 * @return the tile's column. 
	 */
	public int getCol(int index) {
		return steps.get(index).getCol();
	}
	
	/**
	 * This method will move the player 
	 * to the specified tile , or 'append'
	 * a new move to the list. 
	 * @param row the tile's row.  
	 * @param col the tile's column.
	 */
	public void addStep(Node step) {
		steps.add(step);
	}
	
	/**
	 * Adds a move to the beginning of
	 * the list, or 'prepends' the move.
	 * @param row the tile's row. 
	 * @param col the tile's column. 
	 */
	public void newStartStep(Node step) {
		steps.add(0, step);
	}
	
	public Node getStep(int dex) {
		return new Node(steps.get(dex).getRow(), steps.get(dex).getCol());		
	}
	
	/**
	 * This method will get the next step from
	 * the current step, helps read the next
	 * moves in the path for AI Movement.
	 * @param currentStep the current step. 
	 */
	public Node getNextStep(int currentStep) {
		Node step; //Step to return. 
		
		try {
			//Try getting the next step. 
			step = steps.get(currentStep + 1);
		} catch (IndexOutOfBoundsException e) {
			//If there is not a next step, this is the final step, so return final step. 
			step = steps.get(currentStep);
		}
		return step;
	}
	
	/**
	 * This method returns true if the move
	 * is on the list, if not, it returns
	 * false.
	 * @param row the move's row. 
	 * @param col the move's column. 
	 * @return if the move is in the list. 
	 */
	public boolean contains(Node step) {
		return steps.contains(step);
	}
	
	public void printPath() {
		for(int count = 0; count < steps.size(); count++)
			System.out.println("Row: " + steps.get(count).getRow() + " Col: "  + steps.get(count).getCol());
	}
}