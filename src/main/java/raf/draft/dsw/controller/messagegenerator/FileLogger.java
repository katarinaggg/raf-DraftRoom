package raf.draft.dsw.controller.messagegenerator;

import raf.draft.dsw.model.notification.Message;
import raf.draft.dsw.model.notification.Notification;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class FileLogger implements Logger{

    @Override
    public void update(Notification notification) {
        File fajl = new File("log.txt");
        try {
            FileWriter fw = new FileWriter(fajl, true);
            fw.write(notification.getMessage()+"\n");
            fw.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
