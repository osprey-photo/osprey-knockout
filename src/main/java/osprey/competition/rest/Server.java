
package osprey.competition.rest;
import osprey.competition.knockout.*;
import spark.*;
import java.util.logging.Logger;
import java.util.*;
import jodd.json.*;
import java.net.InetAddress;
import java.net.UnknownHostException;
import static spark.Spark.*;

public class Server {

    Model model;
    Controller ctrl;
    JsonSerializer jsonSerializer;

    private static Logger logger = Logger.getLogger(Server.class.getName());

    public Server(Controller ctrl,Model model){
        jsonSerializer = new JsonSerializer();
       
        this.model = model;
        this.ctrl = ctrl;

        InetAddress ip;
        String hostname;
        try {
            ip = InetAddress.getLocalHost();
            logger.info("Your current IP address : " + ip.getHostAddress());
      
 
        } catch (UnknownHostException e) {
            logger.severe(e.getLocalizedMessage());
            throw new RuntimeException(e);
        }


    }

    public void go() {


        port(2319);
        exception(Exception.class, (e, req, res) -> e.printStackTrace()); // print all exceptions
        String htmlDir = java.nio.file.Paths.get(".","public").toString();
        staticFiles.externalLocation(htmlDir);

        get("/hello", (req, res) -> "Hello World");
        get("/imagesvote",  (req, res) -> getImagesVote(req,res));
        put("/imagesvote",  (req, res) -> sendVoteChoice(req,res));
        post("/imagesvote",  (req, res) -> moveToImage(req,res));
       
        get("/rounds",  (req, res) -> getRoundInfo(req,res));
        put("/rounds",  (req, res) -> startRound(req,res));

    }

    class Imgs {
        ImageWrapper one;
        ImageWrapper two;
    }

    /**
     * Puts the URls of the iamges to vote on into the response
     */
    public  String getImagesVote(Request req, Response res){
        Combatants current = model.getCurrentRound().get(model.getCurrentIndex());

        Imgs i = new Imgs();
        i.one=current.getImageOne();
        i.two=current.getImageTwo();

        String json1 = jsonSerializer.serialize(current.getImageOne());
        String json2 = jsonSerializer.serialize(current.getImageTwo());
        String json="{\"one\":"+json1+", \"two\":"+json2+"}";
        logger.info(json);
        return json;
    }

    /**
     * Get the response from the controlled and send that to the model
     */
    public String sendVoteChoice(Request req, Response res){
        JsonParser jsonParser = new JsonParser();
        Map map = jsonParser.parse(req.body());
        logger.info(map.toString());
        String vote =(String) map.get("vote");
        if (vote.equals("one")){
            ctrl.vote_one();
        } else {
            ctrl.vote_two();
        }

        return "sendVoteChoice";
    }

    public String moveToImage(Request req, Response res){
        JsonParser jsonParser = new JsonParser();
        Map map = jsonParser.parse(req.body());
        logger.info(map.toString());
        String vote = (String)map.get("move");
        if (vote.equals("next")){
            ctrl.next();
        } else {
            ctrl.back();
        }
        return "moveToImage";
    }

    public String getRoundInfo(Request req, Response res){
        return "getRoundInfo";
    }

    public String startRound(Request req, Response res){
        ctrl.start();
        return "startRound";
    }
}