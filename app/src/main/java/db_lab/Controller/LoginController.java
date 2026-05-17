package db_lab.Controller;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import db_lab.App;
import db_lab.data.*;
import db_lab.model.Model;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * POST /api/login
 * Body JSON: { "email": "...", "password": "...", "ruolo": "visitatore"|"amministratore" }
 * Risposta:  { "ok": true, "ruolo": "...", "nome": "...", "token": "..." }
 *         o  { "ok": false, "message": "..." }
 *
 * La sessione è gestita con un token in memoria (Map statica) che viene
 * restituito al client e inviato come header Authorization nelle richieste successive.
 */
public class LoginController implements HttpHandler {

    private final Model model;

    // Token → ruolo:accountID  (es. "visitatore:3")
    // Mappa statica condivisa tra tutti i controller per verificare l'autenticazione
    public static final ConcurrentHashMap<String, String> SESSIONS = new ConcurrentHashMap<>();

    public LoginController(Model model) {
        this.model = model;
    }

    @Override
    public void handle(HttpExchange ex) throws IOException {
        if (App.handleCors(ex)) return;

        if (!"POST".equalsIgnoreCase(ex.getRequestMethod())) {
            App.sendError(ex, 405, "Metodo non consentito");
            return;
        }

        Map<String, String> body = App.parseJson(App.readBody(ex));
        String email  = body.getOrDefault("email", "").trim();
        String pw     = body.getOrDefault("password", "").trim();
        String ruolo  = body.getOrDefault("ruolo", "visitatore").trim();

        if (email.isEmpty() || pw.isEmpty()) {
            App.sendError(ex, 400, "Email e password obbligatorie");
            return;
        }

        try {
            if ("amministratore".equalsIgnoreCase(ruolo)) {
                Amministratore a = model.loginAmministratore(email, pw);
                if (a == null) { App.sendError(ex, 401, "Credenziali errate"); return; }

                String token = generateToken();
                SESSIONS.put(token, "amministratore:" + a.getAccountID());

                App.sendJson(ex, 200,
                    "{\"ok\":true,\"ruolo\":\"amministratore\"" +
                    ",\"nome\":\"" + App.escJson(a.getNome() + " " + a.getCognome()) + "\"" +
                    ",\"token\":\"" + token + "\"}");

            } else {
                Visitatore v = model.loginVisitatore(email, pw);
                if (v == null) { App.sendError(ex, 401, "Credenziali errate"); return; }

                String token = generateToken();
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

    // ── Utility ──────────────────────────────────────────────────────────

    private static String generateToken() {
        return java.util.UUID.randomUUID().toString();
    }

    /** Restituisce "visitatore:ID" o "amministratore:ID" dal token, null se non valido. */
    public static String getSession(HttpExchange ex) {
        String auth = ex.getRequestHeaders().getFirst("Authorization");
        if (auth == null || !auth.startsWith("Bearer ")) return null;
        return SESSIONS.get(auth.substring(7).trim());
    }

    /** Ritorna true se la sessione è di un amministratore. */
    public static boolean isAdmin(HttpExchange ex) {
        String s = getSession(ex);
        return s != null && s.startsWith("amministratore:");
    }

    /** Estrae l'accountID dalla sessione (-1 se assente). */
    public static int getAccountId(HttpExchange ex) {
        String s = getSession(ex);
        if (s == null) return -1;
        try { return Integer.parseInt(s.split(":")[1]); }
        catch (Exception e) { return -1; }
    }
}