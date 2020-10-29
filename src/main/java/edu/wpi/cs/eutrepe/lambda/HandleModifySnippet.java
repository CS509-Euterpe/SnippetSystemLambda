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
import edu.wpi.cs.eutrepe.dto.SnippetDto;

public class HandleModifySnippet implements RequestStreamHandler {
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
		SnippetDto snippet = new Gson().fromJson(event.get("body-json").toString(), SnippetDto.class);
		logger.log(snippet.toString());
		JsonObject params = (JsonObject) event.get("params");
		JsonObject path = (JsonObject) params.get("path");
		String id = new Gson().fromJson(path.get("id"), String.class);
		snippet.setId(Integer.parseInt(id));
		logger.log(snippet.toString());
    }

}
