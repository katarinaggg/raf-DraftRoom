package raf.draft.dsw.state.concrete;

import lombok.NoArgsConstructor;
import raf.draft.dsw.gui.swing.DimensionFrame;
import raf.draft.dsw.gui.swing.RoomView;
import raf.draft.dsw.model.painter.ElementPainter;
import raf.draft.dsw.model.painter.RoomPainter;
import raf.draft.dsw.model.structures.Room;
import raf.draft.dsw.state.State;

import java.awt.*;

@NoArgsConstructor
public class EditRoomState implements State {


    // Klikom na prazan RoomView, iskače prozorčić da se unesu dimenzije sobe
    @Override
    public void misKliknut(RoomView roomView, Point point) {
        DimensionFrame frame = new DimensionFrame(roomView);
        frame.setVisible(true);
    }

    @Override
    public void misPomeren() {

    }

    @Override
    public void misScroll() {

    }
}
