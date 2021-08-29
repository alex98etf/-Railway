package model.vagoni;

import java.util.Random;
import java.util.logging.Logger;

/**
 * @author Alex
 */
public abstract class Vagon {
    private static final Logger LOGGER = Logger.getLogger(Vagon.class.getName());
    private static Random rand = new Random();

    protected int oznaka;
    protected int duzina;

    public Vagon() {
        this.duzina = rand.nextInt((24 - 12)-1) + 12;
        this.oznaka = rand.nextInt((999999 - 111111)) + 111111;
    }

    public void setOznaka(int oznaka) { this.oznaka = oznaka; }

    public void setDuzina(int duzina) { this.duzina = duzina; }

    public int getOznaka() { return oznaka; }

    public int getDuzina() { return duzina; }

}