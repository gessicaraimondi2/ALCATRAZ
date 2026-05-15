package db_lab.view;

public class LoginView {

    private String email;
    private String password;

    // ----------------------------- //
    // GETTER                        //
    // ----------------------------- //
    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    // ----------------------------- //
    // SETTER                        //
    // ----------------------------- //
    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    // ----------------------------- //
    // OUTPUT PER IL CONTROLLER      //
    // ----------------------------- //
    public void showError(String msg) {
        System.out.println("[LOGIN ERROR] " + msg);
    }

    public void showSuccess(String msg) {
        System.out.println("[LOGIN OK] " + msg);
    }
}
