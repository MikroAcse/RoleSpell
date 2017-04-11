package ru.mikroacse.rolespell.model.entities.components.drawable;

import com.badlogic.gdx.graphics.Texture;
import ru.mikroacse.rolespell.model.entities.core.Entity;

/**
 * Created by MikroAcse on 28.03.2017.
 */
public class TextureDrawableComponent extends DrawableComponent {
    private Texture texture;

    public TextureDrawableComponent(Entity entity, Texture texture) {
        super(entity);
        this.texture = texture;
    }

    @Override
    public void dispose() {
        texture.dispose();
    }

    public Texture getTexture() {
        return texture;
    }

    public void setTexture(Texture texture) {
        this.texture = texture;
    }
}
