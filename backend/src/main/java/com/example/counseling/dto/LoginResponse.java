package com.example.counseling.dto;

public class LoginResponse {
    private Long id;
    private String account;
    private String name;
    private String role;
    private String status;

    public LoginResponse(Long id, String account, String name, String role, String status) {
        this.id = id;
        this.account = account;
        this.name = name;
        this.role = role;
        this.status = status;
    }

    public Long getId() { return id; }
    public String getAccount() { return account; }
    public String getName() { return name; }
    public String getRole() { return role; }
    public String getStatus() { return status; }
}
