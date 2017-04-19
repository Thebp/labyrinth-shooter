package gruppe5.resourcemanager;

import gruppe5.common.resources.ResourceSPI;
import java.io.File;
import org.openide.util.lookup.ServiceProvider;

/**
 *
 * @author Daniel
 */
@ServiceProvider(service = ResourceSPI.class)
public class ResourceProcessor implements ResourceSPI {

    File f = new File("");
    String absolutePath = f.getAbsolutePath();
    String shortenedPath;
    String thisModulePath;

    @Override
    public String getResourceUrl(String string) {
        thisModulePath = string;
        return assetString();

    }

    private String correctAbsolutePath() {
        if (absolutePath.endsWith("application\\target\\labyrinthshooter")) {
            //removes the 
            shortenedPath = absolutePath.replace("application\\target\\labyrinthshooter", "");

            //replaces \ with / in the url.
            return shortenedPath.replaceAll("\\\\", "/");

        }
        return "";
    }

    public String assetString() {
        return correctAbsolutePath() + thisModulePath;
    }

}
