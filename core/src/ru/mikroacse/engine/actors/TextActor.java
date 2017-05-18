package ru.mikroacse.engine.actors;

/**
 * Created by MikroAcse on 11.07.2016.
 */

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class TextActor extends Actor implements MeasurableActor {
    private GlyphLayout layout;
    private String text;
    private BitmapFont font;

    public TextActor(BitmapFont font) {
        this(font, "");
    }

    public TextActor(BitmapFont font, String text) {
        super();
        this.font = font;
        this.text = text;

        layout = new GlyphLayout(font, text);
        setColor(Color.WHITE);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        Color color = getColor();
        font.setColor(color.r, color.g, color.b, color.a * parentAlpha);

        Matrix4 matrix = batch.getTransformMatrix();
        Matrix4 original = matrix.cpy();

        matrix.translate(getX() + getOriginX(), getY() + getOriginY(), 0);
        matrix.rotate(0, 0, 1, getRotation());
        matrix.scale(getScaleX(), getScaleY(), 1);
        matrix.translate(-getOriginX(), -getOriginY() + layout.height, 0);

        batch.setTransformMatrix(matrix);
        font.draw(batch, text, 0f, 0f);
        batch.setTransformMatrix(original);
    }

    @Override
    public float getWidth() {
        return layout.width;
    }

    @Override
    public float getRealWidth() {
        return getWidth() * getScaleX();
    }

    @Override
    public float getHeight() {
        return layout.height;
    }

    @Override
    public float getRealHeight() {
        return getHeight() * getScaleY();
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
        layout.setText(font, text);
    }

    public BitmapFont getFont() {
        return font;
    }

    public void setFont(BitmapFont font) {
        this.font = font;
        layout.setText(font, text);
    }
}
