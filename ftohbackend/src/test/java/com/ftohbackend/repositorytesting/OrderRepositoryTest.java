//package com.ftohbackend.repositorytesting;
//
//import static org.assertj.core.api.Assertions.assertThat;
//
//import java.util.List;
//
//import org.junit.jupiter.api.AfterEach;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
//import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
//
//import com.ftohbackend.model.Customer;
//import com.ftohbackend.model.Order;
//import com.ftohbackend.model.Product;
//import com.ftohbackend.model.Seller;
//import com.ftohbackend.repository.CustomerRepository;
//import com.ftohbackend.repository.OrderRepository;
//import com.ftohbackend.repository.ProductRepository;
//import com.ftohbackend.repository.SellerRepository;
//
//@DataJpaTest
//@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
//public class OrderRepositoryTest 
//{
//
//    @Autowired
//    private OrderRepository orderRepository;
//
//    @Autowired
//    private CustomerRepository customerRepository;
//
//    @Autowired
//    private ProductRepository productRepository;
//
//    @Autowired
//    private SellerRepository sellerRepository;
//
//    private Customer testCustomer;
//    private Product testProduct;
//    private Seller testSeller;
//
//    @BeforeEach
//    public void setUp() {
//        testCustomer = new Customer();
//        testCustomer.setCustomerFirstName("Test");
//        testCustomer.setCustomerLastName("User");
//        testCustomer.setCustomerEmail("test.user@example.com");
//        testCustomer.setCustomerPassword("password123");
//        testCustomer.setCustomerPhoneNumber("9876543210");
//        testCustomer.setCustomerCity("City");
//        testCustomer.setCustomerPlace("Place");
//        testCustomer.setCustomerPincode("123456");
//        testCustomer.setCustomerState("State");
//        testCustomer.setCustomerIsActive(true);
//        customerRepository.save(testCustomer);
//
//        testSeller = new Seller();
//        testSeller.setSellerFirstName("John");
//        testSeller.setSellerLastName("Doe");
//        testSeller.setSellerEmail("john.doe@example.com");
//        testSeller.setSellerPassword("securepass");
//        testSeller.setSellerMobileNumber("9876543210");
//        testSeller.setSellerCity("City");
//        testSeller.setSellerPlace("Place");
//        testSeller.setSellerState("State");
//        testSeller.setSellerPincode("123456");
//        testSeller.setSellerStatus("Active");
//        sellerRepository.save(testSeller);
//
//        testProduct = new Product();
//        testProduct.setProductName("Laptop");
//        testProduct.setProductDescription("Gaming laptop");
//        testProduct.setProductPrice(1500.0);
//        testProduct.setProductQuantity(10.0);
//        testProduct.setImageUrl("http://example.com/laptop.jpg");
//        testProduct.setSeller(testSeller);
//        productRepository.save(testProduct);
//    }
//
//    @AfterEach
//    public void tearDown() {
//        orderRepository.deleteAll();
//        productRepository.deleteAll();
//        customerRepository.deleteAll();
//        sellerRepository.deleteAll();
//    }
//
//    @Test
//    @DisplayName("Save order test")
//    public void testSaveOrder() {
//        Order order = new Order();
//        order.setProduct(testProduct);
//        order.setCustomer(testCustomer);
//        order.setOrderQuantity(2.0);
//        order.setOrderStatus("Incart");
//
//        Order savedOrder = orderRepository.save(order);
//
//        assertThat(savedOrder).isNotNull();
//        assertThat(savedOrder.getOrderId()).isNotNull();
//        assertThat(savedOrder.getOrderQuantity()).isEqualTo(2.0);
//        assertThat(savedOrder.getOrderStatus()).isEqualTo("Incart");
//    }
//
//    @Test
//    @DisplayName("Find order by ID test")
//    public void testFindById() {
//        Order order = new Order(null, testProduct, testCustomer, "Ordered", 1.0);
//        Order saved = orderRepository.save(order);
//
//        Order found = orderRepository.findById(saved.getOrderId()).orElse(null);
//
//        assertThat(found).isNotNull();
//        assertThat(found.getOrderStatus()).isEqualTo("Ordered");
//    }
//
//    @Test
//    @DisplayName("Find orders by customer ID test")
//    public void testFindByCustomerCustomerId() {
//        Order order1 = new Order(null, testProduct, testCustomer, "Incart", 1.0);
//        Order order2 = new Order(null, testProduct, testCustomer, "Ordered", 3.0);
//        orderRepository.save(order1);
//        orderRepository.save(order2);
//
//        List<Order> orders = orderRepository.findByCustomerCustomerId(testCustomer.getCustomerId());
//
//        assertThat(orders).hasSize(2);
//        assertThat(orders.get(0).getCustomer().getCustomerId()).isEqualTo(testCustomer.getCustomerId());
//    }
//
//    @Test
//    @DisplayName("Delete order test")
//    public void testDeleteOrder() {
//        Order order = new Order(null, testProduct,testCustomer,"Deleted", 1.0);
//        Order saved = orderRepository.save(order);
//
//        orderRepository.deleteById(saved.getOrderId());
//
//        assertThat(orderRepository.findById(saved.getOrderId())).isEmpty();
//    }
//
//    @Test
//    @DisplayName("Find all orders test")
//    public void testFindAllOrders() {
//        Order order1 = new Order(null, testProduct, testCustomer, "Ordered", 2.0);
//        Order order2 = new Order(null, testProduct, testCustomer,  "Success",5.0);
//        orderRepository.save(order1);
//        orderRepository.save(order2);
//
//        List<Order> allOrders = orderRepository.findAll();
//
//        assertThat(allOrders).isNotNull();
//        assertThat(allOrders.size()).isEqualTo(2);
//    }
//}
