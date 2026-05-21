package db_lab.Controller;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import db_lab.App;
import db_lab.data.Detenuto;
import db_lab.model.Model;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

/**
 * GET    /api/detenuti              → lista tutti
 * GET    /api/detenuti?sezione=A    → filtra per sezione
 * GET    /api/detenuti/{matricola}  → singolo
 * POST   /api/detenuti              → inserisci
 * PUT    /api/detenuti/{matricola}  → aggiorna
 * DELETE /api/detenuti/{matricola}  → elimina
 */
public class DetenutoController implements HttpHandler {

    private final Model model;

    public DetenutoController(Model model) {
        this.model = model;
    }

    @Override
    public void handle(HttpExchange ex) throws IOException {
        if (App.handleCors(ex)) return;

        String path   = ex.getRequestURI().getPath();
        String method = ex.getRequestMethod().toUpperCase();
        String[] segments = path.split("/");
        boolean hasSub = segments.length > 3 && !segments[3].isEmpty();

        // I visitatori possono fare solo GET su una matricola specifica,
        // ma solo se hanno una prenotazione confermata per quel detenuto.
        boolean isAdmin     = LoginController.isAdmin(ex);
        boolean isLoggedIn  = LoginController.getSession(ex) != null;

        if (!isLoggedIn) { App.sendError(ex, 401, "Non autenticato"); return; }
        if (!isAdmin && !(method.equals("GET") && hasSub)) {
            App.sendError(ex, 403, "Accesso negato");
            return;
        }

        try {
            switch (method) {

                case "GET" -> {
                    if (hasSub) {
                        String matricola = segments[3];

                        // Visitatore: verifica prenotazione confermata per questo detenuto
                        if (!isAdmin) {
                            int accountId = LoginController.getAccountId(ex);
                            boolean hasPrenConfermata = model
                                .getPrenotazioniByVisitatore(accountId)
                                .stream()
                                .anyMatch(p ->
                                    p.getMatricolaDetenuto().equals(matricola) &&
                                    p.getEsitoPrenotazione() == db_lab.data.Prenotazione.EsitoPrenotazione.Confermata);
                            if (!hasPrenConfermata) {
                                App.sendError(ex, 403, "Nessuna visita confermata per questo detenuto");
                                return;
                            }
                        }

                        Detenuto d = model.getDetenutoByMatricola(matricola);
                        if (d == null) { App.sendError(ex, 404, "Detenuto non trovato"); return; }
                        App.sendJson(ex, 200, toJson(d));
                    } else {
                        // Solo admin può vedere la lista completa
                        if (!isAdmin) { App.sendError(ex, 403, "Accesso negato"); return; }
                        String query = ex.getRequestURI().getQuery();
                        List<Detenuto> lista;
                        if (query != null && query.startsWith("sezione=")) {
                            lista = model.getDetenutiBySezione(query.substring(8));
                        } else {
                            lista = model.getDetenuti();
                        }
                        App.sendJson(ex, 200, toJsonArray(lista));
                    }
                }

                case "POST" -> {
                    Map<String, String> b = App.parseJson(App.readBody(ex));
                    Detenuto d = fromMap(b);
                    boolean ok = model.inserisciDetenuto(d);
                    if (ok) App.sendOk(ex, "");
                    else    App.sendError(ex, 500, "Errore inserimento");
                }

                case "PUT" -> {
                    if (!hasSub) { App.sendError(ex, 400, "Matricola mancante"); return; }
                    Detenuto d = model.getDetenutoByMatricola(segments[3]);
                    if (d == null) { App.sendError(ex, 404, "Detenuto non trovato"); return; }
                    Map<String, String> b = App.parseJson(App.readBody(ex));
                    if (b.containsKey("reato"))          d.setReato(b.get("reato"));
                    if (b.containsKey("numeroSezione"))  d.setNumeroSezione(b.get("numeroSezione"));
                    if (b.containsKey("numeroCella"))    d.setNumeroCella(b.get("numeroCella"));
                    if (b.containsKey("statoDellaPena")) d.setStatoDellaPena(Detenuto.StatoDellaPena.valueOf(b.get("statoDellaPena")));
                    boolean ok = model.aggiornaDetenuto(d);
                    if (ok) App.sendOk(ex, "");
                    else    App.sendError(ex, 500, "Errore aggiornamento");
                }

                case "DELETE" -> {
                    if (!hasSub) { App.sendError(ex, 400, "Matricola mancante"); return; }
                    boolean ok = model.eliminaDetenuto(segments[3]);
                    if (ok) App.sendOk(ex, "");
                    else    App.sendError(ex, 404, "Detenuto non trovato");
                }

                default -> App.sendError(ex, 405, "Metodo non consentito");
            }
        } catch (Exception e) {
            App.sendError(ex, 500, e.getMessage());
        }
    }

    // ── Serializzazione ───────────────────────────────────────────────

    static String toJson(Detenuto d) {
        return "{" +
            "\"matricola\":\""      + App.escJson(d.getMatricolaDetenuto()) + "\"," +
            "\"nome\":\""           + App.escJson(d.getNome())              + "\"," +
            "\"cognome\":\""        + App.escJson(d.getCognome())           + "\"," +
            "\"dataNascita\":\""    + d.getDataDiNascita()                  + "\"," +
            "\"codiceFiscale\":\""  + App.escJson(d.getCodiceFiscale())     + "\"," +
            "\"dataIngresso\":\""   + d.getDataIngresso()                   + "\"," +
            "\"durataPena\":\""     + App.escJson(d.getDurataPena())        + "\"," +
            "\"reato\":\""          + App.escJson(d.getReato())             + "\"," +
            "\"stato\":\""          + d.getStatoDellaPena()                 + "\"," +
            "\"numeroSezione\":\""  + App.escJson(d.getNumeroSezione())     + "\"," +
            "\"numeroCella\":\""    + App.escJson(d.getNumeroCella())       + "\"" +
            "}";
    }

    static String toJsonArray(List<Detenuto> lista) {
        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < lista.size(); i++) {
            if (i > 0) sb.append(",");
            sb.append(toJson(lista.get(i)));
        }
        sb.append("]");
        return sb.toString();
    }

    private Detenuto fromMap(Map<String, String> b) {
        return new Detenuto(
            b.getOrDefault("matricola", ""),
            b.getOrDefault("nome", ""),
            b.getOrDefault("cognome", ""),
            LocalDate.parse(b.getOrDefault("dataNascita", LocalDate.now().toString())),
            b.getOrDefault("codiceFiscale", ""),
            LocalDate.parse(b.getOrDefault("dataIngresso", LocalDate.now().toString())),
            b.getOrDefault("durataPena", ""),
            b.getOrDefault("reato", ""),
            Detenuto.StatoDellaPena.valueOf(b.getOrDefault("statoDellaPena", "In_corso")),
            0,
            b.getOrDefault("numeroSezione", ""),
            b.getOrDefault("numeroCella", "")
        );
    }
}