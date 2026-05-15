package db_lab.data;

public class Iscrizione {

    public enum Esito {
        Superato,
        Non_superato,
        In_corso,
        Sospeso;

        public String toDBString() {
            return switch (this) {
                case Superato     -> "Superato";
                case Non_superato -> "Non superato";
                case In_corso     -> "In corso";
                case Sospeso      -> "Sospeso";
            };
        }
    }

    private String matricolaDetenuto;   // FK -> DETENUTO (parte della PK)
    private int codiceCorso;             // FK -> CORSO_DI_REINSERIMENTO (parte della PK)
    private Esito esito;                 // nullable

    public Iscrizione() {}

    public Iscrizione(String matricolaDetenuto, int codiceCorso, Esito esito) {
        this.matricolaDetenuto = matricolaDetenuto;
        this.codiceCorso       = codiceCorso;
        this.esito             = esito;
    }

    public String getMatricolaDetenuto()                       { return matricolaDetenuto; }
    public void setMatricolaDetenuto(String matricolaDetenuto) { this.matricolaDetenuto = matricolaDetenuto; }

    public int getCodiceCorso()                                { return codiceCorso; }
    public void setCodiceCorso(int codiceCorso)                { this.codiceCorso = codiceCorso; }

    public Esito getEsito()                                    { return esito; }
    public void setEsito(Esito esito)                          { this.esito = esito; }

    @Override
    public String toString() {
        return "Iscrizione{detenuto='" + matricolaDetenuto + "', corso=" + codiceCorso + ", esito=" + esito + "}";
    }
}
