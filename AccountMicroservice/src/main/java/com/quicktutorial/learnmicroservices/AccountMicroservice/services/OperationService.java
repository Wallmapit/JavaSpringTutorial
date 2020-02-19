package com.quicktutorial.learnmicroservices.AccountMicroservice.services;

import com.quicktutorial.learnmicroservices.AccountMicroservice.entities.Account;
import com.quicktutorial.learnmicroservices.AccountMicroservice.entities.Operation;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface OperationService {

    List<Operation> getAllOperationPerAccount(String accountId);

    List<Account> getAllAccountsPerUser(String userId);

    Operation saveOperation(Operation operation);

}
