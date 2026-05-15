package db_lab.Controller;

import db_lab.model.Model;
import db_lab.view.DetenutoDettaglioView;
import db_lab.data.Provvedimento;

public class ProvvedimentoController {

    private final Model model;
    private final DetenutoDettaglioView view;

    public ProvvedimentoController(Model model, DetenutoDettaglioView view) {
        this.model = model;
        this.view = view;
    }

    public void aggiungiProvvedimento(Provvedimento p) {
        try {
            if (model.inserisciProvvedimento(p))
                view.showMessage("Provvedimento aggiunto");
            else
                view.showError("Errore nell'inserimento");
        } catch (Exception e) {
            view.showError(e.getMessage());
        }
    }
}
