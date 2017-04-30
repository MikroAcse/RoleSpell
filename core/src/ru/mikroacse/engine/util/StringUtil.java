package ru.mikroacse.engine.util;

/**
 * Created by MikroAcse on 09.07.2016.
 */
public class StringUtil {
    /**
     * Returns filename without extension
     */
    public static String getFilename(String nameWithExtension) {
        return nameWithExtension.replaceAll("\\.(.+?)$", "");
    }
}
