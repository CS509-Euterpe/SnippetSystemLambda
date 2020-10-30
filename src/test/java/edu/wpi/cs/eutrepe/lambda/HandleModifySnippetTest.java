package edu.wpi.cs.eutrepe.lambda;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.time.LocalDate;
import java.util.ArrayList;

import org.junit.Assert;
import org.junit.Test;

import com.google.gson.Gson;

import edu.wpi.cs.eutrepe.db.SnippetDao;
import edu.wpi.cs.eutrepe.dto.CommentDto;
import edu.wpi.cs.eutrepe.dto.Language;
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
		SnippetDto snippet = new SnippetDto();
		snippet.setComments(null);
		snippet.setContent("testContent");
		snippet.setInfo("testInfo");
		snippet.setName("testName");
		snippet.setLanguage(Language.JAVA);
		snippet.setPassword("RBESUCKS");
		snippet.setTimestamp(LocalDate.now());
		
		
		SnippetDao testDAO = new SnippetDao();
		Integer testID = testDAO.addSnippet(snippet);
		
		//check that the SQL Entry is the original entry
		assertEquals(testDAO.getSnippet(testID).getContent(),snippet.getContent());
		assertEquals(testDAO.getSnippet(testID).getLanguage(),snippet.getLanguage());
		assertEquals(testDAO.getSnippet(testID).getComments(),snippet.getComments());
		
		//apply modified snippet
		HandleModifySnippet modifyhandler = new HandleModifySnippet();
		SnippetDto modifysnippet = new SnippetDto();
		modifysnippet.setContent("modifiedTestContent");
		modifysnippet.setInfo("modifiedTestInfo");
		modifysnippet.setName("modifiedTestName");
		modifysnippet.setLanguage(Language.PYTHON);
		modifysnippet.setPassword("RBESUCKS1!");
		modifysnippet.setId(testID);
		modifysnippet.setTimestamp(LocalDate.now());
		

		String modifyinput = new Gson().toJson(modifysnippet);
		InputStream modifyinputStream = new ByteArrayInputStream(modifyinput.getBytes());
		OutputStream modifyoutput = new ByteArrayOutputStream();
		modifyhandler.handleRequest(modifyinputStream, modifyoutput, createContext("create"));
		CreateSnippetResponse modifysnippetResponse = new Gson().fromJson(modifyoutput.toString(),
				CreateSnippetResponse.class);

		//check that the SQL Entry has been updated
		assertEquals(testDAO.getSnippet(testID).getContent(),modifysnippet.getContent());
		assertEquals(testDAO.getSnippet(testID).getLanguage(),modifysnippet.getLanguage());
		assertEquals(testDAO.getSnippet(testID).getComments(),modifysnippet.getComments());
		
		// delete snippet
		SnippetDao snippetDao = new SnippetDao();
		snippetDao.deleteSnippet(testID);
		assertTrue(modifysnippetResponse.getHttpCode().equals(200));
	}

	@Test
	public void testBadformatHandleModifySnippet() throws Exception {

		//create bad modified snippet
		String badInput = "{\"foo\": \"bar\"}";
		InputStream badmodifyinputStream = new ByteArrayInputStream(badInput.getBytes());
		OutputStream badmofidyoutput = new ByteArrayOutputStream();
		HandleModifySnippet badhandler = new HandleModifySnippet();
		SnippetDto badsnippet = new SnippetDto();
		
		assertNull(badsnippet.getId());
		
		badhandler.handleRequest(badmodifyinputStream, badmofidyoutput, createContext("create"));
		CreateSnippetResponse snippetResponse = new Gson().fromJson(badmofidyoutput.toString(), CreateSnippetResponse.class);
		assertTrue(snippetResponse.getHttpCode().equals(500));

	}
	
	
}
