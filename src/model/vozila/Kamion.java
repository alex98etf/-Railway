package model.vozila;

import java.util.logging.Logger;

/**
 * @author Alex
 */
public class Kamion extends Vozilo {

    private static final Logger LOGGER = Logger.getLogger(Kamion.class.getName());
    protected int maxNosivost; // 3t-27t

    public Kamion(int brzina) {
        super(brzina);
        maxNosivost = rand.nextInt((27 - 3)-1) + 3;
    }


    public int getNosivost() { return maxNosivost; }

}