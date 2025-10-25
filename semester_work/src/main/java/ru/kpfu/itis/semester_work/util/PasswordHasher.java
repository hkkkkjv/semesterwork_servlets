package ru.kpfu.itis.semester_work.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class PasswordHasher {
    public String hashPassword(String password){
        try{
            MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
            byte[] hashedBytes = messageDigest.digest(password.getBytes());
            StringBuilder stringBuilder = new StringBuilder();
            for (byte b:hashedBytes){
                stringBuilder.append(String.format("%02x",b));
            }
            return stringBuilder.toString();
        }catch (NoSuchAlgorithmException e){
            throw  new RuntimeException("Error hashing password",e);
        }
    }
    public boolean verifyPassword(String inputPassword, String storedHash) {
        String hashedInputPassword = hashPassword(inputPassword);
        return hashedInputPassword.equals(storedHash);
    }
}
