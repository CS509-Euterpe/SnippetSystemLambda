package edu.wpi.cs.eutrepe.lambda;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.time.LocalDate;
import java.util.ArrayList;

import org.junit.Assert;
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
public class HandleGetSnippetTest extends LambdaTest {

    @Test
    public void testHandleGetSnippet() throws Exception {
        HandleGetSnippet handler = new HandleGetSnippet();
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

        //create comment
        CommentDto comment = new CommentDto();
        comment.setSnippetId(snippet.getId().toString());
        comment.setText("testcommentText");
        comment.setTimestamp("2020-12-06 00:00:00");//TODO change time stamp to include seconds and minutes
        comment.setName("snippetcomment");
        comment.setRegion(new Region(1, 2, 3, 4));
       
        //add the comment to the DB
        CommentDao commentDao = new CommentDao();
        int crustycommentId = commentDao.addComment(comment);
        comment.setId(crustycommentId);
        ArrayList<CommentDto> commentlist = new ArrayList<CommentDto>();
        commentlist.add(comment);
        snippet.setComments(commentlist);
        //create JSON to delete snippets older than 11 day
        JsonObject daysold = new JsonObject();
        daysold.addProperty("id", snippet.getId());
    
        
        //send snippetID as if coming from API Gateway 
        String input = new Gson().toJson(daysold);
        InputStream inputStream = new ByteArrayInputStream(input.getBytes());;
        OutputStream outputStream = new ByteArrayOutputStream();
        handler.handleRequest(inputStream, outputStream, createContext("create"));
        SnippetResponse snippetResponse = new Gson().fromJson(outputStream.toString(), SnippetResponse.class);
        
        //check that snippetResponse is success
        assertEquals(snippetResponse.getHttpCode().intValue(), 200);
       
        SnippetDto savedSnippet = snippetResponse.getSnippet();

        //check that the snippet was retrieved
        assertNotNull(savedSnippet.getId());
        snippet.setId(savedSnippet.getId());
        assertEquals(savedSnippet, snippet);
        
        
        //check that snippet exists
        assertNotNull(snippetDao.getSnippet(savedSnippet.getId()));
        snippetDao.deleteSnippet(savedSnippet.getId());
        //check that snippet was deleted
        assertNull(snippetDao.getSnippet(savedSnippet.getId()));
        
        
    }
}
