package raf.draft.dsw.serializer;

import com.fasterxml.jackson.databind.ObjectMapper;
import raf.draft.dsw.core.ApplicationFramework;
import raf.draft.dsw.model.notification.Message;
import raf.draft.dsw.model.notification.MessageType;
import raf.draft.dsw.model.notification.Notification;
import raf.draft.dsw.model.structures.Project;

import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;

public class Serializer {
    private final ObjectMapper objectMapper;

    public Serializer() {
        this.objectMapper = new ObjectMapper();
    }

    // metoda za SAVE PROJECT
    public void saveProject(Project project) {
        if(!project.isChanged()){
            Message message = ApplicationFramework.getInstance().getMessageGenerator().generateMessage("Projekat nije izmenjen", MessageType.INFORMATION, LocalDateTime.now());
            ApplicationFramework.getInstance().getMessageGenerator().notifySubscribers(new Notification(message));
            return;
        }
        if(project.getPath()==null){
            JFileChooser fileChooser = new JFileChooser();
            if (fileChooser.showSaveDialog(null) == JFileChooser.APPROVE_OPTION) {
                File file = fileChooser.getSelectedFile();
                project.setPath(file.getAbsolutePath());
                saveToFile(project, file);
            }
        } else{
            saveToFile(project, new File(project.getPath()));
        }
    }


    // metoda za SAVE AS
    public void saveAs(Project project) {
        JFileChooser fileChooser = new JFileChooser();
        if (fileChooser.showSaveDialog(null) == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            project.setPath(file.getAbsolutePath());
            saveToFile(project, file);
        }
    }


    private void saveToFile(Project project, File file) {
        try {
            objectMapper.writeValue(file, project);
            project.setChanged(false); // Postavljamo changed na false nakon uspešnog čuvanja
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
