package sk.upjs.mongodb_project;

/**
 * Wrapper for a query. Fuj.
 */
public class MenoAPriezvisko {
    String meno;
    String priezvisko;
    
    public String getMeno() {
        return meno;
    }

    public void setMeno(String meno) {
        this.meno = meno;
    }

    public String getPriezvisko() {
        return priezvisko;
    }

    public void setPriezvisko(String priezvisko) {
        this.priezvisko = priezvisko;
    }

    @Override
    public String toString() {
        return "MenoAPriezvisko{" +
                "meno='" + meno + '\'' +
                ", priezvisko='" + priezvisko + '\'' +
                '}';
    }
}
