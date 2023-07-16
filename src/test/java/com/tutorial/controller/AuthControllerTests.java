package com.tutorial.controller;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tutorial.jwt.JwtService;
import com.tutorial.model.AuthRequest;
import com.tutorial.model.AuthResponse;
import com.tutorial.persistence.model.FakeBookUser;
import com.tutorial.persistence.service.AuthServiceImpl;
import com.tutorial.persistence.service.FakeBookUserServiceImpl;

import lombok.SneakyThrows;

@SpringBootTest
@AutoConfigureMockMvc
@EnableAutoConfiguration(exclude = {DataSourceAutoConfiguration.class})
public class AuthControllerTests {	
	@Autowired                           
    private MockMvc mockMvc;;
	
	@MockBean
	AuthServiceImpl authServiceImpl;
	
	@MockBean
	FakeBookUserServiceImpl fakeBookUserServiceImpl;
	
	@MockBean
	JwtService jwtService;

    @Test
    @SneakyThrows
    public void testValidAuthenticate() {
    	Mockito.when(Mockito.mock(AuthController.class).authenticate(ArgumentMatchers.any())).thenReturn(new ResponseEntity<AuthResponse>(getAuthResponseData(), HttpStatus.OK));
    	Mockito.when(Mockito.mock(AuthServiceImpl.class).findByEmailId(ArgumentMatchers.any(), ArgumentMatchers.any())).thenReturn(getAuthResponseData());
    	Mockito.when(Mockito.mock(JwtService.class).generateToken(ArgumentMatchers.any(), ArgumentMatchers.any())).thenReturn(getAuthResponseData().getAccessToken());
    	Mockito.when(Mockito.mock(JwtService.class).generateRefreshToken(ArgumentMatchers.any())).thenReturn(getAuthResponseData().getRefreshToken());
    	Mockito.when(Mockito.mock(FakeBookUserServiceImpl.class).isValid(ArgumentMatchers.any(), ArgumentMatchers.any())).thenReturn(true);
    	Mockito.when(Mockito.mock(FakeBookUserServiceImpl.class).findByEmailId(ArgumentMatchers.any())).thenReturn(getFakeBookUserData());
    	
    	AuthRequest request = new AuthRequest();
    	
    	request.setEmailId("rajivnh@msn.com");
    	request.setPassword("wrongpwd");
    	
    	mockMvc.perform(MockMvcRequestBuilders
    			.post("/authenticate")
    			.accept(MediaType.APPLICATION_JSON)
    			.content(asJsonString(request))
    			.contentType(MediaType.APPLICATION_JSON))
    			.andDo(print())
    			.andExpect(status().isOk());
    }
    
    private AuthResponse getAuthResponseData() {
    	AuthResponse response = new AuthResponse();
    	
    	response.setAccessToken("eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJra2FkbWluQGhvdG1haWwuY29tIiwiaWF0IjoxNjg5NDA1NDczLCJleHAiOjE2ODk0OTE4NzN9.O36eXWKWI2aaJK8ajjTQZg53b-rjUcL-0kQ52gfxYrk");
    	response.setRefreshToken("eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJra2FkbWluQGhvdG1haWwuY29tIiwiaWF0IjoxNjg5NDA1NDczLCJleHAiOjE2OTAwMTAyNzN9.rNmCMnFNj6Y2ovEQC1rQftDrPuKLjX4-R9MTnwgExJ4");
    	
    	return response;
    }
    
    private FakeBookUser getFakeBookUserData() {
    	FakeBookUser userDetails = new FakeBookUser();
    	
    	userDetails.setEmailId("rajivnh@msn.com");
    	userDetails.setRole("ROLE_USER");
    	
    	return userDetails;
    }
    
    @SneakyThrows
    private String asJsonString(final Object obj) {
    	return new ObjectMapper().writeValueAsString(obj);
    }
}
