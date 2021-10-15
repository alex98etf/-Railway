package model.vagoni;

import java.util.logging.Logger;
/**
 * @author Alex
 */
public class Tertni extends Vagon {

    private static final Logger LOGGER = Logger.getLogger(Tertni.class.getName());

    protected int maxNosivost; //25t-75t
    public Tertni(int kapacitet) {
        super(9,18);
        this.maxNosivost = kapacitet;
    }

    public String getOpis(){ return "Litijum ruda : " + maxNosivost + "t"; }

    public int getNosivost() { return maxNosivost;}

}