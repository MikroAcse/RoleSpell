package ru.mikroacse.rolespell.app.screens;

import com.badlogic.gdx.Game;
import ru.mikroacse.rolespell.RoleSpell;
import ru.mikroacse.rolespell.media.AssetManager;
import ru.mikroacse.rolespell.media.AssetManager.Bundle;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by MikroAcse on 09.07.2016.
 */
public class ScreenManager {
    private Game game;
    private Map<BundledScreen, Screen> screens;
    private BundledScreen currentScreen;
    private BundledScreen waitScreen;

    public ScreenManager(Game game) {
        this.game = game;

        screens = new HashMap<>();
    }

    private Screen createScreen(BundledScreen bundledScreen) {
        switch (bundledScreen) {
            case GAME:
                return new GameScreen();
            case LOADER:
                return new LoaderScreen();
            case MENU:
                return new MenuScreen();
            case SETTINGS:
                return new SettingsScreen();
        }
        return null;
    }

    public void dispose() {
        BundledScreen[] screens = new BundledScreen[this.screens.size()];

        this.screens.keySet().toArray(screens);

        for (BundledScreen screen : screens) {
            disposeScreen(screen);
        }
    }

    public void disposeScreen(BundledScreen bundledScreen) {
        Screen screen = screens.get(bundledScreen);

        if (!screen.isDisposable()) {
            return;
        }

        screen.dispose();

        RoleSpell.getAssetManager()
                .unloadBundle(bundledScreen.getBundle());

        screens.remove(bundledScreen);
    }

    public void setScreen(BundledScreen bundledScreen) {
        AssetManager assetManager = RoleSpell.getAssetManager();

        if (!assetManager.isLoaded(bundledScreen.getBundle())) {
            setWaitScreen(bundledScreen);

            if (!assetManager.isLoaded(Bundle.LOADER)) {
                assetManager.loadBundle(Bundle.LOADER, true);
            }

            setScreen(BundledScreen.LOADER);

            assetManager.loadBundle(bundledScreen.getBundle());
            return;
        }

        if (!screens.containsKey(bundledScreen)) {
            screens.put(bundledScreen, createScreen(bundledScreen));
        }

        setCurrentScreen(bundledScreen);
    }

    private void setCurrentScreen(BundledScreen bundledScreen) {
        if (currentScreen != null) {
            getScreen(currentScreen).suspend();
        }

        Screen screen = screens.get(bundledScreen);
        game.setScreen(screen);

        if (currentScreen != null) {
            disposeScreen(currentScreen);
        }

        currentScreen = bundledScreen;

        screen.restore();

        System.out.println(" === Current screen set to " + bundledScreen + " === ");
    }

    public void setWaited() {
        if (waitScreen == null) {
            return;
        }

        BundledScreen waitScreen = this.waitScreen;
        this.waitScreen = null;

        setScreen(waitScreen);
    }

    public BundledScreen getWaitScreen() {
        return waitScreen;
    }

    public void setWaitScreen(BundledScreen bundledScreen) {
        waitScreen = bundledScreen;
    }

    public Screen getScreen(BundledScreen bundledScreen) {
        return screens.get(bundledScreen);
    }

    public enum BundledScreen {
        GAME(Bundle.GAME),
        MENU(Bundle.MENU),
        SETTINGS(Bundle.SETTINGS),
        LOADER(Bundle.LOADER);

        private Bundle bundle;

        BundledScreen(Bundle bundle) {
            this.bundle = bundle;
        }

        public Bundle getBundle() {
            return bundle;
        }
    }
}
