package com.jerry.aiproject.ai;

import com.jerry.aiproject.data.TileMap;
import com.jerry.aiproject.gameobjects.GameObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Abstract class for the path finding
 * Algorithms. It contains all of the
 * Fields and methods a path finding
 * Algorithm Needs to construct a path
 * Between two objects.
 * @author Jerry
 */
public abstract class PathFinder {

    protected TileMap tileMap;
    protected Node nodes[][];
    protected List<Node> visited = new ArrayList<>();
    protected List<Node> notVisited = new ArrayList<>();

    public PathFinder(TileMap map) {
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

    // Method where the algorithm will be implemented.
    public abstract Path findPath(GameObject fromObject, GameObject toObject);

    /**
     * This method is used to grap the neighbors
     * that are surrounding the current node.
     * @param node the current node.
     * @return the neighbors of the current node.
     */
    protected ArrayList<Node> getNeighbors(Node node) {
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
    protected void cleanUp() {
        nodes = null;
        visited.clear();
        notVisited.clear();
    }
}
