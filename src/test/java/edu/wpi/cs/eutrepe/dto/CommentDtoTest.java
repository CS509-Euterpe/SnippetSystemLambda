/*
 * CS-509 Team Eutrepe AWS Application Test
 */

package edu.wpi.cs.eutrepe.dto;

import static org.junit.Assert.*;

import java.time.LocalDate;

import org.junit.Test;

public class CommentDtoTest {
	final Integer start = 4;
	final Integer end = 0;
	final Integer id = 1;
	final String snippetID = "dumbSnippetID";
	final String text = "dumbText";
	final LocalDate date = LocalDate.now();

	@Test
	public void testCreateCommentDto() {
		CommentDto comment = new CommentDto();
		assertNull(comment.id);
		assertNull(comment.text);
		
	}
}
