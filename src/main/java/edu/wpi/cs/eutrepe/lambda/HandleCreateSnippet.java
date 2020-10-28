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
import com.google.gson.JsonSyntaxException;

import edu.wpi.cs.eutrepe.db.SnippetDao;
import edu.wpi.cs.eutrepe.dto.SnippetDto;
import edu.wpi.cs.eutrepe.http.CreateSnippetResponse;

public class HandleCreateSnippet implements RequestStreamHandler {
	final String successMessage = "Successfully saved snippet";
	final String failureMessage = "Failed to save snippet";
	LambdaLogger logger;

	@Override
	public void handleRequest(InputStream input, OutputStream output, Context context) throws IOException {
		logger = context.getLogger();
		BufferedReader reader = new BufferedReader(new InputStreamReader(input, Charset.forName("US-ASCII")));
		PrintWriter writer = new PrintWriter(
				new BufferedWriter(new OutputStreamWriter(output, Charset.forName("US-ASCII"))));
		try {
			SnippetDto snippet = new Gson().fromJson(reader, SnippetDto.class);
			logger.log("STREAM TYPE: " + input.getClass().toString());
			logger.log("SNIPPET TYPE: " + snippet.getClass().toString());
			logger.log(snippet.toString());
			CreateSnippetResponse res = new CreateSnippetResponse();

			SnippetDao snippetDao = new SnippetDao();
			try {
				Integer id = snippetDao.addSnippet(snippet);
				if (id != null) {
					snippet.setId(id);
					res.setHttpCode(200);
					res.setMsg(successMessage);
					res.setSnippet(snippet);
				} else {
					res.setHttpCode(500);
					res.setMsg(failureMessage);
					res.setSnippet(null);
				}
			} catch (Exception e) {
				logger.log(e.getMessage());
				e.printStackTrace();
				res.setHttpCode(500);
				res.setMsg(e.getMessage());
				res.setSnippet(null);
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
