package com.quillraven.quillyscastle.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.quillraven.quillyscastle.QuillysCastleGame.ScreenType;
import com.quillraven.quillyscastle.utils.Utils;

public class MainMenuScreen extends MenuScreen {

    public MainMenuScreen() {
        super("Main Menu");

        TextButton btnNewGame = new TextButton("New game", skin);
        TextButton btnLoadGame = new TextButton("Load game", skin);
        TextButton btnQuitGame = new TextButton("Quit game", skin);

        table.add(btnNewGame).width(256).height(64).row();
        table.add(btnLoadGame).width(256).height(64).row();
        table.add(btnQuitGame).width(256).height(64).row();

        table.pack();

        btnNewGame.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                Utils.changeScreen(ScreenType.NEW_GAME);

                return true;
            }
        });

        btnLoadGame.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                Utils.changeScreen(ScreenType.LOAD_GAME);

                return true;
            }
        });

        btnQuitGame.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                Gdx.app.exit();

                return true;
            }
        });
    }
}
