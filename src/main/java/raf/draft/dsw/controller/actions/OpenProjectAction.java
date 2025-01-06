package raf.draft.dsw.controller.actions;

import com.sun.tools.javac.Main;
import raf.draft.dsw.core.ApplicationFramework;
import raf.draft.dsw.gui.swing.MainFrame;
import raf.draft.dsw.gui.swing.ProjectView;
import raf.draft.dsw.gui.swing.RoomView;
import raf.draft.dsw.gui.swing.jtree.model.DraftTreeItem;
import raf.draft.dsw.model.nodes.DraftNode;
import raf.draft.dsw.model.structures.Building;
import raf.draft.dsw.model.structures.Project;
import raf.draft.dsw.model.structures.ProjectExplorer;
import raf.draft.dsw.model.structures.Room;
import raf.draft.dsw.model.structures.namestaj.*;
import raf.draft.dsw.painter.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.net.URL;
import java.util.Map;

public class OpenProjectAction extends AbstractRoomAction {

    public OpenProjectAction() {
        //putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_S, ActionEvent.SHIFT_MASK));
        putValue(SMALL_ICON, loadIcon("/images/open.png"));
        putValue(NAME, "Open Project");
        putValue(SHORT_DESCRIPTION, "Open Project");
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
        Project project = ApplicationFramework.getInstance().getSerializer().openProject();
        if(project==null){
            return;
        }
        dodajUStablo(project);
        inicijalizacijaPaintera(project);
    }

    private void dodajUStablo(Project project) {
        MainFrame.getInstance().getDraftTree().addDeserializedProject(project);
    }

    private void inicijalizacijaPaintera(Project project) {
        // pravimo novi ProjectView
        ProjectView projectView = new ProjectView(project);
        MainFrame.getInstance().getProjectViewMap().put(project, projectView);
        Map<String, RoomView> roomViewMap = projectView.getRoomViewMap();
        project.addSubscriber(projectView);

        //svoj deci projekta dodajemo subscribera projectView
        for (DraftNode child : project.getChildren()) {
            if (child instanceof Building) {
                // ako je building subscribujemo ProjectView i na svu njegovu decu
                for (DraftNode child2 : ((Building) child).getChildren()) {
                    child2.addSubscriber(projectView);
                }
            }
            // i na zasebne sobe (ili building)
            child.addSubscriber(projectView);
        }

        // prolazimo kroz sve sobe projekta i pravimo im RoomView-ove
        for (Room room : project.getRooms()) {

            // moramo da napravimo nove RoomView-ove
            // i da ih subscribujemo na sobu
            RoomView roomView = new RoomView(room);
            roomViewMap.put(room.getIme(), roomView);
            room.addSubscriber(roomView);

            // dodajemo painter za sobu
            RoomPainter roomPainter = new RoomPainter(room,roomView);
            roomView.addPainter(roomPainter);

            // dodajemo paintere za svaki element sobe
            for (DraftNode element : room.getChildren()) {
                if (element instanceof RoomElement) {
                    RoomElement roomElement = (RoomElement) element;
                    ElementPainter painter = createPainter(roomElement, roomView);
                    roomView.addPainter(painter);
                    //System.out.println("Dodat painter: "+painter);
                }
            }
        }
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
        return elementPainter;
    }
}
