package db_lab.Controller;

import db_lab.model.Model;
import db_lab.view.PrenotazioneView;
import db_lab.data.Prenotazione;

public class PrenotazioneController {

    private final Model model;
    private final PrenotazioneView view;

    public PrenotazioneController(Model model, PrenotazioneView view) {
        this.model = model;
        this.view = view;
    }

    public void creaPrenotazione() {
        try {
            Prenotazione p = new Prenotazione(
                0,
                view.getNumeroAutorizzazione(),
                view.getTipoAutorizzazione(),
                view.getData(),
                view.getEffAccountID(),
                view.getMatricolaDetenuto(),
                null,
                Prenotazione.EsitoPrenotazione.In_attesa
            );

            if (model.inserisciPrenotazione(p))
                view.showSuccess("Prenotazione inviata");
            else
                view.showError("Errore nella prenotazione");

        } catch (Exception e) {
            view.showError(e.getMessage());
        }
    }
}
