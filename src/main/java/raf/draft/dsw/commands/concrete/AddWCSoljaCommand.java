package raf.draft.dsw.commands.concrete;

import raf.draft.dsw.commands.AbstractCommand;
import raf.draft.dsw.gui.swing.MainFrame;
import raf.draft.dsw.gui.swing.RoomView;
import raf.draft.dsw.gui.swing.jtree.model.DraftTreeItem;
import raf.draft.dsw.model.structures.namestaj.WCSolja;
import raf.draft.dsw.painter.WCSoljaPainter;

public class AddWCSoljaCommand extends AbstractCommand {

    private RoomView roomView;
    private WCSolja wcSolja;
    private WCSoljaPainter wcSoljaPainter;

    public AddWCSoljaCommand(RoomView roomView, WCSolja wcSolja, WCSoljaPainter wcSoljaPainter) {
        this.roomView = roomView;
        this.wcSolja = wcSolja;
        this.wcSoljaPainter = wcSoljaPainter;
    }

    @Override
    public void doCommand() {
        roomView.addPainter(wcSoljaPainter);
        DraftTreeItem soba = MainFrame.getInstance().getDraftTree().findTreeItem(roomView.getRoom());
        MainFrame.getInstance().getDraftTree().addRoomChild(soba, wcSolja);
    }

    @Override
    public void undoCommand() {
        DraftTreeItem roomElement = MainFrame.getInstance().getDraftTree().findTreeItem(wcSolja);
        roomView.removePainter(wcSoljaPainter);
        MainFrame.getInstance().getDraftTree().removeChild(roomElement);
        roomView.updateSelection();
    }
}
