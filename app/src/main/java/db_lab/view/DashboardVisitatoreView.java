package db_lab.view;

import java.util.List;
import db_lab.data.*;

public class DashboardVisitatoreView {

    public void showPrenotazioni(List<Prenotazione> prenotazioni) {
        System.out.println("=== LE TUE PRENOTAZIONI ===");
        prenotazioni.forEach(System.out::println);
    }

    public void showVisite(List<Visita> visite) {
        System.out.println("=== LE TUE VISITE ===");
        visite.forEach(System.out::println);
    }

    public void showMessage(String msg) {
        System.out.println("[VISITATORE] " + msg);
    }

    public void showError(String msg) {
        System.out.println("[VISITATORE ERROR] " + msg);
    }
    
}
