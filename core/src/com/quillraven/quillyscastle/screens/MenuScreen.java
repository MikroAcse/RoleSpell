package com.quillraven.quillyscastle.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.quillraven.quillyscastle.utils.Utils;

public abstract class MenuScreen implements Screen {
    protected final OrthographicCamera camera;
    protected final Stage stage;
    protected final Table table;
    protected final Skin skin;

    private final Label title;

    public MenuScreen(String menuTitle) {
        this.camera = new OrthographicCamera();
        camera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        this.stage = new Stage(new ScreenViewport(camera));

        table = new Table();
        table.setFillParent(true);
        table.defaults().expand().fill().pad(0);

        skin = Utils.UI_SKIN;

        table.setBackground(skin.getDrawable("menu-background"));

        title = new Label(menuTitle, skin, "large");
        title.setAlignment(Align.center);
        table.add(title).padTop(275).colspan(2).row();

        stage.addActor(table);
        // stage.setDebugAll(true);
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void hide() {
        Gdx.input.setInputProcessor(null);
    }

    @Override
    public void render(float deltaTime) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        stage.act(deltaTime);
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {
    }

    @Override
    public void dispose() {
        stage.dispose();
    }
}
