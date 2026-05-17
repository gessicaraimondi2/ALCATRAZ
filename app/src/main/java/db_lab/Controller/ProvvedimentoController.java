package db_lab.Controller;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import db_lab.App;
import db_lab.data.Provvedimento;
import db_lab.model.Model;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

/**
 * GET    /api/provvedimenti                 → tutti
 * GET    /api/provvedimenti?detenuto=M001   → per detenuto
 * POST   /api/provvedimenti                 → inserisci
 * DELETE /api/provvedimenti/{id}            → elimina
 */
public class ProvvedimentoController implements HttpHandler {

    private final Model model;

    public ProvvedimentoController(Model model) {
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
                    String query = ex.getRequestURI().getQuery();
                    List<Provvedimento> lista;
                    if (query != null && query.startsWith("detenuto=")) {
                        lista = model.getProvvedimentiByDetenuto(query.substring(9));
                    } else {
                        lista = model.getTuttiProvvedimenti();
                    }
                    App.sendJson(ex, 200, toJsonArray(lista));
                }
                case "POST" -> {
                    Map<String, String> b = App.parseJson(App.readBody(ex));
                    Provvedimento p = new Provvedimento(
                        0,
                        Provvedimento.Tipo.valueOf(b.getOrDefault("tipo", "Lieve")),
                        b.getOrDefault("motivazione", ""),
                        LocalDate.parse(b.getOrDefault("data", LocalDate.now().toString())),
                        b.getOrDefault("matricolaDetenuto", ""),
                        b.getOrDefault("matricolaPersonale", "")
                    );
                    boolean ok = model.inserisciProvvedimento(p);
                    if (ok) App.sendOk(ex, "");
                    else    App.sendError(ex, 500, "Errore inserimento");
                }
                case "DELETE" -> {
                    if (!hasSub) { App.sendError(ex, 400, "ID mancante"); return; }
                    boolean ok = model.eliminaProvvedimento(Integer.parseInt(segs[3]));
                    if (ok) App.sendOk(ex, "");
                    else    App.sendError(ex, 404, "Non trovato");
                }
                default -> App.sendError(ex, 405, "Metodo non consentito");
            }
        } catch (Exception e) {
            App.sendError(ex, 500, e.getMessage());
        }
    }

    static String toJson(Provvedimento p) {
        return "{" +
            "\"id\":"                  + p.getNumeroProv()                    + "," +
            "\"tipo\":\""              + p.getTipo()                          + "\"," +
            "\"motivazione\":\""       + App.escJson(p.getMotivazione())      + "\"," +
            "\"data\":\""              + p.getDataEmissione()                 + "\"," +
            "\"matricolaDetenuto\":\"" + App.escJson(p.getMatricolaDetenuto()) + "\"," +
            "\"matricolaPersonale\":\"" + App.escJson(p.getMatricola())       + "\"" +
            "}";
    }

    static String toJsonArray(List<Provvedimento> lista) {
        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < lista.size(); i++) {
            if (i > 0) sb.append(",");
            sb.append(toJson(lista.get(i)));
        }
        return sb.append("]").toString();
    }
}