package edu.wpi.cs.eutrepe.lambda;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.time.LocalDate;

import org.junit.Assert;
import org.junit.Test;

import edu.wpi.cs.eutrepe.db.SnippetDao;
import edu.wpi.cs.eutrepe.dto.SnippetDto;

/**
 * A simple test harness for locally invoking your Lambda function handler.
 */
public class HandleGetSnippetTest extends LambdaTest {

    @Test
    public void testHandleGetSnippet() throws Exception {
        HandleGetSnippet handler = new HandleGetSnippet();
        SnippetDao snippetDao = new SnippetDao();
		SnippetDto snippet = new SnippetDto();
        snippet.setContent("testContent");
        snippet.setInfo("testInfo");
        snippet.setName("testName");
        snippet.setTimestamp(LocalDate.now());
		Integer id = snippetDao.addSnippet(snippet);
		
        final String SAMPLE_INPUT_STRING = String.format("{ \"pathParameters\": { \"id\": \"%d\"  } }", id);
        InputStream input = new ByteArrayInputStream(SAMPLE_INPUT_STRING.getBytes());;
        OutputStream output = new ByteArrayOutputStream();

		
        handler.handleRequest(input, output, createContext("create"));
		snippetDao.deleteSnippet(id);
    }
}
