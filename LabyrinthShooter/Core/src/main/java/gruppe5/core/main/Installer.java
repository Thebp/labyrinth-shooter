/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gruppe5.core.main;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import org.openide.modules.ModuleInstall;

public class Installer extends ModuleInstall {
    
    private static Game g;

    @Override
    public void restored() {
        g = new Game();

        LwjglApplicationConfiguration cfg = new LwjglApplicationConfiguration();
        cfg.title = "Labyrinth Shooter";
        cfg.width = 1000;
        cfg.height = 800;
        cfg.useGL30 = false;
        cfg.resizable = true;

        new LwjglApplication(g, cfg);
    }

}
