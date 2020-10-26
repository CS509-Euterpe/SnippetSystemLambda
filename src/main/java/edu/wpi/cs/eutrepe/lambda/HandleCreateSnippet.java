package edu.wpi.cs.eutrepe.lambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.RequestHandler;

import edu.wpi.cs.eutrepe.db.SnippetDao;
import edu.wpi.cs.eutrepe.dto.SnippetDto;

public class HandleCreateSnippet implements RequestHandler<SnippetDto, SnippetDto> {
	LambdaLogger logger;
	
    @Override
    public SnippetDto handleRequest(SnippetDto req, Context context) {
		logger = context.getLogger();
		logger.log(req.toString());

    	SnippetDao snippetDao = new SnippetDao();
    	try {
			snippetDao.addSnippet(req);
		} catch (Exception e) {
			logger.log(e.getMessage());
			e.printStackTrace();
		}
    	return null;
    }
}
