package ru.mikroacse.rolespell.app.model.game.entities.components.ai;

import ru.mikroacse.engine.util.Timer;
import ru.mikroacse.rolespell.RoleSpell;
import ru.mikroacse.rolespell.app.model.game.entities.Entity;
import ru.mikroacse.rolespell.app.model.game.entities.components.ai.behaviors.Behavior;
import ru.mikroacse.rolespell.app.model.game.entities.components.ai.behaviors.TeleportBehavior;
import ru.mikroacse.rolespell.app.screens.GameScreen;
import ru.mikroacse.rolespell.app.screens.ScreenManager;

import java.util.EnumSet;

/**
 * Created by Vitaly Rudenko on 18-May-17.
 */
public class TeleportAi extends BehaviorAi {
    private TeleportBehavior teleportBehavior;

    public TeleportAi(Entity entity) {
        super(entity, 0);

        // one teleport at a time
        setMaxTargets(1);

        teleportBehavior = new TeleportBehavior();
        addBehavior(teleportBehavior);
    }

    @Override
    public boolean process(EnumSet<Behavior.Trigger> triggers, Timer timer) {
        boolean result = super.process(triggers, timer);

        if (result) {
            // TODO: beautify
            System.out.println("teleporting to " + teleportBehavior.getPortal().getDestination());

            ((GameScreen) RoleSpell.screens().getScreen(ScreenManager.BundledScreen.GAME))
                    .setWorld(teleportBehavior.getPortal().getDestination(), teleportBehavior.getPortal().getId());
        }

        return result;
    }

    public TeleportBehavior getTeleportBehavior() {
        return teleportBehavior;
    }
}
