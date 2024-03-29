/*
 * CS-509 Team Eutrepe AWS Application Test
 */

package edu.wpi.cs.eutrepe.lambda;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.junit.Assert;
import org.junit.Test;

/**
 * A simple test harness for locally invoking your Lambda function handler.
 */
public class HandleWebsocketConnectTest {

    private static final String SAMPLE_INPUT_STRING = "{\"foo\": \"bar\"}";
    private static final String EXPECTED_OUTPUT_STRING = "{\"FOO\": \"BAR\"}";

    @Test
    public void testHandleWebsocketConnect() throws IOException {
        HandleWebsocketConnect handler = new HandleWebsocketConnect();

        InputStream input = new ByteArrayInputStream(SAMPLE_INPUT_STRING.getBytes());;
        OutputStream output = new ByteArrayOutputStream();
//
//        handler.handleRequest(input, output, null);
//
//        // TODO: validate output here if needed.
//        String sampleOutputString = output.toString();
//        System.out.println(sampleOutputString);
//        Assert.assertEquals(EXPECTED_OUTPUT_STRING, sampleOutputString);
    }
}
