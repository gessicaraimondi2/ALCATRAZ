package db_lab.Controller;

import db_lab.model.Model;
import db_lab.view.RegisterView;
import db_lab.data.Visitatore;

public class RegisterController {

    private final Model model;
    private final RegisterView view;

    public RegisterController(Model model, RegisterView view) {
        this.model = model;
        this.view = view;
    }

    public void registraVisitatore() {
        try {
            Visitatore v = new Visitatore(
                0,
                view.getEmail(),
                view.getPassword(),
                java.time.LocalDate.now(),
                view.getNome(),
                view.getCognome(),
                view.getCodiceFiscale()
            );

            if (model.inserisciVisitatore(v))
                view.showSuccess("Registrazione completata");
            else
                view.showError("Registrazione fallita");

        } catch (Exception e) {
            view.showError(e.getMessage());
        }
    }
}
