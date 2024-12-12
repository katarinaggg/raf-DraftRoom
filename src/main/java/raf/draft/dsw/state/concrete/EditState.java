package raf.draft.dsw.state.concrete;

import lombok.NoArgsConstructor;
import raf.draft.dsw.core.ApplicationFramework;
import raf.draft.dsw.gui.swing.EditRoomElementFrame;
import raf.draft.dsw.gui.swing.RoomView;
import raf.draft.dsw.model.notification.Message;
import raf.draft.dsw.model.notification.MessageType;
import raf.draft.dsw.model.notification.Notification;
import raf.draft.dsw.painter.ElementPainter;
import raf.draft.dsw.painter.RoomPainter;
import raf.draft.dsw.state.State;

import java.awt.*;
import java.awt.event.MouseWheelEvent;
import java.time.LocalDateTime;

@NoArgsConstructor
public class EditState implements State {

    @Override
    public void misKliknut(RoomView roomView, Point point) {

        if(!roomView.isNacrtanaSoba()){
            Message message = ApplicationFramework.getInstance().getMessageGenerator().generateMessage("Morate prvo da nacrtate sobu!", MessageType.WARNING, LocalDateTime.now());
            ApplicationFramework.getInstance().getMessageGenerator().notifySubscribers(new Notification(message));
            return;
        }

        // i ovde nam treba nova lokacija misem ako se nalazimo u nekom zumiranom stanju
        // treba nam da li smo kliknuli bas na taj element iako se nalazimo u zumiranom stanju
        // sto znaci stvarna lokacija misem
        point = roomView.transformMousePoint(point);

        // ako smo kliknuli na element otvara se prozor i unose promene
        // prolazimo kroz paintere da vidimo jesmo li kliknuli na neki
        for(ElementPainter e: roomView.getPainters()){
            if(!(e instanceof RoomPainter) && e.elementAt(point,null)) {
                EditRoomElementFrame frame = new EditRoomElementFrame(e.getRoomElement());
                frame.setVisible(true);

                Dimension dimension = frame.getDimension();
                Color color = frame.getColor();

                // ako se promenila dimenzija setujemo nove vrednosti
                // na RoomElement tog paintera
                if(dimension!=null) {
                    e.getRoomElement().setSize(dimension);
                }
                // ako se promeni boja setujemo boju
                if(color!=null) {
                    e.getRoomElement().setColor(color);
                }

//                System.out.println(e.getRoomElement().getSize().toString());
//                System.out.println(e.getRoomElement().getColor().toString());

                // Room obavestava subscribere da je doslo do promene
                //  tj RoomView ce se repaintovati
                roomView.getRoom().notifySubscribers(new Notification());
            }
        }
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
