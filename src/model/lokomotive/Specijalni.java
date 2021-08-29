package model.lokomotive;

import java.util.logging.Logger;

/**
 * @author Alex
 */
public class Specijalni extends Lokomotiva {

    private static final Logger LOGGER = Logger.getLogger(Specijalni.class.getName());
    public Specijalni(int snaga, int vrstaPogona) {

        super(snaga, vrstaPogona);
    }


}