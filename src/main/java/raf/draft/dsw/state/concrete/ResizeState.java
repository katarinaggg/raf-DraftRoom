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
import java.awt.event.MouseWheelEvent;
import java.awt.geom.Rectangle2D;
import java.time.LocalDateTime;

@NoArgsConstructor
public class ResizeState implements State {

    private Point kliknut;
    private RoomElement resizingElement;
    private Point pocetnaTacka;
    private Dimension pocetneDimenzije;

    @Override
    public void misZadrzan(RoomView roomView, Point point) {

        if(!roomView.isNacrtanaSoba()){
            Message message = ApplicationFramework.getInstance().getMessageGenerator().generateMessage("Morate prvo da nacrtate sobu!", MessageType.WARNING, LocalDateTime.now());
            ApplicationFramework.getInstance().getMessageGenerator().notifySubscribers(new Notification(message));
            return;
        }

        // menjamo point u slucaju da smo u nekom od zumiranih stanja
        // treba da cuvamo pravo mesto klika misem iako smo u zoom-u
        point = roomView.transformMousePoint(point);

        kliknut = point;

        if (!roomView.getSelectionList().isEmpty()) {

            // dobijamo selektovani element
            ElementPainter selectedPainter = roomView.getSelectionList().getFirst();
            RoomElement element = selectedPainter.getRoomElement();
            Shape shape = selectedPainter.getShape();
            Rectangle2D bounds = shape.getBounds2D();

            int donjiX = (int) bounds.getMaxX(); // x koordinata DONJE DESNE IVICE
            int donjiY = (int) bounds.getMaxY(); // y koordinata DONJE DESNE IVICE

            // provera da li je klik blizu donjeg desnog ugla
            int tolerance = 10;

            if (kliknut.x >= donjiX - tolerance && kliknut.x <= donjiX + tolerance && kliknut.y >= donjiY - tolerance && kliknut.y <= donjiY + tolerance) {
                //System.out.println("Kliknut donji desni ugao!");

                resizingElement = element;
                pocetnaTacka = point;
                pocetneDimenzije = element.getSize();
            }
        }
        else {
            // ako je lista prazna nema selektovanih elemenata za resize
            Message message = ApplicationFramework.getInstance().getMessageGenerator().generateMessage("Morate prvo da selektujete element", MessageType.INFORMATION, LocalDateTime.now());
            ApplicationFramework.getInstance().getMessageGenerator().notifySubscribers(new Notification(message));
        }
    }


    @Override

    public void misPovucen(RoomView roomView, Point point) {

        if (resizingElement != null && pocetnaTacka != null && pocetneDimenzije != null) {

            point = roomView.transformMousePoint(point);

            // racunamo promenu u koordinatama misa
            int deltaX = point.x - pocetnaTacka.x;
            int deltaY = point.y - pocetnaTacka.y;

            // menjamo dimenzije elementa na osnovu promene
            int newWidth = Math.max(10, pocetneDimenzije.width + deltaX);
            int newHeight = Math.max(10, pocetneDimenzije.height + deltaY);

            // Postavljamo nove dimenzije elementa
            resizingElement.setSize(new Dimension(newWidth, newHeight));

            roomView.repaint();
            //System.out.println("Dimenzije elementa dok se menjanju " + newWidth + "x" + newHeight);
        }
    }

    @Override
    public void misPusten(RoomView roomView, Point point) {
            point = roomView.transformMousePoint(point);
            resizingElement = null;
            pocetnaTacka = null;
            pocetneDimenzije = null;
            //System.out.println("Resize završen.");
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
