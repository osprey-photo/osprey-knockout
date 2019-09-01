package osprey.competition;

import java.nio.file.Paths;
// import osprey.competition.rest.*;
import osprey.competition.knockout.*;
import javafx.application.Application;
import java.net.*;
import java.util.Enumeration;
public class App {

    public static void main(String[] args) throws Exception {
        Enumeration e = NetworkInterface.getNetworkInterfaces();
        while (e.hasMoreElements()) {
            NetworkInterface n = (NetworkInterface) e.nextElement();
            Enumeration ee = n.getInetAddresses();
            while (ee.hasMoreElements()) {
                InetAddress i = (InetAddress) ee.nextElement();
                System.out.println(i.getHostAddress());
            }
        }
        System.setProperty("java.util.logging.SimpleFormatter.format",
                "%1$tY-%1$tm-%1$td %1$tH:%1$tM:%1$tS %4$-6s %2$s %5$s%6$s%n");
        // Launch the gui interface
        // Application.launch(ImageViewer.class, args);
    }
}
