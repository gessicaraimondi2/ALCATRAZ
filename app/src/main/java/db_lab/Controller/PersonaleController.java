package db_lab.Controller;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import db_lab.App;
import db_lab.data.Personale;
import db_lab.model.Model;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

/**
 * GET    /api/personale              → tutto il personale
 * GET    /api/personale?ruolo=Guardia → filtro
 * GET    /api/personale/{matricola}  → singolo
 * POST   /api/personale              → inserisci
 * DELETE /api/personale/{matricola}  → elimina
 */
public class PersonaleController implements HttpHandler {

    private final Model model;

    public PersonaleController(Model model) {
        this.model = model;
    }

    @Override
    public void handle(HttpExchange ex) throws IOException {
        if (App.handleCors(ex)) return;
        if (!LoginController.isAdmin(ex)) { App.sendError(ex, 403, "Accesso negato"); return; }

        String path   = ex.getRequestURI().getPath();
        String method = ex.getRequestMethod().toUpperCase();
        String[] segs = path.split("/");
        boolean hasSub = segs.length > 3 && !segs[3].isEmpty();

        try {
            switch (method) {
                case "GET" -> {
                    if (hasSub) {
                        Personale p = model.getPersonaleByMatricola(segs[3]);
                        if (p == null) { App.sendError(ex, 404, "Non trovato"); return; }
                        App.sendJson(ex, 200, toJson(p));
                    } else {
                        String query = ex.getRequestURI().getQuery();
                        List<Personale> lista;
                        if (query != null && query.startsWith("ruolo=")) {
                            Personale.Ruolo r = Personale.Ruolo.valueOf(query.substring(6));
                            lista = model.getPersonaleByRuolo(r);
                        } else {
                            lista = model.getTuttoPersonale();
                        }
                        App.sendJson(ex, 200, toJsonArray(lista));
                    }
                }
                case "POST" -> {
                    Map<String, String> b = App.parseJson(App.readBody(ex));
                    Personale p = new Personale(
                        b.getOrDefault("matricola", ""),
                        b.getOrDefault("nome", ""),
                        b.getOrDefault("cognome", ""),
                        Personale.Ruolo.valueOf(b.getOrDefault("ruolo", "Guardia")),
                        LocalDate.parse(b.getOrDefault("dataAssunzione", LocalDate.now().toString())),
                        0
                    );
                    boolean ok = model.inserisciPersonale(p);
                    if (ok) App.sendOk(ex, "");
                    else    App.sendError(ex, 500, "Errore inserimento");
                }
                case "DELETE" -> {
                    if (!hasSub) { App.sendError(ex, 400, "Matricola mancante"); return; }
                    boolean ok = model.eliminaPersonale(segs[3]);
                    if (ok) App.sendOk(ex, "");
                    else    App.sendError(ex, 404, "Non trovato");
                }
                default -> App.sendError(ex, 405, "Metodo non consentito");
            }
        } catch (Exception e) {
            App.sendError(ex, 500, e.getMessage());
        }
    }

    static String toJson(Personale p) {
        return "{" +
            "\"matricola\":\""      + App.escJson(p.getMatricola())      + "\"," +
            "\"nome\":\""           + App.escJson(p.getNome())           + "\"," +
            "\"cognome\":\""        + App.escJson(p.getCognome())        + "\"," +
            "\"ruolo\":\""          + p.getRuolo()                       + "\"," +
            "\"dataAssunzione\":\"" + p.getDataAssunzione()              + "\"" +
            "}";
    }

    static String toJsonArray(List<Personale> lista) {
        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < lista.size(); i++) {
            if (i > 0) sb.append(",");
            sb.append(toJson(lista.get(i)));
        }
        return sb.append("]").toString();
    }
}