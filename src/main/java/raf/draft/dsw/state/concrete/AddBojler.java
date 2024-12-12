package raf.draft.dsw.state.concrete;

import raf.draft.dsw.core.ApplicationFramework;
import raf.draft.dsw.gui.swing.DimensionFrame;
import raf.draft.dsw.gui.swing.MainFrame;
import raf.draft.dsw.gui.swing.RoomView;
import raf.draft.dsw.gui.swing.jtree.model.DraftTreeItem;
import raf.draft.dsw.model.notification.Message;
import raf.draft.dsw.model.notification.MessageType;
import raf.draft.dsw.model.notification.Notification;
import raf.draft.dsw.painter.BojlerPainter;
import raf.draft.dsw.model.structures.namestaj.Bojler;
import raf.draft.dsw.state.State;

import java.awt.*;
import java.awt.event.MouseWheelEvent;
import java.time.LocalDateTime;

public class AddBojler implements State {

    @Override
    public void misKliknut(RoomView roomView, Point point) {

        if(!roomView.isNacrtanaSoba()){
            Message message = ApplicationFramework.getInstance().getMessageGenerator().generateMessage("Morate prvo da nacrtate sobu!", MessageType.WARNING, LocalDateTime.now());
            ApplicationFramework.getInstance().getMessageGenerator().notifySubscribers(new Notification(message));
            return;
        }

        // koristimo metodu koja nam vraca novu lokaciju misa u sljucaju
        // da se nalazimo u nekom zoomiranom stanju
        // treba nam da bi dodali element tacno tamo gde smo kliknuli misem
        point = roomView.transformMousePoint(point);

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

            // Room obavestava RoomView (u addChild metodi) da treba da se repaintuje

            //System.out.println(roomView.getRoom().getNamestaj());
        }
//        else{
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
