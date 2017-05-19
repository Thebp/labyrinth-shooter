package gruppe5.labyrinthshooter;

import gruppe5.common.services.IEntityProcessingService;
import gruppe5.common.services.IGamePluginService;
import java.util.List;
import java.util.logging.Level;
import junit.framework.Test;
import org.netbeans.junit.NbModuleSuite;
import org.netbeans.junit.NbTestCase;
import org.openide.util.Lookup;

public class LoadingUnloadingTest extends NbTestCase {

    public static Test suite() {
        return NbModuleSuite.createConfiguration(LoadingUnloadingTest.class).
                gui(false).
                failOnMessage(Level.WARNING). // works at least in RELEASE71
                failOnException(Level.INFO).
                enableClasspathModules(false). 
                clusters(".*").
                suite(); // RELEASE71+, else use NbModuleSuite.create(NbModuleSuite.createConfiguration(...))
    }

    public LoadingUnloadingTest(String n) {
        super(n);
    }
    
    public void testApplication() {
        assert(true);
    }
    

    private void waitForUpdate(List<IEntityProcessingService> processors,List<IGamePluginService> plugins) throws InterruptedException{
        Thread.sleep(10000);
        processors.clear();
        processors.addAll(Lookup.getDefault().lookupAll(IEntityProcessingService.class));
        
        plugins.clear();
        plugins.addAll(Lookup.getDefault().lookupAll(IGamePluginService.class));
    }

}
