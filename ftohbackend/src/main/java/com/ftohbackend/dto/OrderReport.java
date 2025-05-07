package com.ftohbackend.dto;

import org.springframework.web.multipart.MultipartFile;

import lombok.Data;

@Data
public class OrderReport {

	Integer orderId;
	MultipartFile orderImage;
	String reportReason;
}
