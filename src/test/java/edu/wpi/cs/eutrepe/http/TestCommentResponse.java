package edu.wpi.cs.eutrepe.http;

import static org.junit.Assert.*;

import org.junit.Test;

import edu.wpi.cs.eutrepe.dto.CommentDto;

public class TestCommentResponse {
	
	@Test
	public void testCommentResponse() {
		CommentResponse commentResponse = new CommentResponse();
		assertNull(commentResponse.comment);
		assertNull(commentResponse.httpCode);
		assertNull(commentResponse.msg);
	}
	
	@Test
	public void testFilledCommentResponse() {
		CommentResponse commentResponse = new CommentResponse();
		commentResponse.setComment(new CommentDto());
		commentResponse.setHttpCode(200);
		commentResponse.setMsg("Test");
		assertEquals(commentResponse.msg, "Test");
		assertEquals(commentResponse.comment, new CommentDto());
	}

}
