package raf.draft.dsw.gui.swing;

import raf.draft.dsw.controller.observer.ISubscriber;
import raf.draft.dsw.model.notification.Notification;
import raf.draft.dsw.model.structures.Building;
import raf.draft.dsw.model.structures.Project;
import raf.draft.dsw.model.structures.Room;
import raf.draft.dsw.state.State;
import raf.draft.dsw.state.StateManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseWheelEvent;
import java.util.HashMap;
import java.util.Map;


public class ProjectView extends JPanel implements ISubscriber {

    // MEDIJATOR

    private JTabbedPane tabbedPane;
    private JToolBar sideToolBar;
    private JPanel infoPanel;
    private JLabel infoLabel;
    private JLabel authorLabel;
    private StateManager stateManager = new StateManager();


    // mora da cuva sve RoomView-ove koje ima
    // da nam se ne bi izgubili crtezi kada menjamo nesto
    // ili prolazimo kroz tabove
    private Map<String, RoomView> roomViewMap = new HashMap<>();

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
        //sideToolBar = new JToolBar();
        //sideToolBar.setLayout(new BoxLayout(sideToolBar, BoxLayout.Y_AXIS));

        // gridLayout da bi stali svi dugmici u 2 kolone
        sideToolBar = new JToolBar(JToolBar.VERTICAL);
        sideToolBar.setLayout(new GridLayout(0, 2, 5, 5)); // 5px razmak između dugmića

        // iz nekog razloga poremeti sve
//        // Kreiranje JScrollPane-a za sideToolBar
//        scrollPane = new JScrollPane(sideToolBar);
//        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
//        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
//
//        add(scrollPane, BorderLayout.EAST);

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
        sideToolBar.add(MainFrame.getInstance().getActionManager().getEditRoomAction());
        sideToolBar.add(MainFrame.getInstance().getActionManager().getAddRoomElementAction());
        sideToolBar.add(MainFrame.getInstance().getActionManager().getSelectRoomElementAction());
        sideToolBar.add(MainFrame.getInstance().getActionManager().getResizeAction());
        sideToolBar.add(MainFrame.getInstance().getActionManager().getRotateLeftAction());
        sideToolBar.add(MainFrame.getInstance().getActionManager().getRotateRightAction());
        sideToolBar.add(MainFrame.getInstance().getActionManager().getCopyPasteAction());
        sideToolBar.add(MainFrame.getInstance().getActionManager().getMoveRoomElementAction());
        sideToolBar.add(MainFrame.getInstance().getActionManager().getZoomAction());
        sideToolBar.add(MainFrame.getInstance().getActionManager().getEditRoomElementAction());
        sideToolBar.add(MainFrame.getInstance().getActionManager().getDeleteAction());
    }
/*

    // POKUSAJ 1
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

        // POKUSAJ 2
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
            tabbedPane.setSelectedIndex(0);pre
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
    // POKUSAJ 3
    /*
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

            // kada pravimo RoomView treba da ga subscribujemo odgovarajucoj sobi
            room.addSubscriber(roomPanel);

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
*/


    // POKUSAJ 4
    public void displayRooms(Project selectedProject) {
        // Mapiranje postojećih tabova za praćenje
        Map<String, Integer> existingTabs = new HashMap<>();
        for (int i = 0; i < tabbedPane.getTabCount(); i++) {
            existingTabs.put(tabbedPane.getTitleAt(i), i);
        }

        int tabIndex = 0;

        for (Room room : selectedProject.getRooms()) {
            String roomName = room.getIme();
            RoomView roomPanel;

            // Proveravamo da li postoji RoomView za ovu sobu
            if (roomViewMap.containsKey(roomName)) {
                roomPanel = roomViewMap.get(roomName);
            } else {
                // Kreiramo novi RoomView za novu sobu
                roomPanel = new RoomView(room);
                roomViewMap.put(roomName, roomPanel);
                room.addSubscriber(roomPanel); // Subscribujemo se na odgovarajuci room
            }

            // Ako tab već postoji, ažuriramo ime i sadržaj taba
            if (tabIndex < tabbedPane.getTabCount()) {
                if (!tabbedPane.getTitleAt(tabIndex).equals(roomName)) {
                    tabbedPane.setTitleAt(tabIndex, roomName); // Ažuriramo naziv taba
                }
                tabbedPane.setComponentAt(tabIndex, roomPanel); // Ažuriramo sadržaj taba
            } else {
                // Ako tab ne postoji, dodajemo ga
                tabbedPane.insertTab(roomName, null, roomPanel, null, tabIndex);
            }

            // Postavljamo boju taba na osnovu roditelja (Building)
            if (room.getParent() instanceof Building) {
                Building parentBuilding = (Building) room.getParent();
                Color buildingColor = parentBuilding.getColor();
                tabbedPane.setBackgroundAt(tabIndex, buildingColor);
            }

            tabIndex++;
        }

        // Uklanjamo tabove i RoomView-ove koji više nisu prisutni
        while (tabIndex < tabbedPane.getTabCount()) {
            String roomName = tabbedPane.getTitleAt(tabIndex);
            tabbedPane.removeTabAt(tabIndex);
            roomViewMap.remove(roomName); // Uklanjamo iz mape
        }

        // Selektujemo prvi tab ako postoji
        if (tabbedPane.getTabCount() > 0) {
            tabbedPane.setSelectedIndex(0);
        }

        // Ažuriramo infoPanel na osnovu selektovanog taba
        if (!selectedProject.getRooms().isEmpty()) {
            Room firstRoom = selectedProject.getRooms().get(0);
            updateInfoPanel(selectedProject, firstRoom);
        } else {
            // Ako nema soba, brišemo informacije iz infoLabel
            infoLabel.setText("");
            authorLabel.setText("");
        }

        // Dodajemo listener za promene selektovanog taba
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

    // posto je medijator treba da ima sve metode iz stanja

    // stateManager ce za trenutno aktivan state da pozove njegovu odgovarajucu metodu
    public void misKliknut(RoomView roomView, Point point) {
        stateManager.getCurrentState().misKliknut(roomView, point);
    }


    public void misPomeren() {
        stateManager.getCurrentState().misPomeren();
    }


    public void misScroll(RoomView roomView, MouseWheelEvent e) {
        stateManager.getCurrentState().misScroll(roomView,e);
    }

    public void misZadrzan(RoomView roomView, Point point){
        stateManager.getCurrentState().misZadrzan(roomView,point);
    }

    public void misPovucen(RoomView roomView, Point point){
        stateManager.getCurrentState().misPovucen(roomView,point);
    }

    public void misPusten(RoomView roomView, Point point){
        stateManager.getCurrentState().misPusten(roomView,point);
    }


    public State trenutnoStanje(){
        return stateManager.getCurrentState();
    }


    public void startAddBojlerState(){
        stateManager.setAddBojlerState();
    }

    public void startAddKadaState(){
        stateManager.setAddKadaState();
    }

    public void startAddKrevetState(){
        stateManager.setAddKrevetState();
    }

    public void startAddLavaboState(){
        stateManager.setAddLavaboState();
    }

    public void startAddOrmarState(){
        stateManager.setAddOrmarState();
    }

    public void startAddStoState(){
        stateManager.setAddStoState();
    }

    public void startAddVesMasinaState(){
        stateManager.setAddVesMasinaState();
    }

    public void startAddVrataState(){
        stateManager.setAddVrataState();
    }

    public void startAddWCSoljaState(){
        stateManager.setAddWCSoljaState();
    }

    public void startDeleteState() {
        stateManager.setDeleteState();
    }

    public void startEditRoomState() {
        stateManager.setEditRoomState();
    }

    public void startEditState() {
        stateManager.setEditState();
    }

    public void startMoveState() {
        stateManager.setMoveState();
    }

    public void startResizeState() {
        stateManager.setResizeState();
    }

    public void startSelectState() {
        stateManager.setSelectState();
    }

    public void startZoomState() {
        stateManager.setZoomState();
    }

    public void startRotateLeftState(){
        stateManager.setRotateLeftState();
    }
    public void startRotateRightState(){
        stateManager.setRotateRightState();
    }

    public void startCopyPasteState() {
        stateManager.setCopyPasteState();
    }

}
