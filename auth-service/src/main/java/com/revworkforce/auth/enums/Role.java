package com.revworkforce.auth.enums;

public enum Role {
    EMPLOYEE(1),
    MANAGER(2),
    ADMIN(3);

    private final Integer id;

    Role(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public static Role fromId(Integer id) {
        for (Role role : Role.values()) {
            if (role.getId().equals(id)) {
                return role;
            }
        }
        throw new IllegalArgumentException("Invalid role id: " + id);
    }
}
