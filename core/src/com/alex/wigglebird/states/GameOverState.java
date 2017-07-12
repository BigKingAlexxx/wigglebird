package com.alex.wigglebird.states;

import com.alex.wigglebird.SimpleButton;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;


/**
 * Created by elexx on 04.06.2017.
 */

public class GameOverState extends State {

    private Texture bg, bgNight;
    private BitmapFont font, shadow, fontTafel, fontTafelShadow, fontTafelRot, scoreFont, scoreShadow, fontRate;
    private GlyphLayout glyphFont, glyphShadow;
    private Vector2 posFont, posShadow, velocity, posTafel;
    private Texture tafel, playBtn, homeBtn, playBtnDown, homeBtnDown, rateBtn, rateBtnDown;
    private Preferences prefs = Gdx.app.getPreferences("FlappyPrefs");
    private float averageScore;
    private int bestScore;
    private SimpleButton simplePlay, simpleHome, simpleRate;
    private Sound transition;

    private static final int GRAVITY = -15;
    private long startTXT = System.currentTimeMillis() + 500;
    private long startBtn = System.currentTimeMillis() + 1300;

    public GameOverState(GameStateManager gsm) {
        super(gsm);
        cam.setToOrtho(false, com.alex.wigglebird.FlappyDemo.WIDTH / 2, com.alex.wigglebird.FlappyDemo.HEIGHT / 2);
        bg = new Texture("bg_0.1.png");
        bgNight = new Texture("bg_night.png");
        tafel = new Texture("tafel.png");
        posTafel = new Vector2(cam.position.x - tafel.getWidth() / 1.35f / 2, -tafel.getHeight() / 1.35f);
        transition = Gdx.audio.newSound(Gdx.files.internal("transition.ogg"));

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
        velocity = new Vector2(0, 200);

        fontTafel = new BitmapFont(Gdx.files.internal("font/text.fnt"), true);
        fontTafel.getData().setScale(.35f, -.35f);
        fontTafelShadow = new BitmapFont(Gdx.files.internal("font/shadow.fnt"), true);
        fontTafelShadow.getData().setScale(.35f, -.35f);
        fontTafelRot = new BitmapFont(Gdx.files.internal("font/text.fnt"), true);
        fontTafelRot.getData().setScale(.25f, -.25f);
        scoreFont = new BitmapFont(Gdx.files.internal("font/text.fnt"), true);
        scoreFont.getData().setScale(.5f, -.5f);
        scoreShadow = new BitmapFont(Gdx.files.internal("font/shadow.fnt"), true);
        scoreShadow.getData().setScale(.5f, -.5f);
        playBtn = new Texture("playbtn.png");
        homeBtn = new Texture("home_button.png");
        playBtnDown = new Texture("playbtn_pressed.png");
        homeBtnDown = new Texture("home_button_down.png");
        simplePlay = new SimpleButton(cam.position.x - (playBtn.getWidth() / 4) - 60, cam.position.y - cam.viewportHeight / 10, playBtn.getWidth() / 2, playBtn.getHeight() / 2, playBtn, playBtnDown);
        simpleHome = new SimpleButton(cam.position.x - (playBtn.getWidth() / 4), cam.position.y - cam.viewportHeight / 10, playBtn.getWidth() / 2, playBtn.getHeight() / 2, homeBtn, homeBtnDown);
        rateBtn = new Texture("ratebtn.png");
        rateBtnDown = new Texture("ratebtn_pressed.png");
        simpleRate = new SimpleButton(cam.position.x - (playBtn.getWidth() / 4) + 60, cam.position.y - cam.viewportHeight / 10,
                playBtn.getWidth() / 2, playBtn.getHeight() / 2, rateBtn, rateBtnDown);
        fontRate = new BitmapFont(Gdx.files.internal("font/text.fnt"), true);
        fontRate.getData().setScale(.25f, -.25f);


        float score = PlayState.getScoreFloat();
        bestScore = Math.max(prefs.getInteger("bestScore"), Integer.parseInt(PlayState.getScore()));
        prefs.putInteger("bestScore", bestScore);
        prefs.putFloat("totalRuns", (prefs.getFloat("totalRuns") + 1f));
        prefs.flush();
        averageScore = (prefs.getFloat("averageScore") * (prefs.getFloat("totalRuns") - 1f) + score)
                / (prefs.getFloat("totalRuns"));
        prefs.putFloat("averageScore", averageScore);
        prefs.flush();
    }

    private Vector3 setV3() {
        Vector3 v3 = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
        cam.unproject(v3);
        return v3;
    }

    @Override
    protected void handleInput() {
        if (startBtn <= System.currentTimeMillis()) {
            if (simplePlay.isClicked((int) setV3().x, (int) setV3().y) && Gdx.input.isTouched()) {
                simplePlay.isTouchDown((int) setV3().x, (int) setV3().y);
            } else {
                if (simplePlay.isTouchUp((int) setV3().x, (int) setV3().y)) {
                    transition.play();
                    gsm.set(new GetReadyState(gsm));
                }
            }
            if (simpleHome.isClicked((int) setV3().x, (int) setV3().y) && Gdx.input.isTouched()) {
                simpleHome.isTouchDown((int) setV3().x, (int) setV3().y);
            } else {
                if (simpleHome.isTouchUp((int) setV3().x, (int) setV3().y)) {
                    transition.play();
                    gsm.set(new MenuState(gsm));
                }
            }
            if (simpleRate.isClicked((int) setV3().x, (int) setV3().y) && Gdx.input.isTouched()) {
                simpleRate.isTouchDown((int) setV3().x, (int) setV3().y);
            } else {
                if (simpleRate.isTouchUp((int) setV3().x, (int) setV3().y)) {
                    Gdx.net.openURI("https://play.google.com/store/apps/details?id=com.alex.wigglebird");
                }
            }
        }
    }

    @Override
    public void update(float dt) {
        handleInput();
        if (posFont.y > 0) velocity.add(0, GRAVITY);
        velocity.scl(dt);
        posFont.add(0, velocity.y);
        posShadow.add(0, velocity.y);
        velocity.scl(1 / dt);
        if (posFont.y < (cam.position.y + cam.viewportHeight / 2.75f)) {
            posFont.y = (cam.position.y + cam.viewportHeight / 2.75f);
            posShadow.y = (cam.position.y + cam.viewportHeight / 2.75f) - 2;
        }

        if (startTXT <= System.currentTimeMillis()) {
            posTafel.add(0, 500 * dt);
            if (posTafel.y > cam.viewportHeight / 2) posTafel.y = cam.viewportHeight / 2;
        }

    }

    @Override
    public void render(SpriteBatch sb) {
        sb.setProjectionMatrix(cam.combined);
        sb.begin();
        if (!com.alex.wigglebird.FlappyDemo.IS_NIGHT) sb.draw(bg, cam.position.x - (cam.viewportWidth / 2), -50);
        else sb.draw(bgNight, cam.position.x - (cam.viewportWidth / 2), -50);
        sb.draw(tafel, posTafel.x, posTafel.y, tafel.getWidth() / 1.35f, tafel.getHeight() / 1.35f);

        font.setColor(Color.SCARLET);
        shadow.draw(sb, "GAME OVER", posShadow.x, posShadow.y);
        font.draw(sb, "GAME OVER", posFont.x, posFont.y);

        scoreShadow.draw(sb, PlayState.getScore(), posTafel.x * 4.75f + 2, posTafel.y * 1.4f - 2);
        scoreFont.draw(sb, PlayState.getScore(), posTafel.x * 4.75f, posTafel.y * 1.4f);

        fontTafelShadow.draw(sb, String.format("%.1f", averageScore), posTafel.x * 2 + 2, posTafel.y * 1.4f - 2);
        fontTafel.draw(sb, String.format("%.1f", averageScore), posTafel.x * 2, posTafel.y * 1.4f);

        fontTafelShadow.draw(sb, Integer.toString(bestScore), posTafel.x * 2 + 2, posTafel.y * 1.15f - 2);
        fontTafel.draw(sb, Integer.toString(bestScore), posTafel.x * 2, posTafel.y * 1.15f);

        fontTafelRot.setColor(Color.GOLDENROD);
        fontTafelRot.draw(sb, "AVG.", posTafel.x * 2, posTafel.y * 1.5f);
        fontTafelRot.draw(sb, "SCORE", posTafel.x * 4.75f, posTafel.y * 1.5f);
        fontTafelRot.draw(sb, "BEST", posTafel.x * 2, posTafel.y * 1.25f);

        if (startBtn <= System.currentTimeMillis()) {
            simpleHome.draw(sb);
            simplePlay.draw(sb);
            simpleRate.draw(sb);
            fontRate.draw(sb, "RATE", cam.position.x * 1.325f, cam.position.y - cam.viewportHeight / 22);
        }
        sb.end();
    }

    @Override
    public void dispose() {
        bg.dispose();
        bgNight.dispose();
        font.dispose();
        shadow.dispose();
        fontTafel.dispose();
        fontTafelShadow.dispose();
        fontTafelRot.dispose();
        scoreFont.dispose();
        scoreShadow.dispose();
        homeBtn.dispose();
        playBtn.dispose();
        homeBtnDown.dispose();
        playBtnDown.dispose();
        transition.dispose();
        rateBtn.dispose();
        rateBtnDown.dispose();
        fontRate.dispose();
    }

    @Override
    public void pause() {

    }

}
