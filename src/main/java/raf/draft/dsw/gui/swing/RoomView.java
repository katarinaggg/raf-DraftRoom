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

    //lista Paintera koje koristimo kada se nesto selektuje
    ArrayList<ElementPainter> selectionList = new ArrayList<>();
    private Rectangle selectionRectangleLT = new Rectangle();
    private Rectangle selectionRectangleRB = new Rectangle();

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


        // Definisanje isprekidanog stila linije
        float[] dashPattern = {10, 10}; // Dužina crte i razmaka
        BasicStroke dashedStroke = new BasicStroke(2.0f, BasicStroke.CAP_BUTT,
                BasicStroke.JOIN_MITER, 10.0f, dashPattern, 0.0f);

        g2d.setStroke(dashedStroke);
        g2d.setColor(Color.BLUE);

        ///nesto za selekciju, mozda nije dobro
        g.drawRect(selectionRectangleLT.x, selectionRectangleRB.y, selectionRectangleLT.width, selectionRectangleRB.height);

        for(ElementPainter p : painters) {
            p.paint(g2d);
            //System.out.println("Usao u paintere");
        }
    }

    public void addPainter(ElementPainter painter) {
        painters.add(painter);
        //System.out.println("Dodat painter"+painter.getClass().getName());
    }

    public Rectangle getSelectionRectangleLT() {
        return selectionRectangleLT;
    }

    public void setSelectionRectangleLT(Rectangle selectionRectangleLT) {
        this.selectionRectangleLT = selectionRectangleLT;
    }

    public Rectangle getSelectionRectangleRB() {
        return selectionRectangleRB;
    }

    public void setSelectionRectangleRB(Rectangle selectionRectangleRB) {
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


    @Override
    public void update(Notification notification) {
        repaint();
    }
}
