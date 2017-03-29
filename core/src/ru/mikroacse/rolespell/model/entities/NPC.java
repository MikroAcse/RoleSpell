package ru.mikroacse.rolespell.model.entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import ru.mikroacse.rolespell.model.entities.components.ai.AiComponent;
import ru.mikroacse.rolespell.model.entities.components.ai.CollisionAvoidingAi;
import ru.mikroacse.rolespell.model.entities.components.ai.RandomMovementAi;
import ru.mikroacse.rolespell.model.entities.components.drawable.DrawableComponent;
import ru.mikroacse.rolespell.model.entities.components.drawable.TextureDrawableComponent;
import ru.mikroacse.rolespell.model.entities.components.movement.MovementComponent;
import ru.mikroacse.rolespell.model.entities.components.movement.PathMovementComponent;
import ru.mikroacse.rolespell.model.entities.core.*;
import ru.mikroacse.rolespell.model.world.World;
import ru.mikroacse.util.Interval;

import java.util.ArrayList;

/**
 * Created by MikroAcse on 25.03.2017.
 */
public class Npc extends Entity implements GuidedEntity, IntelligentEntity {
    private ArrayList<AiComponent> ai;

    private RandomMovementAi randomMovementAi;
    private CollisionAvoidingAi collisionAvoidingAi;

    private PathMovementComponent movement;
    private TextureDrawableComponent drawable;

    public Npc(int x, int y) {
        super(EntityType.NPC);

        movement = new PathMovementComponent(x, y, 4.0 /*TODO: magic*/);
        movement.setType(PathMovementComponent.UpdateType.CURRENT);

        ai = new ArrayList<>();

        // TODO: magic numbers
        randomMovementAi = new RandomMovementAi(movement, new Interval(2.0, 10.0), 0, 5);
        ai.add(randomMovementAi);

        collisionAvoidingAi = new CollisionAvoidingAi(movement, new Interval(0.5), 1, 5);
        ai.add(collisionAvoidingAi);

        // TODO: It's supposed to be in the View!
        drawable = new TextureDrawableComponent(new Texture("data/npc.png"));
    }

    public Npc() {
        this(0, 0);
    }

    @Override
    public void update(float delta, World world) {
        for (AiComponent aiComponent : ai) {
            aiComponent.update(this, world, delta);
        }

        movement.update(this, world, delta);
    }

    @Override
    public void dispose() {
        randomMovementAi.dispose();
        collisionAvoidingAi.dispose();

        movement.dispose();
        drawable.dispose();
    }

    @Override
    public ArrayList<AiComponent> getAi() {
        return ai;
    }

    @Override
    public PathMovementComponent getMovement() {
        return movement;
    }

    @Override
    public DrawableComponent getDrawable() {
        return drawable;
    }
}
