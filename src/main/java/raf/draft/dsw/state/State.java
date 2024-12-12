package raf.draft.dsw.state;

import raf.draft.dsw.gui.swing.RoomView;

import java.awt.*;
import java.awt.event.MouseWheelEvent;

public interface State {
    public void misKliknut(RoomView roomView, Point point);
    public void misPomeren();
    public void misScroll(RoomView roomView, MouseWheelEvent e);
    public void misZadrzan(RoomView roomView, Point point);
    public void misPovucen(RoomView roomView, Point point);
    public void misPusten(RoomView roomView, Point point);
}
