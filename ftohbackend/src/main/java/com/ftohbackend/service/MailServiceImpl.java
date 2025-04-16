package com.ftohbackend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ftohbackend.model.Mails;
import com.ftohbackend.repository.MailRepository;

import jakarta.validation.Valid;

@Service
@Transactional
public class MailServiceImpl {

	@Autowired
	MailRepository mailRepository;
	
//	@Override
	public Boolean isMailExists(@Valid String mail)
	{
		
//		System.out.println(mailRepository.save(mails));
		return mailRepository.existsById(mail);
		
		
	}
	
	public void addMail(Mails mails)
	{
		
		mailRepository.save(mails);
		
	}
}
