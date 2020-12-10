/*
 * CS-509 Team Eutrepe AWS Application Test
 */

package edu.wpi.cs.eutrepe.lambda;

import static org.junit.Assert.*;


import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.junit.Test;

import com.google.gson.Gson;

import edu.wpi.cs.eutrepe.db.SnippetDao;
import edu.wpi.cs.eutrepe.dto.Language;
import edu.wpi.cs.eutrepe.dto.SnippetDto;
import edu.wpi.cs.eutrepe.http.SnippetResponse;

public class HandleCreateSnippetTest extends LambdaTest {

    @Test
    public void testHandleCreateSnippet() throws Exception {
        HandleCreateSnippet handler = new HandleCreateSnippet();
        //create sample snippet
        SnippetDto snippet = new SnippetDto();
        snippet.setContent("testContent");
        snippet.setInfo("testInfo");
        snippet.setName("testName");
        snippet.setLanguage(Language.PYTHON);
        snippet.setTimestamp("2020-04-21");
        snippet.setPassword("password");
        assertNull(snippet.getId());
        
        //send snippet as if coming from API gateway
        String input = new Gson().toJson(snippet);
        InputStream inputStream = new ByteArrayInputStream(input.getBytes());
        OutputStream output = new ByteArrayOutputStream();
        handler.handleRequest(inputStream, output, createContext("create"));
        SnippetResponse snippetResponse = new Gson().fromJson(output.toString(), SnippetResponse.class);
        SnippetDto savedSnippet = snippetResponse.getSnippet();
        SnippetDao snippetDao = new SnippetDao();
        
       //check that snippetResponse is success
        assertEquals(snippetResponse.getHttpCode().intValue(), 200);
        
        //check that the snippet was added
        assertNotNull(savedSnippet.getId());
        snippet.setId(savedSnippet.getId());
        assertEquals(savedSnippet, snippet);
        
        
        //check that snippet exists
        assertNotNull(snippetDao.getSnippet(savedSnippet.getId()));
        snippetDao.deleteSnippet(savedSnippet.getId());
        //check that snippet was deleted
        assertNull(snippetDao.getSnippet(savedSnippet.getId()));
        
		
    }
    //TODO Could add test with just missing password to check for 500 code
    @Test
    public void testBadHandleCreateSnippet() throws IOException {
        String badInput = "{\"foo\": \"bar\"}";
        InputStream inputStream = new ByteArrayInputStream(badInput.getBytes());
        OutputStream output = new ByteArrayOutputStream();
        HandleCreateSnippet handler = new HandleCreateSnippet();
        SnippetDto snippet = new SnippetDto();
        assertNull(snippet.getId());
        handler.handleRequest(inputStream, output, createContext("create"));
        SnippetResponse snippetResponse = new Gson().fromJson(output.toString(), SnippetResponse.class);
        assertTrue(snippetResponse.getHttpCode().equals(500));
    }
}
