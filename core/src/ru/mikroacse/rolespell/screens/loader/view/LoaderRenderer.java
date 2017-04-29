package ru.mikroacse.rolespell.screens.loader.view;

import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenCallback;
import aurelienribon.tweenengine.equations.Expo;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Stage;
import ru.mikroacse.engine.actors.AnimationActor;
import ru.mikroacse.engine.listeners.ListenerSupport;
import ru.mikroacse.engine.listeners.ListenerSupportFactory;
import ru.mikroacse.engine.tween.ActorAccessor;
import ru.mikroacse.rolespell.RoleSpell;
import ru.mikroacse.rolespell.media.AssetManager;
import ru.mikroacse.util.AnimationUtil;

/**
 * Created by MikroAcse on 14.07.2016.
 */
public class LoaderRenderer extends Stage {
    private AnimationActor animation;
    
    private Listener listeners;
    
    private boolean busy;
    
    public LoaderRenderer() {
        listeners = ListenerSupportFactory.create(Listener.class);
        
        animation = new AnimationActor(
                AnimationUtil.create(
                        AssetManager.Bundle.LOADER,
                        "loader",
                        false
                )
        );
        
        animation.setFrameDuration(getAnimationFrameDuration());
        animation.setRepeatable(true);
        
        addActor(animation);
        
        busy = false;
    }
    
    public void addListener(Listener listener) {
        ((ListenerSupport<Listener>) listeners).addListener(listener);
    }
    
    public void removeListener(Listener listener) {
        ((ListenerSupport<Listener>) listeners).removeListener(listener);
    }
    
    public void clearListeners() {
        ((ListenerSupport<Listener>) listeners).clearListeners();
    }
    
    @Override
    public void draw() {
        Gdx.gl.glClearColor(1f, 1f, 1f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        
        animation.setScale(getAnimationScale());
    
        animation.setX((int) (getWidth() - animation.getRealWidth()) / 2);
        animation.setY((int) (getHeight() - animation.getRealHeight()) / 2);
        
        super.draw();
    }
    
    public void show() {
        RoleSpell.getTweenManager().killTarget(animation);
        
        Tween.to(animation, ActorAccessor.ALPHA, 0.2f)
             .ease(Expo.OUT)
             .target(1f)
             .setCallback((type, source) -> {
                 if (type == TweenCallback.COMPLETE) {
                     busy = false;
                 }
             })
             .start(RoleSpell.getTweenManager());
        
        animation.getColor().a = 1;
        
        busy = true;
    }
    
    public void hide() {
        RoleSpell.getTweenManager().killTarget(animation);
        
        Tween.to(animation, ActorAccessor.ALPHA, 0.2f)
             .ease(Expo.IN)
             .target(0f)
             .setCallback((type, source) -> {
                 if (type == TweenCallback.COMPLETE) {
                     busy = false;
                     listeners.onHidden();
                 }
             })
             .start(RoleSpell.getTweenManager());
        
        busy = true;
    }
    
    private float getAnimationFrameDuration() {
        return 1 / 20f;
    }
    
    private float getAnimationScale() {
        return 5f * RoleSpell.getAssetManager().getScale();
    }
    
    public boolean isBusy() {
        return busy;
    }
    
    public interface Listener extends ru.mikroacse.engine.listeners.Listener {
        void onHidden();
    }
}
