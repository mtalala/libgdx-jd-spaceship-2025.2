package br.mackenzie;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.math.MathUtils;


public class LargeAsteroid extends Asteroid {

    public LargeAsteroid(Texture texture, float x, float y) {
        super(texture, x, y, 1.5f, 1.5f); // maior que SmallAsteroid
        this.speedX = 0f;
        this.speedY = -1f;             // mais lento que SmallAsteroid
        this.rotationSpeed = 60f;      // gira devagar
        sprite.setOriginCenter();
    }

    @Override
    public void update(float dt) {
        sprite.translate(speedX * dt, speedY * dt);
        sprite.rotate(rotationSpeed * dt);
    }

    // Divide LargeAsteroid em 3 SmallAsteroids com dispersão horizontal
    public Array<SmallAsteroid> split(Texture smallAsteroidTexture) {
    Array<SmallAsteroid> fragments = new Array<>();

    // SmallAsteroid indo para a esquerda e para cima
    SmallAsteroid left = new SmallAsteroid(smallAsteroidTexture, getX(), getY());
    left.setSpeed(-2f, 1f);  // esquerda e sobe
    fragments.add(left);

    // SmallAsteroid indo para a direita e para cima
    SmallAsteroid right = new SmallAsteroid(smallAsteroidTexture, getX(), getY());
    right.setSpeed(2f, 1f);  // direita e sobe
    fragments.add(right);

    // SmallAsteroid central, sobe reto
    SmallAsteroid center = new SmallAsteroid(smallAsteroidTexture, getX(), getY());
    center.setSpeed(0f, 2f);  // sobe reto
    fragments.add(center);

    return fragments;
}

}

