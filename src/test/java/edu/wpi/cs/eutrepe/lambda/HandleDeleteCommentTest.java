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
import java.time.LocalDate;
import java.util.ArrayList;

import org.junit.Assert;
import org.junit.Test;

import com.google.gson.Gson;

import edu.wpi.cs.eutrepe.db.CommentDao;
import edu.wpi.cs.eutrepe.db.SnippetDao;
import edu.wpi.cs.eutrepe.dto.CommentDto;
import edu.wpi.cs.eutrepe.http.CommentResponse;

/**
 * A simple test harness for locally invoking your Lambda function handler.
 */
public class HandleDeleteCommentTest extends LambdaTest{

    private static final String SAMPLE_INPUT_STRING = "{\"foo\": \"bar\"}";
    private static final String EXPECTED_OUTPUT_STRING = "{\"FOO\": \"BAR\"}";

    @Test
    public void testHandleDeleteComment() throws Exception {
    	HandleDeleteComment handler = new HandleDeleteComment();
        //create sample comment 
        CommentDto comment = new CommentDto();
        comment.setSnippetId("999");
        comment.setText("testcommentText");
        comment.setTimestamp("2020-04-20"); //TODO make sure comment timing is in minutes and seconds
        comment.setName("Tester");
        comment.getRegion().setStartLine(1);
        comment.getRegion().setEndLine(2);
        comment.getRegion().setStartChar(3);
        comment.getRegion().setEndChar(4);
        
        CommentDao commentDao = new CommentDao();
        int commentId = commentDao.addComment(comment);
        comment.setId(commentId);
        //check that comment exists
        assertNotNull(commentDao.getComments(Integer.parseInt(comment.getSnippetId())));
        
        //send comment as if coming from API Gateway 
        String input = new Gson().toJson(Integer.parseInt(comment.getSnippetId()));
        InputStream inputStream = new ByteArrayInputStream(input.getBytes());
        OutputStream output = new ByteArrayOutputStream();
        handler.handleRequest(inputStream, output, createContext("create"));
        CommentResponse commentResponse = new Gson().fromJson(output.toString(), CommentResponse.class);
        

        //check that delete comment response is success
        assertEquals(commentResponse.getHttpCode().intValue(), 200);
        
        
        //check that comment does not exist
        assertNull(commentDao.getComments(Integer.parseInt(comment.getSnippetId())));
        //check that comment was deleted
        ArrayList<CommentDto> empty =  new ArrayList<CommentDto>();
        assertTrue(empty.equals(commentDao.getComments(Integer.parseInt(comment.getSnippetId()))));
    }
}
