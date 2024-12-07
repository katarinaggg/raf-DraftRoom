package raf.draft.dsw.controller;

import com.sun.tools.javac.Main;
import raf.draft.dsw.gui.swing.MainFrame;
import raf.draft.dsw.gui.swing.RoomView;

import java.awt.event.*;

public class RoomMouseListener implements MouseListener, MouseMotionListener, MouseWheelListener {

    // treba da ima polje odgovarajuce sobe
    // da bi znali gde crtamo
    private RoomView roomView;

    public RoomMouseListener(RoomView roomView) {
        this.roomView = roomView;
    }

    // nasi State-ovi ce da imaju kopije ovih metoda
    // svaki State radi nesto svoje na klik misem...

    //ova
    @Override
    public void mouseClicked(MouseEvent e) {
        // da li se ovako pristupa ProjectViewu ???
        MainFrame.getInstance().getProjectView().misKliknut(roomView,e.getPoint());
    }

    //ova
    @Override
    public void mouseMoved(MouseEvent e) {
        MainFrame.getInstance().getProjectView().misPomeren();
    }

    //ova
    @Override
    public void mouseWheelMoved(MouseWheelEvent e) {
        MainFrame.getInstance().getProjectView().misScroll();
    }

    @Override
    public void mousePressed(MouseEvent e) {
//ovo za selekciju
        MainFrame.getInstance().getProjectView().misZadrzan(roomView,e.getPoint());
    }


    @Override
    public void mouseDragged(MouseEvent e) {
        MainFrame.getInstance().getProjectView().misPovucen(roomView, e.getPoint());
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        MainFrame.getInstance().getProjectView().misPusten(roomView,e.getPoint());
    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }
}
