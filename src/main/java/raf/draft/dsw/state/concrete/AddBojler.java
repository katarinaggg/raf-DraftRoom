package raf.draft.dsw.state.concrete;

import com.sun.tools.javac.Main;
import raf.draft.dsw.gui.swing.DimensionFrame;
import raf.draft.dsw.gui.swing.MainFrame;
import raf.draft.dsw.gui.swing.RoomView;
import raf.draft.dsw.gui.swing.jtree.DraftTree;
import raf.draft.dsw.gui.swing.jtree.model.DraftTreeItem;
import raf.draft.dsw.model.painter.BojlerPainter;
import raf.draft.dsw.model.structures.namestaj.Bojler;
import raf.draft.dsw.state.State;

import java.awt.*;

public class AddBojler implements State {

    @Override
    public void misKliknut(RoomView roomView, Point point) {

        // kada kliknemo misem na to mesto (point treba nacrtati element)
        // otvara se prozor za unosenje dimenzija

        DimensionFrame frame = new DimensionFrame(roomView);
        frame.setVisible(true);

        Dimension dimenzija = frame.getDimenzija();

        // pravimo novi model
        Bojler bojler = new Bojler(roomView.getRoom(),point,dimenzija);
        BojlerPainter bojlerPainter = new BojlerPainter(bojler);
        // dodajemo painter na roomView
        roomView.addPainter(bojlerPainter);

        // treba dodati bojler u draftTree
        // na Room tipa DTI treba da se doda dete

        // moramo da pronadjemo odgovarajucu sobu u JTree-u
        DraftTreeItem soba = MainFrame.getInstance().getDraftTree().findTreeItem(roomView.getRoom());
        MainFrame.getInstance().getDraftTree().addRoomChild(soba,bojler);

        //System.out.println(roomView.getRoom().getNamestaj());

    }

    @Override
    public void misPomeren() {

    }

    @Override
    public void misScroll() {

    }
}
