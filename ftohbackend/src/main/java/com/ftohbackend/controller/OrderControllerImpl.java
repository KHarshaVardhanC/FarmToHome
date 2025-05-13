package com.ftohbackend.controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

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
import org.springframework.web.bind.annotation.RestController;

import com.ftohbackend.dto.CustomerOrderDTO;
import com.ftohbackend.dto.OrderDTO;
import com.ftohbackend.dto.OrderReport;
import com.ftohbackend.dto.PaymentVerificationRequest;
import com.ftohbackend.dto.PaymentVerificationRequest.Item;
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
	public List<CustomerOrderDTO> getAllOrders() throws Exception {
		List<Order> orders = orderService.getAllOrders();
		List<CustomerOrderDTO> customerorderdtos = new ArrayList<>();
		for (Order order : orders) {

			CustomerOrderDTO customerorderdto = new CustomerOrderDTO();
			customerorderdto.setOrderId(order.getOrderId());
			customerorderdto.setProductName(order.getProduct().getProductName());
			customerorderdto.setCustomerName(
					order.getCustomer().getCustomerFirstName() + " " + order.getCustomer().getCustomerLastName());
			customerorderdto.setOrderQuantity(order.getOrderQuantity());
			customerorderdto.setOrderStatus(order.getOrderStatus());
			if (order.getReportReason() != null) {
				customerorderdto.setOrderReportStatus("Reported");
			} else {
				customerorderdto.setOrderReportStatus("Not Reported");
			}
			customerorderdto.setSellerName(order.getProduct().getSeller().getSellerFirstName() + " "
					+ order.getProduct().getSeller().getSellerLastName());
			customerorderdtos.add(customerorderdto);

		}

		return customerorderdtos;
	}

	// @GetMapping("/{orderId}")
	@Override
	public Order getOrderById(@PathVariable Integer orderId) throws Exception {
		return orderService.getOrderById(orderId);
	}

	// ===============================================================================================================================

	@PostMapping("/payment/create")
	public ResponseEntity<?> createPaymentOrder(@RequestBody OrderDTO orderDTO) throws Exception {

//    	System.out.println("Hello Harsha1");

//    	System.out.println(orderDTO);
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

//            Order order = modelMapper.map(orderDTO, Order.class);
			Order order = orderService.getOrderById(orderDTO.getOrderId());
			order.setProduct(product);

			order.setCustomer(customer);
			order.setOrderStatus("PENDING");
			order.setPaymentStatus("CREATED");

			System.out.println("1xxxxxxxxx");

			RazorpayClient razorpay = new RazorpayClient(razorpayKey, razorpaySecret);

			String uuid = UUID.randomUUID().toString().replace("-", "").substring(0, 20);
			String receiptId = "rcpt_" + uuid;

			Double productPrice = order.getProduct().getProductPrice();
			if (productPrice == null) {
				throw new OrderException(
						"Product price cannot be null for product: " + order.getProduct().getProductId());
			}

			int amount = (int) (order.getOrderQuantity() * productPrice * 100);

			JSONObject options = new JSONObject();
			options.put("amount", amount);
			options.put("currency", "INR");
			options.put("receipt", receiptId);

			com.razorpay.Order razorpayOrder = razorpay.orders.create(options);

			order.setRazorpayOrderId(razorpayOrder.get("id"));
			order.setReceiptId(receiptId);
			orderService.updateOrderStatus(orderDTO.getOrderId(), "ordered");
//            productService.updateProduct(null, product)
			order.setOrderStatus("Ordered");
			order.setPaymentStatus("CREATED");

			Order savedOrder = orderService.saveOrder(order);
			System.out.println("2xxxxxxxxxxxx");
//            System.out.println(savedOrder);

			return ResponseEntity.ok(Map.of("orderId", razorpayOrder.get("id"), "razorpayKey", razorpayKey, "amount",
					razorpayOrder.get("amount"), "currency", "INR", "orderDBId", savedOrder.getOrderId()));

		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to create Razorpay order");
		}
	}

	@PostMapping("/payment/createAll/{customerId}")
	public ResponseEntity<?> createAllPaymentOrder(@PathVariable Integer customerId) throws Exception {

		List<Order> orders = orderService.getOrderByCustomerId(customerId);

		double totalAmount = 0;
		for (Order order : orders) {
			if (order.getOrderStatus().equalsIgnoreCase("incart")
					|| order.getOrderStatus().equalsIgnoreCase("in cart")) {
				totalAmount += order.getOrderPrice();
//                orderService.updateOrderStatus(order.getOrderId(), "ordered");

			}
		}

		RazorpayClient razorpay = new RazorpayClient(razorpayKey, razorpaySecret);

		String uuid = UUID.randomUUID().toString().replace("-", "").substring(0, 20);
		String receiptId = "rcpt_" + uuid;

		// Convert the total amount to paise (100x for INR)
		int amount = (int) (totalAmount * 100); // In paise

		// Razorpay options for creating an order
		JSONObject options = new JSONObject();
		options.put("amount", amount); // Amount in paise
		options.put("currency", "INR");
		options.put("receipt", receiptId);

		// Create the Razorpay order
		com.razorpay.Order razorpayOrder = razorpay.orders.create(options);

		// Step 6: Save the orders to the database
		for (Order order : orders) {
			if (order.getOrderStatus().equalsIgnoreCase("incart")
					|| order.getOrderStatus().equalsIgnoreCase("in cart")) {
			order.setRazorpayOrderId(razorpayOrder.get("id"));
			order.setReceiptId(receiptId);
			order.setOrderStatus("Ordered");
			order.setPaymentStatus("CREATED");
			orderService.updateOrderStatus(order.getOrderId(), "ordered");

			orderService.saveOrder(order);
			}

		}

		// Step 7: Return the response with Razorpay order details
		return ResponseEntity.ok(Map.of("orderId", razorpayOrder.get("id"), "razorpayKey", razorpayKey, "amount",
				razorpayOrder.get("amount"), "currency", "INR", "orderDBIds",
				orders.stream().map(Order::getOrderId).collect(Collectors.toList())));

	}

	

//    @PostMapping("/payment/verify")
//    public ResponseEntity<?> verifyPayment(@RequestBody PaymentVerificationRequest payload) {
//        try {
//        	
//        	Integer orderId=payload.getOrderId();
////            String orderId = payload.getRazorpayOrderId();
//            String paymentId = payload.getRazorpayPaymentId();
//            String signature = payload.getRazorpaySignature();
//
//            String generatedSignature ="";
////            = generateSignature(orderId, paymentId);
//            System.out.println(generatedSignature);
//            
//            System.out.println(signature);
//
//            if (generatedSignature.equals(generatedSignature)) {
//                Order order = orderService.getOrderById(orderId);
//
//                if (order != null) {
//                    order.setPaymentStatus("SUCCESS");
//                    order.setRazorpayPaymentId(paymentId);
//                    System.out.println("Ordered");
//
//                    order.setOrderStatus("ORDERED");
//                    orderService.saveOrder(order);
//
//                    // You may store other fields like amount, items, etc., if needed
//
//                    System.out.println("ordered ss");
//                    return ResponseEntity.ok("Payment verified successfully.");
//                } else {
//                	System.out.println("ordered ss1");
//                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Order not found.");
//                }
//            } 
//            else {
//                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid payment signature.");
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Payment verification failed.");
//        }
//    }
//    

//    @PostMapping("/payment/verifyAll")
//    public ResponseEntity<?> verifyPaymentForAll(@RequestBody PaymentVerificationRequest payload) {
//        try {
//            String orderId = payload.getRazorpayOrderId();
//            String paymentId = payload.getRazorpayPaymentId();
//            String signature = payload.getRazorpaySignature();
//
//            // Generate expected signature
//            String generatedSignature = generateSignature(orderId, paymentId);
//
//            // Check if the signature is valid
//            if (!generatedSignature.equals(signature)) {
//                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid payment signature.");
//            }
//
//            // Fetch all orders with the given Razorpay Order ID
//            List<Order> orders = orderService.getAllOrdersByRazorpayOrderId(orderId);
//
//            if (orders == null || orders.isEmpty()) {
//                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Orders not found.");
//            }
//
//            // Update each order
//            for (Order order : orders) {
//                order.setPaymentStatus("SUCCESS");
//                order.setRazorpayPaymentId(paymentId);
//                System.out.println("Ordered");
//                order.setOrderStatus("ORDERED");
//                orderService.saveOrder(order);
//            }
//
//            // Optional: handle items from payload.getItems() if needed
//
//            return ResponseEntity.ok("Bulk payment verified successfully.");
//        } catch (Exception e) {
//            e.printStackTrace();
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Bulk payment verification failed.");
//        }
//    }

	// Helper method to generate the expected signature for verification
//    private String generateSignature(String orderId, String paymentId) throws Exception {
//        String keySecret = razorpaySecret;  // Razorpay Secret Key
//        String data = orderId + "|" + paymentId;  // Concatenate orderId and paymentId
//
//        // Create HMAC using SHA256
//        SecretKeySpec keySpec = new SecretKeySpec(keySecret.getBytes(), "HmacSHA256");
//        Mac mac = Mac.getInstance("HmacSHA256");
//        mac.init(keySpec);
//
//        byte[] hashBytes = mac.doFinal(data.getBytes());
//
//        // Return the signature in Base64 encoded format
//        return Base64.getEncoder().encodeToString(hashBytes);
//    }

	private String generateSignature(String orderId, String paymentId) throws Exception {
		String keySecret = razorpaySecret;
		String data = orderId + "|" + paymentId;

		SecretKeySpec keySpec = new SecretKeySpec(keySecret.getBytes(), "HmacSHA256");
		Mac mac = Mac.getInstance("HmacSHA256");
		mac.init(keySpec);

		byte[] hashBytes = mac.doFinal(data.getBytes());

		// Convert to HEX string (not Base64!)
		StringBuilder hexString = new StringBuilder();
		for (byte b : hashBytes) {
			String hex = Integer.toHexString(0xff & b);
			if (hex.length() == 1)
				hexString.append('0');
			hexString.append(hex);
		}
		return hexString.toString();
	}

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
				System.out.println(product.getProductQuantityType());
				customerorderdto.setDiscountPercentage(product.getDiscountPercentage());
				customerorderdto.setMinOrderQuantity(product.getMinOrderQuantity());
//				if(product.getMinOrderQuantity() == null)
//				{
//					customerorderdto.setOrderPrice(customerorderdto.getProductPrice()*customerorderdto.getOrderQuantity());
//				}
//				else if(product.getMinOrderQuantity() <= order.)
//				{
//					customerorderdto.setOrderPrice(((100-customerorderdto.getDiscountPercentage())/100.0) * product.getProductPrice());
//				}
//				else
//				{
//					customerorderdto.setOrderPrice(product.getProductPrice());
//				}

				customerorderdto.setOrderPrice(order.getOrderPrice());
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

				if (ratingService.getRatingByOrderId(order.getOrderId())) {
					customerorderdto.setOrderRatingStatus("Rated");
				} else {
					customerorderdto.setOrderRatingStatus("Not Rated");

				}

				Seller seller = product.getSeller();
				customerorderdto.setSellerName(seller.getSellerFirstName() + " " + seller.getSellerLastName());
				customerorders.add(customerorderdto);
			}

		}

		Collections.sort(customerorders, (order1, order2) -> {
			if (order1.getOrderRatingStatus().equals(order2.getOrderRatingStatus())) {
				return order2.getOrderId() - order1.getOrderId();

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
	public String updateOrderQuantity(@PathVariable Integer orderId, @PathVariable Double orderQuantity) {
		return orderService.updateOrderQuantity(orderId, orderQuantity);
	}

	@GetMapping("/seller/{sellerId}")
	@Override
	public List<SellerOrderDTO> getOrdersBySellerId(@PathVariable Integer sellerId) throws Exception, OrderException {
		List<SellerOrderDTO> sellerorders = new ArrayList<>();

		List<Order> orders = orderService.getOrdersBySellerId(sellerId);

		for (Order order : orders) {
			if (!order.getOrderStatus().equalsIgnoreCase("Incart")
					&& !order.getOrderStatus().equalsIgnoreCase("In cart")) {

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

				if (order.getReportReason() != null) {

					sellerorderdto.setOrderStatus("Reported");
					sellerorderdto.setReportReason(order.getReportReason());
				}
				Customer customer = order.getCustomer();
				sellerorderdto.setCustomerName(customer.getCustomerFirstName() + " " + customer.getCustomerLastName());

				sellerorders.add(sellerorderdto);

			}
		}

		Collections.sort(sellerorders, (order1, order2) -> {
			if (order1.getOrderStatus().toLowerCase().equals(order2.getOrderStatus().toLowerCase())) {
				return order1.getOrderId() - order2.getOrderId();
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
		order.setProduct(product); // important
		order.setCustomer(customer); // important
		if (product.getMinOrderQuantity() == null) {
			order.setOrderPrice(product.getProductPrice() * order.getOrderQuantity());
		} else if (product.getMinOrderQuantity() == 1.0) {
			order.setOrderPrice(((100 - product.getDiscountPercentage()) / 100.0) * product.getProductPrice());
		} else {
			order.setOrderPrice(product.getProductPrice());
		}
//        order.setOrderPrice(product.getProductPrice());
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
	@PostMapping(value = "/report", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public String addOrderReport(@ModelAttribute OrderReport orderReport) throws Exception {
		return orderService.addOrderReport(orderReport);
	}
}
