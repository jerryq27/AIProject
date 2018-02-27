package com.jerry.aiproject.ai;

/**
 * This class will mainly be used for the
 * AI algorithms. It will represent a node
 * of the tile map grid structure.
 * @author Jerry
 */
public class Node implements Comparable<Node> {

    // The cost to move to a node.
    public static final int STEP_COST = 1;
    private int row, col;
    // The parent node, needed to construct the path.
    private Node parentNode;
    // Cost needs to be determined for the A Start algorithm.
    private int distanceFromStart;
    // Total cost of the move.
    private int cost;

    public Node(int r, int c) {
        row = r;
        col = c;
    }

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }

    public void setParent(Node parent) {
        parentNode = parent;
    }

    public Node getParent() {
        return parentNode;
    }

    public int getDistanceFromStart() {
        return distanceFromStart;
    }

    public void setDistanceFromStart(int distance) {
        distanceFromStart = distance;
    }


    public void calculateCost(int heuristic) {
        cost = distanceFromStart + heuristic;
    }

    public int getCost() {
        return cost;
    }

    @Override
    public String toString() {
        return "Node{" +
                "row=" + row +
                ", col=" + col +
                '}';
    }

    @Override
    public int compareTo(Node node) {
        if(this.getRow() == node.getRow())
        {
            if(this.getCol() == node.getCol()) { return 0; }
            return this.getRow() < node.getRow()? -1 : 1;
        }
        return this.getCol() < node.getCol()? -1 : 1;
    }
}
