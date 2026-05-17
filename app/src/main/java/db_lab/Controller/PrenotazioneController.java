package db_lab.Controller;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import db_lab.App;
import db_lab.data.Prenotazione;
import db_lab.model.Model;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

/**
 * GET    /api/prenotazioni               → tutte (admin) o del visitatore (visitatore)
 * GET    /api/prenotazioni/attesa        → solo In_attesa (admin)
 * POST   /api/prenotazioni               → nuova prenotazione (visitatore)
 * PUT    /api/prenotazioni/{id}/esito    → approva/rifiuta (admin)
 * DELETE /api/prenotazioni/{id}          → elimina
 */
public class PrenotazioneController implements HttpHandler {

    private final Model model;

    public PrenotazioneController(Model model) {
        this.model = model;
    }

    @Override
    public void handle(HttpExchange ex) throws IOException {
        if (App.handleCors(ex)) return;

        String path    = ex.getRequestURI().getPath(); // /api/prenotazioni[/attesa|/{id}|/{id}/esito]
        String method  = ex.getRequestMethod().toUpperCase();
        String[] segs  = path.split("/");              // ["","api","prenotazioni",...]

        // Controlla autenticazione
        if (LoginController.getSession(ex) == null) {
            App.sendError(ex, 401, "Non autenticato"); return;
        }

        try {
            // GET /api/prenotazioni/attesa
            if (method.equals("GET") && segs.length == 4 && segs[3].equals("attesa")) {
                if (!LoginController.isAdmin(ex)) { App.sendError(ex, 403, "Accesso negato"); return; }
                App.sendJson(ex, 200, toJsonArray(model.getPrenotazioniInAttesa()));
                return;
            }

            // PUT /api/prenotazioni/{id}/esito
            if (method.equals("PUT") && segs.length == 5 && segs[4].equals("esito")) {
                if (!LoginController.isAdmin(ex)) { App.sendError(ex, 403, "Accesso negato"); return; }
                int id = Integer.parseInt(segs[3]);
                Map<String, String> b = App.parseJson(App.readBody(ex));
                Prenotazione.EsitoPrenotazione esito = Prenotazione.EsitoPrenotazione.valueOf(b.get("esito"));
                String motivo = b.getOrDefault("motivo", null);
                boolean ok = model.aggiornaEsitoPrenotazione(id, esito, motivo);
                if (ok) App.sendOk(ex, "");
                else    App.sendError(ex, 404, "Prenotazione non trovata");
                return;
            }

            // DELETE /api/prenotazioni/{id}
            if (method.equals("DELETE") && segs.length == 4) {
                int id = Integer.parseInt(segs[3]);
                Prenotazione p = model.getPrenotazione(id);
                if (p == null) { App.sendError(ex, 404, "Prenotazione non trovata"); return; }
                // Il visitatore può eliminare solo le sue
                if (!LoginController.isAdmin(ex) && p.getEffAccountID() != LoginController.getAccountId(ex)) {
                    App.sendError(ex, 403, "Accesso negato"); return;
                }
                boolean ok = model.eliminaPrenotazione(id);
                if (ok) App.sendOk(ex, "");
                else    App.sendError(ex, 500, "Errore eliminazione");
                return;
            }

            // GET /api/prenotazioni
            if (method.equals("GET") && segs.length == 3) {
                List<Prenotazione> lista;
                if (LoginController.isAdmin(ex)) {
                    lista = model.getPrenotazioniInAttesa(); // admin vede tutte in attesa di default
                } else {
                    lista = model.getPrenotazioniByVisitatore(LoginController.getAccountId(ex));
                }
                App.sendJson(ex, 200, toJsonArray(lista));
                return;
            }

            // POST /api/prenotazioni
            if (method.equals("POST") && segs.length == 3) {
                Map<String, String> b = App.parseJson(App.readBody(ex));
                int accountId = LoginController.getAccountId(ex);
                Prenotazione p = new Prenotazione(
                    0,
                    0,
                    b.getOrDefault("tipoAutorizzazione", "Altro"),
                    LocalDate.parse(b.getOrDefault("data", LocalDate.now().toString())),
                    accountId,
                    b.getOrDefault("matricolaDetenuto", ""),
                    null,
                    Prenotazione.EsitoPrenotazione.In_attesa
                );
                boolean ok = model.inserisciPrenotazione(p);
                if (ok) App.sendOk(ex, "");
                else    App.sendError(ex, 500, "Errore inserimento");
                return;
            }

            App.sendError(ex, 404, "Route non trovata");

        } catch (Exception e) {
            App.sendError(ex, 500, e.getMessage());
        }
    }

    static String toJson(Prenotazione p) {
        return "{" +
            "\"id\":"                  + p.getIdPrenotazione()                       + "," +
            "\"tipoAutorizzazione\":\"" + App.escJson(p.getTipoAutorizzazione())      + "\"," +
            "\"data\":\""              + p.getData()                                 + "\"," +
            "\"matricolaDetenuto\":\"" + App.escJson(p.getMatricolaDetenuto())       + "\"," +
            "\"esito\":\""             + p.getEsitoPrenotazione()                    + "\"," +
            "\"motivoRifiuto\":"       + (p.getMotivoRifiuto() == null ? "null" :
                                         "\"" + App.escJson(p.getMotivoRifiuto()) + "\"") +
            "}";
    }

    static String toJsonArray(List<Prenotazione> lista) {
        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < lista.size(); i++) {
            if (i > 0) sb.append(",");
            sb.append(toJson(lista.get(i)));
        }
        return sb.append("]").toString();
    }
}