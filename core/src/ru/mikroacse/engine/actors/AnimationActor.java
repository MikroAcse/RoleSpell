package ru.mikroacse.engine.actors;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import ru.mikroacse.engine.listeners.ListenerSupport;
import ru.mikroacse.engine.listeners.ListenerSupportFactory;

/**
 * Created by MikroAcse on 14.07.2016.
 */
public class AnimationActor extends Image {
    protected Animation<TextureRegion> animation;
    protected TextureRegionDrawable drawable;
    protected boolean paused;
    protected AnimationListener listeners;
    
    protected float stateTime = 0;
    
    public AnimationActor(Animation<TextureRegion> animation) {
        super(animation.getKeyFrame(0));
        listeners = ListenerSupportFactory.create(AnimationListener.class);
        
        drawable = new TextureRegionDrawable(animation.getKeyFrame(0));
        this.setDrawable(drawable);
        this.animation = animation;
    }
    
    public void addListener(AnimationListener listener) {
        ((ListenerSupport<AnimationListener>) listeners).addListener(listener);
    }
    
    public void removeListener(AnimationListener listener) {
        ((ListenerSupport<AnimationListener>) listeners).removeListener(listener);
    }
    
    public void clearListeners() {
        ((ListenerSupport<AnimationListener>) listeners).clearListeners();
    }
    
    @Override
    public void act(float delta) {
        if (paused) {
            return;
        }
        
        stateTime += delta;
        update();
    }
    
    public void pause() {
        paused = true;
    }
    
    public void resume() {
        paused = false;
    }
    
    public boolean getPaused() {
        return paused;
    }
    
    public void reset() {
        stateTime = 0;
    }
    
    protected void update() {
        if (stateTime >= animation.getAnimationDuration()) {
            stateTime %= animation.getAnimationDuration();
            listeners.onComplete();
        }
        
        drawable.setRegion(animation.getKeyFrame(stateTime));
    }
    
    public void setFrame(int value) {
        stateTime = value * animation.getFrameDuration();
        update();
    }
    
    public int getFramesCount() {
        return (int) (animation.getAnimationDuration() / animation.getFrameDuration());
    }
    
    public Animation getAnimation() {
        return animation;
    }
    
    public float getFrameDuration() {
        return animation.getFrameDuration();
    }
    
    public void setFrameDuration(float value) {
        animation.setFrameDuration(value);
    }
    
    public void setRepeatable(boolean value) {
        if (value) {
            setPlayMode(Animation.PlayMode.LOOP);
        } else {
            setPlayMode(Animation.PlayMode.NORMAL);
        }
    }
    
    public void setPlayMode(Animation.PlayMode value) {
        animation.setPlayMode(value);
    }
    
    public float getRealWidth() {
        return animation.getKeyFrame(0).getRegionWidth() * getScaleX();
    }
    
    public float getRealHeight() {
        return animation.getKeyFrame(0).getRegionHeight() * getScaleY();
    }
}
