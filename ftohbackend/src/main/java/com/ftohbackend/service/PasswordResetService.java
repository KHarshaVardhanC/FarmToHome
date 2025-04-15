
package com.ftohbackend.service;

import org.springframework.stereotype.Service;

import com.ftohbackend.model.Customer;
import com.ftohbackend.model.Seller;

@Service
public class PasswordResetService {
	

	public void updatePassword(Object user, String newPassword) {
		if (user instanceof Customer) {
			((Customer) user).setCustomerPassword(newPassword);
		} else if (user instanceof Seller) {
			((Seller) user).setSellerPassword(newPassword);
		} else {
			throw new IllegalArgumentException("Unsupported user type");
		}
	}
}
