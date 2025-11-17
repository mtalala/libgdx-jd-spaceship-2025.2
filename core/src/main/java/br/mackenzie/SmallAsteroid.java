package br.mackenzie;

import com.badlogic.gdx.graphics.Texture;

// public class SmallAsteroid extends Asteroid {

//     public SmallAsteroid(Texture texture, float x, float y) {
//         super(texture, x, y, 1f, 1f); // tamanho igual à Main original
//         this.speedY = -2f;            // mesma velocidade
//         this.rotationSpeed = 180f;
//         sprite.setOriginCenter();
//     }

//     @Override
//     public void update(float dt) {
//         super.update(dt); // comportamento igual ao Asteroid da Main original
//         sprite.rotate(rotationSpeed * dt);
//     }
// }

public class SmallAsteroid extends Asteroid {

    public SmallAsteroid(Texture texture, float x, float y) {
        super(texture, x, y, 1f, 1f); // tamanho igual à Main original
        this.speedX = 0f;             // inicialmente sem movimento horizontal
        this.speedY = -2f;            // mesma velocidade vertical
        this.rotationSpeed = 180f;    // gira rápido
        sprite.setOriginCenter();
    }

    @Override
    public void update(float dt) {
        // move de acordo com speedX e speedY
        sprite.translate(speedX * dt, speedY * dt);
        sprite.rotate(rotationSpeed * dt);
    }

    // Permite definir velocidade personalizada (útil para split)
    public void setSpeed(float speedX, float speedY) {
        this.speedX = speedX;
        this.speedY = speedY;
    }
}

