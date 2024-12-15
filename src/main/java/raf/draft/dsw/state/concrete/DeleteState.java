package raf.draft.dsw.state.concrete;

import lombok.NoArgsConstructor;
import raf.draft.dsw.core.ApplicationFramework;
import raf.draft.dsw.gui.swing.MainFrame;
import raf.draft.dsw.gui.swing.RoomView;
import raf.draft.dsw.gui.swing.jtree.model.DraftTreeItem;
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
public class DeleteState implements State {

    // Klikom na neki element, on se obriše
    @Override
    public void misKliknut(RoomView roomView, Point point) {

        if(!roomView.isNacrtanaSoba()){
            Message message = ApplicationFramework.getInstance().getMessageGenerator().generateMessage("Morate prvo da nacrtate sobu!", MessageType.WARNING, LocalDateTime.now());
            ApplicationFramework.getInstance().getMessageGenerator().notifySubscribers(new Notification(message));
            return;
        }

        // treba da proverimo listu selektovanih da li treba odatle nesto da brisemo
        if(!roomView.getSelectionList().isEmpty()) {
            for (int i=0; i<roomView.getSelectionList().size(); i++) {
                ElementPainter painter = roomView.getSelectionList().get(i);
                DraftTreeItem roomElement = MainFrame.getInstance().getDraftTree().findTreeItem(painter.getRoomElement());
                roomView.removePainter(painter);
                MainFrame.getInstance().getDraftTree().removeChild(roomElement);
            }
            roomView.getSelectionList().clear();
        }
        else {
            // prolazimo kroz paintere i ako se neki sece sa nasim pointom
            // tj ako smo na neki kliknuli treba da ga obrisemo

            point = roomView.transformMousePoint(point);

            for (int i = 0; i < roomView.getPainters().size(); i++) {
                ElementPainter elementPainter = roomView.getPainters().get(i);
                if (!(elementPainter instanceof RoomPainter) && elementPainter.elementAt(point, null)) {

                    // moramo da pronadjemo odgovarajuci RoomElement u JTree-u
                    // da bi ga izvukli kao tip DraftTreeItem
                    // i brisemo ga iz modela i JtreeView-a
                    DraftTreeItem roomElement = MainFrame.getInstance().getDraftTree().findTreeItem(elementPainter.getRoomElement());

                    // treba da obrisemo i odgovarajuci painter iz liste paintera Roomview-a
                    roomView.removePainter(elementPainter);

                    MainFrame.getInstance().getDraftTree().removeChild(roomElement);
                }
            }
            //System.out.println(roomView.getRoom().getNamestaj());
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
