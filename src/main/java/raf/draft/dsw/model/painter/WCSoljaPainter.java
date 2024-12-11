package raf.draft.dsw.model.painter;

import raf.draft.dsw.gui.swing.RoomView;
import raf.draft.dsw.model.structures.Project;
import raf.draft.dsw.model.structures.namestaj.RoomElement;
import raf.draft.dsw.model.structures.namestaj.WCSolja;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Arc2D;
import java.awt.geom.GeneralPath;

public class WCSoljaPainter implements ElementPainter{

    private WCSolja wcSolja;
    private GeneralPath path;
    private RoomView roomView;

    public WCSoljaPainter(WCSolja wcSolja, RoomView roomView) {
        this.wcSolja = wcSolja;
        this.roomView = roomView;
    }

    @Override
    public void paint(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setColor(wcSolja.getColor());
        g2d.setStroke(wcSolja.getStroke());

        // cuvamo pocetno stanje da bi se kasnije vratili na njega
        AffineTransform start = g2d.getTransform();
        // nad ovom radimo
        AffineTransform af = g2d.getTransform();

        double scalingFactor = roomView.getScalingFactor();
        Dimension scaledSize = wcSolja.getScaledDimensions(scalingFactor); // Skalirane dimenzije


        // Dimenzije spoljnog oblika
        int startX = wcSolja.getLokacija().x; // Gornja leva X koordinata pravougaonika u kojem je luk
        int startY = wcSolja.getLokacija().y; // Gornja leva Y koordinata pravougaonika u kojem je luk
//        int width = wcSolja.getSize().width;  // Širina luka (horizontalni prečnik)
//        int height = wcSolja.getSize().height; // Visina luka (vertikalni prečnik)
        int width = scaledSize.width;
        int height = scaledSize.height;

        double centarX = startX+width/2;
        double centarY = startY+height/2;

        // ugao rotacije na osnovu njegove vrednosti rotacije
        double ugao = Math.toRadians(wcSolja.getRotacija() * 90);

        af.rotate(ugao, centarX, centarY);
        g2d.setTransform(af);

        path = new GeneralPath();

        // Crtanje spoljnog luka
        Arc2D.Double outerArc = new Arc2D.Double(
                startX, startY,        // Gornji levi ugao pravougaonika
                width, height,         // Širina i visina pravougaonika
                0, -180,               // Početni ugao (0°) i ugao luka (-180° za južnu krivinu)
                Arc2D.CHORD            // Otvoreni luk
        );
        path.append(outerArc, false);

        g2d.fill(path);

        g2d.setColor(Color.black);

        // Dimenzije unutrašnjeg oblika (manji luk)
        int innerWidth = width / 2;    // Širina manjeg luka (polovina širine spoljnog luka)
        int innerHeight = height / 3;  // Visina manjeg luka (trecina visine spoljnog luka)

        // Centriranje unutrašnjeg luka unutar spoljnog luka
        int innerStartX = startX + (width - innerWidth) / 2; // Centriranje po X osi
        int innerStartY = startY + (height - innerHeight) / 2; // Centriranje po Y osi

        // Dodatan pomak naniže za unutrašnji luk
        innerStartY += innerHeight / 2; // Dodatno pomicanje unutrašnjeg luka naniže

        // Crtanje manjeg luka
        Arc2D.Double innerArc = new Arc2D.Double(
                innerStartX, innerStartY,  // Gornji levi ugao unutrašnjeg pravougaonika
                innerWidth, innerHeight,   // Širina i visina unutrašnjeg pravougaonika
                0, -180,                   // Početni ugao (0°) i ugao luka (-180° za južnu krivinu)
                Arc2D.CHORD                // Otvoreni luk
        );
        path.append(innerArc, false);

        // Crtanje horizontalne linije iznad unutrašnjeg luka, ali unutar spoljnog
        int lineY = innerStartY + 7; // Pozicija linije neposredno iznad unutrašnjeg luka
        int lineStartX = startX; // Početak linije od leve ivice spoljnog luka
        int lineEndX = startX + width; // Kraj linije do desne ivice spoljnog luka

        g2d.drawLine(lineStartX, lineY, lineEndX, lineY);
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
        return wcSolja;
    }

    @Override
    public GeneralPath getShape() {
        return path;
    }
}
