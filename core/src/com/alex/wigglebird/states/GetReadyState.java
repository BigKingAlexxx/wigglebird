package com.alex.wigglebird.states;

import com.alex.wigglebird.FlappyDemo;
import com.alex.wigglebird.sprites.Ground;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

import java.util.Random;

/**
 * Created by elexx on 24.06.2017.
 */

public class GetReadyState extends State {

    private BitmapFont font, shadow, fontRdy, shadowRdy;
    private GlyphLayout glyphFont, glyphShadow, glyphFontRdy;
    private Vector2 posFont, posShadow;
    private Texture bg, bgNight, birdGray, tap;
    private com.alex.wigglebird.sprites.Bird bird;
    private int flag;
    private Array<Ground> grounds;
    private Random rand;

    private ShapeRenderer rect;
    private float alpha = 1;

    public GetReadyState(GameStateManager gsm) {
        super(gsm);
        bird = new com.alex.wigglebird.sprites.Bird(90, 180);
        cam.setToOrtho(false, FlappyDemo.WIDTH / 2, FlappyDemo.HEIGHT / 2);
        bg = new Texture("bg_0.1.png");
        bgNight = new Texture("bg_night.png");
        font = new BitmapFont(Gdx.files.internal("font/text.fnt"), true);
        font.getData().setScale(.35f, -.35f);
        shadow = new BitmapFont(Gdx.files.internal("font/shadow.fnt"), true);
        shadow.getData().setScale(.35f, -.35f);
        glyphFont = new GlyphLayout(font, "GAME OVER");
        glyphShadow = new GlyphLayout(shadow, "GAME OVER");
        glyphFont.setText(font, "GAME OVER");
        glyphShadow.setText(shadow, "GAME OVER");
        float width = glyphFont.width;
        posFont = new Vector2((cam.position.x) - (width / 2), (cam.position.y + cam.viewportHeight / 2.75f));
        posShadow = new Vector2((cam.position.x + 2) - (width / 2), (cam.position.y + cam.viewportHeight / 2.75f) - 2);
        font.setUseIntegerPositions(false);
        shadow.setUseIntegerPositions(false);
        fontRdy = new BitmapFont(Gdx.files.internal("font/text.fnt"), true);
        fontRdy.getData().setScale(.30f, -.30f);
        shadowRdy = new BitmapFont(Gdx.files.internal("font/shadow.fnt"), true);
        shadowRdy.getData().setScale(.30f, -.30f);
        glyphFontRdy = new GlyphLayout(fontRdy, "GET READY!");
        fontRdy.setUseIntegerPositions(false);
        shadowRdy.setUseIntegerPositions(false);
        grounds = new Array<Ground>();
        grounds.add(new Ground(cam.position.x - (cam.viewportWidth / 2)));
        grounds.add(new Ground(grounds.get(0).getTexture().getWidth()));
        birdGray = new Texture("bird_gray.png");
        tap = new Texture("hand_arrow_tap.png");
        rand = new Random();
        if (rand.nextInt(2) == 0) {
            FlappyDemo.IS_NIGHT = false;
        } else {
            FlappyDemo.IS_NIGHT = true;
        }
        rect = new ShapeRenderer();
        rect.setColor(0, 0, 0, 1);
    }

    @Override
    protected void handleInput() {
        if (Gdx.input.justTouched()) {
            bird.flap.play(0.25f);
            FlappyDemo.toggleAd();
            gsm.set(new PlayState(gsm));
        }
    }

    @Override
    public void update(float dt) {
        if (flag == 0) {
            handleInput();
            bird.update(dt);
            bird.setPosition((int) bird.getPosition().x, 180);
            bird.setRotation(0);
            bird.getBirdAnimation().update(1.5f * dt);
            cam.position.x = bird.getPosition().x + 50;
            for (int i = 0; i < grounds.size; i++) {
                grounds.get(i).update();
            }
            repositionGround();
            cam.update();

            alpha -= Gdx.graphics.getDeltaTime() / 0.5f;
            rect.setColor(0, 0, 0, alpha);
        } else {
            //gsm.push(new GameOverState(gsm));
            flag = 0;
        }
    }

    @Override
    public void render(SpriteBatch sb) {
        sb.setProjectionMatrix(cam.combined);
        sb.begin();
        if (!FlappyDemo.IS_NIGHT) sb.draw(bg, cam.position.x - (cam.viewportWidth / 2), -50);
        else sb.draw(bgNight, cam.position.x - (cam.viewportWidth / 2), -50);
        sb.draw(bird.getTexture(), bird.getPosition().x, bird.getPosition().y, bird.getTexture().getTexture().getWidth() / 7.5f,
                bird.getTexture().getTexture().getHeight() / 2.5f, bird.getTexture().getTexture().getWidth() / 3.75f,
                bird.getTexture().getTexture().getHeight() / 1.25f, 1, 1, bird.getRotation());
        for (Ground ground : grounds) {
            sb.draw(ground.getTexture(), ground.getPosGround().x, ground.getPosGround().y);
        }
        glyphFont.setText(font, "0");
        glyphShadow.setText(shadow, "0");
        float width = glyphFont.width;
        shadow.draw(sb, "0", (cam.position.x + 2) - (width / 2), (cam.position.y + cam.viewportHeight / 2.5f) - 2);
        font.draw(sb, "0", (cam.position.x) - (width / 2), (cam.position.y + cam.viewportHeight / 2.5f));
        fontRdy.setColor(Color.GREEN);
        glyphFontRdy.setText(fontRdy, "GET READY!");
        float widthRdy = glyphFontRdy.width;
        shadowRdy.draw(sb, "GET READY!", (cam.position.x + 2) - (widthRdy / 2), (cam.position.y + cam.viewportHeight / 4.5f) - 2);
        fontRdy.draw(sb, "GET READY!", (cam.position.x) - (widthRdy / 2), (cam.position.y + cam.viewportHeight / 4.5f));
        sb.draw(birdGray, cam.position.x - birdGray.getWidth() / 3f, cam.position.y + 10, birdGray.getWidth() / 1.5f, birdGray.getHeight() / 1.5f);
        sb.draw(tap, cam.position.x - cam.viewportWidth / 30, cam.position.y - cam.viewportHeight / 9);
        sb.end();
        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        rect.begin(ShapeRenderer.ShapeType.Filled);
        rect.setProjectionMatrix(cam.combined);
        rect.rect(cam.position.x - cam.viewportWidth / 2, cam.position.y - cam.viewportHeight / 2,
                cam.viewportWidth, cam.viewportHeight);
        rect.end();
    }

    @Override
    public void dispose() {
        font.dispose();
        shadow.dispose();
        bird.dispose();
        bg.dispose();
        for (Ground ground : grounds) {
            ground.dispose();
        }
        fontRdy.dispose();
        birdGray.dispose();
        tap.dispose();
        bgNight.dispose();
        rect.dispose();
    }

    @Override
    public void pause() {
        flag = 1;
    }

    private void repositionGround() {
        if (cam.position.x - cam.viewportWidth / 2 > grounds.get(0).getPosGround().x + grounds.get(0).getTexture().getWidth()) {
            grounds.get(0).setPosGround(grounds.get(1).getPosGround().x + grounds.get(1).getTexture().getWidth());
        }
        if (cam.position.x - cam.viewportWidth / 2 > grounds.get(1).getPosGround().x + grounds.get(1).getTexture().getWidth()) {
            grounds.get(1).setPosGround(grounds.get(0).getPosGround().x + grounds.get(1).getTexture().getWidth());
        }
    }

}
