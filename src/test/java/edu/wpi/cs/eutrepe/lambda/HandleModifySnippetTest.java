package edu.wpi.cs.eutrepe.lambda;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.time.LocalDate;

import org.junit.Assert;
import org.junit.Test;

import com.google.gson.Gson;

import edu.wpi.cs.eutrepe.db.SnippetDao;
import edu.wpi.cs.eutrepe.dto.SnippetDto;
import edu.wpi.cs.eutrepe.http.CreateSnippetResponse;
import edu.wpi.cs.eutrepe.http.ModifySnippetResponse;

/**
 * A simple test harness for locally invoking your Lambda function handler.
 */
public class HandleModifySnippetTest extends LambdaTest {

	@Test
	public void testHandleModifySnippet() throws Exception {
		// create a snippet to modify
		HandleModifySnippet handler = new HandleModifySnippet();
		SnippetDto snippet = new SnippetDto();
		snippet.setContent("testContent");
		snippet.setInfo("testInfo");
		snippet.setName("testName");
		snippet.setTimestamp(LocalDate.now());
		assertNull(snippet.getId());

		String input = new Gson().toJson(snippet);
		InputStream inputStream = new ByteArrayInputStream(input.getBytes());
		OutputStream output = new ByteArrayOutputStream();
		handler.handleRequest(inputStream, output, createContext("create"));
		ModifySnippetResponse createsnippetResponse = new Gson().fromJson(output.toString(),
				ModifySnippetResponse.class);
		SnippetDto originalSnippet = createsnippetResponse.getSnippet();
		assertEquals(originalSnippet, snippet);
		
		//apply modified snippet
		HandleModifySnippet modifyhandler = new HandleModifySnippet();
		SnippetDto modifysnippet = new SnippetDto();
		snippet.setContent("modifiedTestContent");
		snippet.setInfo("modifiedTestInfo");
		snippet.setName("modifiedTestName");
		snippet.setTimestamp(LocalDate.now());
		assertNull(snippet.getId());

		String modifyinput = new Gson().toJson(modifysnippet);
		InputStream modifyinputStream = new ByteArrayInputStream(modifyinput.getBytes());
		OutputStream modifyoutput = new ByteArrayOutputStream();
		modifyhandler.handleRequest(modifyinputStream, output, createContext("modify"));
		ModifySnippetResponse modifysnippetResponse = new Gson().fromJson(modifyoutput.toString(),
				ModifySnippetResponse.class);
		SnippetDto savedSnippet = modifysnippetResponse.getSnippet();
		assertNotNull(savedSnippet.getId());
		snippet.setId(savedSnippet.getId());
		assertEquals(savedSnippet, modifysnippet);

		// delete snippet
		SnippetDao snippetDao = new SnippetDao();
		snippetDao.deleteSnippet(savedSnippet.getId());
		assertTrue(modifysnippetResponse.getHttpCode().equals(200));
	}

	@Test
	public void testBadformatHandleModifySnippet() throws IOException {
		// create a snippet to modify
		HandleModifySnippet handler = new HandleModifySnippet();
		SnippetDto snippet = new SnippetDto();
		snippet.setContent("testContent");
		snippet.setInfo("testInfo");
		snippet.setName("testName");
		snippet.setTimestamp(LocalDate.now());
		assertNull(snippet.getId());

		String input = new Gson().toJson(snippet);
		InputStream inputStream = new ByteArrayInputStream(input.getBytes());
		OutputStream output = new ByteArrayOutputStream();
		handler.handleRequest(inputStream, output, createContext("create"));
		ModifySnippetResponse createsnippetResponse = new Gson().fromJson(output.toString(),
				ModifySnippetResponse.class);
		SnippetDto originalSnippet = createsnippetResponse.getSnippet();
		assertEquals(originalSnippet, snippet);

		//create bad modified snippet
		String badInput = "{\"foo\": \"bar\"}";
		InputStream badmodifyinputStream = new ByteArrayInputStream(badInput.getBytes());
		OutputStream badmofidyoutput = new ByteArrayOutputStream();
		HandleModifySnippet badhandler = new HandleModifySnippet();
		SnippetDto badsnippet = new SnippetDto();
		assertNull(badsnippet.getId());
		badhandler.handleRequest(badmodifyinputStream, badmofidyoutput, createContext("modify"));
		ModifySnippetResponse snippetResponse = new Gson().fromJson(badmofidyoutput.toString(), ModifySnippetResponse.class);
		assertTrue(snippetResponse.getHttpCode().equals(400));

	}
	@Test
	public void testBadidHandleModifySnippet() throws IOException {
		// create a snippet to modify
		HandleModifySnippet handler = new HandleModifySnippet();
		SnippetDto snippet = new SnippetDto();
		snippet.setContent("testContent");
		snippet.setInfo("testInfo");
		snippet.setName("testName");
		snippet.setTimestamp(LocalDate.now());
		assertNull(snippet.getId());

		String input = new Gson().toJson(snippet);
		InputStream inputStream = new ByteArrayInputStream(input.getBytes());
		OutputStream output = new ByteArrayOutputStream();
		handler.handleRequest(inputStream, output, createContext("create"));
		ModifySnippetResponse createsnippetResponse = new Gson().fromJson(output.toString(),
				ModifySnippetResponse.class);
		SnippetDto originalSnippet = createsnippetResponse.getSnippet();
		assertEquals(originalSnippet, snippet);

		//create bad id modified snippet
		String badInput = "";//TODO Add bad id from snippet
		InputStream badmodifyinputStream = new ByteArrayInputStream(badInput.getBytes());
		OutputStream badmofidyoutput = new ByteArrayOutputStream();
		HandleModifySnippet badhandler = new HandleModifySnippet();
		SnippetDto badsnippet = new SnippetDto();
		assertNull(badsnippet.getId());
		badhandler.handleRequest(badmodifyinputStream, badmofidyoutput, createContext("modify"));
		ModifySnippetResponse snippetResponse = new Gson().fromJson(output.toString(), ModifySnippetResponse.class);
		assertTrue(snippetResponse.getHttpCode().equals(404));

	}
	
}
