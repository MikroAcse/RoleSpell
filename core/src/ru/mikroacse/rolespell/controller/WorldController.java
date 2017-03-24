package ru.mikroacse.rolespell.controller;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Rectangle;
import ru.mikroacse.rolespell.model.GameModel;
import ru.mikroacse.rolespell.model.ai.GraphBuilder;
import ru.mikroacse.rolespell.model.ai.PathFinder;
import ru.mikroacse.rolespell.model.ai.PathFinder2;
import ru.mikroacse.rolespell.model.ai.graph.Graph;
import ru.mikroacse.rolespell.model.world.World;
import ru.mikroacse.rolespell.view.WorldRenderer;

import java.awt.*;

/**
 * Created by MikroAcse on 22.03.2017.
 */
public class WorldController {
    private WorldRenderer renderer;
    private GameModel model;

    private WorldInputAdapter inputAdapter;

    public WorldController(WorldRenderer renderer, GameModel model) {
        this.renderer = renderer;
        this.model = model;

        inputAdapter = new WorldInputAdapter();
        Gdx.input.setInputProcessor(inputAdapter);

        RectangleMapObject pl = (RectangleMapObject) model.getWorld().getMapLayer(World.Layer.SPAWNERS).getObjects().get("PLAYER");
        Rectangle rect = pl.getRectangle();
        Point plSpawnPoint = renderer.realToMap((int) rect.x, (int) rect.y);

        System.out.println(plSpawnPoint);
        model.getPlayer().setPosition(plSpawnPoint.x, plSpawnPoint.y);
    }

    public void update() {
        if (inputAdapter.isJustTouched()) {
            for (int i = 0; i < model.getWorld().getMapWidth(); i++) {
                for (int j = 0; j < model.getWorld().getMapHeight(); j++) {
                    model.getWorld().getMapTileLayer(World.Layer.TOP).setCell(i, j, null);
                }
            }

            int x = inputAdapter.getMouseX();
            int y = inputAdapter.getMouseY();

            Point coordinates = renderer.globalToLocal(x, y);
            Point cellPosition = model.getWorld().getCellPosition(coordinates.x, coordinates.y);

            //model.getPlayer().setPosition(cellPosition.x, cellPosition.y);
            model.setWaypoint(cellPosition.x, cellPosition.y);

            model.isMoving = true;

            int playerX = model.getPlayer().x;
            int playerY = model.getPlayer().y;

            int width = Math.abs(playerX - cellPosition.x);
            int height = Math.abs(playerY - cellPosition.y);
            int distance = Math.max(width, height) + 10;

            Rectangle rect = new Rectangle(
                    Math.max(playerX - distance, 0),
                    Math.max(playerY - distance, 0),
                    distance * 2 + 1,
                    distance * 2 + 1);


            PathFinder pathFinder = new PathFinder();

            Graph graph = GraphBuilder.fromWorld(model.getWorld(), rect);

            /*int i = 0;
            for (LinkedList<AdjacencyListItem> adjacencyListItems : graph.getAdjacencyList()) {
                System.out.print(graph.getNodes()[i].getCellX() + ":" + graph.getNodes()[i].getCellY() + " -> ");
                for (AdjacencyListItem adjacencyListItem : adjacencyListItems) {
                    System.out.print(adjacencyListItem.getNode().getCellX() + ":" + adjacencyListItem.getNode().getCellY() + ", ");
                }
                System.out.println();
                i++;
            }*/

            model.clearPath();
            model.addPath(pathFinder.convertPathToCells(
                    pathFinder.getPath(
                            graph,
                            (int) (rect.height * (playerX - rect.x) + playerY - rect.y),
                            (int) (rect.height * (cellPosition.x - rect.x) + (cellPosition.y - rect.y))
                    )));
        }

        if (model.getPath().size() > 0) {
            Point point = model.getPath().remove();

            TiledMapTileLayer.Cell cell = new TiledMapTileLayer.Cell();
            cell.setTile(model.getWorld().getMap().getTileSets().getTileSet(0).getTile(27));

            model.getWorld().getMapTileLayer(World.Layer.TOP).setCell(point.x, point.y, cell);
            model.getPlayer().setPosition(point.x, point.y);
        }

        inputAdapter.update();
    }
}
