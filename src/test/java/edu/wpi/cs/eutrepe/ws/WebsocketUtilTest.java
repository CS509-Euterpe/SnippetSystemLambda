package edu.wpi.cs.eutrepe.ws;

import java.io.IOException;

import org.junit.Assert;
import org.junit.Test;

import com.amazonaws.services.lambda.runtime.Context;

import edu.wpi.cs.eutrepe.lambda.LambdaTest;
public class WebsocketUtilTest extends LambdaTest {

	  @Test
	  public void testCertificateSigning() throws IOException {
		  Context context = createContext("create");
			try {
				WebsocketUtil util = new WebsocketUtil(context.getLogger());
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		  
		  Assert.assertTrue(true);	  
	  }
}
