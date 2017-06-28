package ru.mikroacse.rolespell.app.model.game.entities;

import com.badlogic.gdx.utils.Array;
import ru.mikroacse.engine.config.ConfigurationNode;
import ru.mikroacse.engine.util.IntVector2;
import ru.mikroacse.rolespell.app.model.game.entities.components.Component;
import ru.mikroacse.rolespell.app.model.game.entities.config.EntityConfig;
import ru.mikroacse.rolespell.app.model.game.world.World;

import java.util.EnumSet;

/**
 * Created by MikroAcse on 23.03.2017.
 */
public abstract class Entity {
    private World world;
    private EntityType type;

    private String id;

    private String name;

    private EntityConfig config;

    private Array<Component> components;
    private EnumSet<Parameter> parameters;

    public Entity(EntityType type, World world) {
        this(type, world, null);
    }

    public Entity(EntityType type, World world, String name) {
        this.type = type;
        this.world = world;
        this.name = name;

        components = new Array<>();
        parameters = Parameter.NONE;

        preInit();
    }

    protected void preInit() {

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

    public void dispose() {
        for (Component component : components) {
            component.dispose();
        }
    }

    public boolean addComponent(Component component) {
        if (component.isSingle()) {
            if (hasComponent(component.getClass())) {
                System.err.println("Trying to add more than one instance of a single component to an entity.");
                return false;
            }
        }

        components.add(component);
        return true;
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

    public EnumSet<Parameter> getParameters() {
        return parameters;
    }

    public void setParameters(EnumSet<Parameter> parameters) {
        this.parameters = parameters;
    }

    public boolean hasParameter(Parameter parameter) {
        return parameters.contains(parameter);
    }

    public boolean hasParameters(EnumSet<Parameter> parameters) {
        for (Parameter parameter : parameters) {
            if (!parameters.contains(parameter)) {
                return false;
            }
        }
        return true;
    }

    public EntityType getType() {
        return type;
    }

    public World getWorld() {
        return world;
    }

    public void setWorld(World world) {
        this.world = world;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public abstract void setPosition(int x, int y);

    public abstract IntVector2 getPosition();

    public void setPosition(IntVector2 position) {
        setPosition(position.x, position.y);
    }

    public int getX() {
        return getPosition().x;
    }

    public int getY() {
        return getPosition().y;
    }

    public abstract void setOrigin(int x, int y);

    public abstract IntVector2 getOrigin();

    public void setOrigin(IntVector2 position) {
        setOrigin(position.x, position.y);
    }

    public EntityConfig getConfig() {
        return config;
    }

    public void setConfig(EntityConfig config) {
        this.config = config;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName() + "{" +
                "world=" + world +
                ", type=" + type +
                ", id=" + id +
                ", name='" + name + '\'' +
                '}';
    }

    public enum Parameter {
        SOLID, // TODO: collisions not allowed
        VULNERABLE; // can be attacked

        public static final EnumSet<Parameter> NONE = EnumSet.noneOf(Parameter.class);
    }
}
