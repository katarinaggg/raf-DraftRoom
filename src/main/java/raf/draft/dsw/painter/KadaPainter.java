package raf.draft.dsw.painter;

import raf.draft.dsw.gui.swing.RoomView;
import raf.draft.dsw.model.structures.namestaj.Kada;
import raf.draft.dsw.model.structures.namestaj.RoomElement;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;
import java.awt.geom.GeneralPath;

public class KadaPainter implements ElementPainter{

    private Kada kada;
    private GeneralPath path;
    private RoomView roomView;

    private GeneralPath transformedPath;

    public KadaPainter(Kada kada, RoomView roomView) {
        this.kada = kada;
        this.roomView = roomView;
        this.transformedPath = new GeneralPath();
    }

    @Override
    public void paint(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setColor(kada.getColor());
        g2d.setStroke(kada.getStroke());

//        // cuvamo pocetno stanje da bi se kasnije vratili na njega
//        AffineTransform start = g2d.getTransform();
//        // nad ovom radimo
//        AffineTransform af = g2d.getTransform();

        // Faktor skaliranja sobe
        double scalingFactor = roomView.getScalingFactor();
        // Skaliranje dimenzija pravougaonika
        Dimension scaledDimensions = kada.getScaledDimensions(scalingFactor);

        // Dimenzije pravougaonika
        int rectX = kada.getLokacija().x; // X koordinata gornjeg levog ugla pravougaonika
        int rectY = kada.getLokacija().y; // Y koordinata gornjeg levog ugla pravougaonika

        int rectWidth = scaledDimensions.width;
        int rectHeight = scaledDimensions.height;

        double centarX = rectX+rectWidth/2;
        double centarY = rectY+rectHeight/2;

//        // ugao rotacije na osnovu njegove vrednosti rotacije
//        double ugao = Math.toRadians(kada.getRotacija() * 90);
//
//        af.rotate(ugao, centarX, centarY);
//        g2d.setTransform(af);

        path = new GeneralPath();

        // Dodavanje pravougaonika u GeneralPath
        path.moveTo(rectX, rectY); // Gornji levi ugao
        path.lineTo(rectX + rectWidth, rectY); // Gornji desni ugao
        path.lineTo(rectX + rectWidth, rectY + rectHeight); // Donji desni ugao
        path.lineTo(rectX, rectY + rectHeight); // Donji levi ugao
        path.closePath(); // Zatvaranje pravougaonika


        // Dimenzije elipse
        int ellipseWidth = (int) (rectWidth * 0.7); // Elipsa je 70% širine pravougaonika
        int ellipseHeight = (int) (rectHeight * 0.7); // Elipsa je 70% visine pravougaonika

        // Pozicija elipse (centrirana)
        double ellipseX = rectX + (rectWidth - ellipseWidth) / 2.0; // Centriranje po x-osi
        double ellipseY = rectY + (rectHeight - ellipseHeight) / 2.0; // Centriranje po y-osi

        // Crtanje elipse
        path.append(new Ellipse2D.Double(ellipseX, ellipseY, ellipseWidth, ellipseHeight), false); //mozda i false

        //transformedPath = (GeneralPath) path.createTransformedShape(af);
        // Transformacija za rotaciju
        AffineTransform transform = AffineTransform.getRotateInstance(Math.toRadians(kada.getRotacija() * 90), centarX, centarY);

        // Kreiranje transformisanog path-a
        transformedPath = (GeneralPath) path.createTransformedShape(transform);

        g2d.fill(transformedPath);

        g2d.setColor(Color.black);
        g2d.draw(transformedPath);

        //g2d.setTransform(start);
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

    public Kada getKada() {
        return kada;
    }

    public void setKada(Kada kada) {
        this.kada = kada;
    }

    public GeneralPath getPath() {
        return path;
    }

    public void setPath(GeneralPath path) {
        this.path = path;
    }

    @Override
    public RoomElement getRoomElement() {
        return kada;
    }

    @Override
    public GeneralPath getShape() {
        return transformedPath;
    }


}
