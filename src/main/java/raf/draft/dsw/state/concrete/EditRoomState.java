package raf.draft.dsw.state.concrete;

import lombok.NoArgsConstructor;
import raf.draft.dsw.gui.swing.DimensionFrame;
import raf.draft.dsw.gui.swing.RoomView;
import raf.draft.dsw.painter.RoomPainter;
import raf.draft.dsw.state.State;

import java.awt.*;
import java.awt.event.MouseWheelEvent;

@NoArgsConstructor
public class EditRoomState implements State {


    // Klikom na prazan RoomView, iskače prozorčić da se unesu dimenzije sobe
    @Override
    public void misKliknut(RoomView roomView, Point point) {
        DimensionFrame frame = new DimensionFrame(roomView);
        frame.setVisible(true);

        // ne pravimo novi objekat soba vec je to ona ciji je tab otvoren i samo treba da
        // joj setujemo duzinu i sirinu koju smo dobili s prozora??

        Dimension dimenzija = frame.getDimenzija();
        Double duzina;
        Double sirina;
        if(dimenzija!=null) {
            duzina = dimenzija.getHeight();
            sirina = dimenzija.getWidth();
            // ovde valjda nije krsenje MVC-a
            roomView.getRoom().setDuzina(duzina);
            roomView.getRoom().setSirina(sirina);

            RoomPainter roomPainter = new RoomPainter(roomView.getRoom(),roomView);
            roomView.addPainter(roomPainter);
            roomView.repaint();


            // nacrtali smo sobu sad mozemo sve ostalo da radimo
            roomView.setNacrtanaSoba(true);
        }

    }

    @Override
    public void misPomeren() {

    }

    @Override
    public void misScroll(RoomView roomView, MouseWheelEvent e) {

    }

    @Override
    public void misZadrzan(RoomView roomView, Point point) {

    }

    @Override
    public void misPovucen(RoomView roomView, Point point) {

    }

    @Override
    public void misPusten(RoomView roomView, Point point) {

    }

}
