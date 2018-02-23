package com.jerry.aiproject.ai;

import java.util.ArrayList;
import java.util.List;

/**
 * This class is going to contain
 * the list of nodes found by the
 * AI algorithms. Implementation
 * broken down by Kevin Glass owner
 * of Cokandcode.com.
 * @author Jerry
 */
public class Path {

	private List<Node> nodes = new ArrayList<>();

	/**
	 * This method gets the length
	 * of the path in nodes.
	 * @return the path length.
	 */
	public int getLength() {
		return nodes.size();
	}

	/**
	 * This method returns the row
	 * of a specified node.
	 * @return the tile's row.
	 */
	public int getRow(int index) {
		return nodes.get(index).getRow();
	}

	/**
	 * This method returns the column
	 * of a specified node.
	 * @return the tile's column.
	 */
	public int getCol(int index) {
		return nodes.get(index).getCol();
	}

	/**
	 * This method will append
	 * a new move to the list.
	 * @param node the node to append.
	 */
	public void appendStep(Node node) {
		nodes.add(node);
	}

	/**
	 * This method will prepend
	 * a new move to the list.
	 * @param node the node to prepend.
	 */
	public void prependNode(Node node) {
		nodes.add(0, node);
	}

    /**
     * This method returns a node at the specified index.
     * @param dex the index of the node.
     * @return the node at the index.
     */
	public Node getStep(int dex) {
		return nodes.get(dex);
	}

	/**
	 * This method will get the next step from
	 * the current step, helps read the next
	 * moves in the path for AI Movement.
	 * @param currentStep the current step.
	 */
	public Node getNextStep(int currentStep) {
		Node node; //Node to return.

		try {
			//Try getting the next node.
			node = nodes.get(currentStep + 1);
		} catch (IndexOutOfBoundsException e) {
			//If there is not a next node, this is the final node, so return final node.
			node = nodes.get(currentStep);
		}
		return node;
	}

	/**
	 * This method returns true if the move
	 * is on the list, if not, it returns
	 * false.
     * @param node the node to check.
	 * @return if the move is in the list.
	 */
	public boolean contains(Node node) {
		return nodes.contains(node);
	}

    /**
     * This method will print out a listing
     * Of the path's nodes.
     */
	public void printPath() {
		for(int count = 0; count < nodes.size(); count++)
			System.out.println("Row: " + nodes.get(count).getRow() + " Col: "  + nodes.get(count).getCol());
	}
}