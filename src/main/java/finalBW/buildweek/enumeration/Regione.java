package finalBW.buildweek.enumeration;

public enum Regione {
    ABRUZZO("Abruzzo"),
    BASILICATA("Basilicata"),
    CALABRIA("Calabria"),
    CAMPANIA("Campania"),
    EMILIA_ROMAGNA("Emilia Romagna"),
    FRIULI_VENEZIA_GIULIA("Friuli Venezia Giulia"),
    LAZIO("Lazio"),
    LIGURIA("Liguria"),
    LOMBARDIA("Lombardia"),
    MARCHE("Marche"),
    MOLISE("Molise"),
    PIEMONTE("Piemonte"),
    PUGLIA("Puglia"),
    SARDEGNA("Sardegna"),
    SICILIA("Sicilia"),
    TOSCANA("Toscana"),
    TRENTINO_ALTO_ADIGE_SUDTIROL("Trentino Alto Adige"),
    UMBRIA("Umbria"),
    VALLE_D_AOSTA("Valle d'Aosta"),
    VENETO("Veneto");

    private final String name;

    Regione(String name) {
        this.name = name;
    }

    public static Regione fromName(String name) {
        for (Regione regione : Regione.values()) {
            if (regione.getName().equalsIgnoreCase(name.trim())) {
                return regione;
            }
        }
        throw new IllegalArgumentException("Regione non valida: " + name);
    }

    public String getName() {
        return name;
    }
}
