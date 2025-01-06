package raf.draft.dsw.gui.swing;

import com.sun.tools.javac.Main;
import raf.draft.dsw.commands.CommandManager;
import raf.draft.dsw.controller.RoomMouseListener;
import raf.draft.dsw.controller.observer.ISubscriber;
import raf.draft.dsw.model.notification.Notification;
import raf.draft.dsw.painter.ElementPainter;
import raf.draft.dsw.model.structures.Room;
import raf.draft.dsw.painter.RoomPainter;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.geom.AffineTransform;
import java.awt.geom.NoninvertibleTransformException;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;

public class RoomView extends JPanel implements ISubscriber {

    private Room room;

    private boolean nacrtanaSoba = false;

    private double scalingFactor; // Faktor skaliranja

    private CommandManager commandManager;

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
        this.commandManager = new CommandManager();

        // dodajemo mu mouse listener
        //addMouseListener(new RoomMouseListener(this));
        RoomMouseListener mouseListener = new RoomMouseListener(this);
        addMouseListener(mouseListener); // Za MousePressed, MouseReleased itd.
        addMouseMotionListener(mouseListener); // Za MouseDragged i MouseMoved
        addMouseWheelListener(mouseListener);

//        // dodajemo listener za promene velicine panela, ne znam menja li ovo nesto
//        addComponentListener(new ComponentAdapter() {
//            @Override
//            public void componentResized(ComponentEvent e) {
//                calculateScalingFactor();
//                repaint();
//            }
//        });
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
        System.out.println("FAKTOR SKALIRANJA: " +this.scalingFactor);
    }


//    public Rectangle getRoomBounds() {
//        // Dimenzije sobe u pikselima
//        int roomWidthPx = (int) (room.getSirina() * scalingFactor * zoomLevel);
//        int roomHeightPx = (int) (room.getDuzina() * scalingFactor * zoomLevel);
//
//        // Pozicija za centriranje sobe u panelu
//        int panelWidth = getWidth();
//        int panelHeight = getHeight();
//        int xStart = (panelWidth - roomWidthPx) / 2; // Centriranje po X
//        int yStart = (panelHeight - roomHeightPx) / 2; // Centriranje po Y
//
//        // Vraćamo pravougaonik koji predstavlja granice sobe
//        return new Rectangle(xStart, yStart, roomWidthPx, roomHeightPx);
//    }

    public Rectangle getRoomBounds() {
        // Dimenzije sobe u pikselima
        int roomWidthPx = (int) (room.getSirina() * scalingFactor * zoomLevel);
        int roomHeightPx = (int) (room.getDuzina() * scalingFactor * zoomLevel);

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
        int panelWidth = getWidth();
        int panelHeight = getHeight();

        // izracunavanje centra panela
        // treba nam da bi zoom bio sa fokusom u centru sobe
        double centarX = panelWidth / 2.0;
        double centarY = panelHeight / 2.0;

        // transformacija
        AffineTransform zoom = new AffineTransform();

        // pomeramo tako da centar zumiranja ostane isti
        zoom.translate(centarX, centarY);
        zoom.scale(zoomLevel, zoomLevel);
        zoom.translate(-centarX, -centarY);

        g2d.transform(zoom);

        //Translacija za pomak sobe ovo je za movestate
        g2d.translate(offsetX, offsetY);


        // Crtanje svih elemenata
        for (ElementPainter p : painters) {
            p.paint(g2d);
        }


        // Vraćanje translacije za dalji rad ovo je za movestate
        g2d.translate(-offsetX, -offsetY);

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

//      ne radi ovo
//    public void updateRoomOffset(int deltaX, int deltaY) {
//        // Ažuriramo pomak sobe
//        offsetX += deltaX;
//        offsetY += deltaY;
//
//        // Dinamične granice na osnovu skaliranja i zumiranja
//        int roomWidthPx = (int) (room.getSirina() * scalingFactor * zoomLevel);
//        int roomHeightPx = (int) (room.getDuzina() * scalingFactor * zoomLevel);
//
//        int panelWidth = getWidth();
//        int panelHeight = getHeight();
//
//        //  azurira granice pomeranja na osnovu dimenzija sobe i panela
//        // Ograničavamo pomeranje levo/desno
//        offsetX = Math.max(offsetX, Math.min(0, panelWidth - roomWidthPx));
//        offsetX = Math.min(offsetX, Math.max(0, panelWidth - roomWidthPx));
//
//        // Ograničavamo pomeranje gore/dole
//        offsetY = Math.max(offsetY, Math.min(0, panelHeight - roomHeightPx));
//        offsetY = Math.min(offsetY, Math.max(0, panelHeight - roomHeightPx));
//
//        repaint(); // Osvežavanje prikaza
//    }

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

    public int getOffsetX() {
        return offsetX;
    }

    public void setOffsetX(int offsetX) {
        this.offsetX = offsetX;
    }

    public int getOffsetY() {
        return offsetY;
    }

    public void setOffsetY(int offsetY) {
        this.offsetY = offsetY;
    }

    // kada zumiramo treba nam ona stara lokacija misem tj stvarna
    // ova metoda vraca tu stvarnu lokaciju misem kao da nismo u zoom modu
    // preko inverzne matrice
    // ovu novu tacku treba sada svuda da prosledimo gde postoji rad s misem
    // jer kad zumiramo bice nam potrebna ta nova lokacija misem da bi sve
    // opcije radile i u stanju zoom-a
    // u slucaju da je zoom=1 vratice se normalna lokacija misem

    public Point transformMousePoint(Point point) {

        // ako nismo zumiralki vraca normalan point
        if (zoomLevel == 1.0) {
            //System.out.println("Kliknuta tačka (bez zuma): " + point);
            //return new Point (point.x + offsetX, point.y + offsetY) ;
            return point;
        }

        try {
            // transformacija koja se desila pri zumiranju
            AffineTransform zoomTransform = new AffineTransform();

            // isto ko i u paintComponent kada se desava zoom
            double panelWidth = getWidth();
            double panelHeight = getHeight();
            double centerX = panelWidth / 2.0;
            double centerY = panelHeight / 2.0;
            zoomTransform.translate(centerX, centerY);
            zoomTransform.scale(zoomLevel, zoomLevel);
            zoomTransform.translate(-centerX, -centerY);

            // sada nam treba inverzna matrica ovoj za zumiranje
            // da bi iz nje dobili odgovarajucu lokaciju misa
            // a ne neku pomerenu zbog transformacije zumiranja
            AffineTransform inverseZoom = zoomTransform.createInverse();

            // trenutni klik transformisemo u taj novi koji nam treba iz pocetnog stanja (da nismo zumirali)
            Point2D transformedPoint2D = inverseZoom.transform(point, null);

            // tu novu tacku treba da sacuvamo da bi je vratili
            Point transformedPoint = new Point((int) transformedPoint2D.getX(), (int) transformedPoint2D.getY());

//            System.out.println("Kliknuta tačka: " + point);
//            System.out.println("Transformisana tačka (sa zumiranjem): " + transformedPoint);
//            System.out.println("Faktor skaliranja: " + scalingFactor);
//            System.out.println("ZoomLevel: " + zoomLevel);
//            System.out.println("RoomBounds: " + roomBounds);

            return transformedPoint;

        } catch (NoninvertibleTransformException e) {
            return point;
        }
    }

    public boolean preklapanjeElemenata(Shape novi) {
        // prolazimo kroz listu paintera u nasem roomView
        // i gledamo da li se neki shape sece da ovim novim
        // koji treba da dodamo
        for(ElementPainter painter : painters) {
            // treba da iskljucimo roomPainter iz provere
            // jer se svakako svaki element sece sa sobom
            Shape trenutni = painter.getShape();
            if(!(painter instanceof RoomPainter) && trenutni!=null && trenutni.intersects(novi.getBounds2D())){
                return true;
            }
        }
        return false;
    }


    // ne radi kod zumiranja????
    public boolean unutarSobe(int elementX, int elementY, int elementWidth, int elementHeight) {
        Rectangle roomBounds = getRoomBounds();
        // pravimo pravougaonik za element
        Rectangle2D elementBounds = new Rectangle2D.Double(elementX, elementY, elementWidth, elementHeight);
        // proveravamo da li se granice elementa nalaze unutar granica sobe
        return roomBounds.contains(elementBounds);
    }

    public void updateTranslate(int x, int y) {
        offsetX += x;
        offsetY += y;
    }

    public CommandManager getCommandManager() {
        return commandManager;
    }

    // za enable/disable dugmica na osnovu toga da li imamo komande za redo/undo
    public void enableUndoAction(){
        MainFrame.getInstance().getActionManager().getUndoAction().setEnabled(true);
    }

    public void disableUndoAction(){
        MainFrame.getInstance().getActionManager().getUndoAction().setEnabled(false);
    }

    public void enableRedoAction(){
        MainFrame.getInstance().getActionManager().getRedoAction().setEnabled(true);
    }

    public void disableRedoAction(){
        MainFrame.getInstance().getActionManager().getRedoAction().setEnabled(false);
    }

    public void updateSelection(){
        this.selectionRectangleLT = null;
        this.selectionRectangleRB = null;
        this.selectionList.clear();
        repaint();
    }

    public RoomPainter getRoomPainter(Room room) {
        RoomPainter painter = null;
        for(ElementPainter elementPainter : painters) {
            if(elementPainter instanceof RoomPainter) {
                painter = (RoomPainter) elementPainter;
            }
        }
        return painter;
    }
}
