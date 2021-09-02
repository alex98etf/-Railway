package model.vozila;

import main.Zeljeznica;
import model.kompozicija.Kompozicija;
import model.lokomotive.Lokomotiva;
import model.put.Putevi;
import model.vagoni.Vagon;

import java.util.Random;
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
    protected boolean stigao = false;
    protected char smjer;
    protected int brzina;
    private static final Random rand = new Random();


    public Vozilo(int brzina) {
        this.ID = 111111 + rand.nextInt(50);
        this.model = "TEST";
        this.marka = "MARKA ZVAKA";
        this.godiste = 2003;
        this.brzina = brzina;

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





    @Override
    public String toString() {
        return "Vozilo{" + "ID=" + ID + ", marka=" + marka + ", model=" + model + '}';
    }

    public void run(){
        System.out.println("to " + ID);
        for (int i = 0; i < Zeljeznica.MAP_SIZE-1; i++) {
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
                try {
                    if (Zeljeznica.map[i][j] == this || Zeljeznica.map[29][j] == this) {
                        if (smjer == 'd') {
                            if (Zeljeznica.map[i + 1][j] == null && Zeljeznica.background[i + 1][j] instanceof Putevi) {
                                String vrsta = ((Putevi) Zeljeznica.background[i + 1][j]).getVrsta();

                                if(Putevi.PRELAZ.equals(vrsta)){
                                    if(Zeljeznica.map[i+1][j-1] instanceof Lokomotiva || Zeljeznica.map[i+1][j+1] instanceof Lokomotiva) {
                                        i--;
                                        continue;
                                    }
                                    if(Zeljeznica.map[i+1][j-1] instanceof Vagon || Zeljeznica.map[i+1][j+1] instanceof Vagon) {
                                        i--;
                                        continue;
                                    }
                                }

                                System.out.println("DOLE" + "(" + i + "," + j + ")");
                                Zeljeznica.map[i+1][j] = this;
                                Zeljeznica.map[i][j] = null;
                                sleep(brzina);
                            }
                            else if(Zeljeznica.map[i + 1][j] != null){
                                i--;
                                System.out.println(i +" zastoj " +j +" "+ ID);
                                sleep(brzina);
                                continue;
                            }
                            if (i+1 >= Zeljeznica.MAP_SIZE - 1) {
                                stigao = true;
                                Zeljeznica.map[i+1][j] = null;
                                System.out.println("stigao " + ID);
                            }
                        }
                        // IDE PREMA GORE
                        else if(smjer == 'u'){
                            if(Zeljeznica.map[i-1][j] == null && Zeljeznica.background[i-1][j] instanceof Putevi){
                                String vrsta = ((Putevi) Zeljeznica.background[i-1][j]).getVrsta();

                                if(Putevi.PRELAZ.equals(vrsta)){
                                    if(Zeljeznica.map[i-1][j-1] instanceof Lokomotiva || Zeljeznica.map[i+1][j+1] instanceof Lokomotiva) {
                                        i--;
                                        continue;
                                    }
                                    if(Zeljeznica.map[i-1][j-1] instanceof Vagon || Zeljeznica.map[i+1][j+1] instanceof Vagon) {
                                        i--;
                                        continue;
                                    }
                                }
                                System.out.println("GORE" + "(" + i + "," + j + ")");
                                Zeljeznica.map[i-1][j] = this;
                                Zeljeznica.map[i][j] = null;
                                sleep(brzina);
                                if(i-1 <= 0) {
                                    Zeljeznica.map[i - 1][j] = null;
                                    stigao = true;
                                }
                                i-=2;
                            }
                            else if(Zeljeznica.map[i-1][j] != null){
                                i--;
                                System.out.println("zastoj");
                                sleep(brzina);
                                continue;
                            }
                            // GORE pa Lijevo
                            if(i>0 && Zeljeznica.background[i][j] == null && Zeljeznica.background[i+1][j-1] instanceof Putevi){
                                String vrsta = ((Putevi) Zeljeznica.background[i+1][j-1]).getVrsta();
                                if(Putevi.KOLOVOZ.equals(vrsta))
                                smjer = 'l';
                            }
                            // GORE pa Desno
                            if(Zeljeznica.background[i+1][j+1] instanceof Putevi && Zeljeznica.background[i+1][j] instanceof Putevi){
                                String vrsta = ((Putevi) Zeljeznica.background[i][j+1]).getVrsta();
                                if(Putevi.KOLOVOZ.equals(vrsta))
                                    smjer = 'r';
                            }
                            /*if(i <= 0){
                                stigao = true;
                                Zeljeznica.map[i+2][j] = null;
                            }*/

                        }
                        else if(smjer == 'l'){
                            System.out.println("lijevo");
                            if(Zeljeznica.map[i][j-1] == null && Zeljeznica.background[i][j-1] instanceof Putevi){
                                String vrsta = ((Putevi) Zeljeznica.background[i][j-1]).getVrsta();
                                if(Putevi.PRELAZ.equals(vrsta)){
                                    if(Zeljeznica.map[i+1][j-1] instanceof Lokomotiva || Zeljeznica.map[i+1][j+1] instanceof Lokomotiva) {
                                        j--;
                                        continue;
                                    }
                                    if(Zeljeznica.map[i+1][j-1] instanceof Vagon || Zeljeznica.background[i][j-1] instanceof Vagon) {
                                        j--;
                                        continue;
                                    }
                                }

                                System.out.println("Lijevo (" + i + ";" + j + ")");
                                Zeljeznica.map[i][j-1] = this;
                                Zeljeznica.map[i][j] = null;
                                sleep(brzina);
                                if(j-1 == 0){
                                    Zeljeznica.map[i][j-1] = null;
                                    stigao = true;
                                }

                                j-=2;
                            }
                            else if(Zeljeznica.map[i][j-1] != null){
                                j--;
                                System.out.println(i+" zastoj " +j +" "+ID);
                                sleep(brzina);
                                continue;
                            }
                            if(j>0 && Zeljeznica.background[i][j] == null && Zeljeznica.background[i+1][j+1] instanceof Putevi){
                                String vrsta = ((Putevi) Zeljeznica.background[i+1][j+1]).getVrsta();
                                if(Putevi.KOLOVOZ.equals(vrsta))
                                    smjer = 'd';
                            }
                            /*if(j <= 0){
                                stigao = true;
                                Zeljeznica.map[i][j+2] = null;
                            }*/

                        }
                        else if(smjer == 'r'){
                            if(Zeljeznica.map[i][j+1] == null && Zeljeznica.background[i][j+1] instanceof Putevi){
                                String vrsta = ((Putevi) Zeljeznica.background[i][j+1]).getVrsta();

                                if(Putevi.PRELAZ.equals(vrsta)){
                                    if(Zeljeznica.map[i-1][j+1] instanceof Lokomotiva || Zeljeznica.map[i+1][j+1] instanceof Lokomotiva) {
                                        j--;
                                        continue;
                                    }
                                    if(Zeljeznica.map[i-1][j+1] instanceof Vagon || Zeljeznica.map[i+1][j+1] instanceof Vagon) {
                                        j--;
                                        continue;
                                    }
                                }

                                System.out.println("Desno (" + i + ";" + j + ")");
                                Zeljeznica.map[i][j+1] = this;
                                Zeljeznica.map[i][j] = null;
                                sleep(brzina);
                            }
                            else if(Zeljeznica.map[i][j+1] == null){
                                j--;
                                System.out.println("zasroj");
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