package db_lab.Controller;

import db_lab.model.Model;
import db_lab.view.LoginView;
import db_lab.data.*;

public class LoginController {

    private final Model model;
    private final LoginView view;

    public LoginController(Model model, LoginView view) {
        this.model = model;
        this.view = view;
    }

    public void loginVisitatore() {
        try {
            Visitatore v = model.loginVisitatore(view.getEmail(), view.getPassword());
            if (v != null) view.showSuccess("Login visitatore effettuato");
            else view.showError("Credenziali errate");
        } catch (Exception e) {
            view.showError(e.getMessage());
        }
    }

    public void loginAmministratore() {
        try {
            Amministratore a = model.loginAmministratore(view.getEmail(), view.getPassword());
            if (a != null) view.showSuccess("Login amministratore effettuato");
            else view.showError("Credenziali errate");
        } catch (Exception e) {
            view.showError(e.getMessage());
        }
    }
}

