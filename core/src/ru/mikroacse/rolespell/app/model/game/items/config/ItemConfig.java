package ru.mikroacse.rolespell.app.model.game.items.config;

import com.badlogic.gdx.utils.JsonValue;
import ru.mikroacse.engine.config.Configuration;
import ru.mikroacse.rolespell.app.model.game.items.ItemType;

/**
 * Created by Vitaly Rudenko on 28-May-17.
 */
public class ItemConfig {
    ItemConfig parent;

    String name;
    ItemType type;
    String texture;

    boolean throwable;
    boolean pickable;

    Configuration parameters;

    ItemConfig() {

    }

    public JsonValue getParameter(String key) {
        ItemConfig config = this;

        while (config.getParameters() != null && !config.getParameters().has(key)) {
            config = config.parent;

            if (config == null) {
                return null;
            }
        }

        return config.getParameters().getNode(key);
    }

    public String getName() {
        return name;
    }

    public ItemType getType() {
        return type;
    }

    public String getTexture() {
        return texture;
    }

    public boolean isThrowable() {
        return throwable;
    }

    public boolean isPickable() {
        return pickable;
    }

    public Configuration getParameters() {
        return parameters;
    }

    public ItemConfig getParent() {
        return parent;
    }

    public ItemConfig clone() {
        ItemConfig itemConfig = new ItemConfig();

        itemConfig.name = name;
        itemConfig.type = type;
        itemConfig.texture = texture;
        itemConfig.throwable = throwable;
        itemConfig.pickable = pickable;
        itemConfig.parent = parent;
        itemConfig.parameters = parameters;

        return itemConfig;
    }
}