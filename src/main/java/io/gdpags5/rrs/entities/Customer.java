package io.gdpags5.rrs.entities;

import io.gdpags5.rrs.commons.BaseEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "rrs_customer")
public class Customer extends BaseEntity {
    @Column(name = "full_name", length = 100, nullable = false)
    private String fullName;

    @Column(name = "phone_number", nullable = false)
    private Long phoneNumber;

    @Column(name = "email", length = 100, nullable = false)
    private String email;

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public Long getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(Long phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
