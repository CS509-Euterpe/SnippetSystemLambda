package edu.wpi.cs.eutrepe.lambda;

import static org.junit.Assert.*;

import org.junit.Test;

import edu.wpi.cs.eutrepe.dto.SnippetDto;

/**
 * A simple test harness for locally invoking your Lambda function handler.
 */
public class HandleCreateSnippetTest {

    @Test
    public void testHandleCreateSnippet() {
        HandleCreateSnippet handler = new HandleCreateSnippet();
        SnippetDto snippet = new SnippetDto();
        assertNull(snippet.getId());
        SnippetDto savedSnippet = handler.handleRequest(snippet, null);
        assertNotNull(savedSnippet.getId());
        snippet.setId(savedSnippet.getId());
        assertEquals(savedSnippet, snippet);
    }
}
