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

package osprey.competition;

import java.io.File;
import java.util.Properties;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.LogManager;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Separator;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import osprey.competition.knockout.ImageViewer;
import osprey.competition.knockout.Model;
import osprey.competition.knockout.StateStore;

public class Knockout extends Application {

	private final static Logger log = Logger.getLogger(Knockout.class.getName());

	Model model;
    ScrollPane s1; 
	TextFlow textArea;

	public void appendFeedback(String s) {
		Platform.runLater(() -> {
			Text t1 = new Text();
            t1.setText(s);
            if (s.startsWith("!!")){
                t1.setFill(Color.RED);
            }

            textArea.getChildren().addAll(t1,new Text("\n"));
            s1.setVvalue(1.0);
		});

	}

	@Override
	public void start(Stage stage) {
		log.info("Knockout::Start >");
		// 5 pixels space between child nodes
		VBox vb = new VBox();
		vb.setPadding(new Insets(10, 50, 50, 50));
		vb.setSpacing(20);
		textArea = new TextFlow();
		Label dirLabel = new Label(
			"Step 1: Images");
		dirLabel.setMinWidth(120);
		

		DirectoryChooser directoryChooser = new DirectoryChooser();
		String startingDir = "";//StateStore.getStateStore().getDataDir();
		if (startingDir == null || startingDir.trim().length() == 0) {
			startingDir = System.getProperty("user.home");
		}
		directoryChooser.setInitialDirectory(new File(startingDir));

		Button dirButton = new Button("Select directory with all images...");
		dirButton.setMinWidth(300);
		dirButton.setOnAction(e -> {
			File selectedDirectory = directoryChooser.showDialog(stage);
			appendFeedback("Selected Directory: " + selectedDirectory.getAbsolutePath());
			StateStore.getStateStore().put("src.image.dir", selectedDirectory.getAbsolutePath());

		});

		Label titleLabel = new Label("Step 2: Title");
		titleLabel.setMinWidth(120);

		String mainTitle = StateStore.getStateStore().getProperty("competition.title");
		TextField titleField = new TextField(mainTitle);
		titleField.setMinWidth(300);

		Label loadLabel = new Label("Step 3: Check");
		loadLabel.setMinWidth(120);
		Button loadButton = new Button("Load and check images...");
		loadButton.setMinWidth(300);
		loadButton.setOnAction(e -> {
			try {

				this.model = new Model();
				model.setMainTitle(titleField.getText());
				StateStore.getStateStore().setProperty("competition.title", model.getMainTitle());
				model.setFeedbackSupplier(this::appendFeedback);
				new Thread(() -> {
					try {
						model.init();
					} catch (Exception e1) {
						throw new RuntimeException(e1);
					}

				}).start();

			} catch (Exception e1) {
				throw new RuntimeException(e1);
			}
		});

		Label runLabel = new Label("Step 4: Compete");
		runLabel.setMinWidth(120);

		Button runButton = new Button("Run competition...");
		runButton.setMinWidth(300);
		runButton.setOnAction(e -> {

			StateStore.getStateStore().write();

			ImageViewer im = new ImageViewer();
			im.init(this.model);

			Stage newStage = new Stage();
			stage.close();
			im.start(newStage);

		});
		final Text text1 = new Text(25, 25, "Osprey Knockout v0.2");
		text1.setFill(Color.web("#564787", 1.0));
		text1.setFont(Font.font(java.awt.Font.SANS_SERIF, 25));


        s1 = new ScrollPane();
        s1.setContent(textArea);
        s1.setPrefHeight(300);
        s1.setVmax(1.0);
		vb.getChildren().addAll(text1, new HBox(10, dirLabel, dirButton), new HBox(10, titleLabel, titleField),
				new HBox(10, loadLabel, loadButton), new HBox(10, runLabel, runButton),
				new Separator(Orientation.HORIZONTAL), s1);

		Scene scene = new Scene(new StackPane(vb));

		// stage.setTitle("Osprey Knockout");
		stage.getIcons().add(new Image(Knockout.class.getResourceAsStream("/Osprey-logo-v3-trans.png")));
		stage.setScene(scene);
		stage.show();

	}

	public static void main(String[] args) {
		try {
			System.setProperty("java.util.logging.SimpleFormatter.format",
					"%1$tY-%1$tm-%1$td %1$tH:%1$tM:%1$tS %4$-6s %2$s %5$s%6$s%n");

			Logger rootLogger = LogManager.getLogManager().getLogger("");
			Handler fd = new FileHandler(StateStore.getStateStore().getDataFile());
			fd.setFormatter(new SimpleFormatter());
			rootLogger.addHandler(fd);

			Properties p = System.getProperties();
			p.forEach((key, value) -> {
				rootLogger.info(key + "==" + value);
			});
			launch();
		} catch (Throwable e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}