package raf.draft.dsw.commands.concrete;

import raf.draft.dsw.commands.AbstractCommand;
import raf.draft.dsw.gui.swing.RoomView;
import raf.draft.dsw.model.structures.namestaj.RoomElement;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class MoveCommand extends AbstractCommand {

    private RoomView roomView;
    private Map<RoomElement, Point> initialLocations;
    private Map<RoomElement, Point> finalLocations;

    public MoveCommand(RoomView roomView, Map<RoomElement, Point> initialLocations, Map<RoomElement, Point> finalLocations) {
        this.roomView = roomView;
        this.initialLocations = new HashMap<>(initialLocations);
        this.finalLocations = new HashMap<>(finalLocations);
    }


    @Override
    public void doCommand() {
        // postavljamo elemente na krajnje lokacije
        for (Map.Entry<RoomElement, Point> entry : finalLocations.entrySet()) {
            RoomElement element = entry.getKey();
            Point newLocation = entry.getValue();
            element.setLokacija(newLocation);
        }
        roomView.repaint();
    }

    @Override
    public void undoCommand() {
        // vracamo na pocetne lokacije
        for (Map.Entry<RoomElement, Point> entry : initialLocations.entrySet()) {
            RoomElement element = entry.getKey();
            Point initialLocation = entry.getValue();
            element.setLokacija(initialLocation);
        }

        // Osvežavanje prikaza
        roomView.repaint();
    }
}
