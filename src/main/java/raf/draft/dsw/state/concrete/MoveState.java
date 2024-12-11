package raf.draft.dsw.state.concrete;

import lombok.NoArgsConstructor;
import raf.draft.dsw.gui.swing.RoomView;
import raf.draft.dsw.model.painter.ElementPainter;
import raf.draft.dsw.model.structures.namestaj.RoomElement;
import raf.draft.dsw.state.State;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.util.HashMap;
import java.util.Map;

@NoArgsConstructor
public class MoveState implements State {

    private Point initialPoint; // Početna tačka klika miša

    private static int tolerance=10;

    private final HashMap<RoomElement, Point> initialLocations = new HashMap<>(); // Početne lokacije svih selektovanih elemenata


    @Override
    public void misZadrzan(RoomView roomView, Point point) {
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

//
//      //metoda ima i presek, ali umesto da vrati na staro mesto, samo ga zalepi za drugi objekat
//    @Override
//    public void misPovucen(RoomView roomView, Point point) {
//        if (initialPoint != null && !initialLocations.isEmpty()) {
//            // Računamo pomeranje miša
//            int deltaX = point.x - initialPoint.x;
//            int deltaY = point.y - initialPoint.y;
//
//            // Dobijamo granice sobe
//            Rectangle roomBounds = roomView.getRoomBounds();
//
//            // Pomeramo sve selektovane elemente
//            for (Map.Entry<RoomElement, Point> entry : initialLocations.entrySet()) {
//                RoomElement element = entry.getKey();
//                Point initialLocation = entry.getValue();
//
//                // Nova lokacija elementa
//                int newX = initialLocation.x + deltaX;
//                int newY = initialLocation.y + deltaY;
//                Point newLocation = new Point(newX, newY);
//
//                // Dimenzije elementa (skalirane)
//                int elementWidth = (int) (element.getSize().width * roomView.getScalingFactor());
//                int elementHeight = (int) (element.getSize().height * roomView.getScalingFactor());
//
//                // Proveravamo da li je nova lokacija unutar granica sobe
//                if (newX < roomBounds.x || newY < roomBounds.y ||
//                        newX + elementWidth > roomBounds.x + roomBounds.width ||
//                        newY + elementHeight > roomBounds.y + roomBounds.height) {
//                    continue; // Preskočimo pomeranje ovog elementa ako izlazi iz granica sobe
//                }
//
//                // Primena "snap-to-edge" logike
//                if (Math.abs(newLocation.x - roomBounds.x) < tolerance) {
//                    newLocation.x = roomBounds.x; // Lepljenje uz levu ivicu
//                }
//                if (Math.abs(newLocation.y - roomBounds.y) < tolerance) {
//                    newLocation.y = roomBounds.y; // Lepljenje uz gornju ivicu
//                }
//                if (Math.abs((newLocation.x + elementWidth) - (roomBounds.x + roomBounds.width)) < tolerance) {
//                    newLocation.x = (roomBounds.x + roomBounds.width) - elementWidth; // Lepljenje uz desnu ivicu
//                }
//                if (Math.abs((newLocation.y + elementHeight) - (roomBounds.y + roomBounds.height)) < tolerance) {
//                    newLocation.y = (roomBounds.y + roomBounds.height) - elementHeight; // Lepljenje uz donju ivicu
//                }
//
//                // Proveravamo presek sa drugim elementima
//                if (hasIntersection(roomView, element, newLocation)) {
//                    System.out.println("Preklapanje detektovano! Pomeranje poništeno.");
//                    return; // Poništavamo pomeranje
//                }
//
//                // Ažuriramo lokaciju elementa
//                element.setLokacija(newLocation);
//            }
//
//            // Osvežavamo prikaz
//            roomView.repaint();
//        }
//    }


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

            // Pomeramo sve selektovane elemente
            for (Map.Entry<RoomElement, Point> entry : initialLocations.entrySet()) {
                RoomElement element = entry.getKey();
                Point initialLocation = entry.getValue();

                // Nova lokacija elementa
                int newX = initialLocation.x + deltaX;
                int newY = initialLocation.y + deltaY;
                Point newLocation = new Point(newX, newY);

                // Dimenzije elementa (skalirane)
                int elementWidth = (int) (element.getSize().width * roomView.getScalingFactor());
                int elementHeight = (int) (element.getSize().height * roomView.getScalingFactor());

                // Proveravamo da li je nova lokacija unutar granica sobe
                if (newX < roomBounds.x || newY < roomBounds.y ||
                        newX + elementWidth > roomBounds.x + roomBounds.width ||
                        newY + elementHeight > roomBounds.y + roomBounds.height) {
                    continue; // Preskočimo pomeranje ovog elementa ako izlazi iz granica sobe
                }

                // Primena "snap-to-edge" logike
                newLocation = snapToEdge(roomBounds, newLocation, elementWidth, elementHeight, tolerance);

                // Dodajemo novu lokaciju u mapu
                newLocations.put(element, newLocation);
            }

            // Proveravamo presek sa drugim elementima
            for (Map.Entry<RoomElement, Point> entry : newLocations.entrySet()) {
                RoomElement element = entry.getKey();
                Point newLocation = entry.getValue();

                if (hasIntersection(roomView, element, newLocation)) {
                    //System.out.println("Preklapanje detektovano! Vraćanje elemenata na početne pozicije.");

                    // Vraćamo sve elemente na početne pozicije
                    for (Map.Entry<RoomElement, Point> initialEntry : initialLocations.entrySet()) {
                        initialEntry.getKey().setLokacija(initialEntry.getValue());
                    }

                    roomView.repaint(); // Osvežavamo prikaz
                    return; // Prekidamo pomeranje
                }
            }

            // Ažuriramo lokacije elemenata
            for (Map.Entry<RoomElement, Point> entry : newLocations.entrySet()) {
                entry.getKey().setLokacija(entry.getValue());
            }

            // Osvežavamo prikaz
            roomView.repaint();
        }
    }

    @Override
    public void misPusten(RoomView roomView, Point point) {
        initialPoint = null; // Resetujemo početnu tačku
        initialLocations.clear(); // Resetujemo početne lokacije
    }


    private Point snapToEdge(Rectangle roomBounds, Point newLocation, int elementWidth, int elementHeight, int tolerance) {
        Point snappedLocation = new Point(newLocation);

        // Lepljenje uz levu ivicu
        if (Math.abs(snappedLocation.x - roomBounds.x) < tolerance) {
            snappedLocation.x = roomBounds.x;
        }

        // Lepljenje uz gornju ivicu
        if (Math.abs(snappedLocation.y - roomBounds.y) < tolerance) {
            snappedLocation.y = roomBounds.y;
        }

        // Lepljenje uz desnu ivicu
        if (Math.abs((snappedLocation.x + elementWidth) - (roomBounds.x + roomBounds.width)) < tolerance) {
            snappedLocation.x = (roomBounds.x + roomBounds.width) - elementWidth;
        }

        // Lepljenje uz donju ivicu
        if (Math.abs((snappedLocation.y + elementHeight) - (roomBounds.y + roomBounds.height)) < tolerance) {
            snappedLocation.y = (roomBounds.y + roomBounds.height) - elementHeight;
        }

        return snappedLocation;
    }

    private boolean hasIntersection(RoomView roomView, RoomElement movingElement, Point newLocation) {
        // Kreiramo pravougaonik za pomerani element
        Rectangle movingRect = new Rectangle(
                newLocation.x,
                newLocation.y,
                (int) (movingElement.getSize().width * roomView.getScalingFactor()),
                (int) (movingElement.getSize().height * roomView.getScalingFactor())
        );

        // Prolazimo kroz sve elemente u sobi
        for (ElementPainter painter : roomView.getPainters()) {
            RoomElement otherElement = painter.getRoomElement();

            // Preskačemo element koji trenutno pomeramo
            if (otherElement == movingElement) continue;

            // Preskačemo ako je element null
            if (otherElement == null || otherElement.getLokacija() == null) continue;

            // Kreiramo pravougaonik za drugi element
            Rectangle otherRect = new Rectangle(
                    otherElement.getLokacija().x,
                    otherElement.getLokacija().y,
                    (int) (otherElement.getSize().width * roomView.getScalingFactor()),
                    (int) (otherElement.getSize().height * roomView.getScalingFactor())
            );

            // Provera preseka
            if (movingRect.intersects(otherRect)) {
                return true; // Preklapanje detektovano
            }
        }
        return false; // Nema preklapanja
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
