package com.jerry.aiproject.aialgorithms;

import java.util.ArrayList;
import java.util.Collections;

import com.jerry.aiproject.core.TileMap;
import com.jerry.aiproject.gameobjects.GameObject;
import com.jerry.aiproject.utils.Path;

/**
 * This class will implement the 
 * A* Search algorithm, implementation
 * of this algorithm was broken down by
 * Kevin Glass, owner of Cokeandcode.com.
 * It is like Breadth First Search, but uses
 * a priority queue to sort the open list. The
 * open list is sorted based on the heuristic 
 * cost of the Nodes.
 * @author Jerry
 */
public class AStarSearch {
	
	private ArrayList<Node> closedList = new ArrayList<Node>(); //List containing searched tiles. 
	private ArrayList<Node> openList = new ArrayList<Node>(); //List containing unsearched tiles. 
	private TileMap tileMap; //The map to search. 
	
	private boolean diagonalMoves; //If diagonal moves are allowed, default: false;
	private int maxDepth; //How far to search before giving up. 
	private Node[][] nodes; //The nodes of the TileMap. 
	
	/**
	 * Constructor for an A* Search. 
	 * @param map the TileMap. 
	 * @param mapTiles booleans of all the tile states. 
	 * @param depth how far to search before giving up. 
	 */
	public AStarSearch(TileMap map, int depth, boolean allowDiagonal) {
		tileMap = map;
		maxDepth = depth;
		diagonalMoves = allowDiagonal;
		
		nodes = new Node[tileMap.getRows()][tileMap.getCols()];
		
		//Get the nodes array to have the coordinates for each tile. 
		for(int row = 0; row < tileMap.getRows(); row++)
		{
			for(int col = 0; col < tileMap.getCols(); col++)
			{
				nodes[row][col] = new Node(row, col);
			}
		}
	}
	
	/**
	 * This method calculates the heuristic
	 * needed for the A* algorithm.
	 * @param object the GameObject doing the search. 
	 * @param row the tile row. 
	 * @param col the tile column.
	 * @param tileR the goal tile row. 
	 * @param tileC the goal tile column. 
	 * @return the cost of the move. 
	 */
	public float getHeuristicCost(GameObject object, int row, int col, int tileR, int tileC) {
		float newRow = Math.abs(tileR - row);
		float newCol = Math.abs(tileC - col);
		
		float heuristic = newRow + newCol;
		
		return heuristic;
	}
	
	/**
	 * This method does the main work of the A* Algorithm.
	 * @param objectSearching the object that is going to use the path. 
	 * @param objectToFind the object being searched for. 
	 * @return the Path object containing the information about the chosen path.
	 */
	public Path findPath(GameObject objectSearching, GameObject objectToFind) {	
		//if(tileMap.isBlocked(object, goalR, goalC)) //If next tile is blocked, we can't continue;.
			//return null;
		
		//Get the coordinates of each object. 
		int startR = tileMap.getRow(objectSearching.getY()), 
			startC = tileMap.getCol(objectSearching.getX());
		int goalR = tileMap.getRow(objectToFind.getY()),
			goalC = tileMap.getCol(objectToFind.getX());
		
		//Reset values.
		nodes[startR][startC].cost = 0;
		nodes[startR][startC].depth = 0;
		//Clear the lists. 
		openList.clear();
		closedList.clear();
		
		//Add the first node to the open list. 
		openList.add(nodes[startR][startC]);
		sortOpenList(); //This list must be sorted.
		
		//First node has no parent. 
		nodes[startR][startC].parent = null;
		
		//Keeps track of the search length, 0 since we haven't started. 
		int treeDepth = 0; 
		while(treeDepth < maxDepth && openList.size() != 0)
		{
			/* Since the open list is sorted, pull out the first node, 
			 * it is most likely to be the correct node based on the heuristic.
			 */
			Node currentNode = openList.get(0);
			//If the node is our goal, just break, we've reached the destination. 
			if(currentNode == nodes[goalR][goalC])
				break;
			
			openList.remove(currentNode); //Node explored, remove from the list. 
			closedList.add(currentNode); //Add to the closed/explored list. 
		 
			//Begin searching through the neighbors of the current node.
			for(int nRow = -1; nRow < 2; nRow++)
			{
				for(int nCol = -1; nCol < 2; nCol++)
				{
					//0, 0 would be the current node, we want neighbors, so continue.
					if(nRow == 0 && nCol == 0)
						continue;
					
					//If diagonal moves are not allowed, the only nRow or nCol may be set. 
					if(!diagonalMoves)
					{
						if(nRow != 0 && nCol != 0) //One must be zero, otherwise, continue. 
							continue;
					}
					
					//Get the location of the neighbor, and evaluate.
					int neiRow = nRow + currentNode.row;
					int neiCol = nCol + currentNode.col;
					
					//Check if the neighbor is within the map and unblocked. 
					if(tileMap.isWithinMap(objectSearching, startR, startC, neiRow, neiCol))
					{
						
						float movementCost = currentNode.cost + tileMap.getMoveCost(objectSearching, currentNode.row, 
								currentNode.col, neiRow, neiCol);
						Node neighborNode = nodes[neiRow][neiCol];
						//Node has been visited. 
						tileMap.pathFinderVisited(neiRow, neiCol);
						
						//Based on the cost, this checks if there might be a better path. 
						//If the calculated cost is less than the neighbor cost. 
						if(movementCost < neighborNode.cost)
						{
							if(openList.contains(neighborNode))
								openList.remove(neighborNode);
							if(closedList.contains(neighborNode))
								closedList.remove(neighborNode);
						}
						
						//If the node has been removed, add it as a next possible step and update the cost. 
						if(!openList.contains(neighborNode) && !closedList.contains(neighborNode))
						{
							neighborNode.cost = movementCost;
							neighborNode.heuristic = getHeuristicCost(objectSearching, neiRow, neiCol, goalR, goalC);
							treeDepth = Math.max(treeDepth, neighborNode.setParent(currentNode));
							openList.add(neighborNode);
							sortOpenList();
						}
					}
				}
			}
		}
		
		//If there is nothing left to search, there was no path. 
		if(nodes[goalR][goalC].parent == null)
			return null;
		
		//The path has been found, use the parent nodes starting from
		//The current node to determine the path. 
		Path path = new Path();
		Node goalNode = nodes[goalR][goalC];
		
		while(goalNode != nodes[startR][startC])
		{
			path.newStartStep(goalNode); //Keep adding previous nodes to the top. 
			goalNode = goalNode.parent; //Set node to parent node. 
		}
		path.newStartStep(goalNode); //Add the first step to the path.
		path.printPath();
		//Path has been determined. 
		return path;
	}
	
	/**
	 * The open list (unexplored tiles) 
	 * should be sorted.
	 */
	public void sortOpenList() {
		Collections.sort(openList);
	}
}