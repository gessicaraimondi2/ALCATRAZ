package db_lab.Controller;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import db_lab.App;
import db_lab.data.DAOIscrizione;
import db_lab.data.Iscrizione;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * GET    /api/iscrizioni?detenuto=MAT   → iscrizioni di un detenuto (loggato)
 * GET    /api/iscrizioni?corso=ID       → iscrizioni di un corso (admin)
 * POST   /api/iscrizioni                → iscrivi detenuto a corso (admin)
 * PUT    /api/iscrizioni/{mat}/{id}     → aggiorna esito (admin)
 * DELETE /api/iscrizioni/{mat}/{id}     → elimina iscrizione (admin)
 */
public class IscrizioneController implements HttpHandler {

    private final DAOIscrizione daoIscrizione;

    public IscrizioneController(DAOIscrizione daoIscrizione) {
        this.daoIscrizione = daoIscrizione;
    }

    @Override
    public void handle(HttpExchange ex) throws IOException {
        if (App.handleCors(ex)) return;

        String   path      = ex.getRequestURI().getPath();
        String   method    = ex.getRequestMethod().toUpperCase();
        String[] segs      = path.split("/");
        // /api/iscrizioni/{matricola}/{codice} → segs[3]=matricola, segs[4]=codice
        boolean  hasMat    = segs.length > 3 && !segs[3].isEmpty();
        boolean  hasCodice = segs.length > 4 && !segs[4].isEmpty();

        boolean isAdmin    = LoginController.isAdmin(ex);
        boolean isLoggedIn = LoginController.getSession(ex) != null;

        if (!isLoggedIn) { App.sendError(ex, 401, "Non autenticato"); return; }

        try {
            switch (method) {

                case "GET" -> {
                    String query = ex.getRequestURI().getQuery();
                    if (query != null && query.startsWith("detenuto=")) {
                        // Visitatori loggati possono vedere le iscrizioni dei detenuti
                        // per cui hanno una prenotazione confermata (accesso già verificato
                        // a monte da DetenutoController prima di chiamare questo endpoint)
                        String mat = query.substring(9);
                        App.sendJson(ex, 200, toJsonArray(daoIscrizione.getByDetenuto(mat)));
                    } else if (query != null && query.startsWith("corso=")) {
                        if (!isAdmin) { App.sendError(ex, 403, "Accesso negato"); return; }
                        int codice;
                        try { codice = Integer.parseInt(query.substring(6)); }
                        catch (NumberFormatException e) { App.sendError(ex, 400, "ID corso non valido"); return; }
                        App.sendJson(ex, 200, toJsonArray(daoIscrizione.getByCorso(codice)));
                    } else {
                        App.sendError(ex, 400, "Parametro 'detenuto' o 'corso' richiesto");
                    }
                }

                case "POST" -> {
                    if (!isAdmin) { App.sendError(ex, 403, "Accesso negato"); return; }
                    Map<String, String> b = App.parseJson(App.readBody(ex));
                    String mat = b.getOrDefault("matricolaDetenuto", "");
                    int codice;
                    try { codice = Integer.parseInt(b.getOrDefault("codiceCorso", "0")); }
                    catch (NumberFormatException e) { App.sendError(ex, 400, "codiceCorso non valido"); return; }
                    boolean ok = daoIscrizione.insert(mat, codice);
                    if (ok) App.sendOk(ex, "");
                    else    App.sendError(ex, 500, "Errore inserimento iscrizione");
                }

                case "PUT" -> {
                    if (!isAdmin) { App.sendError(ex, 403, "Accesso negato"); return; }
                    if (!hasMat || !hasCodice) { App.sendError(ex, 400, "Matricola e codice corso richiesti"); return; }
                    String mat = segs[3];
                    int codice;
                    try { codice = Integer.parseInt(segs[4]); }
                    catch (NumberFormatException e) { App.sendError(ex, 400, "ID corso non valido"); return; }
                    Map<String, String> b = App.parseJson(App.readBody(ex));
                    String esitoStr = b.get("esito");
                    Iscrizione.Esito esito = (esitoStr == null || esitoStr.isBlank())
                        ? null : Iscrizione.Esito.valueOf(esitoStr);
                    boolean ok = daoIscrizione.updateEsito(mat, codice, esito);
                    if (ok) App.sendOk(ex, "");
                    else    App.sendError(ex, 500, "Errore aggiornamento esito");
                }

                case "DELETE" -> {
                    if (!isAdmin) { App.sendError(ex, 403, "Accesso negato"); return; }
                    if (!hasMat || !hasCodice) { App.sendError(ex, 400, "Matricola e codice corso richiesti"); return; }
                    String mat = segs[3];
                    int codice;
                    try { codice = Integer.parseInt(segs[4]); }
                    catch (NumberFormatException e) { App.sendError(ex, 400, "ID corso non valido"); return; }
                    boolean ok = daoIscrizione.delete(mat, codice);
                    if (ok) App.sendOk(ex, "");
                    else    App.sendError(ex, 404, "Iscrizione non trovata");
                }

                default -> App.sendError(ex, 405, "Metodo non consentito");
            }
        } catch (Exception e) {
            App.sendError(ex, 500, e.getMessage());
        }
    }

    // "codiceCorso" e "esito" usati dall'HTML nel modal info detenuto
    static String toJson(Iscrizione i) {
        String esitoJson = i.getEsito() == null
            ? "null"
            : "\"" + App.escJson(i.getEsito().toDBString()) + "\"";
        return "{" +
            "\"matricolaDetenuto\":\"" + App.escJson(i.getMatricolaDetenuto()) + "\"," +
            "\"codiceCorso\":"         + i.getCodiceCorso()                    + "," +
            "\"esito\":"               + esitoJson                             +
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