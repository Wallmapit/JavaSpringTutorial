package com.quicktutorial.learnmicroservices.AccountMicroservice.utils;

import org.jasypt.util.text.BasicTextEncryptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * This class provides a series of method in order to encrypt and decript user passwords. it uses jasypt library
 */
@Component
public class EncryptionUtils {

    @Autowired
    BasicTextEncryptor textEncryptor;

    /*
    this method provides an encryption of the String passed in input
    */
    public String encrypt(String data){
        return textEncryptor.encrypt(data);
    }

    /*
    this method provides a decryption of the String passed in input
    */
    public String decrypt(String encriptedData){
        return textEncryptor.decrypt(encriptedData);
    }

}