package raf.draft.dsw.gui.swing.jtree;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import raf.draft.dsw.gui.swing.MainFrame;
import raf.draft.dsw.model.nodes.DraftNode;
import raf.draft.dsw.model.structures.Building;
import raf.draft.dsw.model.structures.Project;
import raf.draft.dsw.model.structures.ProjectExplorer;
import raf.draft.dsw.model.structures.Room;

import javax.swing.*;
import java.util.Random;

@Data@NoArgsConstructor
public class ChildFactory {

    private static int counterProject = 1;
    private static int counterBuilding = 1;
    private static int counterRoom = 1;


    public DraftNode createChild(DraftNode parent){
        if(parent instanceof ProjectExplorer){
            return new Project("Project " + counterProject++, parent);
        }
        else if(parent instanceof Project){
            /// otvori se prozor i bira da li zeli Building ili Room


            Object[] options = {"Building", "Room"};

            // Prikazivanje JOptionPane dialoga za izbor opcije
            int choice = JOptionPane.showOptionDialog(
                    ///da li treba da bude parent component null ili MainFrame
                    null,
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
                    return new Building("Building " + counterBuilding++, parent);
                case 1:
                    return new Room("Room " + counterRoom++, parent);
                default:
                    return null; // Ako korisnik zatvori dialog ili ne odabere ništa
            }
        }
        else if(parent instanceof Building){
            return new Room("Room " + counterRoom++, parent);
        }
        return null;
    }
}
