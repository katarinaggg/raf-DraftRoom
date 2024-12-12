package raf.draft.dsw.state.concrete;

import lombok.NoArgsConstructor;
import raf.draft.dsw.core.ApplicationFramework;
import raf.draft.dsw.gui.swing.RoomView;
import raf.draft.dsw.model.notification.Message;
import raf.draft.dsw.model.notification.MessageType;
import raf.draft.dsw.model.notification.Notification;
import raf.draft.dsw.painter.ElementPainter;
import raf.draft.dsw.model.structures.namestaj.RoomElement;
import raf.draft.dsw.state.State;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.time.LocalDateTime;

@NoArgsConstructor
public class ResizeState implements State {

    private Point kliknut;
    private RoomElement resizingElement;
    private Point initialPoint;
    private Dimension initialSize;

    @Override
    public void misZadrzan(RoomView roomView, Point point) {

        if(!roomView.isNacrtanaSoba()){
            Message message = ApplicationFramework.getInstance().getMessageGenerator().generateMessage("Morate prvo da nacrtate sobu!", MessageType.WARNING, LocalDateTime.now());
            ApplicationFramework.getInstance().getMessageGenerator().notifySubscribers(new Notification(message));
            return;
        }

        kliknut = point;

        if (!roomView.getSelectionList().isEmpty()) {
            // Dobijamo selektovani element
            ElementPainter selectedPainter = roomView.getSelectionList().get(0);
            RoomElement element = selectedPainter.getRoomElement();
            Shape shape = selectedPainter.getShape();
            Rectangle2D bounds = shape.getBounds2D();

            int donjiX = (int) bounds.getMaxX(); // x koordinata DONJE DESNE IVICE
            int donjiY = (int) bounds.getMaxY(); // y koordinata DONJE DESNE IVICE

            // Provera da li je klik blizu donjeg desnog ugla
            int tolerance = 10;

            if (kliknut.x >= donjiX - tolerance && kliknut.x <= donjiX + tolerance && kliknut.y >= donjiY - tolerance && kliknut.y <= donjiY + tolerance) {
                //System.out.println("Kliknuto u donji desni ugao!");

                resizingElement = element;   // Čuvamo referencu na element koji se menja
                initialPoint = point;        // Početna tačka klika
                initialSize = element.getSize(); // Početna dimenzija elementa
            }
        }
    }


    @Override
    public void misPovucen(RoomView roomView, Point point) {
        if (resizingElement != null && initialPoint != null && initialSize != null) {

            // Racunamo promenu u koordinatama misa
            int deltaX = point.x - initialPoint.x;
            int deltaY = point.y - initialPoint.y;

            // Menjamo dimenzije elementa na osnovu promene
            int newWidth = Math.max(10, initialSize.width + deltaX); // Minimalna širina 10
            int newHeight = Math.max(10, initialSize.height + deltaY); // Minimalna visina 10

            // Postavljamo nove dimenzije elementa
            resizingElement.setSize(new Dimension(newWidth, newHeight));

            roomView.repaint();
            //System.out.println("Dimenzije elementa dok se menjanju " + newWidth + "x" + newHeight);
        }
    }

    @Override
    public void misPusten(RoomView roomView, Point point) {
            resizingElement = null;
            initialPoint = null;
            initialSize = null;
            //System.out.println("Resize završen.");
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
