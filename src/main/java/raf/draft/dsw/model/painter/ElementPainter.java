package raf.draft.dsw.model.painter;

import raf.draft.dsw.model.structures.namestaj.RoomElement;

import java.awt.*;

public interface ElementPainter{

    public void paint(Graphics g);

    // trebace nam za selekciju
    // vrati true ako se point nalazi unutar nekog paintera
    public boolean elementAt(RoomElement element, Point point);
}
