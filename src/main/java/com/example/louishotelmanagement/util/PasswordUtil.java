package com.example.louishotelmanagement.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;

/**
 * Utility class để xử lý mật khẩu
 */
public class PasswordUtil {

    /**
     * Hash mật khẩu với salt
     */
    public static String hashPassword(String password) {
        try {
            // Tạo salt ngẫu nhiên
            SecureRandom random = new SecureRandom();
            byte[] salt = new byte[16];
            random.nextBytes(salt);

            // Hash password với salt
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.update(salt);
            byte[] hashedPassword = md.digest(password.getBytes());

            // Kết hợp salt và hash
            byte[] combined = new byte[salt.length + hashedPassword.length];
            System.arraycopy(salt, 0, combined, 0, salt.length);
            System.arraycopy(hashedPassword, 0, combined, salt.length, hashedPassword.length);

            return Base64.getEncoder().encodeToString(combined);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Error hashing password", e);
        }
    }

    /**
     * Kiểm tra mật khẩu có đúng không
     */
    public static boolean verifyPassword(String password, String hashedPassword) {
        try {
            // Decode base64
            byte[] combined = Base64.getDecoder().decode(hashedPassword);

            // Tách salt và hash
            byte[] salt = new byte[16];
            byte[] hash = new byte[32];
            System.arraycopy(combined, 0, salt, 0, 16);
            System.arraycopy(combined, 16, hash, 0, 32);

            // Hash password với salt
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.update(salt);
            byte[] testHash = md.digest(password.getBytes());

            // So sánh hash
            return MessageDigest.isEqual(hash, testHash);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Error verifying password", e);
        }
    }

    public static void main(String[] args) {
        System.out.printf("hash manager123: " + hashPassword("manager123"));
        System.out.printf("hash staff123: " + hashPassword("staff123"));
    }
}
