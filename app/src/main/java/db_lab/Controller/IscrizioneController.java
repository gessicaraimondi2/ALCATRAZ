package db_lab.Controller;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import db_lab.App;
import db_lab.data.Iscrizione;
import db_lab.model.Model;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * Gestione iscrizioni detenuti ai corsi di reinserimento (solo admin).
 *
 * POST   /api/iscrizioni                               → iscrive detenuto a corso
 * GET    /api/iscrizioni?detenuto=D001                 → iscrizioni di un detenuto
 * GET    /api/iscrizioni?corso=3                       → iscritti a un corso
 * PUT    /api/iscrizioni/{matricola}/{codiceCorso}      → aggiorna esito
 * DELETE /api/iscrizioni/{matricola}/{codiceCorso}      → rimuove iscrizione
 */
public class IscrizioneController implements HttpHandler {

    private final Model model;

    public IscrizioneController(Model model) {
        this.model = model;
    }

    @Override
    public void handle(HttpExchange ex) throws IOException {
        if (App.handleCors(ex)) return;

        boolean isAdmin    = LoginController.isAdmin(ex);
        boolean isLoggedIn = LoginController.getSession(ex) != null;
        if (!isLoggedIn) { App.sendError(ex, 401, "Non autenticato"); return; }

        // Visitatori: solo GET ?detenuto=X con prenotazione confermata
        if (!isAdmin && !ex.getRequestMethod().equalsIgnoreCase("GET")) {
            App.sendError(ex, 403, "Accesso negato"); return;
        }

        String path   = ex.getRequestURI().getPath();
        String method = ex.getRequestMethod().toUpperCase();
        String[] segs = path.split("/");

        try {
            switch (method) {

                case "GET" -> {
                    String query = ex.getRequestURI().getQuery();
                    List<Iscrizione> lista;
                    if (query != null && query.startsWith("detenuto=")) {
                        String matricola = query.substring(9);
                        if (!isAdmin) {
                            int accountId = LoginController.getAccountId(ex);
                            boolean ok = model.getPrenotazioniByVisitatore(accountId).stream()
                                .anyMatch(p -> p.getMatricolaDetenuto().equals(matricola) &&
                                    p.getEsitoPrenotazione() == db_lab.data.Prenotazione.EsitoPrenotazione.Confermata);
                            if (!ok) { App.sendError(ex, 403, "Nessuna visita confermata per questo detenuto"); return; }
                        }
                        lista = model.getIscrizioniByDetenuto(matricola);
                    } else if (query != null && query.startsWith("corso=")) {
                        if (!isAdmin) { App.sendError(ex, 403, "Accesso negato"); return; }
                        lista = model.getIscrizioniByCorso(Integer.parseInt(query.substring(6)));
                    } else {
                        App.sendError(ex, 400, "Parametro richiesto: detenuto= oppure corso=");
                        return;
                    }
                    App.sendJson(ex, 200, toJsonArray(lista));
                }

                case "POST" -> {
                    Map<String, String> b = App.parseJson(App.readBody(ex));
                    String matricola = b.getOrDefault("matricolaDetenuto", "");
                    int codiceCorso  = Integer.parseInt(b.getOrDefault("codiceCorso", "0"));
                    if (matricola.isEmpty() || codiceCorso == 0) {
                        App.sendError(ex, 400, "matricolaDetenuto e codiceCorso obbligatori");
                        return;
                    }
                    boolean ok = model.iscriviDetenutoACorso(matricola, codiceCorso);
                    if (ok) App.sendOk(ex, "");
                    else    App.sendError(ex, 500, "Errore inserimento (detenuto/corso inesistente o già iscritto)");
                }

                case "PUT" -> {
                    // PUT /api/iscrizioni/{matricola}/{codiceCorso}
                    if (segs.length < 5) { App.sendError(ex, 400, "Percorso: /api/iscrizioni/{matricola}/{codiceCorso}"); return; }
                    String matricola = segs[3];
                    int codiceCorso  = Integer.parseInt(segs[4]);
                    Map<String, String> b = App.parseJson(App.readBody(ex));
                    Iscrizione.Esito esito = Iscrizione.Esito.valueOf(b.getOrDefault("esito", "In_corso"));
                    boolean ok = model.aggiornaEsitoIscrizione(matricola, codiceCorso, esito);
                    if (ok) App.sendOk(ex, "");
                    else    App.sendError(ex, 404, "Iscrizione non trovata");
                }

                case "DELETE" -> {
                    // DELETE /api/iscrizioni/{matricola}/{codiceCorso}
                    if (segs.length < 5) { App.sendError(ex, 400, "Percorso: /api/iscrizioni/{matricola}/{codiceCorso}"); return; }
                    String matricola = segs[3];
                    int codiceCorso  = Integer.parseInt(segs[4]);
                    boolean ok = model.eliminaIscrizione(matricola, codiceCorso);
                    if (ok) App.sendOk(ex, "");
                    else    App.sendError(ex, 404, "Iscrizione non trovata");
                }

                default -> App.sendError(ex, 405, "Metodo non consentito");
            }
        } catch (Exception e) {
            App.sendError(ex, 500, e.getMessage());
        }
    }

    static String toJson(Iscrizione i) {
        return "{" +
            "\"matricolaDetenuto\":\"" + App.escJson(i.getMatricolaDetenuto()) + "\"," +
            "\"codiceCorso\":"         + i.getCodiceCorso()                    + "," +
            "\"esito\":"               + (i.getEsito() == null ? "null" :
                                          "\"" + i.getEsito().name() + "\"") +
            "}";
    }

    static String toJsonArray(List<Iscrizione> lista) {
        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < lista.size(); i++) {
            if (i > 0) sb.append(",");
            sb.append(toJson(lista.get(i)));
        }
        return sb.append("]").toString();
    }
}