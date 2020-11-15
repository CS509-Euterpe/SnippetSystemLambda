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
import java.time.LocalDate;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.RequestStreamHandler;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;

import edu.wpi.cs.eutrepe.db.CommentDao;
import edu.wpi.cs.eutrepe.dto.CommentDto;
import edu.wpi.cs.eutrepe.http.CommentResponse;

public class HandleCreateComment implements RequestStreamHandler {
	final String successMessage = "Successfully created comment";
	final String failureMessage = "Failed to create comment";
	LambdaLogger logger;

    @Override
    public void handleRequest(InputStream input, OutputStream output, Context context) throws IOException {
    	logger = context.getLogger();
    	BufferedReader reader = new BufferedReader(new InputStreamReader(input, Charset.forName("US-ASCII")));
		PrintWriter writer = new PrintWriter(
				new BufferedWriter(new OutputStreamWriter(output, Charset.forName("US-ASCII"))));
       
		try {
			
			JsonObject event = new GsonBuilder().create().fromJson(reader, JsonObject.class);
			JsonObject region = (JsonObject) event.get("region");
			//CommentDto comment = new Gson().fromJson(event, CommentDto.class);
			CommentDto comment = new CommentDto();
			comment.setSnippetId(new Gson().fromJson(event.get("snippetId"), String.class));
			comment.setText(new Gson().fromJson(event.get("text"), String.class));
			comment.setTimestamp(new Gson().fromJson(event.get("timestamp"), String.class));
			comment.setName(new Gson().fromJson(event.get("name"), String.class));
			
			comment.getRegion().setStartLine(new Gson().fromJson(region.get("startLine"), Integer.class));
			comment.getRegion().setEndLine(new Gson().fromJson(region.get("endLine"), Integer.class));
			comment.getRegion().setStartChar(new Gson().fromJson(region.get("startChar"), Integer.class));
			comment.getRegion().setEndChar(new Gson().fromJson(region.get("endChar"), Integer.class));
			
			System.out.println(comment);
			logger.log("STREAM TYPE: " + input.getClass().toString());
			logger.log("COMMENT TYPE: " + comment.getClass().toString());
			
			logger.log(comment.toString());
			CommentResponse res = new CommentResponse();
			CommentDao commentDao = new CommentDao();
			try {
				Integer id = commentDao.addComment(comment);
				if (id != null) {
					comment.setId(id);
					res.setHttpCode(200);
					res.setMsg(successMessage);
					res.setComment(comment);
				} else {
					res.setHttpCode(500);
					res.setMsg(failureMessage);
					res.setComment(null);
				}
			} catch (Exception e) {
				logger.log(e.getMessage());
				e.printStackTrace();
				res.setHttpCode(500);
				res.setMsg(e.getMessage());
				res.setComment(null);
			}

			writer.write(new Gson().toJson(res));
			if (writer.checkError()) {
				logger.log("WARNING: Writer encountered an error.");
			}
		} catch (IllegalStateException | JsonSyntaxException exception) {
			logger.log(exception.toString());
		} finally {
			reader.close();
			writer.close();
		}
	}
}

