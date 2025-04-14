package com.ftohbackend.controllertesting;

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.BDDMockito.willThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

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

@WebMvcTest
@ContextConfiguration(classes = { OrderControllerImpl.class })//@AutoConfigureMockMvc

public class OrderControllerTest {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private OrderService orderService;

    @MockBean
    private ProductService productService;

    @MockBean
    private CustomerService customerService;

    @MockBean
    private ModelMapper modelMapper;

    private Order order;
    private OrderDTO orderDTO;
    private Product product;
    private Customer customer;
    private Seller seller;
    private CustomerOrderDTO customerOrderDTO;
    private SellerOrderDTO sellerOrderDTO;

    @BeforeEach
    public void setUp() {
        // Set up the Customer
        customer = new Customer();
        customer.setCustomerId(1);
        customer.setCustomerFirstName("John");
        customer.setCustomerLastName("Doe");
        customer.setCustomerEmail("john.doe@example.com");
        customer.setCustomerPassword("password");
        customer.setCustomerPlace("SomePlace");
        customer.setCustomerCity("SomeCity");
        customer.setCustomerPincode("123456");
        customer.setCustomerState("SomeState");
        customer.setCustomerPhoneNumber("9876543210");
        customer.setCustomerIsActive(true);
        
        // Set up the Seller
        seller = new Seller();
        seller.setSellerId(1);
        seller.setSellerFirstName("Jane");
        seller.setSellerLastName("Smith");
        seller.setSellerEmail("jane.smith@example.com");
        seller.setSellerPassword("password");
        seller.setSellerPlace("SomePlace");
        seller.setSellerCity("SomeCity");
        seller.setSellerPincode("654321");
        seller.setSellerState("SomeState");
        seller.setSellerMobileNumber("8765432109");
        seller.setSellerStatus("Active");
        
        // Set up the Product
        product = new Product();
        product.setProductId(1);
        product.setProductName("Test Product");
        product.setProductPrice(100.0);
        product.setProductQuantity(10.0);
        product.setProductQuantityType("Kg");
        product.setImageUrl("test-image-url.jpg");
        product.setProductDescription("Test product description");
        product.setProductCategory("Test Category");
        product.setProductRatingValue(4.5);
        product.setProductRatingCount(10);
        product.setSeller(seller);
        
        // Set up the Order
        order = new Order();
        order.setOrderId(1);
//        order.setProductId(1);
//        order.setCustomerId(1);
        order.setOrderQuantity(2.0);
        order.setOrderStatus("Placed");
        order.setProduct(product);
        order.setCustomer(customer);
        
        // Set up the OrderDTO
        orderDTO = new OrderDTO();
        orderDTO.setOrderId(1);
        orderDTO.setProductId(1);
        orderDTO.setCustomerId(1);
        orderDTO.setOrderQuantity(2.0);
        orderDTO.setOrderStatus("Placed");
        
        // Set up the CustomerOrderDTO
        customerOrderDTO = new CustomerOrderDTO();
        customerOrderDTO.setOrderId(1);
        customerOrderDTO.setProductName("Test Product");
        customerOrderDTO.setImageUrl("test-image-url.jpg");
        customerOrderDTO.setProductDescription("Test product description");
        customerOrderDTO.setOrderQuantity(2.0);
        customerOrderDTO.setProductPrice(100.0);
        customerOrderDTO.setOrderStatus("Placed");
        customerOrderDTO.setSellerName("Jane Smith");
        customerOrderDTO.setSellerPlace("SomePlace");
        customerOrderDTO.setSellerCity("SomeCity");
        customerOrderDTO.setSellerState("SomeState");
        customerOrderDTO.setSellerPincode("654321");
        customerOrderDTO.setCustomerName("John Doe");
        customerOrderDTO.setCustomerPlace("SomePlace");
        customerOrderDTO.setCustomerCity("SomeCity");
        customerOrderDTO.setCustomerState("SomeState");
        customerOrderDTO.setCustomerPincode("123456");
        customerOrderDTO.setProductQuantityType("Kg");
        
        // Set up the SellerOrderDTO
        sellerOrderDTO = new SellerOrderDTO();
        sellerOrderDTO.setOrderId(1);
        sellerOrderDTO.setProductName("Test Product");
        sellerOrderDTO.setImageUrl("test-image-url.jpg");
        sellerOrderDTO.setProductDescription("Test product description");
        sellerOrderDTO.setOrderQuantity(2.0);
        sellerOrderDTO.setProductQuantityType("Kg");
        sellerOrderDTO.setProductPrice(100.0);
        sellerOrderDTO.setCustomerName("John Doe");
        sellerOrderDTO.setOrderStatus("Placed");
    }
    
    @AfterEach
    public void tearDown() {
        order = null;
        orderDTO = null;
        product = null;
        customer = null;
        seller = null;
        customerOrderDTO = null;
        sellerOrderDTO = null;
    }

    @Test
    @DisplayName("JUnit test for getAllOrders operation")
    public void givenOrderList_whenGetAllOrders_thenReturnOrderList() throws Exception {
        // given - precondition or setup
        List<Order> orders = new ArrayList<>();
        orders.add(order);
        
        given(orderService.getAllOrders()).willReturn(orders);
        
        // when - action or behavior
        ResultActions response = mockMvc.perform(get("/order"));
        
        // then - verify the output
        response.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()", is(orders.size())));
    }
    
    @Test
    @DisplayName("JUnit test for getCartOrdersByCustomerId operation")
    public void givenCustomerId_whenGetCartOrdersByCustomerId_thenReturnCustomerOrderDTOList() throws Exception {
        // given - precondition or setup
        List<Order> orders = new ArrayList<>();
        order.setOrderStatus("Incart");
        orders.add(order);
        
        List<CustomerOrderDTO> customerOrderDTOs = new ArrayList<>();
        customerOrderDTOs.add(customerOrderDTO);
        
        given(orderService.getOrderByCustomerId(anyInt())).willReturn(orders);
        
        // when - action or behavior
        ResultActions response = mockMvc.perform(get("/order/orders/incart/{customerId}", 1));
        
        // then - verify the output
        response.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()", is(1)));
    }
    
    @Test
    @DisplayName("JUnit test for getOrdersByCustomerId operation")
    public void givenCustomerId_whenGetOrdersByCustomerId_thenReturnCustomerOrderDTOList() throws Exception {
        // given - precondition or setup
        List<Order> orders = new ArrayList<>();
        orders.add(order);
        
        List<CustomerOrderDTO> customerOrderDTOs = new ArrayList<>();
        customerOrderDTOs.add(customerOrderDTO);
        
        given(orderService.getOrderByCustomerId(anyInt())).willReturn(orders);
        
        // when - action or behavior
        ResultActions response = mockMvc.perform(get("/order/customer/{customerId}", 1));
        
        // then - verify the output
        response.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()", is(1)));
    }
    
    @Test
    @DisplayName("JUnit test for updateOrderStatus operation")
    public void givenOrderIdAndStatus_whenUpdateOrderStatus_thenReturnSuccessMessage() throws Exception {
        // given - precondition or setup
        given(orderService.updateOrderStatus(anyInt(), anyString())).willReturn("Order status updated successfully");
        
        // when - action or behavior
        ResultActions response = mockMvc.perform(put("/order/order/{orderId}/{orderStatus}", 1, "Delivered"));
        
        // then - verify the output
        response.andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string("Order status updated successfully"));
    }
    
    @Test
    @DisplayName("JUnit test for updateOrderStatus operation - OrderException")
    public void givenInvalidOrderIdOrStatus_whenUpdateOrderStatus_thenThrowsOrderException() throws Exception {
        // given - precondition or setup
        given(orderService.updateOrderStatus(anyInt(), anyString())).willThrow(OrderException.class);
        
        // when - action or behavior
        ResultActions response = mockMvc.perform(put("/order/order/{orderId}/{orderStatus}", 999, "Invalid"));
        
        // then - verify the output
        response.andDo(print())
                .andExpect(status().isInternalServerError());
    }
    
    @Test
    @DisplayName("JUnit test for getOrdersBySellerId operation")
    public void givenSellerId_whenGetOrdersBySellerId_thenReturnSellerOrderDTOList() throws Exception {
        // given - precondition or setup
        List<Order> orders = new ArrayList<>();
        orders.add(order);
        
        List<SellerOrderDTO> sellerOrderDTOs = new ArrayList<>();
        sellerOrderDTOs.add(sellerOrderDTO);
        
        given(orderService.getOrdersBySellerId(anyInt())).willReturn(orders);
        
        // when - action or behavior
        ResultActions response = mockMvc.perform(get("/order/seller/{sellerId}", 1));
        
        // then - verify the output
        response.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()", is(1)));
    }
    
    @Test
    @DisplayName("JUnit test for addOrder operation")
    public void givenOrderDTO_whenAddOrder_thenReturnSuccessMessage() throws Exception {
        // given - precondition or setup
        given(modelMapper.map(any(OrderDTO.class), any())).willReturn(order);
        given(orderService.addOrder(any(Order.class))).willReturn("Order added successfully");
        
        // when - action or behavior
        ResultActions response = mockMvc.perform(post("/order/add")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(orderDTO)));
                
        // then - verify the output
        response.andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string("Order added successfully"));
    }
    
    @Test
    @DisplayName("JUnit test for deleteOrder operation")
    public void givenOrderId_whenDeleteOrder_thenReturnSuccessMessage() throws Exception {
        // given - precondition or setup
        willDoNothing().given(orderService).deleteOrder(anyInt());
        
        // when - action or behavior
        ResultActions response = mockMvc.perform(delete("/order/delete/{orderId}", 1));
        
        // then - verify the output
        response.andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string("Order with ID 1 deleted successfully!"));
    }
    
    @Test
    @DisplayName("JUnit test for deleteOrder operation - OrderException")
    public void givenInvalidOrderId_whenDeleteOrder_thenThrowsOrderException() throws Exception {
        // given - precondition or setup
        willThrow(OrderException.class).given(orderService).deleteOrder(anyInt());
        
        // when - action or behavior
        ResultActions response = mockMvc.perform(delete("/order/delete/{orderId}", 999));
        
        // then - verify the output
        response.andDo(print())
                .andExpect(status().isInternalServerError());
    }
    
    @Test
    @DisplayName("JUnit test for getOrderInvoice operation")
    public void givenOrderId_whenGetOrderInvoice_thenReturnCustomerOrderDTO() throws Exception {
        // given - precondition or setup
        given(orderService.getOrderInvoice(anyInt())).willReturn(customerOrderDTO);
        
        // when - action or behavior
        ResultActions response = mockMvc.perform(get("/order/invoice/{orderId}", 1));
        
        // then - verify the output
        response.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.orderId", is(customerOrderDTO.getOrderId())))
                .andExpect(jsonPath("$.productName", is(customerOrderDTO.getProductName())))
                .andExpect(jsonPath("$.orderQuantity", is(customerOrderDTO.getOrderQuantity())))
                .andExpect(jsonPath("$.productPrice", is(customerOrderDTO.getProductPrice())))
                .andExpect(jsonPath("$.orderStatus", is(customerOrderDTO.getOrderStatus())));
    }
    
    @Test
    @DisplayName("JUnit test for getOrderInvoice operation - OrderException")
    public void givenInvalidOrderId_whenGetOrderInvoice_thenThrowsOrderException() throws Exception {
        // given - precondition or setup
        given(orderService.getOrderInvoice(anyInt())).willThrow(OrderException.class);
        
        // when - action or behavior
        ResultActions response = mockMvc.perform(get("/order/invoice/{orderId}", 999));
        
        // then - verify the output
        response.andDo(print())
                .andExpect(status().isInternalServerError());
    }
}