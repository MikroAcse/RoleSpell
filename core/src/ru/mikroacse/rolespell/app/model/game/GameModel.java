package ru.mikroacse.rolespell.app.model.game;

import ru.mikroacse.rolespell.app.model.game.entities.core.Entity;
import ru.mikroacse.rolespell.app.model.game.world.World;

import java.util.List;

/**
 * Created by MikroAcse on 22.03.2017.
 */
public class GameModel {
    private Entity controllable;
    private Entity observable;
    private World world;
    
    public GameModel() {
    
    }
    
    private void initializeWorld() {
        observable = world.getPlayer();
        controllable = world.getPlayer();
    }
    
    public void update(float delta) {
        List<Entity> entities = world.getEntities();
        
        for (Entity entity : entities) {
            entity.update(delta);
        }
    }
    
    public Entity getControllable() {
        return controllable;
    }
    
    public void setControllable(Entity controllable) {
        this.controllable = controllable;
    }
    
    public Entity getObservable() {
        return observable;
    }
    
    public void setObservable(Entity observable) {
        this.observable = observable;
    }
    
    public World getWorld() {
        return world;
    }
    
    public void setWorld(World world) {
        this.world = world;
        
        initializeWorld();
    }
}
