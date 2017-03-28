package ru.mikroacse.rolespell.controller;

import com.badlogic.gdx.Gdx;
import ru.mikroacse.rolespell.model.GameModel;
import ru.mikroacse.rolespell.model.entities.Player;
import ru.mikroacse.rolespell.model.entities.components.movement.PathMovementComponent;
import ru.mikroacse.rolespell.model.world.World;
import ru.mikroacse.rolespell.view.WorldRenderer;
import ru.mikroacse.util.Position;

import java.util.List;

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
        if (inputAdapter.isJustTouched()) {
            cellTouched();
        }

        model.update(delta);
        inputAdapter.update();
    }

    private void cellTouched() {
        Player player = model.getPlayer();
        World world = model.getWorld();

        player.getMovementComponent().clearPath();

        int mouseX = inputAdapter.getMouseX();
        int mouseY = inputAdapter.getMouseY();

        Position coordinates = renderer.globalToLocal(mouseX, mouseY);
        Position touchedCell = world.getCellPosition(coordinates.x, coordinates.y);

        int x = touchedCell.x;
        int y = touchedCell.y;

        if (world.isValidPosition(touchedCell)) {
            // TODO: bad and magic
            // looking for the nearest passable cells
            List<Position> passableCells = world.getPassableCells(x, y, 0, 10, false);
            Position destination = null;

            // checking passable cells for available paths
            for (Position passableCell : passableCells) {
                if (moveTo(passableCell)) {
                    destination = passableCell;
                    break;
                }
            }

            // no path found to any nearest cell
            if(destination == null) {
                System.out.println("Path not found!");
            }
        }
    }

    public boolean moveTo(Position destination) {
        Player player = model.getPlayer();
        PathMovementComponent movement = player.getMovementComponent();

        if (movement.moveTo(player, model.getWorld(), destination, 10/*TODO: magic*/)) {
            model.getWaypoint().set(destination);
            return true;
        }

        return false;
    }
}
