package vehiclerental.model;

import java.util.ArrayList;
import java.util.List;

public class Borrower extends User {
    private double securityDeposit;
    private final List<String> cartVehicleIds = new ArrayList<>();

    public Borrower(String email, String password, String name, double securityDeposit) {
        super(email, password, name, "BORROWER");
        this.securityDeposit = securityDeposit;
    }

    public double getSecurityDeposit() {
        return securityDeposit;
    }

    public void setSecurityDeposit(double securityDeposit) {
        this.securityDeposit = securityDeposit;
    }

    public List<String> getCartVehicleIds() {
        return cartVehicleIds;
    }
}
