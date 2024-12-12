package raf.draft.dsw.state.concrete;

import lombok.NoArgsConstructor;
import raf.draft.dsw.core.ApplicationFramework;
import raf.draft.dsw.gui.swing.RoomView;
import raf.draft.dsw.model.notification.Message;
import raf.draft.dsw.model.notification.MessageType;
import raf.draft.dsw.model.notification.Notification;
import raf.draft.dsw.painter.ElementPainter;
import raf.draft.dsw.painter.BojlerPainter;
import raf.draft.dsw.painter.ElementPainter;
import raf.draft.dsw.model.structures.namestaj.RoomElement;
import raf.draft.dsw.painter.VrataPainter;
import raf.draft.dsw.painter.WCSoljaPainter;
import raf.draft.dsw.state.State;

import java.awt.*;
import java.awt.geom.*;
import java.time.LocalDateTime;
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
        if (initialPoint != null && roomView.getSelectionList().isEmpty()) {
            // Pomeranje cele sobe
            int deltaX = point.x - initialPoint.x;
            int deltaY = point.y - initialPoint.y;

            // Ažuriramo pomak sobe
            roomView.updateRoomOffset(deltaX, deltaY);

            // Ažuriramo početnu tačku
            initialPoint = point;
        } else if (initialPoint != null && !initialLocations.isEmpty()) {
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

                // Dobijamo pravougaonik za selekciju elementa
                Rectangle elementBounds = painter.getShape().getBounds();

                // Ažuriramo poziciju pravougaonika prema trenutnom pomeranju
                elementBounds.setLocation(newLocation);

                // Lepljenje uz ivicu sobe
                Point snappedLocation = snapToEdgeRectangle(roomBounds, elementBounds, tolerance);

                // Proveravamo da li je nova lokacija unutar granica sobe
                if (!isWithinRoomBounds(roomView, element, snappedLocation)) {
                    System.out.println("Element " + element.getIme() + " izlazi iz granica sobe!");
                    returnToInitialLocations();
                    roomView.repaint();
                    return;
                }

                // Dodajemo novu lokaciju u mapu
                newLocations.put(element, snappedLocation);
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

            // Proveravamo tačan presek između stvarnih oblika
            if (translatedShape.getBounds2D().intersects(otherShape.getBounds2D()) &&
                    translatedShape.intersects(otherShape.getBounds2D())) {
                // Dodatna precizna provera preseka stvarnih oblika
                Area movingArea = new Area(translatedShape);
                Area otherArea = new Area(otherShape);
                movingArea.intersect(otherArea);
                if (!movingArea.isEmpty()) {
                    System.out.println("Precizan presek detektovan između " + movingElement.getIme() + " i " + otherElement.getIme());
                    return true;
                }
            }
        }

        return false; // Nema preklapanja
    }


//    private boolean isWithinRoomBounds(RoomView roomView, RoomElement movingElement, Point newLocation) {
//        ElementPainter movingPainter = roomView.getPainters().stream()
//                .filter(p -> p.getRoomElement() == movingElement)
//                .findFirst()
//                .orElse(null);
//
//        if (movingPainter == null) {
//            System.out.println("Painter za element nije pronađen: " + movingElement.getIme());
//            return false;
//        }
//
//        Shape movingShape = movingPainter.getShape();
//        boolean isEllipse = movingPainter instanceof VrataPainter || movingPainter instanceof WCSoljaPainter
//                || movingPainter instanceof BojlerPainter;
//
//        if (isEllipse) {
//            // Dimenzije elipse
//            Dimension size = movingElement.getScaledDimensions(roomView.getScalingFactor());
//            int semiMajorAxis = size.width / 2;
//            int semiMinorAxis = size.height / 2;
//
//            // Centar elipse
//            int centerX = newLocation.x + semiMajorAxis;
//            int centerY = newLocation.y + semiMinorAxis;
//
//            // Proveravamo da li je cela elipsa unutar granica sobe
//            Rectangle roomBounds = roomView.getRoomBounds();
//            return centerX - semiMajorAxis >= roomBounds.x &&
//                    centerX + semiMajorAxis <= roomBounds.x + roomBounds.width &&
//                    centerY - semiMinorAxis >= roomBounds.y &&
//                    centerY + semiMinorAxis <= roomBounds.y + roomBounds.height;
//        } else {
//            // Provera za pravougaonik
//            AffineTransform transform = AffineTransform.getTranslateInstance(
//                    newLocation.x - movingElement.getLokacija().x,
//                    newLocation.y - movingElement.getLokacija().y
//            );
//            Shape translatedShape = transform.createTransformedShape(movingShape);
//
//            Rectangle roomBounds = roomView.getRoomBounds();
//            return roomBounds.contains(translatedShape.getBounds2D());
//        }
//    }

    private boolean isWithinRoomBounds(RoomView roomView, RoomElement movingElement, Point newLocation) {
        ElementPainter movingPainter = roomView.getPainters().stream()
                .filter(p -> p.getRoomElement() == movingElement)
                .findFirst()
                .orElse(null);

        if (movingPainter == null) {
            return false;
        }

        Shape movingShape = movingPainter.getShape();
        boolean isEllipse = movingPainter instanceof VrataPainter || movingPainter instanceof WCSoljaPainter || movingPainter instanceof BojlerPainter;

        if (isEllipse && movingShape instanceof GeneralPath) {
            Rectangle2D bounds = movingShape.getBounds2D();

            double radiusX = bounds.getWidth() / 2;
            double radiusY = bounds.getHeight() / 2;
            double centerX = newLocation.x + radiusX;
            double centerY = newLocation.y + radiusY;

            Rectangle roomBounds = roomView.getRoomBounds();
            return centerX - radiusX >= roomBounds.x &&
                    centerX + radiusX <= roomBounds.x + roomBounds.width &&
                    centerY - radiusY >= roomBounds.y &&
                    centerY + radiusY <= roomBounds.y + roomBounds.height;
        }

        // Provera za pravougaone oblike
        Rectangle roomBounds = roomView.getRoomBounds();
        AffineTransform transform = AffineTransform.getTranslateInstance(
                newLocation.x - movingElement.getLokacija().x,
                newLocation.y - movingElement.getLokacija().y
        );
        Shape translatedShape = transform.createTransformedShape(movingShape);

        return roomBounds.contains(translatedShape.getBounds2D());
    }


    private Point snapToEdgeRectangle(Rectangle roomBounds, Rectangle elementBounds, int tolerance) {
        // Nova pozicija elementa
        Point adjustedLocation = new Point(elementBounds.x, elementBounds.y);

        // Provera udaljenosti od svake ivice sobe
        if (Math.abs(elementBounds.x - roomBounds.x) <= tolerance) {
            // Lepljenje uz levu ivicu sobe
            adjustedLocation.x = roomBounds.x;
        } else if (Math.abs(elementBounds.x + elementBounds.width - (roomBounds.x + roomBounds.width)) <= tolerance) {
            // Lepljenje uz desnu ivicu sobe
            adjustedLocation.x = roomBounds.x + roomBounds.width - elementBounds.width;
        }

        if (Math.abs(elementBounds.y - roomBounds.y) <= tolerance) {
            adjustedLocation.y = roomBounds.y;
            System.out.println("Snapped to top edge");
        } else if (Math.abs(elementBounds.y + elementBounds.height - (roomBounds.y + roomBounds.height)) <= tolerance) {
            adjustedLocation.y = roomBounds.y + roomBounds.height - elementBounds.height;
            System.out.println("Snapped to bottom edge");
        }
        return adjustedLocation;
    }


    private Point snapToEdge(Rectangle roomBounds, Shape elementShape, boolean isEllipse, int tolerance) {
        if (isEllipse && elementShape instanceof GeneralPath) {
            // Dobijamo okvirne granice oblika (bounding box)
            Rectangle2D bounds = elementShape.getBounds2D();

            double centerX = bounds.getCenterX();
            double centerY = bounds.getCenterY();
            double radiusX = bounds.getWidth() / 2;
            double radiusY = bounds.getHeight() / 2;

            // Prilagođavanje pozicije prema sobnim granicama
            if (Math.abs(centerX - radiusX - roomBounds.x) <= tolerance) {
                centerX = roomBounds.x + radiusX;
            } else if (Math.abs(centerX + radiusX - (roomBounds.x + roomBounds.width)) <= tolerance) {
                centerX = roomBounds.x + roomBounds.width - radiusX;
            }

            if (Math.abs(centerY - radiusY - roomBounds.y) <= tolerance) {
                centerY = roomBounds.y + radiusY;
            } else if (Math.abs(centerY + radiusY - (roomBounds.y + roomBounds.height)) <= tolerance) {
                centerY = roomBounds.y + roomBounds.height - radiusY;
            }

            return new Point((int) (centerX - radiusX), (int) (centerY - radiusY));
        }

        // Za pravougaone oblike, koristimo postojeću logiku
        Rectangle elementBounds = elementShape.getBounds();
        return snapToEdgeRectangle(roomBounds, elementBounds, tolerance);
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
    public void misScroll() {

    }
}
