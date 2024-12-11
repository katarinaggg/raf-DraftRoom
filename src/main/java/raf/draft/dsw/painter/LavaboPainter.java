package raf.draft.dsw.painter;

import raf.draft.dsw.gui.swing.RoomView;
import raf.draft.dsw.model.structures.namestaj.Lavabo;
import raf.draft.dsw.model.structures.namestaj.RoomElement;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;
import java.awt.geom.GeneralPath;

public class LavaboPainter implements ElementPainter{

    private Lavabo lavabo;
    private GeneralPath path;
    private RoomView roomView;

    public LavaboPainter(Lavabo lavabo, RoomView roomView) {
        this.lavabo = lavabo;
        this.roomView = roomView;
    }

    @Override
    public void paint(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setColor(lavabo.getColor());
        g2d.setStroke(lavabo.getStroke());

        // cuvamo pocetno stanje da bi se kasnije vratili na njega
        AffineTransform start = g2d.getTransform();
        // nad ovom radimo
        AffineTransform af = g2d.getTransform();

        double scalingFactor = roomView.getScalingFactor();
        // Skalirane dimenzije i pozicija
        Dimension scaledSize = lavabo.getScaledDimensions(scalingFactor);

        int width = scaledSize.width;
        int height = scaledSize.height;

        // Dimenzije i pozicija obrnutog trougla
        int baseX = lavabo.getLokacija().x; // X koordinata početne tačke baze trougla
        int baseY = lavabo.getLokacija().y; // Y koordinata baze trougla
//        int width = lavabo.getSize().width; // Širina baze trougla
//        int height = lavabo.getSize().height; // Visina trougla

        double centarX = baseX+width/2;
        double centarY = baseY+height/2;

        // ugao rotacije na osnovu njegove vrednosti rotacije
        double ugao = Math.toRadians(lavabo.getRotacija() * 90);

        af.rotate(ugao, centarX, centarY);
        g2d.setTransform(af);

        // Koordinate vrhova trougla
        int[] xPoints = {
                baseX,                    // Leva tačka na osnovi
                baseX + width,            // Desna tačka na osnovi
                baseX + width / 2         // Vrh trougla (sredina baze)
        };
        int[] yPoints = {
                baseY,                    // Leva tačka na osnovi (Y)
                baseY,                    // Desna tačka na osnovi (Y)
                baseY + height            // Donji vrh trougla
        };

        // Kreiranje GeneralPath objekta za trougao
        path = new GeneralPath();
        path.moveTo(xPoints[0], yPoints[0]); // Leva tačka na osnovi
        path.lineTo(xPoints[1], yPoints[1]); // Desna tačka na osnovi
        path.lineTo(xPoints[2], yPoints[2]); // Vrh trougla
        path.closePath();

        // Iscrtavanje obrnutog trougla
        g2d.fill(path);

        // Izračunavanje koordinata centra (težišta) trougla
        int centerX = (xPoints[0] + xPoints[1] + xPoints[2]) / 3; // Srednja vrednost X koordinata
        int centerY = (yPoints[0] + yPoints[1] + yPoints[2]) / 3; // Srednja vrednost Y koordinata

        // Dodavanje tačke u centar trougla
        int dotSize = 6; // Prečnik tačke
        Ellipse2D.Double centerDot = new Ellipse2D.Double(centerX - dotSize / 2.0, centerY - dotSize / 2.0, dotSize, dotSize);
        path.append(centerDot, false);

        // Ponovno iscrtavanje celog oblika (trougao i tačka)
        g2d.setColor(Color.black);
        g2d.draw(path);

        g2d.setTransform(start);
    }

    @Override
    public boolean elementAt(Point point, Rectangle rectangle) {
        //return path!= null && (path.contains(point) || path.intersects(rectangle));
        if(rectangle != null){
            return path.intersects(rectangle);
        }
        else if(point != null){
            return path.contains(point);
        }
        return false;
    }

    @Override
    public RoomElement getRoomElement() {
        return lavabo;
    }

    @Override
    public GeneralPath getShape() {
        return path;
    }
}
