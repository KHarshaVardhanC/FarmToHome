package com.ftohbackend.servicetesting;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.ftohbackend.dto.CustomerOrderDTO;
import com.ftohbackend.exception.OrderException;
import com.ftohbackend.exception.ProductException;
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

    private Order testOrder;
    private Customer testCustomer;
    private Product testProduct;
    private Seller testSeller;
    private List<Order> orderList;

    @BeforeEach
    void setUp() {
        // Setup Customer
        testCustomer = new Customer();
        testCustomer.setCustomerId(1);
        testCustomer.setCustomerFirstName("John");
        testCustomer.setCustomerLastName("Doe");
        testCustomer.setCustomerEmail("john.doe@example.com");
        testCustomer.setCustomerPlace("Downtown");
        testCustomer.setCustomerCity("Metropolis");
        testCustomer.setCustomerState("State");
        testCustomer.setCustomerPincode("123456");
        testCustomer.setCustomerPhoneNumber("9876543210");

        // Setup Seller
        testSeller = new Seller();
        testSeller.setSellerId(1);
        testSeller.setSellerFirstName("Jane");
        testSeller.setSellerLastName("Smith");
        testSeller.setSellerEmail("jane.smith@example.com");
        testSeller.setSellerPlace("Uptown");
        testSeller.setSellerCity("Metropolis");
        testSeller.setSellerState("State");
        testSeller.setSellerPincode("654321");
        testSeller.setSellerMobileNumber("1234567890");

        // Setup Product
        testProduct = new Product();
        testProduct.setProductId(1);
        testProduct.setProductName("Test Product");
        testProduct.setProductDescription("Test Description");
        testProduct.setProductPrice(100.0);
        testProduct.setProductQuantity(50.0);
        testProduct.setProductQuantityType("kg");
        testProduct.setImageUrl("http://example.com/image.jpg");
        testProduct.setSeller(testSeller);

        // Setup Order
        testOrder = new Order();
        testOrder.setOrderId(1);
        testOrder.setCustomer(testCustomer);
        testOrder.setProduct(testProduct);
        testOrder.setOrderQuantity(10.0);
        testOrder.setOrderStatus("Incart");

        // Setup Order List
        orderList = new ArrayList<>();
        orderList.add(testOrder);

        Order order2 = new Order();
        order2.setOrderId(2);
        order2.setCustomer(testCustomer);
        order2.setProduct(testProduct);
        order2.setOrderQuantity(5.0);
        order2.setOrderStatus("Ordered");
        orderList.add(order2);
    }

    @Test
    void testGetAllOrders_Success() throws OrderException {
        when(orderRepository.findAll()).thenReturn(orderList);

        List<Order> result = orderService.getAllOrders();

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(testOrder.getOrderId(), result.get(0).getOrderId());
        verify(orderRepository, times(1)).findAll();
    }

    @Test
    void testGetAllOrders_EmptyList() {
        when(orderRepository.findAll()).thenReturn(new ArrayList<>());

        Exception exception = assertThrows(OrderException.class, () -> {
            orderService.getAllOrders();
        });

        assertEquals("No orders found", exception.getMessage());
        verify(orderRepository, times(1)).findAll();
    }

    @Test
    void testGetOrderById_Success() throws OrderException {
        when(orderRepository.findById(anyInt())).thenReturn(Optional.of(testOrder));

        Order result = orderService.getOrderById(1);

        assertNotNull(result);
        assertEquals(testOrder.getOrderId(), result.getOrderId());
        verify(orderRepository, times(1)).findById(1);
    }

    @Test
    void testGetOrderById_NullId() {
        Exception exception = assertThrows(OrderException.class, () -> {
            orderService.getOrderById(null);
        });

        assertEquals("Order ID cannot be null", exception.getMessage());
        verify(orderRepository, never()).findById(anyInt());
    }

    @Test
    void testGetOrderById_NotFound() {
        when(orderRepository.findById(anyInt())).thenReturn(Optional.empty());

        Exception exception = assertThrows(OrderException.class, () -> {
            orderService.getOrderById(999);
        });

        assertEquals("Order not found with ID: 999", exception.getMessage());
        verify(orderRepository, times(1)).findById(999);
    }

    @Test
    void testGetOrderByCustomerId_Success() throws OrderException {
        when(customerRepository.findById(anyInt())).thenReturn(Optional.of(testCustomer));
        when(orderRepository.findByCustomerCustomerId(anyInt())).thenReturn(orderList);

        List<Order> result = orderService.getOrderByCustomerId(1);

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(testOrder.getOrderId(), result.get(0).getOrderId());
        verify(customerRepository, times(1)).findById(1);
        verify(orderRepository, times(1)).findByCustomerCustomerId(1);
    }

    @Test
    void testGetOrderByCustomerId_NullId() {
        Exception exception = assertThrows(OrderException.class, () -> {
            orderService.getOrderByCustomerId(null);
        });

        assertEquals("Customer ID cannot be null", exception.getMessage());
        verify(customerRepository, never()).findById(anyInt());
        verify(orderRepository, never()).findByCustomerCustomerId(anyInt());
    }

    @Test
    void testGetOrderByCustomerId_CustomerNotFound() {
        when(customerRepository.findById(anyInt())).thenReturn(Optional.empty());

        Exception exception = assertThrows(OrderException.class, () -> {
            orderService.getOrderByCustomerId(999);
        });

        assertEquals("Customer not found with ID: 999", exception.getMessage());
        verify(customerRepository, times(1)).findById(999);
        verify(orderRepository, never()).findByCustomerCustomerId(anyInt());
    }

    @Test
    void testGetOrderByCustomerId_NoOrders() {
        when(customerRepository.findById(anyInt())).thenReturn(Optional.of(testCustomer));
        when(orderRepository.findByCustomerCustomerId(anyInt())).thenReturn(new ArrayList<>());

        Exception exception = assertThrows(OrderException.class, () -> {
            orderService.getOrderByCustomerId(1);
        });

        assertEquals("No orders found for customer ID: 1", exception.getMessage());
        verify(customerRepository, times(1)).findById(1);
        verify(orderRepository, times(1)).findByCustomerCustomerId(1);
    }

    @Test
    void testAddOrder_Success() throws Exception {
        when(productService.getProduct(anyInt())).thenReturn(testProduct);
        when(orderRepository.save(any(Order.class))).thenReturn(testOrder);

        String result = orderService.addOrder(testOrder);

        assertEquals("Order Successful", result);
        verify(productService, times(1)).getProduct(testProduct.getProductId());
        verify(productService, times(1)).updateProduct(eq(testProduct.getProductId()), any(Product.class));
        verify(orderRepository, times(1)).save(testOrder);
    }

    @Test
    void testAddOrder_InsufficientQuantity() throws Exception {
        // Setup order with quantity greater than product quantity
        testOrder.setOrderQuantity(100.0);
        
        when(productService.getProduct(anyInt())).thenReturn(testProduct);

        String result = orderService.addOrder(testOrder);

        assertEquals("Order Unsuccessful and Not had Sufficient Quantity", result);
        verify(productService, times(1)).getProduct(testProduct.getProductId());
        verify(productService, never()).updateProduct(anyInt(), any(Product.class));
        verify(orderRepository, never()).save(any(Order.class));
    }

    @Test
    void testAddOrder_NullOrder() throws ProductException {
        Exception exception = assertThrows(OrderException.class, () -> {
            orderService.addOrder(null);
        });

        assertEquals("Order, customer, or product details cannot be null", exception.getMessage());
        verify(productService, never()).getProduct(anyInt());
        verify(orderRepository, never()).save(any(Order.class));
    }

    @Test
    void testAddOrder_NullCustomer() throws ProductException {
        testOrder.setCustomer(null);
        
        Exception exception = assertThrows(OrderException.class, () -> {
            orderService.addOrder(testOrder);
        });

        assertEquals("Order, customer, or product details cannot be null", exception.getMessage());
        verify(productService, never()).getProduct(anyInt());
        verify(orderRepository, never()).save(any(Order.class));
    }

    @Test
    void testAddOrder_NullProduct() throws ProductException {
        testOrder.setProduct(null);
        
        Exception exception = assertThrows(OrderException.class, () -> {
            orderService.addOrder(testOrder);
        });

        assertEquals("Order, customer, or product details cannot be null", exception.getMessage());
        verify(productService, never()).getProduct(anyInt());
        verify(orderRepository, never()).save(any(Order.class));
    }

    @Test
    void testGetOrdersBySellerId_Success() throws OrderException {
        when(orderRepository.findByProductSellerSellerId(anyInt())).thenReturn(orderList);

        List<Order> result = orderService.getOrdersBySellerId(1);

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(testOrder.getOrderId(), result.get(0).getOrderId());
        verify(orderRepository, times(1)).findByProductSellerSellerId(1);
    }

    @Test
    void testGetOrdersBySellerId_NullId() {
        Exception exception = assertThrows(OrderException.class, () -> {
            orderService.getOrdersBySellerId(null);
        });

        assertEquals("Seller ID cannot be null", exception.getMessage());
        verify(orderRepository, never()).findByProductSellerSellerId(anyInt());
    }

    @Test
    void testGetOrdersBySellerId_NoOrders() {
        when(orderRepository.findByProductSellerSellerId(anyInt())).thenReturn(new ArrayList<>());

        Exception exception = assertThrows(OrderException.class, () -> {
            orderService.getOrdersBySellerId(1);
        });

        assertEquals("No orders found for seller ID: 1", exception.getMessage());
        verify(orderRepository, times(1)).findByProductSellerSellerId(1);
    }

    @Test
    void testDeleteOrder_Success() throws OrderException {
        when(orderRepository.existsById(anyInt())).thenReturn(true);
        doNothing().when(orderRepository).deleteById(anyInt());

        String result = orderService.deleteOrder(1);

        assertEquals("Order Deletion Successful", result);
        verify(orderRepository, times(1)).existsById(1);
        verify(orderRepository, times(1)).deleteById(1);
    }

    @Test
    void testDeleteOrder_NotFound() {
        when(orderRepository.existsById(anyInt())).thenReturn(false);

        Exception exception = assertThrows(OrderException.class, () -> {
            orderService.deleteOrder(999);
        });

        assertEquals("Order not found with ID: 999", exception.getMessage());
        verify(orderRepository, times(1)).existsById(999);
        verify(orderRepository, never()).deleteById(anyInt());
    }

    @Test
    void testUpdateOrderStatus_OrderedSuccess() throws Exception {
        when(orderRepository.findById(anyInt())).thenReturn(Optional.of(testOrder));
        when(orderRepository.save(any(Order.class))).thenReturn(testOrder);

        String result = orderService.updateOrderStatus(1, "Ordered");

        assertEquals("Order Status updated Succesfully Ordered", result);
        assertEquals("Ordered", testOrder.getOrderStatus());
        verify(orderRepository, times(1)).findById(1);
        verify(productService, times(1)).updateProduct(eq(testProduct.getProductId()), any(Product.class));
        verify(orderRepository, times(1)).save(testOrder);
    }

    @Test
    void testUpdateOrderStatus_OrderedInsufficientQuantity() throws Exception {
        // Setup order with quantity greater than product quantity
        testOrder.setOrderQuantity(100.0);
        
        when(orderRepository.findById(anyInt())).thenReturn(Optional.of(testOrder));
        when(orderRepository.save(any(Order.class))).thenReturn(testOrder);

        String result = orderService.updateOrderStatus(1, "Ordered");

        assertEquals("Quantity Exceeded! \n  Order failed \n try Again", result);
        assertEquals("failed", testOrder.getOrderStatus());
        verify(orderRepository, times(1)).findById(1);
        verify(productService, never()).updateProduct(anyInt(), any(Product.class));
        verify(orderRepository, times(1)).save(testOrder);
    }

    @Test
    void testUpdateOrderStatus_DeliveredSuccess() throws Exception {
        when(orderRepository.findById(anyInt())).thenReturn(Optional.of(testOrder));
        when(orderRepository.save(any(Order.class))).thenReturn(testOrder);

        String result = orderService.updateOrderStatus(1, "Delivered");

        assertEquals("Ordered Delivered Successfully", result);
        assertEquals("Delivered", testOrder.getOrderStatus());
        verify(orderRepository, times(1)).findById(1);
        verify(productService, never()).updateProduct(anyInt(), any(Product.class));
        verify(orderRepository, times(1)).save(testOrder);
    }

    @Test
    void testUpdateOrderStatus_NullId() {
        Exception exception = assertThrows(OrderException.class, () -> {
            orderService.updateOrderStatus(null, "Ordered");
        });

        assertEquals("Order ID cannot be null", exception.getMessage());
        verify(orderRepository, never()).findById(anyInt());
        verify(orderRepository, never()).save(any(Order.class));
    }

    @Test
    void testUpdateOrderStatus_OrderNotFound() {
        when(orderRepository.findById(anyInt())).thenReturn(Optional.empty());

        Exception exception = assertThrows(OrderException.class, () -> {
            orderService.updateOrderStatus(999, "Ordered");
        });

        assertEquals("Order not found with ID: 999", exception.getMessage());
        verify(orderRepository, times(1)).findById(999);
        verify(orderRepository, never()).save(any(Order.class));
    }

    @Test
    void testUpdateOrderStatus_InvalidStatus() {
        when(orderRepository.findById(anyInt())).thenReturn(Optional.of(testOrder));

        Exception exception = assertThrows(OrderException.class, () -> {
            orderService.updateOrderStatus(1, "InvalidStatus");
        });

        assertEquals("Invalid order status: InvalidStatus", exception.getMessage());
        verify(orderRepository, times(1)).findById(1);
        verify(orderRepository, never()).save(any(Order.class));
    }

    @Test
    void testGetOrderInvoice_Success() throws Exception {
        when(orderRepository.findById(anyInt())).thenReturn(Optional.of(testOrder));

        CustomerOrderDTO result = orderService.getOrderInvoice(1);

        assertNotNull(result);
        assertEquals(testOrder.getOrderId(), result.getOrderId());
        assertEquals(testProduct.getProductName(), result.getProductName());
        assertEquals(testProduct.getProductDescription(), result.getProductDescription());
        assertEquals(testProduct.getProductPrice(), result.getProductPrice());
        assertEquals(testProduct.getProductQuantityType(), result.getProductQuantityType());
        assertEquals(testProduct.getImageUrl(), result.getImageUrl());
        assertEquals(testOrder.getOrderQuantity(), result.getOrderQuantity());
        assertEquals(testOrder.getOrderStatus(), result.getOrderStatus());
        
        assertEquals(testCustomer.getCustomerFirstName() + " " + testCustomer.getCustomerLastName(), result.getCustomerName());
        assertEquals(testCustomer.getCustomerCity(), result.getCustomerCity());
        assertEquals(testCustomer.getCustomerPlace(), result.getCustomerPlace());
        assertEquals(testCustomer.getCustomerPincode(), result.getCustomerPincode());
        assertEquals(testCustomer.getCustomerState(), result.getCustomerState());
        
        assertEquals(testSeller.getSellerFirstName() + " " + testSeller.getSellerLastName(), result.getSellerName());
        assertEquals(testSeller.getSellerCity(), result.getSellerCity());
        assertEquals(testSeller.getSellerPlace(), result.getSellerPlace());
        assertEquals(testSeller.getSellerPincode(), result.getSellerPincode());
        assertEquals(testSeller.getSellerState(), result.getSellerState());
        
        verify(orderRepository, times(1)).findById(1);
    }
}