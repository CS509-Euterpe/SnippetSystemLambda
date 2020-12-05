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
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.RequestStreamHandler;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

import edu.wpi.cs.eutrepe.db.CommentDao;
import edu.wpi.cs.eutrepe.db.SnippetDao;
import edu.wpi.cs.eutrepe.dto.CommentDto;
import edu.wpi.cs.eutrepe.dto.SnippetDto;

public class HandleDeleteStaleSnippets implements RequestStreamHandler {
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
//		CommentDao commentDao = new CommentDao();
//		logger.log(event.toString());
//		
//		if (path.get("comment-id") != null) {
//            Integer id = new Gson().fromJson(path.get("comment-id"), Integer.class);
//            try {
		
		int days = new Gson().fromJson(path.get("days"), Integer.class);
		
		
		
		CommentDao commentDao = new CommentDao();
  		SnippetDao snippetDao = new SnippetDao();
  		logger.log("Deleting snippets older than "+days+" days");

             try {
            	 writer.write("deleting snippets"); 
            	 
            	writer.write("deleting comments");
  				ArrayList<SnippetDto> snippets = snippetDao.getAllSnippets();
  			
//  				SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss.SSS");

  				SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
  				
  				for (SnippetDto cur: snippets)
  				{
  					Date parsedDate = dateFormat.parse(cur.getTimestamp());
  					
  					Timestamp timestamp = new java.sql.Timestamp(parsedDate.getTime());
  					Timestamp comparator = new java.sql.Timestamp(System.currentTimeMillis()-(days*86400000));
  					if(timestamp.before(comparator))
  					{
  						List<CommentDto> stalecomments = cur.getComments();
  						for(CommentDto curr: stalecomments)
  						{
  							commentDao.deleteComment(curr.getId());
  						}
  						snippetDao.deleteSnippet(cur.getId());
	
  					}
  				}
  				
  			} catch (Exception e) {
  				logger.log(e.getMessage());
  				e.printStackTrace();
  				writer.write("Failed to get all snippets");
  			} finally {
  				reader.close();
  				writer.close();
  			}
        }
    }

