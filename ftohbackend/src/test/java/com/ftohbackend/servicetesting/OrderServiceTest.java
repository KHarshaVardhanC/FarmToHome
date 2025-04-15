package com.ftohbackend.servicetesting;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.ftohbackend.dto.CustomerOrderDTO;
import com.ftohbackend.exception.OrderException;
import com.ftohbackend.model.Customer;
import com.ftohbackend.model.Order;
import com.ftohbackend.model.Product;
import com.ftohbackend.model.Seller;
import com.ftohbackend.repository.CustomerRepository;
import com.ftohbackend.repository.OrderRepository;
import com.ftohbackend.repository.SellerRepository;
import com.ftohbackend.service.OrderServiceImpl;
import com.ftohbackend.service.ProductService;

@ExtendWith(MockitoExtension.class)
public class OrderServiceTest {

    @Mock
    private OrderRepository orderRepository;
    
    @Mock
    private CustomerRepository customerRepository;
    
    @Mock
    private SellerRepository sellerRepository;
    
    @Mock
    private ProductService productService;
    
    @InjectMocks
    private OrderServiceImpl orderService;

    private Order order;
    private Customer customer;
    private Product product;
    private Seller seller;

    @BeforeEach
    public void setUp() {
        // Setup customer based on CustomerDTO fields
        customer = new Customer();
        customer.setCustomerId(1);
        customer.setCustomerFirstName("John");
        customer.setCustomerLastName("Doe");
        customer.setCustomerEmail("john.doe@example.com");
        customer.setCustomerPassword("password1234");
        customer.setCustomerPlace("Downtown");
        customer.setCustomerCity("New York");
        customer.setCustomerPincode("100001");
        customer.setCustomerState("New York");
        customer.setCustomerPhoneNumber("9876543210");
        customer.setCustomerIsActive(true);
        
        // Setup seller based on SellerDTO fields
        seller = new Seller();
        // seller.setSellerId(1);  // Commented line
        seller.setSellerEmail("seller@example.com");
        seller.setSellerFirstName("Jane");
        seller.setSellerLastName("Smith");
        // seller.setSellerDOB(new Date(90, 1, 15)); // Feb 15, 1990  // Commented line
        seller.setSellerMobileNumber("9876543210");
        seller.setSellerPlace("Uptown");
        seller.setSellerCity("Los Angeles");
        seller.setSellerState("California");
        seller.setSellerPincode("900001");
        seller.setSellerPassword("seller123");
        seller.setSellerStatus("Active");
        
        // Setup product based on ProductDTO fields
        product = new Product();
        product.setProductId(1);
        product.setSeller(seller);
        product.setProductPrice(100.0);
        product.setProductName("Test Product");
        product.setProductQuantity(10.0);
        product.setImageUrl("http://example.com/image.jpg");
        product.setProductDescription("A test product description");
        
        // Setup order based on OrderDTO fields
        order = new Order();
        order.setOrderId(1);
        order.setProduct(product);
        order.setOrderQuantity(2.0); // Assuming orderQuantity maps to quantity
        order.setCustomer(customer);
        order.setOrderStatus("Ordered"); // Using the orderStatus from DTO
        // order.setTotalPrice(200.0); // This might be calculated in your service // Commented line
    }

    @AfterEach
    public void tearDown() {
        order = null;
        customer = null;
        product = null;
        seller = null;
    }

    @Test
    @DisplayName("JUnit test for getAllOrders operation")
    public void givenOrdersList_whenGetAllOrders_thenReturnOrdersList() throws OrderException {
        // given - precondition or setup
        Order order2 = new Order();
        order2.setOrderId(2);
        order2.setCustomer(customer);
        order2.setProduct(product);
        order2.setOrderQuantity(1.0);
        order2.setOrderStatus("In cart");
        // order2.setTotalPrice(100.0); // Commented line
        
        when(orderRepository.findAll()).thenReturn(List.of(order, order2));
        
        // when - action or the behaviour
        List<Order> orders = orderService.getAllOrders();
        
        // then - verify the output
        assertThat(orders).isNotNull();
        assertThat(orders.size()).isEqualTo(2);
    }

    @Test
    @DisplayName("JUnit test for getAllOrders operation - throw OrderException when no orders found")
    public void givenEmptyOrdersList_whenGetAllOrders_thenThrowOrderException() {
        // given - precondition or setup
        when(orderRepository.findAll()).thenReturn(Collections.emptyList());
        
        // when/then - action and verify the output
        assertThrows(OrderException.class, () -> {
            orderService.getAllOrders();
        });
    }
    
    @Test
    @DisplayName("JUnit test for getOrderById operation")
    public void givenOrderId_whenGetOrderById_thenReturnOrderObject() throws OrderException {
        // given - precondition or setup
        when(orderRepository.findById(order.getOrderId())).thenReturn(Optional.of(order));
        
        // when - action or the behaviour
        Order foundOrder = orderService.getOrderById(order.getOrderId());
        
        // then - verify the output
        assertThat(foundOrder).isNotNull();
        assertThat(foundOrder.getOrderId()).isEqualTo(order.getOrderId());
        assertThat(foundOrder.getOrderStatus()).isEqualTo("Ordered");
    }
    
    @Test
    @DisplayName("JUnit test for getOrderById operation - throw OrderException when ID is null")
    public void givenNullOrderId_whenGetOrderById_thenThrowOrderException() {
        // when/then - action and verify the output
        assertThrows(OrderException.class, () -> {
            orderService.getOrderById(null);
        });
    }
    
    @Test
    @DisplayName("JUnit test for getOrderById operation - throw OrderException when order not found")
    public void givenOrderId_whenGetOrderById_thenThrowOrderException() {
        // given - precondition or setup
        when(orderRepository.findById(anyInt())).thenReturn(Optional.empty());
        
        // when/then - action and verify the output
        assertThrows(OrderException.class, () -> {
            orderService.getOrderById(1);
        });
    }
    
    @Test
    @DisplayName("JUnit test for getOrderByCustomerId operation")
    public void givenCustomerId_whenGetOrderByCustomerId_thenReturnOrdersList() throws OrderException {
        // given - precondition or setup
        when(customerRepository.findById(customer.getCustomerId())).thenReturn(Optional.of(customer));
        when(orderRepository.findByCustomerCustomerId(customer.getCustomerId())).thenReturn(List.of(order));
        
        // when - action or the behaviour
        List<Order> orders = orderService.getOrderByCustomerId(customer.getCustomerId());
        
        // then - verify the output
        assertThat(orders).isNotNull();
        assertThat(orders.size()).isEqualTo(1);
        assertThat(orders.get(0).getCustomer().getCustomerId()).isEqualTo(customer.getCustomerId());
        assertThat(orders.get(0).getOrderStatus()).isEqualTo("Ordered");
    }
    
    @Test
    @DisplayName("JUnit test for getOrderByCustomerId operation - throw OrderException when customer ID is null")
    public void givenNullCustomerId_whenGetOrderByCustomerId_thenThrowOrderException() {
        // when/then - action and verify the output
        assertThrows(OrderException.class, () -> {
            orderService.getOrderByCustomerId(null);
        });
    }
    
    @Test
    @DisplayName("JUnit test for getOrderByCustomerId operation - throw OrderException when customer not found")
    public void givenCustomerId_whenGetOrderByCustomerId_thenThrowOrderExceptionForCustomerNotFound() {
        // given - precondition or setup
        when(customerRepository.findById(anyInt())).thenReturn(Optional.empty());
        
        // when/then - action and verify the output
        assertThrows(OrderException.class, () -> {
            orderService.getOrderByCustomerId(1);
        });
    }
    
    @Test
    @DisplayName("JUnit test for getOrderByCustomerId operation - throw OrderException when no orders found for customer")
    public void givenCustomerId_whenGetOrderByCustomerId_thenThrowOrderExceptionForNoOrders() {
        // given - precondition or setup
        when(customerRepository.findById(customer.getCustomerId())).thenReturn(Optional.of(customer));
        when(orderRepository.findByCustomerCustomerId(customer.getCustomerId())).thenReturn(Collections.emptyList());
        
        // when/then - action and verify the output
        assertThrows(OrderException.class, () -> {
            orderService.getOrderByCustomerId(customer.getCustomerId());
        });
    }
    
    @Test
    @DisplayName("JUnit test for addOrder operation")
    public void givenOrderObject_whenAddOrder_thenReturnSuccessMessage() throws Exception {
        // given - precondition or setup
        when(productService.getProduct(anyInt())).thenReturn(product);
        when(orderRepository.save(order)).thenReturn(order);
        
        // when - action or the behaviour
        String result = orderService.addOrder(order);
        
        // then - verify the output
        assertThat(result).isEqualTo("Order Successful");
        verify(orderRepository, times(1)).save(order);
    }
    
    @Test
    @DisplayName("JUnit test for addOrder operation - throw OrderException when order is null")
    public void givenNullOrderObject_whenAddOrder_thenThrowOrderException() {
        // when/then - action and verify the output
        assertThrows(OrderException.class, () -> {
            orderService.addOrder(null);
        });
        
        verify(orderRepository, never()).save(any(Order.class));
    }
    
    @Test
    @DisplayName("JUnit test for addOrder operation - throw OrderException when customer is null")
    public void givenOrderObjectWithNullCustomer_whenAddOrder_thenThrowOrderException() {
        // given - precondition or setup
        order.setCustomer(null);
        
        // when/then - action and verify the output
        assertThrows(OrderException.class, () -> {
            orderService.addOrder(order);
        });
        
        verify(orderRepository, never()).save(any(Order.class));
    }
    
    @Test
    @DisplayName("JUnit test for addOrder operation - throw OrderException when product is null")
    public void givenOrderObjectWithNullProduct_whenAddOrder_thenThrowOrderException() {
        // given - precondition or setup
        order.setProduct(null);
        
        // when/then - action and verify the output
        assertThrows(OrderException.class, () -> {
            orderService.addOrder(order);
        });
        
        verify(orderRepository, never()).save(any(Order.class));
    }
    
    @Test
    @DisplayName("JUnit test for getOrdersBySellerId operation")
    public void givenSellerId_whenGetOrdersBySellerId_thenReturnOrdersList() throws OrderException {
        // given - precondition or setup
        List<Order> orderList = new ArrayList<>();
        orderList.add(order);
        
        when(orderRepository.findAll()).thenReturn(orderList);
        
        // when - action or the behaviour
        List<Order> orders = orderService.getOrdersBySellerId(seller.getSellerId());
        
        // then - verify the output
        assertThat(orders).isNotNull();
        assertThat(orders.size()).isEqualTo(1);
        assertThat(orders.get(0).getProduct().getSeller().getSellerId()).isEqualTo(seller.getSellerId());
    }
    
    @Test
    @DisplayName("JUnit test for getOrdersBySellerId operation - throw OrderException when seller ID is null")
    public void givenNullSellerId_whenGetOrdersBySellerId_thenThrowOrderException() {
        // when/then - action and verify the output
        assertThrows(OrderException.class, () -> {
            orderService.getOrdersBySellerId(null);
        });
    }
    
    @Test
    @DisplayName("JUnit test for getOrdersBySellerId operation - throw OrderException when no orders found for seller")
    public void givenSellerId_whenGetOrdersBySellerId_thenThrowOrderExceptionForNoOrders() {
        // given - precondition or setup
        when(orderRepository.findAll()).thenReturn(Collections.emptyList());
        
        // when/then - action and verify the output
        assertThrows(OrderException.class, () -> {
            orderService.getOrdersBySellerId(seller.getSellerId());
        });
    }
    
    @Test
    @DisplayName("JUnit test for deleteOrder operation")
    public void givenOrderId_whenDeleteOrder_thenReturnSuccessMessage() throws OrderException {
        // given - precondition or setup
        when(orderRepository.existsById(order.getOrderId())).thenReturn(true);
        doNothing().when(orderRepository).deleteById(order.getOrderId());
        
        // when - action or the behaviour
        String result = orderService.deleteOrder(order.getOrderId());
        
        // then - verify the output
        assertThat(result).isEqualTo("Order Deletion Successful");
        verify(orderRepository, times(1)).deleteById(order.getOrderId());
    }
    
    @Test
    @DisplayName("JUnit test for deleteOrder operation - throw OrderException when order not found")
    public void givenOrderId_whenDeleteOrder_thenThrowOrderException() {
        // given - precondition or setup
        when(orderRepository.existsById(anyInt())).thenReturn(false);
        
        // when/then - action and verify the output
        assertThrows(OrderException.class, () -> {
            orderService.deleteOrder(1);
        });
        
        verify(orderRepository, never()).deleteById(anyInt());
    }
    
    @Test
    @DisplayName("JUnit test for updateOrderStatus - ordered status with sufficient quantity")
    public void givenOrderIdAndOrderedStatus_whenUpdateOrderStatus_thenReturnSuccessMessage() throws Exception {
        // given
        when(orderRepository.findById(order.getOrderId())).thenReturn(Optional.of(order));
        when(productService.updateProduct(anyInt(), any(Product.class))).thenReturn("Product updated successfully");        
        // when
        String result = orderService.updateOrderStatus(order.getOrderId(), "ordered");
        
        // then
        assertThat(result).contains("Order Status updated Succesfully");
        verify(orderRepository, times(1)).save(order);
        verify(productService, times(1)).updateProduct(anyInt(), any(Product.class));
    }

    @Test
    @DisplayName("JUnit test for updateOrderStatus - ordered status with insufficient quantity")
    public void givenOrderIdAndOrderedStatusWithInsufficientQuantity_whenUpdateOrderStatus_thenReturnFailureMessage() throws Exception {
        // given
        order.setOrderQuantity(20.0); // More than product quantity (10.0)
        when(orderRepository.findById(order.getOrderId())).thenReturn(Optional.of(order));
        
        // when
        String result = orderService.updateOrderStatus(order.getOrderId(), "ordered");
        
        // then
        assertThat(result).contains("Quantity Exceeded");
        assertThat(order.getOrderStatus()).isEqualTo("failed");
        verify(orderRepository, times(1)).save(order);
        verify(productService, never()).updateProduct(anyInt(), any(Product.class));
    }

    @Test
    @DisplayName("JUnit test for updateOrderStatus - delivered status")
    public void givenOrderIdAndDeliveredStatus_whenUpdateOrderStatus_thenReturnSuccessMessage() throws Exception {
        // given
        when(orderRepository.findById(order.getOrderId())).thenReturn(Optional.of(order));
        
        // when
        String result = orderService.updateOrderStatus(order.getOrderId(), "delivered");
        
        // then
        assertThat(result).isEqualTo("Ordered Delivered Successfully");
        assertThat(order.getOrderStatus()).isEqualTo("delivered");
        verify(orderRepository, times(1)).save(order);
    }

    @Test
    @DisplayName("JUnit test for updateOrderStatus - invalid status")
    public void givenOrderIdAndInvalidStatus_whenUpdateOrderStatus_thenThrowOrderException() {
        // given
        when(orderRepository.findById(order.getOrderId())).thenReturn(Optional.of(order));
        
        // when/then
        assertThrows(OrderException.class, () -> {
            orderService.updateOrderStatus(order.getOrderId(), "invalid_status");
        });
    }

    @Test
    @DisplayName("JUnit test for updateOrderStatus - null order ID")
    public void givenNullOrderId_whenUpdateOrderStatus_thenThrowOrderException() {
        // when/then
        assertThrows(OrderException.class, () -> {
            orderService.updateOrderStatus(null, "ordered");
        });
    }

    @Test
    @DisplayName("JUnit test for updateOrderStatus - order not found")
    public void givenNonExistingOrderId_whenUpdateOrderStatus_thenThrowOrderException() {
        // given
        when(orderRepository.findById(anyInt())).thenReturn(Optional.empty());
        
        // when/then
        assertThrows(OrderException.class, () -> {
            orderService.updateOrderStatus(999, "ordered");
        });
    }
    @Test
    @DisplayName("JUnit test for getOrderInvoice operation")
    public void givenOrderId_whenGetOrderInvoice_thenReturnCustomerOrderDTO() throws Exception {
        // given
        when(orderRepository.findById(order.getOrderId())).thenReturn(Optional.of(order));
        
        // when
        CustomerOrderDTO result = orderService.getOrderInvoice(order.getOrderId());
        
        // then
        assertThat(result).isNotNull();
        assertThat(result.getOrderId()).isEqualTo(order.getOrderId());
        assertThat(result.getOrderQuantity()).isEqualTo(order.getOrderQuantity());
        assertThat(result.getOrderStatus()).isEqualTo(order.getOrderStatus());
        assertThat(result.getProductName()).isEqualTo(product.getProductName());
        assertThat(result.getCustomerName()).isEqualTo(customer.getCustomerFirstName() + " " + customer.getCustomerLastName());
        assertThat(result.getSellerName()).isEqualTo(seller.getSellerFirstName() + " " + seller.getSellerLastName());
    }
    @Test
    @DisplayName("JUnit test for addOrder operation - insufficient quantity")
    public void givenOrderWithInsufficientQuantity_whenAddOrder_thenReturnUnsuccessfulMessage() throws Exception {
        // given
        order.setOrderQuantity(15.0); // More than product quantity (10.0)
        when(productService.getProduct(anyInt())).thenReturn(product);
        
        // when
        String result = orderService.addOrder(order);
        
        // then
        assertThat(result).isEqualTo("Order Unsuccessful and Not had Sufficient Quantity");
        verify(orderRepository, never()).save(any(Order.class));
    }
}