package db_lab.Controller;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import db_lab.App;
import db_lab.data.DAODetenuto;
import db_lab.data.DAOPrenotazione;
import db_lab.data.Detenuto;
import db_lab.data.Prenotazione;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

/**
 * GET    /api/detenuti              → lista tutti (admin)
 * GET    /api/detenuti?sezione=A    → filtra per sezione (admin)
 * GET    /api/detenuti/{matricola}  → singolo (admin o visitatore con prenotazione confermata)
 * POST   /api/detenuti              → inserisci (admin)
 * PUT    /api/detenuti/{matricola}  → aggiorna (admin)
 * DELETE /api/detenuti/{matricola}  → elimina (admin)
 */
public class DetenutoController implements HttpHandler {

    private final DAODetenuto     daoDetenuto;
    private final DAOPrenotazione daoPrenotazione;

    public DetenutoController(DAODetenuto daoDetenuto, DAOPrenotazione daoPrenotazione) {
        this.daoDetenuto     = daoDetenuto;
        this.daoPrenotazione = daoPrenotazione;
    }

    @Override
    public void handle(HttpExchange ex) throws IOException {
        if (App.handleCors(ex)) return;

        String   path    = ex.getRequestURI().getPath();
        String   method  = ex.getRequestMethod().toUpperCase();
        String[] segs    = path.split("/");
        boolean  hasSub  = segs.length > 3 && !segs[3].isEmpty();

        boolean isAdmin    = LoginController.isAdmin(ex);
        boolean isLoggedIn = LoginController.getSession(ex) != null;

        if (!isLoggedIn) { App.sendError(ex, 401, "Non autenticato"); return; }
        if (!isAdmin && !(method.equals("GET") && hasSub)) {
            App.sendError(ex, 403, "Accesso negato"); return;
        }

        try {
            switch (method) {

                case "GET" -> {
                    if (hasSub) {
                        String matricola = segs[3];
                        if (!isAdmin) {
                            int accountId = LoginController.getAccountId(ex);
                            boolean hasPren = daoPrenotazione.getByVisitatore(accountId).stream()
                                .anyMatch(p -> p.getMatricolaDetenuto().equals(matricola) &&
                                    p.getEsitoPrenotazione() == Prenotazione.EsitoPrenotazione.Confermata);
                            if (!hasPren) {
                                App.sendError(ex, 403, "Nessuna visita confermata per questo detenuto");
                                return;
                            }
                        }
                        Detenuto d = daoDetenuto.getByMatricola(matricola);
                        if (d == null) { App.sendError(ex, 404, "Detenuto non trovato"); return; }
                        App.sendJson(ex, 200, toJson(d));
                    } else {
                        if (!isAdmin) { App.sendError(ex, 403, "Accesso negato"); return; }
                        String query = ex.getRequestURI().getQuery();
                        List<Detenuto> lista = (query != null && query.startsWith("sezione="))
                            ? daoDetenuto.getBySezione(query.substring(8))
                            : daoDetenuto.getAll();
                        App.sendJson(ex, 200, toJsonArray(lista));
                    }
                }

                case "POST" -> {
                    Map<String, String> b = App.parseJson(App.readBody(ex));
                    boolean ok = daoDetenuto.insert(fromMap(b, LoginController.getAccountId(ex)));
                    if (ok) App.sendOk(ex, "");
                    else    App.sendError(ex, 500, "Errore inserimento");
                }

                case "PUT" -> {
                    if (!hasSub) { App.sendError(ex, 400, "Matricola mancante"); return; }
                    Detenuto d = daoDetenuto.getByMatricola(segs[3]);
                    if (d == null) { App.sendError(ex, 404, "Detenuto non trovato"); return; }
                    Map<String, String> b = App.parseJson(App.readBody(ex));
                    if (b.containsKey("reato"))          d.setReato(b.get("reato"));
                    if (b.containsKey("numeroSezione"))  d.setNumeroSezione(b.get("numeroSezione"));
                    if (b.containsKey("numeroCella"))    d.setNumeroCella(b.get("numeroCella"));
                    if (b.containsKey("statoDellaPena")) d.setStatoDellaPena(Detenuto.StatoDellaPena.valueOf(b.get("statoDellaPena")));
                    boolean ok = daoDetenuto.update(d);
                    if (ok) App.sendOk(ex, "");
                    else    App.sendError(ex, 500, "Errore aggiornamento");
                }

                case "DELETE" -> {
                    if (!hasSub) { App.sendError(ex, 400, "Matricola mancante"); return; }
                    boolean ok = daoDetenuto.delete(segs[3]);
                    if (ok) App.sendOk(ex, "");
                    else    App.sendError(ex, 404, "Detenuto non trovato");
                }

                default -> App.sendError(ex, 405, "Metodo non consentito");
            }
        } catch (Exception e) {
            App.sendError(ex, 500, e.getMessage());
        }
    }

    static String toJson(Detenuto d) {
        return "{" +
            "\"matricola\":\""     + App.escJson(d.getMatricolaDetenuto()) + "\"," +
            "\"nome\":\""          + App.escJson(d.getNome())              + "\"," +
            "\"cognome\":\""       + App.escJson(d.getCognome())           + "\"," +
            "\"dataNascita\":\""   + d.getDataDiNascita()                  + "\"," +
            "\"codiceFiscale\":\"" + App.escJson(d.getCodiceFiscale())     + "\"," +
            "\"dataIngresso\":\""  + d.getDataIngresso()                   + "\"," +
            "\"durataPena\":\""    + App.escJson(d.getDurataPena())        + "\"," +
            "\"reato\":\""         + App.escJson(d.getReato())             + "\"," +
            "\"stato\":\""         + d.getStatoDellaPena()                 + "\"," +
            "\"numeroSezione\":\"" + App.escJson(d.getNumeroSezione())     + "\"," +
            "\"numeroCella\":\""   + App.escJson(d.getNumeroCella())       + "\"" +
            "}";
    }

    static String toJsonArray(List<Detenuto> lista) {
        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < lista.size(); i++) {
            if (i > 0) sb.append(",");
            sb.append(toJson(lista.get(i)));
        }
        return sb.append("]").toString();
    }

    private Detenuto fromMap(Map<String, String> b, int adminId) {
        return new Detenuto(
            b.getOrDefault("matricola",    ""),
            b.getOrDefault("nome",         ""),
            b.getOrDefault("cognome",      ""),
            LocalDate.parse(b.getOrDefault("dataNascita",   LocalDate.now().toString())),
            b.getOrDefault("codiceFiscale", ""),
            LocalDate.parse(b.getOrDefault("dataIngresso",  LocalDate.now().toString())),
            b.getOrDefault("durataPena",   ""),
            b.getOrDefault("reato",        ""),
            Detenuto.StatoDellaPena.valueOf(b.getOrDefault("statoDellaPena", "In_corso")),
            adminId,
            b.getOrDefault("numeroSezione", null),
            b.getOrDefault("numeroCella",   null)
        );
    }
}