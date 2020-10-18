package edu.wpi.cs.eutrepe.dto;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Date;

import org.junit.Test;

public class SnippetDtoTest {
	final String content = "dumbContent";
	final String id = "dumbID";
	final String info = "dumbInfo";
	final Date timestamp = new Date();
	
	@Test
	public void testCreateSnippetDto() {
		SnippetDto snippet = new SnippetDto();
		assertNull(snippet.comments);
		assertNull(snippet.content);
		assertNull(snippet.id);
		assertNull(snippet.info);
		assertNull(snippet.language);
		assertNull(snippet.parameters);
		assertNull(snippet.timestamp);
		
		snippet.setComments(new ArrayList<CommentDto>());
		snippet.setContent(content);
		snippet.setId(id);
		snippet.setInfo(info);
		snippet.setLanguage(Language.JAVA);
		snippet.setTimestamp(timestamp);
		
		assertEquals(snippet.comments, new ArrayList<CommentDto>());
		assertEquals(snippet.content, content);
		assertEquals(snippet.id, id);
		assertEquals(snippet.info, info);
		assertEquals(snippet.language, Language.JAVA);
		assertEquals(snippet.timestamp, timestamp);
	}
}
