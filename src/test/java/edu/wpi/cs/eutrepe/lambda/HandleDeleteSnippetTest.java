package edu.wpi.cs.eutrepe.lambda;

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
import edu.wpi.cs.eutrepe.dto.Language;
import edu.wpi.cs.eutrepe.dto.SnippetDto;
import edu.wpi.cs.eutrepe.http.CreateSnippetResponse;
import edu.wpi.cs.eutrepe.http.DeleteSnippetResponse;

/**
 * A simple test harness for locally invoking your Lambda function handler.
 */
public class HandleDeleteSnippetTest extends LambdaTest{

	//private static final String SAMPLE_DELETE_INPUT_STRING = "{\"foo\": \"bar\"}";
	

	@Test
	public void testHandleDeleteSnippet() throws Exception {
		// create snippet to delete
		HandleCreateSnippet createhandler = new HandleCreateSnippet();
        SnippetDto snippet = new SnippetDto();
        snippet.setContent("testContent");
        snippet.setInfo("testInfo");
        snippet.setName("testName");
        snippet.setLanguage(Language.JAVA);
        snippet.setTimestamp(LocalDate.now());
        assertNull(snippet.getId());
        
       
        
        String input = new Gson().toJson(snippet);
        InputStream inputStream = new ByteArrayInputStream(input.getBytes());
        OutputStream output = new ByteArrayOutputStream();
        createhandler.handleRequest(inputStream, output, createContext("create"));
        
        SnippetDao snippetDao = new SnippetDao();
        Integer id = snippetDao.addSnippet(snippet); 
        final String SAMPLE_DELETE_INPUT_STRING = String.format("{ \"id\": \"%d\"  }", id);
        
		// apply delete snippet
		HandleDeleteSnippet deletehandler = new HandleDeleteSnippet();
		InputStream deleteinput = new ByteArrayInputStream(SAMPLE_DELETE_INPUT_STRING.getBytes());
		OutputStream deleteoutput = new ByteArrayOutputStream();
		deletehandler.handleRequest(deleteinput, deleteoutput, createContext("delete"));
        
        
		assertNull(new SnippetDao().getSnippet(id)); 

	}

	@Test
	public void testBadPermissionHandleDeleteSnippet() throws IOException {
		
		
		//assertTrue(snippetResponse.getHttpCode().equals(403));
		
	}
	
	@Test
	public void testBadIdHandleDeleteSnippet() throws IOException {
		
		//assertTrue(snippetResponse.getHttpCode().equals(404));
		
	}
}
