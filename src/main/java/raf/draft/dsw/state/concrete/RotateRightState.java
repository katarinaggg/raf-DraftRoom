package raf.draft.dsw.state.concrete;

import lombok.NoArgsConstructor;
import raf.draft.dsw.core.ApplicationFramework;
import raf.draft.dsw.gui.swing.RoomView;
import raf.draft.dsw.model.notification.Message;
import raf.draft.dsw.model.notification.MessageType;
import raf.draft.dsw.model.notification.Notification;
import raf.draft.dsw.painter.ElementPainter;
import raf.draft.dsw.state.State;

import java.awt.*;
import java.awt.event.MouseWheelEvent;
import java.time.LocalDateTime;

@NoArgsConstructor
public class RotateRightState implements State {

    @Override
    public void misKliknut(RoomView roomView, Point point) {

        if(!roomView.isNacrtanaSoba()){
            Message message = ApplicationFramework.getInstance().getMessageGenerator().generateMessage("Morate prvo da nacrtate sobu!", MessageType.WARNING, LocalDateTime.now());
            ApplicationFramework.getInstance().getMessageGenerator().notifySubscribers(new Notification(message));
            return;
        }

        // ako je lista prazna nemamo sta da rotiramo
        if(roomView.getSelectionList().isEmpty()){
            Message message = ApplicationFramework.getInstance().getMessageGenerator().generateMessage("Morate prvo da selektujete element", MessageType.INFORMATION, LocalDateTime.now());
            ApplicationFramework.getInstance().getMessageGenerator().notifySubscribers(new Notification(message));
            return;
        }

        for(ElementPainter e: roomView.getSelectionList()){
            // jednom se rotira od prethodnog stanja
            e.getRoomElement().setRotateRatio(e.getRoomElement().getRotacija()+1);
        }
        roomView.repaint();
    }

    @Override
    public void misPomeren() {

    }

    @Override
    public void misScroll(RoomView roomView, MouseWheelEvent e) {

    }

    @Override
    public void misZadrzan(RoomView roomView, Point point) {

    }

    @Override
    public void misPovucen(RoomView roomView, Point point) {

    }

    @Override
    public void misPusten(RoomView roomView, Point point) {

    }


}
