package raf.draft.dsw.gui.swing;

import raf.draft.dsw.controller.RoomMouseListener;
import raf.draft.dsw.controller.observer.ISubscriber;
import raf.draft.dsw.model.notification.Notification;
import raf.draft.dsw.model.painter.ElementPainter;
import raf.draft.dsw.model.painter.RoomPainter;
import raf.draft.dsw.model.structures.Room;
import raf.draft.dsw.model.structures.namestaj.RoomElement;
import raf.draft.dsw.state.concrete.SelectState;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.GeneralPath;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;

public class RoomView extends JPanel implements ISubscriber {

    private Room room;

    private double scalingFactor; // Faktor skaliranja

    // lista Paintera koje treba da iscrta
    ArrayList<ElementPainter> painters = new ArrayList<>();

    //lista Paintera koje koristimo kada se nesto selektuje
    ArrayList<ElementPainter> selectionList = new ArrayList<>();

    private Point selectionRectangleLT;
    private Point selectionRectangleRB;

    public RoomView(Room room) {
        this.room = room;
        // dodajemo mu mouse listener
        //addMouseListener(new RoomMouseListener(this));
        RoomMouseListener mouseListener = new RoomMouseListener(this);
        addMouseListener(mouseListener); // Za MousePressed, MouseReleased itd.
        addMouseMotionListener(mouseListener); // Za MouseDragged i MouseMoved
    }

    public void calculateScalingFactor() {
        // Uzimamo dimenzije panela
        int panelWidth = getWidth();
        int panelHeight = getHeight();

        // Pretvaranje dimenzija sobe iz cm u px
        int roomWidthPx = room.convertCmToPx(room.getSirina(), 1); // Faktor 1 jer se skaliranje računa kasnije
        int roomHeightPx = room.convertCmToPx(room.getDuzina(), 1);

        // Pronađi najmanji faktor skaliranja koji odgovara dimenzijama panela
        scalingFactor = Math.min((double) panelWidth / roomWidthPx, (double) panelHeight / roomHeightPx);
    }


    public Rectangle getRoomBounds() {
        // Dimenzije sobe u pikselima
        int roomWidthPx = (int) (room.getSirina() * scalingFactor);
        int roomHeightPx = (int) (room.getDuzina() * scalingFactor);

        // Pozicija za centriranje sobe u panelu
        int panelWidth = getWidth();
        int panelHeight = getHeight();
        int xStart = (panelWidth - roomWidthPx) / 2; // Centriranje po X
        int yStart = (panelHeight - roomHeightPx) / 2; // Centriranje po Y

        // Vraćamo pravougaonik koji predstavlja granice sobe
        return new Rectangle(xStart, yStart, roomWidthPx, roomHeightPx);
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;

//        Rectangle roomBounds = getRoomBounds(); // Dobijamo granice sobe
////        g2d.setColor(Color.RED); // Postavljamo boju za granice sobe
////        g2d.drawRect(roomBounds.x, roomBounds.y, roomBounds.width, roomBounds.height); // Crtamo pravougaonik


        // Crtanje svih elemenata
        for (ElementPainter p : painters) {
            p.paint(g2d);
        }

        // Crtanje selektovanih elemenata (ako ih ima) ovo mozda treba da se obrise
        if (!selectionList.isEmpty()) {
            for (ElementPainter selectedPainter : selectionList) {
                Shape shape = selectedPainter.getShape();
                if (shape != null) {
                    Rectangle2D bounds = shape.getBounds2D();
                    g2d.setColor(Color.BLUE);
                    g2d.setStroke(new BasicStroke(3));
                    g2d.draw(bounds);
                }
            }
        }

        // Crtanje isprekidanog pravougaonika za selekciju (ako je potrebno)
        if (selectionRectangleLT != null && selectionRectangleRB != null) {
            int x = Math.min(selectionRectangleLT.x, selectionRectangleRB.x);
            int y = Math.min(selectionRectangleLT.y, selectionRectangleRB.y);
            int width = Math.abs(selectionRectangleRB.x - selectionRectangleLT.x);
            int height = Math.abs(selectionRectangleRB.y - selectionRectangleLT.y);

            if (width > 0 && height > 0) {
                drawDashedRectangle(g2d, x, y, width, height);
            }
        }
    }

    public void addPainter(ElementPainter painter) {
        painters.add(painter);
        //System.out.println("Dodat painter"+painter.getClass().getName());
    }

    public void drawDashedRectangle(Graphics2D g2d, int x, int y, int width, int height) {
        // Definisanje isprekidanog stila linije
        float[] dashPattern = {10, 10}; // Dužina crte i razmaka
        BasicStroke dashedStroke = new BasicStroke(2.0f,        // Debljina linije
                BasicStroke.CAP_BUTT,      // Oblik kraja linije
                BasicStroke.JOIN_MITER,    // Stil spojeva
                10.0f,                     // Miter limit
                dashPattern,               // Šablon isprekidanih linija
                0.0f);                     // Početna faza

        // Postavljanje isprekidanog stila
        g2d.setStroke(dashedStroke);
        g2d.setColor(Color.BLUE);

        // Crtanje pravougaonika
        g2d.drawRect(x, y, width, height);
    }


    @Override
    public void update(Notification notification) {
        repaint();
    }
    public void removePainter(ElementPainter painter) {
        painters.remove(painter);
    }

    public Point getSelectionRectangleLT() {
        return selectionRectangleLT;
    }

    public void setSelectionRectangleLT(Point selectionRectangleLT) {
        this.selectionRectangleLT = selectionRectangleLT;
    }

    public Point getSelectionRectangleRB() {
        return selectionRectangleRB;
    }

    public void setSelectionRectangleRB(Point selectionRectangleRB) {
        this.selectionRectangleRB = selectionRectangleRB;
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

    public ArrayList<ElementPainter> getSelectionList() {
        return selectionList;
    }
}
