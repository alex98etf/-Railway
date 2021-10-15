package model.vagoni;

import java.util.Random;
import java.util.logging.Logger;

/**
 * @author Alex
 */
public abstract class Vagon {
    private static final Logger LOGGER = Logger.getLogger(Vagon.class.getName());
    private static final Random rand = new Random();

    protected int oznaka;
    protected int duzina;
    protected char smijer;
    protected int positionX;
    protected int positionY;
    public Vagon(int min, int max) {
        this.duzina = rand.nextInt((max - min)-1) + min;
        this.oznaka = rand.nextInt((999999 - 111111)) + 111111;
    }

    public void setOznaka(int oznaka) { this.oznaka = oznaka; }

    public void setDuzina(int duzina) { this.duzina = duzina; }

    public int getOznaka() { return oznaka; }

    public int getDuzina() { return duzina; }

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

    public abstract String getOpis();

    @Override
    public String toString(){ return this.getClass().getSimpleName() + " id: " + this.oznaka;}

}