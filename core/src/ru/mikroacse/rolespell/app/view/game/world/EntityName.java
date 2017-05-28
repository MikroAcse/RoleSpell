package ru.mikroacse.rolespell.app.view.game.world;

import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import com.badlogic.gdx.utils.Pool;
import ru.mikroacse.engine.actors.MeasurableActor;
import ru.mikroacse.engine.actors.TextActor;
import ru.mikroacse.rolespell.RoleSpell;
import ru.mikroacse.rolespell.app.model.game.entities.Entity;
import ru.mikroacse.rolespell.media.AssetManager;

/**
 * Created by MikroAcse on 07-May-17.
 */
public class EntityName extends Group implements MeasurableActor, Pool.Poolable {
    private TextActor text;
    private Button background;

    private Entity entity;

    public EntityName() {
        this(null);
    }

    public EntityName(Entity entity) {
        super();

        // TODO: ↓
        NinePatchDrawable npd = new NinePatchDrawable(new NinePatch(RoleSpell.getAssetManager()
                .getBundle(AssetManager.Bundle.GAME)
                .getTexture("ui/entity-name-background"), 9, 9, 9, 9));

        background = new Button(npd);
        background.setTouchable(Touchable.disabled);

        text = new TextActor(RoleSpell.getAssetManager()
                .getBundle(AssetManager.Bundle.GLOBAL).getFont("cg-24"));

        addActor(background);
        addActor(text);

        setEntity(entity);

        // TODO: normal ui scaling
        setScale(0.5f);
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
        this.entity = entity;

        if (entity == null) {
            text.setText("");
            return;
        }

        text.setText(RoleSpell.getLang().get(entity.getName()));

        // TODO: magic numbers
        background.setWidth(text.getRealWidth() + 10);
        background.setHeight(text.getRealHeight() + 10);
        text.setX(5);
        text.setY(5);
    }

    @Override
    public float getRealWidth() {
        return background.getWidth() * getScaleX();
    }

    @Override
    public float getRealHeight() {
        return background.getHeight() * getScaleY();
    }
}
