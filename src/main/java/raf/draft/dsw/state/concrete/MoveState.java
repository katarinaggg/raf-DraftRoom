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
import java.awt.geom.*;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@NoArgsConstructor
public class MoveState implements State {

    private Point pocetnaTacka;

    private static final int tolerance = 10;

    private final HashMap<RoomElement, Point> pocetneLokacije = new HashMap<>();


    @Override
    public void misZadrzan(RoomView roomView, Point point) {

        point = roomView.transformMousePoint(point);

        if(!roomView.isNacrtanaSoba()){
            Message message = ApplicationFramework.getInstance().getMessageGenerator().generateMessage("Morate prvo da nacrtate sobu!", MessageType.WARNING, LocalDateTime.now());
            ApplicationFramework.getInstance().getMessageGenerator().notifySubscribers(new Notification(message));
            return;
        }

        if (!roomView.getSelectionList().isEmpty()) {

            pocetnaTacka = point;

            // resetovanje pravougaonika za selekciju
            roomView.setSelectionRectangleLT(null);
            roomView.setSelectionRectangleRB(null);


            // cuvam pocetne lokacije svih selektovanih elemenata, prvo ocistim mapu, onda dodajem
            pocetneLokacije.clear();
            for (ElementPainter painter : roomView.getSelectionList()) {
                RoomElement element = painter.getRoomElement();
                if (element != null) {
                    pocetneLokacije.put(element, new Point(element.getLokacija()));
                }
            }
        }else{
            pocetnaTacka = point;
        }
    }

    @Override
    public void misPovucen(RoomView roomView, Point point) {

        point = roomView.transformMousePoint(point);

        if (pocetnaTacka != null && roomView.getSelectionList().isEmpty()) {
            // pomeranje cele sobe
            int deltaX = point.x - pocetnaTacka.x;
            int deltaY = point.y - pocetnaTacka.y;

            // promenimo pomeraj sobe
            roomView.updateRoomOffset(deltaX, deltaY);

            pocetnaTacka = point;

        } else if (pocetnaTacka != null && !pocetneLokacije.isEmpty()) {
            // racunamo pomeranje misa
            int deltaX = point.x - pocetnaTacka.x;
            int deltaY = point.y - pocetnaTacka.y;

            Rectangle roomBounds = roomView.getRoomBounds();

            HashMap<RoomElement, Point> novaLokacija = new HashMap<>();

            for (ElementPainter painter : roomView.getSelectionList()) {
                RoomElement element = painter.getRoomElement();
                Point initialLocation = pocetneLokacije.get(element);

                if (initialLocation == null) continue;

                // nova lokacija elementa
                int newX = initialLocation.x + deltaX;
                int newY = initialLocation.y + deltaY;
                Point newLocation = new Point(newX, newY);

                Rectangle elementBounds = painter.getShape().getBounds();

                elementBounds.setLocation(newLocation);

                // pepljenje uz ivicu sobe
                Point snappedLocation = snapToEdgeRectangle(roomBounds, elementBounds, tolerance);

                // proveravamo da li je nova lokacija unutar granica sobe
                if (!isWithinRoomBounds(roomView, element, snappedLocation)) {
                    System.out.println("Element " + element.getIme() + " izlazi iz granica sobe!");
                    returnToInitialLocations();
                    roomView.repaint();
                    return;
                }

                // dodajemo novu lokaciju u mapu
                novaLokacija.put(element, snappedLocation);
            }

            // Proveravamo presek sa drugim elementima
            for (Map.Entry<RoomElement, Point> entry : novaLokacija.entrySet()) {
                RoomElement element = entry.getKey();
                Point newLocation = entry.getValue();

                if (hasIntersection(roomView, element, newLocation)) {
                    //System.out.println("Preklapanje detektovano! Vraćanje elemenata na početne pozicije.");
                    returnToInitialLocations();
                    roomView.repaint();
                    return;
                }
            }

            // Ažuriramo lokacije elemenata
            for (Map.Entry<RoomElement, Point> entry : novaLokacija.entrySet()) {
                entry.getKey().setLokacija(entry.getValue());
            }

            // Osvežavanje prikaza
            roomView.repaint();
        }
    }

    @Override
    public void misPusten(RoomView roomView, Point point) {
        point = roomView.transformMousePoint(point);
        pocetnaTacka = null; // Resetujemo početnu tačku
        pocetneLokacije.clear(); // Resetujemo početne lokacije
    }

    private boolean hasIntersection(RoomView roomView, RoomElement movingElement, Point newLocation) {
        // Dohvatamo stvarni oblik pomeranog elementa
        /// .stream() pretvara listu u stream kako bi se lakse obradjivali podaci
        // p predstavlja pojedinacni painter
        ElementPainter movingPainter = roomView.getPainters().stream().filter(p -> p.getRoomElement() == movingElement).findFirst().orElse(null);

        if (movingPainter == null) {
            System.out.println("Painter za pomerani element nije pronađen.");
            return false;
        }

        Shape movingShape = movingPainter.getShape();
        if (movingShape == null) {
            System.out.println("Oblik za pomerani element nije pronađen.");
            return false;
        }

        // transliram oblik na novu lokaciju
        AffineTransform transform = AffineTransform.getTranslateInstance(
                newLocation.x - movingElement.getLokacija().x,
                newLocation.y - movingElement.getLokacija().y
        );
        Shape translatedShape = transform.createTransformedShape(movingShape);


        // provera preseka
        for (ElementPainter painter : roomView.getPainters()) {

            RoomElement randomElement = painter.getRoomElement();

            // preskacemo element koji trenutno pomeramo ili ako je random element null
            if (randomElement == null || randomElement == movingElement) continue;

            Shape randomShape = painter.getShape();
            if (randomShape == null) {
                //System.out.println("Oblik za element " + randomElement + " nije pronađen. Preskačem.");
                continue;
            }
            if (translatedShape.getBounds2D().intersects(randomShape.getBounds2D())) {
                // proveravam da li postoji stvarni presek izmedju oblika
                if (translatedShape.intersects(randomShape.getBounds2D())) {
                    //System.out.println("Presek detektovan između " + movingElement.getIme() + " i " + randomElement.getIme());
                    return true;
                }
            }

        }

        return false;
    }

    private boolean isWithinRoomBounds(RoomView roomView, RoomElement movingElement, Point newLocation) {
        // hvatamo painter za pomerani element
        ElementPainter movingPainter = roomView.getPainters().stream()
                .filter(p -> p.getRoomElement() == movingElement)
                .findFirst()
                .orElse(null);

        if (movingPainter == null) {
            return false;
        }

        // hvatamo oblik elementa
        Shape movingShape = movingPainter.getShape();

        if (movingShape == null) {
            return false;
        }

        // Transliramo oblik na novu lokaciju
        AffineTransform transform = AffineTransform.getTranslateInstance(
                newLocation.x - movingElement.getLokacija().x,
                newLocation.y - movingElement.getLokacija().y
        );
        Shape translatedShape = transform.createTransformedShape(movingShape);

        // proverava da li granice oblika ostaju unutar granica sobe
        Rectangle roomBounds = roomView.getRoomBounds();
        return roomBounds.contains(translatedShape.getBounds());
    }


    private Point snapToEdgeRectangle(Rectangle roomBounds, Rectangle elementBounds, int tolerance) {
        // nova pozicija elementa
        Point uzIvicu = new Point(elementBounds.x, elementBounds.y);

        // provera udaljenosti od svake ivice sobe
        if (Math.abs(elementBounds.x - roomBounds.x) <= tolerance) {
            // lepljenje uz levu ivicu sobe
            uzIvicu.x = roomBounds.x;
        } else if (Math.abs(elementBounds.x + elementBounds.width - (roomBounds.x + roomBounds.width)) <= tolerance) {
            // lepljenje uz desnu ivicu sobe
            uzIvicu.x = roomBounds.x + roomBounds.width - elementBounds.width;
        }


        if (Math.abs(elementBounds.y - roomBounds.y) <= tolerance) {
            // lepljenje uz gornju ivicu
            uzIvicu.y = roomBounds.y;
            System.out.println("Snapped to top edge");


        } else if (Math.abs(elementBounds.y + elementBounds.height - (roomBounds.y + roomBounds.height)) <= tolerance) {
            //lepljenje uz donju ivicu
            uzIvicu.y = roomBounds.y + roomBounds.height - elementBounds.height;
            System.out.println("Snapped to bottom edge");
        }
        return uzIvicu;
    }

    private void returnToInitialLocations() {
        for (Map.Entry<RoomElement, Point> entry : pocetneLokacije.entrySet()) {
            RoomElement element = entry.getKey();
            Point initialLocation = entry.getValue();

            // vraćamo element na pocetnu poziciju
            element.setLokacija(initialLocation);
        }
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
