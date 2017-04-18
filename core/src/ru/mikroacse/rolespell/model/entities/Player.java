package ru.mikroacse.rolespell.model.entities;

import com.badlogic.gdx.graphics.Texture;
import ru.mikroacse.rolespell.model.entities.components.drawable.DrawableComponent;
import ru.mikroacse.rolespell.model.entities.components.drawable.TextureDrawableComponent;
import ru.mikroacse.rolespell.model.entities.components.movement.PathMovementComponent;
import ru.mikroacse.rolespell.model.entities.components.status.StatusComponent;
import ru.mikroacse.rolespell.model.entities.components.status.parameters.ExperienceParameter;
import ru.mikroacse.rolespell.model.entities.components.status.parameters.HealthParameter;
import ru.mikroacse.rolespell.model.entities.components.status.parameters.ManaParameter;
import ru.mikroacse.rolespell.model.entities.components.status.parameters.StaminaParameter;
import ru.mikroacse.rolespell.model.entities.core.Entity;
import ru.mikroacse.rolespell.model.world.World;

/**
 * Created by MikroAcse on 22.03.2017.
 */
public class Player extends Entity {
    private PathMovementComponent movement;
    private DrawableComponent drawable;
    private StatusComponent status;

    public Player(World world, int x, int y) {
        super(EntityType.PLAYER, world);

        // TODO: magic number
        movement = new PathMovementComponent(this, x, y, 8f);
        movement.setType(PathMovementComponent.UpdateType.BOTH);
        addComponent(movement);

        status = new StatusComponent(this);
        status.add(new HealthParameter(status));
        status.add(new ManaParameter(status));
        status.add(new StaminaParameter(status));
        status.add(new ExperienceParameter(status));
        addComponent(status);

        // TODO: Is it supposed to be in the View?!
        drawable = new TextureDrawableComponent(this, new Texture("data/player.png"));
        addComponent(drawable);
    }

    public Player(World world) {
        this(world, 0, 0);
    }

    @Override
    public void dispose() {
        movement.dispose();
        drawable.dispose();
        status.dispose();
    }
}
