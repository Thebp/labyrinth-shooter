package gruppe5.core.main;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import gruppe5.common.audio.AudioSPI;
import gruppe5.common.data.Entity;
import gruppe5.common.data.GameData;
import gruppe5.common.data.GameKeys;
import gruppe5.common.data.World;
import gruppe5.common.services.IEntityProcessingService;
import gruppe5.common.services.IGameInitService;
import gruppe5.common.services.IGamePluginService;
import gruppe5.common.services.IRenderService;
import gruppe5.common.player.PlayerSPI;
import gruppe5.common.resources.ResourceSPI;
import gruppe5.core.managers.AssetsJarFileResolver;
import gruppe5.core.managers.GameInputProcessor;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import org.openide.util.Lookup;
import org.openide.util.LookupEvent;
import org.openide.util.LookupListener;
import org.openide.util.lookup.ServiceProvider;

@ServiceProvider(service = AudioSPI.class)

public class Game implements ApplicationListener, AudioSPI {

    private ShapeRenderer sr;
    private BitmapFont bitmapfont;
    private SpriteBatch spriteBatch;
    private Sprite sprite;
    private static OrthographicCamera cam;
    private final GameData gameData = new GameData();
    private World world = new World();
    private final Lookup lookup = Lookup.getDefault();
    private List<IGamePluginService> gamePlugins = new CopyOnWriteArrayList<>();
    private List<IGameInitService> gameInits = new CopyOnWriteArrayList<>();
    private Lookup.Result<IGamePluginService> gamePluginResult;
    private Lookup.Result<IGameInitService> gameInitResult;
    private Lookup.Result<IGamePluginService> result;
    private final float displayWidth = 400;
    private final float displayHeight = 400;
    private final int worldWidth = 2000;
    private final int worldHeight = 2000;
    private AssetsJarFileResolver jfhr;
    private AssetManager am;
    private Texture texture;
    private Sound newsound;
    private Music music;

    @Override
    public void create() {

        gameData.setDisplayWidth(worldWidth);
        gameData.setDisplayHeight(worldHeight);

        cam = new OrthographicCamera(displayWidth, displayHeight);
        cam.position.set(cam.viewportWidth / 2f, cam.viewportHeight / 2f, 0);
        cam.update();

        sr = new ShapeRenderer();
        bitmapfont = new BitmapFont();
        bitmapfont.setScale(.50f, .50f);
        spriteBatch = new SpriteBatch();

        Gdx.input.setInputProcessor(new GameInputProcessor(gameData));

        gameInitResult = lookup.lookupResult(IGameInitService.class);
        gameInitResult.allItems();

        gamePluginResult = lookup.lookupResult(IGamePluginService.class);
        gamePluginResult.addLookupListener(lookupListener);
        gamePluginResult.allItems();

        for (IGameInitService initService : gameInitResult.allInstances()) {
            initService.start(gameData, world);
            gameInits.add(initService);
        }

        for (IGamePluginService plugin : gamePluginResult.allInstances()) {
            plugin.start(gameData, world);
            gamePlugins.add(plugin);
        }
//        for (IRenderService renderService : getRenderServices()) {
//            renderService.create(gameData, world);
//        }

        sprite = new Sprite();

        jfhr = new AssetsJarFileResolver();
        am = new AssetManager(jfhr);

        ResourceSPI musicSPI = Lookup.getDefault().lookup(ResourceSPI.class);
        String musicURL = musicSPI.getResourceUrl("Core/target/Core-1.0.0-SNAPSHOT.jar!/assets/sound/music.ogg");
        am.load(musicURL, Music.class);
        am.finishLoading();

        music = am.get(musicURL, Music.class);
        //music.setLooping(true);
        //music.play();
    }

    @Override
    public void render() {
        // clear screen to black
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        gameData.setDelta(Gdx.graphics.getDeltaTime());

        update();

        updateCam();

        draw();

        gameData.getKeys().update();
    }

    private void update() {
        for (IEntityProcessingService entityProcessorService : getEntityProcessingServices()) {
            entityProcessorService.process(gameData, world);
        }
        zoomCam();
    }

    private void zoomCam() {
        if (gameData.getKeys().isPressed(GameKeys.SHIFT)) {
            if (cam.viewportWidth == displayWidth) {
                cam.viewportWidth = worldWidth;
                cam.viewportHeight = worldHeight;
            } else {
                cam.viewportWidth = displayWidth;
                cam.viewportHeight = displayHeight;
            }
        }
    }

    private Entity getPlayer() {
        PlayerSPI playerSPI = Lookup.getDefault().lookup(PlayerSPI.class);
        return playerSPI.getPlayer(world);
    }

    private void updateCam() {
        cam.position.set(getPlayer().getX(), getPlayer().getY(), 0);
        cam.update();
        spriteBatch.setProjectionMatrix(cam.combined);
        sr.setProjectionMatrix(cam.combined);
    }

    private void draw() {
//        for (IRenderService renderService : getRenderServices()) {
//            renderService.render(gameData, world);
//        }
        for (Entity entity : world.getEntities()) {
            //float distance = (float) Math.sqrt(Math.pow(entity.getX() - getPlayer().getX(), 2) + Math.pow(entity.getY() - getPlayer().getY(), 2));
            //if (distance < 300) {
            sr.setColor(1, 1, 1, 1);

            sr.begin(ShapeRenderer.ShapeType.Line);

            float[] shapex = entity.getShapeX();
            float[] shapey = entity.getShapeY();

            for (int i = 0, j = shapex.length - 1;
                    i < shapex.length;
                    j = i++) {

                sr.line(shapex[i], shapey[i], shapex[j], shapey[j]);
            }

            sr.end();

            drawSprite(entity);

            //}
            drawFont();

        }
    }

    private void drawSprite(Entity entity) {
        if (entity.getImagePath() != null) {
            //uses ResourceSPI that takes entity.getImagePath as argument (string url).
            ResourceSPI spriteSPI = Lookup.getDefault().lookup(ResourceSPI.class);
            String url = spriteSPI.getResourceUrl(entity.getImagePath());

            am.load(url, Texture.class);
            am.finishLoading();
            texture = am.get(url, Texture.class);
            sprite = new Sprite(texture);
            spriteBatch.begin();
            sprite.setSize(entity.getRadius(), entity.getRadius());
            sprite.setPosition(entity.getX() - (entity.getRadius() / 2), entity.getY() - (entity.getRadius() / 2));
            sprite.setOrigin(sprite.getWidth() / 2, sprite.getHeight() / 2);
            sprite.setRotation((float) Math.toDegrees(entity.getRadians()));
            sprite.draw(spriteBatch);
            spriteBatch.end();
        }
    }

    private void drawFont() {
        spriteBatch.begin();
        bitmapfont.setColor(Color.GREEN);
        bitmapfont.drawMultiLine(spriteBatch, "LABYRINTH SHOOTER" + "\n" + "FPS: "
                + Gdx.graphics.getFramesPerSecond() + "\n" + "Entites: " + world.getEntities().size(),
                getPlayer().getX() - 180, getPlayer().getY() + 180);
        spriteBatch.end();
    }

    @Override
    public void resize(int width, int height) {
    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {
    }

    @Override
    public void dispose() {
    }

    private Collection<? extends IEntityProcessingService> getEntityProcessingServices() {
        return lookup.lookupAll(IEntityProcessingService.class);
    }

    private Collection<? extends IRenderService> getRenderServices() {
        return lookup.lookupAll(IRenderService.class);
    }

    private final LookupListener lookupListener = new LookupListener() {
        @Override
        public void resultChanged(LookupEvent le) {

            Collection<? extends IGamePluginService> updated = gamePluginResult.allInstances();

            for (IGamePluginService us : updated) {
                // Newly installed modules
                if (!gamePlugins.contains(us)) {
                    us.start(gameData, world);
                    gamePlugins.add(us);
                }
            }

            // Stop and remove module
            for (IGamePluginService gs : gamePlugins) {
                if (!updated.contains(gs)) {
                    gs.stop(gameData, world);
                    gamePlugins.remove(gs);
                }
            }
        }

    };

    @Override
    public void playAudio(String soundURL, Entity entity) {
        if (entity.getSoundPath() != null) {
            //if (newsound == null) {
            AssetsJarFileResolver ajfr = new AssetsJarFileResolver();
            ResourceSPI soundSPI = Lookup.getDefault().lookup(ResourceSPI.class);
            List<String> soundPaths = new ArrayList<>();
            soundURL = soundSPI.getResourceUrl(entity.getSoundPath());
            soundPaths.add(soundURL);
            for (int i = 0; i < soundPaths.size(); i++) {
                newsound = Gdx.audio.newSound(ajfr.resolve(soundPaths.get(i)));
                newsound.play();
            }

        }
    }
}
