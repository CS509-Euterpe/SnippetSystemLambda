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

import edu.wpi.cs.eutrepe.db.CommentDao;

public class HandleDeleteComment implements RequestStreamHandler {
	final String successMessage = "Successfully deleted comment";
	final String failureMessage = "Failed to delete comment";
	LambdaLogger logger;
	
    @Override
    public void handleRequest(InputStream input, OutputStream output, Context context) throws IOException {
    	logger = context.getLogger();
    	BufferedReader reader = new BufferedReader(new InputStreamReader(input, Charset.forName("US-ASCII")));
		PrintWriter writer = new PrintWriter(
				new BufferedWriter(new OutputStreamWriter(output, Charset.forName("US-ASCII"))));
       
		JsonObject event = new GsonBuilder().create().fromJson(reader, JsonObject.class);
		JsonObject params = (JsonObject) event.get("params");
		JsonObject path = (JsonObject) params.get("path");
		CommentDao commentDao = new CommentDao();
		logger.log(event.toString());
		
		if (path.get("comment-id") != null) {
            Integer id = new Gson().fromJson(path.get("comment-id"), Integer.class);
            try {
				boolean status = commentDao.deleteComment(id);
				writer.write(new Gson().toJson(status));
			} catch (Exception e) {
				logger.log(e.getMessage());
				e.printStackTrace();
				writer.write("Failed to delete snippet");
			} finally {
				reader.close();
				writer.close();
			}
		}
    }

}
