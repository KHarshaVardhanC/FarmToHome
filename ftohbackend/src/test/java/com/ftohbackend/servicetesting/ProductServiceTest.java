/*package com.ftohbackend.servicetesting;


import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.web.multipart.MultipartFile;

import com.cloudinary.Cloudinary;
import com.cloudinary.Uploader;
import com.ftohbackend.dto.CustomerProductDTO;
import com.ftohbackend.dto.ProductDTO;
import com.ftohbackend.dto.ProductRequest;
import com.ftohbackend.exception.ProductException;
import com.ftohbackend.exception.SellerException;
import com.ftohbackend.model.Product;
import com.ftohbackend.model.Seller;
import com.ftohbackend.repository.ProductRepository;
import com.ftohbackend.repository.SellerRepository;
import com.ftohbackend.service.ProductServiceImpl;
import com.ftohbackend.service.SellerService;

@ExtendWith(MockitoExtension.class)
public class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;
    
    @Mock
    private SellerRepository sellerRepository;
    
    @Mock
    private SellerService sellerService;
    
    @Mock
    private Cloudinary cloudinary;
    
    @Mock
    private Uploader uploader;
    
    @Mock
    private ModelMapper modelMapper;
    
    @Mock
    private MultipartFile mockMultipartFile;

    @InjectMocks
    private ProductServiceImpl productService;

    private Product product;
    private ProductRequest productRequest;
    private ProductDTO productDTO;
    private Seller seller;

    @BeforeEach
    public void setUp() {
        seller = new Seller();
//        seller.setSellerId(1);
        seller.setSellerEmail("test@example.com");
        seller.setSellerFirstName("John");
        seller.setSellerLastName("Doe");
        
        product = new Product();
        product.setProductId(1);
        product.setProductName("Test Product");
        product.setProductPrice(100.0);
        product.setProductQuantity(10.0);
        product.setImageUrl("http://example.com/image.jpg");
        product.setProductDescription("Test Description");
        product.setSeller(seller);
        
        productRequest = new ProductRequest();
        productRequest.setProductName("Test Product");
        productRequest.setProductPrice(100.0);
        productRequest.setProductQuantity(10.0);
        productRequest.setProductDescription("Test Description");
        productRequest.setSellerId(1);
        productRequest.setImage(mockMultipartFile);
        
        productDTO = new ProductDTO();
        productDTO.setProductId(1);
        productDTO.setProductName("Test Product");
        productDTO.setProductPrice(100.0);
        productDTO.setProductQuantity(10.0);
        productDTO.setImageUrl("http://example.com/image.jpg");
        productDTO.setProductDescription("Test Description");
        productDTO.setSellerId(1);
    }

    @AfterEach
    public void tearDown() {
        product = null;
        productRequest = null;
        productDTO = null;
        seller = null;
    }

    @Test
    @DisplayName("JUnit test for addProduct operation")
    public void givenProductRequest_whenAddProduct_thenReturnProductDTO() throws Exception {
        // given - precondition or setup
        given(sellerService.getSeller(anyInt())).willReturn(seller);
        given(cloudinary.uploader()).willReturn(uploader);
        
        Map<String, Object> uploadResult = Map.of("url", "http://example.com/image.jpg");
        given(uploader.upload(any(byte[].class), any())).willReturn(uploadResult);
        
        given(productRepository.save(any(Product.class))).willReturn(product);
        given(modelMapper.map(any(Product.class), any())).willReturn(productDTO);
        
        // when - action or behavior
        ProductDTO savedProduct = productService.addProduct(productRequest);
        
        // then - verify the output
        assertThat(savedProduct).isNotNull();
        assertThat(savedProduct.getProductName()).isEqualTo(productRequest.getProductName());
        verify(productRepository, times(1)).save(any(Product.class));
    }
    
    @Test
    @DisplayName("JUnit test for addProduct operation - with null ProductRequest")
    public void givenNullProductRequest_whenAddProduct_thenThrowsProductException() {
        // given - precondition or setup
        ProductRequest nullRequest = null;
        
        // when - action or behavior & then - verify the output
        assertThrows(ProductException.class, () -> {
            productService.addProduct(nullRequest);
        });
        
        verify(productRepository, never()).save(any(Product.class));
    }
    
    @Test
    @DisplayName("JUnit test for addProduct operation - with null image")
    public void givenProductRequestWithNullImage_whenAddProduct_thenThrowsProductException() throws SellerException {
        // given - precondition or setup
        productRequest.setImage(null);
        given(sellerService.getSeller(anyInt())).willReturn(seller);
        
        // when - action or behavior & then - verify the output
        assertThrows(ProductException.class, () -> {
            productService.addProduct(productRequest);
        });
        
        verify(productRepository, never()).save(any(Product.class));
    }
    
    @Test
    @DisplayName("JUnit test for getAllProduct operation")
    public void givenProductsList_whenGetAllProduct_thenReturnProductsList() throws ProductException {
        // given - precondition or setup
        Product product2 = new Product();
        product2.setProductId(2);
        product2.setProductName("Test Product 2");
        
        given(productRepository.findAll()).willReturn(List.of(product, product2));
        
        // when - action or behavior
        List<Product> products = productService.getAllProduct();
        
        // then - verify the output
        assertThat(products).isNotNull();
        assertThat(products.size()).isEqualTo(2);
        verify(productRepository, times(1)).findAll();
    }
    
    @Test
    @DisplayName("JUnit test for getAllProduct operation - Empty List (Negative Scenario)")
    public void givenEmptyProductsList_whenGetAllProduct_thenThrowsProductException() {
        // given - precondition or setup
        given(productRepository.findAll()).willReturn(Collections.emptyList());
        
        // when - action or behavior & then - verify the output
        assertThrows(ProductException.class, () -> {
            productService.getAllProduct();
        });
        
        verify(productRepository, times(1)).findAll();
    }
    
    @Test
    @DisplayName("JUnit test for getAllProduct operation by sellerId")
    public void givenSellerId_whenGetAllProduct_thenReturnProductsList() throws ProductException {
        // given - precondition or setup
        Product product2 = new Product();
        product2.setProductId(2);
        product2.setProductName("Test Product 2");
        
        given(productRepository.findBySellerSellerId(anyInt())).willReturn(List.of(product, product2));
        
        // when - action or behavior
        List<Product> products = productService.getAllProduct(1);
        
        // then - verify the output
        assertThat(products).isNotNull();
        assertThat(products.size()).isEqualTo(2);
        verify(productRepository, times(1)).findBySellerSellerId(1);
    }
    
    @Test
    @DisplayName("JUnit test for getAllProduct operation by sellerId - Empty List")
    public void givenSellerId_whenGetAllProductWithNoResults_thenThrowsProductException() throws ProductException {
        // given - precondition or setup
        given(productRepository.findBySellerSellerId(anyInt())).willReturn(Collections.emptyList());
        
        // when - action or behavior & then - verify the output
        assertThrows(ProductException.class, () -> {
            productService.getAllProduct(1);
        });
        
        verify(productRepository, times(1)).findBySellerSellerId(1);
    }
    
    @Test
    @DisplayName("JUnit test for getProductByTitle operation")
    public void givenProductName_whenGetProductByTitle_thenReturnProduct() throws ProductException {
        // given - precondition or setup
        given(productRepository.findAll()).willReturn(List.of(product));
        
        // when - action or behavior
        Product foundProduct = productService.getProductByTitle("Test Product");
        
        // then - verify the output
        assertThat(foundProduct).isNotNull();
        assertThat(foundProduct.getProductName()).isEqualTo("Test Product");
    }
    
    @Test
    @DisplayName("JUnit test for getProductByTitle operation - Product Not Found")
    public void givenInvalidProductName_whenGetProductByTitle_thenThrowsProductException() {
        // given - precondition or setup
        given(productRepository.findAll()).willReturn(List.of(product));
        
        // when - action or behavior & then - verify the output
        assertThrows(ProductException.class, () -> {
            productService.getProductByTitle("Non-existent Product");
        });
    }
    
    @Test
    @DisplayName("JUnit test for updateProduct operation")
    public void givenProductIdAndUpdatedDetails_whenUpdateProduct_thenReturnSuccess() throws ProductException {
        // given - precondition or setup
        Product updatedProduct = new Product();
        updatedProduct.setProductName("Updated Product");
        updatedProduct.setProductPrice(150.0);
        updatedProduct.setProductQuantity(20.0);
        
        given(productRepository.findById(anyInt())).willReturn(Optional.of(product));
        given(productRepository.save(any(Product.class))).willReturn(product);
        
        // when - action or behavior
        String result = productService.updateProduct(1, updatedProduct);
        
        // then - verify the output
        assertThat(result).isEqualTo("Product updated successfully");
        verify(productRepository, times(1)).save(any(Product.class));
    }
    
    @Test
    @DisplayName("JUnit test for updateProduct operation - Product Not Found")
    public void givenInvalidProductId_whenUpdateProduct_thenThrowsProductException() {
        // given - precondition or setup
        Product updatedProduct = new Product();
        updatedProduct.setProductName("Updated Product");
        
        given(productRepository.findById(anyInt())).willReturn(Optional.empty());
        
        // when - action or behavior & then - verify the output
        assertThrows(ProductException.class, () -> {
            productService.updateProduct(999, updatedProduct);
        });
        
        verify(productRepository, never()).save(any(Product.class));
    }
    
    @Test
    @DisplayName("JUnit test for deleteProduct operation")
    public void givenProductId_whenDeleteProduct_thenReturnSuccess() throws ProductException {
        // given - precondition or setup
        given(productRepository.findById(anyInt())).willReturn(Optional.of(product));
        willDoNothing().given(productRepository).delete(any(Product.class));
        
        // when - action or behavior
        String result = productService.deleteProduct(1);
        
        // then - verify the output
        assertThat(result).isEqualTo("Product deleted successfully");
        verify(productRepository, times(1)).delete(any(Product.class));
    }
    
    @Test
    @DisplayName("JUnit test for deleteProduct operation - Product Not Found")
    public void givenInvalidProductId_whenDeleteProduct_thenThrowsProductException() {
        // given - precondition or setup
        given(productRepository.findById(anyInt())).willReturn(Optional.empty());
        
        // when - action or behavior & then - verify the output
        assertThrows(ProductException.class, () -> {
            productService.deleteProduct(999);
        });
        
        verify(productRepository, never()).delete(any(Product.class));
    }
    
    @Test
    @DisplayName("JUnit test for getProductsBySellerId operation")
    public void givenSellerId_whenGetProductsBySellerId_thenReturnProductsList() throws ProductException {
        // given - precondition or setup
        Product product2 = new Product();
        product2.setProductId(2);
        product2.setProductName("Test Product 2");
        
        given(productRepository.findBySellerSellerId(anyInt())).willReturn(List.of(product, product2));
        
        // when - action or behavior
        List<Product> products = productService.getProductsBySellerId(1);
        
        // then - verify the output
        assertThat(products).isNotNull();
        assertThat(products.size()).isEqualTo(2);
        verify(productRepository, times(1)).findBySellerSellerId(1);
    }
    
    @Test
    @DisplayName("JUnit test for searchProductsWithSellerDetails operation")
    public void givenProductName_whenSearchProductsWithSellerDetails_thenReturnCustomerProductDTOs() throws ProductException {
        // given - precondition or setup
        given(productRepository.findProductsByNameWithSeller(any(String.class))).willReturn(List.of(product));
        
        // when - action or behavior
        List<CustomerProductDTO> result = productService.searchProductsWithSellerDetails("Test");
        
        // then - verify the output
        assertThat(result).isNotNull();
        assertThat(result.size()).isEqualTo(1);
    }
    
    @Test
    @DisplayName("JUnit test for searchProductsWithSellerDetails operation - No Products Found")
    public void givenInvalidProductName_whenSearchProductsWithSellerDetails_thenThrowsProductException() throws ProductException {
        // given - precondition or setup
        given(productRepository.findProductsByNameWithSeller(any(String.class))).willReturn(Collections.emptyList());
        
        // when - action or behavior & then - verify the output
        assertThrows(ProductException.class, () -> {
            productService.searchProductsWithSellerDetails("Non-existent");
        });
    }
    
    @Test
    @DisplayName("JUnit test for getProduct operation")
    public void givenProductId_whenGetProduct_thenReturnProduct() throws ProductException {
        // given - precondition or setup
        given(productRepository.findById(anyInt())).willReturn(Optional.of(product));
        
        // when - action or behavior
        Product foundProduct = productService.getProduct(1);
        
        // then - verify the output
        assertThat(foundProduct).isNotNull();
        assertThat(foundProduct.getProductId()).isEqualTo(1);
    }
    
    @Test
    @DisplayName("JUnit test for getProduct operation - Product Not Found")
    public void givenInvalidProductId_whenGetProduct_thenThrowsProductException() {
        // given - precondition or setup
        given(productRepository.findById(anyInt())).willReturn(Optional.empty());
        
        // when - action or behavior & then - verify the output
        assertThrows(ProductException.class, () -> {
            productService.getProduct(999);
        });
    }
}
*/