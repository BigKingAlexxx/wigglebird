package com.alex.wigglebird.states;

import com.alex.wigglebird.SimpleButton;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector3;


/**
 * Created by elexx on 01.06.2017.
 */

public class MenuState extends State {

    private Texture background;
    private Texture playBtn;
    /*private Texture volumeOn;
    private Texture volumeOff;*/
    private Texture playBtnDown;

    private SimpleButton simpleButton;
    private ShapeRenderer rect;
    private ShapeRenderer rectBegin;
    private float alphaBegin = 1;
    private float alpha = 0;
    private boolean isTransition = false;
    private Sound transition;

    public MenuState(GameStateManager gsm) {
        super(gsm);
        cam.setToOrtho(false, com.alex.wigglebird.FlappyDemo.WIDTH / 2, com.alex.wigglebird.FlappyDemo.HEIGHT / 2);
        background = new Texture("bg_0.1.png");
        playBtn = new Texture("playbtn.png");
        playBtnDown = new Texture("playbtn_pressed.png");
        /*volumeOn = new Texture("volume_on.png");
        volumeOff = new Texture("volume_off.png");*/
        simpleButton = new SimpleButton(cam.position.x - playBtn.getWidth() / 4, cam.position.y, playBtn.getWidth() / 2, playBtn.getHeight() / 2, playBtn, playBtnDown);
        rect = new ShapeRenderer();
        rect.setColor(0, 0, 0, 0);
        rectBegin = new ShapeRenderer();
        rectBegin.setColor(0, 0, 0, 1);
        transition = Gdx.audio.newSound(Gdx.files.internal("transition.ogg"));
    }

    private Vector3 setV3() {
        Vector3 v3 = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
        cam.unproject(v3);
        return v3;
    }

    @Override
    public void handleInput() {
        if (simpleButton.isClicked((int) setV3().x, (int) setV3().y) && Gdx.input.isTouched()) {
            simpleButton.isTouchDown((int) setV3().x, (int) setV3().y);
        } else {
            if (simpleButton.isTouchUp((int) setV3().x, (int) setV3().y)) {
                isTransition = true;
                transition.play();
            }
        }
    }

    @Override
    public void update(float dt) {
        handleInput();
        if (isTransition) {
            alpha += Gdx.graphics.getDeltaTime() / 0.5f;
            rect.setColor(0, 0, 0, alpha);
        }
        if (alpha > 1) gsm.set(new GetReadyState(gsm));
        alphaBegin -= Gdx.graphics.getDeltaTime() / 0.5f;
        rectBegin.setColor(0, 0, 0, alphaBegin);
    }

    @Override
    public void render(SpriteBatch sb) {
        sb.setProjectionMatrix(cam.combined);
        sb.begin();
        sb.draw(background, 0, -50);
        /*Vector3 input = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
        cam.unproject(input);
        float volumeOnXPos = cam.position.x * 1.77f;
        float volumeOnYPos = cam.position.y * 1.86f;
        float volumeOnXPosEnd = (cam.position.x * 1.77f) + (volumeOn.getWidth() / 10);
        float volumeOnYPosEnd = (cam.position.y * 1.86f) + (volumeOn.getHeight() / 10);
        if ((input.x >= volumeOnXPos && input.x <= volumeOnXPosEnd) && (input.y >= volumeOnYPos && input.y <= volumeOnYPosEnd) && Gdx.input.justTouched() && com.alex.wigglebird.FlappyDemo.MASTER_SOUND_ON)
            com.alex.wigglebird.FlappyDemo.MASTER_SOUND_ON = false;
        else if ((input.x >= volumeOnXPos && input.x <= volumeOnXPosEnd) && (input.y >= volumeOnYPos && input.y <= volumeOnYPosEnd) && Gdx.input.justTouched() && !com.alex.wigglebird.FlappyDemo.MASTER_SOUND_ON)
            com.alex.wigglebird.FlappyDemo.MASTER_SOUND_ON = true;
        if (com.alex.wigglebird.FlappyDemo.MASTER_SOUND_ON)
            sb.draw(volumeOn, cam.position.x * 1.77f, cam.position.y * 1.86f, volumeOn.getWidth() / 10, volumeOn.getHeight() / 10);
        else
            sb.draw(volumeOff, cam.position.x * 1.77f, cam.position.y * 1.86f, volumeOff.getWidth() / 10, volumeOff.getHeight() / 10);*/
        simpleButton.draw(sb);
        sb.end();
        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        rect.begin(ShapeRenderer.ShapeType.Filled);
        rect.setProjectionMatrix(cam.combined);
        rect.rect(cam.position.x - cam.viewportWidth / 2, cam.position.y - cam.viewportHeight / 2,
                cam.viewportWidth, cam.viewportHeight);
        rect.end();
        rectBegin.begin(ShapeRenderer.ShapeType.Filled);
        rectBegin.setProjectionMatrix(cam.combined);
        rectBegin.rect(cam.position.x - cam.viewportWidth / 2, cam.position.y - cam.viewportHeight / 2,
                cam.viewportWidth, cam.viewportHeight);
        rectBegin.end();
    }

    @Override
    public void dispose() {
        background.dispose();
        playBtn.dispose();
        playBtnDown.dispose();
        /*volumeOn.dispose();
        volumeOff.dispose();*/
        rect.dispose();
        rectBegin.dispose();
        transition.dispose();
    }

    @Override
    public void pause() {
    }
}
