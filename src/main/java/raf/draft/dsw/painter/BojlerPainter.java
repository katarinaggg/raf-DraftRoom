package raf.draft.dsw.painter;

import raf.draft.dsw.gui.swing.RoomView;
import raf.draft.dsw.model.structures.namestaj.Bojler;
import raf.draft.dsw.model.structures.namestaj.RoomElement;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;
import java.awt.geom.GeneralPath;

public class BojlerPainter implements ElementPainter{
    private GeneralPath shape;

    private Bojler bojler;

    private RoomView roomView;

    private GeneralPath transformedPath;

    public BojlerPainter(Bojler bojler, RoomView roomView) {
        this.bojler = bojler;
        this.roomView = roomView;
    }

    @Override
    public void paint(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setColor(bojler.getColor());
        g2d.setStroke(bojler.getStroke());

        // cuvamo pocetno stanje da bi se kasnije vratili na njega
        AffineTransform start = g2d.getTransform();

        // faktor skaliranja sobe
        double scalingFactor = roomView.getScalingFactor();

        // skaliranje dimenzija
        Dimension scaledDimensions = bojler.getScaledDimensions(scalingFactor);

        int precnik = Math.min(scaledDimensions.width, scaledDimensions.height);
        int radius = precnik / 2;

        // uzimamo mesto klika misa za crtanje
        Point mouseClickLocation = bojler.getLokacija();
        int offsetX = mouseClickLocation.x;
        int offsetY = mouseClickLocation.y;

        // ugao rotacije na osnovu njegove vrednosti rotacije
        double ugao = Math.toRadians(bojler.getRotacija() * 90);

        // Kreiramo shape u koordinatama gornjeg levog ugla u odnosu na klik
        shape = new GeneralPath();

        Ellipse2D.Double krug = new Ellipse2D.Double(offsetX, offsetY, precnik, precnik);
        shape.append(krug, false);

        // Koordinate za X unutar kruga
        int x1 = offsetX + radius / 2; // Leva gornja tačka za prvu liniju
        int y1 = offsetY + radius / 2; // Gornja tačka za prvu liniju
        int x2 = offsetX + precnik - radius / 2; // Desna donja tačka za prvu liniju
        int y2 = offsetY + precnik - radius / 2; // Donja tačka za prvu liniju

        int x3 = offsetX + precnik - radius / 2; // Desna gornja tačka za drugu liniju
        int y3 = offsetY + radius / 2; // Gornja tačka za drugu liniju
        int x4 = offsetX + radius / 2; // Leva donja tačka za drugu liniju
        int y4 = offsetY + precnik - radius / 2; // Donja tačka za drugu liniju

        // Dodavanje linija za X u oblik
        shape.moveTo(x1, y1);
        shape.lineTo(x2, y2);
        shape.moveTo(x3, y3);
        shape.lineTo(x4, y4);

        // cuvamo transformisani shape
        AffineTransform transform = AffineTransform.getRotateInstance(ugao, offsetX + radius, offsetY + radius);
        transformedPath = (GeneralPath) shape.createTransformedShape(transform);

        g2d.fill(transformedPath);

        g2d.setColor(Color.black);
        g2d.draw(transformedPath);

        // vracamo na pocetno stanje
        g2d.setTransform(start);
    }


    @Override
    public boolean elementAt(Point point, Rectangle rectangle) {
        if(rectangle != null){
            return transformedPath.intersects(rectangle);
        }
        else if(point != null){
            return transformedPath.contains(point);
        }
        return false;
    }

    public GeneralPath getShape() {
        return transformedPath;
    }

    public void setShape(GeneralPath shape) {
        this.shape = shape;
    }

    public Bojler getBojler() {
        return bojler;
    }

    public void setBojler(Bojler bojler) {
        this.bojler = bojler;
    }


    @Override
    public RoomElement getRoomElement() {
        return bojler;
    }


}
