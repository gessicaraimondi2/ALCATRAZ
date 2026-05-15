package db_lab.Controller;

import db_lab.model.Model;
import db_lab.view.DetenutoDettaglioView;
import db_lab.data.*;

public class DetenutoController {

    private final Model model;
    private final DetenutoDettaglioView view;

    public DetenutoController(Model model, DetenutoDettaglioView view) {
        this.model = model;
        this.view = view;
    }

    public void mostraDetenuto(String matricola) {
        try {
            Detenuto d = model.getDetenutoByMatricola(matricola);
            if (d != null) view.showDetenuto(d);
            else view.showError("Detenuto non trovato");
        } catch (Exception e) {
            view.showError(e.getMessage());
        }
    }

    public void mostraProvvedimenti(String matricola) {
        try {
            view.showProvvedimenti(model.getProvvedimentiByDetenuto(matricola));
        } catch (Exception e) {
            view.showError(e.getMessage());
        }
    }
}

