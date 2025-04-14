package com.ftohbackend.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Entity
@Table(name = "mail")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Mails {

	
	@Id
	@Column(name="mailId", nullable = false, unique = true)
	String mailId;
	
	
}
