package raf.draft.dsw.controller.actions;

import raf.draft.dsw.core.ApplicationFramework;
import raf.draft.dsw.gui.swing.MainFrame;
import raf.draft.dsw.gui.swing.OrganizeMyRoom;
import raf.draft.dsw.gui.swing.ProjectView;
import raf.draft.dsw.gui.swing.RoomView;
import raf.draft.dsw.gui.swing.jtree.model.DraftTreeItem;
import raf.draft.dsw.model.notification.Message;
import raf.draft.dsw.model.notification.MessageType;
import raf.draft.dsw.model.notification.Notification;
import raf.draft.dsw.model.structures.Project;
import raf.draft.dsw.model.structures.Room;
import raf.draft.dsw.model.structures.namestaj.*;
import raf.draft.dsw.painter.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.*;
import java.util.List;

public class OrganizeRoomAction extends AbstractRoomAction {

    public OrganizeRoomAction() {
        putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_O, ActionEvent.CTRL_MASK));
        putValue(SMALL_ICON, loadIcon("/images/organizeRoom.png"));
        putValue(NAME, "Organize room");
        putValue(SHORT_DESCRIPTION, "Organize room");
    }

    @Override
    public Icon loadIcon(String path) {
        Icon icon = null;
        URL ImageURL = getClass().getResource(path);
        if (ImageURL != null) {
            Image img = new ImageIcon(ImageURL).getImage();
            Image newImg = img.getScaledInstance(30, 30, Image.SCALE_DEFAULT);
            icon = new ImageIcon(newImg);
        } else {
            System.err.println("File " + path + " not found");
        }
        return icon;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Room selectedRoom = MainFrame.getInstance().getClickedRoom();
        Message message;

        if (selectedRoom == null) {
            message = ApplicationFramework.getInstance().getMessageGenerator().generateMessage("Morate da izaberete sobu u koju zelite da organizujete", MessageType.WARNING, LocalDateTime.now());
            ApplicationFramework.getInstance().getMessageGenerator().notifySubscribers(new Notification(message));
            return;
        }
        if(selectedRoom.getSirina()==0 || selectedRoom.getDuzina()==0){
            message = ApplicationFramework.getInstance().getMessageGenerator().generateMessage("Morate da nacrtate sobu i unesete dimenzije kako biste mogli da je organizujete", MessageType.INFORMATION, LocalDateTime.now());
            ApplicationFramework.getInstance().getMessageGenerator().notifySubscribers(new Notification(message));
            return;
        }

        // Otvaranje dijaloga za unos elemenata
        OrganizeMyRoom dialog = new OrganizeMyRoom(MainFrame.getInstance());
        if (dialog.isSubmitted()) {
            Map<String, Dimension> elementsMap = dialog.getRoomElementsMap();


            if (elementsMap.isEmpty()) {
                message = ApplicationFramework.getInstance().getMessageGenerator().generateMessage("Nema elemenata za dodavanje!", MessageType.ERROR, LocalDateTime.now());
                ApplicationFramework.getInstance().getMessageGenerator().notifySubscribers(new Notification(message));
                return;
            }

            System.out.println("Mapa elemenata iz dijaloga:");
            elementsMap.forEach((key, value) -> {
                System.out.println("Element: " + key + ", Dimenzije: " + value);
            });

            // Kreiranje liste elemenata na osnovu mape
            List<RoomElement> elements = new ArrayList<>();
            for (Map.Entry<String, Dimension> entry : elementsMap.entrySet()) {
                String name = entry.getKey().split(" ")[0];
                Dimension size = entry.getValue();
                RoomElement element = createRoomElement(name, (int) size.getWidth(), (int) size.getHeight(), selectedRoom);
                if (element != null) {
                    elements.add(element);
                }
            }
            System.out.println("Elementi za dodavanje u sobu");
            for(RoomElement element : elements) {
                System.out.println(element);
            }

            // dodajemo u model i Jtree
            for(RoomElement element : elements) {
                DraftTreeItem soba = MainFrame.getInstance().getDraftTree().findTreeItem(selectedRoom);
                MainFrame.getInstance().getDraftTree().addRoomChild(soba,element);
                element.setParent(selectedRoom);
            }
            // Postavljanje elemenata u sobu
            placeElementsInRoom(selectedRoom, elements);
            //displayRoomLayout(selectedRoom, elements, matrixSize[0], matrixSize[1]);

        }
    }

    private RoomElement createRoomElement(String name, int width, int height, Room room) {
        switch (name) {
            case "Bojler":
                return new Bojler(room, new Point(0, 0), new Dimension(width, height));
            case "Kada":
                return new Kada(room, new Point(0, 0), new Dimension(width, height));
            case "Krevet":
                return new Krevet(room, new Point(0, 0), new Dimension(width, height));
            case "Lavabo":
                return new Lavabo(room, new Point(0, 0), new Dimension(width, height));
            case "Ormar":
                return new Ormar(room, new Point(0, 0), new Dimension(width, height));
            case "Sto":
                return new Sto(room, new Point(0, 0), new Dimension(width, height));
            case "VesMasina":
                return new VesMasina(room, new Point(0, 0), new Dimension(width, height));
            case "Vrata":
                return new Vrata(room, new Point(0, 0), new Dimension(width, height));
            case "WCSolja":
                return new WCSolja(room, new Point(0, 0), new Dimension(width, height));
            default:
                return null;
        }
    }

    private void placeElementsInRoom(Room room, List<RoomElement> elements) {
        int[] matrixSize = calculateMatrixSize(room, elements);
        int n = matrixSize[0];
        int m = matrixSize[1];

        List<Point> spiralOrder = generateSpiralOrder(n, m);

        if (elements.size() > spiralOrder.size()) {
            Message message = ApplicationFramework.getInstance().getMessageGenerator().generateMessage("Nema dovoljno mesta u sobi za sve elemente!", MessageType.ERROR, LocalDateTime.now());
            ApplicationFramework.getInstance().getMessageGenerator().notifySubscribers(new Notification(message));
            return;
        }

        // izvlacimo RoomView na koji treba da iscrtamo (vec postoji)
        Project project = room.getProjectParent();
        ProjectView projectView = MainFrame.getInstance().getProjectViewMap().get(project);
        RoomView roomView = projectView.getRoomViewMap().get(room.getIme());

        // za svaki element treba da napravimo odgovarajuci painter
        // koji cemo dodati na RoomView
        // i da mu setujemo lokaciju u matrici

        for (int i = 0; i < elements.size(); i++) {
            Point position = spiralOrder.get(i);
            RoomElement element = elements.get(i);
            element.setLokacija(new Point(position.x * (int) element.getSize().getWidth(), position.y * (int) element.getSize().getHeight()));
            ElementPainter elementPainter = createPainter(element,roomView);
            roomView.addPainter(elementPainter);
        }

        room.notifySubscribers(new Notification());
    }

//    private void displayRoomLayout(Room room, List<RoomElement> elements, int rows, int cols) {
//        JFrame frame = new JFrame("Prikaz sobe");
//        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
//        frame.setSize(700, 700);
//        frame.setLayout(new GridLayout(rows, cols));
//
//        for (int i = 0; i < rows; i++) {
//            for (int j = 0; j < cols; j++) {
//                JPanel cell = new JPanel();
//                cell.setBorder(BorderFactory.createLineBorder(Color.BLACK));
//                frame.add(cell);
//
//                for (RoomElement element : elements) {
//                    Point location = element.getLokacija();
//                    if (location.x / element.getSize().width == j && location.y / element.getSize().height == i) {
//                        JLabel label = new JLabel(element.getIme());
//                        label.setHorizontalAlignment(SwingConstants.CENTER);
//                        cell.add(label);
//                        break;
//                    }
//                }
//            }
//        }
//
//        frame.setVisible(true);
//    }



    private int[] calculateMatrixSize(Room room, List<RoomElement> elements) {
        int maxElementWidth = 1; // Podrazumevana vrednost za izbegavanje deljenja sa nulom
        int maxElementHeight = 1;

        for (RoomElement element : elements) {
            maxElementWidth = Math.max(maxElementWidth, (int) element.getSize().getWidth());
            maxElementHeight = Math.max(maxElementHeight, (int) element.getSize().getHeight());
        }

        int n = (int) Math.ceil((double) room.getDuzina() / maxElementHeight);
        int m = (int) Math.ceil((double) room.getSirina() / maxElementWidth);

        // Ograničite maksimalne dimenzije matrice kako biste izbegli prevelike vrednosti
        n = Math.min(n, 100); // Na primer, maksimalno 1000 redova
        m = Math.min(m, 100); // Na primer, maksimalno 1000 kolona

        return new int[]{n, m};
    }


    private List<Point> generateSpiralOrder(int rows, int cols) {
        List<Point> order = new ArrayList<>(rows * cols); // Unapred definišite veličinu liste

        int top = 0, bottom = rows - 1, left = 0, right = cols - 1;

        while (top <= bottom && left <= right) {
            for (int i = left; i <= right; i++) {
                order.add(new Point(top, i));
            }
            top++;

            for (int i = top; i <= bottom; i++) {
                order.add(new Point(i, right));
            }
            right--;

            if (top <= bottom) {
                for (int i = right; i >= left; i--) {
                    order.add(new Point(bottom, i));
                }
                bottom--;
            }

            if (left <= right) {
                for (int i = bottom; i >= top; i--) {
                    order.add(new Point(i, left));
                }
                left++;
            }

            // Provera za sprečavanje beskonačne petlje
            if (order.size() > rows * cols) {
                throw new IllegalStateException("Greška: Previše elemenata u spirali!");
            }
        }

        return order;
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