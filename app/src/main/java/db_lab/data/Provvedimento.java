package db_lab.data;

import java.time.LocalDate;

public class Provvedimento {

    public enum Tipo {
        Lieve,
        Medio,
        Grave
    }

    private int numeroProv;
    private Tipo tipo;
    private String motivazione;
    private LocalDate dataEmissione;
    private String matricolaDetenuto;   // FK -> DETENUTO
    private String matricola;            // FK -> PERSONALE (Guardia)

    public Provvedimento() {}

    public Provvedimento(int numeroProv, Tipo tipo, String motivazione,
                         LocalDate dataEmissione, String matricolaDetenuto, String matricola) {
        this.numeroProv         = numeroProv;
        this.tipo               = tipo;
        this.motivazione        = motivazione;
        this.dataEmissione      = dataEmissione;
        this.matricolaDetenuto  = matricolaDetenuto;
        this.matricola          = matricola;
    }

    public int getNumeroProv()                            { return numeroProv; }
    public void setNumeroProv(int numeroProv)             { this.numeroProv = numeroProv; }

    public Tipo getTipo()                                 { return tipo; }
    public void setTipo(Tipo tipo)                        { this.tipo = tipo; }

    public String getMotivazione()                        { return motivazione; }
    public void setMotivazione(String motivazione)        { this.motivazione = motivazione; }

    public LocalDate getDataEmissione()                   { return dataEmissione; }
    public void setDataEmissione(LocalDate d)             { this.dataEmissione = d; }

    public String getMatricolaDetenuto()                  { return matricolaDetenuto; }
    public void setMatricolaDetenuto(String m)            { this.matricolaDetenuto = m; }

    public String getMatricola()                          { return matricola; }
    public void setMatricola(String matricola)            { this.matricola = matricola; }

    @Override
    public String toString() {
        return "Provvedimento{numero=" + numeroProv + ", tipo=" + tipo + ", detenuto='" + matricolaDetenuto + "'}";
    }
}