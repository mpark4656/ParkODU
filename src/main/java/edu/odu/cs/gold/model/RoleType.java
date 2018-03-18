package edu.odu.cs.gold.model;

import java.io.Serializable;
import java.util.UUID;

/*
 *
 * Current RoleType Types:
 * - user
 * - admin
 *
 */

public class RoleType implements Serializable{

    private String roleKey;
    private String name;

    public RoleType() {
        this.roleKey = UUID.randomUUID().toString();
    }

    public RoleType(String name) {
        this.name = name;
    }

    public String getRoleKey() {
        return roleKey;
    }

    public void setRoleKey(String roleKey) {
        this.roleKey = roleKey;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "RoleType{" +
                "roleKey='" + roleKey + '\'' +
                ", name='" + name + '\'' +
                '}';
    }

}
