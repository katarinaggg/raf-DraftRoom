package raf.draft.dsw.model.painter;

import raf.draft.dsw.model.structures.namestaj.Kada;
import raf.draft.dsw.model.structures.namestaj.RoomElement;

import java.awt.*;

public class KadaPainter implements ElementPainter{

    private Kada kada;

    public KadaPainter(Kada kada) {
        this.kada = kada;
    }

    @Override
    public void paint(Graphics g) {

    }

    @Override
    public boolean elementAt(RoomElement element, Point point) {
        return false;
    }
}
