package raf.draft.dsw.model.painter;

import raf.draft.dsw.model.structures.namestaj.Bojler;
import raf.draft.dsw.model.structures.namestaj.RoomElement;

import java.awt.*;

public class BojlerPainter implements ElementPainter{

    private Bojler bojler;

    public BojlerPainter(Bojler bojler) {
        this.bojler = bojler;
    }

    @Override
    public void paint(Graphics g) {

    }

    @Override
    public boolean elementAt(RoomElement element, Point point) {
        return false;
    }
}
