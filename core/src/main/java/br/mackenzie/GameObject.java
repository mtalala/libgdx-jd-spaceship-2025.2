package br.mackenzie;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;

/**
 * Classe base para todos os objetos do jogo.
 * Define propriedades comuns como sprite, bounds (colisão), atualização e desenho.
 */
public abstract class GameObject {

    protected Sprite sprite;
    protected Rectangle bounds;

    public GameObject(Texture texture, float x, float y, float width, float height) {
        this.sprite = new Sprite(texture);
        this.sprite.setSize(width, height);
        this.sprite.setPosition(x, y);

        this.bounds = new Rectangle(x, y, width, height);
    }

    /** Método de atualização específico de cada objeto */
    public abstract void update(float dt);

    /** Desenha o sprite do objeto */
    public void draw(SpriteBatch batch) {
        sprite.draw(batch);
    }

    /** Retorna o retângulo de colisão (sempre atualizado) */
    public Rectangle getBounds() {
        bounds.setPosition(sprite.getX(), sprite.getY());
        return bounds;
    }

    public void setPosition(float x, float y) {
        sprite.setPosition(x, y);
        bounds.setPosition(x, y);
    }

    public void translate(float dx, float dy) {
        sprite.translate(dx, dy);
        bounds.setPosition(sprite.getX(), sprite.getY());
    }

    public float getX() { return sprite.getX(); }
    public float getY() { return sprite.getY(); }
    public float getWidth() { return sprite.getWidth(); }
    public float getHeight() { return sprite.getHeight(); }

    public Sprite getSprite() {
        return sprite;
    }
}
