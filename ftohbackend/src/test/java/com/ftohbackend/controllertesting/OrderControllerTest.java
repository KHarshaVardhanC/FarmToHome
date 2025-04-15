package com.ftohbackend.controllertesting;

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.List;

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
import com.ftohbackend.controller.OrderController;
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

@WebMvcTest(OrderControllerImpl.class)
@ContextConfiguration(classes = { OrderController.class })

public class OrderControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private OrderService orderService;

    @MockBean
    private ProductService productService;

    @MockBean
    private CustomerService customerService;

    @MockBean
    private RatingService ratingService;

    @MockBean
    private ModelMapper modelMapper;

    @Autowired
    private ObjectMapper objectMapper;

    private Order order;
    private OrderDTO orderDTO;
    private Product product;
    private Customer customer;
    private Seller seller;
    private CustomerOrderDTO customerOrderDTO;
    private SellerOrderDTO sellerOrderDTO;

    @BeforeEach
    public void setup() {
        // Initialize test data
        seller = new Seller();
        seller.setSellerId(1);
        seller.setSellerFirstName("John");
        seller.setSellerLastName("Doe");

        product = new Product();
        product.setProductId(1);
        product.setProductName("Test Product");
        product.setProductPrice(100.0);
        product.setProductDescription("Test Description");
        product.setProductQuantityType("kg");
        product.setImageUrl("http://test.com/image.jpg");
        product.setSeller(seller);

        customer = new Customer();
        customer.setCustomerId(1);
        customer.setCustomerFirstName("Jane");
        customer.setCustomerLastName("Smith");

        order = new Order();
        order.setOrderId(1);
        order.setOrderQuantity(2.0);
        order.setOrderStatus("Incart");
        order.setProduct(product);
        order.setCustomer(customer);

        orderDTO = new OrderDTO();
        orderDTO.setOrderId(1);
        orderDTO.setOrderQuantity(2.0);
        orderDTO.setCustomerId(1);
        orderDTO.setProductId(1);

        customerOrderDTO = new CustomerOrderDTO();
        customerOrderDTO.setOrderId(1);
        customerOrderDTO.setOrderQuantity(2.0);
        customerOrderDTO.setOrderStatus("Incart");
        customerOrderDTO.setProductName("Test Product");
        customerOrderDTO.setProductPrice(100.0);
        customerOrderDTO.setImageUrl("http://test.com/image.jpg");
        customerOrderDTO.setProductDescription("Test Description");
        customerOrderDTO.setProductQuantityType("kg");
        customerOrderDTO.setSellerName("John Doe");
        customerOrderDTO.setOrderRatingStatus("5");

        sellerOrderDTO = new SellerOrderDTO();
        sellerOrderDTO.setOrderId(1);
        sellerOrderDTO.setOrderQuantity(2.0);
        sellerOrderDTO.setOrderStatus("Incart");
        sellerOrderDTO.setProductName("Test Product");
        sellerOrderDTO.setProductPrice(100.0);
        sellerOrderDTO.setImageUrl("http://test.com/image.jpg");
        sellerOrderDTO.setProductDescription("Test Description");
        sellerOrderDTO.setProductQuantityType("kg");
        sellerOrderDTO.setCustomerName("Jane Smith");
    }

    @Test
    @DisplayName("JUnit test for getAllOrders operation")
    public void givenOrderList_whenGetAllOrders_thenReturnOrderList() throws Exception {
        // given
        List<Order> orders = new ArrayList<>();
        orders.add(order);
        given(orderService.getAllOrders()).willReturn(orders);
        
        // when
        ResultActions response = mockMvc.perform(get("/order"));
        
        // then
        response.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()", is(1)))
                .andExpect(jsonPath("$[0].orderId", is(order.getOrderId())))
                .andExpect(jsonPath("$[0].orderQuantity", is(order.getOrderQuantity())))
                .andExpect(jsonPath("$[0].orderStatus", is(order.getOrderStatus())));
    }

    @Test
    @DisplayName("JUnit test for getAllOrders operation - Empty List")
    public void givenEmptyOrderList_whenGetAllOrders_thenReturnEmptyList() throws Exception {
        // given
        List<Order> orders = new ArrayList<>();
        given(orderService.getAllOrders()).willReturn(orders);
        
        // when
        ResultActions response = mockMvc.perform(get("/order"));
        
        // then
        response.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()", is(0)));
    }

    @Test
    @DisplayName("JUnit test for getAllOrders operation - Exception")
    public void givenServiceError_whenGetAllOrders_thenThrowException() throws Exception {
        // given
        given(orderService.getAllOrders()).willThrow(new Exception("Internal error"));
        
        // when
        ResultActions response = mockMvc.perform(get("/order"));
        
        // then
        response.andDo(print())
                .andExpect(status().isInternalServerError());
    }

    // Remove or comment out the getOrderById tests since the endpoint is commented out in the controller
    // If you implement this endpoint later, you can uncomment these tests

    @Test
    @DisplayName("JUnit test for getCartOrdersByCustomerId operation")
    public void givenCustomerId_whenGetCartOrdersByCustomerId_thenReturnOrdersList() throws Exception {
        // given
        List<Order> orders = new ArrayList<>();
        orders.add(order);
        List<CustomerOrderDTO> customerOrders = new ArrayList<>();
        customerOrders.add(customerOrderDTO);
        
        given(orderService.getOrderByCustomerId(anyInt())).willReturn(orders);
        given(ratingService.getRatingByOrderId(anyInt())).willReturn(true);
        
        // when
        ResultActions response = mockMvc.perform(get("/order/orders/incart/{customerId}", 1));
        
        // then
        response.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()", is(1)))
                .andExpect(jsonPath("$[0].orderId", is(customerOrderDTO.getOrderId())))
                .andExpect(jsonPath("$[0].orderQuantity", is(customerOrderDTO.getOrderQuantity())))
                .andExpect(jsonPath("$[0].orderStatus", is(customerOrderDTO.getOrderStatus())))
                .andExpect(jsonPath("$[0].productName", is(customerOrderDTO.getProductName())))
                .andExpect(jsonPath("$[0].orderRatingStatus", is(customerOrderDTO.getOrderRatingStatus())));
    }

    @Test
    @DisplayName("JUnit test for getCartOrdersByCustomerId operation - Empty List")
    public void givenCustomerId_whenGetCartOrdersByCustomerId_thenReturnEmptyList() throws Exception {
        // given
        List<Order> orders = new ArrayList<>();
        given(orderService.getOrderByCustomerId(anyInt())).willReturn(orders);
        
        // when
        ResultActions response = mockMvc.perform(get("/order/orders/incart/{customerId}", 1));
        
        // then
        response.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()", is(0)));
    }

    @Test
    @DisplayName("JUnit test for getCartOrdersByCustomerId operation - Exception")
    public void givenInvalidCustomerId_whenGetCartOrdersByCustomerId_thenThrowException() throws Exception {
        // given
        given(orderService.getOrderByCustomerId(anyInt())).willThrow(new OrderException("Customer not found"));
        
        // when
        ResultActions response = mockMvc.perform(get("/order/orders/incart/{customerId}", 999));
        
        // then
        response.andDo(print())
                .andExpect(status().isInternalServerError());
    }

    @Test
    @DisplayName("JUnit test for getOrdersByCustomerId operation")
    public void givenCustomerId_whenGetOrdersByCustomerId_thenReturnOrdersList() throws Exception {
        // given
        List<Order> orders = new ArrayList<>();
        order.setOrderStatus("Delivered");  // Not "Incart"
        orders.add(order);
        List<CustomerOrderDTO> customerOrders = new ArrayList<>();
        customerOrderDTO.setOrderStatus("Delivered");
        customerOrders.add(customerOrderDTO);
        
        given(orderService.getOrderByCustomerId(anyInt())).willReturn(orders);
        
        // when
        ResultActions response = mockMvc.perform(get("/order/customer/{customerId}", 1));
        
        // then
        response.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()", is(1)))
                .andExpect(jsonPath("$[0].orderId", is(customerOrderDTO.getOrderId())))
                .andExpect(jsonPath("$[0].orderQuantity", is(customerOrderDTO.getOrderQuantity())))
                .andExpect(jsonPath("$[0].orderStatus", is("Delivered")));
    }

    @Test
    @DisplayName("JUnit test for getOrdersByCustomerId operation - Empty List")
    public void givenCustomerId_whenGetOrdersByCustomerId_thenReturnEmptyList() throws Exception {
        // given
        List<Order> orders = new ArrayList<>();
        given(orderService.getOrderByCustomerId(anyInt())).willReturn(orders);
        
        // when
        ResultActions response = mockMvc.perform(get("/order/customer/{customerId}", 1));
        
        // then
        response.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()", is(0)));
    }

    @Test
    @DisplayName("JUnit test for getOrdersByCustomerId operation - OrderException")
    public void givenInvalidCustomerId_whenGetOrdersByCustomerId_thenThrowOrderException() throws Exception {
        // given
        given(orderService.getOrderByCustomerId(anyInt())).willThrow(new OrderException("Customer not found"));
        
        // when
        ResultActions response = mockMvc.perform(get("/order/customer/{customerId}", 999));
        
        // then
        response.andDo(print())
                .andExpect(status().isInternalServerError());
    }

    @Test
    @DisplayName("JUnit test for getOrdersBySellerId operation")
    public void givenSellerId_whenGetOrdersBySellerId_thenReturnOrdersList() throws Exception {
        // given
        List<Order> orders = new ArrayList<>();
        orders.add(order);
        List<SellerOrderDTO> sellerOrders = new ArrayList<>();
        sellerOrders.add(sellerOrderDTO);
        
        given(orderService.getOrdersBySellerId(anyInt())).willReturn(orders);
        
        // when
        ResultActions response = mockMvc.perform(get("/order/seller/{sellerId}", 1));
        
        // then
        response.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()", is(1)))
                .andExpect(jsonPath("$[0].orderId", is(sellerOrderDTO.getOrderId())))
                .andExpect(jsonPath("$[0].orderQuantity", is(sellerOrderDTO.getOrderQuantity())))
                .andExpect(jsonPath("$[0].orderStatus", is(sellerOrderDTO.getOrderStatus())));
    }

    @Test
    @DisplayName("JUnit test for getOrdersBySellerId operation - Empty List")
    public void givenSellerId_whenGetOrdersBySellerId_thenReturnEmptyList() throws Exception {
        // given
        List<Order> orders = new ArrayList<>();
        given(orderService.getOrdersBySellerId(anyInt())).willReturn(orders);
        
        // when
        ResultActions response = mockMvc.perform(get("/order/seller/{sellerId}", 1));
        
        // then
        response.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()", is(0)));
    }

    @Test
    @DisplayName("JUnit test for getOrdersBySellerId operation - Exception")
    public void givenInvalidSellerId_whenGetOrdersBySellerId_thenThrowException() throws Exception {
        // given
        given(orderService.getOrdersBySellerId(anyInt())).willThrow(new Exception("Seller not found"));
        
        // when
        ResultActions response = mockMvc.perform(get("/order/seller/{sellerId}", 999));
        
        // then
        response.andDo(print())
                .andExpect(status().isInternalServerError());
    }

    @Test
    @DisplayName("JUnit test for addOrder operation")
    public void givenOrderDTO_whenAddOrder_thenReturnSuccessMessage() throws Exception {
        // given
        String successMessage = "Order added successfully";
        given(modelMapper.map(any(OrderDTO.class), any())).willReturn(order);
        given(orderService.addOrder(any(Order.class))).willReturn(successMessage);
        
        // when
        ResultActions response = mockMvc.perform(post("/order/add")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(orderDTO)));
                
        // then
        response.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", is(successMessage)));
    }

    @Test
    @DisplayName("JUnit test for addOrder operation - OrderException")
    public void givenInvalidOrderDTO_whenAddOrder_thenThrowsOrderException() throws Exception {
        // given
        given(modelMapper.map(any(OrderDTO.class), any())).willReturn(order);
        given(orderService.addOrder(any(Order.class))).willThrow(new OrderException("Invalid order"));
        
        // when
        ResultActions response = mockMvc.perform(post("/order/add")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(orderDTO)));
                
        // then
        response.andDo(print())
                .andExpect(status().isInternalServerError());
    }

    @Test
    @DisplayName("JUnit test for updateOrderStatus operation")
    public void givenOrderIdAndStatus_whenUpdateOrderStatus_thenReturnSuccessMessage() throws Exception {
        // given
        String successMessage = "Order status updated successfully";
        given(orderService.updateOrderStatus(anyInt(), anyString())).willReturn(successMessage);
        
        // when
        ResultActions response = mockMvc.perform(put("/order/order/{orderId}/{orderStatus}", 1, "Delivered"));
        
        // then
        response.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", is(successMessage)));
    }

    @Test
    @DisplayName("JUnit test for updateOrderStatus operation - Exception")
    public void givenInvalidOrderIdOrStatus_whenUpdateOrderStatus_thenThrowException() throws Exception {
        // given
        given(orderService.updateOrderStatus(anyInt(), anyString())).willThrow(new OrderException("Invalid order"));
        
        // when
        ResultActions response = mockMvc.perform(put("/order/order/{orderId}/{orderStatus}", 999, "Invalid"));
        
        // then
        response.andDo(print())
                .andExpect(status().isInternalServerError());
    }

    @Test
    @DisplayName("JUnit test for deleteOrder operation")
    public void givenOrderId_whenDeleteOrder_thenReturnSuccessMessage() throws Exception {
        // given
        String successMessage = "Order with ID 1 deleted successfully!";
        
        // when
        ResultActions response = mockMvc.perform(delete("/order/delete/{orderId}", 1));
        
        // then
        response.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", is(successMessage)));
    }

    @Test
    @DisplayName("JUnit test for deleteOrder operation - Exception")
    public void givenInvalidOrderId_whenDeleteOrder_thenThrowException() throws Exception {
        // given
        given(orderService.deleteOrder(anyInt())).willThrow(new OrderException("Order not found"));
        
        // when
        ResultActions response = mockMvc.perform(delete("/order/delete/{orderId}", 999));
        
        // then
        response.andDo(print())
                .andExpect(status().isInternalServerError());
    }

    @Test
    @DisplayName("JUnit test for getOrderInvoice operation")
    public void givenOrderId_whenGetOrderInvoice_thenReturnCustomerOrderDTO() throws Exception {
        // given
        given(orderService.getOrderInvoice(anyInt())).willReturn(customerOrderDTO);
        
        // when
        ResultActions response = mockMvc.perform(get("/order/invoice/{orderId}", 1));
        
        // then
        response.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.orderId", is(customerOrderDTO.getOrderId())))
                .andExpect(jsonPath("$.orderQuantity", is(customerOrderDTO.getOrderQuantity())))
                .andExpect(jsonPath("$.orderStatus", is(customerOrderDTO.getOrderStatus())));
    }

    @Test
    @DisplayName("JUnit test for getOrderInvoice operation - Exception")
    public void givenInvalidOrderId_whenGetOrderInvoice_thenThrowException() throws Exception {
        // given
        given(orderService.getOrderInvoice(anyInt())).willThrow(new OrderException("Order not found"));
        
        // when
        ResultActions response = mockMvc.perform(get("/order/invoice/{orderId}", 999));
        
        // then
        response.andDo(print())
                .andExpect(status().isInternalServerError());
    }
}