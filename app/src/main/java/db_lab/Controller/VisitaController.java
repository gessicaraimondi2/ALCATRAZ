package db_lab.Controller;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import db_lab.App;
import db_lab.data.Visita;
import db_lab.model.Model;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;

/**
 * GET    /api/visite            → tutte
 * POST   /api/visite            → registra
 * PUT    /api/visite/{id}/esito → aggiorna esito
 * DELETE /api/visite/{id}       → elimina
 */
public class VisitaController implements HttpHandler {

    private final Model model;

    public VisitaController(Model model) {
        this.model = model;
    }

    @Override
    public void handle(HttpExchange ex) throws IOException {
        if (App.handleCors(ex)) return;
        if (!LoginController.isAdmin(ex)) { App.sendError(ex, 403, "Accesso negato"); return; }

        String path   = ex.getRequestURI().getPath();
        String method = ex.getRequestMethod().toUpperCase();
        String[] segs = path.split("/");

        try {
            // PUT /api/visite/{id}/esito
            if (method.equals("PUT") && segs.length == 5 && segs[4].equals("esito")) {
                int id = Integer.parseInt(segs[3]);
                Map<String, String> b = App.parseJson(App.readBody(ex));
                Visita.EsitoVisita esito = Visita.EsitoVisita.valueOf(b.get("esito"));
                boolean ok = model.aggiornaEsitoVisita(id, esito);
                if (ok) App.sendOk(ex, "");
                else    App.sendError(ex, 404, "Visita non trovata");
                return;
            }

            switch (method) {
                case "GET" -> {
                    List<Visita> lista = model.getTutteVisite();
                    App.sendJson(ex, 200, toJsonArray(lista));
                }
                case "POST" -> {
                    Map<String, String> b = App.parseJson(App.readBody(ex));
                    Visita v = new Visita(
                        0,
                        Integer.parseInt(b.getOrDefault("idPrenotazione", "0")),
                        LocalDate.parse(b.getOrDefault("data", LocalDate.now().toString())),
                        LocalTime.parse(b.getOrDefault("orario", "09:00")),
                        0,
                        Visita.EsitoVisita.Effettuata
                    );
                    boolean ok = model.inserisciVisita(v);
                    if (ok) App.sendOk(ex, "");
                    else    App.sendError(ex, 500, "Errore inserimento");
                }
                case "DELETE" -> {
                    if (segs.length < 4) { App.sendError(ex, 400, "ID mancante"); return; }
                    boolean ok = model.eliminaVisita(Integer.parseInt(segs[3]));
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
            "\"id\":"               + v.getNumeroVisita()    + "," +
            "\"idPrenotazione\":"   + v.getIdPrenotazione()  + "," +
            "\"data\":\""           + v.getData()            + "\"," +
            "\"orario\":\""         + v.getOrario()          + "\"," +
            "\"esito\":\""          + v.getEsitoVisita()     + "\"" +
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
}