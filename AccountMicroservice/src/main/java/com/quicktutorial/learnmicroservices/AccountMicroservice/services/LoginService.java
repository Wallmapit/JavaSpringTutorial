package com.quicktutorial.learnmicroservices.AccountMicroservice.services;

import com.quicktutorial.learnmicroservices.AccountMicroservice.entities.User;
import com.quicktutorial.learnmicroservices.AccountMicroservice.utils.UserNotLoggedException;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.Map;
import java.util.Optional;

@Service
public interface LoginService {

    Optional<User> getUserFromDbAndVerifyPassword(String id, String password) throws UserNotLoggedException;

    String createJwt(String subject, String name,  String permission, Date date) throws UnsupportedEncodingException;

    Map<String , Object> verifyJwtAndGetData(HttpServletRequest request) throws UserNotLoggedException, UnsupportedEncodingException;

}
