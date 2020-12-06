package edu.wpi.cs.eutrepe.lambda;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.junit.Assert;
import org.junit.Test;

import com.google.gson.Gson;

import edu.wpi.cs.eutrepe.db.DatabaseUtil;
import edu.wpi.cs.eutrepe.db.SnippetDao;

/**
 * A simple test harness for locally invoking your Lambda function handler.
 */
public class HandleGetAllSnippetsTest extends LambdaTest{
	
     
    private static final String SAMPLE_INPUT_STRING = "{\"foo\": \"bar\"}";
    private static final String EXPECTED_OUTPUT_STRING = "{\"FOO\": \"BAR\"}";

    @Test
    public void testHandleGetAllSnippets() throws IOException {
        HandleGetAllSnippets handler = new HandleGetAllSnippets();
       
        SnippetDao snippetDao = new SnippetDao();
        
        String input = new Gson().toJson(null);
        InputStream inputStream = new ByteArrayInputStream(input.getBytes());
        OutputStream output = new ByteArrayOutputStream();
        handler.handleRequest(inputStream, output, createContext("create"));
        
        
        
        
        
       
       
    }
}
