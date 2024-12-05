package raf.draft.dsw.model.painter;

import raf.draft.dsw.model.structures.namestaj.RoomElement;
import raf.draft.dsw.model.structures.namestaj.Vrata;

import java.awt.*;

public class VrataPainter implements ElementPainter{

    private Vrata vrata;

    public VrataPainter(Vrata vrata) {
        this.vrata = vrata;
    }

    @Override
    public void paint(Graphics g) {

    }

    @Override
    public boolean elementAt(RoomElement element, Point point) {
        return false;
    }
}
