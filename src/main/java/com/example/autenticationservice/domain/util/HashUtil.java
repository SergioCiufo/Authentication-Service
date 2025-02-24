package com.example.autenticationservice.domain.util;

import org.springframework.stereotype.Component;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

@Component
public class HashUtil {
    //SHA-1 è un algoritmo che prende una stringa e la trasforma in una sequenza di byte (hash)
    public String stringToSha1(String password) {
        try{
            MessageDigest messageDigest = MessageDigest.getInstance("SHA-1"); //classe per calcolare l'hash e getIstance scegliamo che tipo di hash usare
            byte[] hashBytes = messageDigest.digest(password.getBytes()); //conversione in un array di byte
            return Base64.getEncoder().encodeToString(hashBytes); //codifica l'array dui byte in una stringa in formato base64
        } catch (NoSuchAlgorithmException e){ //errore raro, in caso l'algoritmo non sia disponibile
            throw new RuntimeException("SHA-1 Hash Failed");
        }

    }
}
