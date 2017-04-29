package ru.mikroacse.engine.utils;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;

/**
 * Created by MikroAcse on 29.07.2016.
 */
public class TextureUtil {
    public static Texture create(int width, int height) {
        return create(width, height, Color.WHITE);
    }
    
    public static Texture create(int width, int height, Color color) {
        return create(width, height, color, Texture.TextureFilter.Nearest);
    }
    
    public static Texture create(int width, int height, Color color, Texture.TextureFilter filter) {
        Pixmap pixmap = new Pixmap(width, height, Pixmap.Format.RGBA8888);
        pixmap.setColor(color);
        pixmap.fillRectangle(0, 0, pixmap.getWidth(), pixmap.getHeight());
        
        Texture texture = new Texture(pixmap, false);
        texture.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);
        
        pixmap.dispose();
        
        return texture;
    }
}
