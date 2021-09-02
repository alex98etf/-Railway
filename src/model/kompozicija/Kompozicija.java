package model.kompozicija;

import main.Zeljeznica;
import model.lokomotive.*;
import model.put.Putevi;
import model.stanica.Stanica;
import model.vagoni.*;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.logging.Logger;

public class Kompozicija extends Thread{
    private static final Logger LOGGER = Logger.getLogger(Kompozicija.class.getName());
    private ArrayList<Stanica> stanice;
    private ArrayList<Lokomotiva> lokomotive;
    private ArrayList<Vagon> vagoni;
    private ArrayList<Float> kordinate;
    private final long startTime;
    private long stopTime;
    private String obrediste;
    private int brzina;
    private boolean cekaj;
    private String name;

    public Kompozicija(int numLokomotiva, int numVagona, String odrediste, int brzina){
        lokomotive = new ArrayList<>(numLokomotiva);
        vagoni = new ArrayList<>(numVagona);
        this.obrediste = odrediste;
        this.brzina = brzina;

        lokomotive.add(new Univerzalna(5,6));
        lokomotive.add(new Putnička(4,8));

        vagoni.add(new Restoran("Pavlaka"));

        startTime = System.currentTimeMillis();
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

    public static void getKomdozicija(){

    }
    private void serijalizuj(){
        Date startDate = new Date(startTime);
        Date stopDate = new Date(stopTime);

        try {
            FileWriter fileWriter = new FileWriter(Zeljeznica.dirKretanja + File.separator + this.name);
            BufferedWriter writer = new BufferedWriter(fileWriter);
            StringBuilder istorija = new StringBuilder();

            istorija.append(name).append(":\n");
            istorija.append("Linija:\n");
            for(Stanica stanica : stanice)
                istorija.append(stanica.getName()).append(" - ");
            istorija.delete(istorija.length()-1,istorija.length()); //"- "
            istorija.append("\n");

            istorija.append("Vrijeme polaska: ").append(startDate).append("\n");
            istorija.append("Vrijeme dolaska: ").append(startDate).append("\n");

            istorija.append("Lokomotive :\n");
            for(Lokomotiva lokomotiva : lokomotive) {
                istorija.append("\t").append(lokomotiva.getClass().getName());
                istorija.append(" - ").append(lokomotiva.getOznaka()).append("\n");
//                istorija.append(" - ").append(lokomotiva.getVrstaPogona());
            }
            istorija.append("Vagoni: \n");
            for(Vagon vagon : vagoni){
                istorija.append("\t").append(vagon.getClass().getName());
                istorija.append(" - ").append(vagon.getOznaka()).append("\n");
            }
            istorija.append("Kretanje: ");
            for (float kordinata: kordinate)
                istorija.append("(").append(kordinata).append(")\n");

            writer.write(istorija.toString());
            writer.flush();
            writer.close();

            fileWriter.close();

        } catch (Exception exception) {
            LOGGER.warning(exception.fillInStackTrace().toString());
        }

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
