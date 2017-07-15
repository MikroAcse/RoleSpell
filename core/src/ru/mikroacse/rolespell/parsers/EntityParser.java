package ru.mikroacse.rolespell.parsers;

import ru.mikroacse.engine.config.ConfigurationNode;
import ru.mikroacse.rolespell.app.model.game.entities.Entity;
import ru.mikroacse.rolespell.app.model.game.entities.EntityRepository;
import ru.mikroacse.rolespell.app.model.game.entities.EntityType;
import ru.mikroacse.rolespell.app.model.game.entities.components.inventory.InventoryComponent;
import ru.mikroacse.rolespell.app.model.game.entities.config.EntityConfig;
import ru.mikroacse.rolespell.app.model.game.inventory.Inventory;
import ru.mikroacse.rolespell.app.model.game.world.World;

import java.util.Map;

/**
 * Created by MikroAcse on 29.06.2017.
 */
public class EntityParser {
    public static Entity parse(Object value, EntityRepository repository, World world, int x, int y) {
        EntityConfig entityConfig;

        if (value instanceof String) {
            entityConfig = repository.get((String) value);

        } else if (value instanceof Map) {
            Map map = (Map) value;

            entityConfig = new EntityConfig(map, (String) map.get("parent"));

        } else if (value instanceof ConfigurationNode) {
            ConfigurationNode node = (ConfigurationNode) value;

            entityConfig = new EntityConfig(node.getMap(), node.get("parent", null));
        } else {
            throw new IllegalArgumentException("Can parse only strings or nodes.");
        }

        return create(entityConfig, world, x, y);
    }

    public static Entity create(EntityConfig config, World world, int x, int y) {
        EntityType type = config.getType(EntityType.ENTITY);

        Entity entity = EntityType.create(type, world, x, y);

        entity.setConfig(config);

        // TODO: magic strings
        if (config.contains("inventory") && entity.hasComponent(InventoryComponent.class)) {
            Inventory inventory = entity.getComponent(InventoryComponent.class).getInventory();

            InventoryParser.parse(config.extractNodeList("inventory"), inventory);
        }

        return entity;
    }
}
