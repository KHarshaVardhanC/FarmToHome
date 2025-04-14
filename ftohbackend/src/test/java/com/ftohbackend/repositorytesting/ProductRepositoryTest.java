//package com.ftohbackend.repositorytesting;
//
//
//import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
//import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
//
//@DataJpaTest
//@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
//public class ProductRepositoryTest {
////
////    @Autowired
////    private ProductRepository productRepository;
////
////    @Autowired
////    private SellerRepository sellerRepository;
////
////    private Seller testSeller;
////    private Product testProduct;
////
////    @BeforeEach
////    public void setUp() {
////        testSeller = new Seller();
////        testSeller.setSellerFirstName("John");
////        testSeller.setSellerLastName("Doe");
////        testSeller.setSellerEmail("john.doe@example.com");
////        testSeller.setSellerPassword("securepass");
////        testSeller.setSellerMobileNumber("9876543210");
////        testSeller.setSellerCity("City");
////        testSeller.setSellerPlace("Place");
////        testSeller.setSellerState("State");
////        testSeller.setSellerPincode("123456");
////        testSeller.setSellerStatus("Active");
////        sellerRepository.save(testSeller);
////
////        testProduct = new Product();
////        testProduct.setProductName("Laptop");
////        testProduct.setProductDescription("Gaming laptop");
////        testProduct.setProductPrice(1500.0);
////        testProduct.setProductQuantity(10.0);
////        testProduct.setImageUrl("http://example.com/laptop.jpg");
////        testProduct.setSeller(testSeller);
////        productRepository.save(testProduct);
////    }
////
////    @AfterEach
////    public void tearDown() {
////        productRepository.deleteAll();
////        sellerRepository.deleteAll();
////    }
////
////    @Test
////    @DisplayName("Save product test")
////    public void testSaveProduct() {
////        Product product = new Product();
////        product.setProductName("Smartphone");
////        product.setProductDescription("Latest model");
////        product.setProductPrice(999.99);
////        product.setProductQuantity(20.0);
////        product.setImageUrl("http://example.com/smartphone.jpg");
////        product.setSeller(testSeller);
////
////        Product savedProduct = productRepository.save(product);
////
////        assertThat(savedProduct).isNotNull();
////        assertThat(savedProduct.getProductId()).isNotNull();
////        assertThat(savedProduct.getProductName()).isEqualTo("Smartphone");
////        assertThat(savedProduct.getProductPrice()).isEqualTo(999.99);
////    }
////
////    @Test
////    @DisplayName("Find product by ID test")
////    public void testFindById() {
////        Product found = productRepository.findById(testProduct.getProductId()).orElse(null);
////
////        assertThat(found).isNotNull();
////        assertThat(found.getProductName()).isEqualTo("Laptop");
////        assertThat(found.getProductDescription()).isEqualTo("Gaming laptop");
////    }
////
////    @Test
////    @DisplayName("Find products by seller ID test")
////    public void testFindBySellerSellerId() throws ProductException {
////        // Add another product with the same seller
////        Product anotherProduct = new Product();
////        anotherProduct.setProductName("Headphones");
////        anotherProduct.setProductDescription("Wireless headphones");
////        anotherProduct.setProductPrice(199.99);
////        anotherProduct.setProductQuantity(30.0);
////        anotherProduct.setImageUrl("http://example.com/headphones.jpg");
////        anotherProduct.setSeller(testSeller);
////        productRepository.save(anotherProduct);
////
////        List<Product> products = productRepository.findBySellerSellerId(testSeller.getSellerId());
////
////        assertThat(products).hasSize(2);
////        assertThat(products.get(0).getSeller().getSellerId()).isEqualTo(testSeller.getSellerId());
////        assertThat(products.get(1).getSeller().getSellerId()).isEqualTo(testSeller.getSellerId());
////    }
////
////    @Test
////    @DisplayName("Delete product test")
////    public void testDeleteProduct() {
////        productRepository.deleteById(testProduct.getProductId());
////
////        assertThat(productRepository.findById(testProduct.getProductId())).isEmpty();
////    }
////
////    @Test
////    @DisplayName("Find all products test")
////    public void testFindAllProducts() {
////        // Add another product
////        Product anotherProduct = new Product();
////        anotherProduct.setProductName("Tablet");
////        anotherProduct.setProductDescription("Android tablet");
////        anotherProduct.setProductPrice(399.99);
////        anotherProduct.setProductQuantity(15.0);
////        anotherProduct.setImageUrl("http://example.com/tablet.jpg");
////        anotherProduct.setSeller(testSeller);
////        productRepository.save(anotherProduct);
////
////        List<Product> allProducts = productRepository.findAll();
////
////        assertThat(allProducts).isNotNull();
////        assertThat(allProducts).hasSize(2);
////    }
////
////    @Test
////    @DisplayName("Find products by name containing test")
////    public void testFindByProductNameContainingIgnoreCase() throws ProductException {
////        // Add products with different names
////        Product product1 = new Product();
////        product1.setProductName("Gaming Mouse");
////        product1.setProductDescription("RGB gaming mouse");
////        product1.setProductPrice(89.99);
////        product1.setProductQuantity(25.0);
////        product1.setImageUrl("http://example.com/mouse.jpg");
////        product1.setSeller(testSeller);
////        productRepository.save(product1);
////
////        Product product2 = new Product();
////        product2.setProductName("Gaming Keyboard");
////        product2.setProductDescription("Mechanical keyboard");
////        product2.setProductPrice(129.99);
////        product2.setProductQuantity(15.0);
////        product2.setImageUrl("http://example.com/keyboard.jpg");
////        product2.setSeller(testSeller);
////        productRepository.save(product2);
////
////        List<Product> gamingProducts = productRepository.findByProductNameContainingIgnoreCase("Gaming");
////
////        assertThat(gamingProducts).hasSize(2);
////        assertThat(gamingProducts).extracting(Product::getProductName)
////                .containsExactlyInAnyOrder("Gaming Mouse", "Gaming Keyboard");
////    }
////
////    @Test
////    @DisplayName("Find products by name with seller test")
////    public void testFindProductsByNameWithSeller() throws ProductException {
////        // Add products with different names
////        Product product1 = new Product();
////        product1.setProductName("Smartwatch");
////        product1.setProductDescription("Fitness tracker");
////        product1.setProductPrice(199.99);
////        product1.setProductQuantity(15.0);
////        product1.setImageUrl("http://example.com/smartwatch.jpg");
////        product1.setSeller(testSeller);
////        productRepository.save(product1);
////
////        List<Product> products = productRepository.findProductsByNameWithSeller("Smart");
////
////        assertThat(products).hasSize(1);
////        assertThat(products.get(0).getProductName()).isEqualTo("Smartwatch");
////        assertThat(products.get(0).getSeller()).isNotNull();
////        assertThat(products.get(0).getSeller().getSellerFirstName()).isEqualTo("John");
////    }
////
////    @Test
////    @DisplayName("Update product test")
////    public void testUpdateProduct() {
////        Product productToUpdate = productRepository.findById(testProduct.getProductId()).orElse(null);
////        assertThat(productToUpdate).isNotNull();
////
////        productToUpdate.setProductName("Updated Laptop");
////        productToUpdate.setProductPrice(1699.99);
////
////        Product updatedProduct = productRepository.save(productToUpdate);
////
////        assertThat(updatedProduct.getProductName()).isEqualTo("Updated Laptop");
////        assertThat(updatedProduct.getProductPrice()).isEqualTo(1699.99);
////        assertThat(updatedProduct.getProductId()).isEqualTo(testProduct.getProductId());
////    }
//}
