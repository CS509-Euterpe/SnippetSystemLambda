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
import edu.wpi.cs.eutrepe.dto.CommentDto;
import edu.wpi.cs.eutrepe.http.CommentResponse;
import edu.wpi.cs.eutrepe.http.DeleteSnippetResponse;
import edu.wpi.cs.eutrepe.ws.WebsocketUtil;

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
		System.out.println(params.toString());
		JsonObject path = (JsonObject) params.get("path");
		CommentDao commentDao = new CommentDao();
		logger.log(event.toString());
		CommentResponse response = new CommentResponse();
		
		
		if (path.get("comment-id") != null) {
            Integer id = new Gson().fromJson(path.get("comment-id"), Integer.class);
            System.out.println(id);
            try {
				boolean status = commentDao.deleteComment(id);
				System.out.println(status);
            	response.setHttpCode(200);
            	response.setMsg(successMessage);
				writer.write(new Gson().toJson(response));
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
