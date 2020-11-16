package edu.wpi.cs.eutrepe.ws;

import java.nio.ByteBuffer;
import java.util.ArrayList;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder.EndpointConfiguration;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.apigatewaymanagementapi.*;
import com.amazonaws.services.apigatewaymanagementapi.model.*;

import edu.wpi.cs.eutrepe.db.WebsocketDao;
import edu.wpi.cs.eutrepe.dto.ConnectionDto;

public class WebsocketUtil {
	
	final String connectionsUrl = "https://6u65iac1vf.execute-api.us-east-1.amazonaws.com/Alpha/@connections/";
		
	private AmazonApiGatewayManagementApi _aws;
	
	private LambdaLogger _logger;
	
	public WebsocketUtil(LambdaLogger logger) throws ClassNotFoundException
	{
		Class.forName("com.amazonaws.services.apigatewaymanagementapi.AmazonApiGatewayManagementApiClient");
		Class.forName("com.amazonaws.services.apigatewaymanagementapi.AmazonApiGatewayManagementApiClientBuilder");
		
		_logger = logger;

		_aws = AmazonApiGatewayManagementApiClient.builder()
				.withEndpointConfiguration(
						new EndpointConfiguration(
								"https://6u65iac1vf.execute-api.us-east-1.amazonaws.com/Alpha/",
								"us-east-1"))
				.withCredentials(new AWSStaticCredentialsProvider(
						new BasicAWSCredentials("AKIAQRSS6QT5VSSV2J5V", "MVhmGoLJ0IaG6O+TyxPlk1rQP2vX61v2EofNvAJ6")))
				.build();

	}
	
	public int notifyUsers(int snippetId, String message)
	{
		try 
		{
			int successes = 0;
			WebsocketDao dao = new WebsocketDao();
			ArrayList<ConnectionDto> connections = dao.getConnections(snippetId);
			_logger.log("Got " + connections.size() + " connections");
			ArrayList<ConnectionDto> closed = new ArrayList<ConnectionDto>();
			for (ConnectionDto connection : connections)
			{

				GetConnectionRequest get = new GetConnectionRequest()
						.withConnectionId(connection.getConnectionId());
				
				try {
					GetConnectionResult getResult = _aws.getConnection(get);

					PostToConnectionRequest post = new PostToConnectionRequest()
							.withConnectionId(connection.getConnectionId())
							.withData(ByteBuffer.wrap(message.getBytes()));
					_logger.log("Notifying " + connection.getConnectionId());
					PostToConnectionResult postResult = _aws.postToConnection(post);
					successes++;
				}
				catch (GoneException ex)
				{
					closed.add(connection);
				}	
			}
			for (ConnectionDto connection : closed)
			{
				dao.deleteConnection(connection.getConnectionId());
			}
			return successes;
			
		}
		catch (Exception ex)
		{
			System.out.println("Util Error:" + ex.toString());
			ex.printStackTrace();
		}
		return -1;
	}
	
}
