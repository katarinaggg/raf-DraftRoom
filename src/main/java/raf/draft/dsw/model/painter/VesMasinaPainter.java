package raf.draft.dsw.model.painter;

import raf.draft.dsw.model.structures.namestaj.RoomElement;
import raf.draft.dsw.model.structures.namestaj.VesMasina;

import java.awt.*;

public class VesMasinaPainter implements ElementPainter{

    private VesMasina vesMasina;

    public VesMasinaPainter(VesMasina vesMasina) {
        this.vesMasina = vesMasina;
    }

    @Override
    public void paint(Graphics g) {

    }

    @Override
    public boolean elementAt(RoomElement element, Point point) {
        return false;
    }
}
