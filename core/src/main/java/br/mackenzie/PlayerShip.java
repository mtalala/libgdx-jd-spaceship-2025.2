package br.mackenzie;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class PlayerShip extends GameObject {

    private Animation<TextureRegion> moveAnimation;
    private TextureRegion staticFrame;
    private float stateTime = 0f;
    private boolean movingForward = false;

    public PlayerShip(Texture staticTexture, Texture moveTexture1, Texture moveTexture2,
                      float x, float y, float width, float height) {
        super(staticTexture, x, y, width, height);
        staticFrame = new TextureRegion(staticTexture);

        TextureRegion[] frames = new TextureRegion[2];
        frames[0] = new TextureRegion(moveTexture1);
        frames[1] = new TextureRegion(moveTexture2);

        moveAnimation = new Animation<>(0.1f, frames);
        moveAnimation.setPlayMode(Animation.PlayMode.NORMAL);
    }

    public void setMovingForward(boolean movingForward) {
        this.movingForward = movingForward;
        if (!movingForward) stateTime = 0f;
    }

    @Override
    public void update(float dt) {
        if (movingForward) stateTime += dt;
    }

    public void draw(SpriteBatch batch) {
        TextureRegion frame;

        if (movingForward) {
            frame = moveAnimation.getKeyFrame(stateTime);
            if (moveAnimation.isAnimationFinished(stateTime)) {
                frame = moveAnimation.getKeyFrames()[1]; // mantém o segundo frame
            }
        } else {
            frame = staticFrame;
        }

        batch.draw(frame, getX(), getY(), getWidth(), getHeight());
    }
}
