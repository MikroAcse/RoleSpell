package com.quillraven.quillyscastle.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.quillraven.quillyscastle.conversation.Conversation;
import com.quillraven.quillyscastle.conversation.ConversationChoice;
import com.quillraven.quillyscastle.conversation.ConversationGraph;
import com.quillraven.quillyscastle.utils.Utils;

public class ConversationUI extends Window {
    private static final String TAG = ConversationUI.class.getSimpleName();

    private Label dialogText;
    private List<ConversationChoice> choices;
    private ConversationGraph graph;
    private TextButton closeButton;

    public ConversationUI(Stage stage) {
        super("", Utils.UI_SKIN);
        this.getTitleTable().setBackground(Utils.UI_SKIN.getDrawable("default-window-title"));
        this.pad(this.getPadTop() + 10, 0, 0, 0);

        dialogText = new Label("", Utils.UI_SKIN);
        dialogText.setWrap(true);
        dialogText.setAlignment(Align.center);

        closeButton = new TextButton("x", Utils.UI_SKIN);

        choices = new List<ConversationChoice>(Utils.UI_SKIN);

        ScrollPane scrollPane = new ScrollPane(choices);
        scrollPane.setOverscroll(false, false);
        scrollPane.setFadeScrollBars(false);
        scrollPane.setScrollingDisabled(true, false);
        scrollPane.setForceScroll(true, false);
        scrollPane.setScrollBarPositions(false, true);

        this.add();
        this.add(closeButton);
        this.row();

        this.defaults().expand().fill();
        this.add(dialogText).pad(10, 10, 10, 10);
        this.row();
        this.add(scrollPane).pad(10, 10, 10, 10);

        this.pack();

        choices.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                final ConversationChoice choice = choices.getSelected();

                if (choice.getConversationCommandEvent() != null) {
                    graph.fireConversationEvent(choice.getConversationCommandEvent(), graph);
                }

                if (graph.isValid(choice.getDestinationID())) {
                    populateConversationDialog(choice.getDestinationID());
                }
            }
        });

        closeButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                ConversationUI.this.setVisible(false);
            }
        });

        setMovable(true);
        setVisible(false);
        setPosition(stage.getWidth() / 2, 0);
        setWidth(stage.getWidth() / 2);
        setHeight(stage.getHeight() / 2);
        stage.addActor(this);
    }

    public void loadConversation(String filePath, String title) {
        clearDialog();

        if (filePath.isEmpty() || !Gdx.files.internal(filePath).exists()) {
            Gdx.app.debug(TAG, "Conversation file " + filePath + " does not exist!");
            return;
        }

        this.getTitleLabel().setText(title);
        setGraph(Utils.fromJson(ConversationGraph.class, Gdx.files.internal(filePath)));
    }

    public ConversationGraph getGraph() {
        return graph;
    }

    public void setGraph(ConversationGraph graph) {
        if (this.graph != null) {
            this.graph.removeAllObservers();
        }

        this.graph = graph;
        populateConversationDialog(graph.getCurrentConversationID());
    }

    private void populateConversationDialog(String conversationID) {
        clearDialog();

        final Conversation conversation = graph.getConversationByID(conversationID);
        if (conversation == null) {
            Gdx.app.debug(TAG, "Invalid conversationID: " + conversationID);
            return;
        }

        graph.setCurrentConversation(conversationID);
        dialogText.setText(graph.getCurrentConversationText());
        this.choices.setItems(graph.getCurrentChoices());
    }

    private void clearDialog() {
        dialogText.setText("");
        choices.clearItems();
    }
}
