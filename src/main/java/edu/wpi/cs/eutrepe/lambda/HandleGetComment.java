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
import java.util.ArrayList;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.RequestStreamHandler;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

import edu.wpi.cs.eutrepe.db.CommentDao;
import edu.wpi.cs.eutrepe.dto.CommentDto;
import edu.wpi.cs.eutrepe.ws.WebsocketUtil;

public class HandleGetComment implements RequestStreamHandler {
	LambdaLogger logger;
	
    @Override
    public void handleRequest(InputStream input, OutputStream output, Context context) throws IOException {

        logger = context.getLogger();
        BufferedReader reader = new BufferedReader(new InputStreamReader(input, Charset.forName("US-ASCII")));
 		PrintWriter writer = new PrintWriter(
 				new BufferedWriter(new OutputStreamWriter(output, Charset.forName("US-ASCII"))));
 		
 		JsonObject event = new GsonBuilder().create().fromJson(reader, JsonObject.class);
 		CommentDao commentDao = new CommentDao();
 		logger.log(event.toString());
 		
 		if (event.get("snippetId") != null) {
 			ArrayList<CommentDto> comments = new ArrayList<CommentDto>();
            Integer id = new Gson().fromJson(event.get("snippetId"), Integer.class);
            try {
 				comments = commentDao.getComments(id);
 				writer.write(new Gson().toJson(comments));
 			} catch (Exception e) {
 				logger.log(e.getMessage());
 				e.printStackTrace();
 				writer.write("Failed to get comments");
 			} finally {
 				reader.close();
 				writer.close();
 			}
 		}
     	
     }

}
