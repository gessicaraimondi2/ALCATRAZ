package db_lab;

import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpExchange;

import db_lab.data.*;
import db_lab.Controller.*;

import java.io.*;
import java.net.InetSocketAddress;
import java.nio.file.*;
import java.sql.Connection;
import java.util.*;

public class App {

    static final int    PORT       = 8080;
    static final String STATIC_DIR = "app/src/main/java/db_lab/view";

    public static void main(String[] args) throws Exception {

        Connection connection;
        try {
            connection = DAOUtils.localMySQLConnection("alcatraz", "root", "BananaInPigiama!2");
            System.out.println("[DB] Connesso a MySQL.");
        } catch (Exception e) {
            System.err.println("[ERRORE] Impossibile connettersi: " + e.getMessage());
            return;
        }

        var daoAmministratore = new DAOAmministratore(connection);
        var daoVisitatore     = new DAOVisitatore(connection);
        var daoDetenuto       = new DAODetenuto(connection);
        var daoPersonale      = new DAOPersonale(connection);
        var daoPrenotazione   = new DAOPrenotazione(connection);
        var daoVisita         = new DAOVisita(connection);
        var daoProvvedimento  = new DAOProvvedimento(connection);
        var daoCorso          = new DAOCorso(connection);
        var daoIscrizione     = new DAOIscrizione(connection);
        var daoStatistiche    = new DAOStatistiche(connection);

        HttpServer server = HttpServer.create(new InetSocketAddress(PORT), 0);

        server.createContext("/", new StaticHandler());

        server.createContext("/api/login",         new LoginController(daoAmministratore, daoVisitatore));
        server.createContext("/api/register",      new RegisterController(daoVisitatore));
        server.createContext("/api/detenuti",      new DetenutoController(daoDetenuto, daoPrenotazione));
        server.createContext("/api/prenotazioni",  new PrenotazioneController(daoPrenotazione));
        server.createContext("/api/personale",     new PersonaleController(daoPersonale));
        server.createContext("/api/corsi",         new CorsoController(daoCorso));
        server.createContext("/api/iscrizioni",    new IscrizioneController(daoIscrizione, daoDetenuto)); // ← daoDetenuto aggiunto
        server.createContext("/api/visite",        new VisitaController(daoVisita));
        server.createContext("/api/provvedimenti", new ProvvedimentoController(daoProvvedimento));
        server.createContext("/api/statistiche",   new StatisticheController(daoStatistiche));

        server.setExecutor(null);
        server.start();

        System.out.println("Server avviato su http://localhost:" + PORT);

        try {
            String os = System.getProperty("os.name").toLowerCase();
            if (os.contains("win"))       Runtime.getRuntime().exec("rundll32 url.dll,FileProtocolHandler http://localhost:" + PORT);
            else if (os.contains("mac"))  Runtime.getRuntime().exec("open http://localhost:" + PORT);
            else                          Runtime.getRuntime().exec("xdg-open http://localhost:" + PORT);
        } catch (Exception ignored) {}

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            try { connection.close(); } catch (Exception ignored) {}
            server.stop(0);
            System.out.println("[Server] Arrestato.");
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
            if (uriPath.equals("/")) uriPath = "/Index.html";
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
    //  UTILITY — usate da tutti i Controller                            //
    // ================================================================== //

    public static String readBody(HttpExchange ex) throws IOException {
        return new String(ex.getRequestBody().readAllBytes());
    }

    public static void sendJson(HttpExchange ex, int code, String json) throws IOException {
        byte[] bytes = json.getBytes("UTF-8");
        ex.getResponseHeaders().set("Content-Type", "application/json; charset=UTF-8");
        ex.getResponseHeaders().set("Access-Control-Allow-Origin", "*");
        ex.sendResponseHeaders(code, bytes.length);
        ex.getResponseBody().write(bytes);
        ex.getResponseBody().close();
    }

    public static void sendError(HttpExchange ex, int code, String msg) throws IOException {
        sendJson(ex, code, "{\"ok\":false,\"message\":\"" + escJson(msg) + "\"}");
    }

    public static void sendOk(HttpExchange ex, String extraFields) throws IOException {
        sendJson(ex, 200, "{\"ok\":true" + (extraFields.isEmpty() ? "" : "," + extraFields) + "}");
    }

    public static String escJson(String s) {
        if (s == null) return "";
        return s.replace("\\", "\\\\").replace("\"", "\\\"")
                .replace("\n", "\\n").replace("\r", "\\r");
    }

    public static Map<String, String> parseJson(String json) {
        Map<String, String> map = new LinkedHashMap<>();
        if (json == null || json.isBlank()) return map;
        json = json.trim();
        if (json.startsWith("{")) json = json.substring(1);
        if (json.endsWith("}"))   json = json.substring(0, json.length() - 1);
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

    public static String lastSegment(HttpExchange ex) {
        String[] parts = ex.getRequestURI().getPath().split("/");
        return parts[parts.length - 1];
    }

    public static boolean handleCors(HttpExchange ex) throws IOException {
        ex.getResponseHeaders().set("Access-Control-Allow-Origin",  "*");
        ex.getResponseHeaders().set("Access-Control-Allow-Methods", "GET,POST,PUT,DELETE,OPTIONS");
        ex.getResponseHeaders().set("Access-Control-Allow-Headers", "Content-Type,Authorization");
        if ("OPTIONS".equalsIgnoreCase(ex.getRequestMethod())) {
            ex.sendResponseHeaders(204, -1);
            return true;
        }
        return false;
    }
}