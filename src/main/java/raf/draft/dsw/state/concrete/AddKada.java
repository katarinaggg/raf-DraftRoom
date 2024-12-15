package raf.draft.dsw.state.concrete;

import raf.draft.dsw.core.ApplicationFramework;
import raf.draft.dsw.gui.swing.DimensionFrame;
import raf.draft.dsw.gui.swing.MainFrame;
import raf.draft.dsw.gui.swing.RoomView;
import raf.draft.dsw.gui.swing.jtree.model.DraftTreeItem;
import raf.draft.dsw.model.notification.Message;
import raf.draft.dsw.model.notification.MessageType;
import raf.draft.dsw.model.notification.Notification;
import raf.draft.dsw.painter.KadaPainter;
import raf.draft.dsw.model.structures.namestaj.Kada;
import raf.draft.dsw.state.State;

import java.awt.*;
import java.awt.event.MouseWheelEvent;
import java.time.LocalDateTime;

public class AddKada implements State {

    @Override
    public void misKliknut(RoomView roomView, Point point) {

        if(!roomView.isNacrtanaSoba()){
            Message message = ApplicationFramework.getInstance().getMessageGenerator().generateMessage("Morate prvo da nacrtate sobu!", MessageType.WARNING, LocalDateTime.now());
            ApplicationFramework.getInstance().getMessageGenerator().notifySubscribers(new Notification(message));
            return;
        }

        point = roomView.transformMousePoint(point);

        DimensionFrame frame = new DimensionFrame(roomView);
        frame.setVisible(true);

        Dimension dimenzija = frame.getDimenzija();

        if(dimenzija!=null && dimenzija.getWidth()>0 && dimenzija.getHeight()>0) {

            Kada kada = new Kada(roomView.getRoom(), point, dimenzija);
            KadaPainter kadaPainter = new KadaPainter(kada,roomView);

//            if(!roomView.unutarSobe(point.x,point.y,dimenzija.width,dimenzija.height)){
//                Message message = ApplicationFramework.getInstance().getMessageGenerator().generateMessage("Ne mozete da dodate element van granica sobe!", MessageType.WARNING, LocalDateTime.now());
//                ApplicationFramework.getInstance().getMessageGenerator().notifySubscribers(new Notification(message));
//                return;
//            }

            if(roomView.preklapanjeElemenata(kadaPainter.getShape())){
                // ako se preklapaju ne mozemo da ga dodamo i izlazi poruka
                Message message = ApplicationFramework.getInstance().getMessageGenerator().generateMessage("Ne mozete da dodate element jer se preklapa sa postojecim!", MessageType.WARNING, LocalDateTime.now());
                ApplicationFramework.getInstance().getMessageGenerator().notifySubscribers(new Notification(message));
                return;
            }

            roomView.addPainter(kadaPainter);

            DraftTreeItem soba = MainFrame.getInstance().getDraftTree().findTreeItem(roomView.getRoom());
            MainFrame.getInstance().getDraftTree().addRoomChild(soba, kada);
        }
//        else {
//            Message message = ApplicationFramework.getInstance().getMessageGenerator().generateMessage("Niste uneli dimenzije", MessageType.WARNING, LocalDateTime.now());
//            ApplicationFramework.getInstance().getMessageGenerator().notifySubscribers(new Notification(message));
//            frame.setVisible(true);
//        }
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
