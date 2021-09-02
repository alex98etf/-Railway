package model.vozila;

import main.Zeljeznica;

import java.util.ArrayList;
import java.util.Random;
import java.util.logging.Logger;

public class Kreator implements Runnable {

    private static final Logger LOGGER = Logger.getLogger(Kreator.class.getName());

    private final ArrayList<Vozilo> put_0 = new ArrayList<>();
    private final ArrayList<Vozilo> put_1 = new ArrayList<>();
    private final ArrayList<Vozilo> put_2 = new ArrayList<>();
    private int stanjePuta_0 = 0;
    private int stanjePuta_1 = 0;
    private int stanjePuta_2 = 0;
    private boolean start = false;
    private static final Random rand = new Random();


    public Kreator(){}

    public boolean isStart() { return start; }

    public void setStart(boolean start) { this.start = start; }
    public void setPauza(boolean status){
        put_0.forEach(v -> v.setPauza(status));
        put_1.forEach(v -> v.setPauza(status));
        put_2.forEach(v -> v.setPauza(status));
    }
    @Override
    public void run(){
            if(!start){
                synchronized (this) {
                    try {
                        wait(20);
                    } catch (Exception exception) {
                        LOGGER.warning(exception.fillInStackTrace().toString());
                    }
                }
            }
            System.out.println(stanjePuta_0 + " " + stanjePuta_1+" " + stanjePuta_2);
            if(stanjePuta_0 <= Zeljeznica.kapacitetPuteva[0]) {
                if (rand.nextBoolean()) {
                    Auto auto = new Auto(Zeljeznica.brzinePuteva[0]);
                    if (rand.nextBoolean()) {
                        auto.setSmjer('r');
                        Zeljeznica.map[21][0] = auto;
                        auto.start();
                    } else {
                        auto.setSmjer('u');
                        Zeljeznica.map[28][8] = auto;
                        auto.start();
                    }
                    auto.setPauza(false);
                    put_0.add(auto);
                } else {
                    Kamion kamion = new Kamion(Zeljeznica.brzinePuteva[0]);
                    if (rand.nextBoolean()) {
                        kamion.setSmjer('r');
                        Zeljeznica.map[21][0] = kamion;
                    } else {
                        kamion.setSmjer('u');
                        Zeljeznica.map[28][8] = kamion;
                    }
                    kamion.start();
                    kamion.setPauza(false);
                    put_0.add(kamion);
                    stanjePuta_0++;
                }
            }
            else {
                  Vozilo vozilo =  put_0.stream().filter(Vozilo::isStigao).findFirst().orElse(null);
//                put_0.removeIf(Vozilo::isStigao)
               if(vozilo != null){
                   stanjePuta_0--;
                   put_0.remove(vozilo);
                   try {
                       vozilo.join();
                   } catch (Exception exception) {
                       LOGGER.warning(exception.fillInStackTrace().toString());
                   }
                   System.out.println("stigao");
               }
            }

            if(stanjePuta_1 <= Zeljeznica.kapacitetPuteva[1]) {
                if (rand.nextBoolean()) {
                    Auto auto = new Auto(Zeljeznica.brzinePuteva[1]);
                    if (rand.nextBoolean()) {
                        auto.setSmjer('d');
                        Zeljeznica.map[0][13] = auto;
                    } else {
                        auto.setSmjer('u');
                        Zeljeznica.map[28][14] = auto;
                    }
                    auto.start();
                    auto.setPauza(false);
                    put_1.add(auto);
                } else {
                    Kamion kamion = new Kamion(Zeljeznica.brzinePuteva[1]);
                    if (rand.nextBoolean()) {
                        kamion.setSmjer('d');
                        Zeljeznica.map[0][13] = kamion;
                    } else {
                        kamion.setSmjer('u');
                        Zeljeznica.map[28][14] = kamion;
                    }
                    put_1.add(kamion);
                    kamion.setPauza(false);
                    kamion.start();
                    stanjePuta_1++;
                }
            }
            else
            {
                Vozilo vozilo =  put_1.stream().filter(Vozilo::isStigao).findFirst().orElse(null);
//                put_0.removeIf(Vozilo::isStigao)
                if(vozilo != null){
                    try {
                        stanjePuta_1--;
                        put_1.remove(vozilo);
                        vozilo.join();
                    } catch (Exception exception) {
                        LOGGER.warning(exception.fillInStackTrace().toString());
                    }
                    System.out.println("stigao");
                }
            }



        if(stanjePuta_2 <= Zeljeznica.kapacitetPuteva[2]) {
                if (rand.nextBoolean()) {
                    Auto auto = new Auto(Zeljeznica.brzinePuteva[2]);
                    if (rand.nextBoolean()) {
                        auto.setSmjer('l');
                        Zeljeznica.map[20][28] = auto;
                    } else {
                        auto.setSmjer('u');
                        Zeljeznica.map[28][22] = auto;
                    }
                    auto.start();
                    auto.setPauza(false);
                    put_2.add(auto);
                } else {
                    Kamion kamion = new Kamion(Zeljeznica.brzinePuteva[2]);
                    if (rand.nextBoolean()) {
                        kamion.setSmjer('l');
                        Zeljeznica.map[20][28] = kamion;
                    } else {
                        kamion.setSmjer('u');
                        Zeljeznica.map[28][22] = kamion;
                    }
                    kamion.start();
                    kamion.setPauza(false);
                    put_2.add(kamion);
                    stanjePuta_2++;
                }
            }
            else{
            Vozilo vozilo =  put_2.stream().filter(Vozilo::isStigao).findFirst().orElse(null);
//                put_0.removeIf(Vozilo::isStigao)
            if(vozilo != null){
                try {
                    stanjePuta_2--;
                    put_2.remove(vozilo);
                    vozilo.join();
                } catch (Exception exception) {
                    LOGGER.warning(exception.fillInStackTrace().toString());
                }
                System.out.println("stigao");
            }
        }

        }

}

