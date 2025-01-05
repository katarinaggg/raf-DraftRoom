package raf.draft.dsw.controller.actions;

import raf.draft.dsw.core.ApplicationFramework;
import raf.draft.dsw.gui.swing.MainFrame;
import raf.draft.dsw.gui.swing.OrganizeMyRoom;
import raf.draft.dsw.model.notification.Message;
import raf.draft.dsw.model.notification.MessageType;
import raf.draft.dsw.model.notification.Notification;
import raf.draft.dsw.model.structures.Room;
import raf.draft.dsw.model.structures.namestaj.*;

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
            message = ApplicationFramework.getInstance().getMessageGenerator().generateMessage(
                    "Niste selektovali sobu", MessageType.WARNING, LocalDateTime.now());
            ApplicationFramework.getInstance().getMessageGenerator().notifySubscribers(new Notification(message));
            return;
        }

        // Otvaranje dijaloga za unos elemenata
        OrganizeMyRoom dialog = new OrganizeMyRoom(MainFrame.getInstance());
        if (dialog.isSubmitted()) {
            Map<String, Dimension> elementsMap = dialog.getRoomElementsMap();


            if (elementsMap.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Nema elemenata za dodavanje!", "Greška", JOptionPane.ERROR_MESSAGE);
                return;
            }

            System.out.println("Mapa elemenata iz dijaloga:");
            elementsMap.forEach((key, value) -> {
                System.out.println("Element: " + key + ", Dimenzije: " + value);
            });

            // Kreiranje liste elemenata na osnovu mape
            List<RoomElement> elements = new ArrayList<>();
            for (Map.Entry<String, Dimension> entry : elementsMap.entrySet()) {
                String name = entry.getKey();
                Dimension size = entry.getValue();
                RoomElement element = createRoomElement(name, (int) size.getWidth(), (int) size.getHeight(), selectedRoom);
                if (element != null) {
                    elements.add(element);
                }
            }


            for (Map.Entry<String, Dimension> entry : elementsMap.entrySet()) {
                System.out.println("Element: " + entry.getKey() + ", Dimenzije: " + entry.getValue());
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
            case "Ves masina":
                return new VesMasina(room, new Point(0, 0), new Dimension(width, height));
            case "Vrata":
                return new Vrata(room, new Point(0, 0), new Dimension(width, height));
            case "WC solja":
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
            Message message = ApplicationFramework.getInstance().getMessageGenerator().generateMessage(
                    "Nema dovoljno mesta u sobi za sve elemente!", MessageType.ERROR, LocalDateTime.now());
            ApplicationFramework.getInstance().getMessageGenerator().notifySubscribers(new Notification(message));
            return;
        }

        for (int i = 0; i < elements.size(); i++) {
            Point position = spiralOrder.get(i);
            RoomElement element = elements.get(i);
            element.setLokacija(new Point(position.x * (int) element.getSize().getWidth(), position.y * (int) element.getSize().getHeight()));
            room.addChild(element);
        }

        room.notifySubscribers(new Notification(room));
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
        n = Math.min(n, 1000); // Na primer, maksimalno 1000 redova
        m = Math.min(m, 1000); // Na primer, maksimalno 1000 kolona

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

}