package br.mackenzie;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;

public class Main implements ApplicationListener {

    Texture backgroundTexture;
    Texture asteroidTexture;

    Texture naveParada;
    Texture naveMove1;
    Texture naveMove2;

    PlayerShip playerShip;
    Array<Asteroid> asteroids;

    SpriteBatch spriteBatch;
    FitViewport viewport;

    Vector2 touchPos;
    float asteroidTimer;

    float backgroundOffsetX = 0;
    float backgroundOffsetY = 0;
    float backgroundSpeed = 2f;

    Sound hitSound;
    Music music;

    private int score;
    private float gameTime;

    private Stage uiStage;
    private Label scoreLabel;
    private Label timeLabel;
    private BitmapFont font;

    @Override
    public void create() {
        backgroundTexture = new Texture("background.png");
        asteroidTexture = new Texture("asteroid.png");

        backgroundTexture.setWrap(Texture.TextureWrap.Repeat, Texture.TextureWrap.Repeat);

        naveParada = new Texture("3.png");
        naveMove1 = new Texture("2.png");
        naveMove2 = new Texture("1.png");

        spriteBatch = new SpriteBatch();
        viewport = new FitViewport(8, 5);

        playerShip = new PlayerShip(naveParada, naveMove1, naveMove2, 4f - 0.5f, 0.5f, 1f, 1f);

        asteroids = new Array<>();
        touchPos = new Vector2();

        hitSound = Gdx.audio.newSound(Gdx.files.internal("hit.mp3"));
        music = Gdx.audio.newMusic(Gdx.files.internal("mass-effect.mp3"));
        music.setLooping(true);
        music.setVolume(0.5f);
        music.play();

        score = 0;
        gameTime = 0;

        uiStage = new Stage(new FitViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight()));
        font = new BitmapFont();
        Label.LabelStyle style = new Label.LabelStyle(font, Color.WHITE);

        scoreLabel = new Label("Score: 0", style);
        timeLabel = new Label("Time: 0", style);

        scoreLabel.setPosition(10, Gdx.graphics.getHeight() - 30);
        timeLabel.setPosition(10, Gdx.graphics.getHeight() - 60);

        uiStage.addActor(scoreLabel);
        uiStage.addActor(timeLabel);
    }

    @Override
    public void resize(int width, int height) {
        if (width <= 0 || height <= 0) return;
        viewport.update(width, height, true);
        uiStage.getViewport().update(width, height, true);
    }

    @Override
    public void render() {
        float dt = Gdx.graphics.getDeltaTime();
        gameTime += dt;

        input(dt);
        logic(dt);
        draw();
        drawUI();
    }

    private void input(float dt) {
        float speed = 4f;

        boolean movingForward = Gdx.input.isKeyPressed(Input.Keys.UP) || Gdx.input.isKeyPressed(Input.Keys.W);
        playerShip.setMovingForward(movingForward);

        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            playerShip.translate(speed * dt, 0);
            backgroundOffsetX += backgroundSpeed * dt;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            playerShip.translate(-speed * dt, 0);
            backgroundOffsetX -= backgroundSpeed * dt;
        }
        if (movingForward) {
            playerShip.translate(0, speed * dt);
            backgroundOffsetY += backgroundSpeed * dt;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
            playerShip.translate(0, -speed * dt);
            backgroundOffsetY -= backgroundSpeed * dt;
        }

        if (Gdx.input.isTouched()) {
            touchPos.set(Gdx.input.getX(), Gdx.input.getY());
            viewport.unproject(touchPos);
            playerShip.setPosition(
                    touchPos.x - playerShip.getWidth() / 2,
                    touchPos.y - playerShip.getHeight() / 2
            );
        }
    }

    private void logic(float dt) {
        float worldWidth = viewport.getWorldWidth();
        float worldHeight = viewport.getWorldHeight();

        playerShip.setPosition(
                MathUtils.clamp(playerShip.getX(), 0, worldWidth - playerShip.getWidth()),
                MathUtils.clamp(playerShip.getY(), 0, worldHeight - playerShip.getHeight())
        );

        playerShip.update(dt);

        for (Asteroid asteroid : asteroids) asteroid.update(dt);

        for (int i = asteroids.size - 1; i >= 0; i--) {
            Asteroid asteroid = asteroids.get(i);

            if (asteroid.getBounds().overlaps(playerShip.getBounds())) {
                asteroids.removeIndex(i);
                hitSound.play();

                if (asteroid instanceof LargeAsteroid) {
                    score += 3;
                    LargeAsteroid large = (LargeAsteroid) asteroid;
                    asteroids.addAll(large.split(asteroidTexture));
                } else if (asteroid instanceof SmallAsteroid) {
                    score += 1;
                }

            } else if (asteroid.getY() + asteroid.getHeight() < 0) {
                asteroids.removeIndex(i);
            }
        }

        asteroidTimer += dt;
        if (asteroidTimer > 1f) {
            asteroidTimer = 0;
            createAsteroid();
        }
    }

    private void draw() {
        ScreenUtils.clear(Color.BLACK);
        viewport.apply();
        spriteBatch.setProjectionMatrix(viewport.getCamera().combined);

        spriteBatch.begin();

        spriteBatch.draw(
                backgroundTexture,
                0, 0, // posição na tela
                viewport.getWorldWidth(), viewport.getWorldHeight(), // tamanho
                backgroundOffsetX, backgroundOffsetY, // u,v iniciais
                backgroundOffsetX + viewport.getWorldWidth(), // u2
                backgroundOffsetY + viewport.getWorldHeight() // v2
        );

        playerShip.draw(spriteBatch);
        for (Asteroid asteroid : asteroids) asteroid.draw(spriteBatch);

        spriteBatch.end();
    }

    private void createAsteroid() {
        float worldWidth = viewport.getWorldWidth();
        float x = MathUtils.random(0f, worldWidth - 1f);
        float y = viewport.getWorldHeight();

        Asteroid asteroid;
        if (MathUtils.randomBoolean(0.3f)) {
            asteroid = new LargeAsteroid(asteroidTexture, x, y);
        } else {
            asteroid = new SmallAsteroid(asteroidTexture, x, y);
        }

        asteroids.add(asteroid);
    }

    private void drawUI() {
        scoreLabel.setText("Score: " + score);
        timeLabel.setText("Time: " + (int)gameTime);

        uiStage.act();
        uiStage.draw();
    }

    @Override
    public void pause() { }

    @Override
    public void resume() { }

    @Override
    public void dispose() {
        spriteBatch.dispose();
        backgroundTexture.dispose();
        asteroidTexture.dispose();
        naveParada.dispose();
        naveMove1.dispose();
        naveMove2.dispose();
        hitSound.dispose();
        music.dispose();
        font.dispose();
        uiStage.dispose();
    }
}
