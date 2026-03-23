package library.model;

import java.util.ArrayList;
import java.util.List;

public class Borrower extends User {
    private double securityDeposit;
    private boolean membershipCardLost;
    private List<String> cart;

    public Borrower(String email, String password, String name) {
        super(email, password, name, "BORROWER");
        this.securityDeposit   = 1500.0;
        this.membershipCardLost = false;
        this.cart              = new ArrayList<>();
    }

    public double getSecurityDeposit()              { return securityDeposit; }
    public void   setSecurityDeposit(double amount) { this.securityDeposit = amount; }

    public boolean isMembershipCardLost()           { return membershipCardLost; }
    public void    setMembershipCardLost(boolean v) { this.membershipCardLost = v; }

    public List<String> getCart() { return cart; }
}
