package com.example.bottonnaviagtion.ui.dashboard;

import android.util.Log;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Sha256 {
    private static String bytesToHexString(byte[] bytes) {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < bytes.length; i++) {
            String hex = Integer.toHexString(0xFF & bytes[i]);
            if (hex.length() == 1) {
                sb.append('0');
            }
            sb.append(hex);
        }
        return sb.toString();
    }

    static String getSha(String password) {
        MessageDigest digest = null;
        String hash = null;
        try {
            digest = MessageDigest.getInstance("SHA-256");
            digest.update(password.getBytes());

            hash = bytesToHexString(digest.digest());

            Log.i("Eamorr", "result is " + hash);
        } catch (NoSuchAlgorithmException e1) {
            e1.printStackTrace();
        }
        return hash;
    }
}