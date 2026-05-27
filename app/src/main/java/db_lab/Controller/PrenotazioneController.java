package db_lab.Controller;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import db_lab.App;
import db_lab.data.DAOPrenotazione;
import db_lab.data.Prenotazione;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

/**
 * GET    /api/prenotazioni               → tutte (admin) o del visitatore
 * GET    /api/prenotazioni/attesa        → solo In_attesa (admin)
 * POST   /api/prenotazioni               → nuova prenotazione (visitatore)
 * PUT    /api/prenotazioni/{id}/esito    → approva/rifiuta (admin)
 * DELETE /api/prenotazioni/{id}          → elimina
 */
public class PrenotazioneController implements HttpHandler {

    private final DAOPrenotazione daoPrenotazione;

    public PrenotazioneController(DAOPrenotazione daoPrenotazione) {
        this.daoPrenotazione = daoPrenotazione;
    }

    @Override
    public void handle(HttpExchange ex) throws IOException {
        if (App.handleCors(ex)) return;

        if (LoginController.getSession(ex) == null) {
            App.sendError(ex, 401, "Non autenticato"); return;
        }

        String   path   = ex.getRequestURI().getPath();
        String   method = ex.getRequestMethod().toUpperCase();
        String[] segs   = path.split("/");

        try {
            // GET /api/prenotazioni/attesa
            if (method.equals("GET") && segs.length == 4 && segs[3].equals("attesa")) {
                if (!LoginController.isAdmin(ex)) { App.sendError(ex, 403, "Accesso negato"); return; }
                App.sendJson(ex, 200, toJsonArray(daoPrenotazione.getInAttesa()));
                return;
            }

            // PUT /api/prenotazioni/{id}/esito
            if (method.equals("PUT") && segs.length == 5 && segs[4].equals("esito")) {
                if (!LoginController.isAdmin(ex)) { App.sendError(ex, 403, "Accesso negato"); return; }
                int id = Integer.parseInt(segs[3]);
                Map<String, String> b = App.parseJson(App.readBody(ex));
                Prenotazione.EsitoPrenotazione esito = Prenotazione.EsitoPrenotazione.valueOf(b.get("esito"));
                boolean ok = daoPrenotazione.aggiornaEsito(id, esito, b.getOrDefault("motivo", null));
                if (ok) App.sendOk(ex, "");
                else    App.sendError(ex, 404, "Prenotazione non trovata");
                return;
            }

            // DELETE /api/prenotazioni/{id}
            if (method.equals("DELETE") && segs.length == 4) {
                int id = Integer.parseInt(segs[3]);
                Prenotazione p = daoPrenotazione.getByID(id);
                if (p == null) { App.sendError(ex, 404, "Prenotazione non trovata"); return; }
                if (!LoginController.isAdmin(ex) && p.getEffAccountID() != LoginController.getAccountId(ex)) {
                    App.sendError(ex, 403, "Accesso negato"); return;
                }
                boolean ok = daoPrenotazione.delete(id);
                if (ok) App.sendOk(ex, "");
                else    App.sendError(ex, 500, "Errore eliminazione");
                return;
            }

            // GET /api/prenotazioni
            if (method.equals("GET") && segs.length == 3) {
                String query = ex.getRequestURI().getQuery();
                List<Prenotazione> lista;
                if (query != null && query.startsWith("matricola=")) {
                    if (!LoginController.isAdmin(ex)) { App.sendError(ex, 403, "Accesso negato"); return; }
                    lista = daoPrenotazione.getByDetenuto(query.substring(10));
                } else if (LoginController.isAdmin(ex)) {
                    lista = daoPrenotazione.getAll();
                } else {
                    lista = daoPrenotazione.getByVisitatore(LoginController.getAccountId(ex));
                }
                App.sendJson(ex, 200, toJsonArray(lista));
                return;
            }

            // POST /api/prenotazioni
            if (method.equals("POST") && segs.length == 3) {
                Map<String, String> b = App.parseJson(App.readBody(ex));
                Prenotazione p = new Prenotazione(
                    0, 0,
                    b.getOrDefault("tipoAutorizzazione", "Altro"),
                    LocalDate.parse(b.getOrDefault("data", LocalDate.now().toString())),
                    LoginController.getAccountId(ex),
                    b.getOrDefault("matricolaDetenuto", ""),
                    null,
                    Prenotazione.EsitoPrenotazione.In_attesa
                );
                boolean ok = daoPrenotazione.insert(p);
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
            "\"id\":"                   + p.getIdPrenotazione()                + "," +
            "\"tipoAutorizzazione\":\"" + App.escJson(p.getTipoAutorizzazione()) + "\"," +
            "\"data\":\""               + p.getData()                          + "\"," +
            "\"matricolaDetenuto\":\"" + App.escJson(p.getMatricolaDetenuto()) + "\"," +
            "\"esito\":\""              + p.getEsitoPrenotazione()             + "\"," +
            "\"motivoRifiuto\":"        + (p.getMotivoRifiuto() == null ? "null" :
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