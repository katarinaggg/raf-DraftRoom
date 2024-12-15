package raf.draft.dsw.painter;

import raf.draft.dsw.gui.swing.RoomView;
import raf.draft.dsw.model.structures.Room;
import raf.draft.dsw.model.structures.namestaj.RoomElement;

import java.awt.*;
import java.awt.geom.GeneralPath;
import java.awt.geom.Rectangle2D;

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

    @Override
    public RoomElement getRoomElement() {
        return null;
    }

    @Override
    public GeneralPath getShape() {
        if (room == null || roomView == null) {
            return null;
        }

//        double scalingFactor = roomView.getScalingFactor();
//        int roomWidthPx = room.convertCmToPx(room.getSirina(), scalingFactor);
//        int roomHeightPx = room.convertCmToPx(room.getDuzina(), scalingFactor);
//
//        GeneralPath path = new GeneralPath();
//        path.append(new Rectangle2D.Double(0, 0, roomWidthPx, roomHeightPx), false);
//
//        return path;
        double scalingFactor = roomView.getScalingFactor();
        int roomWidthPx = room.convertCmToPx(room.getSirina(), scalingFactor);
        int roomHeightPx = room.convertCmToPx(room.getDuzina(), scalingFactor);

        // Dimenzije panela
        int panelWidth = roomView.getWidth();
        int panelHeight = roomView.getHeight();

        // Pozicija za centriranje sobe u panelu
        int xStart = (panelWidth - roomWidthPx) / 2;
        int yStart = (panelHeight - roomHeightPx) / 2;

        // Pravougaonik sobe centriran u panelu
        GeneralPath path = new GeneralPath();
        path.append(new Rectangle2D.Double(xStart, yStart, roomWidthPx, roomHeightPx), false);

        return path;
    }

}
