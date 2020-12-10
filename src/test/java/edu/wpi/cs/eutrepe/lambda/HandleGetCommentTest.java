/*
 * CS-509 Team Eutrepe AWS Application Test
 */

package edu.wpi.cs.eutrepe.lambda;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
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


public class HandleGetCommentTest extends LambdaTest{

    @Test
    public void testHandleGetComment() throws Exception {
    	
        HandleGetComment getcommenthandler = new HandleGetComment();

        //create snippet
        SnippetDto snippet = new SnippetDto();
        snippet.setContent("testContent");
        snippet.setInfo("testInfo");
        snippet.setName("testName");
        snippet.setLanguage(Language.PYTHON);
        snippet.setComments(new ArrayList<CommentDto>());
        snippet.setTimestamp("2020-01-25");
        snippet.setPassword("");
        
        SnippetDao snippetDao = new SnippetDao();
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
        
        
        //create JSON to delete snippets older than 11 day
        JsonObject params = new JsonObject();
        JsonObject path = new JsonObject();
        JsonObject daysold = new JsonObject();
        daysold.addProperty("id", snippet.getId());
        path.add("path", daysold);
        params.add("params", path);
    
 
        //send snippetID as if coming from API Gateway 
        String input = new Gson().toJson(params);
        InputStream getcommentinputStream = new ByteArrayInputStream(input.getBytes());;
        OutputStream getcommentoutputStream = new ByteArrayOutputStream();
        getcommenthandler.handleRequest(getcommentinputStream, getcommentoutputStream, createContext("create"));
        
        JsonArray returnedcomments = JsonParser.parseString(getcommentoutputStream.toString()).getAsJsonArray();
       
        
        CommentDto getcommentResponse = new Gson().fromJson(returnedcomments.get(0), CommentDto.class);
       
        //check that the returned comment is equivalent to the original
        assertEquals(getcommentResponse,comment);
        
        //check that comment exists
        assertNotNull(commentDao.getComments(Integer.parseInt(comment.getSnippetId())));
        commentDao.deleteComment(getcommentResponse.getId());
        //check that comment was deleted
        ArrayList<CommentDto> empty =  new ArrayList<CommentDto>();
        assertTrue(empty.equals(commentDao.getComments(Integer.parseInt(comment.getSnippetId()))));
        
    }
}
