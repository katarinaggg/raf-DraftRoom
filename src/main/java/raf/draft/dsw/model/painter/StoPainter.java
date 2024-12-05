package raf.draft.dsw.model.painter;

import raf.draft.dsw.model.structures.namestaj.RoomElement;
import raf.draft.dsw.model.structures.namestaj.Sto;

import java.awt.*;

public class StoPainter implements ElementPainter{

    private Sto sto;

    @Override
    public void paint(Graphics g) {

    }

    @Override
    public boolean elementAt(RoomElement element, Point point) {
        return false;
    }
}
