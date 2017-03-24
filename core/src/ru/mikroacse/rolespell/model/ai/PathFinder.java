package ru.mikroacse.rolespell.model.ai;

import ru.mikroacse.rolespell.model.ai.graph.AdjacencyListItem;
import ru.mikroacse.rolespell.model.ai.graph.Graph;
import ru.mikroacse.rolespell.model.ai.graph.GraphNode;

import java.awt.*;
import java.util.*;
import java.util.List;

/**
 * Created by MikroAcse on 24.03.2017.
 */
public class PathFinder {
    private Graph graph;
    private GraphNode[] nodes;
    private List<AdjacencyListItem>[] adjacencyList;

    private List<Cell> cells;
    private PriorityQueue<GraphNode> open;

    public LinkedList<GraphNode> getPath(Graph mapGraph, int startingPoint, int endPoint) {
        this.graph = mapGraph;
        this.nodes = this.graph.getNodes();
        this.adjacencyList = this.graph.getAdjacencyList();

        LinkedList<GraphNode> path = new LinkedList<>();
        getShortestPathBetweenTwoNodes(startingPoint, endPoint, path);

        return path;
    }

    public LinkedList<Point> convertPathToCells(LinkedList<GraphNode> path) {
        LinkedList<Point> convertedPath = new LinkedList<>();

        for (GraphNode node : path) {
            convertedPath.add(new Point(node.getCellX(), node.getCellY()));
        }

        return convertedPath;
    }

    private void getShortestPathBetweenTwoNodes(int indexNodeStart, int indexNodeFinish, LinkedList<GraphNode> path) {
        cells = new ArrayList<>();

        // priority queue, which returns a cell with the smallest F cost;
        // if F costs of 2 or more cells are equal, the queue returns
        // a cell with the smallest H cost
        open = new PriorityQueue<>((o1, o2) -> {
            Cell cell1 = cells.get(o1.getNodeIndex());
            Cell cell2 = cells.get(o2.getNodeIndex());

            double f1 = cell1.getFCost();
            double f2 = cell2.getFCost();

            if (f1 != f2)
                return f1 < f2 ? 1 : -1;

            if (cell1.hCost != cell2.hCost)
                return cell1.hCost < cell2.hCost ? 1 : -1;

            return 0;
        });

        GraphNode start = nodes[indexNodeStart];
        GraphNode finish = nodes[indexNodeFinish];

        // using "visited" variable instead of a list of the closed cells
        for (GraphNode node : nodes) {
            node.setVisited(false);
        }

        // calculate G and H costs
        for (GraphNode node : nodes) {
            double hCost = heuristic(node, finish);

            cells.add(new Cell(node, 0, hCost));
        }

        GraphNode current = start;
        open.add(current);

        while (!open.isEmpty()) {
            // current cell = open cell with the lowest f cost
            current = open.remove();
            Cell currentCell = cells.get(current.getNodeIndex());

            // set current cell as closed
            current.setVisited(true);

            // if current node is the finish node
            if (current == finish) break;

            // for each neighbour of the current node
            List<AdjacencyListItem> neighbours = adjacencyList[current.getNodeIndex()];

            for (AdjacencyListItem neighbourItem : neighbours) {
                GraphNode neighbour = neighbourItem.getNode();
                double weight = neighbourItem.getWeight();

                // don't check already closed cells
                if (neighbour.isVisited()) continue;

                Cell neighbourCell = cells.get(neighbour.getNodeIndex());

                // if not checked yet or new path from current cell is shorter
                double newCost = currentCell.gCost + weight;
                if (!open.contains(neighbour) || newCost < neighbourCell.gCost) {
                    if (!open.contains(neighbour)) {
                        open.add(neighbour);
                    }

                    neighbourCell.gCost = newCost;
                    neighbourCell.parent = current;
                }
            }
        }

        // no path found
        if (current != finish) {
            System.out.println("No path found!");
            return;
        }

        // build path
        path.add(current);

        while (current != start) {
            current = cells.get(current.getNodeIndex()).parent;

            path.add(current);
        }

        Collections.reverse(path);
    }

    private double heuristic(GraphNode node, GraphNode goal) {
        int dx = Math.abs(node.getCellX() - goal.getCellX());
        int dy = Math.abs(node.getCellY() - goal.getCellY());

        return dx + dy;
    }

    private class Cell {
        GraphNode parent;
        double gCost;
        double hCost;

        GraphNode node;

        Cell(GraphNode node, GraphNode parent, double gCost, double hCost) {
            this.node = node;
            this.parent = parent;
            this.gCost = gCost;
            this.hCost = hCost;
        }

        Cell(GraphNode node, double gCost, double hCost) {
            this(node, null, gCost, hCost);
        }

        double getFCost() {
            return gCost + hCost;
        }
    }
}
