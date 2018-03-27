package edu.odu.cs.gold.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.data.annotation.Transient;
import org.springframework.security.core.userdetails.UserDetails;
import sun.security.util.Password;

import java.io.Serializable;
import java.util.UUID;


public class User implements Serializable{

    private String userKey;
    private String email;
    private String userName;
    private String password;
    private String firstName;
    private String lastName;
    private String roleType;
    private String roleTypeKey;
    private boolean enabled;
    private String confirmationToken;

    public User() { }

    public User(String email,
                String userName,
                String password,
                String firstName,
                String lastName,
                String roleType,
                String roleTypeKey,
                boolean enabled,
                String confirmationToken) {
        this.email = email;
        this.userName = userName;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.roleTypeKey = roleTypeKey;
        this.roleType = roleType;
        this.enabled = enabled;
        this.confirmationToken = confirmationToken;
    }

    public void generateUserKey() {
        this.userKey = UUID.randomUUID().toString();
    }

    public void generateConfirmationToken() { this.confirmationToken = UUID.randomUUID().toString(); }

    public void setConfirmationToken(String confirmationToken) {
        this.confirmationToken = confirmationToken;
    }

    public String getConfirmationToken() {
        return confirmationToken;
    }

    public String getUserKey() {
        return userKey;
    }

    public void setUserKey(String userKey) {
        this.userKey = userKey;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public boolean getEnabled() {
        return enabled;
    }

    public String getRoleType() { return roleType; }

    public void setRole(String roleType) { this.roleType = roleType; }

    public void setEnabled(boolean value) {
        this.enabled = value;
    }

    @Override
    public String toString() {
        return "User{" +
                "userKey='" + userKey + '\'' +
                ", confirmationToken='" + confirmationToken + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", userName='" + userName + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", roleTypeKey='" + roleTypeKey + '\'' +
                ", roleType='" + roleType + '\'' +
                ", enabled=" + enabled +
                '}';
    }
}