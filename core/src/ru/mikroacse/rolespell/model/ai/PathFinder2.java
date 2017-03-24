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
public class PathFinder2 {
    private Graph graph;
    private GraphNode[] nodes;
    private List<AdjacencyListItem>[] adjacencyList;

    private ArrayList<Cell> cells;
    private PriorityQueue<GraphNode> frontier;

    public LinkedList<GraphNode> getPath(Graph mapGraph, int startingPoint, int endPoint) {
        this.graph = mapGraph;
        this.nodes = this.graph.getNodes();
        this.adjacencyList = this.graph.getAdjacencyList();

        LinkedList<GraphNode> path = new LinkedList<>();
        getShortestPathBetweenTwoNodes(startingPoint, endPoint, path);

        return path;
    }

    /**
     * Найти кратчайший путь между указанными вершинами.
     *
     * @param indexNodeStart  Начальная вершина
     * @param indexNodeFinish Конечная вершина
     * @param path            Список вершин в пути
     */
    private void getShortestPathBetweenTwoNodes(int indexNodeStart, int indexNodeFinish, LinkedList<GraphNode> path) {
        cells = new ArrayList<>();

        frontier = new PriorityQueue<>((o1, o2) -> {
            Cell cell1 = cells.get(o1.getNodeIndex());
            Cell cell2 = cells.get(o2.getNodeIndex());

            if (cell1.cost != cell2.cost)
                return cell1.cost < cell2.cost ? 1 : -1;

            return 0;
        });

        GraphNode start = nodes[indexNodeStart];
        GraphNode finish = nodes[indexNodeFinish];

        for (GraphNode node : nodes) {
            node.setVisited(false);
        }

        for (GraphNode node : nodes) {
            cells.add(new Cell(node, 0));
        }

        GraphNode current = start;
        frontier.add(current);

        while (!frontier.isEmpty()) {
            current = frontier.remove();
            Cell currentCell = cells.get(current.getNodeIndex());

            current.setVisited(true);

            if (current == finish) break;

            List<AdjacencyListItem> neighbours = adjacencyList[current.getNodeIndex()];

            for (AdjacencyListItem neighbourItem : neighbours) {
                GraphNode neighbour = neighbourItem.getNode();
                double weight = neighbourItem.getWeight();

                if (neighbour.isVisited()) continue;

                Cell neighbourCell = cells.get(neighbour.getNodeIndex());

                double newCost = currentCell.cost + weight;
                if (!frontier.contains(neighbour) || newCost < neighbourCell.cost) {
                    if (!frontier.contains(neighbour)) {
                        frontier.add(neighbour);
                    }

                    neighbourCell.cost = newCost;
                    neighbourCell.parent = current;
                }
            }
        }

        if (current != finish) {
            System.out.println("No path found!");
            return;
        }

        path.add(current);

        while (current != start) {
            current = cells.get(current.getNodeIndex()).parent;

            path.add(current);
        }

        Collections.reverse(path);
    }

    public LinkedList<Point> convertPathToCells(LinkedList<GraphNode> path) {
        LinkedList<Point> convertedPath = new LinkedList<>();

        for (GraphNode node : path) {
            convertedPath.add(new Point(node.getCellX(), node.getCellY()));
        }

        return convertedPath;
    }

    private class Cell {
        GraphNode parent;
        double cost;

        GraphNode node;

        Cell(GraphNode node, GraphNode parent, double cost) {
            this.node = node;
            this.parent = parent;
            this.cost = cost;
        }

        Cell(GraphNode node, double cost) {
            this(node, null, cost);
        }
    }
}
