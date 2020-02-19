package com.quicktutorial.learnmicroservices.AccountMicroservice.daos;

import com.quicktutorial.learnmicroservices.AccountMicroservice.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserDao extends JpaRepository<User, String>{
    //custom
    Optional<User> findByID(String id);
}