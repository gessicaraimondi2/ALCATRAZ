package db_lab.model;

import db_lab.data.*;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class MockedModel implements Model {

    private final List<Visitatore> visitatori = new ArrayList<>();
    private final List<Detenuto> detenuti = new ArrayList<>();
    private final List<Visita> visite = new ArrayList<>();
    private final List<Prenotazione> prenotazioni = new ArrayList<>();
    private final List<Provvedimento> provvedimenti = new ArrayList<>();
    private final List<Iscrizione> iscrizioni = new ArrayList<>();

    public MockedModel() {
        // Dati finti di esempio
        visitatori.add(new Visitatore(
                1, "test@mail.com", "1234",
                LocalDate.now(), "Mario", "Rossi", "RSSMRA80A01H501Z"));

        detenuti.add(new Detenuto(
                "D001", "Luca", "Bianchi",
                LocalDate.of(1980, 5, 10),
                "CF123", LocalDate.now(),
                "5 anni", "Furto",
                Detenuto.StatoDellaPena.In_corso,
                10, "A", "1"));
    }

    // --------------------------------------------------------------- //
    // VISITATORI                                                      //
    // --------------------------------------------------------------- //
    @Override
    public boolean inserisciVisitatore(Visitatore v) {
        return visitatori.add(v);
    }

    @Override
    public Visitatore getVisitatore(int accountID) {
        return visitatori.stream()
                .filter(v -> v.getAccountID() == accountID)
                .findFirst()
                .orElse(null);
    }

    @Override
    public Visitatore getVisitatoreByEmail(String email) {
        return visitatori.stream()
                .filter(v -> v.getEmail().equals(email))
                .findFirst()
                .orElse(null);
    }

    @Override
    public List<Visitatore> getTuttiVisitatori() {
        return visitatori;
    }

    @Override
    public boolean aggiornaVisitatore(Visitatore v) {
        int idx = -1;
        for (int i = 0; i < visitatori.size(); i++) {
            if (visitatori.get(i).getAccountID() == v.getAccountID()) {
                idx = i;
                break;
            }
        }
        if (idx >= 0) {
            visitatori.set(idx, v);
            return true;
        }
        return false;
    }

    @Override
    public boolean eliminaVisitatore(int accountID) {
        return visitatori.removeIf(v -> v.getAccountID() == accountID);
    }

    @Override
    public Visitatore loginVisitatore(String email, String password) {
        return visitatori.stream()
                .filter(v -> v.getEmail().equals(email) && v.getPassword().equals(password))
                .findFirst()
                .orElse(null);
    }

    // --------------------------------------------------------------- //
    // AMMINISTRATORI                                                  //
    // --------------------------------------------------------------- //
    @Override
    public boolean inserisciAmministratore(Amministratore a) {
        return true;
    }

    @Override
    public Amministratore getAmministratore(int accountID) {
        return null;
    }

    @Override
    public List<Amministratore> getTuttiAmministratori() {
        return new ArrayList<>();
    }

    @Override
    public Amministratore loginAmministratore(String email, String password) {
        return null;
    }

    @Override
    public boolean aggiornaAmministratore(Amministratore a) {
        return true;
    }

    @Override
    public boolean eliminaAmministratore(int accountID) {
        return true;
    }

    // --------------------------------------------------------------- //
    // PERSONALE                                                       //
    // --------------------------------------------------------------- //
    @Override
    public boolean inserisciPersonale(Personale p) {
        return true;
    }

    @Override
    public Personale getPersonaleByMatricola(String matricola) {
        return null;
    }

    @Override
    public List<Personale> getTuttoPersonale() {
        return new ArrayList<>();
    }

    @Override
    public List<Personale> getPersonaleByRuolo(Personale.Ruolo ruolo) {
        return new ArrayList<>();
    }

    @Override
    public boolean aggiornaPersonale(Personale p) {
        return true;
    }

    @Override
    public boolean eliminaPersonale(String matricola) {
        return true;
    }

    // --------------------------------------------------------------- //
    // DETENUTI                                                        //
    // --------------------------------------------------------------- //
    @Override
    public boolean inserisciDetenuto(Detenuto d) {
        return detenuti.add(d);
    }

    @Override
    public Detenuto getDetenutoByMatricola(String matricola) {
        return detenuti.stream()
                .filter(d -> d.getMatricolaDetenuto().equals(matricola))
                .findFirst()
                .orElse(null);
    }

    @Override
    public List<Detenuto> getDetenuti() {
        return detenuti;
    }

    @Override
    public List<Detenuto> getDetenutiByCella(String sezione, String cella) {
        List<Detenuto> res = new ArrayList<>();
        for (Detenuto d : detenuti) {
            if (d.getNumeroSezione().equals(sezione) && d.getNumeroCella().equals(cella)) {
                res.add(d);
            }
        }
        return res;
    }

    @Override
    public List<Detenuto> getDetenutiBySezione(String sezione) {
        List<Detenuto> res = new ArrayList<>();
        for (Detenuto d : detenuti) {
            if (d.getNumeroSezione().equals(sezione)) {
                res.add(d);
            }
        }
        return res;
    }

    @Override
    public boolean aggiornaDetenuto(Detenuto d) {
        int idx = -1;
        for (int i = 0; i < detenuti.size(); i++) {
            if (detenuti.get(i).getMatricolaDetenuto().equals(d.getMatricolaDetenuto())) {
                idx = i;
                break;
            }
        }
        if (idx >= 0) {
            detenuti.set(idx, d);
            return true;
        }
        return false;
    }

    @Override
    public boolean eliminaDetenuto(String matricola) {
        return detenuti.removeIf(d -> d.getMatricolaDetenuto().equals(matricola));
    }

    // --------------------------------------------------------------- //
    // PRENOTAZIONI                                                    //
    // --------------------------------------------------------------- //
    @Override
    public boolean inserisciPrenotazione(Prenotazione p) {
        return prenotazioni.add(p);
    }

    @Override
    public Prenotazione getPrenotazione(int idPrenotazione) {
        return prenotazioni.stream()
                .filter(pr -> pr.getIdPrenotazione() == idPrenotazione)
                .findFirst()
                .orElse(null);
    }

    @Override
    public List<Prenotazione> getPrenotazioniByVisitatore(int accountID) {
        List<Prenotazione> res = new ArrayList<>();
        for (Prenotazione p : prenotazioni) {
            if (p.getEffAccountID() == accountID) res.add(p);
        }
        return res;
    }

    @Override
    public List<Prenotazione> getPrenotazioniByDetenuto(String matricolaDetenuto) {
        List<Prenotazione> res = new ArrayList<>();
        for (Prenotazione p : prenotazioni) {
            if (p.getMatricolaDetenuto().equals(matricolaDetenuto)) res.add(p);
        }
        return res;
    }

    @Override
    public List<Prenotazione> getPrenotazioniInAttesa() {
        List<Prenotazione> res = new ArrayList<>();
        for (Prenotazione p : prenotazioni) {
            if (p.getEsitoPrenotazione() == Prenotazione.EsitoPrenotazione.In_attesa) res.add(p);
        }
        return res;
    }

    @Override
    public boolean aggiornaEsitoPrenotazione(int idPrenotazione, Prenotazione.EsitoPrenotazione esito, String motivo) {
        Prenotazione p = getPrenotazione(idPrenotazione);
        if (p == null) return false;
        p.setEsitoPrenotazione(esito);
        p.setMotivoRifiuto(motivo);
        return true;
    }

    @Override
    public boolean eliminaPrenotazione(int idPrenotazione) {
        return prenotazioni.removeIf(p -> p.getIdPrenotazione() == idPrenotazione);
    }

    // --------------------------------------------------------------- //
    // VISITE                                                          //
    // --------------------------------------------------------------- //
    @Override
    public boolean inserisciVisita(Visita v) {
        return visite.add(v);
    }

    @Override
    public Visita getVisita(int numeroVisita) {
        return visite.stream()
                .filter(v -> v.getNumeroVisita() == numeroVisita)
                .findFirst()
                .orElse(null);
    }

    @Override
    public Visita getVisitaByPrenotazione(int idPrenotazione) {
        return visite.stream()
                .filter(v -> v.getIdPrenotazione() == idPrenotazione)
                .findFirst()
                .orElse(null);
    }

    @Override
    public List<Visita> getTutteVisite() {
        return visite;
    }

    @Override
    public List<Visita> getVisiteEffettuate() {
        List<Visita> res = new ArrayList<>();
        for (Visita v : visite) {
            if (v.getEsitoVisita() == Visita.EsitoVisita.Effettuata) res.add(v);
        }
        return res;
    }

    @Override
    public boolean aggiornaEsitoVisita(int numeroVisita, Visita.EsitoVisita esito) {
        Visita v = getVisita(numeroVisita);
        if (v == null) return false;
        v.setEsitoVisita(esito);
        return true;
    }

    @Override
    public boolean eliminaVisita(int numeroVisita) {
        return visite.removeIf(v -> v.getNumeroVisita() == numeroVisita);
    }

    // --------------------------------------------------------------- //
    // PROVVEDIMENTI DISCIPLINARI                                      //
    // --------------------------------------------------------------- //
    @Override
    public boolean inserisciProvvedimento(Provvedimento p) {
        return provvedimenti.add(p);
    }

    @Override
    public Provvedimento getProvvedimento(int numeroProv) {
        return provvedimenti.stream()
                .filter(pr -> pr.getNumeroProv() == numeroProv)
                .findFirst()
                .orElse(null);
    }

    @Override
    public List<Provvedimento> getProvvedimentiByDetenuto(String matricolaDetenuto) {
        List<Provvedimento> res = new ArrayList<>();
        for (Provvedimento p : provvedimenti) {
            if (p.getMatricolaDetenuto().equals(matricolaDetenuto)) res.add(p);
        }
        return res;
    }

    @Override
    public List<Provvedimento> getProvvedimentiByTipo(Provvedimento.Tipo tipo) {
        List<Provvedimento> res = new ArrayList<>();
        for (Provvedimento p : provvedimenti) {
            if (p.getTipo() == tipo) res.add(p);
        }
        return res;
    }

    @Override
    public List<Provvedimento> getTuttiProvvedimenti() {
        return provvedimenti;
    }

    @Override
    public boolean eliminaProvvedimento(int numeroProv) {
        return provvedimenti.removeIf(p -> p.getNumeroProv() == numeroProv);
    }
        // --------------------------------------------------------------- //
    // CORSI DI REINSERIMENTO                                          //
    // --------------------------------------------------------------- //
    @Override
    public boolean inserisciCorso(Corso c) {
        // puoi creare una lista corsi se vuoi, per ora mock semplice:
        return true;
    }

    @Override
    public Corso getCorso(int codiceCorso) {
        return null; // mock
    }

    @Override
    public List<Corso> getTuttiCorsi() {
        return new ArrayList<>(); // mock
    }

    @Override
    public List<Corso> getCorsiByEducatore(String matricola) {
        return new ArrayList<>(); // mock
    }

    @Override
    public boolean aggiornaCorso(Corso c) {
        return true; // mock
    }

    @Override
    public boolean eliminaCorso(int codiceCorso) {
        return true; // mock
    }


    // --------------------------------------------------------------- //
    // STATISTICHE                                                     //
    // --------------------------------------------------------------- //
    @Override
    public double getTassoPartecipazione() {
        return 50.0;
    }

    @Override
    public double getMediaDetenutiPerSezione() {
        return 10.0;
    }

    @Override
    public int getNumeroPrenotazioniInAttesa() {
        return (int) prenotazioni.stream()
                .filter(p -> p.getEsitoPrenotazione() == Prenotazione.EsitoPrenotazione.In_attesa)
                .count();
    }

    @Override
    public void stampaDetenutiPerStato() {
        System.out.println("Mock: detenuti per stato pena");
    }

    // --------------------------------------------------------------- //
    // ISCRIZIONI AI CORSI                                            //
    // --------------------------------------------------------------- //
    @Override
    public boolean iscriviDetenutoACorso(String matricolaDetenuto, int codiceCorso) {
        iscrizioni.add(new Iscrizione(matricolaDetenuto, codiceCorso, null));
        return true;
    }

    @Override
    public List<Iscrizione> getIscrizioniByDetenuto(String matricolaDetenuto) {
        return iscrizioni.stream()
                .filter(i -> i.getMatricolaDetenuto().equals(matricolaDetenuto))
                .collect(java.util.stream.Collectors.toList());
    }

    @Override
    public List<Iscrizione> getIscrizioniByCorso(int codiceCorso) {
        return iscrizioni.stream()
                .filter(i -> i.getCodiceCorso() == codiceCorso)
                .collect(java.util.stream.Collectors.toList());
    }

    @Override
    public boolean aggiornaEsitoIscrizione(String matricolaDetenuto, int codiceCorso, Iscrizione.Esito esito) {
        for (Iscrizione i : iscrizioni) {
            if (i.getMatricolaDetenuto().equals(matricolaDetenuto) && i.getCodiceCorso() == codiceCorso) {
                i.setEsito(esito);
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean eliminaIscrizione(String matricolaDetenuto, int codiceCorso) {
        return iscrizioni.removeIf(i ->
            i.getMatricolaDetenuto().equals(matricolaDetenuto) && i.getCodiceCorso() == codiceCorso);
    }
}