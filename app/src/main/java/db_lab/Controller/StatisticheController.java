package db_lab.Controller;

import db_lab.model.Model;
import db_lab.view.StatisticheView;

public class StatisticheController {

    private final Model model;
    private final StatisticheView view;

    public StatisticheController(Model model, StatisticheView view) {
        this.model = model;
        this.view = view;
    }

    public void mostraStatistiche() {
        try {
            view.showTassoPartecipazione(model.getTassoPartecipazione());
            view.showMediaDetenuti(model.getMediaDetenutiPerSezione());
            view.showPrenotazioniInAttesa(model.getNumeroPrenotazioniInAttesa());
        } catch (Exception e) {
            view.showError(e.getMessage());
        }
    }
}
