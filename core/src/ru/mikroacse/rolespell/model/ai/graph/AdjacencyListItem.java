package ru.mikroacse.rolespell.model.ai.graph;

public class AdjacencyListItem {
    private GraphNode node;
    private double weight = 0;

    public AdjacencyListItem(GraphNode node, double weight) {
        this.node = node;
        this.weight = weight;
    }

    public AdjacencyListItem(GraphNode node) {
        this(node, 0.0);
    }

    public GraphNode getNode() {
        return node;
    }

    public void setNode(GraphNode node) {
        this.node = node;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }
}
