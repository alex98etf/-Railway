package main;

import gui.MainWindow;
import gui.Refresh;
import model.stanica.Kondukter;
import model.watcther.DirWatcher;
import model.watcther.FileWatcher;
import model.kompozicija.Kompozicija;
import model.put.Putevi;
import model.stanica.Stanica;
import model.vozila.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;

import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;


/**
 * @author Alex
 */
public class Zeljeznica {
    private static final Logger LOGGER = Logger.getLogger(MainWindow.class.getName());

    public static final int MAP_SIZE = 30;
    private static final String CONF_FILE = "conflib.txt";
    public static Object[][] background = new Object[MAP_SIZE][MAP_SIZE];
    public static Object[][] map = new Object[MAP_SIZE][MAP_SIZE];
    public static ArrayList<Stanica> stanice = new ArrayList<>();
    private static String mainMap;
    public static Integer[] brzinePuteva = new Integer[3];
    public static Integer[] kapacitetPuteva = new Integer[3];
    public static String dirKretanja;
    public static String dirVozovi;
    public static boolean START;
    public static boolean PAUZA;
    private static Kreator kreator;
    private static final ScheduledExecutorService executor = Executors.newScheduledThreadPool(2);

    public static void main(String[] args) {

        setUp();

        kreator = new Kreator();

        stanice.add(new Stanica("A"));
        stanice.add(new Stanica("B"));
        stanice.add(new Stanica("C"));
        stanice.add(new Stanica("D"));
        stanice.add(new Stanica("E"));
        stanice.add(new Stanica("O"));

        // Kreiranje i osvjezavanje GUI-ja
        Refresh refresh = new Refresh();
        refresh.start();
    }

    private static void setUp(){
        getConfig();
        getMainMap();
        setMainMap();
        watchConf();
        watchVozovi();
    }

    public static void getConfig(){
        try{
            FileInputStream stream = new FileInputStream(CONF_FILE);
            BufferedReader reader = new BufferedReader(new InputStreamReader(stream));

            String[] confData = reader.readLine().split(";");

            brzinePuteva[0] = Integer.parseInt(confData[0]);
            brzinePuteva[1] = Integer.parseInt(confData[1]);
            brzinePuteva[2] = Integer.parseInt(confData[2]);

            kapacitetPuteva[0] = Integer.parseInt(confData[3]);
            kapacitetPuteva[1] = Integer.parseInt(confData[4]);
            kapacitetPuteva[2] = Integer.parseInt(confData[5]);

            dirVozovi = confData[6];
            dirKretanja = confData[7];
            stream.close();
            reader.close();

        } catch (Exception exception) {
            LOGGER.warning(exception.fillInStackTrace().toString());
        }
    }
    private static void getMainMap(){
        try{
            FileInputStream stream = new FileInputStream("map.txt");
            BufferedReader reader = new BufferedReader(new InputStreamReader(stream));

            mainMap = reader.readLine();

            stream.close();
            reader.close();

        } catch (Exception exception) {
            LOGGER.warning(exception.fillInStackTrace().toString());
        }
    }
    private static void setMainMap(){
        for(int i = 0; i < MAP_SIZE; ++i)
            for (int j = 0; j < MAP_SIZE; ++j){
                char c = mainMap.charAt(MAP_SIZE*i+j);
                if(c == '0')
                    background[i][j] = null;
                else
                    background[i][j] = new Putevi(c);
            }
    }
    private static void watchConf(){
        TimerTask task = new FileWatcher(CONF_FILE) {
            @Override
            protected void onChange(File file) {
                getConfig();
            }
        };
        Timer timer = new Timer(true);
        timer.schedule(task,new Date(), 1000);
    }
    private static void watchVozovi(){
        TimerTask task = new DirWatcher(dirVozovi) {
            @Override
            protected void onChange(String name) {
                readKompozicija(name);
            }
        };

        Timer timer = new Timer(true);
        timer.schedule(task,new Date(),1000);
    }
    private static void readAllKompozicije(){
        File vozovi = new File(dirVozovi);
        File[] listOfVozovi = vozovi.listFiles();

        if(listOfVozovi != null)
            for(File file  : listOfVozovi)
                if(file.isFile())
                    readKompozicija(file.getName());

    }
    private static void readKompozicija(String name){
        try {
            FileInputStream stream = new FileInputStream(Zeljeznica.dirVozovi + File.separator + name );
            BufferedReader reader = new BufferedReader(new InputStreamReader(stream));

            String[] text = new String[4];

            int i = 0;
            while(reader.ready() && i<=4)
                text[i++] = reader.readLine();

            stream.close();
            reader.close();

            String[] linija = text[0].split(";");
            int brzina = Integer.parseInt(text[1]);
            String[] lokomotive = text[2].split(";");

            // Provjera Linije
            if(!Kondukter.checkLinija(linija))
                throw new Exception("Nije Moguce Ostvariti Zeljenu Liniju");

            Kompozicija kompozicija = new Kompozicija(name, linija, brzina);

            if(i > 3) {
                String[] vagoni = text[3].split(";");

                // Namjenski vagoni mogu samo sa Manevarskim i Univerzalnim loko.
                if (text[3].contains("n")) {
                    if (!(text[2].contains("U") || text[2].contains("M")) || (text[2].contains("T") || text[2].contains("P")))
                        throw new Exception("Nije moguca kombinacija vagona i lokomotiva");
                }

                // Teretni vagoni mogu samo sa Uni. i Teretnim loko.
                if (text[3].contains("t"))
                    if ((text[2].contains("P") || text[2].contains("M")) || !(text[2].contains("T") || text[2].contains("U")))
                        throw new Exception("Nije moguca kombinacija vagona i lokomotiva");

                if(text[3].contains("s") || text[3].contains("l") || text[3].contains("r"))
                    if(!(text[2].contains("U") || text[2].contains("P")) || (text[2].contains("T") || text[2].contains("M")))
                        throw new Exception("Nije moguca kombinacija vagona i lokomotiva");

                // Dodavanje vagona u kompoziciju
                for (String vagon : vagoni)
                    if (vagon.contains("-")) {
                        String[] tip = vagon.split("-");
                        kompozicija.addVagon(tip[0], tip[1]);
                    }
            }

            // Dodavanje lokomotiva u kompoziciju
            for (String loko: lokomotive)
                if (loko.contains("-")) {
                    String[] tip = loko.split("-");
                    kompozicija.addLokomotiva(tip[0], tip[1]);
                }

            String[] kordinate = Kondukter.postavi(kompozicija,linija[0],linija[1]);
            kompozicija.setPocetneKordinate(kordinate);
            if(START)
                kompozicija.start();

        } catch (Exception exception) {
            LOGGER.warning(exception.fillInStackTrace().toString());
        }
    }
    public static void runSimulacija(boolean status) {
        readAllKompozicije();
        kreator.setStart(status);
        stanice.forEach(Stanica::startKompozicije);
        START = status;
        executor.scheduleAtFixedRate(kreator,0,2, TimeUnit.SECONDS);
    }
    public static void setPauza(){
        PAUZA = !PAUZA;
        if(PAUZA){
            kreator.setPauza(false);
            stanice.forEach(s -> s.pauzirajKompozicije(false));
        }
        else{
            kreator.setPauza(true);
            stanice.forEach(s -> s.pauzirajKompozicije(true));
        }
    }
    // Not in use
    public static void zaustavi(){
        kreator.zaustavi();
        stanice.forEach(Stanica::zaustavi);
        executor.shutdownNow();
    }
}
