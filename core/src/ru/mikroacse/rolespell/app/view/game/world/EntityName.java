package ru.mikroacse.rolespell.app.view.game.world;

import com.badlogic.gdx.scenes.scene2d.Group;
import ru.mikroacse.engine.actors.RealActor;
import ru.mikroacse.engine.actors.TextActor;

/**
 * Created by MikroAcse on 07-May-17.
 */
public class EntityName extends Group implements RealActor {
    private TextActor text;

    public EntityName() {
        super();
    }

    @Override
    public float getRealWidth() {
        return 0;
    }

    @Override
    public float getRealHeight() {
        return 0;
    }
}
