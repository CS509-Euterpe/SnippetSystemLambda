package edu.wpi.cs.eutrepe.lambda;

import static org.junit.Assert.*;

import java.sql.Date;

import org.junit.Test;

import edu.wpi.cs.eutrepe.db.SnippetDao;
import edu.wpi.cs.eutrepe.dto.SnippetDto;
import edu.wpi.cs.eutrepe.http.CreateSnippetResponse;

/**
 * A simple test harness for locally invoking your Lambda function handler.
 */
public class HandleCreateSnippetTest extends LambdaTest{

    @Test
    public void testHandleCreateSnippet() {
        HandleCreateSnippet handler = new HandleCreateSnippet();
        SnippetDto snippet = new SnippetDto();
        snippet.setContent("testContent");
        snippet.setInfo("testInfo");
        snippet.setName("testName");
        snippet.setTimestamp(new Date(0));
        assertNull(snippet.getId());
        CreateSnippetResponse snippetResponse = handler.handleRequest(snippet, createContext("create"));
        SnippetDto savedSnippet = snippetResponse.getSnippet();
        assertNotNull(savedSnippet.getId());
        snippet.setId(savedSnippet.getId());
        assertEquals(savedSnippet, snippet);
        
        SnippetDao snippetDao = new SnippetDao();
        try {
			snippetDao.deleteSnippet(savedSnippet.getId());
		} catch (Exception e) {
			e.printStackTrace();
		}
    }
    
    @Test
    public void testBadHandleCreateSnippet() {
        HandleCreateSnippet handler = new HandleCreateSnippet();
        SnippetDto snippet = new SnippetDto();
        assertNull(snippet.getId());
        CreateSnippetResponse snippetResponse = handler.handleRequest(snippet, createContext("create"));
        assertTrue(snippetResponse.getHttpCode().equals(500));
    }
}
