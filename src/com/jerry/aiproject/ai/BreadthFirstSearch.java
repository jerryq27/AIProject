package com.jerry.aiproject.ai;

import com.jerry.aiproject.data.TileMap;
import com.jerry.aiproject.gameobjects.GameObject;

import java.util.ArrayList;
import java.util.List;

/**
 * This class uses the Breadth First Search
 * Algorithm to find a path between two
 * Game Objects. This algorithm works by
 * Searching each node and their neighbors
 * Until the object is found.
 * @author Jerry
 */
public class BreadthFirstSearch {

    private List<Node> visitedList = new ArrayList<>();
    private List<Node> notVisitedList = new ArrayList<>();
	private TileMap tileMap;
	private Node[][] nodes;

	public BreadthFirstSearch(TileMap map) {
		tileMap = map;

		// Create the grid map.
		nodes = new Node[tileMap.getRows()][tileMap.getCols()];
		for(int row = 0; row < tileMap.getRows(); row++)
		{
			for(int col = 0; col < tileMap.getCols(); col++)
			{
				nodes[row][col] = new Node(row, col);
			}
		}
	}

    /**
     * This method implements the actual BFS algorithm
     * To find a path between two game objects.
     * @param fromObject The object that's searching.
     * @param toObject The object to find.
     * @return a path between the two points.
     */
	public Path findPath(GameObject fromObject, GameObject toObject) {
		Path path = new Path();

		// Figure out the start and end point's coordinates.
		int startRow = tileMap.getRow(fromObject.getY()),
				startCol = tileMap.getCol(fromObject.getX());
		int endRow = tileMap.getRow(toObject.getY()),
				endCol = tileMap.getCol(toObject.getX());

		// Set the start and end nodes.
		Node startingNode = nodes[startRow][startCol];
		startingNode.setParent(null);
		Node endingNode = nodes[endRow][endCol];

		// First area to check is the starting point.
		notVisitedList.add(startingNode);
		while(!notVisitedList.isEmpty())
		{
			// FIFO behavior.
			Node currentNode = notVisitedList.get(0);
			// If we find the object, break the loop.
			if(currentNode.compareTo(endingNode) == 0) { break; }

			// Grab the nodes surrounding the current node.
			ArrayList<Node> neighbors = getNeighbors(currentNode);
			for(Node neighbor : neighbors)
			{
				// Check if we have not visited the neighbors.
				if(!visitedList.contains(neighbor))
				{
					// Set the parent and add the node to the not visited list.
					neighbor.setParent(currentNode);
					notVisitedList.add(neighbor);
				}
			}
			// Update lists now that the current node has been visited.
			visitedList.add(currentNode);
			notVisitedList.remove(currentNode);
		}

		// Create the path.
		Node currentNode = endingNode;
		while(currentNode.compareTo(startingNode) != 0)
		{
			path.newStartStep(currentNode);
			currentNode = currentNode.getParent();
		}
		// path.newStartStep(startingNode);

		cleanUp();
		return path;
	}

	/**
	 * This method is used to grap the neighbors
	 * that are surrounding the current node.
	 * @param node the current node.
	 * @return the neighbors of the current node.
	 */
	public ArrayList<Node> getNeighbors(Node node) {
		ArrayList<Node> neighbors = new ArrayList<>();

		Node north, east, south, west;
		try { north = nodes[node.getRow() - 1][node.getCol()]; }
		catch(ArrayIndexOutOfBoundsException e) { north = null; }
		try { east = nodes[node.getRow()][node.getCol() + 1]; }
		catch(ArrayIndexOutOfBoundsException e) { east = null; }
		try { south = nodes[node.getRow() + 1][node.getCol()]; }
		catch(ArrayIndexOutOfBoundsException e) { south = null; }
		try { west = nodes[node.getRow()][node.getCol() - 1]; }
		catch(ArrayIndexOutOfBoundsException e) { west = null; }

		// Check North.
		if(north != null) { neighbors.add(north); }
		// East.
		if(east != null) { neighbors.add(east); }
		// South.
		if(south != null) { neighbors.add(south); }
		// West.
		if(west != null) { neighbors.add(west); }

		return neighbors;
	}

    /**
     * Housekeeping.
     */
	public void cleanUp() {
        nodes = null;
        visitedList.clear();
        notVisitedList.clear();
    }
}
