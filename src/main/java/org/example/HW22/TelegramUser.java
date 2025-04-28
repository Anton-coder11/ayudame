package org.example.HW22;

public class TelegramUser {
    public String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public boolean isAdmin() {
        return isAdmin;
    }

    public void setAdmin(boolean admin) {
        isAdmin = admin;
    }

    public String username;
    public boolean isAdmin;
    public TelegramUser(){}
    public TelegramUser(String id, String username, boolean isAdmin){
        this.id = id;
        this.username = username;
        this.isAdmin = isAdmin;
    }
}
