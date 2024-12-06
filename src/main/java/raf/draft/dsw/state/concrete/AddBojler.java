package raf.draft.dsw.state.concrete;

import com.sun.tools.javac.Main;
import raf.draft.dsw.core.ApplicationFramework;
import raf.draft.dsw.gui.swing.DimensionFrame;
import raf.draft.dsw.gui.swing.MainFrame;
import raf.draft.dsw.gui.swing.RoomView;
import raf.draft.dsw.gui.swing.jtree.DraftTree;
import raf.draft.dsw.gui.swing.jtree.model.DraftTreeItem;
import raf.draft.dsw.model.notification.Message;
import raf.draft.dsw.model.notification.MessageType;
import raf.draft.dsw.model.notification.Notification;
import raf.draft.dsw.model.painter.BojlerPainter;
import raf.draft.dsw.model.structures.namestaj.Bojler;
import raf.draft.dsw.state.State;

import java.awt.*;
import java.time.LocalDateTime;

public class AddBojler implements State {

    @Override
    public void misKliknut(RoomView roomView, Point point) {

        // kada kliknemo misem na to mesto (point treba nacrtati element)
        // otvara se prozor za unosenje dimenzija

        DimensionFrame frame = new DimensionFrame(roomView);
        frame.setVisible(true);

        Dimension dimenzija = frame.getDimenzija();

        if(dimenzija != null && dimenzija.width > 0 && dimenzija.height > 0) {
            // pravimo novi model
            Bojler bojler = new Bojler(roomView.getRoom(),point,dimenzija);
            BojlerPainter bojlerPainter = new BojlerPainter(bojler,roomView);
            // dodajemo painter na roomView
            roomView.addPainter(bojlerPainter);

            // treba dodati bojler u draftTree
            // na Room tipa DTI treba da se doda dete

            // moramo da pronadjemo odgovarajucu sobu u JTree-u
            DraftTreeItem soba = MainFrame.getInstance().getDraftTree().findTreeItem(roomView.getRoom());
            MainFrame.getInstance().getDraftTree().addRoomChild(soba,bojler);

            //System.out.println(roomView.getRoom().getNamestaj());
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
}
