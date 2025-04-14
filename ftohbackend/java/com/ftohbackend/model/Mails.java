package com.ftohbackend.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;


@Entity
@Table(name = "mail")
public class Mails {

	
	@Id
	@Column(name="mailId", nullable = false, unique = true)
	String mailId;
	
	public Mails() {}
	
	public Mails(String mailId)
	{
		this.mailId=mailId;
	}

	public String getMailId() {
		return mailId;
	}

	public void setMailId(String mailId) {
		this.mailId = mailId;
	}

	@Override
	public String toString() {
		return "Mails [mailId=" + mailId + "]";
	}
	
}
