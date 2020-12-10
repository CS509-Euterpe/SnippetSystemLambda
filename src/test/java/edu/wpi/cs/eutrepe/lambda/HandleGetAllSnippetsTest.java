/*
 * CS-509 Team Eutrepe AWS Application Test
 */

package edu.wpi.cs.eutrepe.lambda;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import org.junit.Test;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonParser;
import edu.wpi.cs.eutrepe.db.SnippetDao;
import edu.wpi.cs.eutrepe.dto.Language;
import edu.wpi.cs.eutrepe.dto.SnippetDto;

public class HandleGetAllSnippetsTest extends LambdaTest{
	
     
    @Test
    public void testHandleGetAllSnippets() throws Exception {
        HandleGetAllSnippets handler = new HandleGetAllSnippets();
       
        SnippetDao snippetDao = new SnippetDao();
        
        //send days as if coming from API Gateway 
        String input = new Gson().toJson("");
        InputStream inputStream = new ByteArrayInputStream(input.getBytes());
        OutputStream output = new ByteArrayOutputStream();
        handler.handleRequest(inputStream, output, createContext("create"));
        
        JsonArray jsonObject = JsonParser.parseString(output.toString()).getAsJsonArray();
        int beforetotalsnippets=jsonObject.size();

        
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        //create sample snippet created NOW
        SnippetDto snippetOne = new SnippetDto();
        snippetOne.setContent("testContent");
        snippetOne.setInfo("testInfo");
        snippetOne.setName("testName");
        snippetOne.setLanguage(Language.PYTHON);
        snippetOne.setPassword("password");
        snippetOne.setTimestamp(dateFormat.format(new java.sql.Timestamp(System.currentTimeMillis())));

        //create sample snippet created 10 days ago
        SnippetDto snippetTwo = new SnippetDto();
        snippetTwo.setContent("testContent");
        snippetTwo.setInfo("testInfo");
        snippetTwo.setName("testName");
        snippetTwo.setLanguage(Language.PYTHON);
        snippetTwo.setPassword("password");
        snippetTwo.setTimestamp(dateFormat.format(new java.sql.Timestamp(System.currentTimeMillis())));
        //create sample snippet created 100 days ago
        SnippetDto snippetThree = new SnippetDto();
        snippetThree.setContent("testContent");
        snippetThree.setInfo("testInfo");
        snippetThree.setName("testName");
        snippetThree.setLanguage(Language.PYTHON);
        snippetThree.setPassword("password");
        snippetThree.setTimestamp(dateFormat.format(new java.sql.Timestamp(System.currentTimeMillis())));

        
        //check that the snippet ids are null when initializing
        assertNull(snippetOne.getId());
        
        //add all three snippets to DB & check that snippets exists in DB
        
        int snippetId = snippetDao.addSnippet(snippetOne);
        snippetOne.setId(snippetId);
        assertNotNull(snippetDao.getSnippet(snippetId));
        
        snippetId = snippetDao.addSnippet(snippetTwo);
        snippetTwo.setId(snippetId);
        assertNotNull(snippetDao.getSnippet(snippetId));
        
        snippetId = snippetDao.addSnippet(snippetThree);
        snippetThree.setId(snippetId);
        assertNotNull(snippetDao.getSnippet(snippetId));
       

        //send days as if coming from API Gateway 
        input = new Gson().toJson("");
        inputStream = new ByteArrayInputStream(input.getBytes());
        output = new ByteArrayOutputStream();
        handler.handleRequest(inputStream, output, createContext("create"));
        
        
        
        jsonObject = JsonParser.parseString(output.toString()).getAsJsonArray();
        int aftertotalsnippets=jsonObject.size();
        
        //check that the total number of snippets is the previous amount plus 3
        assertEquals(beforetotalsnippets+3,aftertotalsnippets);
        
        //maybe delete all snippets???
        snippetDao.deleteSnippet(snippetOne.getId());
        snippetDao.deleteSnippet(snippetTwo.getId());
        snippetDao.deleteSnippet(snippetThree.getId());
        
        
       
       
    }
}
