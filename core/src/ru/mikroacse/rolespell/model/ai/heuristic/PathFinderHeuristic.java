package ru.mikroacse.rolespell.model.ai.heuristic;

import ru.mikroacse.rolespell.model.ai.graph.GraphNode;

/**
 * Created by MikroAcse on 25.03.2017.
 */
public abstract class PathFinderHeuristic {
    private double d;

    /**
     * @param d Minimal cost of orthogonal moving
     */
    public PathFinderHeuristic(double d) {
        this.d = d;
    }

    public PathFinderHeuristic() {

    }

    /**
     * @param node 'From' node
     * @param goal 'To' node
     * @param d   Minimal cost of orthogonal moving
     */
    public abstract double get(GraphNode node, GraphNode goal, double d);

    /**
     * @param node 'From' node
     * @param goal 'To' node
     */
    public abstract double get(GraphNode node, GraphNode goal);

    public double getD() {
        return d;
    }

    public void setD(double d) {
        this.d = d;
    }
}
