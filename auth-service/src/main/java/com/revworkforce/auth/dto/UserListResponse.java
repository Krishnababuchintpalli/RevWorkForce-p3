package com.revworkforce.auth.dto;

import com.revworkforce.auth.entity.User;
import com.revworkforce.auth.enums.Role;

public class UserListResponse {

    private Long id;
    private String employeeId;
    private String fullName;
    private String email;
    private String role;
    private boolean active;
    private String phone;
    private String address;
    private String emergencyContact;

    public UserListResponse() {
    }

    public UserListResponse(User user) {
        this.id = user.getId();
        this.employeeId = user.getEmployeeId();
        this.fullName = user.getFullName();
        this.email = user.getEmail();
        this.role = Role.fromId(user.getRoleId()).name();
        this.active = "ACTIVE".equals(user.getStatus());
        this.phone = user.getPhone();
        this.address = user.getAddress();
        this.emergencyContact = user.getEmergencyContact();
    }

    public Long getId() { return id; }
    public String getEmployeeId() { return employeeId; }
    public String getFullName() { return fullName; }
    public String getEmail() { return email; }
    public String getRole() { return role; }
    public boolean isActive() { return active; }
    public String getPhone() { return phone; }
    public String getAddress() { return address; }
    public String getEmergencyContact() { return emergencyContact; }

    public void setId(Long id) { this.id = id; }
    public void setEmployeeId(String employeeId) { this.employeeId = employeeId; }
    public void setFullName(String fullName) { this.fullName = fullName; }
    public void setEmail(String email) { this.email = email; }
    public void setRole(String role) { this.role = role; }
    public void setActive(boolean active) { this.active = active; }
    public void setPhone(String phone) { this.phone = phone; }
    public void setAddress(String address) { this.address = address; }
    public void setEmergencyContact(String emergencyContact) { this.emergencyContact = emergencyContact; }
}
