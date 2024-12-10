package raf.draft.dsw.state.concrete;

import lombok.NoArgsConstructor;
import raf.draft.dsw.gui.swing.DimensionFrame;
import raf.draft.dsw.gui.swing.EditRoomElementFrame;
import raf.draft.dsw.gui.swing.RoomView;
import raf.draft.dsw.model.notification.Notification;
import raf.draft.dsw.model.painter.ElementPainter;
import raf.draft.dsw.model.painter.RoomPainter;
import raf.draft.dsw.state.State;

import java.awt.*;

@NoArgsConstructor
public class EditState implements State {

    @Override
    public void misKliknut(RoomView roomView, Point point) {

        // ako smo kliknuli na element otvara se prozor i unose promene
        // prolazimo kroz paintere da vidimo jesmo li kliknuli na neki
        for(ElementPainter e: roomView.getPainters()){
            if(!(e instanceof RoomPainter) && e.elementAt(point,null)) {
                EditRoomElementFrame frame = new EditRoomElementFrame();
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
    public void misScroll() {

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
