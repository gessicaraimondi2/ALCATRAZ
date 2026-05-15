package db_lab.view;

import java.util.List;
import db_lab.data.*;

public class DashboardAdminView {

    public void showDetenuti(List<Detenuto> detenuti) {
        System.out.println("=== LISTA DETENUTI ===");
        detenuti.forEach(System.out::println);
    }

    public void showPersonale(List<Personale> personale) {
        System.out.println("=== LISTA PERSONALE ===");
        personale.forEach(System.out::println);
    }

    public void showCorsi(List<Corso> corsi) {
        System.out.println("=== LISTA CORSI ===");
        corsi.forEach(System.out::println);
    }

    public void showMessage(String msg) {
        System.out.println("[ADMIN] " + msg);
    }

    public void showError(String msg) {
        System.out.println("[ADMIN ERROR] " + msg);
    }
}
