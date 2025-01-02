package raf.draft.dsw.commands.concrete;

import raf.draft.dsw.commands.AbstractCommand;
import raf.draft.dsw.gui.swing.MainFrame;
import raf.draft.dsw.gui.swing.RoomView;
import raf.draft.dsw.gui.swing.jtree.model.DraftTreeItem;
import raf.draft.dsw.model.structures.Room;
import raf.draft.dsw.model.structures.namestaj.Ormar;
import raf.draft.dsw.painter.OrmarPainter;

public class AddOrmarCommand extends AbstractCommand {

    private RoomView roomView;
    private Ormar ormar;
    private OrmarPainter ormarPainter;

    public AddOrmarCommand(RoomView roomView, Ormar ormar, OrmarPainter ormarPainter) {
        this.roomView = roomView;
        this.ormar = ormar;
        this.ormarPainter = ormarPainter;
    }

    @Override
    public void doCommand() {
        roomView.addPainter(ormarPainter);
        DraftTreeItem soba = MainFrame.getInstance().getDraftTree().findTreeItem(roomView.getRoom());
        MainFrame.getInstance().getDraftTree().addRoomChild(soba, ormar);
    }

    @Override
    public void undoCommand() {
        DraftTreeItem roomElement = MainFrame.getInstance().getDraftTree().findTreeItem(ormar);
        roomView.removePainter(ormarPainter);
        MainFrame.getInstance().getDraftTree().removeChild(roomElement);
        roomView.updateSelection();
    }
}
