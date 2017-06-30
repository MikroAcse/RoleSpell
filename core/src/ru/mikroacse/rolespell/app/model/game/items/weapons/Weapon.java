package ru.mikroacse.rolespell.app.model.game.items.weapons;

import ru.mikroacse.engine.util.Interval;
import ru.mikroacse.engine.util.Timer;
import ru.mikroacse.rolespell.app.model.game.items.Item;
import ru.mikroacse.rolespell.app.model.game.items.config.ItemConfig;
import ru.mikroacse.rolespell.parsers.IntervalParser;
import ru.mikroacse.rolespell.parsers.TimerParser;

/**
 * Created by Vitaly Rudenko on 28-May-17.
 */
public class Weapon extends Item {
    private Timer attackTimer;

    private Interval damage;

    private double attackDistance;
    private int limit;

    public Weapon() {
        super();
    }

    @Override
    public void setConfig(ItemConfig config) {
        super.setConfig(config);

        attackTimer = TimerParser.parse(config.get("attack-timer"));
        damage = IntervalParser.parse(config.get("damage"));

        attackDistance = (double) config.get("attack-distance");
        limit = (int) config.get("limit");
    }

    public Timer getAttackTimer() {
        return attackTimer;
    }

    public Interval getDamage() {
        return damage;
    }

    public double getAttackDistance() {
        return attackDistance;
    }

    public int getLimit() {
        return limit;
    }
}
