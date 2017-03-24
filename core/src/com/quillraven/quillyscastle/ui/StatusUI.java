package com.quillraven.quillyscastle.ui;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.quillraven.quillyscastle.utils.Utils;

public class StatusUI extends Window {
    private static final String GOLD_LABEL = "Gold: ";
    private final Label hpLabel;
    private final Image hpBar;
    private final Image hpBarBorder;

    private final Label mpLabel;
    private final Image mpBar;
    private final Image mpBarBorder;

    private final Label xpLabel;
    private final Image xpBar;
    private final Image xpBarBorder;

    private final ImageButton inventoryButton;
    private final Label lvlLabel;
    private final Label goldLabel;

    public StatusUI(Stage stage) {
        super("", Utils.UI_SKIN);
        this.setMovable(false);

        hpLabel = new Label("10 / 10", Utils.UI_SKIN);
        hpBar = new Image(Utils.UI_TEXTURE_ATLAS.findRegion("hp_bar"));
        hpBarBorder = new Image(Utils.UI_TEXTURE_ATLAS.findRegion("bar"));

        mpLabel = new Label("15 / 15", Utils.UI_SKIN);
        mpBar = new Image(Utils.UI_TEXTURE_ATLAS.findRegion("mp_bar"));
        mpBarBorder = new Image(Utils.UI_TEXTURE_ATLAS.findRegion("bar"));

        xpLabel = new Label("27  / 50", Utils.UI_SKIN);
        xpBar = new Image(Utils.UI_TEXTURE_ATLAS.findRegion("xp_bar"));
        xpBarBorder = new Image(Utils.UI_TEXTURE_ATLAS.findRegion("bar"));

        inventoryButton = new ImageButton(Utils.UI_SKIN, "inventory-button");
        lvlLabel = new Label("Level: 1", Utils.UI_SKIN);
        goldLabel = new Label(GOLD_LABEL, Utils.UI_SKIN);

        WidgetGroup hpGroup = new WidgetGroup();
        WidgetGroup mpGroup = new WidgetGroup();
        WidgetGroup xpGroup = new WidgetGroup();

        hpLabel.setPosition(43, 7);
        hpBar.setPosition(33, 10);
        hpBar.scaleBy(-0.49f);
        hpBarBorder.scaleBy(-0.5f);
        hpGroup.addActor(hpBar);
        hpGroup.addActor(hpBarBorder);
        hpGroup.addActor(hpLabel);

        mpLabel.setPosition(43, 7);
        mpBar.setPosition(33, 10);
        mpBar.scaleBy(-0.49f);
        mpBarBorder.scaleBy(-0.5f);
        mpGroup.addActor(mpBar);
        mpGroup.addActor(mpBarBorder);
        mpGroup.addActor(mpLabel);

        xpLabel.setPosition(43, 7);
        xpBar.setPosition(33, 10);
        xpBar.scaleBy(-0.49f);
        xpBar.scaleBy(-0.25f, 0);
        xpBarBorder.scaleBy(-0.5f);
        xpGroup.addActor(xpBar);
        xpGroup.addActor(xpBarBorder);
        xpGroup.addActor(xpLabel);

        inventoryButton.getImageCell().size(32, 32);

        this.defaults().expand().fill().pad(5);
        this.add(hpGroup).width(323);
        this.add(xpGroup).width(323);
        this.add(inventoryButton);
        this.row();
        this.add(mpGroup).width(323);
        this.add(lvlLabel).padLeft(50);
        this.add(goldLabel).padRight(50);

        pack();
        setPosition(stage.getWidth() / 2 - getWidth() / 2, 0);
        stage.addActor(this);
    }

    public ImageButton getInventoryButton() {
        return inventoryButton;
    }

    public void updateGoldValue(int goldValue) {
        goldLabel.setText(GOLD_LABEL + goldValue);
        this.pack();
    }
}
