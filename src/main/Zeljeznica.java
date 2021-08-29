package main;

import gui.MainWindow;
import model.kompozicija.Kompozicija;
import model.put.Putevi;
import model.vozila.*;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.logging.Logger;

public class Zeljeznica {
    private static final Logger LOGGER = Logger.getLogger(MainWindow.class.getName());

    public static final int MAP_SIZE = 30;
    public static Object[][] background = new Object[MAP_SIZE][MAP_SIZE];
    public static Object[][] map = new Object[MAP_SIZE][MAP_SIZE];
    private static String mainMap;
    private static Vozilo vozilo;
    public static Integer[] brzinePuteva = new Integer[3];
    public static Integer[] kapacitetPuteva = new Integer[3];
    public static String dirKretanja;
    public static String dirVozovi;
    public static boolean start;

    public static void main(String[] args){
        setUp();
        System.out.println(background[0][0]);
        vozilo = new Vozilo();
        Kompozicija kom = new Kompozicija(2,2, "A",50);
        map[0][13] = vozilo;

        vozilo.start();
        vozilo.setPauza(false);
        new MainWindow();
       /* while (true){
            vozilo.setPauza(!start);
        }*/

    }

    private static void setUp(){
        getConfig();
        getMainMap();
        setMainMap();
    }
    private static void getConfig(){
        try{
            FileInputStream stream = new FileInputStream("conflib.txt");
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
    public static void setPauza() {
        vozilo.setPauza(true);
        System.out.println(vozilo.getPauza());
    }
}
