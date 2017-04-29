package ru.mikroacse.rolespell.screens.game.model.pathfinding;

import com.badlogic.gdx.math.Rectangle;
import ru.mikroacse.rolespell.screens.game.model.pathfinding.graph.Graph;
import ru.mikroacse.rolespell.screens.game.model.world.World;

/**
 * Created by MikroAcse on 24.03.2017.
 */
public class GraphBuilder {
    public static Graph fromWorld(World world, Rectangle bounds) {
        int startX = (int) bounds.x;
        int startY = (int) bounds.y;

        int endX = (int) (startX + bounds.width);
        int endY = (int) (startY + bounds.height);

        int width = (int) bounds.width;
        int height = (int) bounds.height;

        Graph graph = new Graph(width * height);

        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                graph.setCellXY(i * height + j, i + startX, j + startY);
            }
        }

        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                int x = i + startX;
                int y = j + startY;

                int cellIndex = getIndex(i, j, height);

                if (!world.isPassable(x, y, false)) {
                    continue;
                }

                // LEFT
                if (i - 1 >= 0 && world.isPassable(x - 1, y, false)) {
                    graph.addEdge(cellIndex, getIndex(i - 1, j, height), world.getWeight(x - 1, y));
                }
                // RIGHT
                if (i + 1 < width && world.isPassable(x + 1, y, false)) {
                    graph.addEdge(cellIndex, getIndex(i + 1, j, height), world.getWeight(x + 1, y));
                }
                // BOTTOM
                if (j - 1 >= 0 && world.isPassable(x, y - 1, false)) {
                    graph.addEdge(cellIndex, getIndex(i, j - 1, height), world.getWeight(x, y - 1));
                }
                // TOP
                if (j + 1 < height && world.isPassable(x, y + 1, false)) {
                    graph.addEdge(cellIndex, getIndex(i, j + 1, height), world.getWeight(x, y + 1));
                }
            }
        }

        return graph;
    }

    private static int getIndex(int x, int y, int height) {
        return x * height + y;
    }
}
