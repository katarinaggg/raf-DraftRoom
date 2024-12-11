package raf.draft.dsw.state.concrete;

import lombok.NoArgsConstructor;
import raf.draft.dsw.gui.swing.MainFrame;
import raf.draft.dsw.gui.swing.RoomView;
import raf.draft.dsw.gui.swing.jtree.model.DraftTreeItem;
import raf.draft.dsw.model.painter.ElementPainter;
import raf.draft.dsw.model.painter.RoomPainter;
import raf.draft.dsw.state.State;

import java.awt.*;

@NoArgsConstructor
public class DeleteState implements State {

    // Klikom na neki element, on se obriše
    @Override
    public void misKliknut(RoomView roomView, Point point) {

        // da li ovo treba na klik misem?

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
            //for(ElementPainter e : roomView.getPainters()){
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
