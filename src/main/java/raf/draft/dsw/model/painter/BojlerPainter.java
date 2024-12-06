package raf.draft.dsw.model.painter;

import raf.draft.dsw.model.structures.namestaj.Bojler;
import raf.draft.dsw.model.structures.namestaj.RoomElement;

import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.GeneralPath;

public class BojlerPainter implements ElementPainter{
    private GeneralPath shape;

    private Bojler bojler;

    public BojlerPainter(Bojler bojler) {
        this.bojler = bojler;
    }

    @Override
    public void paint(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setColor(bojler.getColor());
        g2d.setStroke(bojler.getStroke());

        // Dimenzije kruga pre metode za skaliranje
        int centarX = bojler.getLokacija().x; // X koordinata centra kruga
        int centarY = bojler.getLokacija().y; // Y koordinata centra kruga
        int precnik = Math.min(bojler.getSize().width, bojler.getSize().height); // Prečnik kruga
        int radius = precnik / 2; // Poluprečnik kruga

        shape = new GeneralPath();

        Ellipse2D.Double krug = new Ellipse2D.Double(centarX - radius, centarY-radius, precnik, precnik);

        shape.append(krug,false);


        //Koordinate za X unutar kruga
        int x1 = centarX - radius / 2; // Leva gornja tačka za prvu liniju
        int y1 = centarY - radius / 2; // Gornja tačka za prvu liniju
        int x2 = centarX + radius / 2; // Desna donja tačka za prvu liniju
        int y2 = centarY + radius / 2; // Donja tačka za prvu liniju

        int x3 = centarX + radius / 2; // Desna gornja tačka za drugu liniju
        int y3 = centarY - radius / 2; // Gornja tačka za drugu liniju
        int x4 = centarX - radius / 2; // Leva donja tačka za drugu liniju
        int y4 = centarY + radius / 2; // Donja tačka za drugu liniju

//        // Crtanje prve dijagonale (leva gornja ka desnoj donjoj tački)
//        g2d.drawLine(x1, y1, x2, y2);
//
//        // Crtanje druge dijagonale (desna gornja ka levoj donjoj tački)
//        g2d.drawLine(x3, y3, x4, y4);

        // Dodavanje linija za X u oblik
        shape.moveTo(x1, y1);
        shape.lineTo(x2, y2);
        shape.moveTo(x3, y3);
        shape.lineTo(x4, y4);

        g2d.fill(shape);

        g2d.setColor(Color.black);
        g2d.draw(shape);

    }

    @Override
    public boolean elementAt(RoomElement element, Point point) {
        return shape != null && shape.contains(point);
    }

    public GeneralPath getShape() {
        return shape;
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
}
