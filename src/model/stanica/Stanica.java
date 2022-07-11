package model.stanica;

import model.kompozicija.Kompozicija;

import java.util.ArrayList;
import java.util.Collections;
import java.util.logging.Logger;

/**
 * @author Alex
 */
public class Stanica extends Thread{
    private static final Logger LOGGER = Logger.getLogger(Stanica.class.getName());

    private ArrayList<Kompozicija> kompozicije = new ArrayList<>();
    private ArrayList<String> dolasci = new ArrayList<>();
    private ArrayList<String> odlasci = new ArrayList<>();
    private final String name;


    public Stanica(String name){
        this.name = name;
    }

    public String getStanicaName(){ return this.name; }

    public void addKompoziciju(Kompozicija kompozicija){ kompozicije.add(kompozicija); }

    public void removeKompoziciju(Kompozicija kompozicija) { kompozicije.remove(kompozicija);}

    public void addDolazak(String dolazak){
        dolasci.add(dolazak);
    }

    public void removeDolazak(String dolazak){
        dolasci.remove(dolazak);
    }

    public void addOdlasci(String odlazak){
        odlasci.add(odlazak);
    }

    public void removeOdlasci(String odlazak){
        odlasci.remove(odlazak);
    }

    public boolean chechOdlaske(){
        return odlasci.isEmpty();
    }

    public boolean checkOdlaske(String stanica){
        long count = odlasci.stream().filter(o -> o.equals(stanica)).count();
        return count > 1L;
    }

    public boolean checkIzlaske(String stanica){
        if(!kompozicije.isEmpty())
            return kompozicije.stream().anyMatch(k -> k.getNextStanica().equals(stanica) && !k.isuStanici());
        return false;
    }

    public boolean chechDolaske(){
        return dolasci.isEmpty();
    }

    public boolean checkDolaske(String stanica){
        return dolasci.stream().anyMatch(d -> d.equals(stanica));
    }

    public void printLinije(){
        System.out.println(name);
        System.out.println("Dolaza : " + dolasci);
        System.out.println("Odlazci : " + odlasci);
    }

    /**
     *
     * @param kompozicija
     * @param stanica
     * @return Vraca brzinu kompozicije ispred mene
     */
    public int setSpeedLimit(Kompozicija kompozicija,String stanica){
        if(!kompozicije.isEmpty()){
            int index = kompozicije.indexOf(kompozicija);
            Kompozicija tmpKomp = kompozicije.get(index-1);
            return tmpKomp.getBrzina();
        }
        return 1;
    }

    public void revertSpeed(String stanica) {
        if(!kompozicije.isEmpty()) {
            Kompozicija kompozicija = kompozicije.stream().filter(k -> k.getNextStanica().equals(stanica) && k.isSpeedLimited()).findFirst().orElse(null);
            if (kompozicija != null)
                kompozicija.revertSpeed();
        }
    }

    public boolean izlazi(String stanica) {
        if(!kompozicije.isEmpty()){
            return kompozicije.stream().anyMatch(k -> k.getNextStanica().equals(stanica) && k.izlazim());
        }
        return false;
    }

    public void startKompozicije(){
        kompozicije.forEach(Thread::start);
    }

    public void pauzirajKompozicije(boolean stanje){
        kompozicije.forEach(k -> k.setPauza(stanje));
    }

    public void zaustavi(){
        kompozicije.forEach(Kompozicija::kill);
    }

    public Kompozicija getFirstStart(String stanica){
        return kompozicije.stream().filter(k -> k.isuStanici() && k.getNextStanica().equals(stanica)).findFirst().orElse(null);
    }

    @Override
    public String toString() { return "Stanica: " + name;}

}
