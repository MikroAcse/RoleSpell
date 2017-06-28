package ru.mikroacse.rolespell.app.view.game.quests;

import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import ru.mikroacse.engine.actors.MeasurableActor;
import ru.mikroacse.engine.actors.TextActor;
import ru.mikroacse.rolespell.media.Bundle;

import static ru.mikroacse.rolespell.RoleSpell.assets;
import static ru.mikroacse.rolespell.RoleSpell.bundle;

/**
 * Created by Vitaly Rudenko on 07-Jun-17.
 */
public class QuestView extends Group implements MeasurableActor {
    private Button background;

    private TextActor title;
    private TextActor description;

    public QuestView(String titleText, String descriptionText) {
        super();

        // TODO: ↓
        NinePatchDrawable npd = new NinePatchDrawable(
                new NinePatch(
                        bundle(Bundle.GAME).getTexture("quests/quest-background"),
                        29, 29, 29, 29
                )
        );

        background = new Button(npd);
        background.setTouchable(Touchable.disabled);

        title = new TextActor(assets().getGlobalFont("cg-24"));

        description = new TextActor(assets().getGlobalFont("cg-24"));

        title.setText(titleText);
        description.setText(descriptionText);

        addActor(background);
        addActor(title);
        addActor(description);

        update();
    }

    private void update() {
        float width = Math.max(title.getRealWidth(), description.getRealWidth());
        float height = background.getHeight();

        // TODO: magic number (padding)
        width += 60;

        background.setWidth(width);

        title.setX((int) (width / 2 - title.getRealWidth() / 2));
        title.setY((int) (height * 3 / 4 - title.getRealHeight() / 2));

        description.setX((int) (width / 2 - description.getRealWidth() / 2));
        description.setY((int) (height / 4 - description.getRealHeight() / 2));
    }

    @Override
    public void setHeight(float height) {
        background.setHeight(height);
        update();
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
