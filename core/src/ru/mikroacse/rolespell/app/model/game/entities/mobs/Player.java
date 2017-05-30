package ru.mikroacse.rolespell.app.model.game.entities.mobs;

import ru.mikroacse.engine.util.Interval;
import ru.mikroacse.engine.util.Timer;
import ru.mikroacse.rolespell.app.model.game.entities.EntityType;
import ru.mikroacse.rolespell.app.model.game.entities.components.ai.AttackAi;
import ru.mikroacse.rolespell.app.model.game.entities.components.ai.BehaviorAi;
import ru.mikroacse.rolespell.app.model.game.entities.components.ai.PickupAi;
import ru.mikroacse.rolespell.app.model.game.entities.components.ai.TeleportAi;
import ru.mikroacse.rolespell.app.model.game.entities.components.inventory.InventoryComponent;
import ru.mikroacse.rolespell.app.model.game.entities.components.status.properties.*;
import ru.mikroacse.rolespell.app.model.game.inventory.Inventory;
import ru.mikroacse.rolespell.app.model.game.world.World;

import java.util.EnumSet;

/**
 * Created by MikroAcse on 22.03.2017.
 */
public class Player extends Mob {
    private AttackAi attackAi;
    private PickupAi pickupAi;

    private InventoryComponent inventory;

    public Player(World world, int x, int y) {
        super(EntityType.PLAYER, world, null, x, y, 8f);

        getParameters().add(Parameter.VULNERABLE);

        getStatus().addProperty(new HealthProperty(getStatus(),
                new Interval(0, 100, 50),
                3));

        getStatus().addProperty(new ManaProperty(getStatus(),
                new Interval(0, 100, 100),
                5));

        getStatus().addProperty(new StaminaProperty(getStatus(),
                new Interval(0, 100, 100),
                5));

        getStatus().addProperty(new ExperienceProperty(getStatus(),
                new Interval(0, 100, 35)));

        // TODO: looks bad
        getStatus().addProperty(new DamageProperty(
                getStatus(),
                new Interval(10.0, 50.0),
                2));

        attackAi = new AttackAi(this, new Timer(1.0));
        addComponent(attackAi);

        attackAi.setTargetSelectors(EnumSet.of(BehaviorAi.TargetSelector.CUSTOM));

        attackAi.setTargetTypes(EnumSet.of(EntityType.NPC));
        attackAi.setBlacklist(true);

        pickupAi = new PickupAi(this);
        addComponent(pickupAi);

        addComponent(new TeleportAi(this));

        // TODO: post init?
        getController().setEntity(this);
    }
}
