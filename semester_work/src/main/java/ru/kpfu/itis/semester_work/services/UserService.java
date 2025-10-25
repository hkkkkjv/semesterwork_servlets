package ru.kpfu.itis.semester_work.services;

import ru.kpfu.itis.semester_work.dao.UserDAO;
import ru.kpfu.itis.semester_work.model.Role;
import ru.kpfu.itis.semester_work.model.User;
import ru.kpfu.itis.semester_work.util.PasswordHasher;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public class UserService {
    private final UserDAO userDAO;
    private final PasswordHasher passwordHasher = new PasswordHasher();

    public UserService(UserDAO userDAO) {
        this.userDAO = userDAO;
    }
    public User createUser (String username, String email, String password, String roleString) throws Exception {
        Role role;
        try {
            role = Role.valueOf(roleString);
        } catch (IllegalArgumentException e) {
            role = Role.STUDENT;
        }
        String hashedPassword = passwordHasher.hashPassword(password);
        User user = new User(username, email, hashedPassword, role);
        return registerUser(user);
    }
    public User registerUser(User user) {
        return userDAO.create(user);
    }

    public User authentication(String username, String password) {
        return userDAO.get(username, password);
    }

    public boolean login(HttpServletRequest request, String username, String password) {
        User user = authentication(username, password);
        if (user != null) {
            User sessionUser = new User(user.getId(), user.getUsername(), user.getRole());
            request.getSession().setAttribute("user", sessionUser);
            return true;
        }
        return false;
    }

    public boolean isUsernameAvailable(String username) {
        User user = userDAO.getByUsername(username);
        return user == null;
    }

    public boolean isUsernameAvailable(String username, Integer userId) {
        User user = userDAO.getByUsername(username);
        return user == null || user.getId().equals(userId);
    }

    public boolean isEmailAvailable(String email) {
        return userDAO.getByEmail(email) == null;
    }

    public boolean isEmailAvailable(String email, Integer userId) {
        User user = userDAO.getByEmail(email);
        return user == null || user.getId().equals(userId);
    }

    public void updateUserProfile(User user, String username, String email, String about, String password) {
        boolean isUpdated = false;
        if (!user.getUsername().equals(username)) {
            user.setUsername(username);
            isUpdated = true;
        }
        if (!user.getEmail().equals(email)) {
            user.setEmail(email);
            isUpdated = true;
        }
        if (!user.getAbout().equals(about)) {
            user.setAbout(about);
            isUpdated = true;
        }
        if (password != null && !password.isEmpty() && !passwordHasher.verifyPassword(password, user.getPassword())) {
            user.setPassword(passwordHasher.hashPassword(password));
            isUpdated = true;
        }
        if (isUpdated) {
            updateUser(user);
        }
    }

    public User getUserByUsername(String username) {
        return userDAO.getByUsername(username);
    }

    public void updateUser(User user) {
        userDAO.update(user);
    }

    public User get(Integer id) {
        return userDAO.get(id);
    }
    public List<User> getAllTeachers(){
        return userDAO.getAllTeachers();
    }
}
