package com.jerry.aiproject.ai;

import com.jerry.aiproject.data.TileMap;
import com.jerry.aiproject.gameobjects.GameObject;

import java.util.ArrayList;

/**
 * This class uses the Breadth First Search
 * Algorithm to find a path between two
 * Game Objects. This algorithm works by
 * Searching each node and their neighbors
 * Until the object is found.
 * @author Jerry
 */
public class BreadthFirstSearch extends PathFinder {

	public BreadthFirstSearch(TileMap map) {
		super(map);
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
		notVisited.add(startingNode);
		while(!notVisited.isEmpty())
		{
			// FIFO behavior.
			Node currentNode = notVisited.get(0);
			// If we find the object, break the loop.
			if(currentNode.compareTo(endingNode) == 0) { break; }

			// Grab the nodes surrounding the current node.
			ArrayList<Node> neighbors = getNeighbors(currentNode);
			for(Node neighbor : neighbors)
			{
				// Check if we have not visited the neighbors.
				if(!visited.contains(neighbor) && !notVisited.contains(neighbor))
				{
					// Set the parent and add the node to the not visited list.
					neighbor.setParent(currentNode);
					notVisited.add(neighbor);
				}
			}
			// Update lists now that the current node has been visited.
			visited.add(currentNode);
			notVisited.remove(currentNode);
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
