package osprey.competition.knockout;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

public class StateStore extends Properties {

	private static StateStore stateStore;

	public static StateStore getStateStore() {
		if (stateStore == null) {
			stateStore = new StateStore().init();
		}
		return stateStore;
	}

	private StateStore init() {

		//
		// src.image.dir=C:/Users/Matthew/Google Drive/BWPS/2017/AGM Competition
		setProperty("fullsize.width", "1267");
		setProperty("fullsize.height", "900");
		setProperty("winner.width", "700");
		setProperty("winner.height", "525");
		setProperty("src.image.dir", ".");
		setProperty("competition.title", "Knock-out Competition");
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
					try (FileOutputStream fos = new FileOutputStream(propsFile)) {
						store(fos, "Osprey Knockout");
					}

				}
				File cacheDir = new File(dataDir, "cache");
				if (!cacheDir.exists()) {
					cacheDir.mkdirs();
				}
				this.cacheDir = cacheDir;

				// load the file
				try (FileInputStream fis = new FileInputStream(propsFile)) {
					load(fis);
				}

			}
			return this;
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}

	File dataDir;
	File cacheDir;

	public String getFileCache() {
		return cacheDir.getAbsolutePath();
	}

	public String getDataFile() {
		System.out.println(dataDir);
		return new File(dataDir, "osprey.log").toString();
	}
	public String getDataDir() {
		System.out.println(dataDir);
		return dataDir.toString();
	}
	public StateStore write() {
		File propsFile = new File(dataDir, "ospreyknockout.props");

		// try w/ resources
		try (FileOutputStream fos = new FileOutputStream(propsFile)) {
			store(fos, "Osprey Knockout");
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		
		return this;

	}

}