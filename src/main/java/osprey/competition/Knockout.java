package osprey.competition;

import java.io.File;
import java.io.IOException;
import java.util.Properties;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.LogManager;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import osprey.competition.knockout.ImageViewer;
import osprey.competition.knockout.Model;
import osprey.competition.knockout.StateStore;

public class Knockout extends Application {

    Model model;

    TextArea textArea;

    public void appendFeedback(String s){
        textArea.appendText(s);
        textArea.appendText("\n");
    }

    @Override
    public void start(Stage stage) {
        
        // 5 pixels space between child nodes
        VBox vb = new VBox();
        vb.setPadding(new Insets(10, 50, 50, 50));
        vb.setSpacing(20);
        textArea = new TextArea();          
        Label dirLabel = new Label("Step 1: Images");
        dirLabel.setMinWidth(100);
        
        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setInitialDirectory(new File(System.getProperty("user.home")));
        
        Button dirButton = new Button("Select directory with all images...");
        dirButton.setMinWidth(300);
        dirButton.setOnAction(e -> {
            File selectedDirectory = directoryChooser.showDialog(stage);
            textArea.appendText("Selected Directory: "+selectedDirectory.getAbsolutePath());
            StateStore.getStateStore().put("src.image.dir", selectedDirectory.getAbsolutePath());
        });

        Label titleLabel = new Label("Step 2: Title");
        titleLabel.setMinWidth(100);
        TextField titleField = new TextField();
        titleField.setMinWidth(300);

        Label loadLabel = new Label("Step 3: Check");
        loadLabel.setMinWidth(100);
        Button loadButton = new Button("Load and check images...");
        loadButton.setMinWidth(300);
        loadButton.setOnAction(e -> {
            try {

                this.model = new Model();
                model.setFeedbackSupplier(this::appendFeedback);
                new Thread(()->{
                         try {
                        model.init();
                    } catch (Exception e1) {
                        // TODO Auto-generated catch block
                        e1.printStackTrace();
                    }
 
                }).start();
                
            } catch (Exception e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }
        });

        Label runLabel = new Label("Step 4: Compete");
        runLabel.setMinWidth(100);
        
        Button runButton = new Button("Run competition...");
        runButton.setMinWidth(300);
        runButton.setOnAction(e -> {
            ImageViewer im = new ImageViewer();
            im.init(this.model);

            Stage newStage = new Stage();
            stage.close();
            im.start(newStage);

        });
        final Text text1 = new Text(25, 25, "Osprey Knockout v0.2");
        text1.setFill(Color.web("#564787",1.0));
        text1.setFont(Font.font(java.awt.Font.SANS_SERIF, 25));
        
        vb.getChildren().addAll(text1,new HBox(10, dirLabel, dirButton), new HBox(10, titleLabel, titleField),
                new HBox(10,loadLabel,loadButton),
                new HBox(10, runLabel, runButton),
                new Separator(Orientation.HORIZONTAL), textArea);

        Scene scene = new Scene(new StackPane(vb));

        // stage.setTitle("Osprey Knockout");
        stage.getIcons().add(new Image(Knockout.class.getResourceAsStream("/Osprey-logo-v3-trans.png")));
        stage.setScene(scene);
        stage.show();
        
    }

    public static void main(String[] args) throws SecurityException, IOException {
        System.setProperty("java.util.logging.SimpleFormatter.format",
        "%1$tY-%1$tm-%1$td %1$tH:%1$tM:%1$tS %4$-6s %2$s %5$s%6$s%n");

        Logger rootLogger = LogManager.getLogManager().getLogger("");
        Handler fd = new FileHandler(StateStore.getStateStore().getDataDir());
        fd.setFormatter(new SimpleFormatter());
        rootLogger.addHandler(fd);

        launch();
    }

    private void debugInfo(){
        Properties p = System.getProperties();
        p.forEach((key, value) -> {
            // textArea.appendText(key + " = " + value + "\n");
        });
    }

}