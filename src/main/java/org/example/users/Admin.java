package org.example.users;

public class Admin extends User {


    public Admin(String userName, StringBuilder password, int servicePrice) {
        super(userName, password);
    }

    public Admin(String userName, StringBuilder password) {
        super(userName, password);

    }

    @Override
    public String getUserName() {
        return super.getUserName();
    }

    @Override
    public StringBuilder getPassword() {
        return super.getPassword();
    }

    @Override
    public void setUserName(String userName) {
        super.setUserName(userName);
    }

    @Override
    public void setPassword(StringBuilder password) {
        super.setPassword(password);
    }


}
