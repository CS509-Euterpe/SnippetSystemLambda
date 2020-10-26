package edu.wpi.cs.eutrepe.lambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.RequestHandler;

import edu.wpi.cs.eutrepe.db.SnippetDao;
import edu.wpi.cs.eutrepe.dto.SnippetDto;
import edu.wpi.cs.eutrepe.http.CreateSnippetResponse;

public class HandleCreateSnippet implements RequestHandler<SnippetDto, CreateSnippetResponse> {
	final String successMessage = "Successfully saved snippet";
	final String failureMessage = "Failed to save snippet";
	LambdaLogger logger;
	
    @Override
    public CreateSnippetResponse handleRequest(SnippetDto req, Context context) {
		logger = context.getLogger();
		logger.log(req.toString());
		CreateSnippetResponse res = new CreateSnippetResponse();

    	SnippetDao snippetDao = new SnippetDao();
    	try {
    		SnippetDto savedSnippet = req;
    		Integer id = snippetDao.addSnippet(req);
    		if (id != null) {
        		savedSnippet.setId(id);
        		res.setHttpCode(200);
        		res.setMsg(successMessage);
        		res.setSnippet(savedSnippet);
    		} else {
    			res.setHttpCode(500);
    			res.setMsg(failureMessage);
    			res.setSnippet(null);
    		}
    		return res;
		} catch (Exception e) {
			logger.log(e.getMessage());
			e.printStackTrace();
			res.setHttpCode(500);
			res.setMsg(e.getMessage());
			res.setSnippet(null);
    		return res;
		}
    }
}
