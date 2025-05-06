package com.ftohbackend.controller;

import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
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
import org.springframework.web.bind.annotation.RestController;

import com.ftohbackend.dto.CustomerProductDTO;
import com.ftohbackend.dto.ProductCity;
import com.ftohbackend.dto.ProductDTO;
import com.ftohbackend.dto.ProductRequest;
import com.ftohbackend.dto.SearchRequestDTO;
import com.ftohbackend.dto.SellerProductDTO;
import com.ftohbackend.exception.ProductException;
import com.ftohbackend.model.Product;
import com.ftohbackend.service.ProductService;
import com.ftohbackend.service.ProductServiceImpl;

import jakarta.validation.Valid;


@CrossOrigin(origins = "*")
@RestController
public class ProductControllerImpl implements ProductController {

	@Autowired
	ModelMapper modelMapper;

	@Autowired
	ProductService productService;

	@Autowired
	ProductServiceImpl productServiceImpl;


	@PostMapping(value = "/product", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<ProductDTO> addProduct( @Valid @ModelAttribute ProductRequest productRequest)
			throws Exception {

		ProductDTO productDTO = productService.addProduct(productRequest);
		return ResponseEntity.status(HttpStatus.CREATED).body(productDTO);
	}


	@Override
	@GetMapping("/product2/{productId}")
	public CustomerProductDTO getCustomerProductByProductId(@PathVariable Integer productId) throws ProductException
	{
		Product product=productService.getProduct(productId);
		CustomerProductDTO customerProductDTO=new CustomerProductDTO();
		
		customerProductDTO.setProductId(productId);
		customerProductDTO.setProductName(product.getProductName());
		customerProductDTO.setProductPrice(product.getProductPrice());
		customerProductDTO.setProductDescription(product.getProductDescription());
		customerProductDTO.setProductQuantity(product.getProductQuantity());
		customerProductDTO.setProductQuantityType(product.getProductQuantityType());
		customerProductDTO.setImageUrl(product.getImageUrl());
		customerProductDTO.setProductRatingValue(product.getProductRatingValue());
		customerProductDTO.setProductRatingCount(product.getProductRatingCount());
		customerProductDTO.setDiscountPercentage(product.getDiscountPercentage());
		customerProductDTO.setMinOrderQuantity(product.getMinOrderQuantity());
		
		
		customerProductDTO.setSellerPlace(product.getSeller().getSellerPlace());
		customerProductDTO.setSellerCity(product.getSeller().getSellerCity());
		customerProductDTO.setSellerName(product.getSeller().getSellerFirstName() + " "+product.getSeller().getSellerLastName());
		return customerProductDTO;
	}
	
	
//	public List<CustomerProductDTO> getProductByNameAndCity(SearchRequestDTO searchRequestDTO)
//	{
//		List<Product> products= productService.getAllProductByNameAndCity(searchRequestDTO);
//		List<CustomerProductDTO> customerproductdtos=new ArrayList<>();
//		
//	}
	

	@GetMapping("/product1/{productName}")
	@Override
	public List<CustomerProductDTO> getProductByName(@PathVariable String productName) throws Exception,ProductException {
		
		
		List<Product> products = productService.searchProductsWithSellerDetails(productName);
		if (products == null || products.isEmpty()) {
			throw new ProductException("No products found with name: " + productName);
		}

		List<CustomerProductDTO> customerproductdto = new ArrayList<>();
		for (Product product : products) {
			if (product.getProductQuantity() != 0.0) {

				CustomerProductDTO customerProductDTO = new CustomerProductDTO();

				customerProductDTO.setImageUrl(product.getImageUrl());
				customerProductDTO.setProductDescription(product.getProductDescription());
				customerProductDTO.setProductId(product.getProductId());
				customerProductDTO.setProductPrice(product.getProductPrice());
				customerProductDTO.setProductName(product.getProductName());
				customerProductDTO.setProductQuantity(product.getProductQuantity());
				customerProductDTO.setProductQuantityType(product.getProductQuantityType());
				customerProductDTO.setProductRatingCount(product.getProductRatingCount());
				customerProductDTO.setProductRatingValue(product.getProductRatingValue());
				customerProductDTO.setSellerName(product.getSeller().getSellerFirstName() + " " + product.getSeller().getSellerLastName());
				customerProductDTO.setSellerCity(product.getSeller().getSellerCity());
				customerProductDTO.setSellerPlace(product.getSeller().getSellerPlace());
				customerProductDTO.setDiscountPercentage(product.getDiscountPercentage());
				customerProductDTO.setMinOrderQuantity(product.getMinOrderQuantity());

				customerproductdto.add(customerProductDTO);
			}
		}

		return customerproductdto;
		
	}

	@Override
	@PostMapping("/NameCity") 
	public List<CustomerProductDTO> getProductByNameAndCity(@RequestBody ProductCity productCity) throws Exception
	{
		List<Product> products=new ArrayList<>();
		if(productCity.getCityName() == null || productCity.getCityName().isEmpty())
		{
			products=productService.searchProductsWithSellerDetails(productCity.getProductName());
		}
		else
		{
			products=productService.searchProductsWithSellerDetails(productCity);
		}
		
		List<CustomerProductDTO> customerproductdtos=new ArrayList<CustomerProductDTO>();
		for(Product product: products)
		{
			CustomerProductDTO customerProductDTO = new CustomerProductDTO();

			customerProductDTO.setImageUrl(product.getImageUrl());
			customerProductDTO.setProductDescription(product.getProductDescription());
			customerProductDTO.setProductId(product.getProductId());
			customerProductDTO.setProductPrice(product.getProductPrice());
			customerProductDTO.setProductName(product.getProductName());
			customerProductDTO.setProductQuantity(product.getProductQuantity());
			customerProductDTO.setProductQuantityType(product.getProductQuantityType());
			customerProductDTO.setProductRatingCount(product.getProductRatingCount());
			customerProductDTO.setProductRatingValue(product.getProductRatingValue());
			customerProductDTO.setSellerName(product.getSeller().getSellerFirstName() + " " + product.getSeller().getSellerLastName());
			customerProductDTO.setSellerCity(product.getSeller().getSellerCity());
			customerProductDTO.setSellerPlace(product.getSeller().getSellerPlace());
			customerProductDTO.setDiscountPercentage(product.getDiscountPercentage());
			customerProductDTO.setMinOrderQuantity(product.getMinOrderQuantity());
			

			customerproductdtos.add(customerProductDTO);
			
		}
		
		return customerproductdtos;
	}
	
	@PutMapping("/product/{productId}")
	@Override
	public String updateProduct(@PathVariable Integer productId, @RequestBody ProductDTO updatedDetails)
			throws Exception {
		Product prod = modelMapper.map(updatedDetails, Product.class);
		return productService.updateProduct(productId, prod);
	}

	@DeleteMapping("/product/{productId}")
	@Override
	public String deleteProduct(@PathVariable Integer productId) throws ProductException {
		return productService.deleteProduct(productId);
	}

	@GetMapping("/products")
	@Override
	public List<ProductDTO> getAllProducts() throws ProductException{
		return productService.getAllProduct().stream().map(product -> modelMapper.map(product, ProductDTO.class)).toList();
		
	}
	
	@GetMapping("products/{productCategory}")
	public List<ProductDTO> getCategoryProducts(@PathVariable String productCategory) throws ProductException
	{
		return productService.getCategoryProducts(productCategory).stream().map(product -> modelMapper.map(product, ProductDTO.class)).toList();
	}
	
	@GetMapping("/product/{sellerId}")
	@Override
	public List<SellerProductDTO> getProducts(@PathVariable Integer sellerId) throws ProductException {
		// TODO Auto-generated method stub

		List<Product> products = productService.getAllProduct(sellerId);

		List<SellerProductDTO> sellerproductdtos = new ArrayList<>();
		for (Product product : products) {
			SellerProductDTO sellerproductDTO = new SellerProductDTO();
			sellerproductDTO.setProductId(product.getProductId());
			sellerproductDTO.setProductName(product.getProductName());
			sellerproductDTO.setProductQuantity(product.getProductQuantity());
			sellerproductDTO.setProductQuantityType(product.getProductQuantityType());
			sellerproductDTO.setProductPrice(product.getProductPrice());
			sellerproductDTO.setImageUrl(product.getImageUrl());
			sellerproductDTO.setProductDescription(product.getProductDescription());
			if(product.getProductRatingValue() == null)
			{  
				sellerproductDTO.setProductRatingValue(0.0);
				sellerproductDTO.setProductRatingCount(0);
				
			}
			else
			{
				
				sellerproductDTO.setProductRatingValue(product.getProductRatingValue());
				sellerproductDTO.setProductRatingCount(product.getProductRatingCount());
			}
			
			sellerproductDTO.setDiscountPercentage(product.getDiscountPercentage());
			sellerproductDTO.setMinOrderQuantity(product.getMinOrderQuantity());
			
			sellerproductdtos.add(sellerproductDTO);
			
		}

		return sellerproductdtos;

	}

	@GetMapping("/{productId}")
	@Override
	public ProductDTO getProduct(@PathVariable Integer productId) throws ProductException {
		return modelMapper.map(productService.getProduct(productId), ProductDTO.class);
	}




}
