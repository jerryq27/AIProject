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

    /**
     * Heuristic calculation for the
     * A* search algorithm. It determines
     * The cost of a node based on its
     * Distance to the goal node.
     * @param goalNode the end node.
     * @return the cost of the node to the distance.
     */
    public int calculateHeuristic(Node goalNode) {
        int rowCost = goalNode.getRow() - this.getRow();
        int colCost = goalNode.getCol() - this.getRow();

        return rowCost + colCost;
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
