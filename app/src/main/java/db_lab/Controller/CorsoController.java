package db_lab.Controller;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import db_lab.App;
import db_lab.data.Corso;
import db_lab.data.DAOCorso;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

/**
 * GET    /api/corsi                   → lista tutti (loggato)
 * GET    /api/corsi?educatore=MAT     → filtra per educatore (admin)
 * GET    /api/corsi/{id}              → singolo (admin)
 * POST   /api/corsi                   → inserisci (admin)
 * PUT    /api/corsi/{id}              → aggiorna (admin)
 * DELETE /api/corsi/{id}              → elimina (admin)
 */
public class CorsoController implements HttpHandler {

    private final DAOCorso daoCorso;

    public CorsoController(DAOCorso daoCorso) {
        this.daoCorso = daoCorso;
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
                        int codice;
                        try { codice = Integer.parseInt(segs[3]); }
                        catch (NumberFormatException e) { App.sendError(ex, 400, "ID non valido"); return; }
                        Corso c = daoCorso.getByCodice(codice);
                        if (c == null) { App.sendError(ex, 404, "Corso non trovato"); return; }
                        App.sendJson(ex, 200, toJson(c));
                    } else {
                        String query = ex.getRequestURI().getQuery();
                        if (query != null && query.startsWith("educatore=")) {
                            if (!isAdmin) { App.sendError(ex, 403, "Accesso negato"); return; }
                            App.sendJson(ex, 200, toJsonArray(daoCorso.getByEducatore(query.substring(10))));
                        } else {
                            App.sendJson(ex, 200, toJsonArray(daoCorso.getAll()));
                        }
                    }
                }

                case "POST" -> {
                    if (!isAdmin) { App.sendError(ex, 403, "Accesso negato"); return; }

                    Map<String, String> b = App.parseJson(App.readBody(ex));

                    // ✔️ QUI LA MODIFICA: recuperiamo l'AccountID dell'admin loggato
                    int adminId = LoginController.getAccountId(ex);

                    boolean ok = daoCorso.insert(fromMap(b, adminId));
                    if (ok) App.sendOk(ex, "");
                    else    App.sendError(ex, 500, "Errore inserimento");
                }

                case "PUT" -> {
                    if (!isAdmin) { App.sendError(ex, 403, "Accesso negato"); return; }
                    if (!hasSub) { App.sendError(ex, 400, "ID mancante"); return; }
                    int codice;
                    try { codice = Integer.parseInt(segs[3]); }
                    catch (NumberFormatException e) { App.sendError(ex, 400, "ID non valido"); return; }
                    Corso c = daoCorso.getByCodice(codice);
                    if (c == null) { App.sendError(ex, 404, "Corso non trovato"); return; }
                    Map<String, String> b = App.parseJson(App.readBody(ex));
                    if (b.containsKey("titolo"))      c.setTitolo(b.get("titolo"));
                    if (b.containsKey("descrizione")) c.setDescrizione(b.get("descrizione"));
                    if (b.containsKey("dataInizio"))  c.setDataInizio(LocalDate.parse(b.get("dataInizio")));
                    if (b.containsKey("dataFine"))    c.setDataFine(LocalDate.parse(b.get("dataFine")));
                    if (b.containsKey("tipologia"))   c.setTipologia(Corso.Tipologia.valueOf(b.get("tipologia")));
                    if (b.containsKey("matricola"))   c.setMatricola(b.get("matricola"));
                    boolean ok = daoCorso.update(c);
                    if (ok) App.sendOk(ex, "");
                    else    App.sendError(ex, 500, "Errore aggiornamento");
                }

                case "DELETE" -> {
                    if (!isAdmin) { App.sendError(ex, 403, "Accesso negato"); return; }
                    if (!hasSub) { App.sendError(ex, 400, "ID mancante"); return; }
                    int codice;
                    try { codice = Integer.parseInt(segs[3]); }
                    catch (NumberFormatException e) { App.sendError(ex, 400, "ID non valido"); return; }
                    boolean ok = daoCorso.delete(codice);
                    if (ok) App.sendOk(ex, "");
                    else    App.sendError(ex, 404, "Corso non trovato");
                }

                default -> App.sendError(ex, 405, "Metodo non consentito");
            }
        } catch (Exception e) {
            App.sendError(ex, 500, e.getMessage());
        }
    }

    static String toJson(Corso c) {
        return "{" +
            "\"id\":"            + c.getCodiceCorso()              + "," +
            "\"titolo\":\""      + App.escJson(c.getTitolo())      + "\"," +
            "\"descrizione\":\"" + App.escJson(c.getDescrizione()) + "\"," +
            "\"dataInizio\":\""  + c.getDataInizio()               + "\"," +
            "\"dataFine\":\""    + c.getDataFine()                 + "\"," +
            "\"tipologia\":\""   + c.getTipologia().name()         + "\"," +
            "\"matricola\":\""   + App.escJson(c.getMatricola())   + "\"" +
            "}";
    }

    static String toJsonArray(List<Corso> lista) {
        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < lista.size(); i++) {
            if (i > 0) sb.append(",");
            sb.append(toJson(lista.get(i)));
        }
        return sb.append("]").toString();
    }

    private Corso fromMap(Map<String, String> b, int adminId) {
        return new Corso(
            0,
            b.getOrDefault("titolo",      ""),
            b.getOrDefault("descrizione", ""),
            LocalDate.parse(b.getOrDefault("dataInizio", LocalDate.now().toString())),
            LocalDate.parse(b.getOrDefault("dataFine",   LocalDate.now().toString())),
            Corso.Tipologia.valueOf(b.getOrDefault("tipologia", "Professionale")),
            adminId,
            b.getOrDefault("matricola", null)
        );
    }
}
