
package com.ftohbackend.service;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ftohbackend.model.Customer;
import com.ftohbackend.model.Seller;

@Service
@Transactional
public class PasswordResetService {
	

	public void updatePassword(Object user, String newPassword) {
		
		BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
		if (user instanceof Customer) {
			
			
			((Customer) user).setCustomerPassword(passwordEncoder.encode(newPassword) );
		} else if (user instanceof Seller) {
			((Seller) user).setSellerPassword(passwordEncoder.encode(newPassword));
		} else {
			throw new IllegalArgumentException("Unsupported user type");
		}
	}
}
