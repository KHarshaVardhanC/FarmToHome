package com.ftohbackend;

import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;

@SpringBootTest
class FtohbackendApplicationTests {

	@Test
	void contextLoads() {
	}
	public static void main(String[] args) {
		SpringApplication.run(FtohbackendApplicationTests.class, args);
	}
	
	@Bean 
	public ModelMapper modelMapper()
	{
		
	    ModelMapper modelMapper = new ModelMapper();

	    
		
		return new ModelMapper();
	}


}
