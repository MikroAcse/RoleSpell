package ru.mikroacse.rolespell.model.entities;

import com.badlogic.gdx.graphics.Texture;
import ru.mikroacse.rolespell.model.entities.components.ai.BehaviorAi;
import ru.mikroacse.rolespell.model.entities.components.ai.behaviors.AttackBehavior;
import ru.mikroacse.rolespell.model.entities.components.ai.behaviors.SeekBehavior;
import ru.mikroacse.rolespell.model.entities.components.drawable.DrawableComponent;
import ru.mikroacse.rolespell.model.entities.components.drawable.TextureDrawableComponent;
import ru.mikroacse.rolespell.model.entities.components.movement.PathMovementComponent;
import ru.mikroacse.rolespell.model.entities.components.status.StatusComponent;
import ru.mikroacse.rolespell.model.entities.components.status.parameters.*;
import ru.mikroacse.rolespell.model.entities.core.Entity;
import ru.mikroacse.rolespell.model.world.World;
import ru.mikroacse.util.Interval;
import ru.mikroacse.util.LimitedDouble;
import ru.mikroacse.util.Priority;

/**
 * Created by MikroAcse on 22.03.2017.
 */
public class Player extends Entity {
    private BehaviorAi behaviorAi;

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
        addComponent(status);

        status.addParameter(new HealthParameter(status));
        status.addParameter(new ManaParameter(status));
        status.addParameter(new StaminaParameter(status));
        status.addParameter(new ExperienceParameter(status));

        // TODO: this is bad
        status.addParameter(new DamageParameter(
                status,
                new LimitedDouble(1.0, 5.0),
                2,
                true) {
            @Override
            public boolean bump(Entity entity) {
                if (entity instanceof Npc) {
                    return super.bump(entity);
                }
                return false;
            }
        });

        behaviorAi = new BehaviorAi(this, 20);
        addComponent(behaviorAi);

        behaviorAi.setTargetType(BehaviorAi.Target.CUSTOM);
        behaviorAi.setMaxTargets(1);

        behaviorAi.addBehavior(
                new SeekBehavior(
                        Priority.LOW,
                        new Interval(1.0),
                        2
                )
        );

        behaviorAi.addBehavior(
                new AttackBehavior(
                        Priority.NORMAL,
                        new Interval(3.0)
                )
        );

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
