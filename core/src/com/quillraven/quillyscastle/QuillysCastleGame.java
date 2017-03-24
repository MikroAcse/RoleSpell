package com.quillraven.quillyscastle;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Screen;
import com.quillraven.quillyscastle.screens.LoadGameScreen;
import com.quillraven.quillyscastle.screens.MainGameScreen;
import com.quillraven.quillyscastle.screens.MainMenuScreen;
import com.quillraven.quillyscastle.screens.NewGameScreen;
import com.quillraven.quillyscastle.utils.Utils;

public class QuillysCastleGame extends Game {
    private MainMenuScreen mainMenuScreen;
    private NewGameScreen newGameScreen;
    private LoadGameScreen loadGameScreen;
    private MainGameScreen mainGameScreen;

    @Override
    public void create() {
        mainMenuScreen = new MainMenuScreen();
        mainGameScreen = new MainGameScreen();
        newGameScreen = new NewGameScreen();
        loadGameScreen = new LoadGameScreen();

        // setScreen(mainMenuScreen);
        setScreen(mainGameScreen);
    }

    public Screen getScreen(ScreenType type) {
        switch (type) {
            case MAIN_MENU:
                return mainMenuScreen;
            case NEW_GAME:
                return newGameScreen;
            case GAME:
                return mainGameScreen;
            case LOAD_GAME:
                return loadGameScreen;
        }

        return null;
    }

    @Override
    public void dispose() {
        mainMenuScreen.dispose();
        newGameScreen.dispose();
        loadGameScreen.dispose();
        mainGameScreen.dispose();
        Utils.dispose();
    }

    public enum ScreenType {
        MAIN_MENU,
        NEW_GAME,
        LOAD_GAME,
        GAME;
    }
}
