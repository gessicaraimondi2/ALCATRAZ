package db_lab.Controller;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import db_lab.App;
import db_lab.data.DAOVisita;
import db_lab.data.Visita;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;

/**
 * GET    /api/visite                       → lista tutte (admin)
 * GET    /api/visite?prenotazione=ID       → per prenotazione (admin)
 * GET    /api/visite?effettuate=true       → solo effettuate (admin)
 * GET    /api/visite/{id}                  → singola (admin)
 * POST   /api/visite                       → inserisci (admin)
 * PUT    /api/visite/{id}                  → aggiorna esito (admin)
 * DELETE /api/visite/{id}                  → elimina (admin)
 */
public class VisitaController implements HttpHandler {

    private final DAOVisita daoVisita;

    public VisitaController(DAOVisita daoVisita) {
        this.daoVisita = daoVisita;
    }

    @Override
    public void handle(HttpExchange ex) throws IOException {
        if (App.handleCors(ex)) return;

        String   path   = ex.getRequestURI().getPath();
        String   method = ex.getRequestMethod().toUpperCase();
        String[] segs   = path.split("/");
        boolean  hasSub = segs.length > 3 && !segs[3].isEmpty();

        boolean isAdmin    = LoginController.isAdmin(ex);
        boolean isLoggedIn = LoginController.getSession(ex) != null;

        if (!isLoggedIn) { App.sendError(ex, 401, "Non autenticato"); return; }
        if (!isAdmin)    { App.sendError(ex, 403, "Accesso negato");  return; }

        try {
            switch (method) {

                case "GET" -> {
                    if (hasSub) {
                        int id;
                        try { id = Integer.parseInt(segs[3]); }
                        catch (NumberFormatException e) { App.sendError(ex, 400, "ID non valido"); return; }
                        Visita v = daoVisita.getByID(id);
                        if (v == null) { App.sendError(ex, 404, "Visita non trovata"); return; }
                        App.sendJson(ex, 200, toJson(v));
                    } else {
                        String query = ex.getRequestURI().getQuery();
                        if (query != null && query.startsWith("prenotazione=")) {
                            int idPren;
                            try { idPren = Integer.parseInt(query.substring(13)); }
                            catch (NumberFormatException e) { App.sendError(ex, 400, "ID prenotazione non valido"); return; }
                            Visita v = daoVisita.getByPrenotazione(idPren);
                            App.sendJson(ex, 200, v == null ? "null" : toJson(v));
                        } else if (query != null && query.equals("effettuate=true")) {
                            App.sendJson(ex, 200, toJsonArray(daoVisita.getEffettuate()));
                        } else {
                            App.sendJson(ex, 200, toJsonArray(daoVisita.getAll()));
                        }
                    }
                }

                case "POST" -> {
                    Map<String, String> b = App.parseJson(App.readBody(ex));
                    boolean ok = daoVisita.insert(fromMap(b, LoginController.getAccountId(ex)));
                    if (ok) App.sendOk(ex, "");
                    else    App.sendError(ex, 500, "Errore inserimento");
                }

                case "PUT" -> {
                    if (!hasSub) { App.sendError(ex, 400, "ID mancante"); return; }
                    int id;
                    try { id = Integer.parseInt(segs[3]); }
                    catch (NumberFormatException e) { App.sendError(ex, 400, "ID non valido"); return; }
                    Map<String, String> b = App.parseJson(App.readBody(ex));
                    if (!b.containsKey("esitoVisita")) { App.sendError(ex, 400, "Campo esitoVisita richiesto"); return; }
                    Visita.EsitoVisita esito;
                    try { esito = Visita.EsitoVisita.valueOf(b.get("esitoVisita")); }
                    catch (IllegalArgumentException e) { App.sendError(ex, 400, "Esito non valido"); return; }
                    boolean ok = daoVisita.aggiornaEsito(id, esito);
                    if (ok) App.sendOk(ex, "");
                    else    App.sendError(ex, 404, "Visita non trovata");
                }

                case "DELETE" -> {
                    if (!hasSub) { App.sendError(ex, 400, "ID mancante"); return; }
                    int id;
                    try { id = Integer.parseInt(segs[3]); }
                    catch (NumberFormatException e) { App.sendError(ex, 400, "ID non valido"); return; }
                    boolean ok = daoVisita.delete(id);
                    if (ok) App.sendOk(ex, "");
                    else    App.sendError(ex, 404, "Visita non trovata");
                }

                default -> App.sendError(ex, 405, "Metodo non consentito");
            }
        } catch (Exception e) {
            App.sendError(ex, 500, e.getMessage());
        }
    }

    static String toJson(Visita v) {
        return "{" +
            "\"id\":"               + v.getNumeroVisita()       + "," +
            "\"idPrenotazione\":"   + v.getIdPrenotazione()     + "," +
            "\"data\":\""           + v.getData()               + "\"," +
            "\"orario\":\""         + v.getOrario()             + "\"," +
            "\"esitoVisita\":\""    + v.getEsitoVisita().name() + "\"" +
            "}";
    }

    static String toJsonArray(List<Visita> lista) {
        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < lista.size(); i++) {
            if (i > 0) sb.append(",");
            sb.append(toJson(lista.get(i)));
        }
        return sb.append("]").toString();
    }

    private Visita fromMap(Map<String, String> b, int accountId) {
        return new Visita(
            0, // NumeroVisita auto-generato dal DB
            Integer.parseInt(b.getOrDefault("idPrenotazione", "0")),
            LocalDate.parse(b.getOrDefault("data",    LocalDate.now().toString())),
            LocalTime.parse(b.getOrDefault("orario",  "00:00:00")),
            accountId,
            Visita.EsitoVisita.valueOf(b.getOrDefault("esitoVisita", "Regolare"))
        );
    }
}