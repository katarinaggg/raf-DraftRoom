package raf.draft.dsw.model.painter;

import raf.draft.dsw.gui.swing.RoomView;
import raf.draft.dsw.model.structures.namestaj.Krevet;
import raf.draft.dsw.model.structures.namestaj.RoomElement;

import java.awt.*;
import java.awt.geom.GeneralPath;

public class KrevetPainter implements ElementPainter{

    private Krevet krevet;

    private GeneralPath path;

    private RoomView roomView;

    public KrevetPainter(Krevet krevet, RoomView roomView) {
        this.krevet = krevet;
        this.roomView = roomView;
    }

    @Override
    public void paint(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setColor(krevet.getColor());
        g2d.setStroke(krevet.getStroke());

        // Faktor skaliranja iz RoomView
        double scalingFactor = roomView.getScalingFactor();
        Dimension scaledDimensions = krevet.getScaledDimensions(scalingFactor);
        int bedWidth = scaledDimensions.width;
        int bedHeight = scaledDimensions.height;

        // Dimenzije kreveta
//        int bedWidth = krevet.getSize().width; // Širina kreveta (povećana za dužinu)
//        int bedHeight = krevet.getSize().height; // Visina kreveta

        // Dimenzije jastuka
        int pillowWidth = bedWidth / 2; // Jastuk zauzima 1/2 širine kreveta
        int pillowHeight = bedHeight / 7; // Visina jastuka je 1/7 visine kreveta
        int pillowMargin = 10; // Udaljenost jastuka od gornje ivice kreveta
        //int pillowMargin = (int) (10 * scalingFactor);

        path = new GeneralPath();

        // Crtanje kreveta (veliki pravougaonik)
        int bedX = krevet.getLokacija().x; // X koordinata gornjeg levog ugla kreveta
        int bedY = krevet.getLokacija().y; // Y koordinata gornjeg levog ugla kreveta
        path.moveTo(bedX, bedY); // Gornji levi ugao kreveta
        path.lineTo(bedX + bedWidth, bedY); // Gornji desni ugao kreveta
        path.lineTo(bedX + bedWidth, bedY + bedHeight); // Donji desni ugao kreveta
        path.lineTo(bedX, bedY + bedHeight); // Donji levi ugao kreveta
        path.closePath();

        //g2d.fill(path);

        // Crtanje jastuka (manji pravougaonik unutar kreveta)
        double pillowXStart = bedX + (bedWidth - pillowWidth) / 2.0; // Centriranje jastuka po širini
        double pillowYStart = bedY + pillowMargin; // Pomicanje jastuka od gornje ivice kreveta

        path.moveTo(pillowXStart, pillowYStart); // Gornji levi ugao jastuka
        path.lineTo(pillowXStart + pillowWidth, pillowYStart); // Gornji desni ugao jastuka
        path.lineTo(pillowXStart + pillowWidth, pillowYStart + pillowHeight); // Donji desni ugao jastuka
        path.lineTo(pillowXStart, pillowYStart + pillowHeight); // Donji levi ugao jastuka
        path.closePath();

        g2d.fill(path);

        g2d.setColor(Color.black);
        g2d.draw(path);
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

    public Krevet getKrevet() {
        return krevet;
    }

    public void setKrevet(Krevet krevet) {
        this.krevet = krevet;
    }

    public GeneralPath getPath() {
        return path;
    }

    public void setPath(GeneralPath path) {
        this.path = path;
    }

    @Override
    public RoomElement getRoomElement() {
        return krevet;
    }

    @Override
    public GeneralPath getShape() {
        return path;
    }
}
