package vehiclerental.model;

public class Admin extends User {

    public Admin(String email, String password, String name) {
        super(email, password, name, "ADMIN");
    }
}
