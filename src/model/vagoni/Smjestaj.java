package model.vagoni;

import java.util.logging.Logger;
/**
 * @author Alex
 */
public class Smjestaj extends Vagon {

    private static final Logger LOGGER = Logger.getLogger(Smjestaj.class.getName());

    protected int brojMjesta;
    protected String vrsta;

    public Smjestaj() {
        super();
    }


    public int getBrojMjesta() { return brojMjesta;}

}