package model.kompozicija;

import main.Zeljeznica;
import model.lokomotive.*;
import model.put.Putevi;
import model.vagoni.*;

import java.util.ArrayList;
import java.util.logging.Logger;

public class Kompozicija extends Thread{
    private static final Logger LOGGER = Logger.getLogger(Kompozicija.class.getName());
    private ArrayList<Lokomotiva> lokomotive;
    private ArrayList<Vagon> vagoni;
    private String obrediste;
    private int brzina;
    private boolean cekaj;

    public Kompozicija(int numLokomotiva, int numVagona, String odrediste, int brzina){
        lokomotive = new ArrayList<>(numLokomotiva);
        vagoni = new ArrayList<>(numVagona);

        this.obrediste = odrediste;
        this.brzina = brzina;

        lokomotive.add(new Univerzalna(5,6));
        lokomotive.add(new Putnička(4,8));

        vagoni.add(new Restoran("Pavlaka"));

    }


    public String getObrediste() {
        return obrediste;
    }

    public void setObrediste(String obrediste) {
        this.obrediste = obrediste;
    }

    public int getBrzina() {
        return brzina;
    }

    public void setBrzina(int brzina) {
        this.brzina = brzina;
    }

    public boolean isCekaj() {
        return cekaj;
    }

    public void setCekaj(boolean cekaj) {
        this.cekaj = cekaj;
    }

    public void run() {
        for(int i = 0; i < Zeljeznica.MAP_SIZE; ++i)
            for (int j = 0; j < Zeljeznica.MAP_SIZE; ++j){
                if(Zeljeznica.background[i][j] instanceof Putevi){
                    String vrsta = ((Putevi) Zeljeznica.background[i][j]).getVrsta();
                    if(Putevi.PRUGA.equals(vrsta)) {
                        if(Zeljeznica.background[i][j+1] == null ||  Zeljeznica.background[i][j-1] == null){
                            // TODO: PRVAO
                        }
                        if(Zeljeznica.background[i][j-1] == null &&  Zeljeznica.background[i+1][j] == null){
//                            TODO: DESNO
                        }
                        if(Zeljeznica.background[i][j+1] == null &&  Zeljeznica.background[i+1][j] == null){
//                            TODO: LIJEVO
                        }

                    }
                }
            }

    }
}
