package raf.draft.dsw.model.painter;

import raf.draft.dsw.gui.swing.RoomView;
import raf.draft.dsw.model.structures.namestaj.Ormar;
import raf.draft.dsw.model.structures.namestaj.RoomElement;

import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.GeneralPath;

public class OrmarPainter implements ElementPainter{

    private Ormar ormar;
    private GeneralPath path;
    private RoomView roomView;

    public OrmarPainter(Ormar ormar, RoomView roomView) {
        this.ormar = ormar;
        this.roomView = roomView;
    }

    @Override
    public void paint(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setColor(ormar.getColor());
        g2d.setStroke(ormar.getStroke());

        double scalingFactor = roomView.getScalingFactor();
        Dimension scaledSize = ormar.getScaledDimensions(scalingFactor);

        //Dimenzije pravougaonika
        int rectX = ormar.getLokacija().x; // X koordinata gornjeg levog ugla pravougaonika
        int rectY = ormar.getLokacija().y; // Y koordinata gornjeg levog ugla pravougaonika
//        int rectWidth = ormar.getSize().width; // Širina pravougaonika
//        int rectHeight = ormar.getSize().height; // Visina pravougaonika
        int rectWidth = scaledSize.width;
        int rectHeight = scaledSize.height;

        path = new GeneralPath();

        // Dodavanje pravougaonika u GeneralPath
        path.moveTo(rectX, rectY); // Gornji levi ugao
        path.lineTo(rectX + rectWidth, rectY); // Gornji desni ugao
        path.lineTo(rectX + rectWidth, rectY + rectHeight); // Donji desni ugao
        path.lineTo(rectX, rectY + rectHeight);
        path.closePath();

        g2d.fill(path);

        g2d.setColor(Color.black);

        // Dodavanje vertikalne linije u sredini pravougaonika
        int centerX = rectX + rectWidth / 2; // X koordinata sredine pravougaonika
        path.moveTo(centerX, rectY); // Početak vertikalne linije
        path.lineTo(centerX, rectY + rectHeight); // Kraj vertikalne linije

        // Dimenzije i udaljenost tačaka od vertikalne linije
        int dotOffset = 10; // Udaljenost tačaka od vertikalne linije
        int dotSize = 4; // Veličina tačaka
        int centerY = rectY + rectHeight / 2; // Y koordinata sredine pravougaonika

        // Dodavanje tačaka u GeneralPath
        path.append(new Ellipse2D.Double(centerX - dotOffset - dotSize / 2.0, centerY - dotSize / 2.0, dotSize, dotSize), false); // Leva tačka
        path.append(new Ellipse2D.Double(centerX + dotOffset - dotSize / 2.0, centerY - dotSize / 2.0, dotSize, dotSize), false); // Desna tačka

        // Iscrtavanje oblika
        g2d.draw(path);

    }

    @Override
    public boolean elementAt(Point point, Rectangle rectangle) {
        return path!= null && (path.contains(point) || path.intersects(rectangle));
    }

    @Override
    public RoomElement getRoomElement() {
        return ormar;
    }
}
