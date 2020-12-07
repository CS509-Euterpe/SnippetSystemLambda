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
import com.amazonaws.services.lambda.runtime.RequestStreamHandler;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

import edu.wpi.cs.eutrepe.db.SnippetDao;
import edu.wpi.cs.eutrepe.http.DeleteSnippetResponse;
import edu.wpi.cs.eutrepe.ws.WebsocketUtil;

public class HandleDeleteSnippet implements RequestStreamHandler {
	final String successMessage = "Successfully deleted snippet";
	final String failureMessage = "Failed to delete snippet";
	LambdaLogger logger;
	
    @Override
    public void handleRequest(InputStream input, OutputStream output, Context context) throws IOException {
    	logger = context.getLogger();
		BufferedReader reader = new BufferedReader(new InputStreamReader(input, Charset.forName("US-ASCII")));
		PrintWriter writer = new PrintWriter(
				new BufferedWriter(new OutputStreamWriter(output, Charset.forName("US-ASCII"))));
       
		JsonObject event = new GsonBuilder().create().fromJson(reader, JsonObject.class);
		SnippetDao snippetDao = new SnippetDao();
		logger.log(event.toString());
		DeleteSnippetResponse response = new DeleteSnippetResponse();
		
		if (event.get("id") != null) {
            Integer id = new Gson().fromJson(event.get("id"), Integer.class);
            try {
            	response.setContent(snippetDao.deleteSnippet(id));
            	response.setHttpCode(200);
            	response.setMsg(successMessage);
				writer.write(new Gson().toJson(response));
				new WebsocketUtil(logger).notifyUsers(id,
						"{\"eventType\":\"snippet\", \"snippetId\":" + id + "}");
			} catch (Exception e) {
				logger.log(e.getMessage());
				e.printStackTrace();
            	response.setContent(false);
            	response.setHttpCode(500);
            	response.setMsg(failureMessage);
				writer.write(new Gson().toJson(response));
			} finally {
				reader.close();
				writer.close();
			}
		}
    }

}
