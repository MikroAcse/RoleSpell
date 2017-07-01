package ru.mikroacse.rolespell.util;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import ru.mikroacse.rolespell.media.Bundle;

import static ru.mikroacse.rolespell.RoleSpell.bundle;

/**
 * Created by MikroAcse on 14.07.2016.
 */
public class AnimationUtil {
    public static Animation<TextureRegion> create(Bundle bundle, String name, boolean smoothing) {
        return create(bundle(bundle).getAtlas(name), smoothing);
    }

    public static Animation<TextureRegion> create(Bundle bundle, String name) {
        return create(bundle, name, true);
    }

    public static Animation<TextureRegion> create(TextureAtlas atlas, boolean smoothing) {
        if (smoothing) {
            for (TextureRegion region : atlas.getRegions()) {
                region.getTexture()
                        .setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
            }
        }
        return new Animation<>(1 / 30f, atlas.getRegions());
    }

    public static Animation<TextureRegion> create(TextureAtlas atlas) {
        return create(atlas, true);
    }
}
