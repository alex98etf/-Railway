package model.vagoni;

import java.util.logging.Logger;
/**
 * @author Alex
 */
public class Tertni extends Vagon {

    private static final Logger LOGGER = Logger.getLogger(Tertni.class.getName());

    public Tertni() {
        super();
    }

    protected int maxNosivost;


    public int getNosivost() { return maxNosivost;}

}