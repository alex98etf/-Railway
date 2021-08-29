package model.vozila;

import main.Zeljeznica;
import model.kompozicija.Kompozicija;
import model.put.Putevi;

import java.util.logging.Logger;

/**
 * @author Alex
 */
public class Vozilo extends Thread{

    private static final Logger LOGGER = Logger.getLogger(Vozilo.class.getName());

    protected int ID;
    protected String model;
    protected String marka;
    protected int godiste;
    protected boolean pauza;

    public Vozilo() {
        this.ID = 111111;
        this.model = "TEST";
        this.marka = "MARKA ZVAKA";
        this.godiste = 2003;
        this.pauza = false;

    }

    public void setPauza(boolean pauza) {
        this.pauza = pauza;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public void setMarka(String marka) {
        this.marka = marka;
    }

    public void setGodiste(int godiste) {
        this.godiste = godiste;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public int getIdVozila() {
        return ID;
    }

    public String getModel() {
        return model;
    }

    public String getMarka() {
        return marka;
    }

    public int getGodiste() {
        return godiste;
    }

    public boolean getPauza() {
        return pauza;
    }

    @Override
    public String toString() {
        return "Vozilo{" + "ID=" + ID + ", marka=" + marka + ", model=" + model + '}';
    }

    public void run(){
        System.out.println("to");
        for (int i = 0; i < Zeljeznica.MAP_SIZE-1 && i!=1; i++) {
            for (int j = 0; j < Zeljeznica.MAP_SIZE-1; j++) {
                if(pauza){
                    synchronized (this) {
                        try {
                            wait();
                        } catch (Exception exception) {
                            LOGGER.warning(exception.fillInStackTrace().toString());
                        }
                    }
                }

                try{
                    if(Zeljeznica.map[i][j] == this){
                        // IDE DOLE
                        if(Zeljeznica.background[i][j+1] instanceof Putevi && Zeljeznica.background[i+1][j] instanceof Putevi){
                            String vrsta_R = ((Putevi) Zeljeznica.background[i][j+1]).getVrsta();
                            String vrsta_D = ((Putevi) Zeljeznica.background[i+1][j]).getVrsta();

                            if(Putevi.PRELAZ.equals(vrsta_D) && Zeljeznica.map[i+1][j-1] instanceof Kompozicija)
                                continue;
                            if(Putevi.KOLOVOZ.equals(vrsta_R) || Putevi.PRELAZ.equals(vrsta_R)){
                                Zeljeznica.map[i+1][j] = this;
                                Zeljeznica.map[i][j] = null;
                                System.out.println("DOLE"+"(" + i + ","+ j+ ")");
                                sleep(Zeljeznica.brzinePuteva[1]);
                                if(i+1 == Zeljeznica.MAP_SIZE-1) {
                                    Zeljeznica.map[i + 1][j] = null;
                                    this.join();
                                }
                            }
                        }
                        // IDE GORE
                        else if(Zeljeznica.background[i][j-1] instanceof Putevi && Zeljeznica.background[i-1][j] instanceof Putevi) {
                            String vrsta_L = ((Putevi) Zeljeznica.background[i][j - 1]).getVrsta();
                            String vrsta_U = ((Putevi) Zeljeznica.background[i - 1][j]).getVrsta();

                            if(Putevi.PRELAZ.equals(vrsta_U) && Zeljeznica.map[i-1][j+1] instanceof Kompozicija)
                                continue;
                            if (Putevi.KOLOVOZ.equals(vrsta_L) || Putevi.PRELAZ.equals(vrsta_L)) {
                                Zeljeznica.map[i - 1][j] = this;
                                Zeljeznica.map[i][j] = null;
                                System.out.println("GORE" + "(" + i + "," + j + ")");
                                sleep(Zeljeznica.brzinePuteva[1]);
                                i -= 2;
                                if (i == 0){
                                    Zeljeznica.map[i][j] = null;
                                    this.join();
                                }
                            }
                        }
                        // IDE DESNO
                        else if(Zeljeznica.background[i+1][j] == null || Zeljeznica.background[i+1][j] instanceof Putevi)
                            if(Zeljeznica.background[i][j+1] instanceof Putevi && Zeljeznica.map[i][j+1] != null){
                                String vrsta_L = ((Putevi) Zeljeznica.background[i][j-1]).getVrsta();
                                System.out.println("Usli");
                                if(Putevi.PRELAZ.equals(vrsta_L) && Zeljeznica.map[i-1][j+1] instanceof Kompozicija)
                                    continue;
                                Zeljeznica.map[i][j-1] = this;
                                Zeljeznica.map[i][j] = null;
                                System.out.println("LIJEVO (" +i + ";" + j+ ")");
                            }

                    }
                } catch (Exception exception){
                    LOGGER.warning(exception.fillInStackTrace().toString());
                }
            }
        }
    }
}