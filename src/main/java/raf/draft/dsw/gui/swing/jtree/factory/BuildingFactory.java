package raf.draft.dsw.gui.swing.jtree.factory;

import raf.draft.dsw.model.nodes.DraftNode;
import raf.draft.dsw.model.structures.Building;

public class BuildingFactory extends DraftNodeFactory{

    private static int counterBuilding = 1;
    @Override
    public DraftNode createDraftNode(DraftNode parent) {
        return new Building("Building " + counterBuilding++, parent);
    }

}
