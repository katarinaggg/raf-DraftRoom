package raf.draft.dsw.controller.actions;

import raf.draft.dsw.core.ApplicationFramework;
import raf.draft.dsw.gui.swing.MainFrame;
import raf.draft.dsw.gui.swing.ProjectView;
import raf.draft.dsw.gui.swing.RoomView;
import raf.draft.dsw.gui.swing.jtree.DraftTreeImplementation;
import raf.draft.dsw.gui.swing.jtree.model.DraftTreeItem;
import raf.draft.dsw.model.nodes.DraftNode;
import raf.draft.dsw.model.notification.Message;
import raf.draft.dsw.model.notification.MessageType;
import raf.draft.dsw.model.notification.Notification;
import raf.draft.dsw.model.structures.Project;
import raf.draft.dsw.model.structures.Room;
import raf.draft.dsw.model.structures.namestaj.*;
import raf.draft.dsw.painter.*;

import javax.swing.*;
import javax.swing.plaf.synth.SynthOptionPaneUI;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.net.URL;
import java.time.LocalDateTime;

public class OpenRoomAction extends AbstractRoomAction {

    public OpenRoomAction() {
        putValue(SMALL_ICON, loadIcon("/images/openRoom.png"));
        putValue(NAME, "Open Room");
        putValue(SHORT_DESCRIPTION, "Open Room");
    }

    @Override
    public Icon loadIcon(String path) {
        Icon icon = null;
        URL ImageURL = getClass().getResource(path);
        if(ImageURL != null)
        {
            Image img = new ImageIcon(ImageURL).getImage();
            Image newImg = img.getScaledInstance(30, 30, Image.SCALE_DEFAULT);
            icon = new ImageIcon(newImg);
        }
        else
        {
            System.err.println("File " + path + " not found");
        }
        return icon;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Room loadedRoom = ApplicationFramework.getInstance().getSerializer().ucitajSablonSobe();
        System.out.println(loadedRoom);

        if (loadedRoom != null) {
            Room clickedRoom = MainFrame.getInstance().getClickedRoom();

            if (clickedRoom != null) {
                // prvo dodajemo svu decu
                for(DraftNode namestaj:loadedRoom.getChildren()){
                    DraftTreeItem soba = MainFrame.getInstance().getDraftTree().findTreeItem(clickedRoom);
                    MainFrame.getInstance().getDraftTree().addRoomChild(soba,(RoomElement)namestaj);
                    namestaj.setParent(clickedRoom);
                }

                System.out.println(clickedRoom.getChildren());

                // ubacujemo sve vrednosti iz sablona u nasu sobu
                clickedRoom.setSirina(loadedRoom.getSirina());
                clickedRoom.setDuzina(loadedRoom.getDuzina());
                clickedRoom.setLineColor(loadedRoom.getLineColor());
                clickedRoom.setLineThickness(loadedRoom.getLineThickness());

                inicijalizacijaPaintera(clickedRoom);

//                for (DraftNode element : clickedRoom.getChildren()) {
//                    RoomElement re = (RoomElement)element;
//                    System.out.println("Element: " + re + ", Lokacija: " + re.getLokacija() + ", Veličina: " + re.getSize());
//                }

                clickedRoom.notifySubscribers(new Notification());
                Message message = ApplicationFramework.getInstance().getMessageGenerator().generateMessage("Uspesno ucitana soba", MessageType.INFORMATION, LocalDateTime.now());
                ApplicationFramework.getInstance().getMessageGenerator().notifySubscribers(new Notification(message));
            } else {
                Message message = ApplicationFramework.getInstance().getMessageGenerator().generateMessage("Morate da izaberete sobu u koju zelite da ucitate", MessageType.WARNING, LocalDateTime.now());
                ApplicationFramework.getInstance().getMessageGenerator().notifySubscribers(new Notification(message));
            }
        }
    }



    private void inicijalizacijaPaintera(Room room){

        Project project = room.getProjectParent();
        ProjectView projectView;

        if(MainFrame.getInstance().getProjectViewMap().containsKey(project)){
            projectView = MainFrame.getInstance().getProjectViewMap().get(project);
            System.out.println("POSTOJI PROJECTVIEW");
            //projectView.getRoomViewMap().put(room.getIme(), roomView);
        }
        else {
            projectView = new ProjectView(project);
            MainFrame.getInstance().getProjectViewMap().put(project, projectView);
            //projectView.getRoomViewMap().put(room.getIme(), roomView);
            project.addSubscriber(projectView);
            System.out.println("NE POSTOJI PROJECTVIEW");
        }

        RoomView roomView;
        if(!projectView.getRoomViewMap().containsKey(room.getIme())){
            roomView = new RoomView(room);
            projectView.getRoomViewMap().put(room.getIme(), roomView);
            room.addSubscriber(roomView);
            System.out.println("NE POSTOJI ROOMVIEW");
        }
        else {
            roomView = projectView.getRoomViewMap().get(room.getIme());
            System.out.println("POSTOJI ROOMVIEW");
        }

        System.out.println("DUZINA: "+room.getDuzina());
        System.out.println("SIRINA: "+room.getSirina());

        System.out.println(roomView.getPainters());
        roomView.getPainters().clear();
        System.out.println(roomView.getPainters());

        // painteri
        RoomPainter roomPainter = new RoomPainter(room,roomView);
        roomView.addPainter(roomPainter);
        // da rade i ostali dugmici jer sad imamo sobu
        roomView.setNacrtanaSoba(true);

        for (DraftNode element : room.getChildren()) {
            if (element instanceof RoomElement) {
                RoomElement roomElement = (RoomElement) element;
                ElementPainter painter = createPainter(roomElement, roomView);
                System.out.println("Dodat painter: "+painter);
                roomView.addPainter(painter);
            }
        }
        System.out.println(roomView.getPainters());
        roomView.revalidate();
        roomView.repaint();
    }

    private ElementPainter createPainter(RoomElement element, RoomView roomView) {
        ElementPainter elementPainter = null;
        if (element instanceof Bojler){
            elementPainter = new BojlerPainter((Bojler) element,roomView);
        }
        else if (element instanceof Kada){
            elementPainter = new KadaPainter((Kada) element,roomView);
        }
        else if (element instanceof Krevet) {
            elementPainter = new KrevetPainter((Krevet) element,roomView);
        }
        else if (element instanceof Lavabo) {
            elementPainter = new LavaboPainter((Lavabo) element,roomView);
        }
        else if (element instanceof Ormar){
            elementPainter = new OrmarPainter((Ormar) element,roomView);
        }
        else if (element instanceof Sto) {
            elementPainter = new StoPainter((Sto) element,roomView);
        }
        else if (element instanceof VesMasina) {
            elementPainter = new VesMasinaPainter((VesMasina) element, roomView);
        }
        else if (element instanceof WCSolja) {
            elementPainter = new WCSoljaPainter((WCSolja) element, roomView);
        }
        else if(element instanceof Vrata){
            elementPainter = new VrataPainter((Vrata) element, roomView);
        }
        return elementPainter;
    }
}
