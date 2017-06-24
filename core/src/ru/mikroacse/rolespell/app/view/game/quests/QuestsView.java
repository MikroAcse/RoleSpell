package ru.mikroacse.rolespell.app.view.game.quests;

import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.Array;
import ru.mikroacse.engine.util.GroupUtil;

/**
 * Created by Vitaly Rudenko on 07-Jun-17.
 */
public class QuestsView extends Group {
    private Array<QuestView> questViews;
    private Group questsContainer;

    private float scroll;

    private Image background;

    public QuestsView() {
        super();

        Pixmap pm = new Pixmap(10, 10, Pixmap.Format.RGBA8888);
        pm.setColor(0x00000077);
        pm.fill();

        background = new Image(new Texture(pm));

        pm.dispose();

        questViews = new Array<>();
        scroll = 0f;

        questsContainer = new Group();

        addActor(background);
        addActor(questsContainer);

        // TODO: loading quests from model
        questViews.add(new QuestView("Quest 1", "Quest much description"));
        questViews.add(new QuestView("Quest 2", "Quest such description"));
        questViews.add(new QuestView("Quest 3", "Quest very description"));
        questViews.add(new QuestView("Quest 4", "Quest nice description"));
        questViews.add(new QuestView("Quest 5", "Quest wow description"));
        questViews.add(new QuestView("Quest 6", "Quest super description"));
        questViews.add(new QuestView("Quest 7", "Quethte dethcriptione"));
        questViews.add(new QuestView("Quest 8", "Description of something"));
        questViews.add(new QuestView("Quest 9", "H-how did these get here?!"));
        questViews.add(new QuestView("Quest 10", "Cursach takes time, and cursach takes work."));

        questViews.reverse();

        for (QuestView questView : questViews) {
            questsContainer.addActor(questView);
        }

        update();
    }

    public void update() {
        scroll = MathUtils.clamp(scroll, -15, GroupUtil.getHeight(questsContainer) - getHeight() + 15);

        float y = 0;
        for (QuestView questView : questViews) {
            // TODO: magic numbers
            questView.setHeight(100f);

            questView.setX((int) (-questView.getRealWidth() / 2));
            questView.setY((int) y);

            // TODO: magic number (offset between quests)
            y += questView.getRealHeight() + 15;
        }

        float height = GroupUtil.getHeight(questsContainer);

        questsContainer.setX((int) (getWidth() / 2));
        questsContainer.setY((int) (getHeight() - height + scroll));

        background.setWidth((int) getWidth());
        background.setHeight((int) getHeight());
    }

    public void scrollBy(float value) {
        setScroll(scroll + value);
    }

    public float getScroll() {
        return scroll;
    }

    public void setScroll(float scroll) {
        this.scroll = scroll;

        update();
    }
}
