package model.vozila;

import java.util.logging.Logger;

/**
 * @author Alex
 */
public class Kamion extends Vozilo {

    private static final Logger LOGGER = Logger.getLogger(Kamion.class.getName());
    public Kamion(int brzina) {
        super(brzina);
    }

    protected int maxNosivost;

    public int getNosivost() { return maxNosivost; }

}