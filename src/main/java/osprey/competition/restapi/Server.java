
package osprey.competition.rest;
import osprey.competition.knockout.*;
import spark.*;
// import spark.template.velocity.*;
import java.util.*;
import static spark.Spark.*;

public class Server {
    public static void go(Controller ctrl,Model model) {
        port(2319);
        exception(Exception.class, (e, req, res) -> e.printStackTrace()); // print all exceptions
        String htmlDir = java.nio.file.Paths.get(java.lang.System.getenv("APP_HOME"),"public").toString();
        staticFiles.externalLocation(htmlDir);

        get("/hello", (req, res) -> "Hello World");
        get("/imagesvote",  (req, res) -> getImagesVote(req,res));
        put("/imagesvote",  (req, res) -> sendVoteChoice(req,res));
        post("/imagesvote",  (req, res) -> moveToImage(req,res));
       
        get("/rounds",  (req, res) -> getRoundInfo(req,res));
        put("/rounds",  (req, res) -> startRound(req,res));

    }

    /**
     * Puts the URls of the iamges to vote on into the response
     */
    public static String getImagesVote(Request req, Response res){
        
    }

    /**
     * Get the response from the controlled and send that to the model
     */
    public static String sendVoteChoice(Request req, Response res){
        return "sendVoteChoice";
    }

    public static String moveToImage(Request req, Response res){
        return "moveToImage";
    }

    public static String getRoundInfo(Request req, Response res){
        return "getRoundInfo";
    }

    public static String startRound(Request req, Response res){
        return "startRound";
    }
}