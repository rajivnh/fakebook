package com.tutorial.service;

import java.util.Optional;

import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import com.tutorial.persistence.model.FakeBookUser;
import com.tutorial.persistence.repository.FakeBookUserRepository;
import com.tutorial.persistence.service.FakeBookUserServiceImpl;

import lombok.SneakyThrows;

@SpringBootTest
@AutoConfigureMockMvc
@EnableAutoConfiguration(exclude = {DataSourceAutoConfiguration.class})
public class FakeBookUserServiceTests {
	@InjectMocks
	FakeBookUserServiceImpl fakeBookUserServiceImpl;
	
	@MockBean
	FakeBookUserRepository repository;
	
	@BeforeEach
    public void setUp(){
        MockitoAnnotations.openMocks(this);
    }
	
	@SneakyThrows
	@Test
	public void testValidFindByEmailId() {
		Mockito.when(Mockito.mock(FakeBookUserServiceImpl.class).findByEmailId(ArgumentMatchers.any())).thenReturn(getFakeBookUserData());
		Mockito.when(repository.findById(ArgumentMatchers.any())).thenReturn(Optional.of(getFakeBookUserData()));
		
		SoftAssertions assertions = new SoftAssertions();
		
		assertions.assertThat(fakeBookUserServiceImpl
				.findByEmailId(getFakeBookUserData().getEmailId()).getEmailId())
				.isEqualTo(getFakeBookUserData().getEmailId());
		
		assertions.assertThat(fakeBookUserServiceImpl
				.findByEmailId(getFakeBookUserData().getEmailId()).getRole())
				.isEqualTo(getFakeBookUserData().getRole());
		
		assertions.assertAll();
	}
	
    private FakeBookUser getFakeBookUserData() {
    	FakeBookUser userDetails = new FakeBookUser();
    	
    	userDetails.setEmailId("rajivnh@msn.com");
    	userDetails.setRole("ROLE_USER");
    	
    	return userDetails;
    }
}
