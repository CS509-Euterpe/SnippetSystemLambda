package edu.wpi.cs.eutrepe.lambda;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;

import org.junit.Assert;
import org.junit.Test;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import edu.wpi.cs.eutrepe.db.CommentDao;
import edu.wpi.cs.eutrepe.db.SnippetDao;
import edu.wpi.cs.eutrepe.dto.CommentDto;
import edu.wpi.cs.eutrepe.dto.Language;
import edu.wpi.cs.eutrepe.dto.SnippetDto;
import edu.wpi.cs.eutrepe.http.DeleteSnippetResponse;

/**
 * A simple test harness for locally invoking your Lambda function handler.
 */
public class HandleDeleteStaleSnippetsTest extends LambdaTest{

    @Test
    public void testHandleDeleteStaleSnippets() throws Exception {
        HandleDeleteStaleSnippets handler = new HandleDeleteStaleSnippets();
        
        

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        //create sample snippet created NOW
        SnippetDto snippet = new SnippetDto();
        snippet.setContent("testContent");
        snippet.setInfo("testInfo");
        snippet.setName("testName");
        snippet.setLanguage(Language.PYTHON);
        snippet.setPassword("password");
        snippet.setTimestamp(dateFormat.format(new java.sql.Timestamp(System.currentTimeMillis())));
        System.out.println(snippet.getTimestamp()); //used for checking time format
        
        //create sample snippet created 10 days ago
        int days = 10;
        SnippetDto oldsnippet = new SnippetDto();
        oldsnippet.setContent("testContent");
        oldsnippet.setInfo("testInfo");
        oldsnippet.setName("testName");
        oldsnippet.setLanguage(Language.PYTHON);
        oldsnippet.setPassword("password");
        oldsnippet.setTimestamp(dateFormat.format(new java.sql.Timestamp(System.currentTimeMillis() - (days * 86400000))));
        System.out.println(oldsnippet.getTimestamp());
        //create sample snippet created 100 days ago
        days = 20;
        SnippetDto crustysnippet = new SnippetDto();
        crustysnippet.setContent("testContent");
        crustysnippet.setInfo("testInfo");
        crustysnippet.setName("testName");
        crustysnippet.setLanguage(Language.PYTHON);
        crustysnippet.setPassword("password");
        crustysnippet.setTimestamp(dateFormat.format(new java.sql.Timestamp(System.currentTimeMillis() - (days * 86400000))));
        System.out.println(crustysnippet.getTimestamp());
        
        //check that the snippet ids are null when initializing
        assertNull(snippet.getId());
        
        //add all three snippets to DB & check that snippets exists in DB
        SnippetDao snippetDao = new SnippetDao();
        int snippetId = snippetDao.addSnippet(snippet);
        snippet.setId(snippetId);
        assertNotNull(snippetDao.getSnippet(snippetId));
        
        snippetId = snippetDao.addSnippet(oldsnippet);
        oldsnippet.setId(snippetId);
        assertNotNull(snippetDao.getSnippet(snippetId));
        
        snippetId = snippetDao.addSnippet(crustysnippet);
        crustysnippet.setId(snippetId);
        assertNotNull(snippetDao.getSnippet(snippetId));
        
        //create sample comment for crustysnippet
        CommentDto comment = new CommentDto();
        comment.setSnippetId(Integer.toString(snippet.getId()));
        comment.setText("testcommentText");
        comment.setTimestamp("2020-04-20"); //TODO make sure comment timing is in minutes and seconds
        comment.setName("snippetcomment");
        comment.getRegion().setStartLine(1);
        comment.getRegion().setEndLine(2);
        comment.getRegion().setStartChar(3);
        comment.getRegion().setEndChar(4);
        
        //add the comment to the DB
        CommentDao commentDao = new CommentDao();
        int crustycommentId = commentDao.addComment(comment);
        comment.setId(crustycommentId);
        
        //create JSON to delete snippets older than 11 day
        JsonObject params = new JsonObject();
        JsonObject path = new JsonObject();
        JsonObject daysold = new JsonObject();
        daysold.addProperty("days", 11);
        path.add("path", daysold);
        params.add("params", path);
    
 
        //send days as if coming from API Gateway 
        String input = new Gson().toJson(params);
        InputStream inputStream = new ByteArrayInputStream(input.getBytes());
        OutputStream output = new ByteArrayOutputStream();
        handler.handleRequest(inputStream, output, createContext("create"));
        //DeleteSnippetResponse snippetResponse = new Gson().fromJson(output.toString(), DeleteSnippetResponse.class);
        
        

        //check that any snippets made past the last 11 days are removed
        assertEquals(output.toString(), "true");
        
        //check that crustysnippet does not exist in database anymore
        assertNull(snippetDao.getSnippet(crustysnippet.getId()));
        //check that old and snippet do still exist in the database
        assertNotNull(snippetDao.getSnippet(oldsnippet.getId()));
        assertNotNull(snippetDao.getSnippet(snippet.getId()));
        //check that snippet's comment still exists
        assertNotNull(commentDao.getComments(crustycommentId));
       
        ///////////////////////////change JSON for within last 5 days
        daysold.addProperty("days", 5);
        //System.out.println(params.getAsJsonObject("params").toString());

      //send days as if coming from API Gateway 
         input = new Gson().toJson(params);
         inputStream = new ByteArrayInputStream(input.getBytes());
         output = new ByteArrayOutputStream();
        handler.handleRequest(inputStream, output, createContext("create"));
        //DeleteSnippetResponse snippetResponse = new Gson().fromJson(output.toString(), DeleteSnippetResponse.class);
        
        

      //check that any snippet made past the last 5 days are removed 
        assertEquals(output.toString(), "true");
        
        //check that crustysnippet does not exist in database anymore
        assertNull(snippetDao.getSnippet(crustysnippet.getId()));
        //check that oldsnippet does not exist in database anymore
        assertNull(snippetDao.getSnippet(oldsnippet.getId()));
        
        //check that snippet still exists in the database
        assertNotNull(snippetDao.getSnippet(snippet.getId()));
        //check that snippet's comment still exists
        assertNotNull(commentDao.getComments(crustycommentId));
        
        

        /////////////////////////////////////////change JSON for within now
        daysold.addProperty("days", 0);
        //System.out.println(params.getAsJsonObject("params").toString());

        //send days as if coming from API Gateway 
         input = new Gson().toJson(params);
         inputStream = new ByteArrayInputStream(input.getBytes());
         output = new ByteArrayOutputStream();
        handler.handleRequest(inputStream, output, createContext("create"));
        //DeleteSnippetResponse snippetResponse = new Gson().fromJson(output.toString(), DeleteSnippetResponse.class);
        
        

        //check that any snippet made now are deleted
        assertEquals(output.toString(), "true");
        
        //check that crustysnippet does not exist in database anymore
        assertNull(snippetDao.getSnippet(crustysnippet.getId()));
        //check that oldsnippet does not exist in database anymore
        assertNull(snippetDao.getSnippet(oldsnippet.getId()));
        //check that snippet does not exist in database anymore
        assertNull(snippetDao.getSnippet(snippet.getId()));
        //check that snippet comment does not exist in database anymore
        ArrayList<CommentDto> empty =  new ArrayList<CommentDto>();
        assertTrue(empty.equals(commentDao.getComments(Integer.parseInt(comment.getSnippetId()))));
    }
}
