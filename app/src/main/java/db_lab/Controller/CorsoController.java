package db_lab.Controller;

import db_lab.model.Model;
import db_lab.view.DashboardAdminView;
import db_lab.data.Corso;

public class CorsoController {

    private final Model model;
    private final DashboardAdminView view;

    public CorsoController(Model model, DashboardAdminView view) {
        this.model = model;
        this.view = view;
    }

    public void mostraCorsi() {
        try {
            view.showCorsi(model.getTuttiCorsi());
        } catch (Exception e) {
            view.showError(e.getMessage());
        }
    }

    public void aggiungiCorso(Corso c) {
        try {
            if (model.inserisciCorso(c))
                view.showMessage("Corso aggiunto");
            else
                view.showError("Errore inserimento corso");
        } catch (Exception e) {
            view.showError(e.getMessage());
        }
    }
}
