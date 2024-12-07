package raf.draft.dsw.state.concrete;

import raf.draft.dsw.core.ApplicationFramework;
import raf.draft.dsw.gui.swing.DimensionFrame;
import raf.draft.dsw.gui.swing.MainFrame;
import raf.draft.dsw.gui.swing.RoomView;
import raf.draft.dsw.gui.swing.jtree.model.DraftTreeItem;
import raf.draft.dsw.model.notification.Message;
import raf.draft.dsw.model.notification.MessageType;
import raf.draft.dsw.model.notification.Notification;
import raf.draft.dsw.model.painter.KadaPainter;
import raf.draft.dsw.model.painter.WCSoljaPainter;
import raf.draft.dsw.model.structures.namestaj.Kada;
import raf.draft.dsw.model.structures.namestaj.WCSolja;
import raf.draft.dsw.state.State;

import java.awt.*;
import java.time.LocalDateTime;

public class AddWCSolja implements State {
    @Override
    public void misKliknut(RoomView roomView, Point point) {

        DimensionFrame frame = new DimensionFrame(roomView);
        frame.setVisible(true);

        Dimension dimenzija = frame.getDimenzija();

        if(dimenzija != null && dimenzija.width > 0 && dimenzija.height > 0) {
            WCSolja wcSolja = new WCSolja(roomView.getRoom(), point, dimenzija);
            WCSoljaPainter wcSoljaPainter = new WCSoljaPainter(wcSolja,roomView);
            roomView.addPainter(wcSoljaPainter);

            DraftTreeItem soba = MainFrame.getInstance().getDraftTree().findTreeItem(roomView.getRoom());
            MainFrame.getInstance().getDraftTree().addRoomChild(soba, wcSolja);
        }
        else{
            Message message = ApplicationFramework.getInstance().getMessageGenerator().generateMessage("Niste uneli dimenzije", MessageType.WARNING, LocalDateTime.now());
            ApplicationFramework.getInstance().getMessageGenerator().notifySubscribers(new Notification(message));
            frame.setVisible(true);
        }
    }

    @Override
    public void misPomeren() {

    }

    @Override
    public void misScroll() {

    }

    @Override
    public void misPovucen() {

    }
}
