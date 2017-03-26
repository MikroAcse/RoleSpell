package ru.mikroacse.rolespell.controller;

import com.badlogic.gdx.Gdx;
import ru.mikroacse.rolespell.model.GameModel;
import ru.mikroacse.rolespell.model.world.World;
import ru.mikroacse.rolespell.view.WorldRenderer;

import java.awt.*;
import java.util.ArrayList;
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
            int mouseX = inputAdapter.getMouseX();
            int mouseY = inputAdapter.getMouseY();

            Point coordinates = renderer.globalToLocal(mouseX, mouseY);
            Point cellPosition = world.getCellPosition(coordinates.x, coordinates.y);

            int x = cellPosition.x;
            int y = cellPosition.y;

            if(world.isValidCoordinates(x, y)) {
                if (world.getMeta(x, y) != World.Meta.SOLID) {
                    moveTo(x, y);
                } else {
                    // TODO: bad
                    ArrayList<Point> passableCells = world.getPassableCells(x, y, 0, 10, false);

                    if (!passableCells.isEmpty()) {
                        moveTo(passableCells.get(0).x, passableCells.get(0).y);
                    }
                }
            }
        }

        model.update(delta);

        inputAdapter.update();
    }

    public void moveTo(int x, int y) {
        int playerX = model.getPlayer().x;
        int playerY = model.getPlayer().y;

        LinkedList<Point> path = model.getWorld().getPath(new Point(playerX, playerY), new Point(x, y), 5);

        if (path.size() > 1) {
            path.remove();

            model.getPlayer().setPath(path);

            model.setWaypoint(x, y);
        }
    }
}
