package com.alex.wigglebird.states;

import com.alex.wigglebird.sprites.Bird;
import com.alex.wigglebird.sprites.Ground;
import com.alex.wigglebird.sprites.Tube;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Array;

import static com.badlogic.gdx.Gdx.audio;

/**
 * Created by elexx on 01.06.2017.
 */

public class PlayState extends State {

    private static final int TUBE_SPACING = 80;
    private static final int TUBE_COUNT = 4;
    private static final int GROUND_Y_OFFSET = -50;

    private Bird bird;
    private Texture bg, bgNight;
    private Array<Tube> tubes;
    public int flag;
    private boolean firstTubes = true;
    private long start = System.currentTimeMillis() + 2000;
    private Array<Ground> grounds;
    private static int score;
    private static float scoreFloat;
    private BitmapFont font, shadow;
    private GlyphLayout glyphFont, glyphShadow;
    private Sound hit, coin;

    public PlayState(com.alex.wigglebird.states.GameStateManager gsm) {
        super(gsm);
        bird = new Bird(90, 180);
        cam.setToOrtho(false, com.alex.wigglebird.FlappyDemo.WIDTH / 2, com.alex.wigglebird.FlappyDemo.HEIGHT / 2);
        bg = new Texture("bg_0.1.png");
        bgNight = new Texture("bg_night.png");
        tubes = new Array<Tube>();
        /*music = audio.newMusic(Gdx.files.internal("music.mp3"));
        music.setLooping(true);
        music.setVolume(0.1f);
        if (com.alex.wigglebird.FlappyDemo.MASTER_SOUND_ON) music.play();*/
        grounds = new Array<Ground>();
        grounds.add(new Ground(cam.position.x - (cam.viewportWidth / 2)));
        grounds.add(new Ground(grounds.get(0).getTexture().getWidth()));
        score = 0;
        scoreFloat = 0f;
        font = new BitmapFont(Gdx.files.internal("font/text.fnt"), true);
        font.getData().setScale(.35f, -.35f);
        shadow = new BitmapFont(Gdx.files.internal("font/shadow.fnt"), true);
        shadow.getData().setScale(.35f, -.35f);
        font.setUseIntegerPositions(false);
        shadow.setUseIntegerPositions(false);
        glyphFont = new GlyphLayout(font, Integer.toString(score));
        glyphShadow = new GlyphLayout(shadow, Integer.toString(score));
        bird.jump();
        hit = Gdx.audio.newSound(Gdx.files.internal("hit17.ogg"));
        coin = Gdx.audio.newSound(Gdx.files.internal("coin.ogg"));
    }

    @Override
    protected void handleInput() {
        if (Gdx.input.justTouched()) bird.jump();
    }

    @Override
    public void update(float dt) {
        if (flag == 0) {
            handleInput();
            bird.update(dt);
            cam.position.x = bird.getPosition().x + 50;
            for (int i = 0; i < grounds.size; i++) {
                grounds.get(i).update();
            }
            repositionGround();

            if (start <= System.currentTimeMillis()) {
                if (firstTubes) {
                    firstTubes();
                    firstTubes = false;
                }
            }

            for (int i = 0; i < tubes.size; i++) {
                Tube tube = tubes.get(i);
                if (cam.position.x - (cam.viewportWidth / 2) > tube.getPosTopTube().x + tube.getTopTube().getWidth() / 1.25f) {
                    tube.reposition(tube.getPosTopTube().x + ((Tube.TUBE_WIDTH + TUBE_SPACING) * TUBE_COUNT));
                    tube.test = false;
                }

                if (tube.collides(bird.getBounds())) {
                    hit.play(0.25f);
                    com.alex.wigglebird.FlappyDemo.toggleAd();
                    gsm.set(new com.alex.wigglebird.states.GameOverState(gsm));
                }

                if (!tube.test && tube.getRectScoreX() < bird.getPosition().x) {
                    tube.test = true;
                    coin.play(0.5f);
                    score += 1;
                    scoreFloat += 1f;
                }
            }

            if (bird.getPosition().y <= grounds.get(0).getTexture().getHeight() + GROUND_Y_OFFSET) {
                hit.play(0.25f);
                com.alex.wigglebird.FlappyDemo.toggleAd();
                gsm.set(new com.alex.wigglebird.states.GameOverState(gsm));
            }
            cam.update();
        } else {
            //gsm.push(new GameOverState(gsm));
            flag = 0;
        }

    }


    @Override
    public void render(SpriteBatch sb) {
        sb.setProjectionMatrix(cam.combined);
        sb.begin();
        if (!com.alex.wigglebird.FlappyDemo.IS_NIGHT) sb.draw(bg, cam.position.x - (cam.viewportWidth / 2), -50);
        else sb.draw(bgNight, cam.position.x - (cam.viewportWidth / 2), -50);
        /*sb.draw(bird.getTexture(), bird.getPosition().x, bird.getPosition().y, bird.getTexture().getTexture().getWidth() / 3.75f,
                bird.getTexture().getTexture().getHeight() / 1.25f);*/
        sb.draw(bird.getTexture(), bird.getPosition().x, bird.getPosition().y, bird.getTexture().getTexture().getWidth() / 7.5f,
                bird.getTexture().getTexture().getHeight() / 2.5f, bird.getTexture().getTexture().getWidth() / 3.75f,
                bird.getTexture().getTexture().getHeight() / 1.25f, 1, 1, bird.getRotation());
        for (Tube tube : tubes) {
            sb.draw(tube.getTopTube(), tube.getPosTopTube().x, tube.getPosTopTube().y, tube.getTopTube().getWidth() / 1.25f,
                    tube.getTopTube().getHeight() / 1.25f);
            sb.draw(tube.getBottomTube(), tube.getPosBotTube().x, tube.getPosBotTube().y, tube.getBottomTube().getWidth() / 1.25f,
                    tube.getBottomTube().getHeight() / 1.25f);
        }
        for (Ground ground : grounds) {
            sb.draw(ground.getTexture(), ground.getPosGround().x, ground.getPosGround().y);
        }
        glyphFont.setText(font, Integer.toString(score));
        glyphShadow.setText(shadow, Integer.toString(score));
        float width = glyphFont.width;
        shadow.draw(sb, Integer.toString(score), (cam.position.x + 2) - (width / 2), (cam.position.y + cam.viewportHeight / 2.5f) - 2);
        font.draw(sb, Integer.toString(score), (cam.position.x) - (width / 2), (cam.position.y + cam.viewportHeight / 2.5f));
        sb.end();
    }

    @Override
    public void dispose() {
        bg.dispose();
        bird.dispose();
        for (Tube tube : tubes) {
            tube.dispose();
        }

        //music.dispose();
        for (Ground ground : grounds) {
            ground.dispose();
        }
        font.dispose();
        shadow.dispose();
        bgNight.dispose();
        hit.dispose();
    }

    private void repositionGround() {
        if (cam.position.x - cam.viewportWidth / 2 > grounds.get(0).getPosGround().x + grounds.get(0).getTexture().getWidth()) {
            grounds.get(0).setPosGround(grounds.get(1).getPosGround().x + grounds.get(1).getTexture().getWidth());
        }
        if (cam.position.x - cam.viewportWidth / 2 > grounds.get(1).getPosGround().x + grounds.get(1).getTexture().getWidth()) {
            grounds.get(1).setPosGround(grounds.get(0).getPosGround().x + grounds.get(1).getTexture().getWidth());
        }

    }

    public static String getScore() {
        return Integer.toString(score);
    }

    public static Float getScoreFloat() {
        return scoreFloat;
    }

    @Override
    public void pause() {
        flag = 1;
    }

    private void firstTubes() {
        /*for (int i = 1; i <= TUBE_COUNT; i++) {
            tubes.add(new Tube(i * (TUBE_SPACING + Tube.TUBE_WIDTH)));
        }*/

        tubes.add(new Tube(cam.position.x + (cam.viewportWidth / 2)));
        tubes.add(new Tube(tubes.get(0).getPosTopTube().x + TUBE_SPACING + Tube.TUBE_WIDTH));
        tubes.add(new Tube(tubes.get(1).getPosTopTube().x + TUBE_SPACING + Tube.TUBE_WIDTH));
        tubes.add(new Tube(tubes.get(2).getPosTopTube().x + TUBE_SPACING + Tube.TUBE_WIDTH));
    }
}
