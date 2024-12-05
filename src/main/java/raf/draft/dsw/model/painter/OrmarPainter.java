package raf.draft.dsw.model.painter;

import raf.draft.dsw.model.structures.namestaj.Ormar;
import raf.draft.dsw.model.structures.namestaj.RoomElement;

import java.awt.*;

public class OrmarPainter implements ElementPainter{

    private Ormar ormar;

    public OrmarPainter(Ormar ormar) {
        this.ormar = ormar;
    }

    @Override
    public void paint(Graphics g) {

    }

    @Override
    public boolean elementAt(RoomElement element, Point point) {
        return false;
    }
}
