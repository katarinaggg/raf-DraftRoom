package raf.draft.dsw.commands.concrete;

import raf.draft.dsw.commands.AbstractCommand;
import raf.draft.dsw.gui.swing.MainFrame;
import raf.draft.dsw.gui.swing.RoomView;
import raf.draft.dsw.gui.swing.jtree.model.DraftTreeItem;
import raf.draft.dsw.model.structures.namestaj.Lavabo;
import raf.draft.dsw.painter.LavaboPainter;

public class AddLavaboCommand extends AbstractCommand {

    private RoomView roomView;
    private Lavabo lavabo;
    private LavaboPainter lavaboPainter;

    public AddLavaboCommand(RoomView roomView, Lavabo lavabo, LavaboPainter lavaboPainter) {
        this.roomView = roomView;
        this.lavabo = lavabo;
        this.lavaboPainter = lavaboPainter;
    }

    @Override
    public void doCommand() {
        roomView.addPainter(lavaboPainter);
        DraftTreeItem soba = MainFrame.getInstance().getDraftTree().findTreeItem(roomView.getRoom());
        MainFrame.getInstance().getDraftTree().addRoomChild(soba, lavabo);
    }

    @Override
    public void undoCommand() {
        DraftTreeItem roomElement = MainFrame.getInstance().getDraftTree().findTreeItem(lavabo);
        roomView.removePainter(lavaboPainter);
        MainFrame.getInstance().getDraftTree().removeChild(roomElement);
        roomView.updateSelection();
    }
}
