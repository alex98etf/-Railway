package main;

import gui.MainWindow;
import main.fileWatcther.DirWatcher;
import main.fileWatcther.FileWatcher;
import model.kompozicija.Kompozicija;
import model.put.Putevi;
import model.vozila.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;



public class Zeljeznica {
    private static final Logger LOGGER = Logger.getLogger(MainWindow.class.getName());

    public static final int MAP_SIZE = 30;
    private static final String CONF_FILE = "conflib.txt";
    public static Object[][] background = new Object[MAP_SIZE][MAP_SIZE];
    public static Object[][] map = new Object[MAP_SIZE][MAP_SIZE];
    private static String mainMap;
    private static Vozilo vozilo;
    public static Integer[] brzinePuteva = new Integer[3];
    public static Integer[] kapacitetPuteva = new Integer[3];
    public static String dirKretanja;
    public static String dirVozovi;
    public static boolean START;
    public static boolean PAUZA;
    private static Kreator kreator;
    private static ScheduledExecutorService executor = Executors.newScheduledThreadPool(2);

    public static void main(String[] args) {
        setUp();
        System.out.println(java.time.LocalTime.now());
        System.out.println(Arrays.toString(kapacitetPuteva));
         kreator = new Kreator();
//        kreator.setPuza(false);

        //Kompozicija kom = new Kompozicija(2,2, "A",50);
//        vozilo = new Auto(100);
//        map[0][13] = vozilo;
//        vozilo.setSmjer('d');
//
//        vozilo.start();
//        vozilo.setPauza(false);


        MainWindow main = new MainWindow();
        try {
            while (true) {
                main.getMatrixPanel().revalidate();
                main.getMatrixPanel().repaint();
                Thread.sleep(20);
            }
        } catch (Exception exception) {
            LOGGER.warning(exception.fillInStackTrace().toString());
        }
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
            FileInputStream stream = new FileInputStream("src/map.txt");
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
                System.out.println("radi");
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
                System.out.println("KILL OR BE KILLED");
                readKompozicija(name);
            }
        };

        Timer timer = new Timer(true);
        timer.schedule(task,new Date(),1000);
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

            String[] stanice = text[0].split(";");
            int brzina = Integer.parseInt(text[1]);
            String[] lokomotive = text[2].split(";");
            String[] vagoni = text[3].split(";");



        } catch (Exception exception) {
            LOGGER.warning(exception.fillInStackTrace().toString());
        }
    }
    public static void runSimulacija(boolean status) {
        kreator.setStart(status);
        START = status;
        executor.scheduleAtFixedRate(kreator,0,2, TimeUnit.SECONDS);
        System.out.println("Start " + status);
    }
    public static void setPauza(boolean status){
        kreator.setPauza(status);
    }
}
