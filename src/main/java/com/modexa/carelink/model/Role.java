package com.modexa.carelink.model;

public enum Role {
    ADMIN(1, "Admin"),
    PHYSICIAN(2, "Physician"),
    NURSE(3, "Nurse"),
    PHYSICIAN_ASSISTANT(4, "Physician Assistant");

    private final int id;
    private final String displayName;

    Role(int id, String displayName) {
        this.id = id;
        this.displayName = displayName;
    }

    public int getId() {
        return id;
    }

    public String getDisplayName() {
        return displayName;
    }

    public static Role fromId(Integer id) {
        if (id == null) {
            return null;
        }
        for (Role role : Role.values()) {
            if (role.getId() == id) {
                return role;
            }
        }
        throw new IllegalArgumentException("No role found with id: " + id);
    }

    public static Role fromString(String roleName) {
        if (roleName == null) {
            return null;
        }
        
        // Convert to uppercase and replace spaces with underscore for enum comparison
        String normalizedRole = roleName.trim().toUpperCase().replace(" ", "_");
        System.out.println("Normalized role: " + normalizedRole); // Debug print
        
        try {
            return Role.valueOf(normalizedRole);
        } catch (IllegalArgumentException e) {
            System.out.println("Available roles: " + java.util.Arrays.toString(Role.values())); // Debug print
            throw new IllegalArgumentException("Invalid role: " + roleName + ". Valid roles are: Admin, Physician, Nurse, Physician Assistant");
        }
    }

    @Override
    public String toString() {
        return displayName;
    }
}
