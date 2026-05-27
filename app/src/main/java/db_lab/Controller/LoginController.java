package db_lab.Controller;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import db_lab.App;
import db_lab.data.DAOAmministratore;
import db_lab.data.DAOVisitatore;
import db_lab.data.Amministratore;
import db_lab.data.Visitatore;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * LoginController — riscritto senza Model/DBModel.
 *
 * Riceve direttamente i DAO di cui ha bisogno invece di passare
 * per l'interfaccia Model che era solo un pass-through.
 */
public class LoginController implements HttpHandler {

    private final DAOAmministratore daoAmministratore;
    private final DAOVisitatore     daoVisitatore;

    // Token → "ruolo:accountID" — condivisa tra tutti i Controller
    public static final ConcurrentHashMap<String, String> SESSIONS = new ConcurrentHashMap<>();

    public LoginController(DAOAmministratore daoAmministratore, DAOVisitatore daoVisitatore) {
        this.daoAmministratore = daoAmministratore;
        this.daoVisitatore     = daoVisitatore;
    }

    @Override
    public void handle(HttpExchange ex) throws IOException {
        if (App.handleCors(ex)) return;

        if (!"POST".equalsIgnoreCase(ex.getRequestMethod())) {
            App.sendError(ex, 405, "Metodo non consentito");
            return;
        }

        Map<String, String> body = App.parseJson(App.readBody(ex));
        String email = body.getOrDefault("email",    "").trim();
        String pw    = body.getOrDefault("password", "").trim();
        String ruolo = body.getOrDefault("ruolo",    "visitatore").trim();

        if (email.isEmpty() || pw.isEmpty()) {
            App.sendError(ex, 400, "Email e password obbligatorie");
            return;
        }

        try {
            if ("amministratore".equalsIgnoreCase(ruolo)) {
                Amministratore a = daoAmministratore.login(email, pw);
                if (a == null) { App.sendError(ex, 401, "Credenziali errate"); return; }

                String token = java.util.UUID.randomUUID().toString();
                SESSIONS.put(token, "amministratore:" + a.getAccountID());

                App.sendJson(ex, 200,
                    "{\"ok\":true,\"ruolo\":\"amministratore\"" +
                    ",\"nome\":\"" + App.escJson(a.getNome() + " " + a.getCognome()) + "\"" +
                    ",\"token\":\"" + token + "\"}");

            } else {
                Visitatore v = daoVisitatore.login(email, pw);
                if (v == null) { App.sendError(ex, 401, "Credenziali errate"); return; }

                String token = java.util.UUID.randomUUID().toString();
                SESSIONS.put(token, "visitatore:" + v.getAccountID());

                App.sendJson(ex, 200,
                    "{\"ok\":true,\"ruolo\":\"visitatore\"" +
                    ",\"nome\":\"" + App.escJson(v.getNome() + " " + v.getCognome()) + "\"" +
                    ",\"token\":\"" + token + "\"}");
            }

        } catch (Exception e) {
            App.sendError(ex, 500, e.getMessage());
        }
    }

    // ── Utility di sessione — usate dagli altri Controller ────────────

    public static String getSession(HttpExchange ex) {
        String auth = ex.getRequestHeaders().getFirst("Authorization");
        if (auth == null || !auth.startsWith("Bearer ")) return null;
        return SESSIONS.get(auth.substring(7).trim());
    }

    public static boolean isAdmin(HttpExchange ex) {
        String s = getSession(ex);
        return s != null && s.startsWith("amministratore:");
    }

    public static int getAccountId(HttpExchange ex) {
        String s = getSession(ex);
        if (s == null) return -1;
        try { return Integer.parseInt(s.split(":")[1]); }
        catch (Exception e) { return -1; }
    }
}