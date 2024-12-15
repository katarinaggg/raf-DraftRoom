package raf.draft.dsw.painter;

import raf.draft.dsw.gui.swing.RoomView;
import raf.draft.dsw.model.structures.namestaj.RoomElement;

import java.awt.*;
import java.awt.geom.GeneralPath;

public interface ElementPainter{

    public void paint(Graphics g);

    // trebace nam za selekciju
    // vrati true ako se point nalazi unutar nekog paintera
    public boolean elementAt(Point point, Rectangle rectangle);

    // trebace nam da izvucemo iz liste selektovanih paintera
    // koji smo tacno element selektovali (tj namestaj)
    public RoomElement getRoomElement();
    public GeneralPath getShape();
}
