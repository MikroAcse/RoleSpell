package ru.mikroacse.rolespell.app.model.game.pathfinding.graph;

public class AdjacencyListItem {
    private GraphNode node;
    private double weight = 0;

    public AdjacencyListItem(GraphNode node, double weight) {
        this.node = node;
        this.weight = weight;
    }

    public GraphNode getNode() {
        return node;
    }

    public double getWeight() {
        return weight;
    }
}
