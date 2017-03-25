package ru.mikroacse.rolespell.controller;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.math.Rectangle;
import ru.mikroacse.rolespell.model.GameModel;
import ru.mikroacse.rolespell.model.pathfinding.GraphBuilder;
import ru.mikroacse.rolespell.model.pathfinding.PathFinder;
import ru.mikroacse.rolespell.model.pathfinding.graph.Graph;
import ru.mikroacse.rolespell.model.pathfinding.heuristic.ManhattanDistance;
import ru.mikroacse.rolespell.model.world.World;
import ru.mikroacse.rolespell.view.WorldRenderer;

import java.awt.*;
import java.util.LinkedList;

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
    }

    public void update(float delta) {
        World world = model.getWorld();

        if (inputAdapter.isJustTouched()) {
            int mouseX = inputAdapter.getTouchMouseX();
            int mouseY = inputAdapter.getTouchMouseY();

            Point coordinates = renderer.globalToLocal(mouseX, mouseY);
            Point cellPosition = world.getCellPosition(coordinates.x, coordinates.y);

            int x = cellPosition.x;
            int y = cellPosition.y;

            if (world.getMeta(x, y) != World.Meta.SOLID) {
                moveTo(x, y);
            } else {
                Point nearestEmptyCell = world.getNearestEmptyCell(World.Layer.META, x, y, 2);

                if (nearestEmptyCell != null) {
                    moveTo(nearestEmptyCell.x, nearestEmptyCell.y);
                }
            }
        }

        model.update(delta);

        inputAdapter.update();
    }

    public void moveTo(int x, int y) {
        int playerX = model.getPlayer().x;
        int playerY = model.getPlayer().y;

        int minX = Math.min(playerX, x);
        int minY = Math.min(playerY, y);

        int maxX = Math.max(playerX, x);
        int maxY = Math.max(playerY, y);

        int width = maxX - minX + 1;
        int height = maxY - minY + 1;

        Rectangle rect = new Rectangle(
                Math.max(minX - 5, 0),
                Math.max(minY - 5, 0),
                width + 10,
                height + 10);


        PathFinder pathFinder = new PathFinder(new ManhattanDistance(model.getWorld().getWeight(World.Meta.PATH)));

        Graph graph = GraphBuilder.fromWorld(model.getWorld(), rect);

        LinkedList<Point> path = pathFinder.convertPathToCells(
                pathFinder.getPath(
                        graph,
                        (int) (rect.height * (playerX - rect.x) + (playerY - rect.y)),
                        (int) (rect.height * (x - rect.x) + (y - rect.y))
                ));

        if (path.size() > 1) {
            path.remove();

            model.getPlayer().clearPath();
            model.getPlayer().addToPath(path);

            model.setWaypoint(x, y);
        }
    }
}
