package model.lokomotive;

import java.util.Random;
import java.util.logging.Logger;

/**
 * @author Alex
 */
public abstract class Lokomotiva {
    private static final String PARNA = "Parna";
    private static final String DIZEL = "Dizel";
    private static final String ELEKTRICNA = "Elektricna";

    private static final Random rand = new Random();

    protected int oznaka;
    protected int snaga;
    protected String vrstaPogona;

    public Lokomotiva(int snaga, int vrsta) {
        this.oznaka = rand.nextInt((999999 - 111111)) + 111111;
        this.snaga = snaga;

        switch (vrsta){
            case 0:
                this.vrstaPogona = PARNA;
            case 1:
                this.vrstaPogona = DIZEL;
            case 2:
                this.vrstaPogona = ELEKTRICNA;
        }

    }

    public void setOznaka(int oznaka) { this.oznaka = oznaka; }

    public void setSnaga(int snaga) { this.snaga = snaga; }

    public void setVrstaPogona(String vrstaPogona) { this.vrstaPogona = vrstaPogona; }

    public int getOznaka() { return oznaka;}

    public int getSnaga() { return snaga;}

    public String getVrstaPogona() { return vrstaPogona;}

    public String toString(){
        return "Oznaka: " + oznaka + " Vrsta Pogona: " + vrstaPogona + " Snaga: " + snaga;
    }

}