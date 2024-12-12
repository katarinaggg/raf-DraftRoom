package raf.draft.dsw.gui.swing;

import raf.draft.dsw.controller.RoomMouseListener;
import raf.draft.dsw.controller.observer.ISubscriber;
import raf.draft.dsw.model.notification.Notification;
import raf.draft.dsw.painter.ElementPainter;
import raf.draft.dsw.model.structures.Room;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;

public class RoomView extends JPanel implements ISubscriber {

    private Room room;

    private boolean nacrtanaSoba = false;

    private double scalingFactor; // Faktor skaliranja

    // za zumiranje
    private double zoomLevel = 1.0;
    private double MAX_ZOOM = 3.0;
    private double MIN_ZOOM = 0.5;

    // lista Paintera koje treba da iscrta
    ArrayList<ElementPainter> painters = new ArrayList<>();

    //lista Paintera koje koristimo kada se nesto selektuje
    ArrayList<ElementPainter> selectionList = new ArrayList<>();

    private Point selectionRectangleLT;
    private Point selectionRectangleRB;

    private int offsetX = 0; // Horizontalni pomak sobe
    private int offsetY = 0; // Vertikalni pomak sobe

    public RoomView(Room room) {
        this.room = room;
        // dodajemo mu mouse listener
        //addMouseListener(new RoomMouseListener(this));
        RoomMouseListener mouseListener = new RoomMouseListener(this);
        addMouseListener(mouseListener); // Za MousePressed, MouseReleased itd.
        addMouseMotionListener(mouseListener); // Za MouseDragged i MouseMoved
        addMouseWheelListener(mouseListener);
    }

    // Metoda za određivanje faktora skaliranja
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

        //g2d.scale(zoomLevel, zoomLevel);

        // ZOOM IZ CENTRA PANELA
        // Dimenzije panela
        int panelWidth = getWidth();
        int panelHeight = getHeight();

        // Izračunavanje centra panela
        double centarX = panelWidth / 2.0;
        double centarY = panelHeight / 2.0;

        // Primena transformacije za zumiranje oko centra
        AffineTransform zoom = new AffineTransform();

        // Pomeramo tako da centar zumiranja ostane isti
        zoom.translate(centarX, centarY);
        zoom.scale(zoomLevel, zoomLevel);
        zoom.translate(-centarX, -centarY);

        g2d.transform(zoom);

        // Translacija za pomak sobe ovo je za movestate
        //g2d.translate(offsetX, offsetY);


        // Crtanje svih elemenata
        for (ElementPainter p : painters) {
            p.paint(g2d);
        }


        // Vraćanje translacije za dalji rad ovo je za movestate
        //g2d.translate(-offsetX, -offsetY);

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

    public void updateRoomOffset(int deltaX, int deltaY) {
        // Ažuriramo pomak sobe
        offsetX += deltaX;
        offsetY += deltaY;

        // Proveravamo granice panela kako bismo osigurali da soba ostane unutar njega
        int roomWidthPx = (int) (room.getSirina() * scalingFactor);
        int roomHeightPx = (int) (room.getDuzina() * scalingFactor);

        int panelWidth = getWidth();
        int panelHeight = getHeight();

        // Ograničavamo offset tako da soba ne izađe iz panela
        offsetX = Math.max(offsetX, panelWidth - roomWidthPx);
        offsetX = Math.min(offsetX, 0);

        offsetY = Math.max(offsetY, panelHeight - roomHeightPx);
        offsetY = Math.min(offsetY, 0);

        repaint(); // Osvežavamo prikaz
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

    public void setNacrtanaSoba(boolean nacrtanaSoba) {
        this.nacrtanaSoba = nacrtanaSoba;
    }

    public boolean isNacrtanaSoba() {
        return nacrtanaSoba;
    }

    // za zumiranje
    public double getZoomLevel() {
        return zoomLevel;
    }

    public double getMAX_ZOOM() {
        return MAX_ZOOM;
    }

    public double getMIN_ZOOM() {
        return MIN_ZOOM;
    }

    public void setZoomLevel(double zoomLevel) {
        this.zoomLevel = zoomLevel;
    }
}
