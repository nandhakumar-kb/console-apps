package supermarket.model;

public class User {
    private String email;
    private String password;
    private String name;
    private String role;

    public User(String email, String password, String name, String role) {
        this.email    = email;
        this.password = password;
        this.name     = name;
        this.role     = role;
    }

    public String getEmail()    { return email; }
    public String getPassword() { return password; }
    public String getName()     { return name; }
    public String getRole()     { return role; }

    public void setPassword(String password) { this.password = password; }
    public void setName(String name)         { this.name = name; }
}
