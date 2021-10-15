package model.vozila;

import main.Zeljeznica;
import model.lokomotive.Lokomotiva;
import model.put.Putevi;
import model.vagoni.Vagon;
import java.util.Random;
import java.util.logging.Logger;

/**
 * @author Alex
 */
public abstract class Vozilo extends Thread{

    private static final Logger LOGGER = Logger.getLogger(Vozilo.class.getName());

    protected int ID;
    protected String model;
    protected String marka;
    protected int godiste;
    protected boolean pauza;
    protected boolean stigao = false;
    protected char smjer;
    protected int brzina;
    protected static final Random rand = new Random();


    public Vozilo(int brzina) {
        this.ID = 111111 + rand.nextInt(888888);
        this.model = "JDK-13.0.2";
        this.marka = "JAVA";
        this.godiste = 2020;
        this.brzina = brzina;
        setDaemon(true);
    }

    public void setPauza(boolean pauza) {
        this.pauza = pauza;
        if(!pauza)
            try {
                synchronized (this){
                    notify();
                }
            } catch (Exception exception){
                LOGGER.warning(exception.fillInStackTrace().toString());
            }
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

    public void setSmjer(char smjer){this.smjer = smjer;}

    public void setBrzina(int brzina) { this.brzina = brzina; }

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

    public boolean isPauza() {
        return pauza;
    }

    public boolean isStigao() { return stigao; }

    public void kill(){
        try {
            this.join();
        } catch (Exception exception){
            LOGGER.warning(exception.fillInStackTrace().toString());
        }
    }

    @Override
    public String toString() {
        return "Vozilo{" + "ID=" + ID + ", marka=" + marka + ", model=" + model + '}';
    }

    public void run(){
        for (int i = 0; i < Zeljeznica.MAP_SIZE; i++) {
            for (int j = 0; j < Zeljeznica.MAP_SIZE; j++) {
                if(pauza){
                    synchronized (this) {
                        try {
                            wait();
                        } catch (Exception exception) {
                            LOGGER.warning(exception.fillInStackTrace().toString());
                        }
                    }
                }
                try {
                    if (Zeljeznica.map[i][j] == this) {

                        // IDe prema dole
                        if (smjer == 'd') {
                            if (Zeljeznica.map[i + 1][j] == null && Zeljeznica.background[i + 1][j] instanceof Putevi) {
                                String vrsta = ((Putevi) Zeljeznica.background[i + 1][j]).getVrsta();

                                if(j>0 && Putevi.PRELAZ.equals(vrsta)){
                                    if(Zeljeznica.map[i+1][j-1] instanceof Lokomotiva || Zeljeznica.map[i+1][j+1] instanceof Lokomotiva) {
                                        i--;
                                        sleep(brzina);
                                        continue;
                                    }
                                    if(Zeljeznica.map[i+1][j-1] instanceof Vagon || Zeljeznica.map[i+1][j+1] instanceof Vagon) {
                                        i--;
                                        sleep(brzina);
                                        continue;
                                    }
                                }

//                                System.out.println("DOLE" + "(" + i + "," + j + ")");
                                Zeljeznica.map[i+1][j] = this;
                                Zeljeznica.map[i][j] = null;
                                sleep(brzina);
                            }
                            // Zastoj
                            else if(Zeljeznica.map[i + 1][j] != null){
                                i--;
                                sleep(brzina);
                                continue;
                            }
                            // Uklanjanje sa mape
                            if (i+1 >= Zeljeznica.MAP_SIZE - 1) {
                                stigao = true;
                                Zeljeznica.map[i+1][j] = null;
                            }
                        }
                        // IDE PREMA GORE
                        else if(smjer == 'u'){
                            if(i>0 && j>0 && Zeljeznica.map[i-1][j] == null && Zeljeznica.background[i-1][j] instanceof Putevi){
                                String vrsta = ((Putevi) Zeljeznica.background[i-1][j]).getVrsta();

                                if(Putevi.PRELAZ.equals(vrsta)){
                                    if(Zeljeznica.map[i-1][j-1] instanceof Lokomotiva || Zeljeznica.map[i+1][j+1] instanceof Lokomotiva) {
                                        i--;
                                        sleep(brzina);
                                        continue;
                                    }
                                    if(Zeljeznica.map[i-1][j-1] instanceof Vagon || Zeljeznica.map[i+1][j+1] instanceof Vagon) {
                                        i--;
                                        sleep(brzina);
                                        continue;
                                    }
                                }
//                                System.out.println("GORE" + "(" + i + "," + j + ")");
                                Zeljeznica.map[i-1][j] = this;
                                Zeljeznica.map[i][j] = null;
                                sleep(brzina);
                                // Ukalanjanje sa mape
                                if(i-1 <= 0) {
                                    Zeljeznica.map[i - 1][j] = null;
                                    stigao = true;
                                }
                                else
                                    i-=2; // ponistavanje ++ i vracanje u prethodni red
                            }
                            // Zastoj
                            else if(i>0 && Zeljeznica.map[i-1][j] != null){
                                i--;
                                sleep(brzina);
                                continue;
                            }
                            // GORE pa Lijevo
                            if(j>0 && Zeljeznica.background[i][j] == null && Zeljeznica.background[i+1][j-1] instanceof Putevi){
                                String vrsta = ((Putevi) Zeljeznica.background[i+1][j-1]).getVrsta();
                                if(Putevi.KOLOVOZ.equals(vrsta))
                                smjer = 'l';
                            }
                            // GORE pa Desno
                            else if(i+1 <=Zeljeznica.MAP_SIZE && Zeljeznica.background[i+1][j+1] instanceof Putevi && Zeljeznica.background[i+1][j] instanceof Putevi){
                               if(Zeljeznica.background[i][j + 1] != null) {
                                   String vrsta = ((Putevi)Zeljeznica.background[i][j + 1]).getVrsta();
                                   if (Putevi.KOLOVOZ.equals(vrsta))
                                       smjer = 'r';
                               }
                            }
                        }
                        // Ide lijevo
                        else if(smjer == 'l'){
                            if(j>0 && Zeljeznica.map[i][j-1] == null && Zeljeznica.background[i][j-1] instanceof Putevi){
                                String vrsta = ((Putevi) Zeljeznica.background[i][j-1]).getVrsta();
                                if(Putevi.PRELAZ.equals(vrsta)){
                                    if(Zeljeznica.map[i+1][j-1] instanceof Lokomotiva || Zeljeznica.map[i+1][j+1] instanceof Lokomotiva) {
                                        j--;
                                        sleep(brzina);
                                        continue;
                                    }
                                    if(Zeljeznica.map[i+1][j-1] instanceof Vagon || Zeljeznica.background[i][j-1] instanceof Vagon) {
                                        j--;
                                        sleep(brzina);
                                        continue;
                                    }
                                }

//                                System.out.println("Lijevo (" + i + ";" + j + ")");
                                Zeljeznica.map[i][j-1] = this;
                                Zeljeznica.map[i][j] = null;
                                sleep(brzina);
                                if(j-1 == 0){
                                    Zeljeznica.map[i][j-1] = null;
                                    stigao = true;
                                }
                                else
                                    j-=2;// Ponistavanje ++ i vracanje u prethodnu kolonu
                            }
                            // Zastoj
                            else if(j>0 && Zeljeznica.map[i][j-1] != null){
                                j--;
                                sleep(brzina);
                                continue;
                            }
                            // Lijevo pa dole
                            if(j>0&&Zeljeznica.background[i][j] == null && Zeljeznica.background[i+1][j+1] instanceof Putevi){
                                String vrsta = ((Putevi) Zeljeznica.background[i+1][j+1]).getVrsta();
                                if(Putevi.KOLOVOZ.equals(vrsta))
                                    smjer = 'd';
                            }
                        }

                        // IDe Desno
                        else if(smjer == 'r'){
                            if(Zeljeznica.map[i][j+1] == null && Zeljeznica.background[i][j+1] instanceof Putevi){

                                String vrsta = ((Putevi) Zeljeznica.background[i][j+1]).getVrsta();
                                if(i>0 && Putevi.PRELAZ.equals(vrsta)){
                                    if(Zeljeznica.map[i-1][j+1] instanceof Lokomotiva || Zeljeznica.map[i+1][j+1] instanceof Lokomotiva) {
                                        if(j>0)
                                            j--;
                                        sleep(brzina);
                                        continue;

                                    }
                                    else if(Zeljeznica.map[i-1][j+1] instanceof Vagon || Zeljeznica.map[i+1][j+1] instanceof Vagon) {
                                        if(j>0)
                                            j--;
                                        sleep(brzina);
                                        continue;
                                    }
                                }
                                Zeljeznica.map[i][j+1] = this;
                                Zeljeznica.map[i][j] = null;
                                sleep(brzina);
                            }
                            else if(Zeljeznica.map[i][j+1] != null){
                                j--;
                                sleep(brzina);
                                continue;
                            }
                            if(Zeljeznica.background[i+1][j+1] instanceof Putevi && Zeljeznica.background[i+1][j] == null){
                                String vrsta = ((Putevi) Zeljeznica.background[i+1][j+1]).getVrsta();
                                if(Putevi.KOLOVOZ.equals(vrsta))
                                    smjer = 'd';
                            }
                            if(j+1 >= Zeljeznica.MAP_SIZE-1){
                                stigao = true;
                                Zeljeznica.map[i][j+1] = null;
                            }
                        }
                    }
                } catch (Exception exception){
                    LOGGER.warning(exception.fillInStackTrace().toString());
                }
            }
        }
    }
}