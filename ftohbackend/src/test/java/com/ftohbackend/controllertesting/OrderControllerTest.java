package com.ftohbackend.controllertesting;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ExceptionHandlerExceptionResolver;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ftohbackend.controller.OrderControllerImpl;
import com.ftohbackend.dto.CustomerOrderDTO;
import com.ftohbackend.dto.OrderDTO;
import com.ftohbackend.dto.SellerOrderDTO;
import com.ftohbackend.exception.OrderException;
import com.ftohbackend.model.Customer;
import com.ftohbackend.model.Order;
import com.ftohbackend.model.Product;
import com.ftohbackend.model.Seller;
import com.ftohbackend.service.CustomerService;
import com.ftohbackend.service.OrderService;
import com.ftohbackend.service.ProductService;
import com.ftohbackend.service.RatingService;

 class OrderControllerTest {

	@ControllerAdvice
    public static class GlobalExceptionHandler {
        @ExceptionHandler(OrderException.class)
        public ResponseEntity<String> handleOrderException(OrderException ex) {
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
        }
        
        @ExceptionHandler(RuntimeException.class)
        public ResponseEntity<String> handleRuntimeException(RuntimeException ex) {
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private MockMvc mockMvc;

    @Mock
    private OrderService orderService;

    @Mock
    private ProductService productService;

    @Mock
    private CustomerService customerService;

    @Mock
    private RatingService ratingService;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private OrderControllerImpl orderController;

    private ObjectMapper objectMapper = new ObjectMapper();
    
    private Order testOrder;
    private Product testProduct;
    private Customer testCustomer;
    private Seller testSeller;
    private OrderDTO testOrderDTO;
    private List<Order> testOrders;
    private List<CustomerOrderDTO> testCustomerOrderDTOs;
    private List<SellerOrderDTO> testSellerOrderDTOs;
    private CustomerOrderDTO testCustomerOrderDTO;
    private SellerOrderDTO testSellerOrderDTO;

    @BeforeEach
     void setup() {
        MockitoAnnotations.openMocks(this);
        
        // Configure MockMvc with the GlobalExceptionHandler
        mockMvc = MockMvcBuilders.standaloneSetup(orderController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .setHandlerExceptionResolvers(new ExceptionHandlerExceptionResolver())
                .build();

        // Initialize test data
        testSeller = new Seller();
        testSeller.setSellerId(1);
        testSeller.setSellerFirstName("John");
        testSeller.setSellerLastName("Doe");
        testSeller.setSellerPlace("Test Place");
        testSeller.setSellerCity("Test City");
        testSeller.setSellerState("Test State");
        testSeller.setSellerPincode("123456");

        testProduct = new Product();
        testProduct.setProductId(1);
        testProduct.setProductName("Test Product");
        testProduct.setProductPrice(100.0);
        testProduct.setProductDescription("Test Description");
        testProduct.setImageUrl("test-image.jpg");
        testProduct.setProductQuantityType("kg");
        testProduct.setSeller(testSeller);

        testCustomer = new Customer();
        testCustomer.setCustomerId(1);
        testCustomer.setCustomerFirstName("Jane");
        testCustomer.setCustomerLastName("Smith");
        testCustomer.setCustomerPlace("Customer Place");
        testCustomer.setCustomerCity("Customer City");
        testCustomer.setCustomerState("Customer State");
        testCustomer.setCustomerPincode("654321");

        testOrder = new Order();
        testOrder.setOrderId(1);
        testOrder.setOrderQuantity(2.0);
        testOrder.setOrderStatus("Ordered");
        testOrder.setProduct(testProduct);
        testOrder.setCustomer(testCustomer);

        testOrders = new ArrayList<>();
        testOrders.add(testOrder);

        testOrderDTO = new OrderDTO();
        testOrderDTO.setOrderId(1);
        testOrderDTO.setProductId(1);
        testOrderDTO.setCustomerId(1);
        testOrderDTO.setOrderQuantity(2.0);
        testOrderDTO.setOrderStatus("Ordered");

        testCustomerOrderDTO = new CustomerOrderDTO();
        testCustomerOrderDTO.setOrderId(1);
        testCustomerOrderDTO.setProductName("Test Product");
        testCustomerOrderDTO.setOrderQuantity(2.0);
        testCustomerOrderDTO.setProductPrice(100.0);
        testCustomerOrderDTO.setImageUrl("test-image.jpg");
        testCustomerOrderDTO.setProductDescription("Test Description");
        testCustomerOrderDTO.setOrderStatus("Ordered");
        testCustomerOrderDTO.setSellerName("John Doe");
        testCustomerOrderDTO.setProductQuantityType("kg");
        testCustomerOrderDTO.setOrderRatingStatus("Not Rated");

        testCustomerOrderDTOs = new ArrayList<>();
        testCustomerOrderDTOs.add(testCustomerOrderDTO);

        testSellerOrderDTO = new SellerOrderDTO();
        testSellerOrderDTO.setOrderId(1);
        testSellerOrderDTO.setProductName("Test Product");
        testSellerOrderDTO.setOrderQuantity(2.0);
        testSellerOrderDTO.setProductPrice(100.0);
        testSellerOrderDTO.setImageUrl("test-image.jpg");
        testSellerOrderDTO.setProductDescription("Test Description");
        testSellerOrderDTO.setOrderStatus("Ordered");
        testSellerOrderDTO.setCustomerName("Jane Smith");
        testSellerOrderDTO.setProductQuantityType("kg");
        
        testSellerOrderDTOs = new ArrayList<>();
        testSellerOrderDTOs.add(testSellerOrderDTO);
    }
    @Test
    @DisplayName("Should get all orders successfully")
     void testGetAllOrders() throws Exception {
        // Arrange
        when(orderService.getAllOrders()).thenReturn(testOrders);

        // Act & Assert
        mockMvc.perform(get("/order")
                .contentType(MediaType.APPLICATION_JSON))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$[0].orderId").value(testOrder.getOrderId()))
               .andExpect(jsonPath("$[0].orderQuantity").value(testOrder.getOrderQuantity()))
               .andExpect(jsonPath("$[0].orderStatus").value(testOrder.getOrderStatus()));
        
        verify(orderService, times(1)).getAllOrders();
    }
    
    @Test
    @DisplayName("Should handle exception when getting all orders")
     void testGetAllOrdersWithException() throws Exception {
        // Arrange
        when(orderService.getAllOrders()).thenThrow(new RuntimeException("Database error"));

        // Act & Assert
        mockMvc.perform(get("/order")
                .contentType(MediaType.APPLICATION_JSON))
               .andExpect(status().isInternalServerError());
        
        verify(orderService, times(1)).getAllOrders();
    }

    @Test
    @DisplayName("Should get order by ID successfully")
     void testGetOrderById() throws Exception {
        // Arrange
        when(orderService.getOrderById(anyInt())).thenReturn(testOrder);

        // Act
        Order result = orderController.getOrderById(1);

        // Assert
        assertEquals(testOrder.getOrderId(), result.getOrderId());
        assertEquals(testOrder.getOrderQuantity(), result.getOrderQuantity());
        assertEquals(testOrder.getOrderStatus(), result.getOrderStatus());
        
        verify(orderService, times(1)).getOrderById(1);
    }
    
    @Test
    @DisplayName("Should handle exception when getting order by ID")
     void testGetOrderByIdWithException() throws Exception {
        // Arrange
        when(orderService.getOrderById(anyInt())).thenThrow(new OrderException("Order not found"));

        // Act & Assert
        Exception exception = assertThrows(OrderException.class, () -> {
            orderController.getOrderById(1);
        });
        
        assertEquals("Order not found", exception.getMessage());
        verify(orderService, times(1)).getOrderById(1);
    }

    @Test
    @DisplayName("Should get cart orders by customer ID successfully")
     void testGetCartOrdersByCustomerId() throws Exception {
        // Arrange
        Order inCartOrder = new Order();
        inCartOrder.setOrderId(1);
        inCartOrder.setOrderQuantity(2.0);
        inCartOrder.setOrderStatus("Incart");
        inCartOrder.setProduct(testProduct);
        inCartOrder.setCustomer(testCustomer);
        
        List<Order> inCartOrders = new ArrayList<>();
        inCartOrders.add(inCartOrder);
        
        when(orderService.getOrderByCustomerId(anyInt())).thenReturn(inCartOrders);
        when(ratingService.getRatingByOrderId(anyInt())).thenReturn(false);

        // Act & Assert
        mockMvc.perform(get("/order/orders/incart/1")
                .contentType(MediaType.APPLICATION_JSON))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$[0].orderId").value(inCartOrder.getOrderId()))
               .andExpect(jsonPath("$[0].orderStatus").value("Incart"))
               .andExpect(jsonPath("$[0].productName").value(testProduct.getProductName()));
        
        verify(orderService, times(1)).getOrderByCustomerId(1);
        verify(ratingService, times(1)).getRatingByOrderId(1);
    }
    
    @Test
    @DisplayName("Should handle empty result when getting cart orders")
     void testGetCartOrdersByCustomerIdWithEmptyResult() throws Exception {
        // Arrange
        List<Order> emptyOrders = new ArrayList<>();
        when(orderService.getOrderByCustomerId(anyInt())).thenReturn(emptyOrders);

        // Act & Assert
        mockMvc.perform(get("/order/orders/incart/1")
                .contentType(MediaType.APPLICATION_JSON))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$").isArray())
               .andExpect(jsonPath("$").isEmpty());
        
        verify(orderService, times(1)).getOrderByCustomerId(1);
    }
    
    @Test
    @DisplayName("Should handle 'In cart' status variation")
     void testGetCartOrdersByCustomerIdWithInCartStatus() throws Exception {
        // Arrange
        Order inCartOrder = new Order();
        inCartOrder.setOrderId(1);
        inCartOrder.setOrderQuantity(2.0);
        inCartOrder.setOrderStatus("In cart"); // Different case
        inCartOrder.setProduct(testProduct);
        inCartOrder.setCustomer(testCustomer);
        
        List<Order> inCartOrders = new ArrayList<>();
        inCartOrders.add(inCartOrder);
        
        when(orderService.getOrderByCustomerId(anyInt())).thenReturn(inCartOrders);
        when(ratingService.getRatingByOrderId(anyInt())).thenReturn(false);

        // Act & Assert
        mockMvc.perform(get("/order/orders/incart/1")
                .contentType(MediaType.APPLICATION_JSON))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$[0].orderId").value(inCartOrder.getOrderId()))
               .andExpect(jsonPath("$[0].orderStatus").value("In cart"));
        
        verify(orderService, times(1)).getOrderByCustomerId(1);
    }

    @Test
    @DisplayName("Should get orders by customer ID successfully")
     void testGetOrdersByCustomerId() throws Exception {
        // Arrange
        Order deliveredOrder = new Order();
        deliveredOrder.setOrderId(1);
        deliveredOrder.setOrderQuantity(2.0);
        deliveredOrder.setOrderStatus("Delivered");
        deliveredOrder.setProduct(testProduct);
        deliveredOrder.setCustomer(testCustomer);
        
        List<Order> orders = new ArrayList<>();
        orders.add(deliveredOrder);
        
        when(orderService.getOrderByCustomerId(anyInt())).thenReturn(orders);
        when(ratingService.getRatingByOrderId(anyInt())).thenReturn(false);

        // Act & Assert
        mockMvc.perform(get("/order/customer/1")
                .contentType(MediaType.APPLICATION_JSON))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$[0].orderId").value(deliveredOrder.getOrderId()))
               .andExpect(jsonPath("$[0].orderStatus").value("Delivered"))
               .andExpect(jsonPath("$[0].orderRatingStatus").value("Not Rated"));
        
        verify(orderService, times(1)).getOrderByCustomerId(1);
        verify(ratingService, times(1)).getRatingByOrderId(1);
    }
    
    @Test
    @DisplayName("Should handle orders with 'Rated' status")
     void testGetOrdersByCustomerIdWithRatedOrder() throws Exception {
        // Arrange
        Order ratedOrder = new Order();
        ratedOrder.setOrderId(1);
        ratedOrder.setOrderQuantity(2.0);
        ratedOrder.setOrderStatus("Delivered");
        ratedOrder.setProduct(testProduct);
        ratedOrder.setCustomer(testCustomer);
        
        List<Order> orders = new ArrayList<>();
        orders.add(ratedOrder);
        
        when(orderService.getOrderByCustomerId(anyInt())).thenReturn(orders);
        when(ratingService.getRatingByOrderId(anyInt())).thenReturn(true);

        // Act & Assert
        mockMvc.perform(get("/order/customer/1")
                .contentType(MediaType.APPLICATION_JSON))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$[0].orderId").value(ratedOrder.getOrderId()))
               .andExpect(jsonPath("$[0].orderRatingStatus").value("Rated"));
        
        verify(orderService, times(1)).getOrderByCustomerId(1);
        verify(ratingService, times(1)).getRatingByOrderId(1);
    }
    
    @Test
    @DisplayName("Should filter out incart orders when getting customer orders")
     void testGetOrdersByCustomerIdFilteringIncartOrders() throws Exception {
        // Arrange
        Order incartOrder = new Order();
        incartOrder.setOrderId(1);
        incartOrder.setOrderStatus("Incart");
        incartOrder.setProduct(testProduct);
        incartOrder.setCustomer(testCustomer);
        
        Order deliveredOrder = new Order();
        deliveredOrder.setOrderId(2);
        deliveredOrder.setOrderStatus("Delivered");
        deliveredOrder.setProduct(testProduct);
        deliveredOrder.setCustomer(testCustomer);
        
        List<Order> mixedOrders = new ArrayList<>();
        mixedOrders.add(incartOrder);
        mixedOrders.add(deliveredOrder);
        
        when(orderService.getOrderByCustomerId(anyInt())).thenReturn(mixedOrders);
        when(ratingService.getRatingByOrderId(anyInt())).thenReturn(false);

        // Act & Assert
        mockMvc.perform(get("/order/customer/1")
                .contentType(MediaType.APPLICATION_JSON))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$[0].orderId").value(deliveredOrder.getOrderId()))
               .andExpect(jsonPath("$[0].orderStatus").value("Delivered"))
               .andExpect(jsonPath("$").isArray())
               .andExpect(jsonPath("$.length()").value(1));
        
        verify(orderService, times(1)).getOrderByCustomerId(1);
    }

    @Test
    @DisplayName("Should update order status successfully")
     void testUpdateOrderStatus() throws Exception {
        // Arrange
        String expectedResponse = "Order status updated successfully";
        when(orderService.updateOrderStatus(anyInt(), anyString())).thenReturn(expectedResponse);

        // Act & Assert
        mockMvc.perform(put("/order/order/1/Delivered")
                .contentType(MediaType.APPLICATION_JSON))
               .andExpect(status().isOk())
               .andExpect(content().string(expectedResponse));
        
        verify(orderService, times(1)).updateOrderStatus(1, "Delivered");
    }
    
    @Test
    @DisplayName("Should handle exception when updating order status")
     void testUpdateOrderStatusWithException() throws Exception {
        // Arrange
        when(orderService.updateOrderStatus(anyInt(), anyString())).thenThrow(new OrderException("Invalid order status"));

        // Act & Assert
        mockMvc.perform(put("/order/order/1/InvalidStatus")
                .contentType(MediaType.APPLICATION_JSON))
               .andExpect(status().isBadRequest());
        
        verify(orderService, times(1)).updateOrderStatus(1, "InvalidStatus");
    }

    @Test
    @DisplayName("Should get orders by seller ID successfully")
     void testGetOrdersBySellerId() throws Exception {
        // Arrange
        Order sellerOrder = new Order();
        sellerOrder.setOrderId(1);
        sellerOrder.setOrderQuantity(2.0);
        sellerOrder.setOrderStatus("Shipped");
        sellerOrder.setProduct(testProduct);
        sellerOrder.setCustomer(testCustomer);
        
        List<Order> orders = new ArrayList<>();
        orders.add(sellerOrder);
        
        when(orderService.getOrdersBySellerId(anyInt())).thenReturn(orders);

        // Act & Assert
        mockMvc.perform(get("/order/seller/1")
                .contentType(MediaType.APPLICATION_JSON))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$[0].orderId").value(sellerOrder.getOrderId()))
               .andExpect(jsonPath("$[0].orderStatus").value("Shipped"))
               .andExpect(jsonPath("$[0].customerName").value("Jane Smith"));
        
        verify(orderService, times(1)).getOrdersBySellerId(1);
    }
    
    @Test
    @DisplayName("Should filter out incart orders when getting seller orders")
     void testGetOrdersBySellerIdFilteringIncartOrders() throws Exception {
        // Arrange
        Order incartOrder = new Order();
        incartOrder.setOrderId(1);
        incartOrder.setOrderStatus("Incart");
        incartOrder.setProduct(testProduct);
        incartOrder.setCustomer(testCustomer);
        
        Order shippedOrder = new Order();
        shippedOrder.setOrderId(2);
        shippedOrder.setOrderStatus("Shipped");
        shippedOrder.setProduct(testProduct);
        shippedOrder.setCustomer(testCustomer);
        
        List<Order> mixedOrders = new ArrayList<>();
        mixedOrders.add(incartOrder);
        mixedOrders.add(shippedOrder);
        
        when(orderService.getOrdersBySellerId(anyInt())).thenReturn(mixedOrders);

        // Act & Assert
        mockMvc.perform(get("/order/seller/1")
                .contentType(MediaType.APPLICATION_JSON))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$[0].orderId").value(shippedOrder.getOrderId()))
               .andExpect(jsonPath("$[0].orderStatus").value("Shipped"))
               .andExpect(jsonPath("$").isArray())
               .andExpect(jsonPath("$.length()").value(1));
        
        verify(orderService, times(1)).getOrdersBySellerId(1);
    }
    
    @Test
    @DisplayName("Should handle empty result when getting seller orders")
     void testGetOrdersBySellerIdWithEmptyResult() throws Exception {
        // Arrange
        when(orderService.getOrdersBySellerId(anyInt())).thenReturn(Collections.emptyList());

        // Act & Assert
        mockMvc.perform(get("/order/seller/1")
                .contentType(MediaType.APPLICATION_JSON))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$").isArray())
               .andExpect(jsonPath("$").isEmpty());
        
        verify(orderService, times(1)).getOrdersBySellerId(1);
    }

    @Test
    @DisplayName("Should add order successfully")
     void testAddOrder() throws Exception {
        // Arrange
        String expectedResponse = "Order added successfully";
        Order mappedOrder = new Order();
        mappedOrder.setOrderQuantity(2.0);
        mappedOrder.setOrderStatus("Incart");
        
        when(modelMapper.map(any(OrderDTO.class), eq(Order.class))).thenReturn(mappedOrder);
        when(orderService.addOrder(any(Order.class))).thenReturn(expectedResponse);

        // Act & Assert
        mockMvc.perform(post("/order/add")
               .contentType(MediaType.APPLICATION_JSON)
               .content(objectMapper.writeValueAsString(testOrderDTO)))
               .andExpect(status().isOk())
               .andExpect(content().string(expectedResponse));
        
        verify(modelMapper, times(1)).map(any(OrderDTO.class), eq(Order.class));
        verify(orderService, times(1)).addOrder(any(Order.class));
    }
    
    @Test
    @DisplayName("Should set order status to Incart when adding order")
     void testAddOrderSetsIncartStatus() throws Exception {
        // Arrange
        String expectedResponse = "Order added successfully";
        when(modelMapper.map(any(OrderDTO.class), eq(Order.class))).thenAnswer(invocation -> {
            OrderDTO dto = invocation.getArgument(0);
            Order order = new Order();
            order.setOrderId(dto.getOrderId());
            order.setOrderQuantity(dto.getOrderQuantity());
            // The status should be set to "Incart" in the controller
            return order;
        });
        
        when(orderService.addOrder(any(Order.class))).thenAnswer(invocation -> {
            Order order = invocation.getArgument(0);
            assertEquals("Incart", order.getOrderStatus());
            return expectedResponse;
        });

        // Act & Assert
        mockMvc.perform(post("/order/add")
               .contentType(MediaType.APPLICATION_JSON)
               .content(objectMapper.writeValueAsString(testOrderDTO)))
               .andExpect(status().isOk())
               .andExpect(content().string(expectedResponse));
    }
    
    @Test
    @DisplayName("Should handle exception when adding order")
     void testAddOrderWithException() throws Exception {
        // Arrange
        when(modelMapper.map(any(OrderDTO.class), eq(Order.class))).thenReturn(testOrder);
        when(orderService.addOrder(any(Order.class))).thenThrow(new OrderException("Failed to add order"));

        // Act & Assert
        mockMvc.perform(post("/order/add")
               .contentType(MediaType.APPLICATION_JSON)
               .content(objectMapper.writeValueAsString(testOrderDTO)))
               .andExpect(status().isBadRequest());
        
        verify(modelMapper, times(1)).map(any(OrderDTO.class), eq(Order.class));
        verify(orderService, times(1)).addOrder(any(Order.class));
    }

    @Test
    @DisplayName("Should delete order successfully")
     void testDeleteOrder() throws Exception {
        // Arrange
        String expectedMessage = "Order with ID 1 deleted successfully!";
        // For void methods we don't need when...thenReturn
        // Just ensure the method doesn't throw an exception
        
        // Act & Assert
        mockMvc.perform(delete("/order/delete/1")
                .contentType(MediaType.APPLICATION_JSON))
               .andExpect(status().isOk())
               .andExpect(content().string(expectedMessage));
        
        verify(orderService, times(1)).deleteOrder(1);
    }
    
    @Test
    @DisplayName("Should handle exception when deleting order")
     void testDeleteOrderWithException() throws Exception {
        // Arrange
        doThrow(new OrderException("Order not found")).when(orderService).deleteOrder(anyInt());

        // Act & Assert
        mockMvc.perform(delete("/order/delete/999")
                .contentType(MediaType.APPLICATION_JSON))
               .andExpect(status().isBadRequest());
        
        verify(orderService, times(1)).deleteOrder(999);
    }

    @Test
    @DisplayName("Should get order invoice successfully")
     void testGetOrderInvoice() throws Exception {
        // Arrange
        when(orderService.getOrderInvoice(anyInt())).thenReturn(testCustomerOrderDTO);

        // Act & Assert
        mockMvc.perform(get("/order/invoice/1")
                .contentType(MediaType.APPLICATION_JSON))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.orderId").value(testCustomerOrderDTO.getOrderId()))
               .andExpect(jsonPath("$.productName").value(testCustomerOrderDTO.getProductName()))
               .andExpect(jsonPath("$.orderStatus").value(testCustomerOrderDTO.getOrderStatus()));
        
        verify(orderService, times(1)).getOrderInvoice(1);
    }
    
    @Test
    @DisplayName("Should handle exception when getting order invoice")
     void testGetOrderInvoiceWithException() throws Exception {
        // Arrange
        when(orderService.getOrderInvoice(anyInt())).thenThrow(new OrderException("Invoice not found"));

        // Act & Assert
        mockMvc.perform(get("/order/invoice/999")
                .contentType(MediaType.APPLICATION_JSON))
               .andExpect(status().isBadRequest());
        
        verify(orderService, times(1)).getOrderInvoice(999);
    }
    
    @Test
    @DisplayName("Should handle multiple order types in customer orders")
     void testGetOrdersByCustomerIdWithMultipleOrderTypes() throws Exception {
        // Arrange
        Order deliveredOrder = new Order();
        deliveredOrder.setOrderId(1);
        deliveredOrder.setOrderStatus("Delivered");
        deliveredOrder.setProduct(testProduct);
        deliveredOrder.setCustomer(testCustomer);
        
        Order shippedOrder = new Order();
        shippedOrder.setOrderId(2);
        shippedOrder.setOrderStatus("Shipped");
        shippedOrder.setProduct(testProduct);
        shippedOrder.setCustomer(testCustomer);
        
        List<Order> orders = new ArrayList<>();
        orders.add(deliveredOrder);
        orders.add(shippedOrder);
        
        when(orderService.getOrderByCustomerId(anyInt())).thenReturn(orders);
        when(ratingService.getRatingByOrderId(1)).thenReturn(true);
        when(ratingService.getRatingByOrderId(2)).thenReturn(false);

        // Act & Assert
     mockMvc.perform(get("/order/customer/1")
                .contentType(MediaType.APPLICATION_JSON))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$").isArray())
               .andExpect(jsonPath("$.length()").value(2))
               .andExpect(jsonPath("$[0].orderId").value(1))
               .andExpect(jsonPath("$[0].orderRatingStatus").value("Rated"))
               .andExpect(jsonPath("$[1].orderId").value(2))
               .andExpect(jsonPath("$[1].orderRatingStatus").value("Not Rated"))
               .andReturn();
        
        verify(orderService, times(1)).getOrderByCustomerId(1);
        verify(ratingService, times(1)).getRatingByOrderId(1);
        verify(ratingService, times(1)).getRatingByOrderId(2);
    }
}	