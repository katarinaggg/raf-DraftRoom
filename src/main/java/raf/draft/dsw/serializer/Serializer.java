package raf.draft.dsw.serializer;

import com.fasterxml.jackson.databind.ObjectMapper;
import raf.draft.dsw.core.ApplicationFramework;
import raf.draft.dsw.model.nodes.DraftNode;
import raf.draft.dsw.model.nodes.DraftNodeComposite;
import raf.draft.dsw.model.notification.Message;
import raf.draft.dsw.model.notification.MessageType;
import raf.draft.dsw.model.notification.Notification;
import raf.draft.dsw.model.structures.Project;
import raf.draft.dsw.model.structures.Room;

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
            Message message = ApplicationFramework.getInstance().getMessageGenerator().generateMessage("Ne postoje izmene koje bi mogle da se sacuvaju", MessageType.INFORMATION, LocalDateTime.now());
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
            Message message = ApplicationFramework.getInstance().getMessageGenerator().generateMessage("Projekat uspesno sacuvan", MessageType.INFORMATION, LocalDateTime.now());
            ApplicationFramework.getInstance().getMessageGenerator().notifySubscribers(new Notification(message));
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


    // metoda za otvaranje projekta vraca odgovarajuci Project
    // koji treba pravilno da ucitamo

    public Project openProject() {
        JFileChooser fileChooser = new JFileChooser();
        if (fileChooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            try {
                // ucitavanje projekta iz JSON datoteke
                Project project = objectMapper.readValue(file, Project.class);

                // povezivanje sa parentima
                setParentReferences(project);

                project.setPath(file.getAbsolutePath());

                // postavljamo changed na false jer je projekat učitan
                project.setChanged(false);

                System.out.println("USPESNO OTVOREN");
                return project;

            } catch (IOException e) {

                e.printStackTrace();

                Message errorMessage = ApplicationFramework.getInstance().getMessageGenerator().generateMessage("Došlo je do greške prilikom učitavanja projekta", MessageType.ERROR, LocalDateTime.now());
                ApplicationFramework.getInstance().getMessageGenerator().notifySubscribers(new Notification(errorMessage));
            }
        }
        return null; // ako korisnik otkaže izbor, vraća null
    }

    private void setParentReferences(DraftNode node) {
        if (node instanceof DraftNodeComposite) {
            DraftNodeComposite composite = (DraftNodeComposite) node;
            for (DraftNode child : composite.getChildren()) {
                // postavljanje parenta
                child.setParent(node);

                // rekurzivno i za svu njegovu decu
                setParentReferences(child);
            }
        }
    }

    public void sacuvajSablonSobe(Room room){

        File templateDir = new File("src/main/resources/templates");
        if (!templateDir.exists()) {
            System.out.println("NE POSTOJI");
            return;
        }

        String fileName = room.getIme() + ".json";
        File file = new File(templateDir, fileName);

        try {
            objectMapper.writeValue(file, room);
            Message message = ApplicationFramework.getInstance().getMessageGenerator().generateMessage("Uspesno sacuvana soba", MessageType.INFORMATION, LocalDateTime.now());
            ApplicationFramework.getInstance().getMessageGenerator().notifySubscribers(new Notification(message));
        } catch (IOException e) {
            e.printStackTrace();
            Message message = ApplicationFramework.getInstance().getMessageGenerator().generateMessage("Došlo je do greške prilikom čuvanja šablona", MessageType.ERROR, LocalDateTime.now());
            ApplicationFramework.getInstance().getMessageGenerator().notifySubscribers(new Notification(message));
        }

    }

    public Room ucitajSablonSobe() {
        File templateDir = new File("src/main/resources/templates");

        if (!templateDir.exists() || !templateDir.isDirectory()) {
            System.out.println("Direktorijum za šablone ne postoji.");
            return null;
        }

        JFileChooser fileChooser = new JFileChooser(templateDir);
        fileChooser.setDialogTitle("Učitaj šablon sobe");

        if (fileChooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            try {
                Room room = objectMapper.readValue(file, Room.class);
                //System.out.println("Uspesno ucitan sablon: " + file.getAbsolutePath());
                return room;
            } catch (IOException e) {
                e.printStackTrace();
                Message message = ApplicationFramework.getInstance().getMessageGenerator().generateMessage("Došlo je do greške prilikom učitavanja šablona", MessageType.ERROR, LocalDateTime.now());
                ApplicationFramework.getInstance().getMessageGenerator().notifySubscribers(new Notification(message));
            }
        }
        return null;
    }


}
