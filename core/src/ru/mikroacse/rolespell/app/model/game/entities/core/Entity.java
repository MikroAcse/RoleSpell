package ru.mikroacse.rolespell.app.model.game.entities.core;

import ru.mikroacse.rolespell.app.model.game.entities.EntityType;
import ru.mikroacse.rolespell.app.model.game.entities.components.Component;
import ru.mikroacse.rolespell.app.model.game.world.World;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;

/**
 * Created by MikroAcse on 23.03.2017.
 */
public abstract class Entity extends Observable {
    private World world;
    private EntityType type;

    private List<Component> components;

    public Entity(EntityType type, World world) {
        this.type = type;
        this.world = world;

        components = new ArrayList<>();
    }

    public Entity() {
        this(null, null);
    }

    /**
     * Updates all entity components.
     */
    public void update(float delta) {
        for (Component component : components) {
            component.update(delta);
        }
    }

    public abstract void dispose();

    public EntityType getType() {
        return type;
    }

    public World getWorld() {
        return world;
    }

    public boolean addComponent(Component component) {
        return components.add(component);
    }

    /**
     * @return True if entity has at least one component of given class.
     */
    public boolean hasComponent(Class<? extends Component> componentClass) {
        for (Component component : components) {
            if (componentClass.isInstance(component)) {
                return true;
            }
        }
        return false;
    }

    /**
     * @return First matching entity component of given class.
     */
    public <T extends Component> T getComponent(Class<T> componentClass) {
        for (Component component : components) {
            if (componentClass.isInstance(component)) {
                return (T) component;
            }
        }

        return null;
    }

    /**
     * @return All entity components of given class.
     */
    public <T extends Component> List<T> getComponents(Class<T> componentClass) {
        List<T> result = new ArrayList<T>();

        for (Component component : components) {
            if (componentClass.isInstance(component)) {
                result.add((T) component);
            }
        }

        return result;
    }
}
