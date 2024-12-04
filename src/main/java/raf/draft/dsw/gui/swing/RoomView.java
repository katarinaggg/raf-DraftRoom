package raf.draft.dsw.gui.swing;

import raf.draft.dsw.model.structures.Room;

import javax.swing.*;
import java.awt.event.MouseListener;

public class RoomView extends JPanel {
    private Room room;
    private double scalingFactor; // Faktor skaliranja

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

}
