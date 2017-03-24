package com.quillraven.quillyscastle.screens;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.List;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.Align;
import com.quillraven.quillyscastle.QuillysCastleGame.ScreenType;
import com.quillraven.quillyscastle.profile.ProfileManager;
import com.quillraven.quillyscastle.utils.Utils;

public class LoadGameScreen extends MenuScreen {
    private final List<String> availableProfiles;

    public LoadGameScreen() {
        super("Load Game");

        availableProfiles = new List<String>(Utils.UI_SKIN);
        ScrollPane scrollPane = new ScrollPane(availableProfiles);

        scrollPane.setOverscroll(false, false);
        scrollPane.setFadeScrollBars(false);
        scrollPane.setScrollingDisabled(true, false);
        scrollPane.setScrollbarsOnTop(true);

        TextButton btnLoad = new TextButton("Load", Utils.UI_SKIN);
        TextButton btnBack = new TextButton("Back", Utils.UI_SKIN);

        Label profileLabel = new Label("Profiles:", skin);

        profileLabel.setAlignment(Align.right | Align.top);
        table.add(profileLabel);
        table.add(scrollPane).width(300).left();
        table.row();

        table.add(btnBack).width(256).height(64);
        table.add(btnLoad).width(256).height(64);
        table.row();

        table.pack();

        btnLoad.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                final String selectedProfile = availableProfiles.getSelected();

                ProfileManager.getInstance().loadProfile(selectedProfile);
                Utils.changeScreen(ScreenType.GAME);

                return true;
            }
        });

        btnBack.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                Utils.changeScreen(ScreenType.MAIN_MENU);

                return true;
            }
        });
    }

    @Override
    public void show() {
        super.show();

        availableProfiles.setItems(ProfileManager.getInstance().getAllProfiles());
    }
}
