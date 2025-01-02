package raf.draft.dsw.commands.concrete;

import raf.draft.dsw.commands.AbstractCommand;
import raf.draft.dsw.gui.swing.RoomView;
import raf.draft.dsw.model.structures.namestaj.RoomElement;

import java.awt.*;

public class ResizeCommand extends AbstractCommand {

    private RoomView roomView;
    private Dimension staraDimenzija;
    private Dimension novaDimenzija;
    private RoomElement roomElement;

    public ResizeCommand(RoomView roomView, Dimension staraDimenzija, Dimension novaDimenzija, RoomElement roomElement) {
        this.roomView = roomView;
        this.staraDimenzija = staraDimenzija;
        this.novaDimenzija = novaDimenzija;
        this.roomElement = roomElement;
    }

    @Override
    public void doCommand() {
        roomElement.setSize(novaDimenzija);
        roomView.repaint();
    }

    @Override
    public void undoCommand() {
        roomElement.setSize(staraDimenzija);
        roomView.repaint();
    }
}
