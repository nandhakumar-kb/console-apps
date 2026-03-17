package supermarket.service;

import supermarket.data.DataStore;
import supermarket.model.User;

public class AuthService {
    public User login(String email, String password) {
        User user = DataStore.findUserByEmail(email);
        if (user != null && user.getPassword().equals(password)) {
            return user;
        }
        return null;
    }
}
