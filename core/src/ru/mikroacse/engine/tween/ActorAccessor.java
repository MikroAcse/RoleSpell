package ru.mikroacse.engine.tween;

import aurelienribon.tweenengine.TweenAccessor;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Actor;

/**
 * Created by MikroAcse on 12.07.2016.
 */
public class ActorAccessor implements TweenAccessor<Actor> {
    public static final int X = 1;
    public static final int Y = 2;
    public static final int POSITION = 3;
    public static final int ALPHA = 4;
    public static final int COLOR_RGB = 5;
    public static final int COLOR_RGBA = 6;
    public static final int ROTATION = 7;
    public static final int WIDTH = 8;
    public static final int HEIGHT = 9;
    public static final int SIZE = 10;
    public static final int SCALE = 11;
    public static final int VISIBILITY = 12;
    
    @Override
    public int getValues(Actor target, int tweenType, float[] returnValues) {
        
        switch (tweenType) {
            case X:
                returnValues[0] = target.getX();
                return 1;
            case Y:
                returnValues[0] = target.getY();
                return 1;
            case POSITION:
                returnValues[0] = target.getX();
                returnValues[1] = target.getY();
                return 2;
            case ALPHA:
                returnValues[0] = target.getColor().a;
                return 1;
            case COLOR_RGB:
                returnValues[0] = target.getColor().r;
                returnValues[1] = target.getColor().g;
                returnValues[2] = target.getColor().b;
                return 3;
            case COLOR_RGBA:
                returnValues[0] = target.getColor().r;
                returnValues[1] = target.getColor().g;
                returnValues[2] = target.getColor().b;
                returnValues[3] = target.getColor().a;
                return 4;
            case ROTATION:
                returnValues[0] = target.getRotation();
                return 1;
            case WIDTH:
                returnValues[0] = target.getWidth();
                return 1;
            case HEIGHT:
                returnValues[0] = target.getHeight();
                return 1;
            case SIZE:
                returnValues[0] = target.getWidth();
                returnValues[1] = target.getHeight();
                return 2;
            case SCALE:
                returnValues[0] = target.getScaleX();
                returnValues[1] = target.getScaleY();
                return 2;
            case VISIBILITY:
                returnValues[0] = target.isVisible() ? 1f : 0f;
                return 1;
            default:
                assert false;
        }
        return 0;
    }
    
    @Override
    public void setValues(Actor target, int tweenType, float[] newValues) {
        switch (tweenType) {
            case X:
                target.setX(newValues[0]);
                break;
            case Y:
                target.setY(newValues[0]);
                break;
            case POSITION:
                target.setX(newValues[0]);
                target.setY(newValues[1]);
                break;
            case ALPHA:
                Color color = target.getColor();
                color.a = newValues[0];
                color.clamp();
                break;
            case COLOR_RGB:
                color = target.getColor();
                color.r = newValues[0];
                color.g = newValues[1];
                color.b = newValues[2];
                color.clamp();
                break;
            case COLOR_RGBA:
                color = target.getColor();
                color.r = newValues[0];
                color.g = newValues[1];
                color.b = newValues[2];
                color.a = newValues[3];
                color.clamp();
                break;
            case ROTATION:
                target.setRotation(newValues[0]);
                break;
            case WIDTH:
                target.setWidth(newValues[0]);
                break;
            case HEIGHT:
                target.setHeight(newValues[0]);
                break;
            case SIZE:
                target.setWidth(newValues[0]);
                target.setHeight(newValues[1]);
                break;
            case SCALE:
                target.setScaleX(newValues[0]);
                target.setScaleY(newValues[1]);
                break;
            case VISIBILITY:
                if (newValues[0] == 0f) {
                    target.setVisible(false);
                } else if (newValues[0] == 1f) {
                    target.setVisible(true);
                }
            default:
                assert false;
                break;
        }
    }
}
