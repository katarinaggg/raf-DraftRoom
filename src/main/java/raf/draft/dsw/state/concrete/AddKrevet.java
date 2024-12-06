package raf.draft.dsw.state.concrete;

import raf.draft.dsw.gui.swing.DimensionFrame;
import raf.draft.dsw.gui.swing.MainFrame;
import raf.draft.dsw.gui.swing.RoomView;
import raf.draft.dsw.gui.swing.jtree.model.DraftTreeItem;
import raf.draft.dsw.model.painter.KadaPainter;
import raf.draft.dsw.model.painter.KrevetPainter;
import raf.draft.dsw.model.structures.namestaj.Kada;
import raf.draft.dsw.model.structures.namestaj.Krevet;
import raf.draft.dsw.state.State;

import java.awt.*;

public class AddKrevet implements State {


    @Override
    public void misKliknut(RoomView roomView, Point point) {

        DimensionFrame frame = new DimensionFrame(roomView);
        frame.setVisible(true);

        Dimension dimenzija = frame.getDimenzija();

        Krevet krevet = new Krevet(roomView.getRoom(),point, dimenzija);
        KrevetPainter krevetPainter = new KrevetPainter(krevet);
        roomView.addPainter(krevetPainter);

        DraftTreeItem soba = MainFrame.getInstance().getDraftTree().findTreeItem(roomView.getRoom());
        MainFrame.getInstance().getDraftTree().addRoomChild(soba,krevet);

    }

    @Override
    public void misPomeren() {

    }

    @Override
    public void misScroll() {

    }
}
