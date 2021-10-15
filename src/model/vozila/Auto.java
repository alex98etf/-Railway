package model.vozila;

import java.util.logging.Logger;

/**
 * @author Alex
 */
public class Auto extends Vozilo {

    private static final Logger LOGGER = Logger.getLogger(Auto.class.getName());
    protected int brojVrata;


    public Auto(int brzina) {
		super(brzina);
		brojVrata = rand.nextInt((5 - 3)-1) + 3;
    }

    public int getBrojVrata() {
        return brojVrata;
    }

}