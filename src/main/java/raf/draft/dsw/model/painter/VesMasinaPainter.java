package raf.draft.dsw.model.painter;

import raf.draft.dsw.gui.swing.RoomView;
import raf.draft.dsw.model.structures.namestaj.RoomElement;
import raf.draft.dsw.model.structures.namestaj.VesMasina;

import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.GeneralPath;

public class VesMasinaPainter implements ElementPainter{

    private VesMasina vesMasina;
    private GeneralPath path;
    private RoomView roomView;

    public VesMasinaPainter(VesMasina vesMasina, RoomView roomView) {
        this.vesMasina = vesMasina;
        this.roomView = roomView;
    }

    @Override
    public void paint(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setColor(vesMasina.getColor());
        g2d.setStroke(vesMasina.getStroke());

        double scalingFactor = roomView.getScalingFactor();
        Dimension scaledSize = vesMasina.getScaledDimensions(scalingFactor); // Skalirane dimenzije


        // Dimenzije kvadrata
        int squareX = vesMasina.getLokacija().x; // X koordinata gornjeg levog ugla kvadrata
        int squareY = vesMasina.getLokacija().y; // Y koordinata gornjeg levog ugla kvadrata
        //int squareSize = vesMasina.getSize().width; // Dužina stranice kvadrata
        int squareSize = scaledSize.width;

        // Crtanje kvadrata
        path = new GeneralPath();
        path.moveTo(squareX, squareY); // Gornji levi ugao
        path.lineTo(squareX + squareSize, squareY); // Gornji desni ugao
        path.lineTo(squareX + squareSize, squareY + squareSize); // Donji desni ugao
        path.lineTo(squareX, squareY + squareSize); // Donji levi ugao
        path.closePath();

        g2d.fill(path);

        g2d.setColor(Color.black);

        // Dimenzije kružnice
        int circleDiameter = (int) (squareSize * 0.7); // Kružnica je 70% veličine kvadrata

        // Pozicija kružnice (centriranje unutar kvadrata)
        double circleX = squareX + (squareSize - circleDiameter) / 2.0; // Centriranje po x-osi
        double circleY = squareY + (squareSize - circleDiameter) / 2.0; // Centriranje po y-osi

        // Crtanje kružnice
        path.append(new Ellipse2D.Double(circleX, circleY, circleDiameter, circleDiameter), false);

        // Iscrtavanje oblika
        g2d.draw(path);

    }

    @Override
    public boolean elementAt(Point point, Rectangle rectangle) {
        return path!= null && (path.contains(point) || path.intersects(rectangle));
    }

    @Override
    public RoomElement getRoomElement() {
        return vesMasina;
    }
}
