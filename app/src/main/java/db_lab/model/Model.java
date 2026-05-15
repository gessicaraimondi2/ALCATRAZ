package db_lab.model;

import db_lab.data.*;
import java.sql.SQLException;
import java.util.List;

public interface Model {

    // --------------------------------------------------------------- //
    // VISITATORI                                                      //
    // --------------------------------------------------------------- //
    boolean inserisciVisitatore(Visitatore v) throws SQLException;
    Visitatore getVisitatore(int accountID) throws SQLException;
    Visitatore getVisitatoreByEmail(String email) throws SQLException;
    List<Visitatore> getTuttiVisitatori() throws SQLException;
    boolean aggiornaVisitatore(Visitatore v) throws SQLException;
    boolean eliminaVisitatore(int accountID) throws SQLException;
    Visitatore loginVisitatore(String email, String password) throws SQLException;

    // --------------------------------------------------------------- //
    // AMMINISTRATORI                                                  //
    // --------------------------------------------------------------- //
    boolean inserisciAmministratore(Amministratore a) throws SQLException;
    Amministratore getAmministratore(int accountID) throws SQLException;
    List<Amministratore> getTuttiAmministratori() throws SQLException;
    Amministratore loginAmministratore(String email, String password) throws SQLException;
    boolean aggiornaAmministratore(Amministratore a) throws SQLException;
    boolean eliminaAmministratore(int accountID) throws SQLException;

    // --------------------------------------------------------------- //
    // PERSONALE                                                       //
    // --------------------------------------------------------------- //
    boolean inserisciPersonale(Personale p) throws SQLException;
    Personale getPersonaleByMatricola(String matricola) throws SQLException;
    List<Personale> getTuttoPersonale() throws SQLException;
    List<Personale> getPersonaleByRuolo(Personale.Ruolo ruolo) throws SQLException;
    boolean aggiornaPersonale(Personale p) throws SQLException;
    boolean eliminaPersonale(String matricola) throws SQLException;

    // --------------------------------------------------------------- //
    // DETENUTI                                                        //
    // --------------------------------------------------------------- //
    boolean inserisciDetenuto(Detenuto d) throws SQLException;
    Detenuto getDetenutoByMatricola(String matricola) throws SQLException;
    List<Detenuto> getDetenuti() throws SQLException;
    List<Detenuto> getDetenutiByCella(String sezione, String cella) throws SQLException;
    List<Detenuto> getDetenutiBySezione(String sezione) throws SQLException;
    boolean aggiornaDetenuto(Detenuto d) throws SQLException;
    boolean eliminaDetenuto(String matricola) throws SQLException;

    // --------------------------------------------------------------- //
    // PRENOTAZIONI                                                    //
    // --------------------------------------------------------------- //
    boolean inserisciPrenotazione(Prenotazione p) throws SQLException;
    Prenotazione getPrenotazione(int idPrenotazione) throws SQLException;
    List<Prenotazione> getPrenotazioniByVisitatore(int accountID) throws SQLException;
    List<Prenotazione> getPrenotazioniByDetenuto(String matricolaDetenuto) throws SQLException;
    List<Prenotazione> getPrenotazioniInAttesa() throws SQLException;
    boolean aggiornaEsitoPrenotazione(int idPrenotazione, Prenotazione.EsitoPrenotazione esito, String motivo) throws SQLException;
    boolean eliminaPrenotazione(int idPrenotazione) throws SQLException;

    // --------------------------------------------------------------- //
    // VISITE                                                          //
    // --------------------------------------------------------------- //
    boolean inserisciVisita(Visita v) throws SQLException;
    Visita getVisita(int numeroVisita) throws SQLException;
    Visita getVisitaByPrenotazione(int idPrenotazione) throws SQLException;
    List<Visita> getTutteVisite() throws SQLException;
    List<Visita> getVisiteEffettuate() throws SQLException;
    boolean aggiornaEsitoVisita(int numeroVisita, Visita.EsitoVisita esito) throws SQLException;
    boolean eliminaVisita(int numeroVisita) throws SQLException;

    // --------------------------------------------------------------- //
    // PROVVEDIMENTI DISCIPLINARI                                      //
    // --------------------------------------------------------------- //
    boolean inserisciProvvedimento(Provvedimento p) throws SQLException;
    Provvedimento getProvvedimento(int numeroProv) throws SQLException;
    List<Provvedimento> getProvvedimentiByDetenuto(String matricolaDetenuto) throws SQLException;
    List<Provvedimento> getProvvedimentiByTipo(Provvedimento.Tipo tipo) throws SQLException;
    List<Provvedimento> getTuttiProvvedimenti() throws SQLException;
    boolean eliminaProvvedimento(int numeroProv) throws SQLException;

    // --------------------------------------------------------------- //
    // CORSI DI REINSERIMENTO                                          //
    // --------------------------------------------------------------- //
    boolean inserisciCorso(Corso c) throws SQLException;
    Corso getCorso(int codiceCorso) throws SQLException;
    List<Corso> getTuttiCorsi() throws SQLException;
    List<Corso> getCorsiByEducatore(String matricola) throws SQLException;
    boolean aggiornaCorso(Corso c) throws SQLException;
    boolean eliminaCorso(int codiceCorso) throws SQLException;

    // --------------------------------------------------------------- //
    // STATISTICHE                                                     //
    // --------------------------------------------------------------- //
    double getTassoPartecipazione() throws SQLException;
    double getMediaDetenutiPerSezione() throws SQLException;
    int getNumeroPrenotazioniInAttesa() throws SQLException;
    void stampaDetenutiPerStato() throws SQLException;
}
