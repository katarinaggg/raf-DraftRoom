package raf.draft.dsw.model.painter;

import raf.draft.dsw.model.structures.namestaj.Krevet;
import raf.draft.dsw.model.structures.namestaj.RoomElement;

import java.awt.*;

public class KrevetPainter implements ElementPainter{

    private Krevet krevet;

    @Override
    public void paint(Graphics g) {

    }

    @Override
    public boolean elementAt(RoomElement element, Point point) {
        return false;
    }
}
