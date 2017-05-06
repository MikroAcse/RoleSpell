package ru.mikroacse.rolespell.app.model.game.pathfinding.heuristic;

import ru.mikroacse.rolespell.app.model.game.pathfinding.graph.GraphNode;

/**
 * Created by MikroAcse on 25.03.2017.
 */
public class ManhattanDistance extends PathFinderHeuristic {
    public ManhattanDistance(double d) {
        super(d);
    }

    public ManhattanDistance() {

    }

    @Override
    public double get(GraphNode node, GraphNode goal, double d) {
        int dx = Math.abs(node.getCellX() - goal.getCellX());
        int dy = Math.abs(node.getCellY() - goal.getCellY());

        return d * Math.max(dx, dy);
    }

    @Override
    public double get(GraphNode node, GraphNode goal) {
        return get(node, goal, getD());
    }
}
