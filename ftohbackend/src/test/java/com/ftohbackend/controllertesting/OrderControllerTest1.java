package com.ftohbackend.controllertesting;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
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
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ftohbackend.controller.OrderControllerImpl;
import com.ftohbackend.dto.CustomerOrderDTO;
import com.ftohbackend.dto.OrderDTO;
import com.ftohbackend.dto.SellerOrderDTO;
import com.ftohbackend.model.Customer;
import com.ftohbackend.model.Order;
import com.ftohbackend.model.Product;
import com.ftohbackend.model.Seller;
import com.ftohbackend.service.CustomerService;
import com.ftohbackend.service.OrderService;
import com.ftohbackend.service.ProductService;
import com.ftohbackend.service.RatingService;

public class OrderControllerTest1 {

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

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(orderController).build();

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

        SellerOrderDTO testSellerOrderDTO = new SellerOrderDTO();
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
    public void testGetAllOrders() throws Exception {
        // Arrange
        when(orderService.getAllOrders()).thenReturn(testOrders);

        // Act & Assert
        mockMvc.perform(get("/order"))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$[0].orderId").value(testOrder.getOrderId()))
               .andExpect(jsonPath("$[0].orderQuantity").value(testOrder.getOrderQuantity()))
               .andExpect(jsonPath("$[0].orderStatus").value(testOrder.getOrderStatus()));
    }

    @Test
    public void testGetCartOrdersByCustomerId() throws Exception {
        // Arrange
        testOrder.setOrderStatus("Incart");
        when(orderService.getOrderByCustomerId(anyInt())).thenReturn(testOrders);
        when(ratingService.getRatingByOrderId(anyInt())).thenReturn(false);

        // Act & Assert
        mockMvc.perform(get("/order/orders/incart/1"))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$[0].orderId").value(testOrder.getOrderId()))
               .andExpect(jsonPath("$[0].orderStatus").value("Incart"))
               .andExpect(jsonPath("$[0].productName").value(testProduct.getProductName()));
    }

    @Test
    public void testGetOrdersByCustomerId() throws Exception {
        // Arrange
        testOrder.setOrderStatus("Delivered");
        when(orderService.getOrderByCustomerId(anyInt())).thenReturn(testOrders);
        when(ratingService.getRatingByOrderId(anyInt())).thenReturn(false);

        // Act & Assert
        mockMvc.perform(get("/order/customer/1"))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$[0].orderId").value(testOrder.getOrderId()))
               .andExpect(jsonPath("$[0].orderStatus").value("Delivered"))
               .andExpect(jsonPath("$[0].orderRatingStatus").value("Not Rated"));
    }

    @Test
    public void testUpdateOrderStatus() throws Exception {
        // Arrange
        String expectedResponse = "Order status updated successfully";
        when(orderService.updateOrderStatus(anyInt(), anyString())).thenReturn(expectedResponse);

        // Act & Assert
        mockMvc.perform(put("/order/order/1/Delivered"))
               .andExpect(status().isOk())
               .andExpect(content().string(expectedResponse));
    }

    @Test
    public void testGetOrdersBySellerId() throws Exception {
        // Arrange
        when(orderService.getOrdersBySellerId(anyInt())).thenReturn(testOrders);

        // Act & Assert
        mockMvc.perform(get("/order/seller/1"))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$[0].orderId").value(testOrder.getOrderId()))
               .andExpect(jsonPath("$[0].productName").value(testProduct.getProductName()))
               .andExpect(jsonPath("$[0].customerName").value(testCustomer.getCustomerFirstName() + " " + testCustomer.getCustomerLastName()));
    }

    @Test
    public void testAddOrder() throws Exception {
        // Arrange
        String expectedResponse = "Order added successfully";
        when(modelMapper.map(any(OrderDTO.class), any())).thenReturn(testOrder);
        when(orderService.addOrder(any(Order.class))).thenReturn(expectedResponse);

        // Act & Assert
        mockMvc.perform(post("/order/add")
               .contentType(MediaType.APPLICATION_JSON)
               .content(objectMapper.writeValueAsString(testOrderDTO)))
               .andExpect(status().isOk())
               .andExpect(content().string(expectedResponse));
    }

    @Test
    public void testDeleteOrder() throws Exception {
        // Arrange
        doNothing().when(orderService).deleteOrder(anyInt());

        // Act & Assert
        mockMvc.perform(delete("/order/delete/1"))
               .andExpect(status().isOk())
               .andExpect(content().string("Order with ID 1 deleted successfully!"));
    }

    @Test
    public void testGetOrderInvoice() throws Exception {
        // Arrange
        when(orderService.getOrderInvoice(anyInt())).thenReturn(testCustomerOrderDTO);

        // Act & Assert
        mockMvc.perform(get("/order/invoice/1"))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.orderId").value(testCustomerOrderDTO.getOrderId()))
               .andExpect(jsonPath("$.productName").value(testCustomerOrderDTO.getProductName()))
               .andExpect(jsonPath("$.orderStatus").value(testCustomerOrderDTO.getOrderStatus()));
    }
}