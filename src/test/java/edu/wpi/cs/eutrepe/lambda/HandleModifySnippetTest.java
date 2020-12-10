package edu.wpi.cs.eutrepe.lambda;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.time.LocalDate;
import java.util.ArrayList;

import org.junit.Test;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import edu.wpi.cs.eutrepe.db.CommentDao;
import edu.wpi.cs.eutrepe.db.SnippetDao;
import edu.wpi.cs.eutrepe.dto.CommentDto;
import edu.wpi.cs.eutrepe.dto.Language;
import edu.wpi.cs.eutrepe.dto.Region;
import edu.wpi.cs.eutrepe.dto.SnippetDto;
import edu.wpi.cs.eutrepe.http.SnippetResponse;

/**
 * A simple test harness for locally invoking your Lambda function handler.
 */
public class HandleModifySnippetTest extends LambdaTest {

	@Test
	public void testHandleModifySnippet() throws Exception {
		HandleModifySnippet handler = new HandleModifySnippet();
		SnippetDao snippetDao = new SnippetDao();
        
        //create snippet
        SnippetDto snippet = new SnippetDto();
        snippet.setContent("testContent");
        snippet.setInfo("testInfo");
        snippet.setName("testName");
        snippet.setLanguage(Language.PYTHON);
        snippet.setComments(new ArrayList<CommentDto>());
        snippet.setTimestamp("2020-01-25");
        snippet.setPassword("");
        
        //check that the snippet id is null when initializing
        assertNull(snippet.getId());
        
        //add snippet to DB & check that snippet exists in DB
        int snippetId = snippetDao.addSnippet(snippet);
        snippet.setId(snippetId);
        assertNotNull(snippetDao.getSnippet(snippetId));

//        //create comment
//        CommentDto comment = new CommentDto();
//        comment.setSnippetId(snippet.getId().toString());
//        comment.setText("testcommentText");
//        comment.setTimestamp("2020-12-06 00:00:00");//TODO change time stamp to include seconds and minutes
//        comment.setName("snippetcomment");
//        comment.setRegion(new Region(1, 2, 3, 4));
//       
//        //add the comment to the DB
//        CommentDao commentDao = new CommentDao();
//        int crustycommentId = commentDao.addComment(comment);
//        comment.setId(crustycommentId);
//        ArrayList<CommentDto> commentlist = new ArrayList<CommentDto>();
//        commentlist.add(comment);
//        snippet.setComments(commentlist);
        
        
        //create snippet that will become the modified snippet
        SnippetDto modsnippet = new SnippetDto();
        modsnippet.setContent("modtestContent");
        modsnippet.setInfo("modtestInfo");
        modsnippet.setName("modtestName");
        modsnippet.setLanguage(Language.JAVA);
        modsnippet.setComments(new ArrayList<CommentDto>());
        modsnippet.setTimestamp("2020-12-10");
        modsnippet.setPassword("");
        modsnippet.setId(snippetId);
        
        
        
        //create JSON of a newly modified snippet
        JsonObject body = new JsonObject();
        JsonArray comments = JsonParser.parseString(modsnippet.getComments().toString()).getAsJsonArray();
        //int beforetotalsnippets=jsonObject.size();

        JsonObject snippo = new JsonObject();
        snippo.addProperty("id", snippetId);
        snippo.addProperty("password", modsnippet.getPassword());
        snippo.addProperty("name", modsnippet.getName());
        snippo.addProperty("info", modsnippet.getInfo());
        snippo.add("comments", comments);
        snippo.addProperty("language", modsnippet.getLanguage().toString());
        snippo.addProperty("content", modsnippet.getContent());
        snippo.addProperty("timestamp", modsnippet.getTimestamp());
        //snippo.addProperty("id", modsnippet.getId());
        body.add("body-json", snippo);
        
        
        //create JSON to delete snippets older than 11 day
//       
//        JsonObject path = new JsonObject();
//        JsonObject daysold = new JsonObject();
//        daysold.addProperty("id", snippet.getId());
//        path.add("path", daysold);
//        params.add("params", path);
        
        //send snippetID as if coming from API Gateway 
        String input = new Gson().toJson(body);
        InputStream inputStream = new ByteArrayInputStream(input.getBytes());;
        OutputStream outputStream = new ByteArrayOutputStream();
        handler.handleRequest(inputStream, outputStream, createContext("create"));
        SnippetResponse snippetResponse = new Gson().fromJson(outputStream.toString(), SnippetResponse.class);
        
        //check that snippetResponse is success
        assertEquals(snippetResponse.getHttpCode().intValue(), 200);
       
        SnippetDto modifiedSnippet = snippetResponse.getSnippet();
        modifiedSnippet.setId(snippetId);
        //check that the snippet info was changed
        assertEquals(modsnippet, modifiedSnippet);
        
        //check that snippet exists
        assertNotNull(snippetDao.getSnippet(modsnippet.getId()));
        snippetDao.deleteSnippet(modsnippet.getId());
        //check that snippet was deleted
        assertNull(snippetDao.getSnippet(modsnippet.getId()));
	}

	@Test
	public void testBadformatHandleModifySnippet() throws Exception {

//		//create bad modified snippet
//		String badInput = "{\"foo\": \"bar\"}";
//		InputStream badmodifyinputStream = new ByteArrayInputStream(badInput.getBytes());
//		OutputStream badmofidyoutput = new ByteArrayOutputStream();
//		HandleModifySnippet badhandler = new HandleModifySnippet();
//		SnippetDto badsnippet = new SnippetDto();
//		
//		assertNull(badsnippet.getId());
//		
//		badhandler.handleRequest(badmodifyinputStream, badmofidyoutput, createContext("create"));
//		SnippetResponse snippetResponse = new Gson().fromJson(badmofidyoutput.toString(), SnippetResponse.class);
//		assertTrue(snippetResponse.getHttpCode().equals(500));

	}
	
	
}
