package raf.draft.dsw.state.concrete;

import lombok.NoArgsConstructor;
import raf.draft.dsw.core.ApplicationFramework;
import raf.draft.dsw.gui.swing.RoomView;
import raf.draft.dsw.model.notification.Message;
import raf.draft.dsw.model.notification.MessageType;
import raf.draft.dsw.model.notification.Notification;
import raf.draft.dsw.state.State;

import java.awt.*;
import java.awt.event.MouseWheelEvent;
import java.time.LocalDateTime;

@NoArgsConstructor
public class ZoomState implements State {

    @Override
    public void misKliknut(RoomView roomView, Point point) {

    }

    @Override
    public void misPomeren() {

    }

    @Override
    public void misScroll(RoomView roomView, MouseWheelEvent e) {

        if(!roomView.isNacrtanaSoba()){
            Message message = ApplicationFramework.getInstance().getMessageGenerator().generateMessage("Morate prvo da nacrtate sobu!", MessageType.WARNING, LocalDateTime.now());
            ApplicationFramework.getInstance().getMessageGenerator().notifySubscribers(new Notification(message));
            return;
        }

        int rotation = e.getWheelRotation();
        double trenutniZoom = roomView.getZoomLevel();

        // ZOOM IN
        if(rotation<0){
            if(trenutniZoom < roomView.getMAX_ZOOM()) {
                roomView.setZoomLevel(roomView.getZoomLevel() * 1.1);
            }
            else {
                Message message = ApplicationFramework.getInstance().getMessageGenerator().generateMessage("Ne mozete vise da zumirate!", MessageType.WARNING, LocalDateTime.now());
                ApplicationFramework.getInstance().getMessageGenerator().notifySubscribers(new Notification(message));
                return;
            }
        }
        // ZOOM OUT
        else if(rotation>0){
            if(trenutniZoom > roomView.getMIN_ZOOM()) {
                roomView.setZoomLevel(roomView.getZoomLevel() / 1.1);
            }
            else {
                Message message = ApplicationFramework.getInstance().getMessageGenerator().generateMessage("Ne mozete vise da zumirate!", MessageType.WARNING, LocalDateTime.now());
                ApplicationFramework.getInstance().getMessageGenerator().notifySubscribers(new Notification(message));
                return;
            }
        }

        // Ponovno izračunavanje skaliranja nakon promene zumiranja
        roomView.calculateScalingFactor();

        roomView.repaint();
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
