package br.com.skylane.dibo;


import java.net.UnknownHostException;
import java.util.Base64;

import org.eclipse.jetty.server.Response;

import spark.Spark;
import br.com.skylane.ferramentas.email.EmailUtils;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.mongodb.util.JSON;

/**
 * Hello world!
 *
 */
public class Dibo {
	
	private static String fromHostName = "smtp.zoho.com";
	private static String fromEmail = "maicon@sixinf.com.br";
	private static String fromEmailName = "Alexandre Dibo";
	private static String fromUser = "maicon@sixinf.com.br";
	private static String fromPass = "mariana123!@";
	private static int port = 465;
	
	private static String TABLE_NAME = "aeronautas";
	
    public static void main( String[] args ) throws UnknownHostException {
    	    	
    	MongoClient mongoClient = new MongoClient();
		DB db = mongoClient.getDB("dibo");
		    	
    	Spark.staticFileLocation("/public");
        
    	Spark.get("/", (request, response) -> {
    		response.redirect("index.html");
    		return "index.html";
    	});
    	
    	Spark.post("/cadastro", (request, response) -> {
    		JsonObject jo = new JsonParser().parse(request.body()).getAsJsonObject();
    		//Cadastro c = g.fromJson(request.body(), Cadastro.class);
    		
    		DBCollection table = db.getCollection(TABLE_NAME);
    		
    		BasicDBObject searchQuery = new BasicDBObject();
    		searchQuery.put("email", jo.get("email").getAsString());
    		
    		DBCursor cursor = table.find(searchQuery);
    		if (!cursor.hasNext()) {
    			DBCollection aero = db.getCollection(TABLE_NAME);
        		DBObject document = (DBObject) JSON.parse(request.body());
        		
        		aero.insert(document);
        		
        		response.status(Response.SC_OK);
    		} else {
    			response.status(Response.SC_FOUND);
    		}
    		
    		return "";
    	});
    	
    	Spark.get("/buscarid", (request, response) -> {
    		String email = new String (Base64.getDecoder().decode(request.queryParams("id")));
    		Gson g = new Gson();
    		JsonObject jo = new JsonObject();
    		jo.addProperty("email", email);
    		response.status(Response.SC_OK);
    		response.type("application/json");
    		
    		return g.toJson(jo);
    	});
    	
    	Spark.get("/lista", (request, response) -> {
    		
    		DBCollection table = db.getCollection(TABLE_NAME);
    		DBCursor c = table.find();
    		
    		response.status(Response.SC_OK);
    		response.type("application/json");
    		
    		return JSON.serialize(c.toArray());
    	});
    	
    	Spark.get("/confirma/:id", (request, response) -> {
    		
    		String email = new String(Base64.getDecoder().decode(request.params("id")));
    		
    		DBCollection table = db.getCollection(TABLE_NAME);

    		BasicDBObject searchQuery = new BasicDBObject();
    		searchQuery.put("email", email);

    		DBCursor cursor = table.find(searchQuery);

    		if (cursor.hasNext()) {
    			BasicDBObject o = (BasicDBObject) cursor.next();
    			String nome = o.getString("nome");
    			if (!nome.isEmpty()) {
    				response.status(Response.SC_FOUND);
    			} else {
    				response.redirect("/confirma.html#/?id=" + request.params("id"));
    			}
    		}
    		
    		return "OK";
    	});
    	
    	Spark.post("/email", (request, response) -> {
    		Gson g = new Gson();
    		String email = g.fromJson(request.body(), String.class);
    		
    		DBCollection table = db.getCollection(TABLE_NAME);

    		BasicDBObject searchQuery = new BasicDBObject();
    		searchQuery.put("email", email);
    		DBCursor cursor = table.find(searchQuery);
    		if (cursor.hasNext()) {
    			BasicDBObject o = (BasicDBObject) cursor.next();
    			String nome = o.getString("nome");
    			if (!nome.isEmpty()) {
    				response.status(Response.SC_FOUND);
    				return "";
    			}
    		} else {
    			DBCollection aero = db.getCollection(TABLE_NAME);
        		BasicDBObject document = new BasicDBObject();
        		
        		document.put("email", email);
        		document.put("nome", "");    		
        		document.put("telefone", "");
        		document.put("prefixo", "");
        		aero.insert(document);
    		}
    		
    		EmailUtils eutil = new EmailUtils(fromHostName, fromEmail, fromEmailName, fromUser, fromPass, port);
    		
    		String mensagem = "Amigo Aeronauta, \r\n"
    				+ "\r\n"
    				+ "Clique no link abaixo para confirmar sua presença no aniversário do Neto do Alexandre Dibo."
    				+ "\r\n"
    				+ "http://www.sixinf.com.br:4567/confirma/" + Base64.getEncoder().encodeToString(email.getBytes()) + "\r\n" 
    				+ "\r\n"
    				+ "\r\n"
    				+ "Alexandre Dibo";
    		eutil.enviarEmail("Convidado", email, "Confirmação Aniversário Net Alexandre Dibo", mensagem);
    		return "";
    	});
        
        Spark.before((request, response) -> {
            response.header("Access-Control-Allow-Origin", "*");
            response.header("Access-Control-Request-Method", "*");
            response.header("Access-Control-Allow-Headers", "*");            
        });
    }
}
