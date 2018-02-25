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
public class AStarSearch extends PathFinder {

	public AStarSearch(TileMap map) {
		super(map);
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
		notVisited.add(startingNode);
		while(!notVisited.isEmpty())
		{
			// FIFO behavior.
			Node currentNode = notVisited.get(0);
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
			if(!notVisited.contains(lowestCostNode) && !visited.contains(currentNode))
			{
				lowestCostNode.setParent(currentNode);
				notVisited.add(lowestCostNode);

				notVisited.remove(currentNode);
				visited.add(currentNode);
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
}
