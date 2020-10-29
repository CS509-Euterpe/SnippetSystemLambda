package edu.wpi.cs.eutrepe.lambda;

import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.time.LocalDate;

import org.junit.Test;

import com.google.gson.Gson;

import edu.wpi.cs.eutrepe.db.SnippetDao;
import edu.wpi.cs.eutrepe.dto.Language;
import edu.wpi.cs.eutrepe.dto.SnippetDto;
import edu.wpi.cs.eutrepe.http.CreateSnippetResponse;

/**
 * A simple test harness for locally invoking your Lambda function handler.
 */
public class HandleCreateSnippetTest extends LambdaTest {

    @Test
    public void testHandleCreateSnippet() throws Exception {
        HandleCreateSnippet handler = new HandleCreateSnippet();
        SnippetDto snippet = new SnippetDto();
        snippet.setContent("testContent");
        snippet.setInfo("testInfo");
        snippet.setName("testName");
        snippet.setLanguage(Language.PYTHON);
        snippet.setTimestamp(LocalDate.now());
        assertNull(snippet.getId());
        
        String input = new Gson().toJson(snippet);
        InputStream inputStream = new ByteArrayInputStream(input.getBytes());
        OutputStream output = new ByteArrayOutputStream();
        handler.handleRequest(inputStream, output, createContext("create"));
        CreateSnippetResponse snippetResponse = new Gson().fromJson(output.toString(), CreateSnippetResponse.class);
        SnippetDto savedSnippet = snippetResponse.getSnippet();
        assertNotNull(savedSnippet.getId());
        snippet.setId(savedSnippet.getId());
        assertEquals(savedSnippet, snippet);
        
        SnippetDao snippetDao = new SnippetDao();
		snippetDao.deleteSnippet(savedSnippet.getId());
    }
    
    @Test
    public void testBadHandleCreateSnippet() throws IOException {
        String badInput = "{\"foo\": \"bar\"}";
        InputStream inputStream = new ByteArrayInputStream(badInput.getBytes());
        OutputStream output = new ByteArrayOutputStream();
        HandleCreateSnippet handler = new HandleCreateSnippet();
        SnippetDto snippet = new SnippetDto();
        assertNull(snippet.getId());
        handler.handleRequest(inputStream, output, createContext("create"));
        CreateSnippetResponse snippetResponse = new Gson().fromJson(output.toString(), CreateSnippetResponse.class);
        assertTrue(snippetResponse.getHttpCode().equals(500));
    }
}
