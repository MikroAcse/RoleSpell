package ru.mikroacse.rolespell.app.model.game.pathfinding;

import com.badlogic.gdx.math.Rectangle;
import ru.mikroacse.rolespell.app.model.game.pathfinding.graph.Graph;
import ru.mikroacse.rolespell.app.model.game.world.World;
import ru.mikroacse.rolespell.app.model.game.world.cells.CellWeigher;

/**
 * Created by MikroAcse on 24.03.2017.
 */
public class GraphBuilder {
    public static Graph fromWorld(World world, Rectangle bounds, CellWeigher cellWeigher) {
        int startX = (int) bounds.x;
        int startY = (int) bounds.y;

        int width = (int) bounds.width;
        int height = (int) bounds.height;

        Graph graph = new Graph(width * height);

        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                graph.setCellXY(getIndex(i, j, height), i + startX, j + startY);
            }
        }

        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                int x = i + startX;
                int y = j + startY;

                if (cellWeigher.weigh(world, x, y) < 0) {
                    continue;
                }

                int cellIndex = getIndex(i, j, height);

                // LEFT
                double leftWeight = cellWeigher.weigh(world, x - 1, y);
                if (i - 1 >= 0 && leftWeight >= 0) {
                    graph.addEdge(cellIndex, getIndex(i - 1, j, height), leftWeight);
                }

                // RIGHT
                double rightWeight = cellWeigher.weigh(world, x + 1, y);
                if (i + 1 < width && rightWeight >= 0) {
                    graph.addEdge(cellIndex, getIndex(i + 1, j, height), rightWeight);
                }

                // BOTTOM
                double bottomWeight = cellWeigher.weigh(world, x, y - 1);
                if (j - 1 >= 0 && bottomWeight >= 0) {
                    graph.addEdge(cellIndex, getIndex(i, j - 1, height), bottomWeight);
                }

                // TOP
                double topWeight = cellWeigher.weigh(world, x, y + 1);
                if (j + 1 < height && topWeight >= 0) {
                    graph.addEdge(cellIndex, getIndex(i, j + 1, height), topWeight);
                }
            }
        }

        return graph;
    }

    private static int getIndex(int x, int y, int height) {
        return x * height + y;
    }
}
