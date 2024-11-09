package raf.draft.dsw.gui.swing.jtree.factory;

import raf.draft.dsw.model.nodes.DraftNode;
import raf.draft.dsw.model.structures.Room;

public class RoomFactory extends DraftNodeFactory{

    private static int counterRoom = 1;

    @Override
    public DraftNode createDraftNode(DraftNode parent) {
        return new Room("Room " + counterRoom++, parent);
    }
}
