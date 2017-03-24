package com.quillraven.quillyscastle.screens;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.utils.Align;
import com.quillraven.quillyscastle.QuillysCastleGame.ScreenType;
import com.quillraven.quillyscastle.profile.ProfileManager;
import com.quillraven.quillyscastle.utils.Utils;

public class NewGameScreen extends MenuScreen {

    public NewGameScreen() {
        super("New Game");

        Label profileLabel = new Label("Enter profile: ", Utils.UI_SKIN);
        final TextField profile = new TextField("", Utils.UI_SKIN);

        TextButton btnStart = new TextButton("Start", Utils.UI_SKIN);
        TextButton btnBack = new TextButton("Back", Utils.UI_SKIN);

        profileLabel.setAlignment(Align.right);
        table.add(profileLabel);
        table.add(profile).width(300).height(64).left();
        table.row();

        table.add(btnBack).width(256).height(64);
        table.add(btnStart).width(256).height(64);
        table.row();

        table.pack();
        stage.setKeyboardFocus(profile);

        btnStart.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                final String profileName = profile.getText();
                if (!ProfileManager.getInstance().containsProfile(profileName)) {
                    ProfileManager.getInstance().newProfile(profileName);

                    Utils.changeScreen(ScreenType.GAME);
                } else {
                    Dialog dialog = new Dialog("Overwrite profile", Utils.UI_SKIN) {
                        {
                            text("Profile already exists - do you want to overwrite it?");
                            getTitleTable().setBackground(Utils.UI_SKIN.getDrawable("default-window-title"));
                            button("Yes", "overwrite");
                            button("No", "cancel");
                        }

                        @Override
                        protected void result(Object object) {
                            if ("overwrite".equals(object)) {
                                ProfileManager.getInstance().newProfile(profileName);

                                Utils.changeScreen(ScreenType.GAME);
                            }
                        }
                    };

                    dialog.show(stage);
                }

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
}
