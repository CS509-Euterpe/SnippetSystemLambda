package edu.wpi.cs.eutrepe.dto;

import static org.junit.Assert.*;

import org.junit.Test;

public class ParameterDtoTest {
	final String password = "dumbPassword";
	final String user = "dumbUser";
	
	@Test
	public void testCreateParameterDto() {
		ParameterDto parameters = new ParameterDto();
		assertNull(parameters.password);
		assertNull(parameters.user);
		parameters.setPassword(password);
		parameters.setUser(user);
		assertEquals(parameters.password, password);
		assertEquals(parameters.user, user);
	}
}
