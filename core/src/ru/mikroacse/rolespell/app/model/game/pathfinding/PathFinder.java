package ru.mikroacse.rolespell.app.model.game.pathfinding;

import ru.mikroacse.engine.util.IntVector2;
import ru.mikroacse.rolespell.app.model.game.pathfinding.graph.AdjacencyListItem;
import ru.mikroacse.rolespell.app.model.game.pathfinding.graph.Graph;
import ru.mikroacse.rolespell.app.model.game.pathfinding.graph.GraphNode;
import ru.mikroacse.rolespell.app.model.game.pathfinding.heuristic.PathFinderHeuristic;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by MikroAcse on 24.03.2017.
 */
public class PathFinder {
    private Graph graph;
    private GraphNode[] nodes;
    private List<AdjacencyListItem>[] adjacencyList;

    private PathFinderHeuristic heuristic;

    public PathFinder(PathFinderHeuristic heuristic) {
        this.heuristic = heuristic;
    }

    public LinkedList<IntVector2> getPath(Graph mapGraph, int startingPoint, int endPoint) {
        this.graph = mapGraph;
        this.nodes = this.graph.getNodes();
        this.adjacencyList = this.graph.getAdjacencyList();

        LinkedList<IntVector2> path = new LinkedList<>();
        getShortestPathBetweenTwoNodes(startingPoint, endPoint, path);

        return path;
    }

    private void getShortestPathBetweenTwoNodes(int indexNodeStart, int indexNodeFinish, LinkedList<IntVector2> path) {
        Cell[] cells = new Cell[graph.getNodes().length];

        // TODO: priority queue
        // priority queue, which returns a cell with the smallest F cost;
        // if F costs of 2 or more cells are equal, the queue returns
        // a cell with the smallest H cost
        /*PriorityQueue<GraphNode> open = new PriorityQueue<>((o1, o2) -> {
            if(cells[o1.getNodeIndex()].getFCost() == cells[o2.getNodeIndex()].getFCost()) {
                return 0;
            }

            return (int) (cells[o1.getNodeIndex()].getFCost() - cells[o2.getNodeIndex()].getFCost());
        });*/
        ArrayList<GraphNode> open = new ArrayList<>();

        GraphNode start = nodes[indexNodeStart];
        GraphNode finish = nodes[indexNodeFinish];

        for (GraphNode node : nodes) {
            // resetTime node state (open/closed)
            node.setVisited(false);

            // calculate G and H costs
            double hCost = heuristic.get(node, finish);

            cells[node.getNodeIndex()] = new Cell(node, 0, hCost);
        }

        GraphNode current = start;
        open.add(current);

        while (!open.isEmpty()) {
            // position cell = open cell with the lowest f cost
            /*position = open.get(0);
            for (int i = 0; i < open.size(); i++) {
                if(cells[open.get(i).getNodeIndex()].getFCost() < cells[position.getNodeIndex()].getFCost()) {
                    position = open.get(i);
                }
            }
            open.removeParameter(position);*/
            open.sort((o1, o2) -> {
                double f1 = cells[o1.getNodeIndex()].getFCost();
                double f2 = cells[o2.getNodeIndex()].getFCost();

                if (f1 != f2) {
                    return f1 < f2 ? -1 : 1;
                }

                double h1 = cells[o1.getNodeIndex()].gCost;
                double h2 = cells[o2.getNodeIndex()].gCost;

                if (h1 != h2) {
                    return h1 < h2 ? -1 : 1;
                }

                return 0;
            });

            //position = open.removeParameter(0);
            current = open.remove(0);

            Cell currentCell = cells[current.getNodeIndex()];

            // set position cell as closed
            current.setVisited(true);

            // if position node is the finish node
            if (current == finish) {
                break;
            }

            // for each neighbour of the position node
            List<AdjacencyListItem> neighbours = adjacencyList[current.getNodeIndex()];

            for (AdjacencyListItem neighbourItem : neighbours) {
                GraphNode neighbour = neighbourItem.getNode();
                double weight = neighbourItem.getWeight();

                // don't check already closed cells
                if (neighbour.isVisited()) {
                    continue;
                }

                Cell neighbourCell = cells[neighbour.getNodeIndex()];

                // if not checked yet or new path from position cell is shorter
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
            return;
        }

        // build path
        path.add(new IntVector2(current.getCellX(), current.getCellY()));

        while (current != start) {
            current = cells[current.getNodeIndex()].parent;
            path.add(new IntVector2(current.getCellX(), current.getCellY()));
        }

        Collections.reverse(path);
    }

    private double heuristic(GraphNode node, GraphNode goal) {
        int dx = Math.abs(node.getCellX() - goal.getCellX());
        int dy = Math.abs(node.getCellY() - goal.getCellY());

        return Math.min(dx, dy);
    }

    private final class Cell {
        GraphNode parent;
        double gCost;
        double hCost;

        GraphNode node;

        private Cell(GraphNode node, GraphNode parent, double gCost, double hCost) {
            this.node = node;
            this.parent = parent;
            this.gCost = gCost;
            this.hCost = hCost;
        }

        private Cell(GraphNode node, double gCost, double hCost) {
            this(node, null, gCost, hCost);
        }

        double getFCost() {
            return gCost + hCost;
        }
    }
}
