package br.mackenzie;

import com.badlogic.gdx.graphics.Texture;

public class Asteroid extends GameObject {

    protected float speedY = -2f; // velocidade vertical
    protected float speedX;
    protected float rotationSpeed; 

    public Asteroid(Texture texture, float x, float y, float width, float height) {
        super(texture, x, y, width, height);
        this.rotationSpeed = 0f;
    }

    @Override
    public void update(float dt) {
        // Movimento para baixo
        translate(0, speedY * dt);
        sprite.rotate(rotationSpeed * dt);
    }
}
