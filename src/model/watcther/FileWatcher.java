package model.watcther;

import java.io.File;
import java.util.TimerTask;

public abstract class FileWatcher extends TimerTask {
    private long timeStamp;
    private final File file;

    public FileWatcher(String fileName){
        this.file = new File(fileName);
        this.timeStamp = file.lastModified();
    }

    public final void run(){
        long newTimeStamp = file.lastModified();
        if(timeStamp != newTimeStamp){
            this.timeStamp = newTimeStamp;
            onChange(file);
        }
    }
    protected abstract void onChange(File file);
}
