package raf.draft.dsw.state.concrete;

import raf.draft.dsw.gui.swing.RoomView;
import raf.draft.dsw.painter.ElementPainter;
import raf.draft.dsw.state.State;

import java.awt.*;

public class RotateLeftState implements State {

    @Override
    public void misKliknut(RoomView roomView, Point point) {
        for(ElementPainter e: roomView.getSelectionList()){
            // jednom se rotira od prethodnog stanja
            e.getRoomElement().setRotateRatio(e.getRoomElement().getRotacija()-1);
        }
        roomView.repaint();
    }

    @Override
    public void misPomeren() {

    }

    @Override
    public void misScroll() {

    }

    @Override
    public void misZadrzan(RoomView roomView, Point point) {

    }

    @Override
    public void misPovucen(RoomView roomView, Point point) {

    }

    @Override
    public void misPusten(RoomView roomView, Point point) {

    }

}
