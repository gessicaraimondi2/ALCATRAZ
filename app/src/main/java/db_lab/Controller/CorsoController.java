package db_lab.Controller;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import db_lab.App;
import db_lab.data.Corso;
import db_lab.model.Model;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

/**
 * GET    /api/corsi                  → tutti
 * GET    /api/corsi?educatore=M001   → filtro educatore
 * POST   /api/corsi                  → inserisci
 * PUT    /api/corsi/{id}             → aggiorna titolo/descrizione
 * DELETE /api/corsi/{id}             → elimina
 */
public class CorsoController implements HttpHandler {

    private final Model model;

    public CorsoController(Model model) {
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
                    List<Corso> lista;
                    if (query != null && query.startsWith("educatore=")) {
                        lista = model.getCorsiByEducatore(query.substring(10));
                    } else {
                        lista = model.getTuttiCorsi();
                    }
                    App.sendJson(ex, 200, toJsonArray(lista));
                }
                case "POST" -> {
                    Map<String, String> b = App.parseJson(App.readBody(ex));
                    Corso c = new Corso(
                        0,
                        b.getOrDefault("titolo", ""),
                        b.getOrDefault("descrizione", ""),
                        LocalDate.parse(b.getOrDefault("dataInizio", LocalDate.now().toString())),
                        LocalDate.parse(b.getOrDefault("dataFine",   LocalDate.now().toString())),
                        Corso.Tipologia.valueOf(b.getOrDefault("tipologia", "Professionale")),
                        0,
                        b.getOrDefault("matricolaEducatore", "")
                    );
                    boolean ok = model.inserisciCorso(c);
                    if (ok) App.sendOk(ex, "");
                    else    App.sendError(ex, 500, "Errore inserimento");
                }
                case "PUT" -> {
                    if (!hasSub) { App.sendError(ex, 400, "ID mancante"); return; }
                    int id = Integer.parseInt(segs[3]);
                    Corso c = model.getCorso(id);
                    if (c == null) { App.sendError(ex, 404, "Corso non trovato"); return; }
                    Map<String, String> b = App.parseJson(App.readBody(ex));
                    if (b.containsKey("titolo"))      c.setTitolo(b.get("titolo"));
                    if (b.containsKey("descrizione")) c.setDescrizione(b.get("descrizione"));
                    boolean ok = model.aggiornaCorso(c);
                    if (ok) App.sendOk(ex, "");
                    else    App.sendError(ex, 500, "Errore aggiornamento");
                }
                case "DELETE" -> {
                    if (!hasSub) { App.sendError(ex, 400, "ID mancante"); return; }
                    boolean ok = model.eliminaCorso(Integer.parseInt(segs[3]));
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
            "\"id\":"              + c.getCodiceCorso()               + "," +
            "\"titolo\":\""        + App.escJson(c.getTitolo())       + "\"," +
            "\"descrizione\":\""   + App.escJson(c.getDescrizione())  + "\"," +
            "\"tipologia\":\""     + c.getTipologia()                 + "\"," +
            "\"dataInizio\":\""    + c.getDataInizio()                + "\"," +
            "\"dataFine\":\""      + c.getDataFine()                  + "\"," +
            "\"educatore\":\""     + App.escJson(c.getMatricola())    + "\"" +
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
}