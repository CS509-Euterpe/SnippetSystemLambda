package edu.wpi.cs.eutrepe.lambda;

import static org.junit.Assert.assertEquals;

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
import edu.wpi.cs.eutrepe.dto.Language;
import edu.wpi.cs.eutrepe.dto.SnippetDto;
import edu.wpi.cs.eutrepe.http.CommentResponse;
import edu.wpi.cs.eutrepe.http.CreateSnippetResponse;

/**
 * A simple test harness for locally invoking your Lambda function handler.
 */
public class HandleGetCommentTest extends LambdaTest{

    @Test
    public void testHandleGetComment() throws Exception {
    	HandleCreateSnippet snippethandler = new HandleCreateSnippet();
    	HandleCreateComment commenthandler = new HandleCreateComment();
        HandleGetComment getcommenthandler = new HandleGetComment();

        //create snippet
        SnippetDto snippet = new SnippetDto();
        snippet.setContent("testContent");
        snippet.setInfo("testInfo");
        snippet.setName("testName");
        snippet.setLanguage(Language.PYTHON);
        snippet.setComments(new ArrayList<CommentDto>());
        snippet.setTimestamp(LocalDate.now());
        String input = new Gson().toJson(snippet);
        InputStream inputStream = new ByteArrayInputStream(input.getBytes());
        OutputStream output = new ByteArrayOutputStream();
        snippethandler.handleRequest(inputStream, output, createContext("create"));
        CreateSnippetResponse snippetResponse = new Gson().fromJson(output.toString(), CreateSnippetResponse.class);
        SnippetDto savedSnippet = snippetResponse.getSnippet();
        //create comment
        CommentDto comment = new CommentDto();
        comment.setSnippetID(savedSnippet.getId().toString());
        comment.setText("testcommentText");
        comment.setTimestamp(LocalDate.now());
        comment.setStart(1);
        comment.setEnd(2);
        String commentinput = new Gson().toJson(comment);
        InputStream commentinputStream = new ByteArrayInputStream(commentinput.getBytes());;
        OutputStream commentoutputStream = new ByteArrayOutputStream();
        commenthandler.handleRequest(commentinputStream, commentoutputStream, createContext("create"));
        CommentResponse commentResponse = new Gson().fromJson(commentoutputStream.toString(), CommentResponse.class);
        CommentDto savedComment = commentResponse.getComment();
        
        //test get comment by id
        final String getcommentinput = "{ \"snippetId\": \""+ savedComment.getSnippetID()+"\" }";
        InputStream getcommentinputStream = new ByteArrayInputStream(getcommentinput.getBytes());;
        OutputStream getcommentoutputStream = new ByteArrayOutputStream();
        getcommenthandler.handleRequest(getcommentinputStream, getcommentoutputStream, createContext("create"));
        CommentResponse getcommentResponse = new Gson().fromJson(getcommentoutputStream.toString(), CommentResponse.class);
        CommentDto retrievedComment = getcommentResponse.getComment();
        assertEquals(retrievedComment,savedComment);
        
        //delete comment
        CommentDao commentDao = new CommentDao();
        commentDao.deleteComment(savedComment.getId());
        SnippetDao snippetDao = new SnippetDao();
		snippetDao.deleteSnippet(savedSnippet.getId());
    }
}
