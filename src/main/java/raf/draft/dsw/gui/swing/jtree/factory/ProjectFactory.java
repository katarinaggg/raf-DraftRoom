package raf.draft.dsw.gui.swing.jtree.factory;

import raf.draft.dsw.model.nodes.DraftNode;
import raf.draft.dsw.model.structures.Project;

public class ProjectFactory extends DraftNodeFactory{

    private static int counterProject = 1;

    @Override
    public DraftNode createDraftNode(DraftNode parent) {
        return new Project("Project " + counterProject++, parent);
    }
}
