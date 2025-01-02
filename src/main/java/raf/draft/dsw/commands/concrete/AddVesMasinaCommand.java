package raf.draft.dsw.commands.concrete;

import raf.draft.dsw.commands.AbstractCommand;
import raf.draft.dsw.gui.swing.MainFrame;
import raf.draft.dsw.gui.swing.RoomView;
import raf.draft.dsw.gui.swing.jtree.model.DraftTreeItem;
import raf.draft.dsw.model.structures.namestaj.VesMasina;
import raf.draft.dsw.painter.VesMasinaPainter;

public class AddVesMasinaCommand extends AbstractCommand {

    private RoomView roomView;
    private VesMasina vesMasina;
    private VesMasinaPainter vesMasinaPainter;

    public AddVesMasinaCommand(RoomView roomView, VesMasina vesMasina, VesMasinaPainter vesMasinaPainter) {
        this.roomView = roomView;
        this.vesMasina = vesMasina;
        this.vesMasinaPainter = vesMasinaPainter;
    }

    @Override
    public void doCommand() {
        roomView.addPainter(vesMasinaPainter);
        DraftTreeItem soba = MainFrame.getInstance().getDraftTree().findTreeItem(roomView.getRoom());
        MainFrame.getInstance().getDraftTree().addRoomChild(soba, vesMasina);
    }

    @Override
    public void undoCommand() {
        DraftTreeItem roomElement = MainFrame.getInstance().getDraftTree().findTreeItem(vesMasina);
        roomView.removePainter(vesMasinaPainter);
        MainFrame.getInstance().getDraftTree().removeChild(roomElement);
        roomView.updateSelection();
    }
}
