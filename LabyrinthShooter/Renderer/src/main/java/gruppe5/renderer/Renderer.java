/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gruppe5.renderer;
import com.badlogic.gdx.Gdx;
import gruppe5.common.data.Entity;
import gruppe5.common.data.GameData;
import gruppe5.common.data.World;
import gruppe5.common.services.IRenderService;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import org.openide.util.lookup.ServiceProvider;

@ServiceProvider(service = IRenderService.class)
/**
 *
 * @author Daniel
 */
public class Renderer implements IRenderService{
    ShapeRenderer sr;

    @Override
    public void create(GameData gameData, World world) {
        sr = new ShapeRenderer();
    }

    @Override
    public void render(GameData gameData, World world) {
        for (Entity entity : world.getEntities()) {
            float[] shapex = entity.getShapeX();
            float[] shapey = entity.getShapeY();
            if (shapex != null && shapey != null) {

                sr.setColor(1, 1, 1, 1);

                sr.begin(ShapeRenderer.ShapeType.Line);

                for (int i = 0, j = shapex.length - 1;
                        i < shapex.length;
                        j = i++) {

                    sr.line(shapex[i], shapey[i], shapex[j], shapey[j]);
                }
                sr.end();
            }
        }
    }
    
}
