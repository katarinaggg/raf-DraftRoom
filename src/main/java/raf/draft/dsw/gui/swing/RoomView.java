package raf.draft.dsw.gui.swing;

import raf.draft.dsw.model.painter.ElementPainter;
import raf.draft.dsw.model.structures.Room;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseListener;
import java.util.ArrayList;

public class RoomView extends JPanel {
    private Room room;
    private double scalingFactor; // Faktor skaliranja

    // lista Paintera koje treba da iscrta
    ArrayList<ElementPainter> painters = new ArrayList<>();

    public RoomView(Room room) {
        this.room = room;
    }

    // Metoda za određivanje faktora skaliranja
    private void calculateScalingFactor() {
        // Uzimamo dimenzije panela
        int panelWidth = getWidth();
        int panelHeight = getHeight();

        // Dimenzije sobe u pikselima
        int roomSirinaPx = (int) room.getSirina();
        int roomVisinaPx = (int) room.getDuzina();

        // Pronađi najmanji faktor skaliranja koji odgovara dimenzijama panela
        scalingFactor = Math.min((double) panelWidth / roomSirinaPx, (double) panelHeight / roomVisinaPx);
    }

    @Override
    public void paintComponents(Graphics g) {
        super.paintComponents(g);
        Graphics2D g2d = (Graphics2D) g;

    }

    public void addPainter(ElementPainter painter) {
        painters.add(painter);
    }

    public ArrayList<ElementPainter> getPainters() {
        return painters;
    }
}
