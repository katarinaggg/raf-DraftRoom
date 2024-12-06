package raf.draft.dsw.model.painter;

import raf.draft.dsw.model.structures.namestaj.RoomElement;
import raf.draft.dsw.model.structures.namestaj.Vrata;

import java.awt.*;
import java.awt.geom.Arc2D;
import java.awt.geom.GeneralPath;

public class VrataPainter implements ElementPainter{

    private Vrata vrata;
    private GeneralPath path;

    public VrataPainter(Vrata vrata) {
        this.vrata = vrata;
    }

    @Override
    public void paint(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setColor(vrata.getColor());
        g2d.setStroke(vrata.getStroke());

        int startX = vrata.getLokacija().x;
        int startY = vrata.getLokacija().y;
        int precnik = Math.max(vrata.getSize().width, vrata.getSize().height); // Prečnik kruga

        path = new GeneralPath();

        // Dodavanje luka (gornja polovina kružnice) u GeneralPath
        Arc2D.Double arc = new Arc2D.Double(
                startX, startY,            // Gornji levi ugao pravougaonika koji sadrži luk
                precnik, precnik,        // Širina i visina pravougaonika
                90, 90,                    // Početni ugao i ugao luka (90° do 90°)
                Arc2D.OPEN                 // Otvoreni luk
        );

        path.append(arc, false); // Dodavanje luka u GeneralPath

        // Dodavanje vertikalne linije u GeneralPath
        int verticalLineX = startX + precnik / 2; // X koordinata vertikalne linije
        int verticalLineStartY = startY;           // Početna Y koordinata linije
        int verticalLineEndY = startY + precnik / 2; // Krajnja Y koordinata linije

        path.moveTo(verticalLineX, verticalLineStartY); // Početak vertikalne linije
        path.lineTo(verticalLineX, verticalLineEndY);   // Kraj vertikalne linije

        g2d.draw(path);

    }

    @Override
    public boolean elementAt(RoomElement element, Point point) {
        return path != null && path.contains(point);
    }
}
