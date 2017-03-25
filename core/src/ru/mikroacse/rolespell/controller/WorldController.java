package ru.mikroacse.rolespell.controller;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.math.Rectangle;
import ru.mikroacse.rolespell.model.GameModel;
import ru.mikroacse.rolespell.model.ai.GraphBuilder;
import ru.mikroacse.rolespell.model.ai.PathFinder;
import ru.mikroacse.rolespell.model.ai.graph.Graph;
import ru.mikroacse.rolespell.model.ai.heuristic.ManhattanDistance;
import ru.mikroacse.rolespell.model.world.World;
import ru.mikroacse.rolespell.view.WorldRenderer;

import java.awt.*;
import java.util.LinkedList;

/**
 * Created by MikroAcse on 22.03.2017.
 */
public class WorldController {
    public static float MOVING_SPEED = 10f; // cells per second

    private WorldRenderer renderer;
    private GameModel model;

    private WorldInputAdapter inputAdapter;

    private float movingUpdate;

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

    public void update(float delta) {
        World world = model.getWorld();

        if (inputAdapter.isJustTouched()) {
            int mouseX = inputAdapter.getMouseX();
            int mouseY = inputAdapter.getMouseY();

            Point coordinates = renderer.globalToLocal(mouseX, mouseY);
            Point cellPosition = world.getCellPosition(coordinates.x, coordinates.y);

            int x = cellPosition.x;
            int y = cellPosition.y;

            if(world.getMeta(x, y) != World.Meta.SOLID) {
                moveTo(x, y);
            } else {
                Point nearestEmptyCell = world.getNearestEmptyCell(World.Layer.META, x, y, 2);

                if(nearestEmptyCell != null) {
                    moveTo(nearestEmptyCell.x, nearestEmptyCell.y);
                }
            }
        }

        if (model.isMoving()) {
            movingUpdate += MOVING_SPEED * delta;

            Point point = null;
            while(model.isMoving() && movingUpdate >= 1f) {
                if (!model.getPath().isEmpty()) {
                    point = model.getPath().remove();
                }

                movingUpdate -= 1f;
            }

            if(point != null) {
                model.getPlayer().setPosition(point.x, point.y);
            }
        }

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
                        (int) (rect.height * (playerX - rect.x) + playerY - rect.y),
                        (int) (rect.height * (x - rect.x) + (y - rect.y))
                ));

        if(path.size() > 1) {
            path.remove();

            model.clearPath();
            model.addPath(path);

            model.setWaypoint(x, y);
        }

        movingUpdate = 0f;
    }
}
