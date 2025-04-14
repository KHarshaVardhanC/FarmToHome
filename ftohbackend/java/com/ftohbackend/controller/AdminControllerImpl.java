package com.ftohbackend.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ftohbackend.dto.LoginRequest;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/admin")
public class AdminControllerImpl {

	@PostMapping("/login")
	public ResponseEntity<?> adminLogin(@RequestBody LoginRequest loginRequest) {
		if (loginRequest.getEmail().equals("villagecart@gmail.com") && loginRequest.getPassword().equals("0000")) {

			// Create and return a response with seller data (excluding password)
			return ResponseEntity.ok(new AdminResponse(1, "villagecart@gmail.com"));
		} else {
			return ResponseEntity.status(401).body("Invalid email or password");
		}

	}

	public static class AdminResponse {
		private Integer adminId;
		private String adminEmail;

		// ... other non-sensitive fields ...

		public AdminResponse(Integer adminId, String adminEmail) {
			this.adminId = adminId;
			this.adminEmail = adminEmail;
		}

		// Getters
		public Integer getAdminId() {
			return adminId;
		}

		public String getAdminEmail() {
			return adminEmail;
		}

	}

}
