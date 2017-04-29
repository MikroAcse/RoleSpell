package ru.mikroacse.rolespell.screens.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.maps.tiled.TiledMap;
import ru.mikroacse.rolespell.RoleSpell;
import ru.mikroacse.rolespell.media.AssetManager;
import ru.mikroacse.rolespell.screens.game.controller.GameController;
import ru.mikroacse.rolespell.screens.game.model.GameModel;
import ru.mikroacse.rolespell.screens.game.model.world.World;
import ru.mikroacse.rolespell.screens.game.view.GameRenderer;


/**
 * Created by MikroAcse on 22.03.2017.
 */
public class GameScreen implements Screen {
    private GameRenderer renderer;
    private GameController controller;
    
    private GameModel model;
    
    private Game game;
    
    public GameScreen(Game game) {
        this.game = game;
        
        model = new GameModel();
        
        TiledMap map = RoleSpell.getAssetManager()
                                .getBundle(AssetManager.Bundle.GAME)
                                .getMap("rottenville");
        
        World world = new World(map);
        
        model.setWorld(world);
        
        renderer = new GameRenderer(model);
        controller = new GameController(renderer, model);
    }
    
    @Override
    public void show() {
    
    }
    
    @Override
    public void render(float delta) {
        controller.update(delta);
        renderer.render(delta);
    }
    
    @Override
    public void resize(int width, int height) {
        renderer.resize(width, height);
    }
    
    @Override
    public void pause() {
    
    }
    
    @Override
    public void resume() {
    
    }
    
    @Override
    public void hide() {
    
    }
    
    @Override
    public void dispose() {
    
    }
}