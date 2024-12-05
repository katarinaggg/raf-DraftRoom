package raf.draft.dsw.model.painter;

import raf.draft.dsw.model.structures.namestaj.Lavabo;
import raf.draft.dsw.model.structures.namestaj.RoomElement;

import java.awt.*;

public class LavaboPainter implements ElementPainter{

    private Lavabo lavabo;

    @Override
    public void paint(Graphics g) {

    }

    @Override
    public boolean elementAt(RoomElement element, Point point) {
        return false;
    }
}
