package ru.mikroacse.rolespell.app.model.game.entities;

import ru.mikroacse.engine.util.IntVector2;
import ru.mikroacse.engine.util.Interval;
import ru.mikroacse.engine.util.Timer;
import ru.mikroacse.rolespell.app.model.game.entities.components.ai.AttackAi;
import ru.mikroacse.rolespell.app.model.game.entities.components.ai.BehaviorAi;
import ru.mikroacse.rolespell.app.model.game.entities.components.ai.PickupAi;
import ru.mikroacse.rolespell.app.model.game.entities.components.ai.TeleportAi;
import ru.mikroacse.rolespell.app.model.game.entities.components.inventory.InventoryComponent;
import ru.mikroacse.rolespell.app.model.game.entities.components.movement.PathMovementComponent;
import ru.mikroacse.rolespell.app.model.game.entities.components.status.StatusComponent;
import ru.mikroacse.rolespell.app.model.game.entities.components.status.properties.*;
import ru.mikroacse.rolespell.app.model.game.inventory.Inventory;
import ru.mikroacse.rolespell.app.model.game.items.weapons.WoodenSword;
import ru.mikroacse.rolespell.app.model.game.world.World;

import java.util.EnumSet;

/**
 * Created by MikroAcse on 22.03.2017.
 */
public class Player extends Entity {
    private AttackAi attackAi;
    private PickupAi pickupAi;

    private PathMovementComponent movement;
    private InventoryComponent inventory;
    private StatusComponent status;

    public Player(World world) {
        this(world, 0, 0);
    }

    public Player(World world, int x, int y) {
        super(EntityType.PLAYER, world);

        setParameters(EnumSet.of(Parameter.SOLID, Parameter.VULNERABLE));

        // TODO: magic number
        movement = new PathMovementComponent(this, x, y, 8f);
        movement.setType(PathMovementComponent.UpdateType.BOTH);
        addComponent(movement);

        inventory = new InventoryComponent(this, new Inventory(12, 3));
        addComponent(inventory);

        WoodenSword woodenSword = new WoodenSword();
        inventory.getInventory().getItems().addItem(woodenSword);

        status = new StatusComponent(this);
        addComponent(status);

        status.addParameter(new HealthProperty(status,
                new Interval(0, 100, 50),
                3));

        status.addParameter(new ManaProperty(status,
                new Interval(0, 100, 100),
                5));

        status.addParameter(new StaminaProperty(status,
                new Interval(0, 100, 100),
                5));

        status.addParameter(new ExperienceProperty(status,
                new Interval(0, 100, 35)));

        // TODO: looks bad
        status.addParameter(new DamageProperty(
                status,
                new Interval(10.0, 50.0),
                2,
                true));

        attackAi = new AttackAi(this, new Timer(1.0));
        addComponent(attackAi);

        attackAi.setTargetSelectors(EnumSet.of(BehaviorAi.TargetSelector.CUSTOM));

        attackAi.setTargetTypes(EnumSet.of(EntityType.NPC));
        attackAi.setBlacklist(true);

        pickupAi = new PickupAi(this);
        addComponent(pickupAi);

        addComponent(new TeleportAi(this));
    }

    @Override
    public void setPosition(int x, int y) {
        movement.setPosition(x, y);
    }

    @Override
    public IntVector2 getPosition() {
        return movement.getPosition();
    }

    @Override
    public void setOrigin(int x, int y) {
        movement.setOrigin(x, y);
    }

    @Override
    public IntVector2 getOrigin() {
        return movement.getOrigin();
    }
}
