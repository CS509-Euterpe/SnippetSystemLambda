/*
 * CS-509 Team Eutrepe AWS Application Test
 */

package edu.wpi.cs.eutrepe.lambda;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import org.junit.Test;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import edu.wpi.cs.eutrepe.db.SnippetDao;
import edu.wpi.cs.eutrepe.dto.Language;
import edu.wpi.cs.eutrepe.dto.SnippetDto;
import edu.wpi.cs.eutrepe.http.DeleteSnippetResponse;

public class HandleDeleteSnippetTest extends LambdaTest{
	
	@Test
	public void testHandleDeleteSnippet() throws Exception {
	    	HandleDeleteSnippet handler = new HandleDeleteSnippet();
	        //create sample snippet
	        SnippetDto snippet = new SnippetDto();
	        snippet.setContent("testContent");
	        snippet.setInfo("testInfo");
	        snippet.setName("testName");
	        snippet.setLanguage(Language.PYTHON);
	        snippet.setTimestamp("2020-04-21");
	        snippet.setPassword("password");
	        //check that the snippet id is null when initializing
	        assertNull(snippet.getId());
	        
	        SnippetDao snippetDao = new SnippetDao();
	        int snippetId = snippetDao.addSnippet(snippet);
	        snippet.setId(snippetId);
	        
	        //check that snippet exists in DB
	        assertNotNull(snippetDao.getSnippet(snippetId));
	        
	        //create JSON snippet-id
	        JsonObject id = new JsonObject();
	        id.addProperty("id", snippet.getId());
	     
	    
	  
	        //send comment-ID as if coming from API Gateway 
	        String input = new Gson().toJson(id);
	        InputStream inputStream = new ByteArrayInputStream(input.getBytes());
	        OutputStream output = new ByteArrayOutputStream();
	        handler.handleRequest(inputStream, output, createContext("create"));
	        DeleteSnippetResponse snippetResponse = new Gson().fromJson(output.toString(), DeleteSnippetResponse.class);
	        

	        //check that delete comment response is success
	      
	        assertEquals(snippetResponse.getHttpCode().intValue(), 200);
	        
	        
	        //check that comment does not exist in database anymore
	        assertNull(snippetDao.getSnippet(snippet.getId()));
	    }

}
