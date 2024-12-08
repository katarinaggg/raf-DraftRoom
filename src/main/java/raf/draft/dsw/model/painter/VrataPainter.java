package raf.draft.dsw.model.painter;

import raf.draft.dsw.gui.swing.RoomView;
import raf.draft.dsw.model.structures.namestaj.RoomElement;
import raf.draft.dsw.model.structures.namestaj.Vrata;

import java.awt.*;
import java.awt.geom.Arc2D;
import java.awt.geom.GeneralPath;

public class VrataPainter implements ElementPainter{

    private Vrata vrata;
    private GeneralPath path;
    private RoomView roomView;

    public VrataPainter(Vrata vrata,RoomView roomView) {
        this.vrata = vrata;
        this.roomView = roomView;
    }

    @Override
    public void paint(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setColor(vrata.getColor());
        g2d.setStroke(vrata.getStroke());

        double scalingFactor = roomView.getScalingFactor();
        Dimension scaledSize = vrata.getScaledDimensions(scalingFactor); // Skalirane dimenzije

        int startX = vrata.getLokacija().x;
        int startY = vrata.getLokacija().y;
        //int precnik = Math.max(vrata.getSize().width, vrata.getSize().height); // Prečnik kruga
        int precnik = Math.max(scaledSize.width, scaledSize.height); // Prečnik kruga (najveća dimenzija)


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
    public boolean elementAt(Point point, Rectangle rectangle) {
        return path!= null && (path.contains(point) || path.intersects(rectangle));
    }

    @Override
    public RoomElement getRoomElement() {
        return vrata;
    }
}
