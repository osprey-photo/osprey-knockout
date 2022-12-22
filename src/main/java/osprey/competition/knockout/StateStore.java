// Copyright 2018-2022 Matthew B White

// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//     http://www.apache.org/licenses/LICENSE-2.0
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package osprey.competition.knockout;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

import javafx.geometry.Rectangle2D;
import javafx.stage.Screen;

public class StateStore extends Properties {

	private static StateStore stateStore;

	public static StateStore getStateStore() {
		if (stateStore == null) {
			stateStore = new StateStore().init();
		}
		return stateStore;
	}

	private StateStore init() {

		Rectangle2D screenBounds = Screen.getPrimary().getBounds();
		System.out.println(screenBounds.getWidth());
		setProperty("fullsize.width", /*"1267"*/screenBounds.getWidth()+"");
		setProperty("fullsize.height", screenBounds.getHeight()+"");//900
		setProperty("winner.width", screenBounds.getWidth()*0.7+"");//700
		setProperty("winner.height", screenBounds.getHeight()*0.7+"");//525
		setProperty("src.image.dir", ".");
		setProperty("competition.title", "Knock-out Competition");
		File propsFile;

		try {
			String appdata = System.getenv("ospreydata");
			File ospreydata;
			if (appdata == null || appdata.trim().isEmpty()) {
				ospreydata = new File(System.getProperty("user.home"), ".osprey");
				if (!ospreydata.exists()) {
					ospreydata.mkdirs();
				}
			} else {
				ospreydata = new File(appdata, ".osprey");
			}

			File dataDir = new File(ospreydata, "ospreyknockout");
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
		return new File(dataDir, "osprey.log").toString();
	}

	public String getDataDir() {
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