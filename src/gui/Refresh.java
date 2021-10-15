package gui;

import java.util.logging.Logger;

/**
 * @author Alex
 */
public class Refresh extends Thread{
    private static final Logger LOGGER = Logger.getLogger(Refresh.class.getName());
    MainWindow main = new MainWindow();

    public Refresh(){
    }

    public void run(){
        while(true){
            main.refresh();
            try {
                sleep(20);
            } catch(Exception exception){
                LOGGER.warning(exception.fillInStackTrace().toString());
            }
        }
    }
}
