package model.lokomotive;

import java.util.logging.Logger;

/**
 * @author Alex
 */
public class Putnička extends Lokomotiva {

    private static final Logger LOGGER = Logger.getLogger(Putnička.class.getName());

    public Putnička(int snaga, int vrstaPogona) {
        super(snaga, vrstaPogona);
    }


}