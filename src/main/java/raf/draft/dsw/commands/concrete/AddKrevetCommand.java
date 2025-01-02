package raf.draft.dsw.commands.concrete;

import raf.draft.dsw.commands.AbstractCommand;
import raf.draft.dsw.gui.swing.MainFrame;
import raf.draft.dsw.gui.swing.RoomView;
import raf.draft.dsw.gui.swing.jtree.model.DraftTreeItem;
import raf.draft.dsw.model.structures.namestaj.Krevet;
import raf.draft.dsw.painter.KrevetPainter;

public class AddKrevetCommand extends AbstractCommand {

    private RoomView roomView;
    private Krevet krevet;
    private KrevetPainter krevetPainter;

    public AddKrevetCommand(RoomView roomView, Krevet krevet, KrevetPainter krevetPainter) {
        this.roomView = roomView;
        this.krevet = krevet;
        this.krevetPainter = krevetPainter;
    }

    @Override
    public void doCommand() {
        roomView.addPainter(krevetPainter);
        DraftTreeItem soba = MainFrame.getInstance().getDraftTree().findTreeItem(roomView.getRoom());
        MainFrame.getInstance().getDraftTree().addRoomChild(soba, krevet);
    }

    @Override
    public void undoCommand() {
        DraftTreeItem roomElement = MainFrame.getInstance().getDraftTree().findTreeItem(krevet);
        roomView.removePainter(krevetPainter);
        MainFrame.getInstance().getDraftTree().removeChild(roomElement);
        roomView.updateSelection();
    }
}
