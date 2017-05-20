package gruppe5.labyrinthshooter;

import gruppe5.common.services.IEntityProcessingService;
import gruppe5.common.services.IGamePluginService;
import java.io.IOException;
import static java.nio.file.Files.copy;
import static java.nio.file.Paths.get;
import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.logging.Level;
import junit.framework.Test;
import org.netbeans.junit.NbModuleSuite;
import org.netbeans.junit.NbTestCase;
import org.openide.util.Lookup;

public class LoadingUnloadingTest extends NbTestCase {
    private static final String LOAD_UPDATES_FILE = "C:\\Users\\Nick\\Documents\\GitHub\\labyrinth-shooter\\test_updates\\updates_load.xml";
    private static final String NONE_UPDATES_FILE = "C:\\Users\\Nick\\Documents\\GitHub\\labyrinth-shooter\\test_updates\\updates_none.xml";
    private static final String UPDATES_FILE = "C:\\Users\\Nick\\Documents\\GitHub\\labyrinth-shooter\\netbeans_site\\updates.xml";
    
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
    

    public void testApplication() throws InterruptedException, IOException {
        List<IEntityProcessingService> processors = new CopyOnWriteArrayList();
        List<IGamePluginService> plugins = new CopyOnWriteArrayList();
        
        copy(get(NONE_UPDATES_FILE), get(UPDATES_FILE), REPLACE_EXISTING);
        waitForUpdate(processors, plugins);
        
        // Pre asserts
        assertEquals("No plugins", 0, plugins.size());
        assertEquals("No processors", 0, processors.size());
        
        
    }
    
    private void waitForUpdate(List<IEntityProcessingService> processors,List<IGamePluginService> plugins) throws InterruptedException {
        Thread.sleep(10000);
        processors.clear();
        processors.addAll(Lookup.getDefault().lookupAll(IEntityProcessingService.class));
        
        plugins.clear();
        plugins.addAll(Lookup.getDefault().lookupAll(IGamePluginService.class));
    }

}
