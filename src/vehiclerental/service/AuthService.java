package vehiclerental.service;

import vehiclerental.data.DataStore;
import vehiclerental.model.Admin;
import vehiclerental.model.Borrower;
import vehiclerental.model.User;

public class AuthService {

    public User login(String email, String password) {
        User user = DataStore.findUserByEmail(email);
        if (user == null) {
            return null;
        }
        return user.getPassword().equals(password) ? user : null;
    }

    public String signup(String role, String email, String password, String name) {
        if (DataStore.findUserByEmail(email) != null) {
            return "This email is already registered.";
        }

        if ("ADMIN".equalsIgnoreCase(role)) {
            DataStore.users.add(new Admin(email, password, name));
            return "Admin account created successfully.";
        }

        DataStore.users.add(new Borrower(email, password, name, 30000));
        return "Borrower account created successfully with initial caution deposit Rs30000.";
    }
}
