package com.ftohbackend.controller;

import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import org.json.JSONObject;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ftohbackend.dto.CustomerOrderDTO;
import com.ftohbackend.dto.OrderDTO;
import com.ftohbackend.dto.OrderReport;
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
import com.razorpay.RazorpayClient;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/order")
public class OrderControllerImpl implements OrderController {

	@Value("${razorpay.api.key}")
    private String razorpayKey;

    @Value("${razorpay.api.secret}")
    private String razorpaySecret;

	@Autowired
	OrderService orderService;

	@Autowired
	ProductService productService;

	@Autowired
	CustomerService customerService;

	@Autowired
	ModelMapper modelMapper;

	@Autowired
	RatingService ratingService;

	@GetMapping("")
	@Override
	public List<Order> getAllOrders() throws Exception {
		return orderService.getAllOrders();
	}

//	@GetMapping("/{orderId}") 
	@Override
	public Order getOrderById(@PathVariable Integer orderId) throws Exception {
		return orderService.getOrderById(orderId);
	}


	 
    //===============================================================================================================================
    
    @PostMapping("/payment/create")
    public ResponseEntity<?> createPaymentOrder(@RequestBody OrderDTO orderDTO) {
        try {
            if (orderDTO.getProductId() == null) {
                return ResponseEntity.badRequest().body("Product ID is required");
            }

            if (orderDTO.getCustomerId() == null) {
                return ResponseEntity.badRequest().body("Customer ID is required");
            }

            Product product = productService.getProductById(orderDTO.getProductId());
            if (product == null) {
                return ResponseEntity.badRequest().body("Invalid product ID");
            }

            Customer customer = customerService.getCustomerById(orderDTO.getCustomerId());
            if (customer == null) {
                return ResponseEntity.badRequest().body("Invalid customer ID");
            }

            Order order = modelMapper.map(orderDTO, Order.class);
            order.setProduct(product);
            order.setCustomer(customer);
            order.setOrderStatus("PENDING");
            order.setPaymentStatus("CREATED");

            RazorpayClient razorpay = new RazorpayClient(razorpayKey, razorpaySecret);

            String uuid = UUID.randomUUID().toString().replace("-", "").substring(0, 20);
            String receiptId = "rcpt_" + uuid;

            Double productPrice = order.getProduct().getProductPrice();
            if (productPrice == null) {
                throw new OrderException("Product price cannot be null for product: " + order.getProduct().getProductId());
            }

            int amount = (int) (order.getOrderQuantity() * productPrice * 100);

            JSONObject options = new JSONObject();
            options.put("amount", amount);
            options.put("currency", "INR");
            options.put("receipt", receiptId);

            com.razorpay.Order razorpayOrder = razorpay.orders.create(options);

            order.setRazorpayOrderId(razorpayOrder.get("id"));
            order.setReceiptId(receiptId);
            order.setPaymentStatus("CREATED");

            Order savedOrder = orderService.saveOrder(order);

            return ResponseEntity.ok(Map.of(
                    "orderId", razorpayOrder.get("id"),
                    "razorpayKey", razorpayKey,
                    "amount", razorpayOrder.get("amount"),
                    "currency", "INR",
                    "orderDBId", savedOrder.getOrderId()
            ));

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to create Razorpay order");
        }
    }

    
    @PostMapping("/payment/verify")
    public ResponseEntity<?> verifyPayment(
        @RequestParam("razorpay_order_id") String orderId, 
        @RequestParam("razorpay_payment_id") String paymentId, 
        @RequestParam("razorpay_signature") String signature) {

        try {
            // Generate the expected signature from Razorpay API using Razorpay secret and order details
            String generatedSignature = generateSignature(orderId, paymentId);

            // Verify if the provided signature matches the generated signature
            if (generatedSignature.equals(signature)) {
                // Signature is valid, proceed with order update
                Order order = orderService.getOrderByRazorpayOrderId(orderId);
                
                if (order != null) {
                    order.setPaymentStatus("SUCCESS");
                    order.setRazorpayPaymentId(paymentId);
                    orderService.saveOrder(order);  // Make sure this method exists and works
                    return ResponseEntity.ok("Payment verified successfully.");
                } else {
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Order not found.");
                }
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid payment signature.");
            }
        } catch (OrderException e) {
            // Specific exception handling for Order not found
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Payment verification failed.");
        }
    }

    // Helper method to generate the expected signature for verification
    private String generateSignature(String orderId, String paymentId) throws Exception {
        String keySecret = razorpaySecret;  // Razorpay Secret Key
        String data = orderId + "|" + paymentId;  // Concatenate orderId and paymentId

        // Create HMAC using SHA256
        SecretKeySpec keySpec = new SecretKeySpec(keySecret.getBytes(), "HmacSHA256");
        Mac mac = Mac.getInstance("HmacSHA256");
        mac.init(keySpec);

        byte[] hashBytes = mac.doFinal(data.getBytes());

        // Return the signature in Base64 encoded format
        return Base64.getEncoder().encodeToString(hashBytes);
    }

//===================================================================================================================================


	@GetMapping("/orders/incart/{customerId}")
	@Override
	public List<CustomerOrderDTO> getCartOrdersByCustomerId(@PathVariable Integer customerId)
			throws OrderException, Exception {
		List<Order> orders = orderService.getOrderByCustomerId(customerId);
		List<CustomerOrderDTO> customerorders = new ArrayList<>();

		for (Order order : orders) {

			if (order.getOrderStatus().equalsIgnoreCase("Incart")
					|| order.getOrderStatus().equalsIgnoreCase("In cart")) {

				CustomerOrderDTO customerorderdto = new CustomerOrderDTO();
				customerorderdto.setOrderId(order.getOrderId());
				customerorderdto.setOrderQuantity(order.getOrderQuantity());
				customerorderdto.setOrderStatus(order.getOrderStatus());

				Product product = order.getProduct();
				customerorderdto.setProductId(product.getProductId());
				customerorderdto.setProductName(product.getProductName());
				customerorderdto.setProductPrice(product.getProductPrice());
				customerorderdto.setImageUrl(product.getImageUrl());
				customerorderdto.setProductDescription(product.getProductDescription());
				customerorderdto.setProductQuantityType(product.getProductQuantityType());
				customerorderdto.setOrderRatingStatus(ratingService.getRatingByOrderId(customerId) + "");
				
				Seller seller = product.getSeller();
				customerorderdto.setSellerName(seller.getSellerFirstName() + " " + seller.getSellerLastName());
				customerorders.add(customerorderdto);
			}
		}
		return customerorders;
	}

	@GetMapping("/customer/{customerId}")
	@Override
	public List<CustomerOrderDTO> getOrdersByCustomerId(@PathVariable Integer customerId) throws OrderException {
		List<Order> orders = orderService.getOrderByCustomerId(customerId);
		List<CustomerOrderDTO> customerorders = new ArrayList<>();

		for (Order order : orders) {

			if (!order.getOrderStatus().equalsIgnoreCase("Incart")
					&& !order.getOrderStatus().equalsIgnoreCase("In cart")) {

				CustomerOrderDTO customerorderdto = new CustomerOrderDTO();
				customerorderdto.setOrderId(order.getOrderId());
				customerorderdto.setOrderQuantity(order.getOrderQuantity());

				Product product = order.getProduct();
				customerorderdto.setProductName(product.getProductName());
				customerorderdto.setProductPrice(product.getProductPrice());
				customerorderdto.setImageUrl(product.getImageUrl());
				customerorderdto.setProductDescription(product.getProductDescription());
				customerorderdto.setOrderStatus(order.getOrderStatus());
				customerorderdto.setProductQuantityType(product.getProductQuantityType());
				
				

				
				if(ratingService.getRatingByOrderId(order.getOrderId()))
				{
					customerorderdto.setOrderRatingStatus("Rated");
				}
				else
				{
					customerorderdto.setOrderRatingStatus("Not Rated");
					
				}
				
				Seller seller = product.getSeller();
				customerorderdto.setSellerName(seller.getSellerFirstName() + " " + seller.getSellerLastName());
				customerorders.add(customerorderdto);
			}
			
			
		}
		
		
		Collections.sort(customerorders, (order1, order2)->{
			if(order1.getOrderRatingStatus().equals(order2.getOrderRatingStatus()))
			{
				return order2.getOrderId()-order1.getOrderId();
				
			}
			return order1.getOrderRatingStatus().compareTo(order2.getOrderRatingStatus());
		});
		return customerorders;
	}

	@Override
	@PutMapping("/order/{orderId}/{orderStatus}")
	public String updateOrderStatus(@PathVariable Integer orderId, @PathVariable String orderStatus)
			throws Exception, OrderException {

		return orderService.updateOrderStatus(orderId, orderStatus);

	}
	
	@Override
	@PutMapping("/update/{orderId}/{orderQuantity}")
	public String updateOrderQuantity(@PathVariable Integer orderId,@PathVariable Double orderQuantity)
	{
		return orderService.updateOrderQuantity(orderId, orderQuantity);
	}
	
	

	@GetMapping("/seller/{sellerId}")
	@Override
	public List<SellerOrderDTO> getOrdersBySellerId(@PathVariable Integer sellerId) throws Exception, OrderException {
		List<SellerOrderDTO> sellerorders = new ArrayList<>();

		List<Order> orders = orderService.getOrdersBySellerId(sellerId);

		for (Order order : orders) {
			if (!order.getOrderStatus().equalsIgnoreCase("Incart")
					&& !order.getOrderStatus().equalsIgnoreCase("In cart") ) 
			{
				

				SellerOrderDTO sellerorderdto = new SellerOrderDTO();
				sellerorderdto.setOrderId(order.getOrderId());
				sellerorderdto.setOrderQuantity(order.getOrderQuantity());
				sellerorderdto.setOrderStatus(order.getOrderStatus());

				Product product = order.getProduct();

				sellerorderdto.setProductName(product.getProductName());
				sellerorderdto.setProductPrice(product.getProductPrice());
				sellerorderdto.setImageUrl(product.getImageUrl());
				sellerorderdto.setProductDescription(product.getProductDescription());
				sellerorderdto.setProductQuantityType(product.getProductQuantityType());

				if(order.getReportReason()!=null)
				{
					
					sellerorderdto.setOrderReportStatus("Reported");
					sellerorderdto.setReportReason(order.getReportReason());
				}
				Customer customer = order.getCustomer();
				sellerorderdto.setCustomerName(customer.getCustomerFirstName() + " " + customer.getCustomerLastName());

				
				sellerorders.add(sellerorderdto);

			}
		}
		
		Collections.sort(sellerorders, (order1,order2)->{
			if(order1.getOrderStatus().toLowerCase().equals(order2.getOrderStatus().toLowerCase()))
			{
				return order1.getOrderId()-order2.getOrderId();
			}
			return order2.getOrderStatus().toLowerCase().compareTo(order1.getOrderStatus().toLowerCase());
		});
		
		
		
		return sellerorders;
	}

	@PostMapping("/add")
	@Override
	public String addOrder(@RequestBody OrderDTO orderDTO) throws Exception, OrderException {
          Product product = productService.getProductById(orderDTO.getProductId());
        Customer customer = customerService.getCustomerById(orderDTO.getCustomerId());

        if (product == null) {
            throw new Exception("Product not found for ID: " + orderDTO.getProductId());
        }

        if (customer == null) {
            throw new Exception("Customer not found for ID: " + orderDTO.getCustomerId());
        }

		Order order = modelMapper.map(orderDTO, Order.class);
		order.setProduct(product);        // important
        order.setCustomer(customer);      // important
		order.setOrderStatus("Incart");

		
		return orderService.addOrder(order);
	}

	@DeleteMapping("/delete/{orderId}")
	@Override
	public String deleteOrder(@PathVariable Integer orderId) throws Exception, OrderException {
		orderService.deleteOrder(orderId);
		return "Order with ID " + orderId + " deleted successfully!";
	}

	@Override
	@GetMapping("/invoice/{orderId}")
	public CustomerOrderDTO getOrderInvoice(@PathVariable Integer orderId) throws Exception, OrderException {
		return orderService.getOrderInvoice(orderId);
	}
	
	@Override
	@PostMapping(value= "/report", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public String addOrderReport(@ModelAttribute OrderReport orderReport) throws Exception
	{
		return orderService.addOrderReport(orderReport);
	}
}
