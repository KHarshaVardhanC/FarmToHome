package com.ftohbackend;

import org.modelmapper.ModelMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;

import com.ftohbackend.dto.ProductRequest;
import com.ftohbackend.model.Product;

@SpringBootApplication
@ComponentScan(basePackages = "com.ftohbackend")

public class FtohbackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(FtohbackendApplication.class, args);
	}
	
	@Bean 
	public ModelMapper modelMapper()
	{
		
	    ModelMapper modelMapper = new ModelMapper();

	    
		
		return new ModelMapper();
	}
	
	

}
