package ru.mikroacse.rolespell.app.model.game.entities;

import ru.mikroacse.engine.util.Interval;
import ru.mikroacse.engine.util.Timer;
import ru.mikroacse.rolespell.app.model.game.entities.components.ai.AttackAi;
import ru.mikroacse.rolespell.app.model.game.entities.components.ai.PickupAi;
import ru.mikroacse.rolespell.app.model.game.entities.components.inventory.InventoryComponent;
import ru.mikroacse.rolespell.app.model.game.entities.components.movement.PathMovementComponent;
import ru.mikroacse.rolespell.app.model.game.entities.components.status.StatusComponent;
import ru.mikroacse.rolespell.app.model.game.entities.components.status.parameters.*;
import ru.mikroacse.rolespell.app.model.game.inventory.Inventory;
import ru.mikroacse.rolespell.app.model.game.items.weapons.WoodenSword;
import ru.mikroacse.rolespell.app.model.game.world.World;

/**
 * Created by MikroAcse on 22.03.2017.
 */
public class Player extends Entity {
    private AttackAi attackAi;
    private PickupAi pickupAi;

    private PathMovementComponent movement;
    private InventoryComponent inventory;
    private StatusComponent status;

    public Player(World world, int x, int y) {
        super(EntityType.PLAYER, world);

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

        status.addParameter(new HealthParameter(status,
                new Interval(0, 100, 50),
                3));

        status.addParameter(new ManaParameter(status,
                new Interval(0, 100, 100),
                5));

        status.addParameter(new StaminaParameter(status,
                new Interval(0, 100, 100),
                5));

        status.addParameter(new ExperienceParameter(status,
                new Interval(0, 100, 35)));

        // TODO: looks bad
        status.addParameter(new DamageParameter(
                status,
                new Interval(1.0, 5.0),
                2,
                true) {
            @Override
            public boolean bump(Entity entity) {
                if (entity.getType() == EntityType.NPC) {
                    return super.bump(entity);
                }
                return false;
            }
        });

        attackAi = new AttackAi(this, new Timer(3.0));
        addComponent(attackAi);

        pickupAi = new PickupAi(this);
        addComponent(pickupAi);
    }

    public Player(World world) {
        this(world, 0, 0);
    }

    @Override
    public void dispose() {
        attackAi.dispose();
        pickupAi.dispose();

        movement.dispose();
        status.dispose();
    }
}
