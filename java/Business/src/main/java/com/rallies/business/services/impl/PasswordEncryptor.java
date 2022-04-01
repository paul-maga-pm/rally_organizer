package com.rallies.business.services.impl;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

class PasswordEncryptor {
    Logger logger = LogManager.getLogger(PasswordEncryptor.class);
    public PasswordEncryptor(){
    }
    
    public boolean authenticate(String password, String hashedPassword){
        return hashedPassword.equals(hash(password));
    }

    public String hash(String password)  {
        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance("SHA-512");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        byte [] inputBytes = new byte[0];
        try {
            inputBytes = password.getBytes("UTF-16LE");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        byte[] digest = md.digest(inputBytes);
        return Base64.getEncoder().encodeToString(digest);
    }
}
