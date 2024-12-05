package raf.draft.dsw.state;

import raf.draft.dsw.gui.swing.RoomView;

import java.awt.*;

public interface State {
    public void misKliknut(RoomView roomView, Point point);
    public void misPomeren();
    public void misScroll();
}
