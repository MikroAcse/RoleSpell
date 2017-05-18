package ru.mikroacse.rolespell.app.model.game.entities.components.ai.behaviors;

import com.badlogic.gdx.utils.Array;
import ru.mikroacse.engine.util.Priority;
import ru.mikroacse.rolespell.RoleSpell;
import ru.mikroacse.rolespell.app.model.game.entities.Entity;
import ru.mikroacse.rolespell.app.model.game.entities.EntityType;
import ru.mikroacse.rolespell.app.model.game.entities.objects.Portal;
import ru.mikroacse.rolespell.app.model.game.world.Map;
import ru.mikroacse.rolespell.app.model.game.world.World;
import ru.mikroacse.rolespell.app.screens.GameScreen;
import ru.mikroacse.rolespell.app.screens.ScreenManager;
import ru.mikroacse.rolespell.media.AssetManager;

import java.util.EnumSet;

/**
 * Created by Vitaly Rudenko on 17-May-17.
 */
public class TeleportBehavior extends Behavior {
    private Portal portal;

    public TeleportBehavior() {
        super(Priority.IMMEDIATELY, true, EnumSet.of(Behavior.Trigger.MOVEMENT));
    }

    @Override
    public boolean process(Entity entity, Array<Entity> targets) {
        if (targets.size == 0) {
            return false;
        }

        World world = entity.getWorld();

        for (Entity target : targets) {
            if(target.getType() != EntityType.PORTAL) {
                continue;
            }

            Portal portal = (Portal) target;

            if(portal.isSpawn()) {
                continue;
            }

            this.portal = portal;

            return true;
        }

        return false;
    }

    public Portal getPortal() {
        return portal;
    }
}
