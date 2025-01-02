package raf.draft.dsw.commands.concrete;

import raf.draft.dsw.commands.AbstractCommand;
import raf.draft.dsw.core.ApplicationFramework;
import raf.draft.dsw.gui.swing.DimensionFrame;
import raf.draft.dsw.gui.swing.MainFrame;
import raf.draft.dsw.gui.swing.RoomView;
import raf.draft.dsw.gui.swing.jtree.model.DraftTreeItem;
import raf.draft.dsw.model.notification.Message;
import raf.draft.dsw.model.notification.MessageType;
import raf.draft.dsw.model.notification.Notification;
import raf.draft.dsw.model.structures.namestaj.Bojler;
import raf.draft.dsw.painter.BojlerPainter;
import raf.draft.dsw.painter.ElementPainter;

import java.awt.*;
import java.time.LocalDateTime;

public class AddBojlerCommand extends AbstractCommand {

    private RoomView roomView;
    private Bojler bojler;
    private BojlerPainter bojlerPainter;

    public AddBojlerCommand(RoomView roomView, Bojler bojler, BojlerPainter bojlerPainter) {
        this.roomView = roomView;
        this.bojler = bojler;
        this.bojlerPainter = bojlerPainter;
    }

    @Override
    public void doCommand() {

            // dodajemo painter na roomView
            roomView.addPainter(bojlerPainter);

            // treba dodati bojler u draftTree
            // na Room tipa DTI treba da se doda dete

            // moramo da pronadjemo odgovarajucu sobu u JTree-u
            DraftTreeItem soba = MainFrame.getInstance().getDraftTree().findTreeItem(roomView.getRoom());

            MainFrame.getInstance().getDraftTree().addRoomChild(soba,bojler);

            // Room obavestava RoomView (u addChild metodi) da treba da se repaintuje

    }

    // inverzna funkcija je brisanje bojlera
    @Override
    public void undoCommand() {
        // posto cuvamo odgovarajucu instancu bojlera samo nju treba da izbrisemo
        DraftTreeItem roomElement = MainFrame.getInstance().getDraftTree().findTreeItem(bojler);
        // brisemo painter
        roomView.removePainter(bojlerPainter);
        // brisemo iz modela
        MainFrame.getInstance().getDraftTree().removeChild(roomElement);

        // ne mora ovde da se zove repaint kad svakako u removeChild() Room ce obavestiti
        // subscribere da je doslo do promene pa ce se RoomView repaintovati u update
        //roomView.repaint();
        roomView.updateSelection();
    }
}
