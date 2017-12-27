import java.nio.file.Paths;
import osprey.competition.rest.*;
import osprey.competition.knockout.*;
import javafx.application.Application;

public class App {

    public static void main(String[] args) {

        // create the model
        Model model = new Model();
		model.init();

        // create the image viewer, with the model
        Application im = new ImageViewer(model);

        // get the controller, and start up the rest server
        Server.go(im.getController(),model);

        // Launch the gui interface
        Application.launch(im);
        
    }
}
