package raf.draft.dsw.commands.concrete;

import raf.draft.dsw.commands.AbstractCommand;
import raf.draft.dsw.gui.swing.MainFrame;
import raf.draft.dsw.gui.swing.RoomView;
import raf.draft.dsw.gui.swing.jtree.model.DraftTreeItem;
import raf.draft.dsw.model.structures.Room;
import raf.draft.dsw.model.structures.namestaj.Sto;
import raf.draft.dsw.painter.StoPainter;

public class AddStoCommand extends AbstractCommand {

    private RoomView roomView;
    private Sto sto;
    private StoPainter stoPainter;

    public AddStoCommand(RoomView roomView, Sto sto, StoPainter stoPainter) {
        this.roomView = roomView;
        this.sto = sto;
        this.stoPainter = stoPainter;
    }

    @Override
    public void doCommand() {
        roomView.addPainter(stoPainter);
        DraftTreeItem soba = MainFrame.getInstance().getDraftTree().findTreeItem(roomView.getRoom());
        MainFrame.getInstance().getDraftTree().addRoomChild(soba, sto);
    }

    @Override
    public void undoCommand() {
        DraftTreeItem roomElement = MainFrame.getInstance().getDraftTree().findTreeItem(sto);
        roomView.removePainter(stoPainter);
        MainFrame.getInstance().getDraftTree().removeChild(roomElement);
        // u slucaju da smo selektovali element i onda kliknemo na UNDO
        // da se obrise i selekcioni pravougaonik
        roomView.updateSelection();
    }
}
