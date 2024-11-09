package raf.draft.dsw.gui.swing;

import lombok.Getter;
import raf.draft.dsw.core.ApplicationFramework;
import raf.draft.dsw.model.messages.MessageType;

import javax.swing.*;
import java.time.LocalDateTime;

@Getter
public class AddNodeFrame {

    String tip;

    public AddNodeFrame() {
        tip=typeOfNode();
    }

    public String typeOfNode() {
        Object[] options = {"Building", "Room"};

        // Prikazivanje JOptionPane dialoga za izbor opcije
        int choice = JOptionPane.showOptionDialog(
                ///da li treba da bude parent component null ili MainFrame
                MainFrame.getInstance(),
                "Choose a project type:",
                "Project types",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.PLAIN_MESSAGE,
                null,
                options,
                options[0]);

        // Kreiranje objekta na osnovu izbora
        switch (choice) {
            case 0:
                return "Building";
            case 1:
                return "Room";
            default:
                // ne radi ovde?
                ApplicationFramework.getInstance().getMessageGenerator().generateMessage("Niste izabrali nijedan objekat!", MessageType.WARNING, LocalDateTime.now());
                return null; // Ako korisnik zatvori dialog ili ne odabere ništa
        }
    }

}
