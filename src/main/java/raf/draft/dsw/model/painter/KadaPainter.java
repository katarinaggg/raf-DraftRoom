package raf.draft.dsw.model.painter;

import raf.draft.dsw.model.structures.namestaj.Kada;
import raf.draft.dsw.model.structures.namestaj.RoomElement;

import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.GeneralPath;

public class KadaPainter implements ElementPainter{
    private Kada kada;
    private GeneralPath path;

    public KadaPainter(Kada kada) {
        this.kada = kada;
    }

    @Override
    public void paint(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setColor(kada.getColor());
        g2d.setStroke(kada.getStroke());

        // Dimenzije pravougaonika
        int rectX = kada.getLokacija().x; // X koordinata gornjeg levog ugla pravougaonika
        int rectY = kada.getLokacija().y; // Y koordinata gornjeg levog ugla pravougaonika
        int rectWidth = kada.getSize().width; // Širina pravougaonika
        int rectHeight = kada.getSize().height; // Visina pravougaonika

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

        g2d.fill(path);

        g2d.setColor(Color.black);
        g2d.draw(path);
    }

    @Override
    public boolean elementAt(RoomElement element, Point point) {
        return path != null && path.contains(point);
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
}
