package com.quicktutorial.learnmicroservices.AccountMicroservice.services;

import com.quicktutorial.learnmicroservices.AccountMicroservice.daos.UserDao;
import com.quicktutorial.learnmicroservices.AccountMicroservice.entities.User;

import com.quicktutorial.learnmicroservices.AccountMicroservice.utils.EncryptionUtils;
import com.quicktutorial.learnmicroservices.AccountMicroservice.utils.JwtUtils;
import com.quicktutorial.learnmicroservices.AccountMicroservice.utils.UserNotLoggedException;
import io.jsonwebtoken.Jwt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.Map;
import java.util.Optional;


@Service
public class LoginServiceImpl implements LoginService{

    private static final Logger log = LoggerFactory.getLogger(LoginServiceImpl.class);

    @Autowired
    UserDao userDao;

    @Autowired
    EncryptionUtils encryptionUtils;

    @Override
    public Optional<User> getUserFromDbAndVerifyPassword(String id, String password) throws UserNotLoggedException {

        Optional<User> userr = userDao.findByID(id);
        if(userr.isPresent()){
            User user = userr.get();
            if(encryptionUtils.decrypt(user.getPassword()).equals(password)) {
                log.info("Username and Password verified");
            } else {
                log.info("Username verified. Passoword not verified");
                throw new UserNotLoggedException("User not correctly logged in");
            }
        }

        return userr;
    }

    @Override
    public String createJwt(String subject, String name,  String permission, Date date) throws UnsupportedEncodingException {

        Date expDate = date;
        expDate.setTime(date.getTime() + (300*1000) );
        log.info("JWT Creation. Expiration Time: " + expDate.getTime());

        String token = JwtUtils.generateJwt(subject, expDate, name, permission);

        return token;
    }

    @Override
    public Map<String , Object> verifyJwtAndGetData(HttpServletRequest request) throws UserNotLoggedException, UnsupportedEncodingException {

        String jwt = JwtUtils.getJwtFromHttpRequest(request);
        if(jwt == null) {
            throw new UserNotLoggedException("Authentication Token not found in the request");
        }
        Map<String, Object> userData = JwtUtils.jwt2Map(jwt);

        return userData;
    }
}
