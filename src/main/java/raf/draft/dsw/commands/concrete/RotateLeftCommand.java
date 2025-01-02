package raf.draft.dsw.commands.concrete;

import raf.draft.dsw.commands.AbstractCommand;
import raf.draft.dsw.gui.swing.RoomView;
import raf.draft.dsw.model.structures.Room;
import raf.draft.dsw.painter.ElementPainter;

import java.util.ArrayList;
import java.util.List;

public class RotateLeftCommand extends AbstractCommand {

    private RoomView roomView;

    // lista elemenata koji treba da se rotiraju
    private List<ElementPainter> selektovani;
    // lista prethodnih stanja rotacija
    private List<Integer> prethodnStanja;

    public RotateLeftCommand(RoomView roomView,List<ElementPainter> selektovani) {
        this.roomView = roomView;
        this.selektovani = new ArrayList<>(selektovani);
        this.prethodnStanja = new ArrayList<Integer>();

        // ubacujemo prvobitna stanja selektovanih elemenata
        for(ElementPainter e: this.selektovani){
            prethodnStanja.add(e.getRoomElement().getRotacija());
        }
    }

    @Override
    public void doCommand() {
        // rotacija za -1
        for (ElementPainter painter : selektovani) {
            painter.getRoomElement().setRotateRatio(painter.getRoomElement().getRotacija() - 1);
        }
        roomView.repaint();
    }

    @Override
    public void undoCommand() {
        // Vraćanje elemenata na prethodne rotacije
        for (int i = 0; i < selektovani.size(); i++) {
            selektovani.get(i).getRoomElement().setRotateRatio(prethodnStanja.get(i));
        }
        roomView.repaint();
    }
}
