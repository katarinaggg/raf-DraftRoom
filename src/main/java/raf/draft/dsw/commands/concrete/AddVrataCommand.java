package raf.draft.dsw.commands.concrete;

import raf.draft.dsw.commands.AbstractCommand;
import raf.draft.dsw.gui.swing.MainFrame;
import raf.draft.dsw.gui.swing.RoomView;
import raf.draft.dsw.gui.swing.jtree.model.DraftTreeItem;
import raf.draft.dsw.model.structures.namestaj.Vrata;
import raf.draft.dsw.painter.VrataPainter;

public class AddVrataCommand extends AbstractCommand {

    private RoomView roomView;
    private Vrata vrata;
    private VrataPainter vrataPainter;

    public AddVrataCommand(RoomView roomView, Vrata vrata, VrataPainter vrataPainter) {
        this.roomView = roomView;
        this.vrata = vrata;
        this.vrataPainter = vrataPainter;
    }

    @Override
    public void doCommand() {
        roomView.addPainter(vrataPainter);
        DraftTreeItem soba = MainFrame.getInstance().getDraftTree().findTreeItem(roomView.getRoom());
        MainFrame.getInstance().getDraftTree().addRoomChild(soba, vrata);
    }

    @Override
    public void undoCommand() {
        DraftTreeItem roomElement = MainFrame.getInstance().getDraftTree().findTreeItem(vrata);
        roomView.removePainter(vrataPainter);
        MainFrame.getInstance().getDraftTree().removeChild(roomElement);
        roomView.updateSelection();
    }
}
