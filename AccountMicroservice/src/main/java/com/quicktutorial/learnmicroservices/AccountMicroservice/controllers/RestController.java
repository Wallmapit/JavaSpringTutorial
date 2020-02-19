package com.quicktutorial.learnmicroservices.AccountMicroservice.controllers;

import com.quicktutorial.learnmicroservices.AccountMicroservice.entities.Operation;
import com.quicktutorial.learnmicroservices.AccountMicroservice.entities.User;
import com.quicktutorial.learnmicroservices.AccountMicroservice.services.LoginService;
import com.quicktutorial.learnmicroservices.AccountMicroservice.services.OperationService;
import com.quicktutorial.learnmicroservices.AccountMicroservice.utils.UserNotLoggedException;
import io.jsonwebtoken.ExpiredJwtException;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;


import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.Map;
import java.util.Optional;

import static org.springframework.web.bind.annotation.RequestMethod.POST;

@org.springframework.web.bind.annotation.RestController
public class RestController {

    private static final Logger log = LoggerFactory.getLogger(RestController.class);

    @Autowired
    LoginService loginService;

    @Autowired
    OperationService operationService;

    @RequestMapping("/hello")
    public String sayHello(){
        return "Hello everyone!";
    }

    @RequestMapping("/newuser1")
    public String addUser (User user) {
        return "User added correctly: " + user.getId() + ", " + user.getUsername();
    }

    @RequestMapping("/newuser2")
    public String addValidUser (@Valid User user) {
        return "User added correctly: " + user.getId() + ", " + user.getUsername();
    }

    @RequestMapping("/newuser3")
    public String addValidUserPlusBinding (@Valid User user, BindingResult result) {
        if (result.hasErrors()) {
            return result.toString();
        }
        return "User added correctly: " + user.getId() + ", " + user.getUsername();
    }

    @RequestMapping("/newuser4")
    public String addValidUserPlusBinding2 (User user, BindingResult result) {
        UserValidator userValidator = new UserValidator();
        userValidator.validate(user, result);

        if (result.hasErrors()) {
            return result.toString();
        }
        return "User added correctly: " + user.getId() + ", " + user.getUsername();
    }

    private class UserValidator implements Validator {

        @Override
        public boolean supports(Class<?> clazz) {
            return User.class.equals(clazz);
        }

        @Override
        public void validate(Object obj, Errors errors) {
            User user = (User) obj;
            if (user .getPassword().length() < 8) {
                errors.rejectValue("password", "password must be at leat 8 char long!");
            }
        }
    }

    //-------------------------------------FINE PROVE---------------------------------

    @AllArgsConstructor
    public class JsonResponseBody {
        @Getter @Setter
        private int server;
        @Getter @Setter
        private Object response;
    }

    @RequestMapping(value ="/login", method = POST)
    public ResponseEntity<JsonResponseBody> loginUser(@RequestParam(value="id") String id, @RequestParam(value="password") String pwd){
        //check if user exists in DB
        try {
            Optional<User> userr = loginService.getUserFromDbAndVerifyPassword(id, pwd);
            if (userr.isPresent()) {
                User user = userr.get();
                String jwt = loginService.createJwt(user.getId(), user.getUsername(), user.getPermission(), new Date());
                return ResponseEntity.status(HttpStatus.OK).header("jwt").body(new JsonResponseBody(HttpStatus.OK.value(), "Success! User logged in!"));
            }
        } catch (UserNotLoggedException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body( new JsonResponseBody(HttpStatus.FORBIDDEN.value(), "Login Failed. Wrong Credentials." + e.toString()));
        } catch (UnsupportedEncodingException e1) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body( new JsonResponseBody(HttpStatus.FORBIDDEN.value(), "Token Error" + e1.toString()));
        }
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body( new JsonResponseBody(HttpStatus.FORBIDDEN.value(), "No corrispondences in the DB!"));
    }

    @RequestMapping("/operations/account/{account}")
    public ResponseEntity<JsonResponseBody> fetchAllOperationsPerAccount(HttpServletRequest request, @PathVariable(name = "account") String account) {
        try {
            loginService.verifyJwtAndGetData(request);
            return ResponseEntity.status(HttpStatus.OK).body(new JsonResponseBody(HttpStatus.OK.value(), operationService.getAllOperationPerAccount(account)));
        } catch (UserNotLoggedException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body( new JsonResponseBody(HttpStatus.FORBIDDEN.value(), "User not correctly logged in!" + e.toString()));
        } catch (UnsupportedEncodingException e1) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body( new JsonResponseBody(HttpStatus.FORBIDDEN.value(), "Unsupported Encoding" + e1.toString()));
        } catch (ExpiredJwtException e2){
            return ResponseEntity.status(HttpStatus.GATEWAY_TIMEOUT).body( new JsonResponseBody(HttpStatus.GATEWAY_TIMEOUT.value(), "Session Expired" + e2.toString()));
        }
    }

    @RequestMapping(value = "/accounts/user", method = POST)
    public ResponseEntity<JsonResponseBody> fetchAllAccountsPerUser (HttpServletRequest request) {
        try {
            Map<String, Object> userData = loginService.verifyJwtAndGetData(request);
            return ResponseEntity.status(HttpStatus.OK).body(new JsonResponseBody(HttpStatus.OK.value(), operationService.getAllAccountsPerUser((String) userData.get("subject"))));
        } catch (UserNotLoggedException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body( new JsonResponseBody(HttpStatus.FORBIDDEN.value(), "User not correctly logged in! " + e.toString()));
        } catch (UnsupportedEncodingException e1) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body( new JsonResponseBody(HttpStatus.FORBIDDEN.value(), "Unsupported Encoding: " + e1.toString()));
        } catch (ExpiredJwtException e2){
            return ResponseEntity.status(HttpStatus.GATEWAY_TIMEOUT).body( new JsonResponseBody(HttpStatus.GATEWAY_TIMEOUT.value(), "Session Expired" + e2.toString()));
        }

    }

    @RequestMapping(value = "/operations/add", method = POST)
    public ResponseEntity<JsonResponseBody> addOperation (HttpServletRequest request, @Valid Operation operation, BindingResult bindingResult) {
        if(bindingResult.hasErrors()){
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new JsonResponseBody(HttpStatus.FORBIDDEN.value(), "Invalid Format of data"));
        }
        try {
            loginService.verifyJwtAndGetData(request);
            return ResponseEntity.status(HttpStatus.OK).body(new JsonResponseBody(HttpStatus.OK.value(), operationService.saveOperation(operation)));
        } catch (UserNotLoggedException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body( new JsonResponseBody(HttpStatus.FORBIDDEN.value(), "User not correctly logged in! " + e.toString()));
        } catch (UnsupportedEncodingException e1) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body( new JsonResponseBody(HttpStatus.FORBIDDEN.value(), "Unsupported Encoding: " + e1.toString()));
        } catch (ExpiredJwtException e2){
            return ResponseEntity.status(HttpStatus.GATEWAY_TIMEOUT).body( new JsonResponseBody(HttpStatus.GATEWAY_TIMEOUT.value(), "Session Expired" + e2.toString()));
        }
    }
}
