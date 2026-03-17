package library.service;

import library.data.DataStore;
import library.model.User;

public class AuthService {
    public User login(String email, String password) {
        User user = DataStore.findUserByEmail(email);
        if (user != null && user.getPassword().equals(password)) {
            return user;
        }
        return null;
    }
}
