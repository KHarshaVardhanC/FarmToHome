package com.ftohbackend.servicetesting;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import com.cloudinary.Cloudinary;
import com.cloudinary.Uploader;
import com.ftohbackend.dto.CustomerProductDTO;
import com.ftohbackend.dto.ProductDTO;
import com.ftohbackend.dto.ProductRequest;
import com.ftohbackend.exception.ProductException;
import com.ftohbackend.model.Product;
import com.ftohbackend.model.Seller;
import com.ftohbackend.repository.ProductRepository;
import com.ftohbackend.repository.SellerRepository;
import com.ftohbackend.service.ProductServiceImpl;
import com.ftohbackend.service.RatingServiceImpl;
import com.ftohbackend.service.SellerService;

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
    private RatingServiceImpl ratingServiceImpl;

    @InjectMocks
    private ProductServiceImpl productService;

    private Product sampleProduct;
    private ProductDTO sampleProductDTO;
    private Seller sampleSeller;
    private ProductRequest sampleProductRequest;
    private MultipartFile sampleMultipartFile;

    @BeforeEach
    void setUp() throws Exception {
        MockitoAnnotations.openMocks(this);

        // Set up sample seller
        sampleSeller = new Seller();
        sampleSeller.setSellerId(1);
        sampleSeller.setSellerFirstName("John");
        sampleSeller.setSellerLastName("Doe");
        sampleSeller.setSellerCity("New York");
        sampleSeller.setSellerPlace("Manhattan");

        // Set up sample product
        sampleProduct = new Product();
        sampleProduct.setProductId(1);
        sampleProduct.setProductName("Apple");
        sampleProduct.setProductPrice(2.99);
        sampleProduct.setProductQuantity(100.0);
        sampleProduct.setProductDescription("Fresh Apples");
        sampleProduct.setProductCategory("Fruits");
        sampleProduct.setProductQuantityType("Kg");
        sampleProduct.setImageUrl("http://example.com/apple.jpg");
        sampleProduct.setSeller(sampleSeller);
        sampleProduct.setProductRatingValue(4.5);
        sampleProduct.setProductRatingCount(10);

        // Set up sample product DTO
        sampleProductDTO = new ProductDTO();
        sampleProductDTO.setProductId(1);
        sampleProductDTO.setProductName("Apple");
        sampleProductDTO.setProductPrice(2.99);
        sampleProductDTO.setProductQuantity(100.0);
        sampleProductDTO.setImageUrl("http://example.com/apple.jpg");
        sampleProductDTO.setProductDescription("Fresh Apples");
        sampleProductDTO.setProductCategory("Fruits");
        sampleProductDTO.setSellerId(1);

        // Set up sample product request
        sampleProductRequest = new ProductRequest();
        sampleProductRequest.setSellerId(1);
        sampleProductRequest.setProductName("Apple");
        sampleProductRequest.setProductPrice(2.99);
        sampleProductRequest.setProductQuantity(100.0);
        sampleProductRequest.setProductDescription("Fresh Apples");
        sampleProductRequest.setProductCategory("Fruits");
        sampleProductRequest.setProductQuantityType("Kg");
        
        // Mock MultipartFile
        sampleMultipartFile = new MockMultipartFile(
            "image", 
            "apple.jpg", 
            "image/jpeg", 
            "test image content".getBytes()
        );
        sampleProductRequest.setImage(sampleMultipartFile);

        // Mock cloudinary behavior
        when(cloudinary.uploader()).thenReturn(uploader);
        Map<String, Object> uploadResult = new HashMap<>();
        uploadResult.put("url", "http://example.com/apple.jpg");
        when(uploader.upload(any(byte[].class), anyMap())).thenReturn(uploadResult);

        // Mock seller service
        when(sellerService.getSeller(1)).thenReturn(sampleSeller);

        // Mock model mapper
        when(modelMapper.map(any(Product.class), eq(ProductDTO.class))).thenReturn(sampleProductDTO);
    }

    @Test
    void testAddProduct_Success() throws ProductException, IOException {
        // Arrange
        when(productRepository.save(any(Product.class))).thenReturn(sampleProduct);

        // Act
        ProductDTO result = productService.addProduct(sampleProductRequest);

        // Assert
        assertNotNull(result);
        assertEquals(sampleProductDTO.getProductName(), result.getProductName());
        assertEquals(sampleProductDTO.getProductPrice(), result.getProductPrice());
        verify(productRepository, times(1)).save(any(Product.class));
    }

    @Test
    void testAddProduct_NullRequest() {
        // Act & Assert
        assertThrows(ProductException.class, () -> productService.addProduct(null));
    }

    @Test
    void testAddProduct_NullImage() {
        // Arrange
        sampleProductRequest.setImage(null);

        // Act & Assert
        assertThrows(ProductException.class, () -> productService.addProduct(sampleProductRequest));
    }

  
    @Test
    void testGetAllProduct_Success() throws ProductException {
        // Arrange
        List<Product> products = new ArrayList<>();
        products.add(sampleProduct);
        when(productRepository.findAll()).thenReturn(products);

        // Act
        List<Product> result = productService.getAllProduct();

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(sampleProduct.getProductName(), result.get(0).getProductName());
    }

    @Test
    void testGetAllProduct_EmptyList() {
        // Arrange
        when(productRepository.findAll()).thenReturn(new ArrayList<>());

        // Act & Assert
        assertThrows(ProductException.class, () -> productService.getAllProduct());
    }

    @Test
    void testGetAllProductBySellerId_Success() throws ProductException {
        // Arrange
        List<Product> products = new ArrayList<>();
        products.add(sampleProduct);
        when(productRepository.findBySellerSellerId(1)).thenReturn(products);

        // Act
        List<Product> result = productService.getAllProduct(1);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(sampleProduct.getProductName(), result.get(0).getProductName());
    }

    @Test
    void testGetAllProductBySellerId_NullSellerId() {
        // Act & Assert
        assertThrows(ProductException.class, () -> productService.getAllProduct((Integer) null));
    }

    @Test
    void testGetAllProductBySellerId_EmptyList() throws ProductException {
        // Arrange
        when(productRepository.findBySellerSellerId(anyInt())).thenReturn(new ArrayList<>());

        // Act & Assert
        assertThrows(ProductException.class, () -> productService.getAllProduct(1));
    }

    @Test
    void testGetProductByTitle_Success() throws ProductException {
        // Arrange
        List<Product> products = new ArrayList<>();
        products.add(sampleProduct);
        when(productRepository.findAll()).thenReturn(products);

        // Act
        Product result = productService.getProductByTitle("Apple");

        // Assert
        assertNotNull(result);
        assertEquals(sampleProduct.getProductName(), result.getProductName());
    }

    @Test
    void testGetProductByTitle_NullName() {
        // Act & Assert
        assertThrows(ProductException.class, () -> productService.getProductByTitle(null));
    }

    @Test
    void testGetProductByTitle_EmptyName() {
        // Act & Assert
        assertThrows(ProductException.class, () -> productService.getProductByTitle("  "));
    }

    @Test
    void testGetProductByTitle_NotFound() {
        // Arrange
        List<Product> products = new ArrayList<>();
        products.add(sampleProduct);
        when(productRepository.findAll()).thenReturn(products);

        // Act & Assert
        assertThrows(ProductException.class, () -> productService.getProductByTitle("Orange"));
    }

    @Test
    void testUpdateProduct_Success() throws Exception {
        // Arrange
        when(productRepository.findById(1)).thenReturn(Optional.of(sampleProduct));
        Product updatedDetails = new Product();
        updatedDetails.setProductPrice(3.99);
        updatedDetails.setProductName("Green Apple");
        updatedDetails.setProductQuantity(50.0);
        updatedDetails.setProductDescription("Fresh Green Apples");
        updatedDetails.setProductCategory("Organic Fruits");
        updatedDetails.setProductQuantityType("Pound");

        // Act
        String result = productService.updateProduct(1, updatedDetails);

        // Assert
        assertEquals("Product updated successfully", result);
        verify(productRepository, times(1)).save(any(Product.class));
    }

    @Test
    void testUpdateProduct_NullProductId() {
        // Act & Assert
        assertThrows(ProductException.class, () -> productService.updateProduct(null, new Product()));
    }

    @Test
    void testUpdateProduct_NullUpdatedDetails() {
        // Act & Assert
        assertThrows(ProductException.class, () -> productService.updateProduct(1, null));
    }

    @Test
    void testUpdateProduct_ProductNotFound() {
        // Arrange
        when(productRepository.findById(anyInt())).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ProductException.class, () -> productService.updateProduct(1, new Product()));
    }

    @Test
    void testUpdateProduct_WithRatingUpdate() throws Exception {
        // Arrange
        when(productRepository.findById(1)).thenReturn(Optional.of(sampleProduct));
        Product updatedDetails = new Product();
        updatedDetails.setProductRatingValue(5.0);
        when(ratingServiceImpl.getRatingsByProductId(1)).thenReturn(new ArrayList<>());

        // Act
        String result = productService.updateProduct(1, updatedDetails);

        // Assert
        assertEquals("Product updated successfully", result);
        assertEquals(5.0, sampleProduct.getProductRatingValue());
        assertEquals(1, sampleProduct.getProductRatingCount());
        verify(productRepository, times(1)).save(any(Product.class));
    }

    @Test
    void testDeleteProduct_Success() throws ProductException {
        // Arrange
        when(productRepository.findById(1)).thenReturn(Optional.of(sampleProduct));

        // Act
        String result = productService.deleteProduct(1);

        // Assert
        assertEquals("Product deleted successfully", result);
        verify(productRepository, times(1)).delete(any(Product.class));
    }

    @Test
    void testDeleteProduct_NullProductId() {
        // Act & Assert
        assertThrows(ProductException.class, () -> productService.deleteProduct(null));
    }

    @Test
    void testDeleteProduct_ProductNotFound() {
        // Arrange
        when(productRepository.findById(anyInt())).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ProductException.class, () -> productService.deleteProduct(1));
    }

    @Test
    void testGetProductsBySellerId_Success() throws ProductException {
        // Arrange
        List<Product> products = new ArrayList<>();
        products.add(sampleProduct);
        when(productRepository.findBySellerSellerId(1)).thenReturn(products);

        // Act
        List<Product> result = productService.getProductsBySellerId(1);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(sampleProduct.getProductName(), result.get(0).getProductName());
    }

    @Test
    void testGetProductsBySellerId_NullSellerId() {
        // Act & Assert
        assertThrows(ProductException.class, () -> productService.getProductsBySellerId(null));
    }

    @Test
    void testGetProductsBySellerId_EmptyList() throws ProductException {
        // Arrange
        when(productRepository.findBySellerSellerId(anyInt())).thenReturn(new ArrayList<>());

        // Act & Assert
        assertThrows(ProductException.class, () -> productService.getProductsBySellerId(1));
    }

    @Test
    void testSearchProductsWithSellerDetails_Success() throws ProductException {
        // Arrange
        List<Product> products = new ArrayList<>();
        Product outOfStockProduct = new Product();
        outOfStockProduct.setProductId(2);
        outOfStockProduct.setProductName("Banana");
        outOfStockProduct.setProductPrice(1.99);
        outOfStockProduct.setProductQuantity(0.0);
        outOfStockProduct.setProductDescription("Fresh Bananas");
        outOfStockProduct.setProductCategory("Fruits");
        outOfStockProduct.setProductQuantityType("Kg");
        outOfStockProduct.setImageUrl("http://example.com/banana.jpg");
        outOfStockProduct.setSeller(sampleSeller);
        outOfStockProduct.setProductRatingValue(4.0);
        outOfStockProduct.setProductRatingCount(5);
        
        products.add(outOfStockProduct);
        when(productRepository.findProductsByNameWithSeller("Banana")).thenReturn(products);

        // Act
        List<Product> result = productService.searchProductsWithSellerDetails("Banana");

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Banana", result.get(0).getProductName());
        
        // Fix: Check first name and last name separately instead of expecting a combined string
        assertEquals("John", result.get(0).getSeller().getSellerFirstName());
        assertEquals("Doe", result.get(0).getSeller().getSellerLastName());
    }

    @Test
    void testSearchProductsWithSellerDetails_NullProductName() {
        // Act & Assert
        assertThrows(ProductException.class, () -> productService.searchProductsWithSellerDetails((String) null));
    }

    @Test
    void testSearchProductsWithSellerDetails_EmptyProductName() {
        // Act & Assert
        assertThrows(ProductException.class, () -> productService.searchProductsWithSellerDetails("  "));
    }

    @Test
    void testSearchProductsWithSellerDetails_NoProductsFound() throws ProductException {
        // Arrange
        when(productRepository.findProductsByNameWithSeller(anyString())).thenReturn(new ArrayList<>());

        // Act & Assert
        ProductException exception = assertThrows(ProductException.class, 
            () -> productService.searchProductsWithSellerDetails("Orange"));
        
        // Optional: Verify the exception message if needed
        assertEquals("No products found with name: Orange", exception.getMessage());
    }

    @Test
    void testGetProduct_Success() throws ProductException {
        // Arrange
        when(productRepository.findById(1)).thenReturn(Optional.of(sampleProduct));

        // Act
        Product result = productService.getProduct(1);

        // Assert
        assertNotNull(result);
        assertEquals(sampleProduct.getProductName(), result.getProductName());
    }

    @Test
    void testGetProduct_NullProductId() {
        // Act & Assert
        assertThrows(ProductException.class, () -> productService.getProduct(null));
    }

    @Test
    void testGetProduct_ProductNotFound() {
        // Arrange
        when(productRepository.findById(anyInt())).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ProductException.class, () -> productService.getProduct(1));
    }

    @Test
    void testGetCategoryProducts_Success() throws ProductException {
        // Arrange
        List<Product> products = new ArrayList<>();
        products.add(sampleProduct);
        when(productRepository.findByProductCategoryContainingIgnoreCase("Fruits")).thenReturn(products);

        // Act
        List<Product> result = productService.getCategoryProducts("Fruits");

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(sampleProduct.getProductName(), result.get(0).getProductName());
    }
}