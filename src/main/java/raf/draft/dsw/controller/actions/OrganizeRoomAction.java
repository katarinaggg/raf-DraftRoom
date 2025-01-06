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
import java.awt.geom.GeneralPath;
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


            // Postavljanje elemenata u sobu

            // ako su stali dodajemo ih u Jtree
            if(placeElementsInRoom(selectedRoom, elements)){
                // dodajemo u model i Jtree
                for(RoomElement element : elements) {
                    DraftTreeItem soba = MainFrame.getInstance().getDraftTree().findTreeItem(selectedRoom);
                    MainFrame.getInstance().getDraftTree().addRoomChild(soba,element);
                    element.setParent(selectedRoom);
                }
            }
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

    private boolean placeElementsInRoom(Room room, List<RoomElement> elements) {
        // izvlacimo RoomView na koji treba da iscrtamo (vec postoji)
        Project project = room.getProjectParent();
        ProjectView projectView = MainFrame.getInstance().getProjectViewMap().get(project);
        RoomView roomView = projectView.getRoomViewMap().get(room.getIme());

        // Dobijanje granica sobe iz RoomPainter-a
        RoomPainter roomPainter = roomView.getRoomPainter(room);
        GeneralPath roomShape = roomPainter.getShape();

        Rectangle bounds = roomShape.getBounds();
        int roomXStart = bounds.x;
        int roomYStart = bounds.y;
        int roomWidth = bounds.width;
        int roomHeight = bounds.height;

        // Prilagodjavanje matrice prema dimenzijama sobe
        int[] matrixSize = calculateMatrixSize(roomWidth, roomHeight, elements);
        int n = matrixSize[0]; // Broj redova
        int m = matrixSize[1]; // Broj kolona

        List<Point> spiralOrder = generateSpiralOrder(n, m); // Generisanje spiralnog redosleda

        if (elements.size() > spiralOrder.size()) {
            Message message = ApplicationFramework.getInstance().getMessageGenerator().generateMessage("Nema dovoljno mesta u sobi za sve elemente!", MessageType.ERROR, LocalDateTime.now());
            ApplicationFramework.getInstance().getMessageGenerator().notifySubscribers(new Notification(message));
            return false;
        }

        // Randomizacija redosleda elemenata
        //Collections.shuffle(elements);

        // Dimenzije ćelije matrice
        int cellWidth = roomWidth / m;
        int cellHeight = roomHeight / n;

        // za svaki element treba da napravimo odgovarajuci painter
        // koji cemo dodati na RoomView
        // i da mu setujemo lokaciju u matrici

        Set<Point> occupiedCells = new HashSet<>(); // Provera zauzetosti ćelija

        for (int i = 0; i < elements.size(); i++) {
            RoomElement element = elements.get(i);

            // Pronađi sledeću slobodnu poziciju
            Point position = null;
            for (Point p : spiralOrder) {
                if (!occupiedCells.contains(p)) {
                    position = p;
                    occupiedCells.add(p);
                    break;
                }
            }

            // Izračunavanje apsolutne pozicije u sobi
            int x = roomXStart + position.y * cellWidth;
            int y = roomYStart + position.x * cellHeight;

            // Proporcionalno skaliranje elemenata prema veličini ćelije
            int elementWidth = Math.min(cellWidth, (int) element.getSize().getWidth());
            int elementHeight = Math.min(cellHeight, (int) element.getSize().getHeight());

            // Prilagođavanje elemenata unutar ćelije
            if (position.x == 0 && position.y == 0) {
                // Gornji levi ugao
                element.setLokacija(new Point(x, y));
            } else if (position.x == 0 && position.y == m - 1) {
                // Gornji desni ugao
                element.setLokacija(new Point(x + cellWidth - elementWidth, y));
            } else if (position.x == n - 1 && position.y == 0) {
                // Donji levi ugao
                element.setLokacija(new Point(x, y + cellHeight - elementHeight));
            } else if (position.x == n - 1 && position.y == m - 1) {
                // Donji desni ugao
                element.setLokacija(new Point(x + cellWidth - elementWidth, y + cellHeight - elementHeight));
            } else if (position.x == 0) {
                // Prvi red - lepljenje uz gornju ivicu
                element.setLokacija(new Point(x, y));
            } else if (position.x == n - 1) {
                // Poslednji red - lepljenje uz donju ivicu
                element.setLokacija(new Point(x, y + cellHeight - elementHeight));
            } else if (position.y == 0) {
                // Prva kolona - lepljenje uz levu ivicu
                element.setLokacija(new Point(x, y));
            } else if (position.y == m - 1) {
                // Poslednja kolona - lepljenje uz desnu ivicu
                element.setLokacija(new Point(x + cellWidth - elementWidth, y));
            } else {
                // Srednja polja - centriranje unutar ćelije
                int offsetX = (cellWidth - elementWidth) / 2;
                int offsetY = (cellHeight - elementHeight) / 2;
                element.setLokacija(new Point(x + offsetX, y + offsetY));
            }

            // Kreiranje painter-a za element
            ElementPainter elementPainter = createPainter(element, roomView);
            roomView.addPainter(elementPainter);
        }

        room.notifySubscribers(new Notification());
        return true;
    }

    private int[] calculateMatrixSize(int roomWidth, int roomHeight, List<RoomElement> elements) {
        int maxElementWidth = 1;
        int maxElementHeight = 1;

        for (RoomElement element : elements) {
            maxElementWidth = Math.max(maxElementWidth, (int) element.getSize().getWidth());
            maxElementHeight = Math.max(maxElementHeight, (int) element.getSize().getHeight());
        }

        int n = Math.max(1, (int) Math.ceil((double) roomHeight / maxElementHeight));
        int m = Math.max(1, (int) Math.ceil((double) roomWidth / maxElementWidth));

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