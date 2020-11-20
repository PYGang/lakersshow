package utils;

import java.io.File;
import java.util.Map;
import java.util.concurrent.Callable;

public class FileThread implements Runnable {
    private File file;
    private Map<Integer,String> nameMap;
    public FileThread(File file,Map<Integer,String> nameMap) {
        this.file = file;
        this.nameMap = nameMap;
    }

    @Override
    public void run() {
        TxtFileUtils.getMapAccount(file,nameMap);
    }
}
