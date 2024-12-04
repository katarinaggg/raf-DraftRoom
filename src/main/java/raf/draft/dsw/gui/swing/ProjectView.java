package raf.draft.dsw.gui.swing;

import lombok.NoArgsConstructor;
import raf.draft.dsw.controller.observer.ISubscriber;
import raf.draft.dsw.model.nodes.DraftNode;
import raf.draft.dsw.model.notification.Notification;
import raf.draft.dsw.model.structures.Building;
import raf.draft.dsw.model.structures.Project;
import raf.draft.dsw.model.structures.Room;
import raf.draft.dsw.state.StateManager;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


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
        sideToolBar = new JToolBar();
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
        sideToolBar.add(editButton);

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

/*

    public void displayRooms(Project selectedProject) {
//        tabbedPane.removeAll();  // uklanjamo stare i otvaramo nove tabove
//
//        tabbedPane.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);
//
//        // prolazimo kroz sve sobe iz liste
//        // i dodajemo JPanel i tab za tu sobu
//
//        //ako je lista soba prazna treba da se obrise i labela sa viewa?
//        if(selectedProject.getRooms().isEmpty()) {
//            infoLabel.setText("");
//            authorLabel.setText("");
//        }
//
//        for (Room room : selectedProject.getRooms()) {
//
//            RoomView roomPanel = new RoomView(room); // panele za uredjivanje soba
//            tabbedPane.addTab(room.getIme(), roomPanel);
//
//            //System.out.println(room.getParent());
//
//            // ako soba za parenta ima Building treba obojiti taj tab
//            // bojom koju cuva Building
//            if(room.getParent() instanceof Building){
//                //tabbedPane.setBackground(((Building) room.getParent()).getColor());
//                Building parentBuilding = (Building) room.getParent();
//                Color buildingColor = parentBuilding.getColor();
//
//                int tabIndex = tabbedPane.indexOfComponent(roomPanel);
//                if (tabIndex != -1) {
//                    tabbedPane.setBackgroundAt(tabIndex, buildingColor);
//                }
//            }
//        }


        sideToolBar.setVisible(!selectedProject.getRooms().isEmpty());
/////////////////////////////////////////////////////////////////////////////////////////
        //  ne radi kad se menja ime Room-a

        Room firstRoom = selectedProject.getRooms().isEmpty() ? null : selectedProject.getRooms().get(0);
        updateInfoPanel(selectedProject, firstRoom);

        // Kreiramo listu trenutnih soba iz tabbedPane-a
        ArrayList<String> existingTabs = new ArrayList<>();
        for (int i = 0; i < tabbedPane.getTabCount(); i++) {
            existingTabs.add(tabbedPane.getTitleAt(i));
        }

        // Prolazimo kroz sve sobe u projektu
        for (Room room : selectedProject.getRooms()) {
            String roomName = room.getIme();
            RoomView roomPanel = new RoomView(room);

            // Ako tab za sobu ne postoji, dodajemo ga
            if (!existingTabs.contains(roomName)) {
                tabbedPane.addTab(roomName, roomPanel);

                // Ako soba ima parent Building, obojimo tab
                if (room.getParent() instanceof Building) {
                    Building parentBuilding = (Building) room.getParent();
                    Color buildingColor = parentBuilding.getColor();

                    int tabIndex = tabbedPane.indexOfComponent(roomPanel);
                    if (tabIndex != -1) {
                        tabbedPane.setBackgroundAt(tabIndex, buildingColor);
                    }
                }
            } else {
                // Ako tab postoji, proveravamo da li treba da ažuriramo sadržaj
                int tabIndex = existingTabs.indexOf(roomName);
                if (tabIndex != -1) {
                    tabbedPane.setComponentAt(tabIndex, roomPanel);
                }
            }
            existingTabs.remove(roomName); // Uklanjamo iz liste, jer još uvek postoji
        }

        // Uklanjamo sve tabove koji više nisu prisutni
        for (String roomName : existingTabs) {
            int tabIndex = tabbedPane.indexOfTab(roomName);
            if (tabIndex != -1) {
                tabbedPane.removeTabAt(tabIndex);
            }
        }

        // Postavljamo selekciju na prvi tab ako postoji
        if (tabbedPane.getTabCount() > 0) {
            tabbedPane.setSelectedIndex(0);
        }

/////////////////////////////////////////////////////////////////////////////////////////
        // od pre
        tabbedPane.addChangeListener(e -> {
            int selectedIndex = tabbedPane.getSelectedIndex();
            if (selectedIndex >= 0 && selectedIndex < selectedProject.getRooms().size()) {
                Room selectedRoom = selectedProject.getRooms().get(selectedIndex);
                updateInfoPanel(selectedProject, selectedRoom);
            }
        });
    }

 */

    public void displayRooms(Project selectedProject) {
        // Kreiramo mapu za čuvanje trenutnih tabova i njihovih indeksa
        Map<String, Integer> existingTabs = new HashMap<>();
        for (int i = 0; i < tabbedPane.getTabCount(); i++) {
            existingTabs.put(tabbedPane.getTitleAt(i), i);
        }

        // Redosled tabova mora odgovarati redosledu soba u projektu
        int tabIndex = 0;
        for (Room room : selectedProject.getRooms()) {
            String roomName = room.getIme();
            RoomView roomPanel = new RoomView(room);

            // Ako tab već postoji na odgovarajućem mestu, samo ga ažuriramo
            if (tabIndex < tabbedPane.getTabCount() && tabbedPane.getTitleAt(tabIndex).equals(roomName)) {
                tabbedPane.setComponentAt(tabIndex, roomPanel);
            } else {
                // Ako tab ne postoji ili je na pogrešnom mestu, dodajemo ga na tačno mesto
                tabbedPane.insertTab(roomName, null, roomPanel, null, tabIndex);
            }

            // Postavljamo boju taba na osnovu roditelja (Building)
            if (room.getParent() instanceof Building) {
                Building parentBuilding = (Building) room.getParent();
                Color buildingColor = parentBuilding.getColor();
                tabbedPane.setBackgroundAt(tabIndex, buildingColor);
            }

            // Povećavamo indeks za sledeći tab
            tabIndex++;
        }

        // Uklanjamo suvišne tabove koji više nisu prisutni u projektu
        while (tabIndex < tabbedPane.getTabCount()) {
            tabbedPane.removeTabAt(tabIndex);
        }

        // Selektujemo prvi tab ako postoji
        if (tabbedPane.getTabCount() > 0) {
            tabbedPane.setSelectedIndex(0);
        }

        // Proveravamo da li treba odmah osvežiti infoPanel na osnovu prvog taba
        if (!selectedProject.getRooms().isEmpty()) {
            Room firstRoom = selectedProject.getRooms().get(0);
            updateInfoPanel(selectedProject, firstRoom);
        } else {
            // Ako nema soba, brišemo informacije iz infoLabel
            infoLabel.setText("");
            authorLabel.setText("");
        }


        // Osvežavamo infoPanel na osnovu selektovanog taba
        tabbedPane.addChangeListener(e -> {
            int selectedIndex = tabbedPane.getSelectedIndex();
            if (selectedIndex >= 0 && selectedIndex < selectedProject.getRooms().size()) {
                Room selectedRoom = selectedProject.getRooms().get(selectedIndex);
                updateInfoPanel(selectedProject, selectedRoom);
            }
        });

        // Ažuriramo vidljivost sideToolBar
        sideToolBar.setVisible(!selectedProject.getRooms().isEmpty());
    }



    private void updateInfoPanel(Project project, Room room) {
        // dodato
        if(room==null){
            infoLabel.setText("");
            authorLabel.setText("");
            return;
        }

        if (room.getParent() instanceof Building) {
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
            // dodato
            remove(sideToolBar);
            infoLabel.setText("");
            authorLabel.setText("");
        }
        if(notification.getDraftNode() instanceof Project && !notification.getBrisanje()) {
            displayRooms((Project) notification.getDraftNode());
        }
    }

//    public void displayRooms2(Project selectedProject, DraftNode promenjen) {
//        Room firstRoom = selectedProject.getRooms().isEmpty() ? null : selectedProject.getRooms().get(0);
//        updateInfoPanel(selectedProject, firstRoom);
//
//        // Kreiramo listu trenutnih soba iz tabbedPane-a
//        ArrayList<String> existingTabs = new ArrayList<>();
//        for (int i = 0; i < tabbedPane.getTabCount(); i++) {
//            existingTabs.add(tabbedPane.getTitleAt(i));
//        }
//
//        // ako smo menjali Building menjali smo mu ime
//        // ili smo dodali novi ali se on svakako ne prikazuje u tabovima
//        // ako smo promenili ima na trenutno otvorenom tabu treba ga updatovati na labelli
//        if(promenjen instanceof Building){
//            firstRoom.getParent().equals(promenjen);
//            updateInfoPanel(selectedProject, firstRoom);
//        }
//
//        // ako smo menjali Project menjali smo mu ime
//        // ili smo dodali novi koji se nece prikazivati svakako jer nije selektovan
//        if(promenjen instanceof Project){
//
//        }
//
//    }

}
