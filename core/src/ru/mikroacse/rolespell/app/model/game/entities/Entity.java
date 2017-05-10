package ru.mikroacse.rolespell.app.model.game.entities;

import com.badlogic.gdx.utils.Array;
import ru.mikroacse.rolespell.app.model.game.entities.components.Component;
import ru.mikroacse.rolespell.app.model.game.world.World;

import java.util.Observable;

/**
 * Created by MikroAcse on 23.03.2017.
 */
public abstract class Entity extends Observable {
    private World world;
    private EntityType type;

    private String name;

    private Array<Component> components;

    public Entity(EntityType type, World world) {
        this.type = type;
        this.world = world;

        components = new Array<>();
    }

    /**
     * Updates all entity's components.
     */
    public void update(float delta) {
        for (int i = components.size - 1; i >= 0; i--) {
            components.get(i).update(delta);
        }
    }

    /**
     * Removes entity from its world.
     */
    public void remove() {
        world.removeEntity(this);
    }

    public abstract void dispose();

    public void addComponent(Component component) {
        components.add(component);
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
    public <T extends Component> Array<T> getComponents(Class<T> componentClass) {
        Array<T> result = new Array<T>();

        for (Component component : components) {
            if (componentClass.isInstance(component)) {
                result.add((T) component);
            }
        }

        return result;
    }

    public EntityType getType() {
        return type;
    }

    public World getWorld() {
        return world;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
