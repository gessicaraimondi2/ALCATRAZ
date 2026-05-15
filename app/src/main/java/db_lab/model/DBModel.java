package db_lab.model;

import db_lab.data.*;
import java.sql.SQLException;
import java.util.List;

public class DBModel implements Model {

    private final DAOVisitatore daoVisitatore = new DAOVisitatore();
    private final DAOAmministratore daoAmministratore = new DAOAmministratore();
    private final DAOPersonale daoPersonale = new DAOPersonale();
    private final DAODetenuto daoDetenuto = new DAODetenuto();
    private final DAOPrenotazione daoPrenotazione = new DAOPrenotazione();
    private final DAOVisita daoVisita = new DAOVisita();
    private final DAOProvvedimento daoProvvedimento = new DAOProvvedimento();
    private final DAOCorso daoCorso = new DAOCorso();
    private final DAOStatistiche daoStatistiche = new DAOStatistiche();

    // --------------------------------------------------------------- //
    // VISITATORI                                                      //
    // --------------------------------------------------------------- //
    @Override
    public boolean inserisciVisitatore(Visitatore v) throws SQLException {
        return daoVisitatore.insert(v);
    }

    @Override
    public Visitatore getVisitatore(int accountID) throws SQLException {
        return daoVisitatore.getByID(accountID);
    }

    @Override
    public Visitatore getVisitatoreByEmail(String email) throws SQLException {
        return daoVisitatore.getByEmail(email);
    }

    @Override
    public List<Visitatore> getTuttiVisitatori() throws SQLException {
        return daoVisitatore.getAll();
    }

    @Override
    public boolean aggiornaVisitatore(Visitatore v) throws SQLException {
        return daoVisitatore.update(v);
    }

    @Override
    public boolean eliminaVisitatore(int accountID) throws SQLException {
        return daoVisitatore.delete(accountID);
    }

    @Override
    public Visitatore loginVisitatore(String email, String password) throws SQLException {
        return daoVisitatore.login(email, password);
    }

    // --------------------------------------------------------------- //
    // AMMINISTRATORI                                                  //
    // --------------------------------------------------------------- //
    @Override
    public boolean inserisciAmministratore(Amministratore a) throws SQLException {
        return daoAmministratore.insert(a);
    }

    @Override
    public Amministratore getAmministratore(int accountID) throws SQLException {
        return daoAmministratore.getByID(accountID);
    }

    @Override
    public List<Amministratore> getTuttiAmministratori() throws SQLException {
        return daoAmministratore.getAll();
    }

    @Override
    public Amministratore loginAmministratore(String email, String password) throws SQLException {
        return daoAmministratore.login(email, password);
    }

    @Override
    public boolean aggiornaAmministratore(Amministratore a) throws SQLException {
        return daoAmministratore.update(a);
    }

    @Override
    public boolean eliminaAmministratore(int accountID) throws SQLException {
        return daoAmministratore.delete(accountID);
    }

    // --------------------------------------------------------------- //
    // PERSONALE                                                       //
    // --------------------------------------------------------------- //
    @Override
    public boolean inserisciPersonale(Personale p) throws SQLException {
        return daoPersonale.insert(p);
    }

    @Override
    public Personale getPersonaleByMatricola(String matricola) throws SQLException {
        return daoPersonale.getByMatricola(matricola);
    }

    @Override
    public List<Personale> getTuttoPersonale() throws SQLException {
        return daoPersonale.getAll();
    }

    @Override
    public List<Personale> getPersonaleByRuolo(Personale.Ruolo ruolo) throws SQLException {
        return daoPersonale.getByRuolo(ruolo);
    }

    @Override
    public boolean aggiornaPersonale(Personale p) throws SQLException {
        return daoPersonale.update(p);
    }

    @Override
    public boolean eliminaPersonale(String matricola) throws SQLException {
        return daoPersonale.delete(matricola);
    }

    // --------------------------------------------------------------- //
    // DETENUTI                                                        //
    // --------------------------------------------------------------- //
    @Override
    public boolean inserisciDetenuto(Detenuto d) throws SQLException {
        return daoDetenuto.insert(d);
    }

    @Override
    public Detenuto getDetenutoByMatricola(String matricola) throws SQLException {
        return daoDetenuto.getByMatricola(matricola);
    }

    @Override
    public List<Detenuto> getDetenuti() throws SQLException {
        return daoDetenuto.getAll();
    }

    @Override
    public List<Detenuto> getDetenutiByCella(String sezione, String cella) throws SQLException {
        return daoDetenuto.getByCella(sezione, cella);
    }

    @Override
    public List<Detenuto> getDetenutiBySezione(String sezione) throws SQLException {
        return daoDetenuto.getBySezione(sezione);
    }

    @Override
    public boolean aggiornaDetenuto(Detenuto d) throws SQLException {
        return daoDetenuto.update(d);
    }

    @Override
    public boolean eliminaDetenuto(String matricola) throws SQLException {
        return daoDetenuto.delete(matricola);
    }

    // --------------------------------------------------------------- //
    // PRENOTAZIONI                                                    //
    // --------------------------------------------------------------- //
    @Override
    public boolean inserisciPrenotazione(Prenotazione p) throws SQLException {
        return daoPrenotazione.insert(p);
    }

    @Override
    public Prenotazione getPrenotazione(int idPrenotazione) throws SQLException {
        return daoPrenotazione.getByID(idPrenotazione);
    }

    @Override
    public List<Prenotazione> getPrenotazioniByVisitatore(int accountID) throws SQLException {
        return daoPrenotazione.getByVisitatore(accountID);
    }

    @Override
    public List<Prenotazione> getPrenotazioniByDetenuto(String matricolaDetenuto) throws SQLException {
        return daoPrenotazione.getByDetenuto(matricolaDetenuto);
    }

    @Override
    public List<Prenotazione> getPrenotazioniInAttesa() throws SQLException {
        return daoPrenotazione.getInAttesa();
    }

    @Override
    public boolean aggiornaEsitoPrenotazione(int idPrenotazione, Prenotazione.EsitoPrenotazione esito, String motivo) throws SQLException {
        return daoPrenotazione.aggiornaEsito(idPrenotazione, esito, motivo);
    }

    @Override
    public boolean eliminaPrenotazione(int idPrenotazione) throws SQLException {
        return daoPrenotazione.delete(idPrenotazione);
    }

    // --------------------------------------------------------------- //
    // VISITE                                                          //
    // --------------------------------------------------------------- //
    @Override
    public boolean inserisciVisita(Visita v) throws SQLException {
        return daoVisita.insert(v);
    }

    @Override
    public Visita getVisita(int numeroVisita) throws SQLException {
        return daoVisita.getByID(numeroVisita);
    }

    @Override
    public Visita getVisitaByPrenotazione(int idPrenotazione) throws SQLException {
        return daoVisita.getByPrenotazione(idPrenotazione);
    }

    @Override
    public List<Visita> getTutteVisite() throws SQLException {
        return daoVisita.getAll();
    }

    @Override
    public List<Visita> getVisiteEffettuate() throws SQLException {
        return daoVisita.getEffettuate();
    }

    @Override
    public boolean aggiornaEsitoVisita(int numeroVisita, Visita.EsitoVisita esito) throws SQLException {
        return daoVisita.aggiornaEsito(numeroVisita, esito);
    }

    @Override
    public boolean eliminaVisita(int numeroVisita) throws SQLException {
        return daoVisita.delete(numeroVisita);
    }

    // --------------------------------------------------------------- //
    // PROVVEDIMENTI DISCIPLINARI                                      //
    // --------------------------------------------------------------- //
    @Override
    public boolean inserisciProvvedimento(Provvedimento p) throws SQLException {
        return daoProvvedimento.insert(p);
    }

    @Override
    public Provvedimento getProvvedimento(int numeroProv) throws SQLException {
        return daoProvvedimento.getByID(numeroProv);
    }

    @Override
    public List<Provvedimento> getProvvedimentiByDetenuto(String matricolaDetenuto) throws SQLException {
        return daoProvvedimento.getByDetenuto(matricolaDetenuto);
    }

    @Override
    public List<Provvedimento> getProvvedimentiByTipo(Provvedimento.Tipo tipo) throws SQLException {
        return daoProvvedimento.getByTipo(tipo);
    }

    @Override
    public List<Provvedimento> getTuttiProvvedimenti() throws SQLException {
        return daoProvvedimento.getAll();
    }

    @Override
    public boolean eliminaProvvedimento(int numeroProv) throws SQLException {
        return daoProvvedimento.delete(numeroProv);
    }
        // --------------------------------------------------------------- //
    // CORSI DI REINSERIMENTO                                          //
    // --------------------------------------------------------------- //
    @Override
    public boolean inserisciCorso(Corso c) throws SQLException {
        return daoCorso.insert(c);
    }

    @Override
    public Corso getCorso(int codiceCorso) throws SQLException {
        return daoCorso.getByCodice(codiceCorso);
    }

    @Override
    public List<Corso> getTuttiCorsi() throws SQLException {
        return daoCorso.getAll();
    }

    @Override
    public List<Corso> getCorsiByEducatore(String matricola) throws SQLException {
        return daoCorso.getByEducatore(matricola);
    }

    @Override
    public boolean aggiornaCorso(Corso c) throws SQLException {
        return daoCorso.update(c);
    }

    @Override
    public boolean eliminaCorso(int codiceCorso) throws SQLException {
        return daoCorso.delete(codiceCorso);
    }


    // --------------------------------------------------------------- //
    // STATISTICHE                                                     //
    // --------------------------------------------------------------- //
    @Override
    public double getTassoPartecipazione() throws SQLException {
        return daoStatistiche.getTassoPartecipazione();
    }

    @Override
    public double getMediaDetenutiPerSezione() throws SQLException {
        return daoStatistiche.getMediaDetenutiPerSezione();
    }

    @Override
    public int getNumeroPrenotazioniInAttesa() throws SQLException {
        return daoStatistiche.getPrenotazioniInAttesa();
    }

    @Override
    public void stampaDetenutiPerStato() throws SQLException {
        daoStatistiche.stampaDetenutiPerStato();
    }
}
