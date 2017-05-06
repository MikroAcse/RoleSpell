package ru.mikroacse.rolespell.app.model.game.pathfinding.graph;

/**
 * Created by Никита on 20.11.2016.
 */
public class GraphNode {
    private int nodeIndex = -1;
    private boolean visited = false;

    private int cellX, cellY;

    public GraphNode(int nodeIndex) {
        this.nodeIndex = nodeIndex;
        visited = false;
    }

    public void setCellXY(int cellX, int cellY) {

        this.cellX = cellX;
        this.cellY = cellY;
    }

    public int getCellX() {
        return cellX;
    }

    public int getCellY() {
        return cellY;
    }

    public int getNodeIndex() {
        return nodeIndex;
    }

    public boolean isVisited() {
        return visited;
    }

    public void setVisited(boolean visitedState) {
        this.visited = visitedState;
    }

    @Override
    public String toString() {
        return "GraphNode{" +
                "nodeIndex=" + nodeIndex +
                ", visited=" + visited +
                ", cellX=" + cellX +
                ", cellY=" + cellY +
                '}';
    }
}
