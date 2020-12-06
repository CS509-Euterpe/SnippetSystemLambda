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


/**
 * A simple test harness for locally invoking your Lambda function handler.
 */
public class HandleDeleteSnippetTest extends LambdaTest{

	//private static final String SAMPLE_DELETE_INPUT_STRING = "{\"foo\": \"bar\"}";
	

	@Test
	public void testHandleDeleteSnippet() throws Exception {


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
