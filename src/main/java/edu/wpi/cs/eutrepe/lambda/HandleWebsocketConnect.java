package edu.wpi.cs.eutrepe.lambda;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.nio.charset.Charset;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.RequestStreamHandler;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

import edu.wpi.cs.eutrepe.db.WebsocketDao;
import edu.wpi.cs.eutrepe.dto.ConnectionDto;
import edu.wpi.cs.eutrepe.dto.WebsocketRegisterDto;
import edu.wpi.cs.eutrepe.ws.WebsocketUtil;

public class HandleWebsocketConnect implements RequestStreamHandler {

	LambdaLogger logger;

	@Override
	public void handleRequest(InputStream input, OutputStream output, Context context) throws IOException {
		logger = context.getLogger();

		BufferedReader reader = new BufferedReader(new InputStreamReader(input, Charset.forName("US-ASCII")));
		PrintWriter writer = new PrintWriter(
				new BufferedWriter(new OutputStreamWriter(output, Charset.forName("US-ASCII"))));
		try 
		{
			JsonObject event = new GsonBuilder().create().fromJson(reader, JsonObject.class);      
			JsonObject requestContext = (JsonObject) event.get("requestContext");
			String connectionId = requestContext.get("connectionId").getAsString();
			logger.log("Connection:" + connectionId);
			
			WebsocketRegisterDto message = new Gson().fromJson(event.get("body").getAsString(), WebsocketRegisterDto.class);
			ConnectionDto connection = new ConnectionDto(0, connectionId ,message.snippetId);
			int key = new WebsocketDao().addConnection(connection);
			logger.log("DatabaseKey:" + key);
				        
	        new WebsocketUtil(logger).notifyUsers(message.snippetId, "Hello, world!");
	        
			int statusCode = key > 0 ? 201: 500;
	        String response = "{\"statusCode\": " + statusCode + ",  \"connectionId\": \"" + connectionId + "\"}" ;	        
	        writer.write(response + "\r\n");
		}
		catch (Exception e) {
			logger.log("Error:" + e.toString());
		}
		writer.write("{\"statusCode\": 500}");
	}

}
