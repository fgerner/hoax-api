package com.example.hoxify;

import com.example.hoxify.user.User;

public class TestUtil {
    public static User createValidUser() {
        User user = new User();
        user.setUsername("test-user");
        user.setDisplayName("test-display");
        user.setPassword("P4ssword");
        user.setImage("user-image.png");
        return user;
    }
}
