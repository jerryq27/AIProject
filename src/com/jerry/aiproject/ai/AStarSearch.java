package com.jerry.aiproject.ai;

import java.util.ArrayList;
import java.util.List;

import com.jerry.aiproject.data.TileMap;
import com.jerry.aiproject.gameobjects.GameObject;

/**
 * This class uses the A* algorithm to find
 * A path between two Game Objects. This
 * Algorithm works by picking a node with
 * The lowest cost based on the cost of
 * Moving from node to node and the
 * Node's distance from the goal.
 *
 * Implementation of this algorithm was broken down by
 * Kevin Glass, owner of Cokeandcode.com.
 * It uses a priority queue to sort the open list.
 * The open list is sorted based on the heuristic
 * cost of the Nodes.
 * @author Jerry
 */
public class AStarSearch {

	private List<Node> visitedList = new ArrayList<>();
	private List<Node> notVisitedList = new ArrayList<>();
	private TileMap tileMap;
	private Node[][] nodes;

	public AStarSearch(TileMap map) {
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
	 * This method implements the actual A* algorithm
	 * To find a path between two game objects.
	 *
	 * Pick a node according to the value f.
	 * f = g + h
	 * g = movement cost to get from the start to the given node.
	 * h = the estimated cost to move from the given node to the goal.
	 *
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

		// Set the cost of the starting node to 0 since we haven't moved yet.
		startingNode.setDistanceFromStart(0);
		// Also set the total cost with the heuristic.
		startingNode.setHeuristic(endingNode);
		startingNode.calculateCost();

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
			Node lowestCostNode = neighbors.get(0);
			for(Node neighbor : neighbors)
			{
				neighbor.setDistanceFromStart(currentNode.getDistanceFromStart() + Node.STEP_COST);
				neighbor.setHeuristic(endingNode);
				neighbor.calculateCost();

				lowestCostNode = neighbor.getCost() < lowestCostNode.getCost()? neighbor : lowestCostNode;
			}
			if(!notVisitedList.contains(lowestCostNode) && !visitedList.contains(currentNode))
			{
				lowestCostNode.setParent(currentNode);
				notVisitedList.add(lowestCostNode);

				notVisitedList.remove(currentNode);
				visitedList.add(currentNode);
			}
		}

		// Create the path.
		Node currentNode = endingNode;
		while(currentNode.compareTo(startingNode) != 0)
		{
			path.prependNode(currentNode);
			currentNode = currentNode.getParent();
		}
		// path.prependNode(startingNode);

		cleanUp();
		return path;
	}

	/**
	 * This method is used to grab the neighbors
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
