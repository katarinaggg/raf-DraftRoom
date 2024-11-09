package raf.draft.dsw.gui.swing;

import lombok.NoArgsConstructor;
import raf.draft.dsw.model.structures.Building;
import raf.draft.dsw.model.structures.Project;
import raf.draft.dsw.model.structures.Room;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;


public class ProjectView extends JPanel {
    private JTabbedPane tabbedPane;

    public ProjectView(Project selectedProject) {
        setLayout(new BorderLayout());

        // labela sa informacijama o Projektu
        JLabel infoLabel = new JLabel();
        JLabel authorLabel = new JLabel();
        infoLabel.setText("NAZIV: "+selectedProject.getIme());
        if(selectedProject.getAutorProjekta()==null){
            authorLabel.setText("Nema unetog autora za ovaj projekat");
        }
        else {
            authorLabel.setText("AUTOR: " + selectedProject.getAutorProjekta());
        }
        JPanel labelPanel = new JPanel();
        labelPanel.setLayout(new BoxLayout(labelPanel, BoxLayout.Y_AXIS));
        labelPanel.add(infoLabel);
        labelPanel.add(authorLabel);

        // Add the label panel to the NORTH position of ProjectView
        add(labelPanel, BorderLayout.NORTH);

        //add(infoLabel, BorderLayout.NORTH);

        // tabbedPane na kom redjamo tabove
        tabbedPane = new JTabbedPane();
        add(tabbedPane, BorderLayout.CENTER);
        // metoda za kreiranje tabova na osnovu liste soba
        // koju izvlacimo iz selektovanog projekta
        displayRooms(selectedProject.getRooms());
    }


    public void displayRooms(ArrayList<Room> rooms) {
        tabbedPane.removeAll();  // uklanjamo stare i otvaramo nove tabove

        tabbedPane.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);

        // prolazimo kroz sve sobe iz liste
        // i dodajemo JPanel i tab za tu sobu
        for (Room room : rooms) {
            JPanel roomPanel = new JPanel(); // panele za uredjivanje soba
            tabbedPane.addTab(room.getIme(), roomPanel);

            //System.out.println(room.getParent());

            // ako soba za parenta ima Building treba obojiti taj tab
            // bojom koju cuva Building
            if(room.getParent() instanceof Building){
                //tabbedPane.setBackground(((Building) room.getParent()).getColor());
                Building parentBuilding = (Building) room.getParent();
                Color buildingColor = parentBuilding.getColor();

                int tabIndex = tabbedPane.indexOfComponent(roomPanel);
                if (tabIndex != -1) {
                    tabbedPane.setBackgroundAt(tabIndex, buildingColor);
                }
            }
        }
    }
}
