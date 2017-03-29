package ru.mikroacse.rolespell.model.entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import ru.mikroacse.rolespell.model.entities.components.drawable.DrawableComponent;
import ru.mikroacse.rolespell.model.entities.components.drawable.TextureDrawableComponent;
import ru.mikroacse.rolespell.model.entities.components.movement.MovementComponent;
import ru.mikroacse.rolespell.model.entities.components.movement.PathMovementComponent;
import ru.mikroacse.rolespell.model.entities.core.*;
import ru.mikroacse.rolespell.model.world.World;

/**
 * Created by MikroAcse on 22.03.2017.
 */
public class Player extends Entity implements GuidedEntity, DrawableEntity {
    private PathMovementComponent movement;
    private DrawableComponent drawable;

    public Player(int x, int y) {
        super(EntityType.PLAYER);

        movement = new PathMovementComponent(x, y, 10.0/*TODO: magic*/);
        movement.setType(PathMovementComponent.UpdateType.BOTH);

        // TODO: It's supposed to be in the View!
        drawable = new TextureDrawableComponent(new Texture("data/player.png")) {
            @Override
            public boolean draw(Entity entity, World world, SpriteBatch batch) {
                if (!(entity instanceof MovableEntity)) {
                    return false;
                }

                MovementComponent movement = ((MovableEntity) entity).getMovement();

                int x = movement.getPosition().x;
                int y = movement.getPosition().y;

                x *= world.getTileWidth();
                y *= world.getTileHeight();

                batch.draw(getTexture(), x, y);

                return true;
            }
        };
    }

    public Player() {
        this(0, 0);
    }

    @Override
    public void update(float delta, World world) {
        movement.update(this, world, delta);
    }

    @Override
    public void dispose() {
        movement.dispose();
        drawable.dispose();
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
