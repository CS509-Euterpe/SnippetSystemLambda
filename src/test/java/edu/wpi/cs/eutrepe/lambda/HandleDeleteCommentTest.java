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
    	CommentDto comment = new CommentDto();
    	  //comment.setSnippetID(snippetID);
        //comment.setId(id);
        comment.setText("testcommentText");
        comment.setTimestamp(LocalDate.now());
        comment.setStart(1);
        comment.setEnd(2);

        CommentDao commentDao = new CommentDao();
        Integer TestID = commentDao.addComment(comment);
        commentDao.deleteComment(TestID);
        assertEquals(new CommentDao().getComments(TestID),new ArrayList<CommentDto>()); 
    }
}
