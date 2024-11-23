package com.example.fileform.util;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MD5Generator {

    private String result;

    public MD5Generator(String input) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        MessageDigest mdMD5 = MessageDigest.getInstance("MD5");
        mdMD5.update(input.getBytes("UTF-8"));
        byte[] md5Hash = mdMD5.digest();

        StringBuffer hexMD5Hash = new StringBuffer();
        for(byte b : md5Hash) {
            String hexString = String.format("%02x", b);
            hexMD5Hash.append(hexString);
        }
        result = hexMD5Hash.toString();
    }

    public String toString() {
        return result;
    }
}
