//package com.ftohbackend.controllertesting;
//
//import static org.hamcrest.CoreMatchers.is;
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.ArgumentMatchers.anyInt;
//import static org.mockito.ArgumentMatchers.anyString;
//import static org.mockito.BDDMockito.given;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
//import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
//
//import java.util.Arrays;
//import java.util.List;
//
//import org.junit.jupiter.api.AfterEach;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.modelmapper.ModelMapper;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
//import org.springframework.boot.test.mock.mockito.MockBean;
//import org.springframework.http.MediaType;
//import org.springframework.mock.web.MockMultipartFile;
//import org.springframework.test.context.ContextConfiguration;
//import org.springframework.test.web.servlet.MockMvc;
//import org.springframework.test.web.servlet.ResultActions;
//import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.ftohbackend.controller.ProductController;
//import com.ftohbackend.controller.ProductControllerImpl;
//import com.ftohbackend.dto.CustomerProductDTO;
//import com.ftohbackend.dto.ProductDTO;
//import com.ftohbackend.dto.ProductRequest;
//import com.ftohbackend.dto.SellerProductDTO;
//import com.ftohbackend.exception.ProductException;
//import com.ftohbackend.model.Product;
//import com.ftohbackend.model.Seller;
//import com.ftohbackend.service.ProductService;
//import com.ftohbackend.service.ProductServiceImpl;
//
//@WebMvcTest(ProductControllerImpl.class)
//@ContextConfiguration(classes = { ProductController.class})
//public class ProductControllerTest {
//
//    @Autowired
//    private MockMvc mockMvc;
//
//    @Autowired
//    private ObjectMapper objectMapper;
//
//    @MockBean
//    private ProductService productService;
//
//    @MockBean
//    private ProductServiceImpl productServiceImpl;
//
//    @MockBean
//    private ModelMapper modelMapper;
//
//    private Product product;
//    private ProductDTO productDTO;
//    private ProductRequest productRequest;
//    private CustomerProductDTO customerProductDTO;
//    private SellerProductDTO sellerProductDTO;
//    private Seller seller;
//
//    @BeforeEach
//    public void setUp() {
//        // Setup seller
//        seller = new Seller();
//        seller.setSellerId(1);
//        seller.setSellerEmail("seller@example.com");
//        seller.setSellerFirstName("Jane");
//        seller.setSellerLastName("Smith");
//        seller.setSellerMobileNumber("9876543210");
//        seller.setSellerPlace("Uptown");
//        seller.setSellerCity("Los Angeles");
//        seller.setSellerState("California");
//        seller.setSellerPincode("900001");
//        seller.setSellerPassword("seller123");
//        seller.setSellerStatus("Active");
//
//        // Setup product
//        product = new Product();
//        product.setProductId(1);
//        product.setSeller(seller);
//        product.setProductPrice(100.0);
//        product.setProductName("Test Product");
//        product.setProductQuantity(10.0);
//        product.setProductQuantityType("kg");
//        product.setImageUrl("http://example.com/image.jpg");
//        product.setProductDescription("A test product description");
//        product.setProductCategory("Grocery");
//        product.setProductRatingValue(4.5);
//        product.setProductRatingCount(10);
//
//        // Setup ProductDTO
//        productDTO = new ProductDTO();
//        productDTO.setProductId(1);
//        productDTO.setSellerId(1);
//        productDTO.setProductPrice(100.0);
//        productDTO.setProductName("Test Product");
//        productDTO.setProductQuantity(10.0);
//        productDTO.setProductQuantityType("kg");
//        productDTO.setImageUrl("http://example.com/image.jpg");
//        productDTO.setProductDescription("A test product description");
//        productDTO.setProductCategory("Grocery");
//        productDTO.setProductRatingValue(4.5);
//        productDTO.setProductRatingCount(10);
//
//        // Setup ProductRequest
//        productRequest = new ProductRequest();
//        productRequest.setSellerId(1);
//        productRequest.setProductPrice(100.0);
//        productRequest.setProductName("Test Product");
//        productRequest.setProductQuantity(10.0);
//        productRequest.setProductQuantityType("kg");
//        productRequest.setProductDescription("A test product description");
//        productRequest.setProductCategory("Grocery");
//
//        // Setup CustomerProductDTO
//        customerProductDTO = new CustomerProductDTO();
//        customerProductDTO.setProductId(1);
//        customerProductDTO.setProductName("Test Product");
//        customerProductDTO.setProductPrice(100.0);
//        customerProductDTO.setProductDescription("A test product description");
//        customerProductDTO.setProductQuantity(10.0);
//        customerProductDTO.setProductQuantityType("kg");
//        customerProductDTO.setImageUrl("http://example.com/image.jpg");
//        customerProductDTO.setProductRatingValue(4.5);
//        customerProductDTO.setProductRatingCount(10);
//        customerProductDTO.setSellerPlace("Uptown");
//        customerProductDTO.setSellerCity("Los Angeles");
//        customerProductDTO.setSellerName("Jane Smith");
//
//        // Setup SellerProductDTO
//        sellerProductDTO = new SellerProductDTO();
//        sellerProductDTO.setProductId(1);
//        sellerProductDTO.setProductName("Test Product");
//        sellerProductDTO.setProductQuantity(10.0);
//        sellerProductDTO.setProductQuantityType("kg");
//        sellerProductDTO.setProductPrice(100.0);
//        sellerProductDTO.setImageUrl("http://example.com/image.jpg");
//        sellerProductDTO.setProductDescription("A test product description");
//        sellerProductDTO.setProductRatingValue(4.5);
//        sellerProductDTO.setProductRatingCount(10);
//    }
//
//    @AfterEach
//    public void tearDown() {
//        product = null;
//        productDTO = null;
//        productRequest = null;
//        customerProductDTO = null;
//        sellerProductDTO = null;
//        seller = null;
//    }
//
//    @Test
//    @DisplayName("Test addProduct method")
//    public void testAddProduct() throws Exception {
//        // given - precondition or setup
//        MockMultipartFile imageFile = new MockMultipartFile(
//            "image", 
//            "test-image.jpg", 
//            MediaType.IMAGE_JPEG_VALUE, 
//            "image content".getBytes()
//        );
//        
//        given(productService.addProduct(any(ProductRequest.class))).willReturn(productDTO);
//
//        // when - action or the behavior
//        ResultActions response = mockMvc.perform(
//            MockMvcRequestBuilders.multipart("/product")
//                .file(imageFile)
//                .param("sellerId", "1")
//                .param("productPrice", "100.0")
//                .param("productName", "Test Product")
//                .param("productQuantity", "10.0")
//                .param("productQuantityType", "kg")
//                .param("productDescription", "A test product description")
//                .param("productCategory", "Grocery")
//                .contentType(MediaType.MULTIPART_FORM_DATA_VALUE)
//        );
//
//        // then - verify the output
//        response.andDo(print())
//            .andExpect(status().isCreated())
//            .andExpect(jsonPath("$.productId", is(productDTO.getProductId())))
//            .andExpect(jsonPath("$.productName", is(productDTO.getProductName())))
//            .andExpect(jsonPath("$.productPrice", is(productDTO.getProductPrice())));
//    }
//
//    @Test
//    @DisplayName("Test getCustomerProductByProductId method")
//    public void testGetCustomerProductByProductId() throws Exception {
//        // given - precondition or setup
//        given(productService.getProduct(anyInt())).willReturn(product);
//
//        // when - action or the behavior
//        ResultActions response = mockMvc.perform(get("/product2/{productId}", 1));
//
//        // then - verify the output
//        response.andDo(print())
//            .andExpect(status().isOk())
//            .andExpect(jsonPath("$.productId", is(customerProductDTO.getProductId())))
//            .andExpect(jsonPath("$.productName", is(customerProductDTO.getProductName())))
//            .andExpect(jsonPath("$.productPrice", is(customerProductDTO.getProductPrice())));
//    }
//
//    @Test
//    @DisplayName("Test getProductByName method")
//    public void testGetProductByName() throws Exception {
//        // given - precondition or setup
//        List<CustomerProductDTO> customerProducts = Arrays.asList(customerProductDTO);
//        given(productService.searchProductsWithSellerDetails(anyString())).willReturn(customerProducts);
//
//        // when - action or the behavior
//        ResultActions response = mockMvc.perform(get("/product1/{productName}", "Test"));
//
//        // then - verify the output
//        response.andDo(print())
//            .andExpect(status().isOk())
//            .andExpect(jsonPath("$.size()", is(customerProducts.size())))
//            .andExpect(jsonPath("$[0].productId", is(customerProductDTO.getProductId())))
//            .andExpect(jsonPath("$[0].productName", is(customerProductDTO.getProductName())));
//    }
//
//    @Test
//    @DisplayName("Test updateProduct method")
//    public void testUpdateProduct() throws Exception {
//        // given - precondition or setup
//        given(modelMapper.map(any(ProductDTO.class), any())).willReturn(product);
//        given(productService.updateProduct(anyInt(), any(Product.class))).willReturn("Product updated successfully");
//
//        // when - action or the behavior
//        ResultActions response = mockMvc.perform(put("/product/{productId}", 1)
//            .contentType(MediaType.APPLICATION_JSON)
//            .content(objectMapper.writeValueAsString(productDTO)));
//
//        // then - verify the output
//        response.andDo(print())
//            .andExpect(status().isOk())
//            .andExpect(jsonPath("$", is("Product updated successfully")));
//    }
//
//    @Test
//    @DisplayName("Test deleteProduct method")
//    public void testDeleteProduct() throws Exception {
//        // given - precondition or setup
//        given(productService.deleteProduct(anyInt())).willReturn("Product deleted successfully");
//
//        // when - action or the behavior
//        ResultActions response = mockMvc.perform(delete("/product/{productId}", 1));
//
//        // then - verify the output
//        response.andDo(print())
//            .andExpect(status().isOk())
//            .andExpect(jsonPath("$", is("Product deleted successfully")));
//    }
//
//    @Test
//    @DisplayName("Test getAllProducts method")
//    public void testGetAllProducts() throws Exception {
//        // given - precondition or setup
//        List<Product> products = Arrays.asList(product);
//        List<ProductDTO> productDTOs = Arrays.asList(productDTO);
//        
//        given(productService.getAllProduct()).willReturn(products);
//        given(modelMapper.map(any(Product.class), any())).willReturn(productDTO);
//
//        // when - action or the behavior
//        ResultActions response = mockMvc.perform(get("/products"));
//
//        // then - verify the output
//        response.andDo(print())
//            .andExpect(status().isOk())
//            .andExpect(jsonPath("$.size()", is(productDTOs.size())));
//    }
//
//    @Test
//    @DisplayName("Test getCategoryProducts method")
//    public void testGetCategoryProducts() throws Exception {
//        // given - precondition or setup
//        List<Product> products = Arrays.asList(product);
//        List<ProductDTO> productDTOs = Arrays.asList(productDTO);
//        
//        given(productService.getCategoryProducts(anyString())).willReturn(products);
//        given(modelMapper.map(any(Product.class), any())).willReturn(productDTO);
//
//        // when - action or the behavior
//        ResultActions response = mockMvc.perform(get("products/{productCategory}", "Grocery"));
//
//        // then - verify the output
//        response.andDo(print())
//            .andExpect(status().isOk())
//            .andExpect(jsonPath("$.size()", is(productDTOs.size())));
//    }
//
//    @Test
//    @DisplayName("Test getProducts method for seller")
//    public void testGetProductsBySellerId() throws Exception {
//        // given - precondition or setup
//        List<Product> products = Arrays.asList(product);
//        given(productService.getAllProduct(anyInt())).willReturn(products);
//
//        // when - action or the behavior
//        ResultActions response = mockMvc.perform(get("/product/{sellerId}", 1));
//
//        // then - verify the output
//        response.andDo(print())
//            .andExpect(status().isOk())
//            .andExpect(jsonPath("$.size()", is(1)))
//            .andExpect(jsonPath("$[0].productId", is(sellerProductDTO.getProductId())))
//            .andExpect(jsonPath("$[0].productName", is(sellerProductDTO.getProductName())));
//    }
//
//    @Test
//    @DisplayName("Test getProduct method by ID")
//    public void testGetProductById() throws Exception {
//        // given - precondition or setup
//        given(productService.getProduct(anyInt())).willReturn(product);
//        given(modelMapper.map(any(Product.class), any())).willReturn(productDTO);
//
//        // when - action or the behavior
//        ResultActions response = mockMvc.perform(get("/{productId}", 1));
//
//        // then - verify the output
//        response.andDo(print())
//            .andExpect(status().isOk())
//            .andExpect(jsonPath("$.productId", is(productDTO.getProductId())))
//            .andExpect(jsonPath("$.productName", is(productDTO.getProductName())));
//    }
//
//    @Test
//    @DisplayName("Test getProduct method throws ProductException")
//    public void testGetProductByIdThrowsException() throws Exception {
//        // given - precondition or setup
//        given(productService.getProduct(anyInt())).willThrow(new ProductException("Product not found"));
//
//        // when - action or the behavior
//        ResultActions response = mockMvc.perform(get("/{productId}", 999));
//
//        // then - verify the output
//        response.andDo(print())
//            .andExpect(status().isNotFound());
//    }
//
//    @Test
//    @DisplayName("Test addProduct method throws Exception")
//    public void testAddProductThrowsException() throws Exception {
//        // given - precondition or setup
//        MockMultipartFile imageFile = new MockMultipartFile(
//            "image", 
//            "test-image.jpg", 
//            MediaType.IMAGE_JPEG_VALUE, 
//            "image content".getBytes()
//        );
//        
//        given(productService.addProduct(any(ProductRequest.class))).willThrow(new Exception("Invalid product data"));
//
//        // when - action or the behavior
//        ResultActions response = mockMvc.perform(
//            MockMvcRequestBuilders.multipart("/product")
//                .file(imageFile)
//                .param("sellerId", "1")
//                .param("productPrice", "100.0")
//                .param("productName", "Test Product")
//                .param("productQuantity", "10.0")
//                .param("productQuantityType", "kg")
//                .param("productDescription", "A test product description")
//                .param("productCategory", "Grocery")
//                .contentType(MediaType.MULTIPART_FORM_DATA_VALUE)
//        );
//
//        // then - verify the output
//        response.andDo(print())
//            .andExpect(status().isInternalServerError());
//    }
//}