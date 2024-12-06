package raf.draft.dsw.model.painter;

import raf.draft.dsw.model.structures.namestaj.RoomElement;
import raf.draft.dsw.model.structures.namestaj.Sto;

import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.GeneralPath;

public class StoPainter implements ElementPainter{

    private Sto sto;
    private GeneralPath path;

    public StoPainter(Sto sto) {
        this.sto = sto;
    }

    @Override
    public void paint(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setColor(sto.getColor());
        g2d.setStroke(sto.getStroke());


        // Dimenzije kvadrata
        int squareX = sto.getLokacija().x; // X koordinata gornjeg levog ugla kvadrata
        int squareY = sto.getLokacija().y; // Y koordinata gornjeg levog ugla kvadrata
        int squareSize = sto.getSize().width; // Veličina kvadrata (dužina stranice)

        // Udaljenost tačaka od uglova (1/10 veličine kvadrata)
        int offset = squareSize / 10;

        int dotSize = 4;

        path = new GeneralPath();

        // Dodavanje kvadrata u GeneralPath
        path.moveTo(squareX, squareY); // Gornji levi ugao
        path.lineTo(squareX + squareSize, squareY); // Gornji desni ugao
        path.lineTo(squareX + squareSize, squareY + squareSize); // Donji desni ugao
        path.lineTo(squareX, squareY + squareSize); // Donji levi ugao
        path.closePath();

        g2d.fill(path);

        g2d.setColor(Color.black);

        // Dodavanje 4 tačke u GeneralPath
        // Gornji levi ugao
        path.append(new Ellipse2D.Double(
                squareX + offset - dotSize / 2.0, // X koordinata tačke
                squareY + offset - dotSize / 2.0, // Y koordinata tačke
                dotSize,                          // Širina tačke
                dotSize                           // Visina tačke
        ), false);

        // Gornji desni ugao
        path.append(new Ellipse2D.Double(
                squareX + squareSize - offset - dotSize / 2.0, // X koordinata tačke
                squareY + offset - dotSize / 2.0,             // Y koordinata tačke
                dotSize,                                      // Širina tačke
                dotSize                                       // Visina tačke
        ), false);

        // Donji levi ugao
        path.append(new Ellipse2D.Double(
                squareX + offset - dotSize / 2.0,              // X koordinata tačke
                squareY + squareSize - offset - dotSize / 2.0, // Y koordinata tačke
                dotSize,                                       // Širina tačke
                dotSize                                        // Visina tačke
        ), false);

        // Donji desni ugao
        path.append(new Ellipse2D.Double(
                squareX + squareSize - offset - dotSize / 2.0, // X koordinata tačke
                squareY + squareSize - offset - dotSize / 2.0, // Y koordinata tačke
                dotSize,                                       // Širina tačke
                dotSize                                        // Visina tačke
        ), false);

        g2d.draw(path);
    }

    @Override
    public boolean elementAt(RoomElement element, Point point) {
        return path != null && path.contains(point);
    }
}
