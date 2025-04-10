package com.ftohbackend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ftohbackend.model.Mails;


@Repository
public interface MailRepository extends JpaRepository<Mails, String> {



}
