package osprey.competition.knockout;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Properties;

public class StateStore extends Properties{

    private static StateStore stateStore;

    public static StateStore getStateStore(){
        if (stateStore == null){
            stateStore = new StateStore().init();
        }
        return stateStore;
    }    
    private StateStore init(){
        
        //
        // src.image.dir=C:/Users/Matthew/Google Drive/BWPS/2017/AGM Competition
        setProperty("fullsize.width", "1267");
        setProperty("fullsize.height", "900");
        setProperty("winner.width", "700");
        setProperty("winner.height", "525");
        setProperty("src.image.dir",".");
        File propsFile;
        try {
            String appdata = System.getenv("AppData");
            if (appdata != null && !appdata.trim().isEmpty()) {
                File dataDir = new File(appdata, "ospreyknockout");
                if (!dataDir.exists()) {
                    dataDir.mkdirs();
                }
                this.dataDir = dataDir;
                propsFile = new File(dataDir, "ospreyknockout.props");
                if (!propsFile.exists()) {
                    // try w/ resources
                    store(new FileOutputStream(propsFile), "Osprey Knockout");
                }
                File cacheDir = new File(dataDir, "cache");
                if (!cacheDir.exists()) {
                    cacheDir.mkdirs();
                }
                this.cacheDir = cacheDir;

                // load the file
                load(new FileInputStream(propsFile));
            }
            return this;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }
File dataDir;
    File cacheDir;
    public String getFileCache(){
        return cacheDir.getAbsolutePath();
    }

    public String getDataDir(){
        System.out.println(dataDir);
        return new File(dataDir,"osprey.log").toString();
    }


}