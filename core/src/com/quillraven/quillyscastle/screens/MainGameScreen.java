package com.quillraven.quillyscastle.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.quillraven.quillyscastle.GameRenderer;
import com.quillraven.quillyscastle.GameWorld;
import com.quillraven.quillyscastle.PlayerHUD;
import com.quillraven.quillyscastle.components.player.PlayerInputComponent;
import com.quillraven.quillyscastle.map.MapManager;
import com.quillraven.quillyscastle.map.MapManager.MapType;
import com.quillraven.quillyscastle.profile.ProfileManager;

public class MainGameScreen implements Screen {
    private final GameWorld world;
    private final GameRenderer renderer;
    private final PlayerHUD playerHud;
    private final InputMultiplexer inputMultiplexer;
    private final float fixedTimeStep;
    private float accumulator;
    private float interpolationValue;

    public MainGameScreen() {
        accumulator = 0;
        fixedTimeStep = 1.0f / Gdx.graphics.getDisplayMode().refreshRate;

        world = new GameWorld();
        renderer = new GameRenderer(world);
        playerHud = new PlayerHUD(world.getPlayer());

        inputMultiplexer = new InputMultiplexer();
        inputMultiplexer.addProcessor(playerHud.getStage());
        inputMultiplexer.addProcessor(world.getPlayer().getComponent(PlayerInputComponent.class));

        MapManager.getInstance().setMap(MapType.TOWN);
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(inputMultiplexer);
    }

    @Override
    public void hide() {
        ProfileManager.getInstance().saveCurrentProfile(true);
        Gdx.input.setInputProcessor(null);
    }

    @Override
    public void render(float deltaTime) {
        if (deltaTime > 0.25f) {
            deltaTime = 0.25f;
        }

        accumulator += deltaTime;

        while (accumulator >= fixedTimeStep) {
            world.update(fixedTimeStep);
            accumulator -= fixedTimeStep;
        }
        playerHud.update(deltaTime);

        interpolationValue = accumulator / fixedTimeStep;
        renderer.render(interpolationValue);
        playerHud.render();
    }

    @Override
    public void resize(int width, int height) {
        renderer.resize(width, height);
        playerHud.resize(width, height);
    }

    @Override
    public void pause() {
        ProfileManager.getInstance().saveCurrentProfile(true);
    }

    @Override
    public void resume() {
    }

    @Override
    public void dispose() {
        world.dispose();
        renderer.dispose();
        playerHud.dispose();
    }
}
