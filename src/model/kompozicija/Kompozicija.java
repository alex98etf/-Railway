package model.kompozicija;

import main.Zeljeznica;
import model.lokomotive.*;
import model.put.Putevi;
import model.stanica.Kondukter;
import model.vagoni.*;

import java.awt.geom.Point2D;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.*;
import java.util.logging.Logger;
import java.util.stream.Collectors;

/**
 * @author Alex
 */

public class Kompozicija extends Thread{
    private static final Logger LOGGER = Logger.getLogger(Kompozicija.class.getName());

    private final ArrayList<String> stanice = new ArrayList<>();
    private final ArrayList<Lokomotiva> lokomotive = new ArrayList<>();
    private final ArrayList<Vagon> vagoni = new ArrayList<>();
    private final ArrayList<Point2D> kordinate = new ArrayList<>();
    private final long startTime;
    private long stopTime;
    private int nextStanica;
    private int brzina;
    private final int startBrzina;
    private boolean uStanici;
    private boolean cekaj;
    private final String name;
    private int headX;
    private int headY;
    private boolean stigao;
    private boolean pauza;
    private boolean speedLimited;
    private boolean izlazimo;

    public Kompozicija(String name,String[] stanice, int brzina){
        this.name = name;
        this.brzina = brzina;
        this.startBrzina = brzina;

        setDaemon(true);

        Collections.addAll(this.stanice, stanice);

        stigao = false;
        cekaj = true;
        speedLimited = false;
        uStanici = true;

        startTime = System.currentTimeMillis();
    }


    public int getBrzina() {
        return brzina;
    }

    public void setBrzina(int brzina) {
        this.brzina = brzina;
    }

    public boolean isuStanici() {
        return uStanici;
    }

    public void setuStanici(boolean cekaj) {
        this.uStanici = cekaj;
    }

    public void setPocetneKordinate(String[] kordinate){
        this.headX = Integer.parseInt(kordinate[0]);
        this.headY = Integer.parseInt(kordinate[1]);

        lokomotive.forEach(l -> l.setPosition(headX,headY));
        vagoni.forEach(v -> v.setPosition(headX,headY));

        vagoni.forEach(v -> v.setSmijer(kordinate[2].charAt(0)));
        lokomotive.forEach(l -> l.setSmijer(kordinate[2].charAt(0)));
    }

    public boolean isPauza() { return pauza; }

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

    public void kill(){
        try {
            synchronized (this){
               join();
            }
        } catch (Exception exception){
            LOGGER.warning(exception.fillInStackTrace().toString());
        }
    }

    public boolean isSpeedLimited() { return speedLimited;}

    public boolean izlazim() { return izlazimo; }

    /**
     *
     * @return Vraca ime sljedecu stanicu u liniji
     */
    public String getNextStanica() {
        if (nextStanica+1 < stanice.size())
            return stanice.get(nextStanica+1);
        return null;
    }
    public void addLokomotiva(String vrsta, String vrstaPogona){
        switch (vrsta) {
            case "P":
                lokomotive.add(new Putnicka(vrstaPogona));
                break;
            case "T":
                lokomotive.add(new Teretna(vrstaPogona));
                break;
            case "U":
                lokomotive.add(new Univerzalna(vrstaPogona));
                break;
            case "M":
                lokomotive.add(new Manevarska(vrstaPogona));
                break;
        }
    }

    public void addVagon(String vrsta, String opis){
        switch (vrsta) {
            case "s":
                vagoni.add(new VagonSaSjedalima(Integer.parseInt(opis)));
                break;
            case "l":
                vagoni.add(new VagonSaLezajima(Integer.parseInt(opis)));
                break;
            case "r":
                vagoni.add(new Restoran(opis));
                break;
            case "t":
                vagoni.add(new Tertni(Integer.parseInt(opis)));
                break;
            case "n":
                vagoni.add(new Namjenski(opis));
                break;
        }
    }

    public void serijalizuj(){
        Date startDate = new Date(startTime);
        Date stopDate = new Date(stopTime);

        try {
            FileWriter fileWriter = new FileWriter(Zeljeznica.dirKretanja + File.separator + this.name);
            BufferedWriter writer = new BufferedWriter(fileWriter);
            StringBuilder istorija = new StringBuilder();

            istorija.append(name).append(":\n");
            istorija.append("Linija: ");
            for(String stanica : stanice)
                istorija.append(stanica).append(" - ");
            istorija.delete(istorija.length()-2,istorija.length()); //"- "
            istorija.append("\n\n");

            istorija.append("Vrijeme polaska: ").append(startDate).append("\n");
            istorija.append("Vrijeme dolaska: ").append(stopDate).append("\n");

            istorija.append("\nLokomotive :\n");
            for(Lokomotiva lokomotiva : lokomotive) {
                istorija.append("\t").append(lokomotiva.getOznaka());
                istorija.append(" - ").append(lokomotiva.getClass().getSimpleName()).append("\n");
            }
            if(!vagoni.isEmpty()) {
                istorija.append("\nVagoni: \n");
                for (Vagon vagon : vagoni) {
                    istorija.append("\t").append(vagon.getOznaka());
                    istorija.append(" - ").append(vagon.getClass().getSimpleName());
                    istorija.append(" - ").append(vagon.getOpis()).append("\n");
                }
            }
            // Uklanjanje duplih kordinata (losa implementaciaj kretanja)
            List<Point2D> discreatKor =  kordinate.stream().distinct().collect(Collectors.toList());

            istorija.append("\nKretanje: \n");
            for (Point2D kordinata: discreatKor)
                istorija.append("\t(").append((int) kordinata.getX()).append(",").append((int) kordinata.getY()).append(")\n");

            writer.write(istorija.toString());
            writer.flush();
            writer.close();

            fileWriter.close();

        } catch (Exception exception) {
            LOGGER.warning(exception.fillInStackTrace().toString());
        }

    }

    private void move(Lokomotiva lokomotiva){
        char smijer = lokomotiva.getSmijer();
        int x = lokomotiva.getPositionX();
        int y = lokomotiva.getPositionY();
        try {
            if(lokomotive.indexOf(lokomotiva) == 0) {
                sleep(brzina);
                kordinate.add(new Point2D.Float(x,y));
            }
            // ide desno
            if(smijer == 'r' && y<Zeljeznica.MAP_SIZE-1) {
                if (Zeljeznica.background[x][y + 1] instanceof Putevi) {
                    String vrsta = ((Putevi) Zeljeznica.background[x][y+1]).getVrsta();
                    if(!Putevi.STANICA.equals(vrsta)) {
                        Zeljeznica.map[x][y + 1] = lokomotiva;
                        Zeljeznica.map[x][y] = null;
                        lokomotiva.setPositionY(++y);
                        // Kraj mape
                        if(y == Zeljeznica.MAP_SIZE-1){
                            if(vagoni.size() == 0 && lokomotive.indexOf(lokomotiva)+1 == lokomotive.size()){
                                Zeljeznica.map[x][y] = null;
                                pauza = true;
                                nextStanica++;
                                uStanici = true;
                            }
                            return;
                        }
                    }
                    else{
                        Zeljeznica.map[x][y] = null;
                        if(vagoni.size() == 0 && lokomotive.indexOf(lokomotiva)+1 == lokomotive.size()){
                            pauza = true;
                            nextStanica++;
                            uStanici = true;
                        }
                    }
                }
                // Promjena smijera ka dole
                if(Zeljeznica.background[x][y + 1] == null && Zeljeznica.background[x-1][y] == null && Zeljeznica.background[x+1][y] instanceof Putevi) {
                    lokomotiva.setSmijer('d');
                    smijer = 'd';
                }
                // Promjena smijera ka gore
                if(Zeljeznica.background[x][y + 1] == null && Zeljeznica.background[x+1][y] == null && Zeljeznica.background[x-1][y] instanceof Putevi) {
                    lokomotiva.setSmijer('u');
                    smijer = 'u';
                }
            }

            // Ide dole
            else if(smijer == 'd' && x < Zeljeznica.MAP_SIZE-1){
                if(Zeljeznica.background[x+1][y] instanceof Putevi) {
                    String vrsta = ((Putevi) Zeljeznica.background[x+1][y]).getVrsta();
                    if(!Putevi.STANICA.equals(vrsta)) {
                        Zeljeznica.map[x + 1][y] = lokomotiva;
                        Zeljeznica.map[x][y] = null;
                        lokomotiva.setPositionX(++x);
                        // Kraj mape
                        if(x == Zeljeznica.MAP_SIZE-1){
                            if(vagoni.size() == 0 && lokomotive.indexOf(lokomotiva)+1 == lokomotive.size()){
                                Zeljeznica.map[x][y] = null;
                                pauza = true;
                                nextStanica++;
                                uStanici = true;
                            }
                            return;
                        }

                    }
                    else{
                        Zeljeznica.map[x][y] = null;
                        if(vagoni.size() == 0 && lokomotive.indexOf(lokomotiva)+1 == lokomotive.size()){
                            pauza = true;
                            nextStanica++;
                            uStanici = true;
                        }
                    }
                }
                // Promjena smijera ka lijevo
                if(Zeljeznica.background[x+1][y] == null && Zeljeznica.background[x][y-1] == null && Zeljeznica.background[x][y+1] instanceof Putevi) {
                    lokomotiva.setSmijer('r');
                    smijer = 'r';
                }
                if(Zeljeznica.background[x+1][y] == null && Zeljeznica.background[x][y+1] == null && Zeljeznica.background[x][y-1] instanceof Putevi) {
                    lokomotiva.setSmijer('l');
                    smijer = 'l';
                }
            }

            // IDe lijevo
            else if(smijer == 'l'){
                if(Zeljeznica.background[x][y-1] instanceof Putevi){
                    String vrsta = ((Putevi) Zeljeznica.background[x][y-1]).getVrsta();
                    if(!Putevi.STANICA.equals(vrsta)) {
                        Zeljeznica.map[x][y - 1] = lokomotiva;
                        Zeljeznica.map[x][y] = null;
                        lokomotiva.setPositionY(--y);
                    }
                    else {
                        Zeljeznica.map[x][y] = null;
                        if(vagoni.size() == 0 && lokomotive.indexOf(lokomotiva)+1 == lokomotive.size()){
                            pauza = true;
                            nextStanica++;
                            uStanici = true;
                        }
                    }
                }
                // Promjena smijera ka dole
                if(Zeljeznica.background[x][y-1] == null && Zeljeznica.background[x-1][y] == null && Zeljeznica.background[x+1][y] instanceof Putevi) {
                    lokomotiva.setSmijer('d');
                    smijer = 'd';
                }
                // Promjena smijera ka gore
                if(Zeljeznica.background[x][y-1] == null && Zeljeznica.background[x+1][y] == null && Zeljeznica.background[x-1][y] instanceof Putevi) {
                    lokomotiva.setSmijer('u');
                    smijer = 'u';
                }
            }

            //Ide gore
            else if(smijer == 'u'){
                if(Zeljeznica.background[x-1][y] instanceof Putevi){
                    String vrsta = ((Putevi) Zeljeznica.background[x-1][y]).getVrsta();
                    if(!Putevi.STANICA.equals(vrsta)) {
                        Zeljeznica.map[x - 1][y] = lokomotiva;
                        Zeljeznica.map[x][y] = null;
                        lokomotiva.setPositionX(--x);
                    }
                    else{
                        Zeljeznica.map[x][y] = null;
                        if(vagoni.size() == 0 && lokomotive.indexOf(lokomotiva)+1 == lokomotive.size()){
                            pauza = true;
                            nextStanica++;
                            uStanici = true;
                        }
                    }
                }
                // Promjena smijera ka desno
                if(Zeljeznica.background[x-1][y] == null && Zeljeznica.background[x][y-1] == null && Zeljeznica.background[x][y+1] instanceof Putevi) {
                    lokomotiva.setSmijer('r');
                    smijer = 'r';
                }
                // Promjena smijera ka lijevo
                if(Zeljeznica.background[x-1][y] == null && Zeljeznica.background[x][y+1] == null && Zeljeznica.background[x][y-1] instanceof Putevi) {
                    lokomotiva.setSmijer('l');
                    smijer = 'l';
                }
            }

        } catch(Exception exception){
            exception.printStackTrace();
        }

    }
    private void move(Vagon vagon){
        char smijer = vagon.getSmijer();
        int x = vagon.getPositionX();
        int y = vagon.getPositionY();
        try {
            // ide desno
            if(smijer == 'r' && y<Zeljeznica.MAP_SIZE-1) {
                if (Zeljeznica.background[x][y + 1] instanceof Putevi) {
                    String vrsta = ((Putevi) Zeljeznica.background[x][y+1]).getVrsta();
                    if(!Putevi.STANICA.equals(vrsta)) {
                        Zeljeznica.map[x][y + 1] = vagon;
                        Zeljeznica.map[x][y] = null;
                        vagon.setPositionY(++y);
                        // Kraj mape
                        if(y == Zeljeznica.MAP_SIZE-1){
                            // Kada posljednji vagon uđe u stanicu kompozicija ide u pauzu
                            if(vagoni.indexOf(vagon)+1 == vagoni.size()){
                                Zeljeznica.map[x][y] = null;
                                pauza = true;
                                uStanici = true;
                                nextStanica++;
                            }
                            return;
                        }
                    }
                    else{
                        Zeljeznica.map[x][y] = null;
                        // Kada posljednji vagon uđe u stanicu kompozicija ide u pauzu
                        if(vagoni.indexOf(vagon)+1 == vagoni.size()){
                            pauza = true;
                            uStanici = true;
                            nextStanica++;
                        }
                    }
                }
                // Promjena smijera ka dole
                if(Zeljeznica.background[x][y + 1] == null && Zeljeznica.background[x-1][y] == null && Zeljeznica.background[x+1][y] instanceof Putevi) {
                    vagon.setSmijer('d');
                    smijer = 'd';
                }
                // Promjena smijera ka gore
                if(Zeljeznica.background[x][y + 1] == null && Zeljeznica.background[x+1][y] == null && Zeljeznica.background[x-1][y] instanceof Putevi) {
                    vagon.setSmijer('u');
                    smijer = 'u';
                }
            }

            // Ide dole
            else if(smijer == 'd' && x < Zeljeznica.MAP_SIZE-1){
                if(Zeljeznica.background[x+1][y] instanceof Putevi) {
                    String vrsta = ((Putevi) Zeljeznica.background[x+1][y]).getVrsta();
                    if(!Putevi.STANICA.equals(vrsta)) {
                        Zeljeznica.map[x + 1][y] = vagon;
                        Zeljeznica.map[x][y] = null;
                        vagon.setPositionX(++x);
                        // Kraj mape
                        if(x == Zeljeznica.MAP_SIZE-1){
                            // Kada posljednji vagon uđe u stanicu kompozicija ide u pauzu
                            if(vagoni.indexOf(vagon)+1 == vagoni.size()){
                                Zeljeznica.map[x][y] = null;
                                pauza = true;
                                uStanici = true;
                                nextStanica++;
                            }
                            return;
                        }

                    }
                    else{
                        Zeljeznica.map[x][y] = null;
                        // Kada posljednji vagon uđe u stanicu kompozicija ide u pauzu
                        if(vagoni.indexOf(vagon)+1 == vagoni.size()){
                            pauza = true;
                            uStanici = true;
                            nextStanica++;
                        }
                    }
                }
                // Promjena smijera ka lijevo
                if(Zeljeznica.background[x+1][y] == null && Zeljeznica.background[x][y-1] == null && Zeljeznica.background[x][y+1] instanceof Putevi) {
                    vagon.setSmijer('r');
                    smijer = 'r';
                }
                if(Zeljeznica.background[x+1][y] == null && Zeljeznica.background[x][y+1] == null && Zeljeznica.background[x][y-1] instanceof Putevi) {
                    vagon.setSmijer('l');
                    smijer = 'l';
                }
            }

            // IDe lijevo
            else if(smijer == 'l'){
                if(Zeljeznica.background[x][y-1] instanceof Putevi){
                    String vrsta = ((Putevi) Zeljeznica.background[x][y-1]).getVrsta();
                    if(!Putevi.STANICA.equals(vrsta)) {
                        Zeljeznica.map[x][y - 1] = vagon;
                        Zeljeznica.map[x][y] = null;
                        vagon.setPositionY(--y);
                    }
                    else {
                        Zeljeznica.map[x][y] = null;
                        // Kada posljednji vagon uđe u stanicu kompozicija ide u pauzu
                        if(vagoni.indexOf(vagon)+1 == vagoni.size()){
                            pauza = true;
                            uStanici = true;
                            nextStanica++;
                        }
                    }
                }
                // Promjena smijera ka dole
                if(Zeljeznica.background[x][y-1] == null && Zeljeznica.background[x-1][y] == null && Zeljeznica.background[x+1][y] instanceof Putevi) {
                    vagon.setSmijer('d');
                    smijer = 'd';
                }
                // Promjena smijera ka gore
                if(Zeljeznica.background[x][y-1] == null && Zeljeznica.background[x+1][y] == null && Zeljeznica.background[x-1][y] instanceof Putevi) {
                    vagon.setSmijer('u');
                    smijer = 'u';
                }
            }

            //Ide gore
            else if(smijer == 'u'){
                if(Zeljeznica.background[x-1][y] instanceof Putevi){
                    String vrsta = ((Putevi) Zeljeznica.background[x-1][y]).getVrsta();
                    if(!Putevi.STANICA.equals(vrsta)) {
                        Zeljeznica.map[x - 1][y] = vagon;
                        Zeljeznica.map[x][y] = null;
                        vagon.setPositionX(--x);
                    }
                    else{
                        Zeljeznica.map[x][y] = null;
                        // Kada posljednji vagon uđe u stanicu kompozicija ide u pauzu
                        if(vagoni.indexOf(vagon)+1 == vagoni.size()){
                            pauza = true;
                            uStanici = true;
                            nextStanica++;
                        }
                    }
                }
                // Promjena smijera ka desno
                if(Zeljeznica.background[x-1][y] == null && Zeljeznica.background[x][y-1] == null && Zeljeznica.background[x][y+1] instanceof Putevi) {
                    vagon.setSmijer('r');
                    smijer = 'r';
                }
                // Promjena smijera ka lijevo
                if(Zeljeznica.background[x-1][y] == null && Zeljeznica.background[x][y+1] == null && Zeljeznica.background[x][y-1] instanceof Putevi) {
                    vagon.setSmijer('l');
                    smijer = 'l';
                }
            }

        } catch(Exception exception){
            exception.printStackTrace();
        }

    }

    public void run(){
        int duzina = vagoni.size() + lokomotive.size();
        int control = 0;
        try {
            while (!stigao) {

                if(pauza){
                    if(uStanici){
                        if(nextStanica+1 == stanice.size()){
                            stopTime = System.currentTimeMillis();
                            stigao = true;
                            cekaj = true;
                            Kondukter.ukloniLiniju(this,stanice.get(nextStanica-1),stanice.get(nextStanica));
                            serijalizuj();
                            continue;
                        }
                        control = 0;
                        pauza = false;
                        cekaj = true;
                        String[] kor = Kondukter.prebaciLiniju(this,stanice.get(nextStanica-1),stanice.get(nextStanica),stanice.get(nextStanica+1));
                        setPocetneKordinate(kor);
                        continue;
                    }
                    synchronized (this) {
                        wait();
                    }
                }
                if(cekaj){
                    int stanje;
                    // Ako tek krecemo
                    stanje = Kondukter.mogulKrenuti(this, stanice.get(nextStanica), stanice.get(nextStanica + 1));

                    if(stanje == -1) {
                        sleep(1000);
                        continue;
                    }
                    else if(stanje == 0){
                        cekaj = false;
                        uStanici = false;
                        izlazimo = true;
                        if(speedLimited)
                            revertSpeed();
                    }
                    else if(stanje >= 1){
                        setBrzina(stanje);
                        speedLimited = true;
                        cekaj = false;
                        uStanici = false;
                        izlazimo = true;
                    }
                }


                for(int index = 0; index <= control-1; index++) {
                    if (index < lokomotive.size()) {
                        move(lokomotive.get(index));
                    } else
                        move(vagoni.get(index - lokomotive.size()));
                }
                if(control < duzina)
                    control++;
                else
                    izlazimo = false;
            }
        } catch (Exception exception){
            LOGGER.warning(exception.fillInStackTrace().toString());
        }
    }

    public void revertSpeed() {
        this.brzina = startBrzina;
        speedLimited = false;
    }

    public String toString(){
        return name + " " + stanice;
    }
}