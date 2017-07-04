package ru.mikroacse.engine.tween;

import aurelienribon.tweenengine.BaseTween;
import aurelienribon.tweenengine.Timeline;
import aurelienribon.tweenengine.Tween;

import java.util.List;

/**
 * Created by MikroAcse on 12.07.2016.
 */
public class TweenManager extends aurelienribon.tweenengine.TweenManager {
    public void killTarget(Object target, boolean finish) {
        if (!finish) {
            killTarget(target);
            return;
        }

        finishObjects(target, getObjects());
    }

    private void finishObjects(Object target, List<BaseTween<?>> objects) {
        for (BaseTween<?> baseTween : objects) {
            if (baseTween instanceof Tween) {
                Tween tween = (Tween) baseTween;
                if (tween.getTarget().equals(target)) {
                    tween.update(tween.getFullDuration());
                }
            } else if (baseTween instanceof Timeline) {
                Timeline timeline = (Timeline) baseTween;
                finishObjects(target, timeline.getChildren());
            }
        }
    }
}
