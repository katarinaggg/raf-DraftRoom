package raf.draft.dsw.gui.swing;

import raf.draft.dsw.controller.RoomMouseListener;
import raf.draft.dsw.controller.observer.ISubscriber;
import raf.draft.dsw.model.notification.Notification;
import raf.draft.dsw.model.painter.ElementPainter;
import raf.draft.dsw.model.structures.Room;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseListener;
import java.util.ArrayList;

public class RoomView extends JPanel implements ISubscriber {

    private Room room;

    private double scalingFactor; // Faktor skaliranja

    // lista Paintera koje treba da iscrta
    ArrayList<ElementPainter> painters = new ArrayList<>();

    public RoomView(Room room) {
        this.room = room;
        // dodajemo mu mouse listener
        addMouseListener(new RoomMouseListener(this));
    }

    // Metoda za određivanje faktora skaliranja
    public void calculateScalingFactor() {

        // Uzimamo dimenzije panela
        int panelWidth = getWidth();
        int panelHeight = getHeight();

        // Dimenzije sobe u pikselima
        int roomSirinaPx = (int) room.getSirina();
        int roomVisinaPx = (int) room.getDuzina();

        // Pronađi najmanji faktor skaliranja koji odgovara dimenzijama panela
        scalingFactor = Math.min((double) panelWidth / roomSirinaPx, (double) panelHeight / roomVisinaPx);
        //System.out.println("Scaling factor: " + scalingFactor);
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2d = (Graphics2D) g;
        //System.out.println("Usao u paintComponent");


        for(ElementPainter p : painters) {
            p.paint(g2d);
            //System.out.println("Usao u paintere");
        }

    }

    public void addPainter(ElementPainter painter) {
        painters.add(painter);
        //System.out.println("Dodat painter"+painter.getClass().getName());
    }

    public ArrayList<ElementPainter> getPainters() {
        return painters;
    }

    public Room getRoom() {
        return room;
    }

    public double getScalingFactor() {
        return scalingFactor;
    }

    @Override
    public void update(Notification notification) {
        repaint();
    }
}
