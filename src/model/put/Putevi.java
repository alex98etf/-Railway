package model.put;

public class Putevi {

    public static final String PRUGA = "pruga";
    public static final String PRUGA_STRUJA = "pruga_struja";
    public static final String KOLOVOZ = "kolovoz";
    public static final String PRELAZ = "prelaz";
    public static final String STANICA = "statica";

    private String vrsta;

    public Putevi(char vrsta){
        if(vrsta == 'a' || vrsta == 'A' ||vrsta == 'k')
            this.vrsta = KOLOVOZ;
        else if(vrsta == 'v')
            this.vrsta = PRUGA;
        else if(vrsta == 's')
            this.vrsta = STANICA;
        else if(vrsta == 'p')
            this.vrsta = PRELAZ;
        else
            this.vrsta = null;
    }
    public Putevi(String vrsta){
        if(KOLOVOZ.equals(vrsta))
            this.vrsta = KOLOVOZ;
        else if(PRUGA.equals(vrsta))
            this.vrsta = PRUGA;
        else if(STANICA.equals(vrsta))
            this.vrsta = STANICA;
        else if(PRELAZ.equals(vrsta))
            this.vrsta = PRELAZ;
        else
            this.vrsta = null;
    }
    public String getVrsta() {
        return vrsta;
    }
    public void setVrsta(String vrsta) {
        this.vrsta = vrsta;
    }
    @Override
    public String toString(){
        return "PUT " + vrsta;
    }

}
