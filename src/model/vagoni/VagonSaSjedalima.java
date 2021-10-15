package model.vagoni;

import java.util.logging.Logger;
/**
 * @author Alex
 */
public class VagonSaSjedalima extends Vagon {

    private static final Logger LOGGER = Logger.getLogger(VagonSaSjedalima.class.getName());

    protected int kapacitet;

    public VagonSaSjedalima(int kapacitet) {
        super(18,26);
        this.kapacitet = kapacitet;
    }
    public String getOpis(){ return "Broj Sjedala : " + kapacitet; }

    public int getKapacitet() { return kapacitet;}

}
