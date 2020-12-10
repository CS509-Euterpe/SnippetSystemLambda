/*
 * CS-509 Team Eutrepe AWS Application
 */

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

import edu.wpi.cs.eutrepe.db.SnippetDao;
import edu.wpi.cs.eutrepe.dto.SnippetDto;

public class HandleGetAllSnippets implements RequestStreamHandler {
	LambdaLogger logger;
	
    @Override
    public void handleRequest(InputStream input, OutputStream output, Context context) throws IOException {

       logger = context.getLogger();
       BufferedReader reader = new BufferedReader(new InputStreamReader(input, Charset.forName("US-ASCII")));
		PrintWriter writer = new PrintWriter(
				new BufferedWriter(new OutputStreamWriter(output, Charset.forName("US-ASCII"))));
		
		
		SnippetDao snippetDao = new SnippetDao();
		logger.log("Returning all snippets");

           try {
				ArrayList<SnippetDto> snippets = snippetDao.getAllSnippets();
				writer.write(new Gson().toJson(snippets));
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
