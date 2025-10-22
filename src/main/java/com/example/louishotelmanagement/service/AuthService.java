package com.example.louishotelmanagement.service;

import com.example.louishotelmanagement.model.TaiKhoan;

/**
 * Service quản lý session và authentication
 */
public class AuthService {
    private static AuthService instance;
    private TaiKhoan currentUser;
    
    private AuthService() {}
    
    public static AuthService getInstance() {
        if (instance == null) {
            instance = new AuthService();
        }
        return instance;
    }
    
    public void setCurrentUser(TaiKhoan user) {
        this.currentUser = user;
    }
    
    public TaiKhoan getCurrentUser() {
        return currentUser;
    }
    
    public boolean isLoggedIn() {
        return currentUser != null;
    }
    
    public boolean hasRole(String role) {
        return currentUser != null && role.equals(currentUser.getQuyen());
    }
    
    public boolean isManager() {
        return hasRole("Manager");
    }
    
    public boolean isStaff() {
        return hasRole("Staff");
    }
    
    public void logout() {
        currentUser = null;
    }
    
    public String getCurrentUserRole() {
        return currentUser != null ? currentUser.getQuyen() : null;
    }
    
    public String getCurrentUserName() {
        return currentUser != null ? currentUser.getTenDangNhap() : null;
    }
}
