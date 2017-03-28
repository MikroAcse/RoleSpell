package ru.mikroacse.rolespell.model.entities.components.drawable;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import ru.mikroacse.rolespell.model.entities.core.Entity;
import ru.mikroacse.rolespell.model.world.World;

/**
 * Created by MikroAcse on 28.03.2017.
 */
public abstract class TextureDrawableComponent implements DrawableComponent {
    private Texture texture;

    public TextureDrawableComponent(Texture texture) {
        this.texture = texture;
    }

    @Override
    public abstract boolean draw(Entity entity, World world, SpriteBatch batch);

    public Texture getTexture() {
        return texture;
    }

    public void setTexture(Texture texture) {
        this.texture = texture;
    }
}
