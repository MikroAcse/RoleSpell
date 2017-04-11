package ru.mikroacse.rolespell.model.entities;

import com.badlogic.gdx.graphics.Texture;
import ru.mikroacse.rolespell.model.entities.components.ai.CollisionAvoidingAi;
import ru.mikroacse.rolespell.model.entities.components.ai.RandomMovementAi;
import ru.mikroacse.rolespell.model.entities.components.ai.SimpleBehaviorAi;
import ru.mikroacse.rolespell.model.entities.components.drawable.TextureDrawableComponent;
import ru.mikroacse.rolespell.model.entities.components.movement.PathMovementComponent;
import ru.mikroacse.rolespell.model.entities.components.status.StatusComponent;
import ru.mikroacse.rolespell.model.entities.components.status.parameters.HealthParameter;
import ru.mikroacse.rolespell.model.entities.core.Entity;
import ru.mikroacse.rolespell.model.world.World;
import ru.mikroacse.util.LimitedDouble;

/**
 * Created by MikroAcse on 25.03.2017.
 */
public class Npc extends Entity {
    private RandomMovementAi randomMovementAi;
    private CollisionAvoidingAi collisionAvoidingAi;

    private PathMovementComponent movement;
    private TextureDrawableComponent drawable;
    private StatusComponent status;

    public Npc(World world, int x, int y) {
        super(EntityType.NPC, world);

        // TODO: magic
        movement = new PathMovementComponent(this, x, y, 4.0);
        movement.setType(PathMovementComponent.UpdateType.CURRENT);
        addComponent(movement);

        randomMovementAi = new RandomMovementAi(this, new LimitedDouble(2.0, 10.0), 0, 5, true);
        addComponent(randomMovementAi);

        collisionAvoidingAi = new CollisionAvoidingAi(this, 1, 3, false);
        addComponent(collisionAvoidingAi);

        SimpleBehaviorAi behaviorAi = new SimpleBehaviorAi(this, new LimitedDouble(0.5), SimpleBehaviorAi.Type.FOLLOW, null, 5);
        //addComponent(behaviorAi);

        status = new StatusComponent(this);
        status.add(new HealthParameter(status));
        addComponent(status);

        // TODO: Is it supposed to be in the View?!
        drawable = new TextureDrawableComponent(this, new Texture("data/npc.png"));
        addComponent(drawable);
    }

    public Npc(World world) {
        this(world, 0, 0);
    }

    @Override
    public void dispose() {
        randomMovementAi.dispose();
        collisionAvoidingAi.dispose();

        movement.dispose();
        drawable.dispose();
        status.dispose();
    }
}
