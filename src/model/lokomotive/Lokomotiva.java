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
    protected char smijer;
    protected int positionX;
    protected int positionY;

    public Lokomotiva(String vrstaPogona) {
        this.oznaka = rand.nextInt((999999 - 111111)) + 111111;
        this.snaga = 50;

        switch (vrstaPogona){
            case "p":
                this.vrstaPogona = PARNA;
            case "d":
                this.vrstaPogona = DIZEL;
            case "e":
                this.vrstaPogona = ELEKTRICNA;
        }

    }

    public void setOznaka(int oznaka) { this.oznaka = oznaka; }

    public void setSnaga(int snaga) { this.snaga = snaga; }

    public void setVrstaPogona(String vrstaPogona) { this.vrstaPogona = vrstaPogona; }

    public int getOznaka() { return oznaka;}

    public int getSnaga() { return snaga;}

    public String getVrstaPogona() { return vrstaPogona;}

    public char getSmijer() { return smijer; }

    public void setSmijer(char smijer) { this.smijer = smijer; }

    public int getPositionX() {
        return positionX;
    }

    public void setPositionX(int positionX) {
        this.positionX = positionX;
    }

    public int getPositionY() {
        return positionY;
    }

    public void setPositionY(int positionY) {
        this.positionY = positionY;
    }

    public void setPosition(int x, int y){
        positionX = x;
        positionY = y;
    }

    public String toString(){
        return "Oznaka: " + oznaka + " Vrsta Pogona: " + vrstaPogona + " Snaga: " + snaga;
    }

}