package raf.draft.dsw.controller.messagegenerator;

import raf.draft.dsw.model.messages.Message;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class FileLogger implements Logger{

    @Override
    public void update(Message message) {
        File fajl = new File("log.txt");
        try {
            FileWriter fw = new FileWriter(fajl, true);
            fw.write(message.toString()+"\n");
            fw.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
