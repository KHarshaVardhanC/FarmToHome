package com.ftohbackend;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;

@SpringBootTest
class FtohbackendApplicationTests {

	@Test
	void contextLoads() {
	}
	public static void main(String[] args) {
		SpringApplication.run(FtohbackendApplicationTests.class, args);
	}
	
	@Autowired
    private ApplicationContext applicationContext;

    @Test
    void contextLoads1() {
        // This test ensures that the Spring application context loads successfully
        assertNotNull(applicationContext);
    }

    @Test
     void modelMapperBeanExists() {
        // Test that the ModelMapper bean is correctly configured
        boolean containsModelMapperBean = applicationContext.containsBean("modelMapper");
        assertTrue(containsModelMapperBean, "ModelMapper bean should be available");
    }

    @Test
    void modelMapperBeanIsCorrectType() {
        // Test that the ModelMapper bean is of the correct type
        ModelMapper modelMapper = applicationContext.getBean("modelMapper", ModelMapper.class);
        assertNotNull(modelMapper, "ModelMapper bean should not be null");
    }
    
    @Test
    void testMainMethod() {
        // Test that the main method can be called without throwing exceptions
        assertDoesNotThrow(() -> {
            FtohbackendApplication.main(new String[]{});
        });
    }
    

}
