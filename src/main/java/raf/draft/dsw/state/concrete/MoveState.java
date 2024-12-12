package raf.draft.dsw.state.concrete;

import lombok.NoArgsConstructor;
import raf.draft.dsw.core.ApplicationFramework;
import raf.draft.dsw.gui.swing.RoomView;
import raf.draft.dsw.model.notification.Message;
import raf.draft.dsw.model.notification.MessageType;
import raf.draft.dsw.model.notification.Notification;
import raf.draft.dsw.painter.ElementPainter;
import raf.draft.dsw.painter.BojlerPainter;
import raf.draft.dsw.model.structures.namestaj.RoomElement;
import raf.draft.dsw.painter.VrataPainter;
import raf.draft.dsw.painter.WCSoljaPainter;
import raf.draft.dsw.state.State;

import java.awt.*;
import java.awt.event.MouseWheelEvent;
import java.time.LocalDateTime;
import java.awt.geom.AffineTransform;
import java.util.HashMap;
import java.util.Map;

@NoArgsConstructor
public class MoveState implements State {

    private Point initialPoint; // Početna tačka klika miša

    private static int tolerance=10;

    private final HashMap<RoomElement, Point> initialLocations = new HashMap<>(); // Početne lokacije svih selektovanih elemenata

    @Override
    public void misZadrzan(RoomView roomView, Point point) {

        if(!roomView.isNacrtanaSoba()){
            Message message = ApplicationFramework.getInstance().getMessageGenerator().generateMessage("Morate prvo da nacrtate sobu!", MessageType.WARNING, LocalDateTime.now());
            ApplicationFramework.getInstance().getMessageGenerator().notifySubscribers(new Notification(message));
            return;
        }

        if (!roomView.getSelectionList().isEmpty()) {
            // Čuvamo početnu tačku miša
            initialPoint = point;

            // Resetovanje pravougaonika za selekciju
            roomView.setSelectionRectangleLT(null);
            roomView.setSelectionRectangleRB(null);


            // Čuvamo početne lokacije svih selektovanih elemenata
            initialLocations.clear();
            for (ElementPainter painter : roomView.getSelectionList()) {
                RoomElement element = painter.getRoomElement();
                if (element != null) {
                    initialLocations.put(element, new Point(element.getLokacija()));
                }
            }
        }
    }

    @Override
    public void misPovucen(RoomView roomView, Point point) {
        if (initialPoint != null && !initialLocations.isEmpty()) {
            // Računamo pomeranje miša
            int deltaX = point.x - initialPoint.x;
            int deltaY = point.y - initialPoint.y;

            // Dobijamo granice sobe
            Rectangle roomBounds = roomView.getRoomBounds();

            // Mapa za nove pozicije
            HashMap<RoomElement, Point> newLocations = new HashMap<>();

            // Iteriramo kroz selektovane elemente
            for (ElementPainter painter : roomView.getSelectionList()) {
                RoomElement element = painter.getRoomElement();
                Point initialLocation = initialLocations.get(element);

                if (initialLocation == null) continue;

                // Nova lokacija elementa
                int newX = initialLocation.x + deltaX;
                int newY = initialLocation.y + deltaY;
                Point newLocation = new Point(newX, newY);

                // Proveravamo oblik elemenata
                boolean isEllipse = painter instanceof VrataPainter || painter instanceof WCSoljaPainter || painter instanceof BojlerPainter;

                // Dimenzije elementa
                Dimension size = element.getScaledDimensions(roomView.getScalingFactor());
                int semiMajorAxis = size.width / 2;
                int semiMinorAxis = size.height / 2;
                int width = size.width;
                int height = size.height;

                // Izbor odgovarajuće metode "snap-to-edge"
                if (isEllipse) {
                    newLocation = snapToEdgeEllipseMath(roomBounds, newLocation, semiMajorAxis, semiMinorAxis, tolerance);
                }
                else {
                    newLocation = snapToEdgeRectangle(roomBounds, newLocation, width, height, tolerance);
                }

                // Proveravamo da li je nova lokacija unutar granica sobe
                if (!isWithinRoomBounds(roomView, element, newLocation)) {
                    System.out.println("Element " + element.getIme() + " izlazi iz granica sobe!");
                    returnToInitialLocations();
                    roomView.repaint();
                    return;
                }

                // Dodajemo novu lokaciju u mapu
                newLocations.put(element, newLocation);
            }

            // Proveravamo presek sa drugim elementima
            for (Map.Entry<RoomElement, Point> entry : newLocations.entrySet()) {
                RoomElement element = entry.getKey();
                Point newLocation = entry.getValue();

                if (hasIntersection(roomView, element, newLocation)) {
                    System.out.println("Preklapanje detektovano! Vraćanje elemenata na početne pozicije.");
                    returnToInitialLocations();
                    roomView.repaint();
                    return;
                }
            }

            // Ažuriramo lokacije elemenata
            for (Map.Entry<RoomElement, Point> entry : newLocations.entrySet()) {
                entry.getKey().setLokacija(entry.getValue());
            }

            // Osvežavanje prikaza
            roomView.repaint();
        }
    }


    @Override
    public void misPusten(RoomView roomView, Point point) {
        initialPoint = null; // Resetujemo početnu tačku
        initialLocations.clear(); // Resetujemo početne lokacije
    }

    private Point snapToEdgeRectangle(Rectangle roomBounds, Point newLocation, int width, int height, int tolerance) {
        Point snappedLocation = new Point(newLocation);

        // Lepljenje uz levu ivicu sobe
        if (Math.abs(snappedLocation.x - roomBounds.x) < tolerance) {
            snappedLocation.x = roomBounds.x;
        }

        // Lepljenje uz gornju ivicu sobe
        if (Math.abs(snappedLocation.y - roomBounds.y) < tolerance) {
            snappedLocation.y = roomBounds.y;
        }

        // Lepljenje uz desnu ivicu sobe
        if (Math.abs((snappedLocation.x + width) - (roomBounds.x + roomBounds.width)) < tolerance) {
            snappedLocation.x = roomBounds.x + roomBounds.width - width;
        }

        // Lepljenje uz donju ivicu sobe
        if (Math.abs((snappedLocation.y + height) - (roomBounds.y + roomBounds.height)) < tolerance) {
            snappedLocation.y = roomBounds.y + roomBounds.height - height;
        }

        return snappedLocation;
    }

    private Point snapToEdgeEllipseMath(Rectangle roomBounds, Point newLocation, int semiMajorAxis, int semiMinorAxis, int tolerance) {
        Point snappedLocation = new Point(newLocation);

        // Centar elipse
        int centerX = newLocation.x + semiMajorAxis;
        int centerY = newLocation.y + semiMinorAxis;

        // 1. Leva ivica sobe
        int leftEdgeX = roomBounds.x;
        double leftEdgeY = centerY + semiMinorAxis * Math.sqrt(1 - Math.pow((leftEdgeX - centerX) / (double) semiMajorAxis, 2));
        if (Math.abs(leftEdgeX - (centerX - semiMajorAxis)) < tolerance) {
            snappedLocation.x = leftEdgeX - semiMajorAxis;
        }

        // 2. Gornja ivica sobe
        int topEdgeY = roomBounds.y;
        double topEdgeX = centerX + semiMajorAxis * Math.sqrt(1 - Math.pow((topEdgeY - centerY) / (double) semiMinorAxis, 2));
        if (Math.abs(topEdgeY - (centerY - semiMinorAxis)) < tolerance) {
            snappedLocation.y = topEdgeY - semiMinorAxis;
        }

        // 3. Desna ivica sobe
        int rightEdgeX = roomBounds.x + roomBounds.width;
        double rightEdgeY = centerY - semiMinorAxis * Math.sqrt(1 - Math.pow((rightEdgeX - centerX) / (double) semiMajorAxis, 2));
        if (Math.abs(rightEdgeX - (centerX + semiMajorAxis)) < tolerance) {
            snappedLocation.x = rightEdgeX - semiMajorAxis * 2;
        }

        // 4. Donja ivica sobe
        int bottomEdgeY = roomBounds.y + roomBounds.height;
        double bottomEdgeX = centerX - semiMajorAxis * Math.sqrt(1 - Math.pow((bottomEdgeY - centerY) / (double) semiMinorAxis, 2));
        if (Math.abs(bottomEdgeY - (centerY + semiMinorAxis)) < tolerance) {
            snappedLocation.y = bottomEdgeY - semiMinorAxis * 2;
        }

        return snappedLocation;
    }

    private boolean hasIntersection(RoomView roomView, RoomElement movingElement, Point newLocation) {
        // Dohvatamo stvarni oblik pomeranog elementa
        ElementPainter movingPainter = roomView.getPainters().stream()
                .filter(p -> p.getRoomElement() == movingElement)
                .findFirst()
                .orElse(null);

        if (movingPainter == null) {
            System.out.println("Painter za pomerani element nije pronađen.");
            return false;
        }

        Shape movingShape = movingPainter.getShape();
        if (movingShape == null) {
            System.out.println("Oblik za pomerani element nije pronađen.");
            return false;
        }

        // Transliramo oblik na novu lokaciju
        AffineTransform transform = AffineTransform.getTranslateInstance(
                newLocation.x - movingElement.getLokacija().x,
                newLocation.y - movingElement.getLokacija().y
        );
        Shape translatedShape = transform.createTransformedShape(movingShape);

        // Proveravamo presek sa svim drugim elementima
        for (ElementPainter painter : roomView.getPainters()) {
            RoomElement otherElement = painter.getRoomElement();

            // Preskačemo element koji trenutno pomeramo ili ako je drugi element null
            if (otherElement == null || otherElement == movingElement) continue;

            Shape otherShape = painter.getShape();
            if (otherShape == null) {
                System.out.println("Oblik za element " + otherElement + " nije pronađen. Preskačem.");
                continue;
            }

            if (translatedShape.intersects(otherShape.getBounds2D())) {
                System.out.println("Presek detektovan između " + movingElement.getIme() + " i " + otherElement.getIme());
                return true;
            }
        }
        return false; // Nema preklapanja
    }


    private boolean isWithinRoomBounds(RoomView roomView, RoomElement movingElement, Point newLocation) {
        ElementPainter movingPainter = roomView.getPainters().stream()
                .filter(p -> p.getRoomElement() == movingElement)
                .findFirst()
                .orElse(null);

        if (movingPainter == null) {
            System.out.println("Painter za element nije pronađen: " + movingElement.getIme());
            return false;
        }

        Shape movingShape = movingPainter.getShape();
        boolean isEllipse = movingPainter instanceof VrataPainter || movingPainter instanceof WCSoljaPainter;

        if (isEllipse) {
            // Dimenzije elipse
            Dimension size = movingElement.getScaledDimensions(roomView.getScalingFactor());
            int semiMajorAxis = size.width / 2;
            int semiMinorAxis = size.height / 2;

            // Centar elipse
            int centerX = newLocation.x + semiMajorAxis;
            int centerY = newLocation.y + semiMinorAxis;

            // Proveravamo da li je cela elipsa unutar granica sobe
            Rectangle roomBounds = roomView.getRoomBounds();
            return centerX - semiMajorAxis >= roomBounds.x &&
                    centerX + semiMajorAxis <= roomBounds.x + roomBounds.width &&
                    centerY - semiMinorAxis >= roomBounds.y &&
                    centerY + semiMinorAxis <= roomBounds.y + roomBounds.height;
        } else {
            // Provera za pravougaonik (postojeći kod)
            AffineTransform transform = AffineTransform.getTranslateInstance(
                    newLocation.x - movingElement.getLokacija().x,
                    newLocation.y - movingElement.getLokacija().y
            );
            Shape translatedShape = transform.createTransformedShape(movingShape);

            Rectangle roomBounds = roomView.getRoomBounds();
            return roomBounds.contains(translatedShape.getBounds2D());
        }
    }

    private void returnToInitialLocations() {
        for (Map.Entry<RoomElement, Point> entry : initialLocations.entrySet()) {
            RoomElement element = entry.getKey();
            Point initialLocation = entry.getValue();

            // Vraćamo element na početnu poziciju
            element.setLokacija(initialLocation);
        }
        System.out.println("Svi elementi su vraćeni na početne pozicije.");
    }

    @Override
    public void misKliknut(RoomView roomView, Point point) {
    }
    @Override
    public void misPomeren() {

    }

    @Override
    public void misScroll(RoomView roomView, MouseWheelEvent e) {

    }
}
