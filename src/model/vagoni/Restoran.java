package model.vagoni;

import java.util.logging.Logger;
/**
 * @author Alex
 */
public class Restoran extends Vagon {

    private static final Logger LOGGER = Logger.getLogger(Restoran.class.getName());

    protected String opis;

    public Restoran(String opis) {
        super(18,26);
        this.opis = opis;
    }


    public String getOpis() { return "Služeno : "+opis;}

}