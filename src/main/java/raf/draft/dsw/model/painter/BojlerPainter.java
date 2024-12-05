package raf.draft.dsw.model.painter;

import raf.draft.dsw.model.structures.namestaj.Bojler;
import raf.draft.dsw.model.structures.namestaj.RoomElement;

import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.GeneralPath;

public class BojlerPainter implements ElementPainter{

    private Bojler bojler;

    @Override
    public void paint(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setColor(bojler.getColor());
        g2d.setStroke(bojler.getStroke());

        // Dimenzije kruga pre nego sto napisemo metodu za skaliranje
        int x = 10; // Gornja leva koordinata kruga (x)
        int y = 10; // Gornja leva koordinata kruga (y)
        int diameter = 15; // Prečnik kruga

        // Crtanje kruga
        g2d.drawOval(x, y, diameter, diameter);

        // Koordinate za crtanje X-a
        int centerX = x + diameter / 2; // Centar po x-osi
        int centerY = y + diameter / 2; // Centar po y-osi
        int radius = diameter / 2; // Poluprečnik

        // Krajnje tačke X-a unutar kruga
        int x1 = x; // Gornji levi ugao
        int y1 = y; // Gornji levi ugao
        int x2 = x + diameter; // Donji desni ugao
        int y2 = y + diameter; // Donji desni ugao

        int x3 = x + diameter; // Gornji desni ugao
        int y3 = y; // Gornji desni ugao
        int x4 = x; // Donji levi ugao
        int y4 = y + diameter; // Donji levi ugao

        // Crtanje prve linije dijagonale za X
        g2d.drawLine(x1, y1, x2, y2);

        // Crtanje druge linije dijagonale za X
        g2d.drawLine(x3, y3, x4, y4);
    }

    @Override
    public boolean elementAt(RoomElement element, Point point) {
        return false;
    }
}
