package db_lab;

import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpExchange;

import db_lab.model.DBModel;
import db_lab.model.Model;
import db_lab.data.*;
import db_lab.Controller.*;

import java.io.*;
import java.net.*;
import java.nio.file.*;
import java.sql.SQLException;
import java.util.*;

/**
 * App — avvia un server HTTP sulla porta 8080.
 *
 * Route statiche : GET  /          → Index.html
 *                  GET  /static/*  → file in src/main/java/db_lab/view/
 *
 * API JSON       : POST /api/login
 *                  POST /api/register
 *                  GET  /api/detenuti
 *                  GET  /api/detenuti/{matricola}
 *                  POST /api/detenuti
 *                  PUT  /api/detenuti/{matricola}
 *                  DELETE /api/detenuti/{matricola}
 *                  GET  /api/prenotazioni
 *                  GET  /api/prenotazioni/attesa
 *                  POST /api/prenotazioni
 *                  PUT  /api/prenotazioni/{id}/esito
 *                  DELETE /api/prenotazioni/{id}
 *                  GET  /api/personale
 *                  POST /api/personale
 *                  DELETE /api/personale/{matricola}
 *                  GET  /api/corsi
 *                  POST /api/corsi
 *                  PUT  /api/corsi/{id}
 *                  DELETE /api/corsi/{id}
 *                  GET  /api/visite
 *                  POST /api/visite
 *                  PUT  /api/visite/{id}/esito
 *                  DELETE /api/visite/{id}
 *                  GET  /api/provvedimenti
 *                  POST /api/provvedimenti
 *                  DELETE /api/provvedimenti/{id}
 *                  GET  /api/statistiche
 */
public class App {

    static final int    PORT       = 8080;
    // Cartella degli HTML — relativa alla working directory (root del progetto Gradle)
    static final String STATIC_DIR = "app/src/main/java/db_lab/view";

    public static void main(String[] args) throws Exception {

        // ── Connessione DB ──────────────────────────────────────────────
        try {
            DBConnection.getConnection();
            System.out.println("[DB] Connesso a MySQL.");
        } catch (SQLException e) {
            System.err.println("[ERRORE] Impossibile connettersi al database: " + e.getMessage());
            System.err.println("Verifica che MySQL sia avviato e il DB 'alcatraz' esista.");
            return;
        }

        Model model = new DBModel();

        // ── Server HTTP ─────────────────────────────────────────────────
        HttpServer server = HttpServer.create(new InetSocketAddress(PORT), 0);

        // File statici (HTML, CSS, JS)
        server.createContext("/", new StaticHandler());

        // ── API ─────────────────────────────────────────────────────────
        server.createContext("/api/login",          new LoginController(model));
        server.createContext("/api/register",       new RegisterController(model));
        server.createContext("/api/detenuti",       new DetenutoController(model));
        server.createContext("/api/prenotazioni",   new PrenotazioneController(model));
        server.createContext("/api/personale",      new PersonaleController(model));
        server.createContext("/api/corsi",          new CorsoController(model));
        server.createContext("/api/visite",         new VisitaController(model));
        server.createContext("/api/provvedimenti",  new ProvvedimentoController(model));
        server.createContext("/api/statistiche",    new StatisticheController(model));

        server.setExecutor(null); // thread di default
        server.start();

        System.out.println("╔══════════════════════════════════════════╗");
        System.out.println("║   SGC — Sistema di Gestione Carceraria   ║");
        System.out.println("║    Server avviato su http://localhost:" + PORT + "  ║");
        System.out.println("╚══════════════════════════════════════════╝");
        System.out.println("Apri il browser su: http://localhost:" + PORT);

        // Apri automaticamente il browser (funziona su Windows/Mac/Linux)
        try {
            Desktop.getDesktop().browse(new URI("http://localhost:" + PORT));
        } catch (Exception ignored) {}

        // Shutdown hook per chiudere il DB
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            DBConnection.closeConnection();
            server.stop(0);
            System.out.println("\n[Server] Arrestato.");
        }));
    }

    // ================================================================== //
    //  HANDLER FILE STATICI                                              //
    // ================================================================== //
    static class StaticHandler implements HttpHandler {

        private static final Map<String, String> MIME = Map.of(
            "html", "text/html; charset=UTF-8",
            "css",  "text/css; charset=UTF-8",
            "js",   "application/javascript; charset=UTF-8",
            "png",  "image/png",
            "jpg",  "image/jpeg",
            "ico",  "image/x-icon"
        );

        @Override
        public void handle(HttpExchange ex) throws IOException {
            String uriPath = ex.getRequestURI().getPath();

            // "/" → Index.html
            if (uriPath.equals("/")) uriPath = "/Index.html";

            // Rimuovi lo slash iniziale e costruisci il path fisico
            String relative = uriPath.startsWith("/") ? uriPath.substring(1) : uriPath;
            Path filePath = Path.of(STATIC_DIR, relative);

            if (!Files.exists(filePath) || Files.isDirectory(filePath)) {
                String body = "404 Not Found: " + uriPath;
                ex.sendResponseHeaders(404, body.length());
                ex.getResponseBody().write(body.getBytes());
                ex.getResponseBody().close();
                return;
            }

            String ext  = relative.contains(".") ? relative.substring(relative.lastIndexOf('.') + 1) : "txt";
            String mime = MIME.getOrDefault(ext, "application/octet-stream");

            byte[] bytes = Files.readAllBytes(filePath);
            ex.getResponseHeaders().set("Content-Type", mime);
            ex.sendResponseHeaders(200, bytes.length);
            ex.getResponseBody().write(bytes);
            ex.getResponseBody().close();
        }
    }

    // ================================================================== //
    //  UTILITY STATICHE usate da tutti i Controller                     //
    // ================================================================== //

    /** Legge il body della richiesta come stringa. */
    public static String readBody(HttpExchange ex) throws IOException {
        return new String(ex.getRequestBody().readAllBytes());
    }

    /** Invia una risposta JSON con il codice HTTP specificato. */
    public static void sendJson(HttpExchange ex, int code, String json) throws IOException {
        byte[] bytes = json.getBytes("UTF-8");
        ex.getResponseHeaders().set("Content-Type", "application/json; charset=UTF-8");
        ex.getResponseHeaders().set("Access-Control-Allow-Origin", "*");
        ex.sendResponseHeaders(code, bytes.length);
        ex.getResponseBody().write(bytes);
        ex.getResponseBody().close();
    }

    /** Risposta di errore JSON standard. */
    public static void sendError(HttpExchange ex, int code, String msg) throws IOException {
        sendJson(ex, code, "{\"ok\":false,\"message\":\"" + escJson(msg) + "\"}");
    }

    /** Risposta di successo JSON standard. */
    public static void sendOk(HttpExchange ex, String extraFields) throws IOException {
        sendJson(ex, 200, "{\"ok\":true" + (extraFields.isEmpty() ? "" : "," + extraFields) + "}");
    }

    /** Escape minimo per inserire stringhe in JSON a mano. */
    public static String escJson(String s) {
        if (s == null) return "";
        return s.replace("\\", "\\\\").replace("\"", "\\\"")
                .replace("\n", "\\n").replace("\r", "\\r");
    }

    /**
     * Parsifica un corpo JSON minimale del tipo {"k":"v","k2":"v2"}.
     * Sufficiente per i form semplici — nessuna dipendenza esterna.
     */
    public static Map<String, String> parseJson(String json) {
        Map<String, String> map = new LinkedHashMap<>();
        if (json == null || json.isBlank()) return map;
        // Rimuovi { }
        json = json.trim();
        if (json.startsWith("{")) json = json.substring(1);
        if (json.endsWith("}"))   json = json.substring(0, json.length() - 1);
        // Dividi per virgola (non perfetto con valori annidati, ma sufficiente qui)
        for (String pair : json.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)")) {
            String[] kv = pair.split(":", 2);
            if (kv.length == 2) {
                String key = kv[0].trim().replaceAll("\"", "");
                String val = kv[1].trim().replaceAll("\"", "");
                map.put(key, val);
            }
        }
        return map;
    }

    /** Estrae il segmento finale di un path. Es: /api/detenuti/M001 → M001 */
    public static String lastSegment(HttpExchange ex) {
        String[] parts = ex.getRequestURI().getPath().split("/");
        return parts[parts.length - 1];
    }

    /** Gestisce le preflight CORS. Ritorna true se la richiesta era OPTIONS. */
    public static boolean handleCors(HttpExchange ex) throws IOException {
        ex.getResponseHeaders().set("Access-Control-Allow-Origin",  "*");
        ex.getResponseHeaders().set("Access-Control-Allow-Methods", "GET,POST,PUT,DELETE,OPTIONS");
        ex.getResponseHeaders().set("Access-Control-Allow-Headers", "Content-Type");
        if ("OPTIONS".equalsIgnoreCase(ex.getRequestMethod())) {
            ex.sendResponseHeaders(204, -1);
            return true;
        }
        return false;
    }
}