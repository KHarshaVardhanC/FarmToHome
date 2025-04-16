package com.ftohbackend.controllertesting;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ftohbackend.controller.ProductControllerImpl;
import com.ftohbackend.dto.CustomerProductDTO;
import com.ftohbackend.dto.ProductCity;
import com.ftohbackend.dto.ProductDTO;
import com.ftohbackend.dto.ProductRequest;
import com.ftohbackend.exception.ProductException;
import com.ftohbackend.model.Product;
import com.ftohbackend.model.Seller;
import com.ftohbackend.service.ProductService;
import com.ftohbackend.service.ProductServiceImpl;

public class ProductControllerTest1 {

    private MockMvc mockMvc;
    
    @Mock
    private ProductService productService;
    
    @Mock
    private ProductServiceImpl productServiceImpl;
    
    @Mock
    private ModelMapper modelMapper;
    
    @InjectMocks
    private ProductControllerImpl productController;
    
    private ObjectMapper objectMapper = new ObjectMapper();
    
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(productController).build();
    }
    
    @Test
    void testAddProduct() throws Exception {
        // Arrange
        MockMultipartFile image = new MockMultipartFile(
            "image",                     // This needs to match the field name in ProductRequest
            "test.jpg",                  // Original filename
            MediaType.IMAGE_JPEG_VALUE,  // Content type
            "test image content".getBytes() // Content
        );
        
        MockMultipartFile jsonData = new MockMultipartFile(
            "productRequest",            // This is the part name for the JSON data
            "",                          // No filename for JSON part
            MediaType.APPLICATION_JSON_VALUE, // Content type
            "{\"sellerId\": 1, \"productPrice\": 100.0, \"productName\": \"Test Product\", \"productQuantity\": 10.0, \"productQuantityType\": \"KG\", \"productDescription\": \"Test Description\", \"productCategory\": \"Vegetables\"}".getBytes()
        );
        
        // Properly mock the service method to handle the request
        ProductDTO expectedResponse = new ProductDTO();
        expectedResponse.setProductId(1);
        expectedResponse.setSellerId(1);
        expectedResponse.setProductName("Test Product");
        
        when(productService.addProduct(any(ProductRequest.class))).thenReturn(expectedResponse);
        
        // Act & Assert
        mockMvc.perform(MockMvcRequestBuilders.multipart("/product")
                .file(image)
                // If necessary, add other form fields as params
                .param("sellerId", "1")
                .param("productPrice", "100.0")
                .param("productName", "Test Product")
                .param("productQuantity", "10.0")
                .param("productQuantityType", "KG")
                .param("productDescription", "Test Description")
                .param("productCategory", "Vegetables")
                .contentType(MediaType.MULTIPART_FORM_DATA_VALUE))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.productId").value(1))
                .andExpect(jsonPath("$.sellerId").value(1))
                .andExpect(jsonPath("$.productName").value("Test Product"));
    }
    
    @Test
    void testGetCustomerProductByProductId() throws Exception {
        // Arrange
        Integer productId = 1;
        Product product = new Product();
        product.setProductId(productId);
        product.setProductName("Test Product");
        product.setProductPrice(100.0);
        product.setProductDescription("Test Description");
        product.setProductQuantity(10.0);
        product.setProductQuantityType("KG");
        product.setImageUrl("test-image.jpg");
        product.setProductRatingValue(4.5);
        product.setProductRatingCount(10);
        
        Seller seller = new Seller();
        seller.setSellerFirstName("John");
        seller.setSellerLastName("Doe");
        seller.setSellerPlace("Test Place");
        seller.setSellerCity("Test City");
        product.setSeller(seller);
        
        when(productService.getProduct(productId)).thenReturn(product);
        
        // Act & Assert
        mockMvc.perform(get("/product2/{productId}", productId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.productId").value(productId))
                .andExpect(jsonPath("$.productName").value("Test Product"))
                .andExpect(jsonPath("$.sellerName").value("John Doe"));
    }
    
    @Test
    void testGetProductByName() throws Exception {
        // Arrange
        String productName = "Test";
        List<CustomerProductDTO> products = new ArrayList<>();
        CustomerProductDTO dto = new CustomerProductDTO();
        dto.setProductId(1);
        dto.setProductName("Test Product");
        products.add(dto);
        
        when(productService.searchProductsWithSellerDetails(productName)).thenReturn(products);
        
        // Act & Assert
        mockMvc.perform(get("/product1/{productName}", productName))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].productId").value(1))
                .andExpect(jsonPath("$[0].productName").value("Test Product"));
    }
    
    @Test
    void testGetProductByNameAndCity() throws Exception {
        // Arrange
        ProductCity productCity = new ProductCity();
        productCity.setProductName("Test");
        productCity.setCityName("Test City");
        
        List<CustomerProductDTO> products = new ArrayList<>();
        CustomerProductDTO dto = new CustomerProductDTO();
        dto.setProductId(1);
        dto.setProductName("Test Product");
        dto.setSellerCity("Test City");
        products.add(dto);
        
        when(productService.searchProductsWithSellerDetails(any(ProductCity.class))).thenReturn(products);
        
        // Act & Assert
        mockMvc.perform(post("/NameCity")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(productCity)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].productId").value(1))
                .andExpect(jsonPath("$[0].productName").value("Test Product"))
                .andExpect(jsonPath("$[0].sellerCity").value("Test City"));
    }
    
    @Test
    void testUpdateProduct() throws Exception {
        // Arrange
        Integer productId = 1;
        ProductDTO updatedDetails = new ProductDTO();
        updatedDetails.setProductName("Updated Product");
        updatedDetails.setProductPrice(150.0);
        
        Product prod = new Product();
        prod.setProductName("Updated Product");
        prod.setProductPrice(150.0);
        
        when(modelMapper.map(updatedDetails, Product.class)).thenReturn(prod);
        when(productService.updateProduct(productId, prod)).thenReturn("Product updated successfully");
        
        // Act & Assert
        mockMvc.perform(put("/product/{productId}", productId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedDetails)))
                .andExpect(status().isOk())
                .andExpect(content().string("Product updated successfully"));
    }
    
    @Test
    void testDeleteProduct() throws Exception {
        // Arrange
        Integer productId = 1;
        when(productService.deleteProduct(productId)).thenReturn("Product deleted successfully");
        
        // Act & Assert
        mockMvc.perform(delete("/product/{productId}", productId))
                .andExpect(status().isOk())
                .andExpect(content().string("Product deleted successfully"));
    }
    
    @Test
    void testGetAllProducts() throws Exception {
        // Arrange
        List<Product> products = new ArrayList<>();
        Product product1 = new Product();
        product1.setProductId(1);
        product1.setProductName("Product 1");
        
        Product product2 = new Product();
        product2.setProductId(2);
        product2.setProductName("Product 2");
        
        products.add(product1);
        products.add(product2);
        
        ProductDTO dto1 = new ProductDTO();
        dto1.setProductId(1);
        dto1.setProductName("Product 1");
        
        ProductDTO dto2 = new ProductDTO();
        dto2.setProductId(2);
        dto2.setProductName("Product 2");
        
        when(productService.getAllProduct()).thenReturn(products);
        when(modelMapper.map(product1, ProductDTO.class)).thenReturn(dto1);
        when(modelMapper.map(product2, ProductDTO.class)).thenReturn(dto2);
        
        // Act & Assert
        mockMvc.perform(get("/products"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].productId").value(1))
                .andExpect(jsonPath("$[0].productName").value("Product 1"))
                .andExpect(jsonPath("$[1].productId").value(2))
                .andExpect(jsonPath("$[1].productName").value("Product 2"));
    }
    
    @Test
    void testGetCategoryProducts() throws Exception {
        // Arrange
        String category = "Vegetables";
        List<Product> products = new ArrayList<>();
        Product product = new Product();
        product.setProductId(1);
        product.setProductName("Carrot");
        product.setProductCategory(category);
        products.add(product);
        
        ProductDTO dto = new ProductDTO();
        dto.setProductId(1);
        dto.setProductName("Carrot");
        dto.setProductCategory(category);
        
        when(productService.getCategoryProducts(category)).thenReturn(products);
        when(modelMapper.map(product, ProductDTO.class)).thenReturn(dto);
        
        // Act & Assert
        mockMvc.perform(get("/products/{productCategory}", category))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].productId").value(1))
                .andExpect(jsonPath("$[0].productName").value("Carrot"))
                .andExpect(jsonPath("$[0].productCategory").value(category));
    }
    
    @Test
    void testGetProducts() throws Exception {
        // Arrange
        Integer sellerId = 1;
        List<Product> products = new ArrayList<>();
        
        Product product = new Product();
        product.setProductId(1);
        product.setProductName("Test Product");
        product.setProductQuantity(10.0);
        product.setProductQuantityType("KG");
        product.setProductPrice(100.0);
        product.setImageUrl("test-image.jpg");
        product.setProductDescription("Test Description");
        product.setProductRatingValue(4.5);
        product.setProductRatingCount(10);
        
        products.add(product);
        
        when(productService.getAllProduct(sellerId)).thenReturn(products);
        
        // Act & Assert
        mockMvc.perform(get("/product/{sellerId}", sellerId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].productId").value(1))
                .andExpect(jsonPath("$[0].productName").value("Test Product"))
                .andExpect(jsonPath("$[0].productRatingValue").value(4.5));
    }
    
    @Test
    void testGetProductsWithNullRating() throws Exception {
        // Arrange
        Integer sellerId = 1;
        List<Product> products = new ArrayList<>();
        
        Product product = new Product();
        product.setProductId(1);
        product.setProductName("Test Product");
        product.setProductQuantity(10.0);
        product.setProductQuantityType("KG");
        product.setProductPrice(100.0);
        product.setImageUrl("test-image.jpg");
        product.setProductDescription("Test Description");
        product.setProductRatingValue(null);
        product.setProductRatingCount(null);
        
        products.add(product);
        
        when(productService.getAllProduct(sellerId)).thenReturn(products);
        
        // Act & Assert
        mockMvc.perform(get("/product/{sellerId}", sellerId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].productId").value(1))
                .andExpect(jsonPath("$[0].productName").value("Test Product"))
                .andExpect(jsonPath("$[0].productRatingValue").value(0.0))
                .andExpect(jsonPath("$[0].productRatingCount").value(0));
    }
    
    @Test
    void testGetProduct() throws Exception {
        // Arrange
        Integer productId = 1;
        Product product = new Product();
        product.setProductId(productId);
        product.setProductName("Test Product");
        
        ProductDTO dto = new ProductDTO();
        dto.setProductId(productId);
        dto.setProductName("Test Product");
        
        when(productService.getProduct(productId)).thenReturn(product);
        when(modelMapper.map(product, ProductDTO.class)).thenReturn(dto);
        
        // Act & Assert
        mockMvc.perform(get("/{productId}", productId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.productId").value(productId))
                .andExpect(jsonPath("$.productName").value("Test Product"));
    }
    
    @Test
    void testGetCustomerProductByProductId_Exception() throws Exception {
        // Arrange
        Integer productId = 1;
        when(productService.getProduct(productId)).thenThrow(new ProductException("Product not found"));
        
        // Act & Assert
        mockMvc.perform(get("/product2/{productId}", productId))
                .andExpect(status().isInternalServerError());
    }
    
    @Test
    void testGetProductByName_Exception() throws Exception {
        // Arrange
        String productName = "Test";
        when(productService.searchProductsWithSellerDetails(productName)).thenThrow(new ProductException("Product not found"));
        
        // Act & Assert
        mockMvc.perform(get("/product1/{productName}", productName))
                .andExpect(status().isInternalServerError());
    }
}