package ru.kpfu.itis.semester_work.helpers;

import ru.kpfu.itis.semester_work.services.UserService;

import java.util.HashMap;
import java.util.Map;

public class RegistrationValidator {
    private UserService userService;

    public RegistrationValidator(UserService userService) {
        this.userService = userService;
    }

    public Map<String, String> validate(String username, String email, String password, String confirmPassword) {
        Map<String, String> errors = new HashMap<>();
        if (username == null || username.isEmpty() || !username.matches("^[a-zA-Z0-9_]{3,20}$")) {
            errors.put("username", "Username must be 3-20 characters long and can contain letters, numbers, and underscores");
        } else if (email == null || email.isEmpty() || !email.matches("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$")) {
            errors.put("email", "Please enter a valid email address.");
        } else if (password == null || password.isEmpty() || !password.matches("(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{8,}$")) {
            errors.put("password", "Please enter a valid email address.");
        } else if (!password.equals(confirmPassword)) {
            errors.put("confirmPassword", "Passwords do not match");
        } else if (!userService.isUsernameAvailable(username)) {
            errors.put("username", "Such username already exists");
        } else if (!userService.isEmailAvailable(email)) {
            errors.put("email", "Such email already exists");
        }
        return errors;
    }
}
