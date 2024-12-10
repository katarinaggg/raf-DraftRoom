package raf.draft.dsw.model.painter;

import raf.draft.dsw.gui.swing.RoomView;
import raf.draft.dsw.model.structures.namestaj.RoomElement;
import raf.draft.dsw.model.structures.namestaj.Sto;

import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.GeneralPath;
import java.awt.geom.Rectangle2D;

public class StoPainter implements ElementPainter{

    private Sto sto;
    private GeneralPath path;
    private RoomView roomView;

    public StoPainter(Sto sto,RoomView roomView) {
        this.sto = sto;
        this.roomView = roomView;
    }

    @Override
    public void paint(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;

        // Podešavanje boje i debljine linije za pravougaonik
        g2d.setColor(sto.getColor());
        g2d.setStroke(sto.getStroke());

        // Računanje skaliranih dimenzija
        double scalingFactor = roomView.getScalingFactor();
        Dimension scaledSize = sto.getScaledDimensions(scalingFactor);

        // Dimenzije pravougaonika
        int rectX = sto.getLokacija().x; // X koordinata gornjeg levog ugla pravougaonika
        int rectY = sto.getLokacija().y; // Y koordinata gornjeg levog ugla pravougaonika
        int rectWidth = scaledSize.width; // Širina pravougaonika
        int rectHeight = scaledSize.height; // Visina pravougaonika

//         //Dodavanje tačaka
//        int offset = Math.min(rectWidth, rectHeight) / 10;
//
//        // Kreiranje GeneralPath objekta za pravougaonik
//        path = new GeneralPath();
//        Rectangle2D.Double rectangle = new Rectangle2D.Double(rectX, rectY, rectWidth, rectHeight);
//        path.append(rectangle, false);
//
//        // Popunjavanje pravougaonika
//        g2d.fill(path);
//
//        // Postavljanje boje za tačke
//        g2d.setColor(Color.BLACK);
//
//        // Veličina tačaka
//        int dotSize = 4;
//
//        // Dodavanje tačaka u uglove pravougaonika
//        // Gornji levi ugao
//        path.append(new Ellipse2D.Double(
//                rectX + offset - dotSize / 2.0, // X koordinata tačke
//                rectY + offset - dotSize / 2.0, // Y koordinata tačke
//                dotSize,                         // Širina tačke
//                dotSize                          // Visina tačke
//        ), false);
//
//        // Gornji desni ugao
//        path.append(new Ellipse2D.Double(
//                rectX + rectWidth - offset - dotSize / 2.0, // X koordinata tačke
//                rectY + offset - dotSize / 2.0,             // Y koordinata tačke
//                dotSize,                                     // Širina tačke
//                dotSize                                      // Visina tačke
//        ), false);
//
//        // Donji levi ugao
//        path.append(new Ellipse2D.Double(
//                rectX + offset - dotSize / 2.0,              // X koordinata tačke
//                rectY + rectHeight - offset - dotSize / 2.0, // Y koordinata tačke
//                dotSize,                                      // Širina tačke
//                dotSize                                       // Visina tačke
//        ), false);
//
//        // Donji desni ugao
//        path.append(new Ellipse2D.Double(
//                rectX + rectWidth - offset - dotSize / 2.0,  // X koordinata tačke
//                rectY + rectHeight - offset - dotSize / 2.0, // Y koordinata tačke
//                dotSize,                                      // Širina tačke
//                dotSize                                       // Visina tačke
//        ), false);
//
//        g2d.draw(path);


        path = new GeneralPath();
        // Dodavanje pravougaonika
        path.append(new Rectangle2D.Double(rectX, rectY, rectWidth, rectHeight), false);

        g2d.fill(path);

        g2d.setColor(Color.black);

        // Dodavanje tačaka
        int offset = Math.min(rectWidth, rectHeight) / 10;
        int dotSize = 4;

        path.append(new Ellipse2D.Double(rectX + offset - dotSize / 2.0, rectY + offset - dotSize / 2.0, dotSize, dotSize), false);
        path.append(new Ellipse2D.Double(rectX + rectWidth - offset - dotSize / 2.0, rectY + offset - dotSize / 2.0, dotSize, dotSize), false);
        path.append(new Ellipse2D.Double(rectX + offset - dotSize / 2.0, rectY + rectHeight - offset - dotSize / 2.0, dotSize, dotSize), false);
        path.append(new Ellipse2D.Double(rectX + rectWidth - offset - dotSize / 2.0, rectY + rectHeight - offset - dotSize / 2.0, dotSize, dotSize), false);

        g2d.draw(path);
    }


    public boolean elementAt(Point point, Rectangle selectionRectangle) {
        if (selectionRectangle != null) {
            return path.intersects(selectionRectangle);
        } else if (point != null) {
            return path.contains(point);
        }
        return false;
    }

    @Override
    public RoomElement getRoomElement() {
        return sto;
    }

    @Override
    public GeneralPath getShape() {
        return path;
    }
}
