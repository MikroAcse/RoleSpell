package ru.mikroacse.rolespell.model.entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import ru.mikroacse.rolespell.model.entities.components.ai.AiComponent;
import ru.mikroacse.rolespell.model.entities.components.ai.RandomMovementAiComponent;
import ru.mikroacse.rolespell.model.entities.components.drawable.DrawableComponent;
import ru.mikroacse.rolespell.model.entities.components.drawable.TextureDrawableComponent;
import ru.mikroacse.rolespell.model.entities.components.movement.MovementComponent;
import ru.mikroacse.rolespell.model.entities.components.movement.PathMovementComponent;
import ru.mikroacse.rolespell.model.entities.core.*;
import ru.mikroacse.rolespell.model.world.World;

/**
 * Created by MikroAcse on 25.03.2017.
 */
public class Npc extends Entity implements GuidedEntity, DrawableEntity, IntelligentEntity {
    private RandomMovementAiComponent aiComponent;
    private PathMovementComponent movementComponent;
    private TextureDrawableComponent drawableComponent;

    public Npc(int x, int y) {
        super(EntityType.NPC);

        movementComponent = new PathMovementComponent(x, y, 5.0 /*TODO: magic*/);
        movementComponent.setType(PathMovementComponent.Type.CURRENT);

        // TODO: magic numbers
        aiComponent = new RandomMovementAiComponent(movementComponent, 5, 10);
        aiComponent.setInterval(2.0);

        // TODO: It's supposed to be in the View!
        drawableComponent = new TextureDrawableComponent(new Texture("data/npc.png")) {
            @Override
            public boolean draw(Entity entity, World world, SpriteBatch batch) {
                if (!(entity instanceof MovableEntity)) {
                    return false;
                }

                MovementComponent movement = ((MovableEntity) entity).getMovementComponent();

                int x = movement.getPosition().x;
                int y = movement.getPosition().y;

                x *= world.getTileWidth();
                y *= world.getTileHeight();

                batch.draw(getTexture(), x, y);

                return true;
            }
        };
    }

    public Npc() {
        this(0, 0);
    }

    @Override
    public void update(float delta, World world) {
        aiComponent.update(this, world, delta);
        movementComponent.update(this, world, delta);
    }

    @Override
    public AiComponent getAiComponent() {
        return aiComponent;
    }

    @Override
    public PathMovementComponent getMovementComponent() {
        return movementComponent;
    }

    @Override
    public DrawableComponent getDrawableComponent() {
        return drawableComponent;
    }
}
