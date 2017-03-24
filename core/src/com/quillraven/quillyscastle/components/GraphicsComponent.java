package com.quillraven.quillyscastle.components;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectMap;
import com.quillraven.quillyscastle.GameWorld;
import com.quillraven.quillyscastle.entity.Entity;
import com.quillraven.quillyscastle.entity.Entity.AnimationType;
import com.quillraven.quillyscastle.entity.Entity.Direction;
import com.quillraven.quillyscastle.entity.Entity.State;
import com.quillraven.quillyscastle.entity.EntityConfig;
import com.quillraven.quillyscastle.entity.EntityConfig.AnimationConfig;
import com.quillraven.quillyscastle.map.Map;
import com.quillraven.quillyscastle.utils.Utils;

public abstract class GraphicsComponent extends ComponentSubject implements Component {
    private static final String TAG = GraphicsComponent.class.getSimpleName();

    protected ObjectMap<Entity.AnimationType, Animation> animations;

    protected State state;
    protected Direction direction;

    protected boolean updateAnimation;
    protected float frameTime;
    protected Animation currentAnimation;
    protected Vector2 previousPosition;
    protected Vector2 origin;
    protected float red, green, blue, alpha;

    public GraphicsComponent() {
        animations = new ObjectMap<Entity.AnimationType, Animation>();

        updateAnimation = false;
        frameTime = 0;
        currentAnimation = null;
        origin = new Vector2(0.5f, 0.5f);
        previousPosition = new Vector2();
        red = green = blue = alpha = 1;
    }

    @Override
    public void dispose() {
        Gdx.app.debug(TAG, "disposed!");
    }

    @Override
    public void receiveMessage(MessageType type, Object... args) {
        switch (type) {
            case LOAD_ANIMATIONS:
                EntityConfig config = (EntityConfig) args[0];
                Array<AnimationConfig> animationConfigs = config.getAnimationConfig();

                int animationWidth = config.getAnimationWidth();
                int animationHeight = config.getAnimationHeight();
                String charAtlasID = config.getCharAtlasID();
                for (AnimationConfig animationConfig : animationConfigs) {
                    Array<GridPoint2> gridPoints = animationConfig.getGridPoints();
                    AnimationType animationType = animationConfig.getAnimationType();
                    float frameDuration = animationConfig.getFrameDuration();

                    animations.put(animationType, loadAnimation(charAtlasID, gridPoints, animationWidth, animationHeight, frameDuration));
                }
                break;
            case SET_SIZE:
                origin.set((Float) args[0] * 0.5f, (Float) args[1] * 0.5f);
                break;
            case SET_STATE:
                this.state = (State) args[0];
                updateCurrentAnimation();
                break;
            case SET_DIRECTION:
                this.direction = (Direction) args[0];
                updateCurrentAnimation();
                break;
            default:
                break;
        }

    }

    protected void updateCurrentAnimation() {
        if (state == null || direction == null) {
            // not yet initialized
            return;
        }

        updateAnimation = (state != State.IDLE && state != State.IMMOBILE);
        frameTime = 0;

        switch (state) {
            case IDLE:
                // if there is already an animation displayed then just stop animating it
                if (currentAnimation == null) {
                    // otherwise display the idle animation
                    currentAnimation = animations.get(AnimationType.IDLE);
                }
                break;
            case IMMOBILE:
                currentAnimation = animations.get(AnimationType.IMMOBILE);
                break;
            case WALKING:
                switch (direction) {
                    case UP:
                        currentAnimation = animations.get(AnimationType.WALK_UP);
                        break;
                    case DOWN:
                        currentAnimation = animations.get(AnimationType.WALK_DOWN);
                        break;
                    case LEFT:
                        currentAnimation = animations.get(AnimationType.WALK_LEFT);
                        break;
                    case RIGHT:
                        currentAnimation = animations.get(AnimationType.WALK_RIGHT);
                        break;
                }
                break;
            default:
                currentAnimation = animations.values().next();
                break;
        }

    }

    @Override
    public void update(Entity entity, GameWorld world, Map map, Camera camera, float deltaTime) {
        if (updateAnimation) {
            frameTime = (frameTime + deltaTime);
            if (frameTime >= Float.MAX_VALUE) {
                frameTime = 0;
            }
        }
    }

    public void render(Entity entity, final float interpolationValue, Batch batch, ShapeRenderer shapeRenderer) {
        batch.setColor(red, green, blue, alpha);
        /*batch.draw(currentAnimation.getKeyFrame(frameTime), // texture region
                entity.getBoundingBox().x * interpolationValue + previousPosition.x * (1 - interpolationValue), // interpolated x position
                entity.getBoundingBox().y * interpolationValue + previousPosition.y * (1 - interpolationValue), // interpolated y position
                origin.x, origin.y, // origin
                entity.getSize().x, entity.getSize().y, // size
                1, 1, 0 // scaling and rotation
        );*/

        entity.getBoundingBox().getPosition(previousPosition);

        // shapeRenderer.setProjectionMatrix(camera.combined);
        // shapeRenderer.begin(ShapeType.Filled);
        // shapeRenderer.setColor(1, 0, 0, 0);
        // shapeRenderer.rect(entity.getCollisionBox().x, entity.getCollisionBox().y, entity.getCollisionBox().width, entity.getCollisionBox().height);
        // shapeRenderer.end();
        //
        // shapeRenderer.setProjectionMatrix(camera.combined);
        // shapeRenderer.begin(ShapeType.Line);
        // shapeRenderer.setColor(0, 0, 1, 0);
        // shapeRenderer.rect(entity.getBoundingBox().x, entity.getBoundingBox().y, entity.getBoundingBox().width, entity.getBoundingBox().height);
        // shapeRenderer.end();
    }

    private Animation loadAnimation(String charAtlasID, Array<GridPoint2> points, int tileWidth, int tileHeight, float frameDuration) {
        AtlasRegion charTextureRegion = Utils.CHARACTERS_TEXTURE_ATLAS.findRegion(charAtlasID);

        TextureRegion[][] frames = charTextureRegion.split(tileWidth, tileHeight);
        Array<TextureRegion> animationFrames = new Array<TextureRegion>(points.size);
        for (GridPoint2 point : points) {
            animationFrames.add(frames[point.y][point.x]);
        }

        return new Animation(frameDuration, animationFrames, Animation.PlayMode.LOOP);
    }
}
