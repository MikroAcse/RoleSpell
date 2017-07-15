package ru.mikroacse.engine.config;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.I18NBundle;

import java.util.Locale;
import java.util.MissingResourceException;

import static com.badlogic.gdx.Gdx.files;

/**
 * Created by MikroAcse on 08.07.2016.
 */
public class Language {
    private I18NBundle bundle;

    public Language() {

    }

    public Language(String path) {
        load(path);
    }

    /**
     * Loads languages bundle (example path: languages/lang for files: lang_en, lang_fr etc.)
     */
    public void load(String path) {
        FileHandle fileHandle = files.internal(path);
        Locale locale = Locale.getDefault();

        // TODO: normal russian numbers handling
        bundle = I18NBundle.createBundle(fileHandle, locale);
    }

    /**
     * Returns formatted text from language bundle.
     * If bundle doesn't have the key, its value returned.
     */
    public String get(String key, Object... args) {
        try {
            return bundle.format(key, (Object[]) args);
        } catch (MissingResourceException e) {
            return key;
        }
    }

    public I18NBundle getBundle() {
        return bundle;
    }
}
