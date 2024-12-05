package raf.draft.dsw.model.painter;

import raf.draft.dsw.model.structures.namestaj.RoomElement;
import raf.draft.dsw.model.structures.namestaj.WCSolja;

import java.awt.*;

public class WCSoljaPainter implements ElementPainter{

    private WCSolja wcSolja;

    public WCSoljaPainter(WCSolja wcSolja) {
        this.wcSolja = wcSolja;
    }

    @Override
    public void paint(Graphics g) {

    }

    @Override
    public boolean elementAt(RoomElement element, Point point) {
        return false;
    }
}
