package db_lab.Controller;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import db_lab.App;
import db_lab.data.Visitatore;
import db_lab.model.Model;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Map;

/**
 * POST /api/register
 * Body JSON: { "nome":"...", "cognome":"...", "email":"...", "password":"...", "codiceFiscale":"..." }
 */
public class RegisterController implements HttpHandler {

    private final Model model;

    public RegisterController(Model model) {
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
        String nome    = body.getOrDefault("nome", "").trim();
        String cognome = body.getOrDefault("cognome", "").trim();
        String email   = body.getOrDefault("email", "").trim();
        String pw      = body.getOrDefault("password", "").trim();
        String cf      = body.getOrDefault("codiceFiscale", "").trim();

        if (nome.isEmpty() || cognome.isEmpty() || email.isEmpty() || pw.isEmpty() || cf.isEmpty()) {
            App.sendError(ex, 400, "Tutti i campi sono obbligatori");
            return;
        }

        try {
            Visitatore v = new Visitatore(0, email, pw, LocalDate.now(), nome, cognome, cf);
            boolean ok = model.inserisciVisitatore(v);
            if (ok) App.sendOk(ex, "\"message\":\"Registrazione completata\"");
            else    App.sendError(ex, 409, "Email già registrata o errore di inserimento");
        } catch (Exception e) {
            App.sendError(ex, 500, e.getMessage());
        }
    }
}