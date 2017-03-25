package ru.mikroacse.rolespell.model.pathfinding.graph;

import java.util.LinkedList;

public class Graph {
    private double[][] adjacencyMatrix;

    private LinkedList<AdjacencyListItem>[] adjacencyList;

    private GraphNode[] nodes;

    public Graph(int numberOfVertices) {
        createNodes(numberOfVertices);

        adjacencyMatrix = new double[numberOfVertices][];
        adjacencyList = new LinkedList[numberOfVertices];

        for (int i = 0; i < numberOfVertices; i++) {
            adjacencyMatrix[i] = new double[numberOfVertices];
            adjacencyList[i] = new LinkedList<>();
        }
    }

    public void setCellXY(int index, int cellX, int cellY) {
        nodes[index].setCellXY(cellX, cellY);
    }

    private void createNodes(int numberOfNodes) {
        this.nodes = new GraphNode[numberOfNodes];

        for (int i = 0; i < nodes.length; i++) {
            nodes[i] = new GraphNode(i);
        }
    }

    public void addEdge(int nodeIndexA, int nodeIndexB, double weight) {
        if (nodeIndexA >= nodes.length || nodeIndexB >= nodes.length)
            return;

        if (adjacencyMatrix[nodeIndexA][nodeIndexB] == 0) {
            adjacencyMatrix[nodeIndexA][nodeIndexB] = weight;

            adjacencyList[nodeIndexA].add(new AdjacencyListItem(nodes[nodeIndexB], weight));
        }
    }

    public void addEdge(int nodeIndexA, int nodeIndexB) {
        addEdge(nodeIndexA, nodeIndexB, 1);
    }

    public void addEdge(int nodeIndexA, int nodeIndexB, double weight, boolean twoDirection) {
        if (twoDirection) {
            addEdge(nodeIndexA, nodeIndexB, weight);
            addEdge(nodeIndexB, nodeIndexA, weight);
        } else {
            addEdge(nodeIndexA, nodeIndexB, weight);
        }
    }

    public void removeEdge(int nodeIndexA, int nodeIndexB) {
        if (nodeIndexA >= nodes.length || nodeIndexB >= nodes.length)
            return;

        adjacencyMatrix[nodeIndexA][nodeIndexB] = 0;

        for (AdjacencyListItem item : adjacencyList[nodeIndexA]) {
            if (item.getNode().getNodeIndex() == nodeIndexB)
                adjacencyList[nodeIndexA].remove();
        }

    }

    public void removeEdge(int nodeIndexA, int nodeIndexB, boolean twoDirection) {
        if (twoDirection) {
            removeEdge(nodeIndexA, nodeIndexB);
            removeEdge(nodeIndexB, nodeIndexA);
        } else {
            removeEdge(nodeIndexA, nodeIndexB);
        }
    }

    public double getWeight(int nodeIndexA, int nodeIndexB) {
        return adjacencyMatrix[nodeIndexA][nodeIndexB];
    }

    public LinkedList<AdjacencyListItem>[] getAdjacencyList() {
        return adjacencyList;
    }

    public GraphNode[] getNodes() {
        return nodes;
    }
}
