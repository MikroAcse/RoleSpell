package ru.mikroacse.rolespell.model.pathfinding.graph;

/**
 * Created by Никита on 20.11.2016.
 */
public class GraphEdge {
    private int nodeIndexIn;
    private int nodeIndexOut;

    private double edgeWeight;

    public GraphEdge(int nodeIndexOut, int nodeIndexIn, double edgeWeight) {
        this.nodeIndexOut = nodeIndexOut;
        this.nodeIndexIn = nodeIndexIn;
        this.edgeWeight = edgeWeight;
    }

    public int getNodeIndexOut() {
        return nodeIndexOut;
    }

    public int getNodeIndexIn() {
        return nodeIndexIn;
    }

    public double getEdgeWeight() {
        return edgeWeight;
    }
}
