package ru.mikroacse.rolespell.screens.loader;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Screen;
import ru.mikroacse.rolespell.RoleSpell;
import ru.mikroacse.rolespell.screens.loader.controller.LoaderController;
import ru.mikroacse.rolespell.screens.loader.view.LoaderRenderer;

/**
 * Created by MikroAcse on 14.07.2016.
 */
public class LoaderScreen implements Screen {
    private LoaderRenderer renderer;
    private LoaderController controller;
    
    public LoaderScreen(Game game) {
        renderer = new LoaderRenderer();
        controller = new LoaderController(renderer);
    }
    
    @Override
    public void show() {
    
    }
    
    @Override
    public void render(float delta) {
        controller.update(delta);
        renderer.draw();
    }
    
    @Override
    public void resize(int width, int height) {
        RoleSpell.getAssetManager().updateScale(width, height);
        
        renderer.getViewport().update(width, height, true);
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
