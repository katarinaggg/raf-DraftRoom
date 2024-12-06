package raf.draft.dsw.state.concrete;

import raf.draft.dsw.gui.swing.DimensionFrame;
import raf.draft.dsw.gui.swing.MainFrame;
import raf.draft.dsw.gui.swing.RoomView;
import raf.draft.dsw.gui.swing.jtree.model.DraftTreeItem;
import raf.draft.dsw.model.painter.KadaPainter;
import raf.draft.dsw.model.painter.OrmarPainter;
import raf.draft.dsw.model.structures.namestaj.Kada;
import raf.draft.dsw.model.structures.namestaj.Ormar;
import raf.draft.dsw.state.State;

import java.awt.*;

public class AddOrmar implements State {
    @Override
    public void misKliknut(RoomView roomView, Point point) {

        DimensionFrame frame = new DimensionFrame(roomView);
        frame.setVisible(true);

        Dimension dimenzija = frame.getDimenzija();

        Ormar ormar = new Ormar(roomView.getRoom(),point, dimenzija);
        OrmarPainter ormarPainter = new OrmarPainter(ormar);
        roomView.addPainter(ormarPainter);

        DraftTreeItem soba = MainFrame.getInstance().getDraftTree().findTreeItem(roomView.getRoom());
        MainFrame.getInstance().getDraftTree().addRoomChild(soba,ormar);

    }

    @Override
    public void misPomeren() {

    }

    @Override
    public void misScroll() {

    }
}
