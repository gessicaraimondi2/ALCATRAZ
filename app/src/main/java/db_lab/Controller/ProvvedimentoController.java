package db_lab.Controller;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import db_lab.App;
import db_lab.data.DAOProvvedimento;
import db_lab.data.Provvedimento;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

/**
 * GET    /api/provvedimenti                  → lista tutti (admin)
 * GET    /api/provvedimenti?detenuto=MAT     → per detenuto (loggato)
 * GET    /api/provvedimenti?tipo=Grave       → per tipo (admin)
 * GET    /api/provvedimenti/{id}             → singolo (admin)
 * POST   /api/provvedimenti                  → inserisci (admin)
 * DELETE /api/provvedimenti/{id}             → elimina (admin)
 */
public class ProvvedimentoController implements HttpHandler {

    private final DAOProvvedimento daoProvvedimento;

    public ProvvedimentoController(DAOProvvedimento daoProvvedimento) {
        this.daoProvvedimento = daoProvvedimento;
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

        try {
            switch (method) {

                case "GET" -> {
                    if (hasSub) {
                        if (!isAdmin) { App.sendError(ex, 403, "Accesso negato"); return; }
                        int id;
                        try { id = Integer.parseInt(segs[3]); }
                        catch (NumberFormatException e) { App.sendError(ex, 400, "ID non valido"); return; }
                        Provvedimento p = daoProvvedimento.getByID(id);
                        if (p == null) { App.sendError(ex, 404, "Provvedimento non trovato"); return; }
                        App.sendJson(ex, 200, toJson(p));
                    } else {
                        String query = ex.getRequestURI().getQuery();
                        if (query != null && query.startsWith("detenuto=")) {
                            // Loggati possono vedere i provvedimenti del detenuto
                            // (la verifica dell'accesso al detenuto è già fatta a monte)
                            String mat = query.substring(9);
                            App.sendJson(ex, 200, toJsonArray(daoProvvedimento.getByDetenuto(mat)));
                        } else if (query != null && query.startsWith("tipo=")) {
                            if (!isAdmin) { App.sendError(ex, 403, "Accesso negato"); return; }
                            Provvedimento.Tipo tipo;
                            try { tipo = Provvedimento.Tipo.valueOf(query.substring(5)); }
                            catch (IllegalArgumentException e) { App.sendError(ex, 400, "Tipo non valido"); return; }
                            App.sendJson(ex, 200, toJsonArray(daoProvvedimento.getByTipo(tipo)));
                        } else {
                            if (!isAdmin) { App.sendError(ex, 403, "Accesso negato"); return; }
                            App.sendJson(ex, 200, toJsonArray(daoProvvedimento.getAll()));
                        }
                    }
                }

                case "POST" -> {
                    if (!isAdmin) { App.sendError(ex, 403, "Accesso negato"); return; }
                    Map<String, String> b = App.parseJson(App.readBody(ex));
                    boolean ok = daoProvvedimento.insert(fromMap(b));
                    if (ok) App.sendOk(ex, "");
                    else    App.sendError(ex, 500, "Errore inserimento");
                }

                case "DELETE" -> {
                    if (!isAdmin) { App.sendError(ex, 403, "Accesso negato"); return; }
                    if (!hasSub) { App.sendError(ex, 400, "ID mancante"); return; }
                    int id;
                    try { id = Integer.parseInt(segs[3]); }
                    catch (NumberFormatException e) { App.sendError(ex, 400, "ID non valido"); return; }
                    boolean ok = daoProvvedimento.delete(id);
                    if (ok) App.sendOk(ex, "");
                    else    App.sendError(ex, 404, "Provvedimento non trovato");
                }

                default -> App.sendError(ex, 405, "Metodo non consentito");
            }
        } catch (Exception e) {
            App.sendError(ex, 500, e.getMessage());
        }
    }

    // "tipo", "data", "motivazione" usati dall'HTML nel modal info detenuto
    static String toJson(Provvedimento p) {
        return "{" +
            "\"id\":"             + p.getNumeroProv()                        + "," +
            "\"tipo\":\""         + p.getTipo().name()                       + "\"," +
            "\"data\":\""         + p.getDataEmissione()                     + "\"," +
            "\"motivazione\":\""  + App.escJson(p.getMotivazione())          + "\"," +
            "\"matricolaDet\":\"" + App.escJson(p.getMatricolaDetenuto())    + "\"," +
            "\"matricola\":\""    + App.escJson(p.getMatricola())            + "\"" +
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

    private Provvedimento fromMap(Map<String, String> b) {
        return new Provvedimento(
            0, // NumeroProv auto-generato dal DB
            Provvedimento.Tipo.valueOf(b.getOrDefault("tipo", "Lieve")),
            b.getOrDefault("motivazione", ""),
            LocalDate.parse(b.getOrDefault("dataEmissione", LocalDate.now().toString())),
            b.getOrDefault("matricolaDetenuto", ""),
            b.getOrDefault("matricola", "")
        );
    }
}