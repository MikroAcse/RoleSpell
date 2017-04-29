package ru.mikroacse.engine.actors;

import com.badlogic.gdx.scenes.scene2d.utils.Cullable;
import ru.mikroacse.engine.utils.GroupUtil;

/**
 * Created by MikroAcse on 24.07.2016.
 */
public class Group extends com.badlogic.gdx.scenes.scene2d.Group implements Cullable {
    public Group() {
        super();
    }
    
    /**
     * Not accurate. Doesn't consider rotation, skewing and other stuff.
     */
    public float getRealWidth() {
        return GroupUtil.getWidth(this);
    }
    
    /**
     * Not accurate. Doesn't consider rotation, skewing and other stuff.
     */
    public float getRealHeight() {
        return GroupUtil.getHeight(this);
    }
}
