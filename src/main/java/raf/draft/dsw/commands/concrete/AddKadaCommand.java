package raf.draft.dsw.commands.concrete;

import raf.draft.dsw.commands.AbstractCommand;
import raf.draft.dsw.gui.swing.MainFrame;
import raf.draft.dsw.gui.swing.RoomView;
import raf.draft.dsw.gui.swing.jtree.model.DraftTreeItem;
import raf.draft.dsw.model.structures.namestaj.Kada;
import raf.draft.dsw.painter.KadaPainter;

public class AddKadaCommand extends AbstractCommand {

    private RoomView roomView;
    private Kada kada;
    private KadaPainter kadaPainter;

    public AddKadaCommand(RoomView roomView, Kada kada, KadaPainter kadaPainter){
        this.roomView = roomView;
        this.kada = kada;
        this.kadaPainter = kadaPainter;
    }

    @Override
    public void doCommand() {

        roomView.addPainter(kadaPainter);
        DraftTreeItem soba = MainFrame.getInstance().getDraftTree().findTreeItem(roomView.getRoom());
        MainFrame.getInstance().getDraftTree().addRoomChild(soba, kada);

    }

    @Override
    public void undoCommand() {
        DraftTreeItem roomElement = MainFrame.getInstance().getDraftTree().findTreeItem(kada);
        roomView.removePainter(kadaPainter);
        MainFrame.getInstance().getDraftTree().removeChild(roomElement);
        //roomView.repaint();
        roomView.updateSelection();
    }
}
