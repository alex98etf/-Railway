package model.watcther;

import java.nio.file.*;
import java.util.TimerTask;
import java.util.logging.Logger;

public abstract class DirWatcher extends TimerTask {
    private static final Logger LOGGER = Logger.getLogger(DirWatcher.class.getName());
    WatchService watchService;
    Path path;
    WatchKey key;

    public DirWatcher(String path){
        try {
            watchService = FileSystems.getDefault().newWatchService();
            this.path = Paths.get(path);
            this.path.register(watchService, StandardWatchEventKinds.ENTRY_CREATE);
        } catch (Exception exception){
            LOGGER.warning(exception.fillInStackTrace().toString());
        }
    }

    public final void run() {
        try {
            if ((key = watchService.take()) != null) {
                for (WatchEvent<?> event : key.pollEvents()) {
                    if(StandardWatchEventKinds.ENTRY_CREATE.equals(event.kind()))
                        onChange(event.context().toString());
                }
                key.reset();
            }
        } catch (Exception exception) {
            LOGGER.warning(exception.fillInStackTrace().toString());
        }
    }
    protected abstract void onChange(String name);
}
