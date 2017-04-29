package ru.mikroacse.engine.utils;

import com.badlogic.gdx.graphics.Color;

/**
 * Created by MikroAcse on 11.07.2016.
 */
public class ColorUtil {
    public static Color fromRGB(int value) {
        Color color = new Color(0, 0, 0, 1f);
        Color.rgb888ToColor(color, value);
        return color;
    }
    
    /**
     * Return average color between two colors.
     */
    public static Color getAverage(Color color1, Color color2) {
        return combine(color1, color2, 0.5f);
    }
    
    /**
     * Combines two colors. Portion defines how much of color 2 will be "added" into color 1.
     */
    public static Color combine(Color color1, Color color2, float portion) {
        float r = color1.r * (1 - portion) + color2.r * portion;
        float g = color1.g * (1 - portion) + color2.g * portion;
        float b = color1.b * (1 - portion) + color2.b * portion;
        float a = color1.a * (1 - portion) + color2.a * portion;
        
        return new Color(r, g, b, 1);
    }
    
    /**
     * Returns difference between two colors: from 0 (no difference) to 3 (full difference, only for black and white)
     */
    public static float getDifference(Color color1, Color color2) {
        float r = Math.abs(color1.r - color2.r);
        float g = Math.abs(color1.g - color2.g);
        float b = Math.abs(color1.b - color2.b);
        
        return r + g + b;
    }
    
    /**
     * Returns if color too contrast for white overlay.
     */
    public static boolean isContrast(Color color) {
        return (color.r * 0.299 + color.g * 0.587 + color.b * 0.114) > 0.780;
    }
    
    /**
     * Returns if colors are the same
     */
    public static boolean equals(Color color1, Color color2, boolean checkAlpha) {
        return color1.r == color2.r
                && color1.g == color2.g
                && color2.b == color2.b
                && (!checkAlpha || color1.a == color2.a);
    }
    
    public static boolean equals(Color color1, Color color2) {
        return equals(color1, color2, false);
    }
}
