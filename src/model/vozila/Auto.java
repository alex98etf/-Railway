package model.vozila;

import java.util.logging.Logger;

/**
 * @author Alex
 */
public class Auto extends Vozilo {

    private static final Logger LOGGER = Logger.getLogger(Auto.class.getName());

    public Auto(int brzina) {
		super(brzina);
    }

    protected int brojVrata;

    public int getBrojVrata() {
        return brojVrata;
    }

}