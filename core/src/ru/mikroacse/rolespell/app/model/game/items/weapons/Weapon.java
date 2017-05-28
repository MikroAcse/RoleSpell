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

    public Weapon(ItemConfig config) {
        super(config);
    }

    @Override
    protected void configure(ItemConfig config) {
        attackTimer = TimerParser.parse(config.getParameter("attack-timer"));
        damage = IntervalParser.parse(config.getParameter("damage"));

        attackDistance = config.getParameter("attack-distance").asDouble();
        limit = config.getParameter("limit").asInt();
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
