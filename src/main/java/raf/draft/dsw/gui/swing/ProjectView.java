package raf.draft.dsw.gui.swing;

import lombok.NoArgsConstructor;
import raf.draft.dsw.controller.observer.ISubscriber;
import raf.draft.dsw.model.notification.Notification;
import raf.draft.dsw.model.structures.Building;
import raf.draft.dsw.model.structures.Project;
import raf.draft.dsw.model.structures.Room;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;


public class ProjectView extends JPanel implements ISubscriber {
    private JTabbedPane tabbedPane;

    private JPanel infoPanel;
    private JLabel infoLabel;
    private JLabel authorLabel;

    public ProjectView(Project selectedProject) {
        setLayout(new BorderLayout());

        infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));

        infoLabel = new JLabel();
        authorLabel = new JLabel();

        infoPanel.add(infoLabel);
        infoPanel.add(authorLabel);
        add(infoPanel, BorderLayout.NORTH);


        // tabbedPane na kom redjamo tabove
        tabbedPane = new JTabbedPane();
        add(tabbedPane, BorderLayout.CENTER);
        // metoda za kreiranje tabova na osnovu liste soba
        // koju izvlacimo iz selektovanog projekta
        displayRooms(selectedProject);

        if (!selectedProject.getRooms().isEmpty()) {
            updateInfoPanel(selectedProject, selectedProject.getRooms().get(0));
        }
    }


    public void displayRooms(Project selectedProject) {
        tabbedPane.removeAll();  // uklanjamo stare i otvaramo nove tabove

        tabbedPane.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);

        // prolazimo kroz sve sobe iz liste
        // i dodajemo JPanel i tab za tu sobu
        for (Room room : selectedProject.getRooms()) {

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

        tabbedPane.addChangeListener(e -> {
            int selectedIndex = tabbedPane.getSelectedIndex();
            if (selectedIndex >= 0) {
                Room selectedRoom = selectedProject.getRooms().get(selectedIndex);
                updateInfoPanel(selectedProject, selectedRoom);
            }
        });
    }

    private void updateInfoPanel(Project project, Room room) {
        if (room != null && room.getParent() instanceof Building) {
            infoLabel.setText(project.getIme() + ", " + room.getParent().getIme());
        } else {
            infoLabel.setText(project.getIme() + ", / ");
        }

        if (project.getAutorProjekta() == null) {
            authorLabel.setText("Nema unetog autora za ovaj projekat");
        } else {
            authorLabel.setText("AUTOR: " + project.getAutorProjekta());
        }
    }


    @Override
    public void update(Notification notification) {
        if(notification.getDraftNode() instanceof Room && notification.getDraftNode().getParent() instanceof Building){
            Project project = (Project) notification.getDraftNode().getParent().getParent();
            displayRooms(project);
        }
        if(notification.getDraftNode() instanceof Room && notification.getDraftNode().getParent() instanceof Project){
            Project project = (Project) notification.getDraftNode().getParent();
            displayRooms(project);
        }
        if(notification.getDraftNode() instanceof Building){
            Project project = (Project) notification.getDraftNode().getParent();
            displayRooms(project);
        }
        if(notification.getDraftNode() instanceof Project){
            displayRooms((Project)notification.getDraftNode());
        }
    }
}
