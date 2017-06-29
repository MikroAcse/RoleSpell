package ru.mikroacse.rolespell.app.view.game.entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import com.badlogic.gdx.utils.Pool;
import ru.mikroacse.rolespell.app.model.game.entities.Entity;
import ru.mikroacse.rolespell.app.model.game.entities.objects.DroppedItem;
import ru.mikroacse.rolespell.app.model.game.items.Item;
import ru.mikroacse.rolespell.app.view.game.items.ItemView;
import ru.mikroacse.rolespell.media.AssetBundle;
import ru.mikroacse.rolespell.media.Bundle;

import static ru.mikroacse.rolespell.RoleSpell.bundle;

/**
 * Created by MikroAcse on 10-May-17.
 */
public class EntityView extends Image implements Pool.Poolable {
    private Entity entity;

    public EntityView(Entity entity) {
        super(getEntityTexture(entity));

        this.entity = entity;
    }

    public EntityView() {
        super();
    }

    public static Texture getEntityTexture(Entity entity) {
        AssetBundle bundle = bundle(Bundle.GAME);

        switch (entity.getType()) {
            case NPC:
                return bundle.getTexture("entities/npc");
            case PLAYER:
                return bundle.getTexture("entities/player");
            case DEVIL:
                return bundle.getTexture("entities/monsters/devil");
            case OGREMAGI:
                return bundle.getTexture("entities/monsters/ogremagi");
            case PORTAL:
                return bundle.getTexture("entities/objects/portal");
            case DROPPED_ITEM:
                Item item = ((DroppedItem) entity).getItem();

                return ItemView.getItemTexture(item);
        }

        return null;
    }

    @Override
    public void reset() {
        remove();

        setEntity(null);
    }

    public Entity getEntity() {
        return entity;
    }

    public void setEntity(Entity entity) {
        if (this.entity == entity) {
            return;
        }

        this.entity = entity;

        if (entity != null) {
            Texture texture = getEntityTexture(entity);

            setDrawable(new SpriteDrawable(new Sprite(texture)));

            setSize(texture.getWidth(), texture.getHeight());
        } else {
            setDrawable(null);
        }
    }
}
