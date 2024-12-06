package raf.draft.dsw.state.concrete;

import raf.draft.dsw.gui.swing.DimensionFrame;
import raf.draft.dsw.gui.swing.MainFrame;
import raf.draft.dsw.gui.swing.RoomView;
import raf.draft.dsw.gui.swing.jtree.model.DraftTreeItem;
import raf.draft.dsw.model.painter.KadaPainter;
import raf.draft.dsw.model.painter.StoPainter;
import raf.draft.dsw.model.structures.namestaj.Kada;
import raf.draft.dsw.model.structures.namestaj.Sto;
import raf.draft.dsw.state.State;

import java.awt.*;

public class AddSto implements State {

    @Override
    public void misKliknut(RoomView roomView, Point point) {

        DimensionFrame frame = new DimensionFrame(roomView);
        frame.setVisible(true);

        Dimension dimenzija = frame.getDimenzija();

        Sto sto = new Sto(roomView.getRoom(),point, dimenzija);
        StoPainter stoPainter = new StoPainter(sto);
        roomView.addPainter(stoPainter);

        DraftTreeItem soba = MainFrame.getInstance().getDraftTree().findTreeItem(roomView.getRoom());
        MainFrame.getInstance().getDraftTree().addRoomChild(soba,sto);

    }

    @Override
    public void misPomeren() {

    }

    @Override
    public void misScroll() {

    }
}
