package db_lab.Controller;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import db_lab.App;
import db_lab.data.DAOStatistiche;

import java.io.IOException;

/**
 * GET /api/statistiche  → tutte le statistiche aggregate (admin)
 *
 * Risposta JSON:
 * {
 *   "tassoPartecipazione":     <double>,   // % detenuti iscritti ad almeno un corso
 *   "mediaDetenutiPerSezione": <double>,   // media detenuti per sezione
 *   "prenotazioniInAttesa":    <int>        // prenotazioni ancora da esaminare
 * }
 *
 * Nota: stampaDetenutiPerStato() scrive su stdout ed è utile solo a scopo
 * di debug; non è esposta via HTTP.
 */
public class StatisticheController implements HttpHandler {

    private final DAOStatistiche daoStatistiche;

    public StatisticheController(DAOStatistiche daoStatistiche) {
        this.daoStatistiche = daoStatistiche;
    }

    @Override
    public void handle(HttpExchange ex) throws IOException {
        if (App.handleCors(ex)) return;

        String method = ex.getRequestMethod().toUpperCase();

        boolean isAdmin    = LoginController.isAdmin(ex);
        boolean isLoggedIn = LoginController.getSession(ex) != null;

        if (!isLoggedIn) { App.sendError(ex, 401, "Non autenticato"); return; }
        if (!isAdmin)    { App.sendError(ex, 403, "Accesso negato");  return; }

        if (!method.equals("GET")) { App.sendError(ex, 405, "Metodo non consentito"); return; }

        try {
            double tassoPartecipazione     = daoStatistiche.getTassoPartecipazione();
            double mediaDetenutiPerSezione = daoStatistiche.getMediaDetenutiPerSezione();
            int    prenotazioniInAttesa    = daoStatistiche.getPrenotazioniInAttesa();

            String json = "{" +
                "\"tassoPartecipazione\":"     + tassoPartecipazione     + "," +
                "\"mediaDetenutiPerSezione\":" + mediaDetenutiPerSezione + "," +
                "\"prenotazioniInAttesa\":"    + prenotazioniInAttesa    +
                "}";

            App.sendJson(ex, 200, json);

        } catch (Exception e) {
            App.sendError(ex, 500, e.getMessage());
        }
    }
}