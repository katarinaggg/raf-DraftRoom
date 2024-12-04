package raf.draft.dsw.gui.swing;

import lombok.NoArgsConstructor;
import raf.draft.dsw.controller.observer.ISubscriber;
import raf.draft.dsw.model.notification.Notification;
import raf.draft.dsw.model.structures.Building;
import raf.draft.dsw.model.structures.Project;
import raf.draft.dsw.model.structures.Room;
import raf.draft.dsw.state.StateManager;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;


public class ProjectView extends JPanel implements ISubscriber {
    private JTabbedPane tabbedPane;
    private JToolBar sideToolBar;
    private JPanel infoPanel;
    private JLabel infoLabel;
    private JLabel authorLabel;
    private StateManager stateManager = new StateManager();

    public ProjectView() {
        tabbedPane = new JTabbedPane();
        infoLabel = new JLabel();
        authorLabel = new JLabel();
    }

    public ProjectView(Project selectedProject) {
        setLayout(new BorderLayout());

        infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));

        infoLabel = new JLabel();
        authorLabel = new JLabel();

        infoPanel.add(infoLabel);
        infoPanel.add(authorLabel);
        add(infoPanel, BorderLayout.NORTH);

        // Dodavanje desnog panela
        sideToolBar = new JToolBar();
        sideToolBar.setLayout(new BoxLayout(sideToolBar, BoxLayout.Y_AXIS));
        add(sideToolBar, BorderLayout.EAST);

        // tabbedPane na kom redjamo tabove
        tabbedPane = new JTabbedPane();
        add(tabbedPane, BorderLayout.CENTER);
        // metoda za kreiranje tabova na osnovu liste soba
        // koju izvlacimo iz selektovanog projekta
        displayRooms(selectedProject);

        if (!selectedProject.getRooms().isEmpty()) {
            updateInfoPanel(selectedProject, selectedProject.getRooms().get(0));
        }

        // Dodavanje kontrola u desni panel
        setupSidePanel();

    }

    private void setupSidePanel() {
        // Dugme za Edit Room State
        JButton editRoomButton = createIconButton("images/editRoom.png", "Edit Room");
        sideToolBar.add(editRoomButton);

        // Dugme za Add State
        JButton addElementButton = createIconButton("images/plus.png", "Dodaj Element");
        sideToolBar.add(addElementButton);

        // Dugme za Select State
        JButton selectElementButton = createIconButton("images/tap.png", "Selektuj Element");
        sideToolBar.add(selectElementButton);

        // Dugme za Resize State
        JButton resizeElementButton = createIconButton("images/resize.png", "Promeni Dimenzije");
        sideToolBar.add(resizeElementButton);

        // Dugme za Rotate State
        JButton rotateLeftButton = createIconButton("images/rotateLeft.png", "Rotiraj Levo");
        sideToolBar.add(rotateLeftButton);

        JButton rotateRightButton = createIconButton("images/rotateRight.png", "Rotiraj Desno");
        sideToolBar.add(rotateRightButton);

        // Dugme za Copy/Paste State
        JButton copyPasteElementButton = createIconButton("images/copypaste.png", "Kopiraj Element");
        sideToolBar.add(copyPasteElementButton);

        // Dugme za Move State
        JButton moveElementButton = createIconButton("images/move.png", "Pomeranje");
        sideToolBar.add(moveElementButton);

        // Dugme za Zoom State
        JButton zoomButton = createIconButton("images/zoom-in.png", "Zumiranje");
        sideToolBar.add(zoomButton);

        JButton editButton = createIconButton("images/editProject.png", "Edit");
        sideToolBar.add(editRoomButton);

        // Dugme za Delete State
        JButton deleteElementButton = createIconButton("images/remove.png", "Obriši Element");
        sideToolBar.add(deleteElementButton);
    }

    private JButton createIconButton(String iconPath, String tooltip) {
        JButton button = new JButton();
        button.setToolTipText(tooltip);

        // Postavljanje ikone na dugme
        try {
            ImageIcon icon = new ImageIcon(getClass().getClassLoader().getResource(iconPath));
            Image scaledIcon = icon.getImage().getScaledInstance(32, 32, Image.SCALE_SMOOTH);
            button.setIcon(new ImageIcon(scaledIcon));
        } catch (Exception e) {
            System.err.println("Ikona nije pronađena: " + iconPath);
        }

        button.setFocusPainted(false);
        button.setContentAreaFilled(false);
        button.setBorderPainted(false);
        return button;
    }


    public void displayRooms(Project selectedProject) {
        tabbedPane.removeAll();  // uklanjamo stare i otvaramo nove tabove

        tabbedPane.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);

        // prolazimo kroz sve sobe iz liste
        // i dodajemo JPanel i tab za tu sobu

        //ako je lista soba prazna treba da se obrise i labela sa viewa?
        if(selectedProject.getRooms().isEmpty()) {
            infoLabel.setText("");
            authorLabel.setText("");
        }

        for (Room room : selectedProject.getRooms()) {

            RoomView roomPanel = new RoomView(room); // panele za uredjivanje soba
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
            if (selectedIndex >= 0 && selectedIndex < selectedProject.getRooms().size()) {
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
        // ako brisemo ceo projekat brisemo i view
        if(notification.getDraftNode() instanceof Project && notification.getBrisanje()){
            tabbedPane.removeAll();
            infoLabel.setText("");
            authorLabel.setText("");
        }
        if(notification.getDraftNode() instanceof Project && !notification.getBrisanje()) {
            displayRooms((Project) notification.getDraftNode());
        }
    }

}
