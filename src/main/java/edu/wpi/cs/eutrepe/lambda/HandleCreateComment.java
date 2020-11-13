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
			
			
			//CommentDto comment = new Gson().fromJson(reader, CommentDto.class);
			JsonObject event = new GsonBuilder().create().fromJson(reader, JsonObject.class);
			JsonObject params = (JsonObject) event.get("params");
			System.out.println(event);
			JsonObject path = (JsonObject) params.get("path");
			CommentDto comment = new CommentDto();
			comment.setSnippetID(new Gson().fromJson(path.get("snippetId"), String.class));
			comment.setText(new Gson().fromJson(path.get("text"), String.class));
			comment.setTimestamp(LocalDate.now());
			comment.setStartLine(new Gson().fromJson(path.get("startLine"), Integer.class));
			comment.setEndLine(new Gson().fromJson(path.get("endLine"), Integer.class));
			comment.setStartChar(new Gson().fromJson(path.get("startChar"), Integer.class));
			comment.setEndChar(new Gson().fromJson(path.get("endChar"), Integer.class));
			
			logger.log("STREAM TYPE: " + input.getClass().toString());
			logger.log("COMMENT TYPE: " + comment.getClass().toString());
			
			logger.log(comment.toString());
			CommentResponse res = new CommentResponse();
//
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

