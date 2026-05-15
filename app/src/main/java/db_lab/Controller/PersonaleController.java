package db_lab.Controller;

import db_lab.model.Model;
import db_lab.view.DashboardAdminView;
import db_lab.data.Personale;

public class PersonaleController {

    private final Model model;
    private final DashboardAdminView view;

    public PersonaleController(Model model, DashboardAdminView view) {
        this.model = model;
        this.view = view;
    }

    public void mostraPersonale() {
        try {
            view.showPersonale(model.getTuttoPersonale());
        } catch (Exception e) {
            view.showError(e.getMessage());
        }
    }

    public void aggiungiPersonale(Personale p) {
        try {
            if (model.inserisciPersonale(p))
                view.showMessage("Personale aggiunto");
            else
                view.showError("Errore inserimento");
        } catch (Exception e) {
            view.showError(e.getMessage());
        }
    }
}
