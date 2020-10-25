package edu.wpi.cs.eutrepe.dto;

import static org.junit.Assert.*;

import java.util.Date;

import org.junit.Test;

public class CommentDtoTest {
	final Integer start = 4;
	final Integer end = 0;
	final String id = "dumbID";
	final String snippetID = "dumbSnippetID";
	final String text = "dumbText";
	final Date date = new Date();

	@Test
	public void testCreateCommentDto() {
		CommentDto comment = new CommentDto();
		assertNull(comment.id);
		assertNull(comment.end);
		assertNull(comment.snippetID);
		assertNull(comment.text);
		assertNull(comment.timestamp);
		
		comment.setEnd(end);
		comment.setId(id);
		comment.setSnippetID(snippetID);
		comment.setStart(start);
		comment.setText(text);
		comment.setTimestamp(date);
		
		assertEquals(comment.end, end);
		assertEquals(comment.start, start);
		assertEquals(comment.id, id);
		assertEquals(comment.snippetID, snippetID);
		assertEquals(comment.text, text);
		assertEquals(comment.timestamp, date);
	}
}
