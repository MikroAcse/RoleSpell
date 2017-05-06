package ru.mikroacse.rolespell.app.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Screen;
import ru.mikroacse.rolespell.RoleSpell;
import ru.mikroacse.rolespell.media.AssetManager;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by MikroAcse on 09.07.2016.
 */
public class ScreenManager {
    private Game game;
    private Map<BundledScreen, Screen> screens;
    private BundledScreen currentScreen;
    private BundledScreen waitScene;

    public ScreenManager(Game game) {
        this.game = game;

        screens = new HashMap<>();
    }

    private Screen createScreen(BundledScreen bundledScreen) {
        switch (bundledScreen) {
            case GAME:
                return new GameScreen(game);
            case LOADER:
                return new LoaderScreen(game);
        }
        return null;
    }

    public void dispose() {
        Iterator<BundledScreen> it = screens.keySet().iterator();

        while (it.hasNext()) {
            BundledScreen bundledScreen = it.next();

            disposeScreen(bundledScreen);
        }
    }

    public void disposeScreen(BundledScreen bundledScreen) {
        Screen screen = screens.get(bundledScreen);
        screen.dispose();

        RoleSpell.getAssetManager()
                .unloadBundle(bundledScreen.getBundle());

        screens.remove(bundledScreen);
    }

    public void setScreen(BundledScreen bundledScreen) {
        AssetManager assetManager = RoleSpell.getAssetManager();

        if (!assetManager.isLoaded(bundledScreen.getBundle())) {
            setWaitScene(bundledScreen);

            game.setScreen(getScreen(BundledScreen.LOADER));
            getScreen(BundledScreen.LOADER).show();

            assetManager.loadBundle(bundledScreen.getBundle());
            disposeScreen(currentScreen);
            return;
        }

        if (!screens.containsKey(bundledScreen)) {
            screens.put(bundledScreen, createScreen(bundledScreen));
        }

        setCurrentScreen(bundledScreen);
    }

    private void setCurrentScreen(BundledScreen bundledScreen) {
        Screen screen = screens.get(bundledScreen);
        game.setScreen(screen);
        screen.show();

        if (currentScreen != null) {
            disposeScreen(currentScreen);
        }
        currentScreen = bundledScreen;

        System.out.println(" === Current screen set to " + bundledScreen + " === ");
    }

    public void setWaited() {
        if (waitScene == null) {
            return;
        }
        setScreen(waitScene);
    }

    public BundledScreen getWaitScene() {
        return waitScene;
    }

    public void setWaitScene(BundledScreen bundledScreen) {
        waitScene = bundledScreen;
    }

    public Screen getScreen(BundledScreen bundledScreen) {
        return screens.get(bundledScreen);
    }

    public enum BundledScreen {
        GAME(AssetManager.Bundle.GAME),
        MENU(AssetManager.Bundle.MENU),
        LOADER(AssetManager.Bundle.LOADER);

        private AssetManager.Bundle bundle;

        BundledScreen(AssetManager.Bundle bundle) {
            this.bundle = bundle;
        }

        public AssetManager.Bundle getBundle() {
            return bundle;
        }
    }
}
