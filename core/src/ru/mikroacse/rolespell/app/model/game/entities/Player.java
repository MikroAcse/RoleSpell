package ru.mikroacse.rolespell.app.model.game.entities;

import ru.mikroacse.rolespell.app.model.game.entities.components.ai.BehaviorAi;
import ru.mikroacse.rolespell.app.model.game.entities.components.ai.behaviors.AttackBehavior;
import ru.mikroacse.rolespell.app.model.game.entities.components.ai.behaviors.SeekBehavior;
import ru.mikroacse.rolespell.app.model.game.entities.components.inventory.InventoryComponent;
import ru.mikroacse.rolespell.app.model.game.entities.components.movement.PathMovementComponent;
import ru.mikroacse.rolespell.app.model.game.entities.components.status.StatusComponent;
import ru.mikroacse.rolespell.app.model.game.entities.components.status.parameters.*;
import ru.mikroacse.rolespell.app.model.game.entities.core.Entity;
import ru.mikroacse.rolespell.app.model.game.inventory.Inventory;
import ru.mikroacse.rolespell.app.model.game.items.ItemStack;
import ru.mikroacse.rolespell.app.model.game.items.weapons.WoodenSword;
import ru.mikroacse.rolespell.app.model.game.world.World;
import ru.mikroacse.engine.util.Interval;
import ru.mikroacse.engine.util.LimitedDouble;
import ru.mikroacse.engine.util.Priority;

/**
 * Created by MikroAcse on 22.03.2017.
 */
public class Player extends Entity {
    private BehaviorAi behaviorAi;
    
    private PathMovementComponent movement;
    private InventoryComponent inventory;
    private StatusComponent status;
    
    public Player(World world, int x, int y) {
        super(EntityType.PLAYER, world);
        
        // TODO: magic number
        movement = new PathMovementComponent(this, x, y, 8f);
        movement.setType(PathMovementComponent.UpdateType.BOTH);
        addComponent(movement);
        
        inventory = new InventoryComponent(this, new Inventory(6, 3, 3));
        addComponent(inventory);
        
        ItemStack<WoodenSword> woodenSword = new ItemStack<>(WoodenSword.class, new WoodenSword());
        inventory.getInventory().setItem(1, 0, woodenSword);
        
        status = new StatusComponent(this);
        addComponent(status);
        
        status.addParameter(new HealthParameter(status));
        status.addParameter(new ManaParameter(status));
        status.addParameter(new StaminaParameter(status));
        status.addParameter(new ExperienceParameter(status));
        
        // TODO: looks bad
        status.addParameter(new DamageParameter(
                status,
                new LimitedDouble(1.0, 5.0),
                2,
                true) {
            @Override
            public boolean bump(Entity entity) {
                if (entity instanceof Npc) {
                    return super.bump(entity);
                }
                return false;
            }
        });
        
        behaviorAi = new BehaviorAi(this, 20);
        addComponent(behaviorAi);
        
        behaviorAi.setTargetType(BehaviorAi.Target.CUSTOM);
        behaviorAi.setMaxTargets(1);
        
        behaviorAi.addBehavior(
                new SeekBehavior(
                        Priority.NORMAL,
                        new Interval(0.2),
                        2
                ) {
                    {
                        this.setTrigger(Trigger.INTERVAL);
                    }
                }
        );
        
        behaviorAi.addBehavior(
                new AttackBehavior(
                        Priority.NORMAL,
                        new Interval(3.0)
                )
        );
    }
    
    public Player(World world) {
        this(world, 0, 0);
    }
    
    @Override
    public void dispose() {
        movement.dispose();
        status.dispose();
    }
}
