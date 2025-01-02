package raf.draft.dsw.commands.concrete;

import com.sun.tools.javac.Main;
import raf.draft.dsw.commands.AbstractCommand;
import raf.draft.dsw.gui.swing.MainFrame;
import raf.draft.dsw.gui.swing.RoomView;
import raf.draft.dsw.gui.swing.jtree.model.DraftTreeItem;
import raf.draft.dsw.model.structures.namestaj.RoomElement;
import raf.draft.dsw.painter.ElementPainter;
import raf.draft.dsw.painter.RoomPainter;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class DeleteCommand extends AbstractCommand {

    private RoomView roomView;
    private Point point;

    // da bi radio UNDO tj da bi se ponovo iscrtali elementi koje smo obrisali
    // treba da ih cuvamo u nekoj listi
    // u UNDO treba da ih ponovo iscrtamo i dodamo u stablo
    List<ElementPainter> obrisaniPainteri = new ArrayList<>();


    public DeleteCommand(RoomView roomView, Point point) {
        this.roomView = roomView;
        this.point = point;
    }

    // logika koja je bila u DeleteState-u
    @Override
    public void doCommand() {

        // svaki put posle undo/redo izgubicemo listu selektovanih
        // tada treba da se brise ono sto je do tad bilo selektovano i brisano
        if(!obrisaniPainteri.isEmpty()){
            for(int i=0;i<obrisaniPainteri.size();i++){
                ElementPainter elementPainter = obrisaniPainteri.get(i);
                DraftTreeItem roomElement = MainFrame.getInstance().getDraftTree().findTreeItem(elementPainter.getRoomElement());
                roomView.removePainter(elementPainter);
                MainFrame.getInstance().getDraftTree().removeChild(roomElement);
            }
        }

        // prvi put ce se brisati selektovani

        // treba da proverimo listu selektovanih da li treba odatle nesto da brisemo
        if(!roomView.getSelectionList().isEmpty()) {
            //zaBrisanje.addAll(roomView.getSelectionList());
            for (int i=0; i<roomView.getSelectionList().size(); i++) {
                ElementPainter painter = roomView.getSelectionList().get(i);
                DraftTreeItem roomElement = MainFrame.getInstance().getDraftTree().findTreeItem(painter.getRoomElement());

                // cuvamo elemente pre brisanja
                this.obrisaniPainteri.add(painter);

                roomView.removePainter(painter);
                MainFrame.getInstance().getDraftTree().removeChild(roomElement);
            }
            roomView.getSelectionList().clear();
        }
        else {
            // prolazimo kroz paintere i ako se neki sece sa nasim pointom
            // tj ako smo na neki kliknuli treba da ga obrisemo

            for (int i = 0; i < roomView.getPainters().size(); i++) {
                ElementPainter elementPainter = roomView.getPainters().get(i);
                if (!(elementPainter instanceof RoomPainter) && elementPainter.elementAt(point, null)) {

                    //zaBrisanje.add(elementPainter);

                    // moramo da pronadjemo odgovarajuci RoomElement u JTree-u
                    // da bi ga izvukli kao tip DraftTreeItem
                    // i brisemo ga iz modela i JtreeView-a
                    DraftTreeItem roomElement = MainFrame.getInstance().getDraftTree().findTreeItem(elementPainter.getRoomElement());

                    // cuvamo element pre brisanja
                    this.obrisaniPainteri.add(elementPainter);

                    // treba da obrisemo i odgovarajuci painter iz liste paintera Roomview-a
                    roomView.removePainter(elementPainter);

                    MainFrame.getInstance().getDraftTree().removeChild(roomElement);
                }
            }
        }
        roomView.updateSelection();
    }

    // inverzna funkcija brisanju je ponovno iscrtavanje onoga sto smo obrisali
    @Override
    public void undoCommand() {
        // prolazimo kroz listu onoga sto smo prethodno obrisali
        // i treba ponovo da sve to iscrtamo i dodamo u stablo
        for(int i=0; i<obrisaniPainteri.size(); i++) {
            ElementPainter elementPainter = obrisaniPainteri.get(i);
            RoomElement roomElement = elementPainter.getRoomElement();

            roomView.addPainter(elementPainter);
            DraftTreeItem soba = MainFrame.getInstance().getDraftTree().findTreeItem(roomView.getRoom());
            MainFrame.getInstance().getDraftTree().addRoomChild(soba,roomElement);
        }

        // sada brisemo listu obrisanih elemenata jer smo ih vratili
        //this.obrisaniPainteri.clear();
        roomView.updateSelection();
    }
}
