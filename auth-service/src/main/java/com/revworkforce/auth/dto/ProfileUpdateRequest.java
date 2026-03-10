package com.revworkforce.auth.dto;

public class ProfileUpdateRequest {

    private String phone;
    private String address;
    private String emergencyContact;

    public ProfileUpdateRequest() {
    }

    public ProfileUpdateRequest(String phone, String address, String emergencyContact) {
        this.phone = phone;
        this.address = address;
        this.emergencyContact = emergencyContact;
    }

    public String getPhone() { return phone; }
    public String getAddress() { return address; }
    public String getEmergencyContact() { return emergencyContact; }

    public void setPhone(String phone) { this.phone = phone; }
    public void setAddress(String address) { this.address = address; }
    public void setEmergencyContact(String emergencyContact) { this.emergencyContact = emergencyContact; }
}
