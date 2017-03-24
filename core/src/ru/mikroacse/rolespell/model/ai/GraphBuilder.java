package ru.mikroacse.rolespell.model.ai;

import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Rectangle;
import ru.mikroacse.rolespell.model.ai.graph.Graph;
import ru.mikroacse.rolespell.model.world.World;

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
                int x = i + startX;
                int y = j + startY;

                int cellIndex = getIndex(i, j, height);

                graph.setCellXY(cellIndex, x, y);

                if (!world.isTraversable(x, y))
                    continue;

                TiledMapTileLayer.Cell cell = new TiledMapTileLayer.Cell();
                cell.setTile(world.getMap().getTileSets().getTileSet(0).getTile(31));
                world.getMapTileLayer(World.Layer.TOP).setCell(x, y, cell);

                // LEFT/RIGHT/TOP/BOTTOM
                if (i - 1 >= 0 && world.isTraversable(x - 1, y))
                    addEdge(graph, cellIndex, getIndex(i - 1, j, height));

                if (i + 1 < width && world.isTraversable(x + 1, y))
                    addEdge(graph, cellIndex, getIndex(i + 1, j, height));

                if (j + 1 < height && world.isTraversable(x, y + 1))
                    addEdge(graph, cellIndex, getIndex(i, j + 1, height));

                if (j - 1 >= 0 && world.isTraversable(x, y - 1))
                    addEdge(graph, cellIndex, getIndex(i, j - 1, height));
            }
        }

        return graph;
    }

    private static int getIndex(int x, int y, int height) {
        return x * height + y;
    }

    private static void addEdge(Graph graph, int cell1Index, int cell2Index) {
        graph.addEdge(cell1Index, cell2Index, 1);
    }
}
