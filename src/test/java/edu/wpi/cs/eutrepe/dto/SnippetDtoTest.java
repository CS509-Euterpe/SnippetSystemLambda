/*
 * CS-509 Team Eutrepe AWS Application Test
 */

package edu.wpi.cs.eutrepe.dto;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.time.LocalDate;

import org.junit.Test;

public class SnippetDtoTest {
	final String content = "dumbContent";
	final Integer id = 1;
	final String info = "dumbInfo";
	final String name = "dumbName";
	final String password = "dumbPassword";
	final String timestamp = LocalDate.now().toString();
	
	@Test
	public void testCreateSnippetDto() {
		SnippetDto snippet = new SnippetDto();
		assertNull(snippet.comments);
		assertNull(snippet.content);
		assertNull(snippet.id);
		assertNull(snippet.info);
		assertNull(snippet.language);
		assertNull(snippet.timestamp);
		assertNull(snippet.name);
		assertNull(snippet.password);
		
		snippet.setComments(new ArrayList<CommentDto>());
		snippet.setContent(content);
		snippet.setId(id);
		snippet.setInfo(info);
		snippet.setLanguage(Language.JAVA);
		snippet.setTimestamp(timestamp);
		snippet.setPassword(password);
		snippet.setName(name);
		
		assertEquals(snippet.comments, new ArrayList<CommentDto>());
		assertEquals(snippet.content, content);
		assertEquals(snippet.id, id);
		assertEquals(snippet.info, info);
		assertEquals(snippet.language, Language.JAVA);
		assertEquals(snippet.timestamp, timestamp);
		assertEquals(snippet.password, password);
		assertEquals(snippet.name, name);
	}
}
