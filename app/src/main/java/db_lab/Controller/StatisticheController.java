package db_lab.Controller;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import db_lab.App;
import db_lab.model.Model;

import java.io.IOException;

/**
 * GET /api/statistiche
 * Risposta: { tassoPartecipazione, mediaDetenutiPerSezione, prenotazioniInAttesa }
 */
public class StatisticheController implements HttpHandler {

    private final Model model;

    public StatisticheController(Model model) {
        this.model = model;
    }

    @Override
    public void handle(HttpExchange ex) throws IOException {
        if (App.handleCors(ex)) return;
        if (!LoginController.isAdmin(ex)) { App.sendError(ex, 403, "Accesso negato"); return; }

        if (!"GET".equalsIgnoreCase(ex.getRequestMethod())) {
            App.sendError(ex, 405, "Metodo non consentito"); return;
        }

        try {
            double tasso  = model.getTassoPartecipazione();
            double media  = model.getMediaDetenutiPerSezione();
            int    attesa = model.getNumeroPrenotazioniInAttesa();

            App.sendJson(ex, 200,
                "{\"tassoPartecipazione\":"    + String.format("%.1f", tasso)  +
                ",\"mediaDetenutiPerSezione\":" + String.format("%.1f", media)  +
                ",\"prenotazioniInAttesa\":"    + attesa                        +
                "}");
        } catch (Exception e) {
            App.sendError(ex, 500, e.getMessage());
        }
    }
}