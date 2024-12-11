package raf.draft.dsw.gui.swing;

import raf.draft.dsw.controller.RoomMouseListener;
import raf.draft.dsw.controller.observer.ISubscriber;
import raf.draft.dsw.model.notification.Notification;
import raf.draft.dsw.model.painter.ElementPainter;
import raf.draft.dsw.model.structures.Room;
import raf.draft.dsw.model.structures.namestaj.RoomElement;
import raf.draft.dsw.state.concrete.SelectState;

import javax.swing.*;
import java.awt.*;
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
//    private Rectangle selectionRectangleLT = new Rectangle();
//    private Rectangle selectionRectangleRB = new Rectangle();


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
// ovo je dobror
//    @Override
//    public void paintComponent(Graphics g) {
//        super.paintComponent(g);
//
//        Graphics2D g2d = (Graphics2D) g;
//        //System.out.println("Usao u paintComponent");
//
//        //drawDashedRectangle(g2d, selectionRectangleLT, selectionRectangleRB);
//
//
////        // Definisanje isprekidanog stila linije
////        float[] dashPattern = {10, 10}; // Dužina crte i razmaka
////        BasicStroke dashedStroke = new BasicStroke(2.0f, BasicStroke.CAP_BUTT,
////                BasicStroke.JOIN_MITER, 10.0f, dashPattern, 0.0f);
////
////        g2d.setStroke(dashedStroke);
////        g2d.setColor(Color.BLUE);
////
////        ///nesto za selekciju, mozda nije dobro
////        g.drawRect(selectionRectangleLT.x, selectionRectangleRB.y, selectionRectangleLT.width, selectionRectangleRB.height);
//
//        if(MainFrame.getInstance().getProjectView().trenutnoStanje() instanceof SelectState){
//            drawDashedRectangle(g2d,selectionRectangleLT,selectionRectangleRB);
//        }
//        for(ElementPainter p : painters) {
//            p.paint(g2d);
//            //System.out.println("Usao u paintere");
//        }
//    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;

        // Crtanje svih elemenata
        for (ElementPainter p : painters) {
            p.paint(g2d);
        }

        // Ako postoji tačno jedan selektovani element
        if (selectionList.size() == 1) {
            ElementPainter selectedPainter = selectionList.get(0); // Prvi selektovani element
            Shape shape = selectedPainter.getShape(); // Dobijamo precizan oblik selektovanog elementa
            Rectangle2D bounds = shape.getBounds2D();
            g2d.setColor(Color.BLUE);
            g2d.setStroke(new BasicStroke(3));

            // Nacrtaj pravougaonik
            g2d.draw(bounds);

            //int donjiX = (int) (bounds.getCenterX() + bounds.getWidth()/2);
            //int donjiY = (int) (bounds.getCenterY() + bounds.getHeight()/2);
            int donjiX = (int) bounds.getMaxX(); // x koordinata DONJE DESNE IVICE
            int gornjiY = (int) bounds.getMaxY(); // y koordinata DONJE DESNE IVICE

            ///ovo ako hocu da uokvirim ivice oblika
//            if (shape != null) {
//                g2d.setColor(Color.BLUE); // Postavi boju za ivicu
//                g2d.setStroke(new BasicStroke(3)); // Postavi debljinu ivice
//                g2d.draw(shape); // Nacrtaj oblik selektovanog elementa
//            }
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

//    public void drawDashedRectangle(Graphics2D g2d, Point point1, Point point2) {
//        // Izračunavanje gornje leve tačke i dimenzija
//        int x = Math.min(point1.x, point2.x); // Manji X
//        int y = Math.min(point1.y, point2.y); // Manji Y
//        int width = Math.abs(point2.x - point1.x); // Širina pravougaonika
//        int height = Math.abs(point2.y - point1.y); // Visina pravougaonika
//
//        // Definisanje isprekidanog stila linije
//        float[] dashPattern = {10, 10}; // Dužina crte i razmaka
//        BasicStroke dashedStroke = new BasicStroke(2.0f,        // Debljina linije
//                BasicStroke.CAP_BUTT,      // Oblik kraja linije
//                BasicStroke.JOIN_MITER,    // Stil spojeva
//                10.0f,                     // Miter limit
//                dashPattern,               // Šablon isprekidanih linija
//                0.0f);                     // Početna faza
//
//        // Postavljanje isprekidanog stila
//        g2d.setStroke(dashedStroke);
//        g2d.setColor(Color.BLUE);
//
//        // Crtanje pravougaonika
//        g2d.drawRect(x, y, width, height);
//    }

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


}
