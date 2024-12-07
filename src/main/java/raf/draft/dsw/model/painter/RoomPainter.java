package raf.draft.dsw.model.painter;

import raf.draft.dsw.gui.swing.RoomView;
import raf.draft.dsw.model.structures.Room;
import raf.draft.dsw.model.structures.namestaj.RoomElement;

import javax.swing.*;
import java.awt.*;

public class RoomPainter implements ElementPainter{

    private Room room;

    // ne znam da li sme da ima ovo polje?
    private RoomView roomView;

    public RoomPainter(Room room, RoomView roomView) {
        this.room = room;
        this.roomView = roomView;
    }

    @Override
    public void paint(Graphics g) {

        if (room == null) {
            return;
        }

        Graphics2D g2d = (Graphics2D) g;
        g2d.setColor(room.getLineColor());
        g2d.setStroke(new BasicStroke(room.getLineThickness()));


        // Izračunaj faktor skaliranja
        roomView.calculateScalingFactor();
        double scalingFactor = roomView.getScalingFactor();

        // Dimenzije sobe u pikselima koristeći convertCmToPx
        int roomWidthPx = room.convertCmToPx(room.getSirina(), scalingFactor);
        int roomHeightPx = room.convertCmToPx(room.getDuzina(), scalingFactor);

        // Dimenzije panela
        int panelWidth = roomView.getWidth();
        int panelHeight = roomView.getHeight();

        // Pozicija za centriranje sobe u panelu
        int xStart = (panelWidth - roomWidthPx) / 2;
        int yStart = (panelHeight - roomHeightPx) / 2;


        // Nacrtaj pravougaonik
        g2d.drawRect(xStart, yStart, roomWidthPx, roomHeightPx);

    }

    @Override
    public boolean elementAt(Point point, Rectangle rectangle) {
        return false;
    }
}
